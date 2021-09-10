package threads;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import modules.Drone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 *
 * Class that implements a thread to manage standard input of the drone
 *
 */

public class DronesInput extends Thread{
    private static final String RESTServerAddress = "http://localhost:1337";

    private Drone d;

    public DronesInput(Drone d){
        this.d = d;
    }

    public void run() {
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("> Hit return to stop...");

        try {
            System.in.read();
            Client client = Client.create();

            System.out.println("> Removing the drone from the network...");
            //Remove a drone to the REST Server
            String deletePath = "/drones/"+d.getId();
            WebResource webResource = client.resource(RESTServerAddress+deletePath);
            try {
                webResource.type("application/json").delete();

            } catch (ClientHandlerException e) {
                System.out.println("> Server not reachable");
            }
            System.out.println("> Drone removed...");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
