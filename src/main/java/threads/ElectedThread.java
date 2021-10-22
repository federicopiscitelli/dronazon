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
        System.out.println("> Elected message to "+drone.getNext().getId());

        final ManagedChannel channel = ManagedChannelBuilder.forTarget(drone.getNext().getIp()).usePlaintext(true).build();

        boolean[] nextNotResponding = {false};

        ManagerGrpc.ManagerStub stub = ManagerGrpc.newStub(channel);
        Welcome.ElectedMessage request = Welcome.ElectedMessage
                .newBuilder()
                .setId(id)
                .build();

        stub.elected(request, new StreamObserver<Welcome.ElectedResponse>() {
            public void onNext(Welcome.ElectedResponse aliveResponse) {}
            public void onError(Throwable throwable) {
                channel.shutdownNow();
                if(throwable.getMessage().equals("UNAVAILABLE: io exception")) {
                    System.err.println("> Next is not responding");
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
                nextNotResponding.wait(); //wait until the lock is released
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(nextNotResponding[0]){
            if(drone.getNext().getId() != id && drone.removeDroneFromList(drone.getNext().getId())) {
                System.out.println("> Retrying with drone: "+drone.getNext().getId());
                run();
            } else if(drone.getNext().getId() == id && drone.removeDroneFromList(drone.getNext().getId())){
                ElectionThread electionThread = new ElectionThread(drone,drone.getId(), drone.getBatteryLevel());
                electionThread.run();
            }
        }

        try {
            channel.awaitTermination(4, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
