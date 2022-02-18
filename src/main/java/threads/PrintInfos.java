package threads;

import modules.Drone;

public class PrintInfos extends Thread{
    private Drone drone;
    private boolean stop;

    public PrintInfos(Drone drone){
        this.drone = drone;
        this.stop = false;
    }

    public void run(){
        while(!stop){
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //System.out.println("> INFORMATIONS -> Deliveries: "+drone.getnDelivery()+". Distance: "+drone.getTotKm()+"km. Battery: "+drone.getBatteryLevel()+"%.");
        }
    }

    public void stopExecution(){
        this.stop = true;
    }
}
