#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <SoftwareSerial.h>
#include <FirebaseArduino.h>
#include <ArduinoJson.h>
#include <ESP8266HTTPClient.h>
// required libraries
// Set these to run example.
#define FIREBASE_HOST "piralarm-c1941-default-rtdb.firebaseio.com" //please remove http and / from firebase url
#define FIREBASE_AUTH "fvIposhKud5M5JYQ6JjkSmLjKk9BtMxkW1MoWkE9" // paste secret key here
#define WIFI_SSID "Ishaq"
#define WIFI_PASSWORD "ugpq6872"


const char* mqtt_server = "broker.hivemq.com";

WiFiClient espClient;
PubSubClient client(espClient);
long lastMsg = 0;
char msg[50];
int value = 0;
int dataspeed = 5000;


String myString; // sending integer data as string ,sensor data can also be send as integer
String fireStatus="";
String motion = "detected";   // string which stores state - motion/no motion 
String nomotion="nomotion";
String light="ON";
int sdata = 0; // PIR sensor  value will be stored in sdata.
int buzzer = 12;  // Digital pin D6 buzzer 
int sensor = 13;  // Digital pin D7
int relaypin = 14; // Digital pin D5 relay 

void setup() {
Serial.begin(9600);
  pinMode(sensor, INPUT);   // declare PIR sensor as input
  pinMode(buzzer, OUTPUT);  // declare Speaker as output
  pinMode(relaypin,OUTPUT); // declare Relay as output 
  int val = 0;   
WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");
  while (WiFi.status() != WL_CONNECTED)
      {
    Serial.print(".");
    delay(500);
      }
  Serial.println();
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());
  //setup for the client
  client.setServer(mqtt_server, 1883);
  client.setCallback(callback);
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
 
}

//Callback for the phone app
void callback(char* topic, byte* payload, unsigned int length) {
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");
  for (int i = 0; i < length; i++) {
    Serial.print((char)payload[i]);
  }
  if ((char)payload[0] == '1'){
    Serial.println("Sprinkler On");
    digitalWrite (relaypin, HIGH);  
  }
  if ((char)payload[0] == '2'){
    Serial.println("the user typed a 2");
    tone(buzzer,350);
    delay(1000);
    noTone(buzzer);
    //Firebase.pushString("Lights",light);
  }

  if ((char)payload[0] == '3'){
    Serial.println("Lights On");
    digitalWrite (relaypin, HIGH);
    //Firebase.pushString("Lights",light);
  }
  
  
  
}


void reconnect() {
  // Loop until we're reconnected
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Create a random client ID
    String clientId = "ESP8266Client-";
    clientId += String(random(0xffff), HEX);
    // Attempt to connect
    if (client.connect(clientId.c_str())) {
      Serial.println("connected");
      // Once connected, publish an announcement...
      client.publish("outTopic", "hello world");
      // ... and resubscribe
      client.subscribe("AT2SKLM4F/MobileApp");
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
      delay(5000);
    }
  }
}



void loop() {
sdata = digitalRead(sensor);
myString = String(sdata); 
Serial.println(myString);//printing string to verify whether sensor is working or not  
//Firebase.setString("Variable/Value",myString);//A string will be sent to real time database under variable /value (user need not create this in advance)
delay(1000); 
    long state = digitalRead(sensor);
    if(state == HIGH) {
     // tone(buzzer,350);
     //digitalWrite (relaypin, HIGH);
     // Serial.println("detected!");
      delay(3000); // wait for 3000 ms or 3s
      //noTone(buzzer);
    }
    else {
     // noTone(buzzer);
      //digitalWrite (relaypin, LOW);
      //Serial.println("absent!");
      delay(1000); // wait for 1000 ms or 1s
      }
  //sending messages
   if (!client.connected()) {
    reconnect();
  }
  client.loop();

//  long now = millis();
//  if (now - lastMsg > dataspeed) {
//    lastMsg = now;
//    ++value;
//    snprintf (msg, 75, "hello worldpie #%ld", value);
//    Serial.print("Publish message: ");
//    Serial.println(msg);
//    client.publish("Assignment1/testtopic", msg);
//  }
}
