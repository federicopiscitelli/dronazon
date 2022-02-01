package threads;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import modules.Drone;
import proto.ManagerGrpc;
import proto.Welcome;

public class DroneUnavailable extends Thread{
    private Drone drone;
    private int unavailableID;
    private int startingDroneID;

    public DroneUnavailable(Drone drone, int unavailableID, int startingDroneID){
        this.drone = drone;
        //calculating the id of the drone starting from his ip
        this.unavailableID = unavailableID;
        this.startingDroneID = startingDroneID;
    }

    public DroneUnavailable(Drone drone, String unavailableIP, int startingDroneID){
        this(drone,Integer.valueOf(unavailableIP.split(":")[1])-3000,startingDroneID);
    }

    public void run(){

        drone.removeDroneFromList(unavailableID);
        //Telling REST server that a drone exit the network
        if(drone.getId() == startingDroneID){ //if i'm the first to notice that the drone is down
            Client client = Client.create();
            //Remove a drone to the REST Server
            String deletePath = "/drones/" + unavailableID;
            WebResource webResource = client.resource("http://localhost:1337" + deletePath);
            try {
                webResource.type("application/json").delete();
            } catch (ClientHandlerException e) {
                System.out.println("> Server not reachable");
            }
        }

        //Telling my next that a drone exit the network
        /*final ManagedChannel channel = ManagedChannelBuilder.forTarget(drone.getNext().getIp()).usePlaintext(true).build();
        ManagerGrpc.ManagerStub stub = ManagerGrpc.newStub(channel);
        Welcome.UnavailableDroneMessage request = Welcome.UnavailableDroneMessage
                .newBuilder()
                .setStartingDroneID(startingDroneID)
                .setId(drone.getId())
                .build();

        stub.unavailableDrone(request, new StreamObserver<Welcome.UnavailableDroneResponse>() {
            public void onNext(Welcome.UnavailableDroneResponse response) {}
            public void onError(Throwable throwable) {
                channel.shutdownNow();
                //what if next is not responding
            }
            public void onCompleted() {
                channel.shutdownNow();
            }
        });*/
    }
}
