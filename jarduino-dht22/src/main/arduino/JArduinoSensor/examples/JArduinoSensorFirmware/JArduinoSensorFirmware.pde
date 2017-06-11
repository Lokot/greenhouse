/**
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors: Franck Fleurey and Brice Morin
 * Company: SINTEF IKT, Oslo, Norway
 * Date: 2011
 */
#include <JArduinoSensor.h>
#include <EEPROM.h>
#include "DHT.h"

#define DHTPIN 7     
#define DHTTYPE DHT22  
#define DHT_SENSOR 0
#define DHT_TEMP 0
#define DHT_HUM 1

#define OPEN_PIN 13
#define OPEN_60_PIN 12
#define OPEN_30_PIN 11
#define CLOSE_PIN 10

DHT dht(DHTPIN, DHTTYPE);

JArduinoSensor _JArduino = JArduinoSensor();

void interrupt0() {
  _JArduino.sendinterruptNotification(0);
}

void interrupt1() {
   _JArduino.sendinterruptNotification(1);
}

void receivepinMode(uint8_t pin, uint8_t mode) {
  pinMode(pin, mode);
}
void receivedigitalRead(uint8_t pin) {
  int result = digitalRead(pin);
  _JArduino.senddigitalReadResult(result);
}
void receivedigitalWrite(uint8_t pin, uint8_t value) {
  digitalWrite(pin, value);
}
void receiveanalogReference(uint8_t type) {
  analogReference(type);
}
void receiveanalogRead(uint8_t pin) {
  int result = analogRead(pin);
  _JArduino.sendanalogReadResult(result);
}
void receiveanalogWrite(uint8_t pin, uint8_t value) {
  analogWrite(pin, value);
}
void receiveping() {
  _JArduino.sendpong();
}
void receiveattachInterrupt(uint8_t interrupt, uint8_t mode) {
  if (interrupt == 0) attachInterrupt(0, interrupt0, mode);
  else if (interrupt == 1) attachInterrupt(1, interrupt1, mode);
}
void receivedetachInterrupt(uint8_t interrupt) {
  detachInterrupt(interrupt);
}
void receivetone(uint8_t pin, uint16_t frequency, uint16_t duration) {
  if (duration  == 0) tone(pin, frequency);
  else tone(pin, frequency, duration);
}
void receivenoTone(uint8_t pin) {
  noTone(pin);
}
void receivepulseIn(uint8_t pin, uint8_t value) {
    unsigned long duration = pulseIn(pin, value);
    _JArduino.sendpulseInResult(duration);
}
void receiveeeprom_read(uint16_t address) {
  uint8_t result = EEPROM.read(address);
  _JArduino.sendeeprom_value(result);
}
void receiveeeprom_sync_write(uint16_t address, uint8_t value) {
  EEPROM.write(address, value);
   _JArduino.sendeeprom_write_ack();
}
void receiveeeprom_write(uint16_t address, uint8_t value) {
  EEPROM.write(address, value);
}
void receivedigitalSensorRead(uint8_t pin, uint8_t sensor, uint8_t parametr) {
  String rv = "NoData";
  if(sensor == DHT_SENSOR){
    if(parametr == DHT_TEMP){
      rv = String(dht.readTemperature());
    } else if(parametr == DHT_HUM){
      rv = String(dht.readHumidity());
    }
  }
  _JArduino.senddigitalSensorReadResult(rv);
}

void setup()
{
  // initialize the JArduino protocol
  _JArduino.init_JArduino(9600);
  dht.begin();
  pinMode(OPEN_PIN, INPUT);
  pinMode(OPEN_60_PIN, INPUT);
  pinMode(OPEN_30_PIN, INPUT);
  pinMode(CLOSE_PIN, INPUT);
}

void loop()
{
  // check for incomming messages for the JArduino protocol
  _JArduino.poll_JArduino();
  // send pin state
  if(digitalRead(OPEN_PIN) > 0) {
  	_JArduino.sendpinStateNotification(OPEN_PIN, HIGH);
  } else if(digitalRead(OPEN_60_PIN) > 0) {
  	_JArduino.sendpinStateNotification(OPEN_60_PIN, HIGH);
  } else if(digitalRead(OPEN_30_PIN) > 0) {
  	_JArduino.sendpinStateNotification(OPEN_30_PIN, HIGH);
  } else if(digitalRead(CLOSE_PIN) > 0) {
  	_JArduino.sendpinStateNotification(CLOSE_PIN, HIGH);
  }
}