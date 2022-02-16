package MQTT;

import com.google.gson.Gson;
import modules.Drone;
import modules.Order;
import org.eclipse.paho.client.mqttv3.*;
import threads.AssignDelivery;

import java.sql.Timestamp;


public class DroneSubscriber extends Thread{

    public static final String BROKER = "tcp://localhost:1883";
    public static final String TOPIC = "dronazon/smartcity/orders";
    public static final int QOS = 2;

    private Drone drone;
    private MqttClient client;
    private String clientId ;


    public DroneSubscriber(Drone drone){
        this.drone = drone;
        clientId = MqttClient.generateClientId();
    }

    public void run() {

        //Waiting a certain amount of time before connecting
        try {
            Thread.sleep(drone.getDronesList().size()* 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            client = new MqttClient(BROKER, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            // Connect the client
            System.out.println("> "+clientId + " Connecting Broker " + BROKER);
            client.connect(connOpts);
            //System.out.println(clientId + " Connected - Thread PID: " + Thread.currentThread().getId());

            Gson gson = new Gson();

            // Callback
            client.setCallback(new MqttCallback() {

                public void messageArrived(String topic, MqttMessage message) {
                    // Called when a message arrives from the server that matches any subscription made by the client
                    String time = new Timestamp(System.currentTimeMillis()).toString();
                    String receivedMessage = new String(message.getPayload());
                    Order newOrder = gson.fromJson(receivedMessage,Order.class);
                    AssignDelivery assignDelivery = new AssignDelivery(drone,newOrder,false);
                    assignDelivery.start();
                }

                public void connectionLost(Throwable cause) {
                    System.out.println(">" + clientId + " Connection lost! cause:" + cause.getMessage() + " " + cause.getCause().toString()+ " -  Thread PID: " + Thread.currentThread().getId());
                }

                public void deliveryComplete(IMqttDeliveryToken token) { }

            });
            System.out.println("> " + clientId + " Subscribing ... - Thread PID: " + Thread.currentThread().getId());
            client.subscribe(TOPIC, QOS);
            System.out.println("> " + clientId + " Subscribed to topics : " + TOPIC);



        } catch (MqttException me) {
            System.out.println("> Reason: " + me.getReasonCode());
            System.out.println("> Msg: " + me.getMessage());
            System.out.println("> Loc: " + me.getLocalizedMessage());
            System.out.println("> Cause: " + me.getCause());
            System.out.println("> Excep: " + me);
            me.printStackTrace();
        }
    }

    public void stopExecution(){
        if(client.isConnected()){
            try {
                client.disconnectForcibly();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }


}
