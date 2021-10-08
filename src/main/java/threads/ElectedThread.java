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
        System.out.println("> "+ System.currentTimeMillis() +": received elected message. Sending to"+drone.getNext().getId());
        System.err.println(drone.getMasterID());

        final ManagedChannel channel = ManagedChannelBuilder.forTarget(drone.getNext().getIp()).usePlaintext(true).build();

        ManagerGrpc.ManagerStub stub = ManagerGrpc.newStub(channel);
        Welcome.ElectedMessage request = Welcome.ElectedMessage
                .newBuilder()
                .setId(id)
                .build();

        stub.elected(request, new StreamObserver<Welcome.ElectedResponse>() {
            public void onNext(Welcome.ElectedResponse aliveResponse) {}
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
