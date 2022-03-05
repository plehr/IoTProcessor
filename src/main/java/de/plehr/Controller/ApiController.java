package de.plehr.Controller;

import java.util.List;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.plehr.Repository.EntryRepository;
import de.plehr.Model.DataEntry;
//import de.plehr.Mqtt.MqttCommunicator;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    EntryRepository entryRepository;

    @RequestMapping("/values")
    public List<DataEntry> getValues() {
        return entryRepository.findAll();
    }

    @RequestMapping("/set")
    String setCommand() throws MqttPersistenceException, MqttException {
      //  new MqttCommunicator().sendMessage("helloooo", "Pascal");
        return "";
    }

}
