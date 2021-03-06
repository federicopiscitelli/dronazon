package threads;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import modules.Drone;
import modules.Order;
import modules.OrdersQueue;
import modules.Position;
import proto.ManagerGrpc;
import proto.Welcome;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AssignDelivery extends Thread{
    private Drone drone;
    private Order order;
    private boolean fromQueue;

    public AssignDelivery(Drone drone, Order order,boolean fromQueue){
        this.drone = drone;
        this.order = order;
        this.fromQueue = fromQueue;
    }

    public AssignDelivery(Drone drone){
        this(drone,drone.ordersQueue.getOrder(),false);
    }

    public void run(){
            boolean assigned = false;
            Drone selected = this.drone;
            List<Drone> availableDrones = new ArrayList<>();

            if(fromQueue){
                this.order = this.drone.ordersQueue.getOrder();
                System.out.println("> Retrieving orders from queue ..");
            }

            if(this.order != null) {
                //System.out.println(">From queue "+fromQueue+". Order id: "+order.getId());
                for (Drone drone : this.drone.getDronesList()) {
                    if (!drone.isInDelivery() && !drone.isRecharging()) {
                        availableDrones.add(drone);
                    }
                }

                if (availableDrones.size() == 0) {
                    drone.ordersQueue.putOrder(order);
                    System.err.println("! No drones available. Putting order " + order.getId() + " in the queue...");
                    assigned = false;
                } else if (availableDrones.size() == 1) {
                    selected = availableDrones.get(0);
                    assigned = true;
                } else {
                    //Compare distances
                    double distance = 100.0;
                    List<Drone> nearest = new ArrayList<>();
                    for (Drone drone : availableDrones) {
                        //System.out.println(drone.toString());
                        if(drone.getPosition()!=null){
                            double newDistance = Math.sqrt(
                                    Math.pow(drone.getPosition().getX() - order.getRetire().getX(), 2) +
                                            Math.pow(drone.getPosition().getY() - order.getRetire().getY(), 2)
                            );
                            //System.out.println("entry-2");
                            if (newDistance < distance) {
                                nearest = new ArrayList<>();
                                nearest.add(drone);
                                distance = newDistance;
                            } else if (newDistance == distance) {
                                nearest.add(drone);
                            }
                        }
                    }

                    if (nearest.size() == 0) {
                        drone.ordersQueue.putOrder(order);
                    } else if (nearest.size() == 1) {
                        selected = nearest.get(0);
                    } else {
                        //Compare battery level
                        int maxBatteryLevel = 0;
                        List<Drone> highestBattery = new ArrayList<>();
                        for (Drone drone : nearest) {
                            if (drone.getBatteryLevel() > maxBatteryLevel) {
                                highestBattery = new ArrayList<>();
                                highestBattery.add(drone);
                                maxBatteryLevel = drone.getBatteryLevel();
                            } else if (drone.getBatteryLevel() == maxBatteryLevel) {
                                highestBattery.add(drone);
                            }
                        }
                        if (highestBattery.size() == 0) {
                            drone.ordersQueue.putOrder(order);
                        } else if (highestBattery.size() == 1) {
                            selected = highestBattery.get(0);
                        } else {
                            //Compare ids
                            int maxId = -1;

                            for (Drone drone : highestBattery) {
                                if (drone.getId() > maxId) {
                                    selected = drone;
                                    maxId = drone.getId();
                                }
                            }
                        }
                    }

                    assigned = true;
                }

                if (assigned) {
                    System.out.println("> Assigned order " + order.getId() + " to: " + selected.getId());
                    drone.incrementAssignedDeliveries();
                    drone.addOrderInDelivery(order, System.currentTimeMillis());
                    sendAssignedDeliveryMessage(selected.getIp(), order.getRetire(), order.getDelivery(), order.getId());
                }
            }

           /* if(drone.ordersQueue.size() > 0){
                AssignDelivery ad = new AssignDelivery(drone,drone.ordersQueue.getOrder(),true);
                ad.start();
            }*/

    }

    private void sendAssignedDeliveryMessage(String ip, Position retire, Position delivery, int orderId){

        final ManagedChannel channel = ManagedChannelBuilder.forTarget(ip).usePlaintext(true).build();
        //creating an asynchronous stub on the channel

        ManagerGrpc.ManagerStub stub = ManagerGrpc.newStub(channel);

        //creating the HelloResponse object which will be provided as input to the RPC method
        Welcome.DeliveryMessage request = Welcome.DeliveryMessage
                .newBuilder()
                .setRetire(Welcome.Position
                        .newBuilder()
                        .setX(retire.getX())
                        .setY(retire.getY())
                        .build())
                .setDelivery(Welcome.Position
                        .newBuilder()
                        .setX(delivery.getX())
                        .setY(delivery.getY())
                        .build())
                .setOrderId(orderId)
                .build();

        stub.delivery(request, new StreamObserver<Welcome.DeliveryResponse>() {
            public void onNext(Welcome.DeliveryResponse deliveryResponse) {
                if(deliveryResponse.getReceived()) {
                    //System.out.println("> Ack received");
                }
            }
            public void onError(Throwable throwable) {
                channel.shutdownNow();
                if(throwable.getMessage().equals("UNAVAILABLE: io exception")) {
                    System.err.println("! Drone with ip "+ip+" is not responding...Putting order "+order.getId()+" in queue");
                    drone.ordersQueue.putOrder(order);
                    DroneUnavailable du = new DroneUnavailable(drone, ip, drone.getId());
                    du.start();
                }
            }
            public void onCompleted() {
                channel.shutdownNow();
            }
        });
        try {
            channel.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
