package threads;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import modules.Drone;
import proto.ManagerGrpc;
import proto.Welcome;

import java.util.concurrent.TimeUnit;

public class UpdatePosition extends Thread{
    private Drone drone;

    public UpdatePosition(Drone drone){
        this.drone = drone;
    }

    public void run(){
        //Send new master my position
        final ManagedChannel channelToMaster = ManagedChannelBuilder.forTarget("127.0.0.1:" + (3000 + drone.getMasterID())).usePlaintext(true).build();

        ManagerGrpc.ManagerStub newStub = ManagerGrpc.newStub(channelToMaster);
        Welcome.PositionMessage positionRequest = Welcome.PositionMessage
                .newBuilder()
                .setId(drone.getId())
                .setPosition(
                        Welcome.Position
                                .newBuilder()
                                .setX(drone.getPosition().getX())
                                .setY(drone.getPosition().getY())
                                .build()
                )
                .build();

        newStub.sendPosition(positionRequest, new StreamObserver<Welcome.PositionResponse>() {
            public void onNext(Welcome.PositionResponse positionResponse) {
                if(positionResponse.getReceived()) {
                   // System.out.println("> Position received successfully");
                }
            }

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
            channelToMaster.awaitTermination(15, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
