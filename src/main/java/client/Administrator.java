package client;

import com.google.gson.Gson;
import com.sun.jersey.api.client.*;
import modules.Drone;
import java.util.List;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Administrator {

    private static final String RESTServerAddress = "http://localhost:1337";

    public static void main(String args[]){
        BufferedReader inFromUser =new BufferedReader(new InputStreamReader(System.in));
        Client client = Client.create();
        ClientResponse clientResponse = null;
        Gson gson = new Gson();

        int choice = -1;

        System.out.println(" _______  .______        ______   .__   __.      ___      ________    ______   .__   __. \n" +
                "|       \\ |   _  \\      /  __  \\  |  \\ |  |     /   \\    |       /   /  __  \\  |  \\ |  | \n" +
                "|  .--.  ||  |_)  |    |  |  |  | |   \\|  |    /  ^  \\   `---/  /   |  |  |  | |   \\|  | \n" +
                "|  |  |  ||      /     |  |  |  | |  . `  |   /  /_\\  \\     /  /    |  |  |  | |  . `  | \n" +
                "|  '--'  ||  |\\  \\----.|  `--'  | |  |\\   |  /  _____  \\   /  /----.|  `--'  | |  |\\   | \n" +
                "|_______/ | _| `._____| \\______/  |__| \\__| /__/     \\__\\ /________| \\______/  |__| \\__| ");

        do {

            System.out.println("\n\n>Choose an option from the menu:");
            System.out.println("> [1] Get the drones list in the network");
            System.out.println("> [2] Get the last n global statistics");
            System.out.println("> [3] Get the average of the deliveries between two timestamp");
            System.out.println("> [4] Get the average of the distance between two timestamp");
            System.out.println("> [0] Exit");

            try {
                System.out.print("> Your choice: ");
                choice = Integer.parseInt(inFromUser.readLine());

                switch (choice){
                    case 1:{
                        String path = "/drones";
                        try {
                            WebResource webResource = client.resource(RESTServerAddress + path);
                            clientResponse = webResource.type("application/json").get(ClientResponse.class);
                            String json = clientResponse.getEntity(String.class);
                            Object o = gson.fromJson(json, Object.class);
                            System.out.println(o.toString());

                        } catch (ClientHandlerException e) {
                            System.out.println("Error during the request: "+e.getMessage());
                        }
                    } break;
                    case 2:{
                        System.out.print("> Your choice: ");
                        String n = inFromUser.readLine();

                        String path = "/stats/"+n;
                        try {
                            WebResource webResource = client.resource(RESTServerAddress + path);
                            clientResponse = webResource.type("application/json").get(ClientResponse.class);
                            String json = clientResponse.getEntity(String.class);
                            Object o = gson.fromJson(json, Object.class);
                            System.out.println(o.toString());

                        } catch (ClientHandlerException e) {
                            System.out.println("Error during the request: "+e.getMessage());
                        }
                    } break;
                    default:
                        break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        }while(choice != 0);

        System.out.println("> Thank you! Bye!");
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
