package threads;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.WebResource;
import modules.Drone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;

/*
 *
 * Class that implements a thread to manage standard input of the drone
 *
 */

public class DronesInput extends Thread{
    private static final String RESTServerAddress = "http://localhost:1337";

    private Drone drone;

    public DronesInput(Drone drone){
        this.drone = drone;
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

                    if(drone.isMaster()){
                        drone.stopSubscriberMQTT();
                        if(drone.ordersQueue.size()>0) {
                            System.out.println("> Assigning pending orders...");
                            for (Drone d : drone.getDronesList()) {
                                if(drone.getId() != d.getId()) {
                                    AssignPendingDeliveries apd = new AssignPendingDeliveries(drone, d.getIp());
                                    apd.start();
                                }
                            }
                        }
                    }

                    Client client = Client.create();
                    System.out.println("> Removing the drone from the network...");
                    //Remove a drone to the REST Server
                    String deletePath = "/drones/" + drone.getId();
                    WebResource webResource = client.resource(RESTServerAddress + deletePath);
                    try {
                        webResource.type("application/json").delete();

                    } catch (ClientHandlerException e) {
                        System.out.println("> Server not reachable");
                    }
                    System.out.println("> Drone removed...");
                    System.exit(0);

                } else if (choice.equals("recharge")) {
                    if(!drone.isInDelivery()) {
                        Timestamp now = new Timestamp(System.currentTimeMillis());
                        drone.setWantRecharge(String.valueOf(now.getTime()));
                        int oldBatteryLevel = drone.getBatteryLevel();
                        RechargingThread rechargingThread = new RechargingThread(drone);
                        rechargingThread.start();

                        new java.util.Timer().schedule(
                                new java.util.TimerTask() {
                                    @Override
                                    public void run() {
                                        //if after some time drone still wants recharging maybe the recharge is stuck
                                        if (!drone.isRecharging() && drone.getWantRecharge() != null && oldBatteryLevel == drone.getBatteryLevel()) {
                                            //retrying
                                            System.err.println("! Recharge is stuck. Retrying ...");
                                            RechargingThread rechargingThread = new RechargingThread(drone);
                                            rechargingThread.start();
                                        }
                                    }
                                },
                                (drone.getDronesList().size() - 1) * 11000
                        );
                    } else {
                        System.err.println("! Can't recharge at the moment. Drone is in delivery");
                    }

                } else {
                    System.out.println("> Unknown command");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
