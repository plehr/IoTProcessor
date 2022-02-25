package de.plehr;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import de.plehr.Model.ConnectionOffer;

public abstract class MqttConfig {
    protected final String broker = new ConnectionOffer().hostname;
    protected final int qos = Integer.parseInt(System.getenv("MQTT_QOS"));
    protected Boolean hasSSL = new ConnectionOffer().ssl;
    protected Integer port = new ConnectionOffer().port;
    protected final String userName = System.getenv("MQTT_USER");
    protected final String password = System.getenv("MQTT_PASS");
    protected final String TCP = "tcp://";
    protected final String SSL = "ssl://";

    /**
     * Custom Configuration
     * 
     * @param broker
     * @param port
     * @param ssl
     * @param withUserNamePass
     */
    protected abstract void config(String broker, Integer port, Boolean ssl, Boolean withUserNamePass);

    /**
     * Default Configuration
     */
    protected abstract void config();

    public abstract void subscribeMessage(String topic);

    public abstract void disconnect();

    public abstract void messageArrived(String mqttTopic, MqttMessage mqttMessage) throws Exception;

}