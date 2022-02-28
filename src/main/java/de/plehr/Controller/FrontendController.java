package de.plehr.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
