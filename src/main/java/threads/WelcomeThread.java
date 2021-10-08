package threads;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import modules.Drone;
import proto.ManagerGrpc;
import proto.Welcome;

import java.util.concurrent.TimeUnit;

public class WelcomeThread extends Thread{
    Drone drone;
    String ip;

    public WelcomeThread(Drone drone, String ip) {
        this.drone = drone;
        this.ip = ip;
    }

    public void run(){

        //plaintext channel on the address of the drone
        final ManagedChannel channel = ManagedChannelBuilder.forTarget(ip).usePlaintext(true).build();
        //creating an asynchronous stub on the channel
        ManagerGrpc.ManagerStub stub = ManagerGrpc.newStub(channel);

        //creating the HelloResponse object which will be provided as input to the RPC method
        Welcome.WelcomeMessage request = Welcome.WelcomeMessage
                .newBuilder()
                .setId(drone.getId())
                .setIp(drone.getIp())
                .setPort(drone.getPort())
                .setPosition(Welcome.Position
                                .newBuilder()
                                .setX(drone.getPosition().getX())
                                .setY(drone.getPosition().getY())
                                .build()
                )
                .build();

        //calling the RPC method
        stub.welcome(request, new StreamObserver<Welcome.WelcomeResponse>() {

            //this hanlder takes care of each item received in the stream
            public void onNext(Welcome.WelcomeResponse welcomeResponse) {

                if(welcomeResponse.getMaster()){
                    drone.setMasterID(welcomeResponse.getId());
                }

                System.out.println("> Hello from drone: " + welcomeResponse.getId() + " master:" + welcomeResponse.getMaster());
            }

            //if there are some errors, such as drone is not available
            public void onError(Throwable throwable) {
                channel.shutdownNow();
                //remove the drone from my network topology
                int id = 3000 - Integer.parseInt(ip.split(":")[1]);
                drone.removeDroneFromList(id);
                System.out.println("> Drone with the id " + id + " is unavailable and was removed from the topology");
            }

            //when the stream is completed (the server called "onCompleted")
            public void onCompleted() {
                channel.shutdownNow();
            }
        });

        //Needed for wait the response
        try {
            channel.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
