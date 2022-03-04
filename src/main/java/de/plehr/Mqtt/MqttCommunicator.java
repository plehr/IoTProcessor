package de.plehr.Mqtt;

import java.sql.Timestamp;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.plehr.Model.ConnectionOffer;
import de.plehr.Model.DataEntry;
import de.plehr.Repository.EntryRepository;

@Component
public class MqttCommunicator extends MqttConfig implements MqttCallback {

    @Autowired
    EntryRepository entryRepository;

    private String brokerUrl = null;
    final private String clientId = "Server " + UUID.randomUUID().toString();

    private MqttClient mqttClient = null;
    private MqttConnectOptions connectionOptions = null;
    private MemoryPersistence persistence = null;

    public MqttCommunicator() {
        System.out.println("MQTT configuration loaded");
        this.config();
    }

    public MqttCommunicator(String topic) {
        this();
        subscribeMessage(topic);
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("MQTT connection Lost" + cause);
        this.config();
    }

    @Override
    protected void config(String broker, Integer port, Boolean ssl, Boolean withUserNamePass) {
        System.out.println("MQTT inside load parameter");

        this.persistence = new MemoryPersistence();
        this.connectionOptions = new MqttConnectOptions();

        try {
            this.mqttClient = new MqttClient(new ConnectionOffer().connectUrl, clientId, persistence);
            this.connectionOptions.setCleanSession(true);
            this.connectionOptions.setPassword(ConnectionOffer.password.toCharArray());
            this.connectionOptions.setUserName(ConnectionOffer.username);
            this.mqttClient.connect(this.connectionOptions);
            this.mqttClient.setCallback(this);
        } catch (MqttException me) {
            throw new RuntimeException("MQTT not Connected");
        }
    }

    @Override
    protected void config() {
        System.out.println("MQTT inside config with parameter");
        this.brokerUrl = new ConnectionOffer().connectUrl;
        this.persistence = new MemoryPersistence();
        this.connectionOptions = new MqttConnectOptions();
        try {
            this.mqttClient = new MqttClient(brokerUrl, clientId, persistence);
            this.connectionOptions.setCleanSession(true);
            this.connectionOptions.setPassword(ConnectionOffer.password.toCharArray());
            this.connectionOptions.setUserName(ConnectionOffer.username);
            this.mqttClient.connect(this.connectionOptions);
            this.mqttClient.setCallback(this);
        } catch (MqttException me) {
            throw new RuntimeException("MQTT not Connected");
        }
    }

    @Override
    public void subscribeMessage(String topic) {
        try {
            this.mqttClient.subscribe(topic, this.qos);
        } catch (MqttException me) {
            System.out.println("MQTT not able to read topic  " + topic + "( " + me.getMessage() + ")");
        }
    }

    public void sendMessage(String message, String topic) throws MqttPersistenceException, MqttException {
        System.out.println("Publishing message: " + message);
        MqttMessage m = new MqttMessage(message.getBytes());
        m.setQos(this.qos);
        this.mqttClient.publish(topic, m);
    }

    @Override
    public void disconnect() {
        try {
            this.mqttClient.disconnect();
        } catch (MqttException me) {
            System.out.println("MQTT ERROR" + me);
        }
    }

    @Override
    public void messageArrived(String mqttTopic, MqttMessage mqttMessage) throws Exception {
        String time = new Timestamp(System.currentTimeMillis()).toString();
        String message = new String(mqttMessage.getPayload());
        String source = mqttTopic.substring(0, mqttTopic.indexOf("/"));
        String topic = mqttTopic.substring(mqttTopic.indexOf("/") + 1, mqttTopic.length());

        System.out.println("***********************************************************************");
        System.out.println("MQTT Message Arrived at Time: " + time + "  Topic: " + mqttTopic + "  Message: "
                + message);
        entryRepository.save(new DataEntry(source, topic, message, Timestamp.valueOf(time)));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}