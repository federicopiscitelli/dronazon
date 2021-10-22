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
    int oldMasterId;

    public ElectionThread(Drone drone, int id, int batteryLevel){
        this.drone = drone;
        this.id = id;
        this.batteryLevel = batteryLevel;
        this.oldMasterId = drone.getMasterID();
    }

    public synchronized void run(){

        drone.removeDroneFromList(drone.getMasterID());

        System.out.println("> Received election message. Sending to "+drone.getNext().getId());

        final ManagedChannel channel = ManagedChannelBuilder.forTarget(drone.getNext().getIp()).usePlaintext(true).build();

        boolean[] nextNotResponding = {false};

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
                nextNotResponding.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(nextNotResponding[0]){
            if(drone.removeDroneFromList(drone.getNext().getId())) {
                System.out.println("> Retrying with drone "+drone.getNext().getId());
                run();
            }
        }

        try {
            channel.awaitTermination(4, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        //if after ten seconds drone is still in election and master is the old master
                        if(!drone.isInElection() && drone.getMasterID() != oldMasterId){
                            //start new election
                            System.err.println("> Election is stuck. Restarting a new election ...");
                            ElectionThread electionThread = new ElectionThread(drone, drone.getId(), drone.getBatteryLevel());
                            electionThread.run();
                        }
                    }
                },
                10000
        );
    }
}
