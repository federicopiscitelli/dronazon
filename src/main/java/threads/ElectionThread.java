package threads;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import modules.Drone;
import proto.ManagerGrpc;
import proto.Welcome;

import java.util.concurrent.TimeUnit;

public class ElectionThread extends Thread{
    Drone drone;
    int id;
    int batteryLevel;

    public ElectionThread(Drone drone, int id, int batteryLevel){
        this.drone = drone;
        this.id = id;
        this.batteryLevel = batteryLevel;
    }

    public void run(){
        System.out.println("> sendElectionMessageToNext called. Network is: "+drone.getDronesList().toString());
        drone.removeDroneFromList(drone.getMasterID());

        if(drone.getNext() != null) {
            final ManagedChannel channel = ManagedChannelBuilder.forTarget(drone.getNext().getIp()).usePlaintext(true).build();
            //creating an asynchronous stub on the channel
            System.out.println("> Drone "+this.getId() +" is sending election REQUEST to the drone "+drone.getNext().getId()+" ...");

            ManagerGrpc.ManagerStub stub = ManagerGrpc.newStub(channel);

            //creating the HelloResponse object which will be provided as input to the RPC method
            Welcome.ElectionMessage request = Welcome.ElectionMessage
                    .newBuilder()
                    .setId(id)
                    .setBattery(batteryLevel)
                    .build();

            //calling the RPC method. since it is asynchronous, we need to define handlers
            stub.election(request, new StreamObserver<Welcome.ElectionResponse>() {
                //this hanlder takes care of each item received in the stream
                public void onNext(Welcome.ElectionResponse electionResponse) {
                    //each item is just printed
                    if(electionResponse.getReceived())
                        System.out.println("> Ack received");
                }
                public void onError(Throwable throwable) {
                    channel.shutdownNow();
                    //removeDroneFromList(next.getId());
                    //sendElectionMessageToNext(id,levelBattery);
                    System.err.println("> Error: " + throwable.getMessage());
                }

                public void onCompleted() {
                    channel.shutdownNow();
                }
            });

            //you need this. otherwise the method will terminate before that answers from the server are received
            try {
                channel.awaitTermination(2, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
