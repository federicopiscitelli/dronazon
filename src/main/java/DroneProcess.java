import GRPC.ManagerServiceImpl;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import modules.AddResponse;
import modules.Drone;
import proto.ManagerGrpc;
import proto.Welcome;
import threads.DronesInput;
import threads.GRPCDroneServer;

import java.io.IOException;
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

        System.out.println("> New drone created with id: "+id);

        //Start thread for standard input
        DronesInput di = new DronesInput(drone);
        di.start();

        //The response of the REST server is the list of the drones in the network
        AddResponse response = clientResponse.getEntity(AddResponse.class);

        List<Drone> tempList = drone.getDronesList();
        tempList.add(drone);
        drone.setDronesList(tempList);
        drone.setPosition(response.getPosition());

        GRPCDroneServer threadServerGRPC = new GRPCDroneServer(drone);
        threadServerGRPC.start();

        System.out.println(response.getDrones().size()>1);

        //if there is more than one drone
        if(response.getDrones().size()>1) {

            //welcome to all drones in the network
            for (Drone d : response.getDrones()) {

                if(!d.getId().equals(drone.getId())) {
                    //plaintext channel on the address of the drone
                    final ManagedChannel channel = ManagedChannelBuilder.forTarget(d.getIp()).usePlaintext(true).build();
                    //creating an asynchronous stub on the channel
                    ManagerGrpc.ManagerStub stub = ManagerGrpc.newStub(channel);

                    //creating the HelloResponse object which will be provided as input to the RPC method
                    Welcome.WelcomeMessage request = Welcome.WelcomeMessage
                            .newBuilder()
                            .setId(Integer.parseInt(drone.getId()))
                            .setIp(drone.getIp())
                            .setPort(drone.getPort())
                            .build();


                    //calling the RPC method. since it is asynchronous, we need to define handlers
                    stub.welcome(request, new StreamObserver<Welcome.WelcomeResponse>() {

                        //this hanlder takes care of each item received in the stream
                        public void onNext(Welcome.WelcomeResponse welcomeResponse) {
                            //each item is just printed
                            if(welcomeResponse.getMaster()){
                                drone.setMasterID(String.valueOf(welcomeResponse.getId()));
                            }
                            System.out.println("> Hello from drone: " + welcomeResponse.getId() + " master:" + welcomeResponse.getMaster());
                        }

                        //if there are some errors, this method will be called
                        public void onError(Throwable throwable) {

                            System.out.println("> Error! " + throwable.getMessage());

                        }

                        //when the stream is completed (the server called "onCompleted") just close the channel
                        public void onCompleted() {
                            channel.shutdownNow();
                        }
                    });

                    //you need this. otherwise the method will terminate before that answers from the server are received
                    try {
                        channel.awaitTermination(10, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            drone.setMaster(true);
        }
    }

}
