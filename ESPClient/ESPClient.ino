#include "DHT.h"
#include <WiFi.h>
#include <PubSubClient.h>
#include <Wire.h>

#define DHTPIN 23


#define DHTTYPE DHT11

const char* ssid = "";
const char* password = "";

const char* mqtt_server = "HOST";
const char* mqtt_user = "00:00:00:00:00:00";
const char* mqtt_pass = "egal :)";

DHT dht(DHTPIN, DHTTYPE);

WiFiClient espClient;
PubSubClient client(espClient);
long lastMsg = 0;
char msg[50];
int value = 0;

float temperatureAvg[10];
float humidityAvg[10];
float heatIndexAvg[10];
int runIndex = 0;

void setup() {
  Serial.begin(9600);
  Serial.println(F("DHTxx test!"));

 setup_wifi();
  client.setServer(mqtt_server, 1883);
  client.setCallback(callback);

  Serial.println(F("DHTxx test!"));

  dht.begin();
}

void setup_wifi() {
  delay(10);
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}

void reconnect() {
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");

    if (client.connect("ESP", mqtt_user,mqtt_pass)) {
      Serial.println("connected");
      client.subscribe("00:00:00:00:00:00/output");
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      delay(5000);
    }
  }
}

void callback(char* topic, byte* message, unsigned int length) {
  Serial.print("Message arrived on topic: ");
  Serial.print(topic);
  Serial.print(". Message: ");
  String messageTemp;

  for (int i = 0; i < length; i++) {
    Serial.print((char)message[i]);
    messageTemp += (char)message[i];
  }
  Serial.println();

  if (String(topic) == "esp32/output") {
    Serial.print("Changing output to ");
    if(messageTemp == "on"){
      Serial.println("on");
    }
    else if(messageTemp == "off"){
      Serial.println("off");
    }
  }
}

void loop() {
  if (!client.connected()) {
    reconnect();
  }
  client.loop();

  delay(2000);

  float humidity = dht.readHumidity();
  // Read temperature as Celsius
  float temperature = dht.readTemperature();
  // Read temperature as Fahrenheit
  float f = dht.readTemperature(true);

  if (isnan(humidity) || isnan(temperature) || isnan(f)) {
    Serial.println(F("Failed to read from DHT sensor!"));
    return;
  }

  // Compute heat index in Fahrenheit (the default)
  float hif = dht.computeHeatIndex(f, humidity);
  // Compute heat index in Celsius (isFahreheit = false)
  float hic = dht.computeHeatIndex(temperature, humidity, false);

  //calculate average
  temperatureAvg[runIndex] = temperature;
  humidityAvg[runIndex] = humidity;
  heatIndexAvg[runIndex] = hic;
  
  runIndex += 1;

  if (runIndex > 9) {
      runIndex  = 0;
      float averageTemp=0; 
      for(int i = 0; i < 10; i++) {
          averageTemp += temperatureAvg[i];
      }
      averageTemp = averageTemp / 10;

      float averageHumidity=0; 
      for(int i = 0; i < 10; i++) {
          averageHumidity += humidityAvg[i];
      }
      averageHumidity = averageHumidity / 10;

      float averageHeatIndex=0; 
      for(int i = 0; i < 10; i++) {
          averageHeatIndex += heatIndexAvg[i];
      }
      averageHeatIndex = averageHeatIndex / 10;

      // Temperature
      char tempString[8];
      dtostrf(averageTemp, 1, 2, tempString);
      Serial.print("Temperature: ");
      Serial.println(tempString);
      client.publish("00:00:00:00:00:00/temperature", tempString);

      // Humidity
      char humString[8];
      dtostrf(averageHumidity, 1, 2, humString);
      Serial.print("Humidity: ");
      Serial.println(humString);
      client.publish("00:00:00:00:00:00/humidity", humString);

      // HeatIndex
      char hifString[8];
      dtostrf(averageHeatIndex, 1, 2, hifString);
      Serial.print("HeatIndex: ");
      Serial.println(hifString);
      client.publish("00:00:00:00:00:00/heatindex", hifString);
  }
}