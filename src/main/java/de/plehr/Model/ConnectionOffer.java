package de.plehr.Model;

public class ConnectionOffer {
    public final String hostname;
    public final int port;
    public final boolean ssl;

    public ConnectionOffer() {
        this.hostname = System.getenv("MQTT_BROKER_URL");
        this.port = Integer.parseInt(System.getenv("MQTT_BROKER_PORT"));
        this.ssl = Boolean.parseBoolean(System.getenv("MQTT_SSL"));
    }
}
