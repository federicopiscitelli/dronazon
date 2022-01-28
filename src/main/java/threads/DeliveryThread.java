package threads;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.WebResource;
import modules.Drone;
import modules.Position;

import java.sql.Timestamp;

public class DeliveryThread extends Thread{
    private static final String RESTServerAddress = "http://localhost:1337";
    Drone drone;
    Position retire;
    Position delivery;

    public DeliveryThread(Drone drone, Position retire, Position delivery){
        this.drone = drone;
        this.retire = retire;
        this.delivery = delivery;
    }

    public void run(){
        System.out.println("> Doing the delivery ...");

        drone.setInDelivery(true);

        try {
            Thread.sleep(5000);

            //Timestamp of arrival to delivery place
            Timestamp arriveTime = new Timestamp(System.currentTimeMillis());

            //New battery level calculated after the delivery
            int newBatteryLevel = drone.getBatteryLevel()-10;
            drone.setBatteryLevel(newBatteryLevel);

            //Total distance traveled
            double distanceFromRetire = Math.sqrt(
                    Math.pow(drone.getPosition().getX()-retire.getX(),2) +
                    Math.pow(drone.getPosition().getY()-retire.getY(),2)
            );
            double distanceDelivery = Math.sqrt(
                    Math.pow(delivery.getX()-retire.getX(),2) +
                    Math.pow(delivery.getY()-retire.getY(),2)
            );
            double totalKm = distanceFromRetire + distanceDelivery;

            System.out.println("> Order delivered with success" +
                    "\n\t Battery: "+drone.getBatteryLevel()+
                    "\n\t Total KM: "+totalKm);
            //AVG of pollution measured

            if(newBatteryLevel < 15){
                Client client = Client.create();
                System.out.println("> Battery is under 15%. Removing the drone from the network...");
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
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        drone.setInDelivery(false);

    }
}
