package threads;

import modules.Drone;
import modules.Position;

import java.sql.Timestamp;

public class DeliveryThread extends Thread{
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

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        drone.setInDelivery(false);

    }
}
