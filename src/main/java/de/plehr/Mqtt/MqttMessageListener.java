package de.plehr.Mqtt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MqttMessageListener implements Runnable {
    @Autowired
    MqttCommunicator subscriber;

    @Override
    public void run() {
        System.out.println("Mqtt listener active");
            subscriber.subscribeMessage("#");
    }
}