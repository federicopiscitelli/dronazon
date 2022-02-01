package threads;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import modules.DeliveryStatistics;
import modules.Drone;
import modules.Stat;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComputeStatistics extends Thread{
    private static final String RESTServerAddress = "http://localhost:1337";

    private Drone drone;

    public ComputeStatistics(Drone drone){
        this.drone = drone;
    }

    public void run(){
        while(true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("> Computing global statistics ...");

            //int networkSize = drone.getDronesList().size();
            double deliveryAvg = 0.0d;
            double kmAvg = 0.0d;
            double pollutionAvg = 0.0d;
            double batteryAvg = 0.0d;

            List<DeliveryStatistics> deliveryStatistics = drone.getDeliveryStatistics();
            Map<Integer,Integer> deliveryCounter = new HashMap<>();

            if(deliveryStatistics.size()>0) {
                //foreach statistic in statistic's list
                for (DeliveryStatistics statistic : deliveryStatistics) {
                    //counting the deliveris for each drone
                    if (deliveryCounter.containsKey(statistic.getIdDrone())) {
                        deliveryCounter.put(statistic.getIdDrone(), (deliveryCounter.get(statistic.getIdDrone()) + 1));
                    } else {
                        deliveryCounter.put(statistic.getIdDrone(), 1);
                    }
                    kmAvg += statistic.getKm();
                    pollutionAvg += statistic.getPollutionAvg();
                    batteryAvg += statistic.getBattery();
                }

                int totDeliveries = 0;
                for (Map.Entry<Integer, Integer> entry : deliveryCounter.entrySet()) {
                    totDeliveries += entry.getValue();
                }

                deliveryAvg = totDeliveries / deliveryCounter.size();
                kmAvg /= deliveryCounter.size();
                pollutionAvg /= deliveryCounter.size();
                batteryAvg /= deliveryCounter.size();

                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                Stat stat = new Stat(timestamp, deliveryAvg, kmAvg, pollutionAvg, batteryAvg);

                System.out.println(timestamp);
                //REST call to the server
                Client client = Client.create();

                //Adding a statistic to the REST Server
                String postPath = "/stats/add";
                WebResource webResource = client.resource(RESTServerAddress + postPath);
                String input = new GsonBuilder()
                                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                                    .create()
                                    .toJson(stat);

                System.out.println(input);
                try {
                    webResource.type("application/json").post(ClientResponse.class, input);
                } catch (ClientHandlerException e) {
                    System.out.println("> Server not reachable");
                }
            }

            drone.emptyGlobalStatistic();

        }

    }

}
