package de.plehr.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.plehr.MqttSubscriberImpl;
import de.plehr.Exception.ForbiddenException;
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
   * @param person
   */
  @PostMapping(value = "/user", consumes = "application/json", produces = "application/json")
  public void reqisterUser(@RequestBody MqttUser person) {
    System.out.println("User-Request" + person);
  }

  /**
   * Check if the user is permit to use admin previleges.
   * @param person
   */
  @PostMapping(value = "/superuser", consumes = "application/json", produces = "application/json")
  public void registerAdmin(@RequestBody MqttUser person) {
    System.out.println("Admin-Request: " + person);
    if (!person.isAdmin())
      throw new ForbiddenException();
  }

  /**
   * Check the requested action and return permit.
   * @param person
   */
  @PostMapping(value = "/acl", consumes = "application/json", produces = "application/json")
  public void checkAcl(@RequestBody MqttUser person) {
    System.out.println("Acl-Request: " + person);
    if (!person.canWrite())
    throw new ForbiddenException();
    if (person.canWrite() && !person.isAdmin())
    {
      MqttSubscriberImpl i = new MqttSubscriberImpl();
        i.subscribeMessage(person.getUsername() + "/#");
    }
  }
}
