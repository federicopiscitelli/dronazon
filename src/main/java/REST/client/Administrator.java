package REST.client;

import com.google.gson.Gson;
import com.sun.jersey.api.client.*;
import modules.Drone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Administrator {

    private static final String RESTServerAddress = "http://localhost:1337";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

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
                            ArrayList<Drone> drones = gson.fromJson(json, ArrayList.class);
                            System.out.println(drones.toString());

                        } catch (ClientHandlerException e) {
                            System.out.println("> Error during the request: "+e.getMessage());
                        }
                    } break;
                    case 2:{
                        System.out.print("> Write the number: ");
                        String n = inFromUser.readLine();

                        String path = "/stats/"+n;
                        try {
                            WebResource webResource = client.resource(RESTServerAddress + path);
                            clientResponse = webResource.type("application/json").get(ClientResponse.class);
                            String json = clientResponse.getEntity(String.class);
                            Object o = gson.fromJson(json, Object.class);
                            System.out.println(o);

                        } catch (ClientHandlerException e) {
                            System.out.println("> Error during the request: "+e.getMessage());
                        }
                    } break;
                    case 3:{
                        System.out.print("> Write the first timestamp: ");
                        String t1 = inFromUser.readLine();
                        System.out.print("> Write the second timestamp: ");
                        String t2 = inFromUser.readLine();


                        Date firstParsedDate = DATE_FORMAT.parse(t1);
                        Timestamp firstTimestamp = new java.sql.Timestamp(firstParsedDate.getTime());
                        Date secondParsedDate = DATE_FORMAT.parse(t2);
                        Timestamp secondTimestamp = new java.sql.Timestamp(secondParsedDate.getTime());

                        String path = "/stats/avg/delivery/"+firstTimestamp+"/"+secondTimestamp;
                        try {
                            WebResource webResource = client.resource(RESTServerAddress + path);
                            clientResponse = webResource.type("application/json").get(ClientResponse.class);
                            String json = clientResponse.getEntity(String.class);
                            Object o = gson.fromJson(json, Object.class);
                            System.out.println(o.toString());

                        } catch (ClientHandlerException e) {
                            System.out.println("> Error during the request: "+e.getMessage());
                        }
                    } break;
                    case 4:{
                        System.out.print("> Write the first timestamp: ");
                        String t1 = inFromUser.readLine();
                        System.out.print("> Write the second timestamp: ");
                        String t2 = inFromUser.readLine();

                        Date firstParsedDate = DATE_FORMAT.parse(t1);
                        Timestamp firstTimestamp = new java.sql.Timestamp(firstParsedDate.getTime());
                        Date secondParsedDate = DATE_FORMAT.parse(t2);
                        Timestamp secondTimestamp = new java.sql.Timestamp(secondParsedDate.getTime());

                        String path = "/stats/avg/distance/"+firstTimestamp+"/"+secondTimestamp;
                        try {
                            WebResource webResource = client.resource(RESTServerAddress + path);
                            clientResponse = webResource.type("application/json").get(ClientResponse.class);
                            String json = clientResponse.getEntity(String.class);
                            Object o = gson.fromJson(json, Object.class);
                            System.out.println(o.toString());

                        } catch (ClientHandlerException e) {
                            System.out.println("> Error during the request: "+e.getMessage());
                        }
                    } break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }while(choice != 0);

        System.out.println("> Thank you! Bye!");
    }

}
