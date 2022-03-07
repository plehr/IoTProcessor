package de.plehr.Controller;

import java.util.List;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.plehr.Repository.EntryRepository;
import de.plehr.Model.DataEntry;
import de.plehr.Mqtt.MqttCommunicator;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    EntryRepository entryRepository;

    @RequestMapping(value="/values", method=RequestMethod.GET)
    public List<DataEntry> getValues() {
        return entryRepository.findAll();
    }

    @RequestMapping(value="/source/{source}", method=RequestMethod.GET)
    public List<DataEntry> getSourceFilterValues(@PathVariable("source") String source) {
        return entryRepository.findBySource(source);
    }

    @RequestMapping(value="/topic/{topic}", method=RequestMethod.GET)
    public List<DataEntry> getTopicFilterValues(@PathVariable("topic") String topic) {
        return entryRepository.findByTopic(topic);
    }

    @RequestMapping(value="/source/{source}/interval/{interval}", method = RequestMethod.POST)
    String setInterval(@PathVariable("source") String source,@PathVariable("interval") int interval) throws MqttPersistenceException, MqttException {
        new MqttCommunicator().sendMessage(source+"/setInterval", interval+"");
        return "";
    }
}
