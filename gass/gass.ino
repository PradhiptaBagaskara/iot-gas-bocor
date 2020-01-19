#include <ESP8266WiFi.h>
#include <Servo.h>
#include <FirebaseArduino.h>

#define servoPin 12 //D6
#define relayPin 4 //D2
#define FIREBASE_HOST "iot-gass.firebaseio.com"
#define FIREBASE_AUTH "2VTgaMObtRDwtL2BZ85o9TZ5sjE07CIVHyqECVlK"


const char *ssid = "WIFI.HOME"; // replace with your wifi ssid and wpa2 key
const char *pass = "esjerukseger";

WiFiClient client;
Servo servo; //instans



void setup()
{
Serial.begin(115200);
delay(10);
pinMode(relayPin, OUTPUT); 
Serial.println("Connecting to ");
Serial.println(ssid);
WiFi.begin(ssid, pass);
while (WiFi.status() != WL_CONNECTED)
{
delay(500);
Serial.print(".");
}
Serial.println("");
Serial.println("WiFi connected");
Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
servo.attach(servoPin); //D6
servo.write(0);
digitalWrite(relayPin, LOW);
}

void loop() {
  // put your main code here, to run repeatedly:
float h = analogRead(A0); // pin A0
int servoStatus = servo.read();
int gassStatus = 0;
if (!isnan(h))
{
//Serial.println("Failed to read from MQ-5 sensor!");
gassStatus = h/1024*100;
}
Serial.print("status servo sebelum : ");
Serial.println(servoStatus);
Serial.print("status gas sebelum : ");
Serial.println(gassStatus);
if(gassStatus >= 50){
  servo.write(180);
  if(servoStatus >= 170){
    Firebase.setString("status_pompa", "ON");
     digitalWrite(relayPin, HIGH);
     Serial.println("status-pompa: on");
     Serial.print("status gas : ");
     Serial.println(gassStatus);
  }
}else{
  servo.write(0);
  digitalWrite(relayPin, LOW);
  Firebase.setString("status_pompa", "OFF");
  Serial.println("status-pompa: off");
  
  }


  
Firebase.setInt("nilai_gass", gassStatus);
Firebase.setInt("gasensor", gassStatus);

delay(2000);
}
