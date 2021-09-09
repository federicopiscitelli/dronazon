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

public class CreateDrone extends Thread{
    private static final String RESTServerAddress = "http://localhost:1337";
    private static final int BASE_PORT = 3000;
    private static final String BASE_IP = "127.0.0.1";

    private int port;
    private int id;
    private String ip;

    public CreateDrone(){
        this.id = (int) (0 + Math.random() * 100);
        this.port = BASE_PORT + id;
        this.ip = BASE_IP +":"+ String.valueOf(port);
    }

    public void run(){

    }

}
