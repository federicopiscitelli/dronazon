import MQTT.DroneSubscriber;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import modules.AddResponse;
import modules.Drone;
import proto.ManagerGrpc;
import proto.Welcome;
import threads.DronesInput;
import GRPC.GRPCDroneServer;
import threads.MasterLifeChecker;
import threads.WelcomeThread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DroneProcess {

    private static final String RESTServerAddress = "http://localhost:1337";
    private static final int BASE_PORT = 3000;
    private static final String BASE_IP = "127.0.0.1";


    private static int id = (int) (0 + Math.random() * 100);
    private static int port = BASE_PORT + id;
    private static String ip = BASE_IP +":"+ String.valueOf(port);

    public static void main(String[] args) throws IOException {

        Client client = Client.create();
        ClientResponse clientResponse = null;

        // Create a drone
        Drone drone = new Drone(id, ip, port);

        //Adding a drone to the REST Server
        String postPath = "/drones/add";
        WebResource webResource = client.resource(RESTServerAddress+postPath);
        String input = new Gson().toJson(drone);
        try {
            clientResponse =  webResource.type("application/json").post(ClientResponse.class, input);
        } catch (ClientHandlerException e) {
            System.out.println("> Server not reachable");
            clientResponse = null;
        }

        System.out.println("> New drone created with id: "+id);

        //Start thread for standard input
        DronesInput di = new DronesInput(drone);
        di.start();

        //The response of the REST server is the list of the drones in the network
        AddResponse response = clientResponse.getEntity(AddResponse.class);

        //Adding the drone list to my drone and setting the position
        drone.setDronesList(response.getDrones());
        drone.setPosition(response.getPosition());
        drone.updateDroneInList(drone);
        System.out.println(drone.toString());

        //Starting GRPC Server
        GRPCDroneServer threadServerGRPC = new GRPCDroneServer(drone);
        threadServerGRPC.start();

        //if there is more than one drone
        if(response.getDrones().size()>1) {
            //welcome to all drones in the network
            for (Drone d : response.getDrones()) {
                if(!(d.getId()==drone.getId())){
                    WelcomeThread welcomeThread = new WelcomeThread(drone,d.getIp());
                    welcomeThread.run();
                }
            }
            //If i'm not the master start the thread to check if master is still alive
            drone.setMaster(false);
        } else {
            //if there is only one drone, setting to master
            drone.setMaster(true);
        }

    }

}
