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

        boolean[] nextNotResponding = {false};

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
                    //System.out.println("> Drone " + ip + " is not recharging");
                    lock.wakeUp();
                }
            }
            public void onError(Throwable throwable) {
                channel.shutdownNow();
                if(throwable.getMessage().equals("UNAVAILABLE: io exception")) {
                    System.err.println("! Next is not responding");
                    nextNotResponding[0] = true;
                    synchronized (nextNotResponding){
                        nextNotResponding.notify();
                    }
                } else {
                    System.err.println(throwable.getMessage());
                }
            }
            public void onCompleted() {
                channel.shutdownNow();
            }
        });

        try {
            synchronized (nextNotResponding){
                nextNotResponding.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(nextNotResponding[0]){
            int id = Integer.valueOf(ip.split(":")[1])-3000;
            if(drone.removeDroneFromList(id)){
                RechargingThread rt = new RechargingThread(drone);
                rt.start();
            }
        }

        try {
            channel.awaitTermination((drone.getDronesList().size()-1)*1000L+1000, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
