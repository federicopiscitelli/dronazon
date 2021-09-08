import modules.AddResponse;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import modules.Drone;
import threads.InsertDrone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DroneProcess {


    public static void main(String[] args) throws IOException {

        InsertDrone threadInsert = new InsertDrone();
        threadInsert.start();


    }

}
