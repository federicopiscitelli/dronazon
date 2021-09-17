package threads;

import MQTT.DroneSubscriber;
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

public class MasterLifeChecker implements Runnable{
    private Drone drone;
    private boolean exit;

    public MasterLifeChecker(Drone drone){
        this.drone = drone;
        exit = false;
    }

    @Override
    public void run() {
        while(!exit) {
            if (!drone.isMaster()) {
                //plaintext channel on the address of the drone
                final ManagedChannel channel = ManagedChannelBuilder.forTarget("127.0.0.1:" + (3000 + drone.getMasterID())).usePlaintext(true).build();

                System.out.println("> Drone " + drone.getId() + " is sending alive message to the drone master ...");

                //creating an asynchronous stub on the channel
                ManagerGrpc.ManagerStub stub = ManagerGrpc.newStub(channel);

                //creating the HelloResponse object which will be provided as input to the RPC method
                Welcome.AliveMessage request = Welcome.AliveMessage
                        .newBuilder()
                        .setId(drone.getId())
                        .build();


                //calling the RPC method. since it is asynchronous, we need to define handlers
                stub.alive(request, new StreamObserver<Welcome.AliveResponse>() {

                    //this hanlder takes care of each item received in the stream
                    public void onNext(Welcome.AliveResponse aliveResponse) {
                        //each item is just printed
                        if (aliveResponse.getIsAlive())
                            System.out.println("> Master is alive");
                    }

                    //if there are some errors, this method will be called
                    public void onError(Throwable throwable) {
                        channel.shutdownNow();
                        drone.stopMasterLifeChecker();
                        drone.removeDroneFromList(drone.getMasterID());
                        System.err.println("> Master is not responding -> " + throwable.getMessage());
                        drone.sendElectionMessageToNext(drone.getId(), drone.getBatteryLevel());
                    }

                    //when the stream is completed (the server called "onCompleted") just close the channel
                    public void onCompleted() {
                        channel.shutdownNow();
                    }
                });

                //you need this. otherwise the method will terminate before that answers from the server are received
                try {
                    channel.awaitTermination(10, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else {
                stop();
            }
            try {
                int timeout = (int) (0 + Math.random() * 20);
                Thread.sleep(timeout*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void stop(){
        exit = true;
    }


}
