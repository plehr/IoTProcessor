package de.plehr.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @RequestMapping("/api")
    String hello() {
        return "hello";
    }
    
}
