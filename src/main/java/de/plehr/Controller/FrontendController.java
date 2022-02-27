package de.plehr.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.stereotype.Controller;

@Controller
public class FrontendController {

    @GetMapping("/")
    String firstPage(Model m) {
        m.addAttribute("hello", "Helloo World!");
        return "firstPage";
    }
}
