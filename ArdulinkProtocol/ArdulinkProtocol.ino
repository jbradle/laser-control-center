/*
  Laser control protocol. Inspired by Luciano Zu project Ardulink http://www.ardulink.org/
*/
#include <Tone.h>

Tone tone1;
Tone tone2;
Tone tone3;

String inputString = "";         // a string to hold incoming data 
boolean stringComplete = false;  // whether the string is complete 

#define digitalPinNum 14

#define laserPin1 9
#define laserPin2 10
#define laserPin3 11

void setup() {
  // initialize serial: (this is general code you can reuse)
  Serial.begin(115200);

  int index = 0;
  // Turn off everything (not on RXTX)
  for (index = 2; index < digitalPinNum; index++) {
    pinMode(index, OUTPUT);
    digitalWrite(index, LOW);
  }


}

void loop() {
  // when a newline arrives:
  if (stringComplete) {

    if (inputString.startsWith("alp://")) { // OK is a message I know

      boolean msgRecognized = true;

      if (inputString.substring(6, 10) == "ppin") { // Power Pin Intensity
        int separatorPosition = inputString.indexOf('/', 11 );
        String pin = inputString.substring(11, separatorPosition);
        String intens = inputString.substring(separatorPosition + 1);
        pinMode(pin.toInt(), OUTPUT);
        analogWrite(pin.toInt(), intens.toInt());
      } else if (inputString.substring(6, 10) == "ppsw") { // Power Pin Switch
        int separatorPosition = inputString.indexOf('/', 11 );
        String pin = inputString.substring(11, separatorPosition);
        String power = inputString.substring(separatorPosition + 1);
        pinMode(pin.toInt(), OUTPUT);
        if (power.toInt() == 1) {
          digitalWrite(pin.toInt(), HIGH);
        } else if (power.toInt() == 0) {
          digitalWrite(pin.toInt(), LOW);
        }
      } else if (inputString.substring(6, 10) == "tone") { // tone request
        int firstSlashPosition = inputString.indexOf('/', 11 );
        int secondSlashPosition = inputString.indexOf('/', firstSlashPosition + 1 );
        int pin = inputString.substring(11, firstSlashPosition).toInt();
        int frequency = inputString.substring(firstSlashPosition + 1, secondSlashPosition).toInt();
        int duration = inputString.substring(secondSlashPosition + 1).toInt();
        if (pin == laserPin1) {
          tone1.begin(pin);
          if (duration == -1) {
            tone1.play(frequency);
          } else {
            tone1.play(frequency, duration);
          }
        }
        if (pin == laserPin2) {
          tone2.begin(pin);
          if (duration == -1) {
            tone2.play(frequency);
          } else {
            tone2.play(frequency, duration);
          }
        }
        if (pin == laserPin3) {
          tone3.begin(pin);
          if (duration == -1) {
            tone3.play(frequency);
          } else {
            tone3.play(frequency, duration);
          }
        }
      } else if (inputString.substring(6, 10) == "notn") { // no tone request
        int firstSlashPosition = inputString.indexOf('/', 11 );
        int pin = inputString.substring(11, firstSlashPosition).toInt();
        softReset();
      } else {
        msgRecognized = false; // this sketch doesn't know other messages in this case command is ko (not ok)
      }

      // Prepare reply message if caller supply a message id (this is general code you can reuse)
      int idPosition = inputString.indexOf("?id=");
      if (idPosition != -1) {
        String id = inputString.substring(idPosition + 4);
        // print the reply
        Serial.print("alp://rply/");
        if (msgRecognized) { // this sketch doesn't know other messages in this case command is ko (not ok)
          Serial.print("ok?id=");
        } else {
          Serial.print("ko?id=");
        }
        Serial.print(id);
        Serial.write(255); // End of Message
        Serial.flush();
      }
    }

    // clear the string:
    inputString = "";
    stringComplete = false;
  }
}

void softReset() {
  asm volatile ("  jmp 0");
}

/*
  SerialEvent occurs whenever a new data comes in the
  hardware serial RX.  This routine is run between each
  time loop() runs, so using delay inside loop can delay
  response.  Multiple bytes of data may be available.
*/
void serialEvent() {

  while (Serial.available() && !stringComplete) {
    // get the new byte:
    char inChar = (char)Serial.read();
    // add it to the inputString:
    inputString += inChar;
    // if the incoming character is a newline, set a flag
    // so the main loop can do something about it:
    if (inChar == '\n') {
      stringComplete = true;
    }
  }
}


