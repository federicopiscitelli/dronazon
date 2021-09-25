package threads;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import modules.Drone;
import proto.ManagerGrpc;
import proto.Welcome;

import java.util.concurrent.TimeUnit;

public class ElectedThread extends Thread{
    Drone drone;
    int id;

    public ElectedThread(Drone drone, int id){
        this.drone = drone;
        this.id = id;
    }

    public void run(){
        System.out.println("> Network "+drone.getDronesList().toString());
        System.out.println("> sendElectedMessageToNext called. My ID: "+this.id+" Next ID: "+this.drone.getNext().getId()+" New Master: "+id);
        if(drone.getNext() != null) {
            final ManagedChannel channel = ManagedChannelBuilder.forTarget(drone.getNext().getIp()).usePlaintext(true).build();
            //creating an asynchronous stub on the channel
            System.out.println("> Drone "+this.getId() +" is sending elected MESSAGE to the drone "+drone.getNext().getId()+" ... Master is now "+id);

            ManagerGrpc.ManagerStub stub = ManagerGrpc.newStub(channel);

            //creating the ElectedMessage object which will be provided as input to the RPC method
            Welcome.ElectedMessage request = Welcome.ElectedMessage
                    .newBuilder()
                    .setId(id)
                    .build();

            //calling the RPC method. since it is asynchronous, we need to define handlers
            stub.elected(request, new StreamObserver<Welcome.ElectedResponse>() {
                public void onNext(Welcome.ElectedResponse electedResponse) { }
                //if there are some errors
                public void onError(Throwable throwable) {
                    channel.shutdownNow();
                    System.out.println("> Error: " + throwable.getMessage());
                }
                //when the stream is completed
                public void onCompleted() {
                    channel.shutdownNow();
                }
            });
            //wait for the response
            try {
                channel.awaitTermination(2, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
