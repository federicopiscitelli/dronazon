package threads;

import MQTT.DroneSubscriber;
import io.grpc.Context;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import modules.Drone;
import proto.ManagerGrpc;
import proto.Welcome;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MasterLifeChecker extends Thread{
    private Drone drone;
    private boolean exit;

    public MasterLifeChecker(Drone drone){
        this.drone = drone;
        exit = false;
    }

    @Override
    public void run() {
        while(!exit) {
            if (!drone.isMaster() && drone.getMasterID() != -1 && !drone.isInElection()) {

                final ManagedChannel channel = ManagedChannelBuilder.forTarget("127.0.0.1:" + (3000 + drone.getMasterID())).usePlaintext(true).build();

                ManagerGrpc.ManagerStub stub = ManagerGrpc.newStub(channel);
                Welcome.AliveMessage request = Welcome.AliveMessage
                        .newBuilder()
                        .setId(drone.getId())
                        .build();

                stub.alive(request, new StreamObserver<Welcome.AliveResponse>() {
                    public void onNext(Welcome.AliveResponse aliveResponse) {
                       // System.out.println("> Master is alive");
                    }

                    public void onError(Throwable throwable) {
                        channel.shutdownNow();
                        stop();
                        System.err.println("! Master is not responding -> " + throwable.getMessage());
                        if(!drone.isInElection() && drone.getDronesList().size()>2) {
                            ElectionThread electionThread = new ElectionThread(drone,drone.getId(),drone.getBatteryLevel());
                            electionThread.start();
                        } else if(!drone.isInElection() && drone.getDronesList().size()<=2){
                            drone.updateDroneInListAfterElection(drone.getId(),drone.getPosition());
                            System.out.println(drone.getDronesList().toString());
                            drone.setMaster(true);
                        }
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
            }else {
                stop();
            }

            if(!exit) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void stopExecution(){
        exit = true;
    }


}
