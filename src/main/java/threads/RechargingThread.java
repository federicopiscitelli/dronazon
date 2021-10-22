package threads;

import com.google.protobuf.Timestamp;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import modules.Drone;
import proto.ManagerGrpc;
import proto.Welcome;

import java.util.ArrayList;
import java.util.List;

public class RechargingThread extends Thread{

    private Drone drone;

    public RechargingThread(Drone drone){
        this.drone = drone;
    }

    public void run(){

        RechargeLock rechargeLock = new RechargeLock(drone.getDronesList().size());
        for(Drone d: drone.getDronesList()){
            //send message to check if someone is recharging
            final ManagedChannel channel = ManagedChannelBuilder.forTarget(d.getIp()).usePlaintext(true).build();

            ManagerGrpc.ManagerStub stub = ManagerGrpc.newStub(channel);
            Welcome.RechargeRequest request = Welcome.RechargeRequest
                    .newBuilder()
                    .setId(this.drone.getId())
                    .setTimestamp(
                            Timestamp.newBuilder()
                                    .setSeconds(System.currentTimeMillis())
                                    .build())
                    .build();

            stub.recharge(request, new StreamObserver<Welcome.RechargeResponse>() {
                public void onNext(Welcome.RechargeResponse aliveResponse) {
                    System.out.println("> Drone "+d.getId()+" is not recharging");
                    rechargeLock.wakeUp();
                }
                public void onError(Throwable throwable) {
                    channel.shutdownNow();
                }
                public void onCompleted() {
                    channel.shutdownNow();
                }
            });

        }
        rechargeLock.block();
        System.out.println("> Eseguito");
    }
}

class RechargeLock{

    public int maxEl;
    public int responses;
    Object lock;

    public RechargeLock(int maxEl){
        this.maxEl = maxEl;
        responses = 0;
        lock = new Object();
    }

    public void block() {
        while(responses < maxEl){
            try {
                synchronized (lock) {
                    lock.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        lock.notifyAll();
    }

    public void wakeUp(){
        responses++;
        lock.notifyAll();
    }
}
