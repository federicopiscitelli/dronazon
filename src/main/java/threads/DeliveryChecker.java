package threads;

import modules.Drone;
import modules.Order;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Map;

public class DeliveryChecker extends Thread{
    private Drone drone;
    private boolean stop;

    public DeliveryChecker(Drone drone){
        this.drone = drone;
        stop = false;
    }

    public void run(){
        while(!stop){
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(drone.getOrdersInDelivery().size()>0){
                Map<Long, Order> map = drone.getOrdersInDelivery();
                Iterator it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<Long, Order> entry = (Map.Entry<Long, Order>) it.next();
                    if((System.currentTimeMillis() - entry.getKey()) >= 10000){
                        System.err.println("! "+(System.currentTimeMillis() - entry.getKey())+" Delivery of order "+entry.getValue().getId()+" was not completed. Putting in queue...");
                        drone.ordersQueue.putOrder(entry.getValue());
                        drone.removeOrderInDeliveryById(entry.getValue().getId());
                    }
                }
            }
        }
    }

    public void stopExecution(){
        stop = true;
    }
}
