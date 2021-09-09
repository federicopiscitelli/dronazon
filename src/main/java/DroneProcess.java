import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import modules.AddResponse;
import modules.Drone;
import threads.CreateDrone;
import threads.DronesInput;

import java.io.IOException;

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
        Drone drone = new Drone(String.valueOf(id), ip, port);

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

        //Start thread for standard input
        DronesInput di = new DronesInput();
        di.start();

        //The response of the REST REST.REST.client.client.server is the list of the drones in the network
        AddResponse response = clientResponse.getEntity(AddResponse.class);
        drone.setDronesList(response.getDrones());
        drone.setPosition(response.getPosition());

        //welcome to all drones in the network
        for(Drone d : response.getDrones()){
            System.out.println(d.toString());
        }
    }

}
