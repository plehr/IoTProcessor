package de.plehr.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

@Controller
public class HelloController {

    @GetMapping("/")
    String hello(Model m) {
        m.addAttribute("hello", "Helloo World!");
        return "hello";
    }

}
