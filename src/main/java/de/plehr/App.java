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

   @Autowired
   Runnable MessageListener;

   public static void main(String[] args) {
      try {
         System.getenv("MQTT_BROKER_URL");
         Integer.parseInt(System.getenv("MQTT_QOS"));
         Boolean.parseBoolean(System.getenv("MQTT_BROKER_URL")); /* By default SSL is disabled */
         Integer.parseInt(System.getenv("MQTT_BROKER_PORT"));
         System.getenv("MQTT_USER");
         System.getenv("MQTT_PASS");
      } catch (Exception ex) {
         System.err.println("Invalid enviroment variables");
      }
      SpringApplication.run(App.class, args);
   }

   @Override
   protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
      return application.sources(App.class);
   }

   @Bean
   public CommandLineRunner schedulingRunner(TaskExecutor executor) {
      return new CommandLineRunner() {
         public void run(String... args) throws Exception {
            executor.execute(MessageListener);
         }
      };
   }
}
