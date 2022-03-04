package de.plehr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;

@SpringBootApplication
public class App extends SpringBootServletInitializer {

   public static void main(String[] args) {
      try {
         System.getenv("CLEARDB_DATABASE_URL");
         System.getenv("STACKHERO_MOSQUITTO_URL_CLEAR");
         System.getenv("STACKHERO_MOSQUITTO_URL_TLS");
         System.getenv("STACKHERO_MOSQUITTO_USER_LOGIN");
         System.getenv("STACKHERO_MOSQUITTO_USER_PASSWORD");
      } catch (Exception ex) {
         System.err.println("Invalid enviroment variables");
      }
      SpringApplication.run(App.class, args);
   }

   @Override
   protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
      return application.sources(App.class);
   }
}
