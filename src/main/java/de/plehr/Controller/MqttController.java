package de.plehr.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.plehr.Model.ConnectionOffer;
import de.plehr.Model.MqttUser;

/**
 * Controller for the communication with the MQTT broker.
 */
@RestController
@RequestMapping("/mqtt")
public class MqttController {

  @GetMapping()
  public ConnectionOffer getEndpoint() {
    return new ConnectionOffer();
  }

  /**
   * Allow user to connect to the MQTT broker.
   * 
   * @param person
   */
  @PostMapping(value = "/user", consumes = "application/json", produces = "application/json")
  public void reqisterUser(@RequestBody MqttUser person) {
    System.out.println("Mqtt User-Request" + person);
  }

  /**
   * Check if the user is permit to use admin previleges.
   * 
   * @param person
   */
  @PostMapping(value = "/superuser", consumes = "application/json", produces = "application/json")
  public ResponseEntity<String> registerAdmin(@RequestBody MqttUser person) {
    System.out.println("Mqtt Admin-Request: " + person);
    if (!person.isAdmin())
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not allowed\n");
    return ResponseEntity.status(HttpStatus.valueOf(200)).body("OK\n");

  }

  /**
   * Check the requested action and return permit.
   * 
   * @param person
   */
  @PostMapping(value = "/acl", consumes = "application/json", produces = "application/json")
  public ResponseEntity<String> checkAcl(@RequestBody MqttUser person) {
    System.out.println("Mqtt Acl-Request: " + person + " -> " + (person.canWrite()));
    if (!person.canWrite()) 
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not allowed\n");
    return ResponseEntity.status(HttpStatus.valueOf(200)).body("OK\n");
  }
}
