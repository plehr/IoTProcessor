// Example testing sketch for various DHT humidity/temperature sensors written by ladyada
// REQUIRES the following Arduino libraries:
// - DHT Sensor Library: https://github.com/adafruit/DHT-sensor-library
// - Adafruit Unified Sensor Lib: https://github.com/adafruit/Adafruit_Sensor

#include "DHT.h"
#include <WiFi.h>
#include <PubSubClient.h>
#include <Wire.h>

#define DHTPIN 23     // Digital pin connected to the DHT sensor
// Feather HUZZAH ESP8266 note: use pins 3, 4, 5, 12, 13 or 14 --
// Pin 15 can work but DHT must be disconnected during program upload.

// Uncomment whatever type you're using!
#define DHTTYPE DHT11   // DHT 11

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
  // We start by connecting to a WiFi network
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
  // Loop until we're reconnected
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Attempt to connect
    if (client.connect("ESP", mqtt_user,mqtt_pass)) {
      Serial.println("connected");
      // Subscribe
      client.subscribe("00:00:00:00:00:00/output");
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
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

  // Feel free to add more if statements to control more GPIOs with MQTT

  // If a message is received on the topic esp32/output, you check if the message is either "on" or "off".
  // Changes the output state according to the message
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

  // Wait a few seconds between measurements.
  delay(2000);

  // Reading temperature or humidity takes about 250 milliseconds!
  // Sensor readings may also be up to 2 seconds 'old' (its a very slow sensor)
  float humidity = dht.readHumidity();
  // Read temperature as Celsius (the default)
  float temperature = dht.readTemperature();
  // Read temperature as Fahrenheit (isFahrenheit = true)
  float f = dht.readTemperature(true);


  // Check if any reads failed and exit early (to try again).
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
      float averageTemp; 
      for(int i = 0; i < 10; i++) {
          averageTemp += temperatureAvg[i];
      }
      averageTemp = averageTemp / 10;

      float averageHumidity; 
      for(int i = 0; i < 10; i++) {
          averageHumidity += humidityAvg[i];
      }
      averageHumidity = averageHumidity / 10;

      float averageHeatIndex; 
      for(int i = 0; i < 10; i++) {
          averageHeatIndex += heatIndexAvg[i];
      }
      averageHeatIndex = averageHeatIndex / 10;

      char tempString[8];
      dtostrf(averageTemp, 1, 2, tempString);
      Serial.print("Temperature: ");
      Serial.println(tempString);
      client.publish("00:00:00:00:00:00/temperature", tempString);

      // humidity
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
    
      averageTemp = 0;
      averageHumidity = 0;
      averageHeatIndex = 0;
  }
}
