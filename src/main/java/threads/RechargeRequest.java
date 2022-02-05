package threads;

import com.google.protobuf.Timestamp;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import modules.Drone;
import proto.ManagerGrpc;
import proto.Welcome;

import java.util.concurrent.TimeUnit;

public class RechargeRequest extends Thread{

    private Drone drone;
    private String ip;
    private RechargeLock lock;

    public RechargeRequest(Drone drone, String ip, RechargeLock lock){
        this.drone = drone;
        this.ip = ip;
        this.lock = lock;
    }

    public void run(){
        final ManagedChannel channel = ManagedChannelBuilder.forTarget(ip).usePlaintext(true).build();

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
            public void onNext(Welcome.RechargeResponse rechargeResponse) {
                if(rechargeResponse.getFree()) {
                    System.out.println("> Drone " + ip + " is not recharging");
                    lock.wakeUp();
                }
            }
            public void onError(Throwable throwable) {
                channel.shutdownNow();

            }
            public void onCompleted() {
                channel.shutdownNow();
            }
        });
        try {
            channel.awaitTermination(15, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
