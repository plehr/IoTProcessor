package de.plehr.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import de.plehr.Repository.EntryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class FrontendController {

    @Autowired
    private EntryRepository entryRepository;

    @GetMapping("/")
    String firstPage(Model m) {
        m.addAttribute("hello", "Helloo World!");
        return "firstPage";
    }

    @GetMapping("/values")
    String getValues(Model m) {
        m.addAttribute("valuelist", entryRepository.findAll());
        return "values";
    }

    @GetMapping("/topic/{topic}")
    String getValuesByTopic(Model m, @PathVariable String topic) {
        m.addAttribute("valuelist", entryRepository.findByTopic(topic));
        m.addAttribute("filtertype", "topic");
        m.addAttribute("filtervalue", topic);
        return "values";
    }

    @GetMapping("/source/{source}")
    String getValuesBySensor(Model m, @PathVariable String source) {
        m.addAttribute("valuelist", entryRepository.findBySource(source));
        m.addAttribute("filtertype", "source");
        m.addAttribute("filtervalue", source);
        return "values";
    }
}
