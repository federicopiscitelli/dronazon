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

        drone.removeDroneFromList(drone.getMasterID());

        System.out.println("> Received election message. Sending to "+drone.getNext().getId());

        final ManagedChannel channel = ManagedChannelBuilder.forTarget(drone.getNext().getIp()).usePlaintext(true).build();

        ManagerGrpc.ManagerStub stub = ManagerGrpc.newStub(channel);
        Welcome.ElectionMessage request = Welcome.ElectionMessage
                .newBuilder()
                .setId(id)
                .setBattery(batteryLevel)
                .build();

        stub.election(request, new StreamObserver<Welcome.ElectionResponse>() {
            public void onNext(Welcome.ElectionResponse aliveResponse) {}
            public void onError(Throwable throwable) {
                channel.shutdownNow();
                stop();
                System.err.println("> Next is not responding -> " + throwable.getMessage());
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
