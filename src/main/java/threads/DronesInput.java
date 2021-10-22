package threads;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
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
        while(true) {
            System.out.println("> Digit 'exit' to stop...");
            System.out.println("> Digit 'recharge' to recharge the drone's battery...");
            try {
                InputStreamReader streamReader = new InputStreamReader(System.in);
                BufferedReader reader = new BufferedReader(streamReader);

                String choice = reader.readLine();

                if (choice.equals("exit")) {
                    Client client = Client.create();
                    System.out.println("> Removing the drone from the network...");
                    //Remove a drone to the REST Server
                    String deletePath = "/drones/" + d.getId();
                    WebResource webResource = client.resource(RESTServerAddress + deletePath);
                    try {
                        webResource.type("application/json").delete();

                    } catch (ClientHandlerException e) {
                        System.out.println("> Server not reachable");
                    }
                    System.out.println("> Drone removed...");
                    System.exit(0);
                } else if (choice.equals("recharge")) {
                    RechargingThread rechargingThread = new RechargingThread(d);
                    rechargingThread.run();
                } else {
                    System.out.println("> Unknown command");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
