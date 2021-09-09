package threads;

import GRPC.ManagerServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import modules.Drone;

import java.io.IOException;

public class GRPCDroneServer extends Thread{

    private Drone drone;

    public GRPCDroneServer(Drone drone){
        this.drone = drone;
    }

    @Override
    public void run() {
        //Starting the GRPC server for
        try {
            Server server = ServerBuilder.forPort(drone.getPort()).addService(new ManagerServiceImpl(drone)).build();
            server.start();
            System.out.println("> GRPC Server started!");
            server.awaitTermination();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
