package threads;

import MQTT.DroneSubscriber;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import modules.Drone;
import proto.ManagerGrpc;
import proto.Welcome;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MasterLifeChecker extends Thread{
    private Drone drone;
    private DroneSubscriber subscriber;

    public MasterLifeChecker(Drone drone, DroneSubscriber subscriber){
        this.drone = drone;
        this.subscriber = subscriber;
    }

    @Override
    public void run() {

        if(!drone.isMaster()) {
            //plaintext channel on the address of the drone
            final ManagedChannel channel = ManagedChannelBuilder.forTarget(findNextDrone(drone).getIp()).usePlaintext(true).build();

            System.out.println("> Drone " + drone.getId() + " is sending election request to the drone " + findNextDrone(drone).getId() + " ...");

            //creating an asynchronous stub on the channel
            ManagerGrpc.ManagerStub stub = ManagerGrpc.newStub(channel);

            //creating the HelloResponse object which will be provided as input to the RPC method
            Welcome.AliveMessage request = Welcome.AliveMessage
                    .newBuilder()
                    .setId(drone.getId())
                    .build();


            //calling the RPC method. since it is asynchronous, we need to define handlers
            stub.alive(request, new StreamObserver<Welcome.AliveResponse>() {

                //this hanlder takes care of each item received in the stream
                public void onNext(Welcome.AliveResponse aliveResponse) {
                    //each item is just printed
                    if (aliveResponse.getIsAlive())
                        System.out.println("> Ack received");
                }

                //if there are some errors, this method will be called
                public void onError(Throwable throwable) {
                    channel.shutdownNow();
                    System.out.println("> Error: " + throwable.getMessage());
                    startElection();
                }

                //when the stream is completed (the server called "onCompleted") just close the channel
                public void onCompleted() {
                    channel.shutdownNow();
                }
            });

            //you need this. otherwise the method will terminate before that answers from the server are received
            try {
                channel.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void startElection(){
        //plaintext channel on the address of the drone
        final ManagedChannel channel = ManagedChannelBuilder.forTarget(findNextDrone(drone).getIp()).usePlaintext(true).build();

        System.out.println("> Drone "+drone.getId() +" is sending election request to the drone "+findNextDrone(drone).getId()+" ...");

        //creating an asynchronous stub on the channel
        ManagerGrpc.ManagerStub stub = ManagerGrpc.newStub(channel);

        //creating the HelloResponse object which will be provided as input to the RPC method
        Welcome.ElectionMessage request = Welcome.ElectionMessage
                .newBuilder()
                .setId(drone.getId())
                .setBattery(drone.getBatteryLevel())
                .build();


        //calling the RPC method. since it is asynchronous, we need to define handlers
        stub.election(request, new StreamObserver<Welcome.ElectionResponse>() {

            //this hanlder takes care of each item received in the stream
            public void onNext(Welcome.ElectionResponse electionResponse) {
                //each item is just printed
                if(electionResponse.getReceived())
                    System.out.println("> Ack received");
            }

            //if there are some errors, this method will be called
            public void onError(Throwable throwable) {
                channel.shutdownNow();
                System.out.println("> Error: " + throwable.getMessage());
            }

            //when the stream is completed (the server called "onCompleted") just close the channel
            public void onCompleted() {
                channel.shutdownNow();
            }
        });

        //you need this. otherwise the method will terminate before that answers from the server are received
        try {
            channel.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static Drone findNextDrone(Drone d){
        int id = d.getId();
        List<Drone> myDronesList = d.getDronesList();
        List<Integer> idList = new ArrayList<>();

        myDronesList.sort(new Comparator<Drone>() {
            @Override
            public int compare(Drone o1, Drone o2) {
                if(o1.getId() > o2.getId()){
                    return 1;
                }
                if(o1.getId() < o2.getId()){
                    return -1;
                }
                return 0;
            }
        });

        for (Drone currentDrone : myDronesList) {
            idList.add(currentDrone.getId());
        }

        Drone next = null;
        for(int i = 0; i < idList.size(); i++){
            if(idList.get(i) == id){
                if((i+1)<idList.size()){
                    next = myDronesList.get(i+1);
                } else {
                    next = myDronesList.get(0);
                }
            }
        }

        return next;

    }
}
