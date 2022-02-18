package threads;

import com.google.protobuf.Timestamp;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import modules.Drone;
import modules.Position;
import proto.ManagerGrpc;
import proto.Welcome;

public class RechargingThread extends Thread{

    private Drone drone;

    public RechargingThread(Drone drone){
        this.drone = drone;
    }

    public void run(){

        RechargeLock rechargeLock = new RechargeLock(drone.getDronesList().size(), drone);

        for(Drone d: drone.getDronesList()){
            //send message to check if someone is recharging
            RechargeRequest rq = new RechargeRequest(drone, d.getIp(), rechargeLock);
            rq.start();
        }
        rechargeLock.block();
    }
}

class RechargeLock{

    public int maxEl;
    public int responses;
    Object lock;
    Drone drone;

    public RechargeLock(int maxEl, Drone drone){
        this.maxEl = maxEl;
        responses = 0;
        lock = new Object();
        this.drone = drone;
    }

    public void block() {
        //System.out.println("> Responses :"+responses + " Max el: " +maxEl);

        synchronized (lock) {
            System.out.println("> Waiting");
            while(responses < maxEl){
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("> Recharging ...");
        drone.setInCharge(true);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        drone.setPosition(new Position(0,0));
        drone.setBatteryLevel(100);
        drone.setInCharge(false);
        System.out.println("> Battery is now fully charged");
        drone.setWantRecharge(null);
        drone.rechargeLockServer.wakeUp();


        //send message to master for updating the position
        String masterIp = "127.0.0.1:"+String.valueOf(3000+drone.getMasterID());
        final ManagedChannel channel = ManagedChannelBuilder.forTarget(masterIp).usePlaintext(true).build();
        ManagerGrpc.ManagerStub stub = ManagerGrpc.newStub(channel);
        Welcome.RechargeComplete request = Welcome.RechargeComplete
                .newBuilder()
                .setId(drone.getId())
                .setBatteryLevel(100)
                .setNewPosition(
                        Welcome.Position.newBuilder().setX(0).setY(0).build()
                )
                .build();

        stub.rechargeCompleted(request, new StreamObserver<Welcome.RechargeCompleteResponse>() {
            public void onNext(Welcome.RechargeCompleteResponse aliveResponse) {}
            public void onError(Throwable throwable) {
                channel.shutdownNow();
            }
            public void onCompleted() {
                channel.shutdownNow();
            }
        });
    }

    public void wakeUp(){
        responses++;
        //System.out.println("> Responses: "+responses);
        synchronized (lock) {
            lock.notifyAll();
        }
    }
}
