package threads;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.WebResource;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import modules.DeliveryStatistics;
import modules.Drone;
import modules.Position;
import proto.ManagerGrpc;
import proto.Welcome;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DeliveryThread extends Thread{
    private static final String RESTServerAddress = "http://localhost:1337";
    Drone drone;
    Position retire;
    Position delivery;
    int orderId;

    public DeliveryThread(Drone drone, Position retire, Position delivery, int orderId){
        this.drone = drone;
        this.retire = retire;
        this.delivery = delivery;
        this.orderId = orderId;
    }

    public void run(){
        System.out.println("> Doing the delivery of the order "+orderId);

        drone.setInDelivery(true);

        try {
            Thread.sleep(5000);

            //Timestamp of arrival to delivery place
            Timestamp arriveTime = new Timestamp(System.currentTimeMillis());

            //New battery level calculated after the delivery
            int newBatteryLevel = drone.getBatteryLevel()-10;
            drone.setBatteryLevel(newBatteryLevel);

            drone.incrementDelivery();

            //Total distance traveled
            double distanceFromRetire = Math.sqrt(
                    Math.pow(drone.getPosition().getX()-retire.getX(),2) +
                    Math.pow(drone.getPosition().getY()-retire.getY(),2)
            );
            double distanceDelivery = Math.sqrt(
                    Math.pow(delivery.getX()-retire.getX(),2) +
                    Math.pow(delivery.getY()-retire.getY(),2)
            );
            double totalKm = distanceFromRetire + distanceDelivery;

            drone.addKms(totalKm);

            /*System.out.println("> Order delivered with success" +
                    "\n\t Battery: "+drone.getBatteryLevel()+
                    "\n\t Total KM: "+totalKm);*/

            //AVG of pollution measured
            List<Double> measures = drone.getAveragesPM10();
            double sum = 0.0d;
            for (double value : measures){
                sum+=value;
            }
            double avg = sum/measures.size();
            drone.emptyAveragesAfterDelivery();

            //send to master delivery data
            System.out.println("> Delivered order "+orderId);
            if(drone.isMaster() || drone.getMasterID()==drone.getId()){
                DeliveryStatistics ds = new DeliveryStatistics(drone.getId(), totalKm, avg, newBatteryLevel);
                drone.addDeliveryStatistic(ds);
                drone.removeOrderInDeliveryById(orderId);
            } else {
                sendDeliveryDataToMaster(arriveTime, delivery, totalKm, avg, newBatteryLevel, orderId);
            }

            if(newBatteryLevel < 15){

                if(drone.isMaster()){
                    drone.stopSubscriberMQTT();
                    if(drone.ordersQueue.size()>0) {
                        System.out.println("> Assigning pending orders...");
                        for (Drone d : drone.getDronesList()) {
                            if(drone.getId() != d.getId()) {
                                AssignPendingDeliveries apd = new AssignPendingDeliveries(drone, d.getIp());
                                apd.start();
                            }
                        }
                    }
                }

                Client client = Client.create();
                System.out.println("> Battery is under 15%. Removing the drone from the network...");
                //Remove a drone to the REST Server
                String deletePath = "/drones/" + drone.getId();
                WebResource webResource = client.resource(RESTServerAddress + deletePath);
                try {
                    webResource.type("application/json").delete();
                } catch (ClientHandlerException e) {
                    System.out.println("> Server not reachable");
                }
                System.out.println("> Drone removed...");
                System.exit(0);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        drone.setInDelivery(false);

    }

    public void sendDeliveryDataToMaster(Timestamp timestamp, Position newPosition, double km, double avgPollution, int batteryLevel, int orderId){
        //Send new master my position
        final ManagedChannel channelToMaster = ManagedChannelBuilder.forTarget("127.0.0.1:" + (3000 + drone.getMasterID())).usePlaintext(true).build();

        ManagerGrpc.ManagerStub newStub = ManagerGrpc.newStub(channelToMaster);
        Welcome.DeliveredMessage deliveredRequest = Welcome.DeliveredMessage
                .newBuilder()
                .setId(drone.getId())
                .setBattery(batteryLevel)
                .setKm(km)
                .setAvgPollution(avgPollution)
                .setTimestamp(timestamp.toString())
                .setNewPosition(
                    Welcome.Position.newBuilder()
                            .setY(newPosition.getY())
                            .setX(newPosition.getX())
                )
                .setOrderId(orderId)
                .build();

        newStub.delivered(deliveredRequest, new StreamObserver<Welcome.DeliveredResponse>() {
            public void onNext(Welcome.DeliveredResponse deliveredResponse) {}

            public void onError(Throwable throwable) {
                channelToMaster.shutdownNow();
                stop();
                System.err.println("> Master is not responding -> " + throwable.getMessage());
                if(!drone.isInElection()) {
                    ElectionThread electionThread = new ElectionThread(drone,drone.getId(),drone.getBatteryLevel());
                    electionThread.start();
                }
            }

            public void onCompleted() {
                channelToMaster.shutdownNow();
            }
        });
        try {
            channelToMaster.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
