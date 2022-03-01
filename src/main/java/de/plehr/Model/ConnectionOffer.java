package de.plehr.Model;

import java.net.URI;
import java.net.URISyntaxException;

public class ConnectionOffer {
    public final String connectUrl;
    public final String connectUrlSecure;
    public final static String username=System.getenv("STACKHERO_MOSQUITTO_USER_LOGIN");
    public final static String password=System.getenv("STACKHERO_MOSQUITTO_USER_PASSWORD");

    public ConnectionOffer() {
        this.connectUrl=System.getenv("STACKHERO_MOSQUITTO_URL_CLEAR");
        this.connectUrlSecure=System.getenv("STACKHERO_MOSQUITTO_URL_TLS");
    }

    public String getHostname() throws URISyntaxException{
        return new URI(connectUrl).getHost();
    }

    public int getPort() throws URISyntaxException{
        return new URI(connectUrl).getPort();
    }
}
