import Server.Drone;
import Server.modules.AddResponse;
import Server.modules.Drones;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DroneProcess {
    private static final String RESTServerAddress = "http://localhost:1337";

    public static void main(String[] args) throws IOException {

        BufferedReader inFromUser =new BufferedReader(new InputStreamReader(System.in));

        Client client = Client.create();
        ClientResponse clientResponse = null;

        //String id = UUID.randomUUID().toString().replace("-", "");
        System.out.println("> Insert a drone");
        System.out.print("> Drone ID: ");
        String id = inFromUser.readLine();
        System.out.print("> Drone IP: ");
        String ip = inFromUser.readLine();
        System.out.print("> Drone comunication port: ");
        int port = Integer.parseInt(inFromUser.readLine());

        // Create a drone
        Drone drone = new Drone(id, ip, port);

        String postPath = "/drones/add";
        clientResponse = postRequest(client,RESTServerAddress+postPath,drone);
        System.out.println(clientResponse.toString());
        AddResponse response = clientResponse.getEntity(AddResponse.class);
        drone.setDronesList(response.getDrones());
        drone.setPosition(response.getPosition());
        System.out.println(drone);
    }


    public static ClientResponse postRequest(Client client, String url, Drone d){
        WebResource webResource = client.resource(url);
        String input = new Gson().toJson(d);
        try {
            return webResource.type("application/json").post(ClientResponse.class, input);
        } catch (ClientHandlerException e) {
            System.out.println("Server not reachable");
            return null;
        }
    }


}
