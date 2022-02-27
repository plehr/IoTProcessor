package de.plehr.Controller;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.plehr.MqttCommunicator;

@RestController
@RequestMapping("/api")
public class ApiController {

    @RequestMapping("/values")
    String getValues() throws MqttPersistenceException, MqttException {
       // new MqttCommunicator().sendMessage("helloooo", "Pascal");
        return "hello";
    }
    
}
