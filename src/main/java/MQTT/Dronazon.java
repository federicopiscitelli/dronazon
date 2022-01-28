package MQTT;

import com.google.gson.Gson;
import com.sun.org.apache.xpath.internal.operations.Or;
import modules.Order;
import modules.Position;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Dronazon {
    public static void main(String args[]) throws InterruptedException {
        MqttClient client;
        String broker = "tcp://localhost:1883";
        String clientId = MqttClient.generateClientId();
        String topic = "dronazon/smartcity/orders";
        int qos = 2; //deliver exactly one time
        int orderId = 0;
        Gson gson = new Gson();

        //brew REST.services.services start mosquitto
        try {
            client = new MqttClient(broker, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            // Connect the REST.REST.REST.client.client
            System.out.println(">" + clientId + " Connecting Broker " + broker);
            client.connect(connOpts);
            System.out.println(">" + clientId + " Connected");

           while(true) {

               int xR = (int) (0 + (Math.random() * 10)); //generate random x for retire position
               int yR = (int) (0 + (Math.random() * 10)); //generate random y for retire position
               int xD = (int) (0 + (Math.random() * 10)); //generate random x for delivery position
               int yD = (int) (0 + (Math.random() * 10)); //generate random y for delivery position

               Position retire = new Position(xR, yR);
               Position delivery = new Position(xD, yD);

               Order order = new Order(orderId, retire, delivery);
               String jsonOrder = gson.toJson(order);
               MqttMessage newOrderMessage = new MqttMessage(jsonOrder.getBytes());

               // Set the QoS on the Message
               newOrderMessage.setQos(qos);
               System.out.println(">" + clientId + " Publishing order: " + jsonOrder + " ...");
               client.publish(topic, newOrderMessage);
               orderId++;

               Thread.sleep(10000);
           }

        } catch (MqttException me) {
            System.out.println("> Reason: " + me.getReasonCode());
            System.out.println("> Msg: " + me.getMessage());
            System.out.println("> Loc: " + me.getLocalizedMessage());
            System.out.println("> Cause: " + me.getCause());
            System.out.println("> Excep: " + me);
            me.printStackTrace();
        }

    }

}
