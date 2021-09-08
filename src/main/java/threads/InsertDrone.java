package threads;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import modules.AddResponse;
import modules.Drone;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class InsertDrone extends Thread{
    private static final String RESTServerAddress = "http://localhost:1337";
    private static final int BASE_PORT = 3000;
    private static final String BASE_IP = "127.0.0.1";

    private int port;
    private int id;
    private String ip;

    public InsertDrone(){
        this.id = (int) (0 + Math.random() * 100);
        this.port = BASE_PORT + id;
        this.ip = BASE_IP +":"+ String.valueOf(port);
    }

    public void run(){
        Client client = Client.create();
        ClientResponse clientResponse = null;

        // Create a drone
        Drone drone = new Drone(String.valueOf(this.id), ip, port);

        String postPath = "/drones/add";
        clientResponse = postRequest(client,RESTServerAddress+postPath,drone);
        System.out.println(clientResponse.toString());
        AddResponse response = clientResponse.getEntity(AddResponse.class);
        drone.setDronesList(response.getDrones());
        drone.setPosition(response.getPosition());
        System.out.println(drone);
    }

    private static ClientResponse postRequest(Client client, String url, Drone d){
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
