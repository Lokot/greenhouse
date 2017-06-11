#include "DHT.h"

#define DHTPIN 2     // вывод, к которому подключается датчик
#define DHTTYPE DHT22   // DHT 22  (AM2302)

DHT dht(DHTPIN, DHTTYPE);

const int pinPhoto = A0;
const int led = 13;
String inputString = "";         // a string to hold incoming data
boolean stringComplete = false;  // whether the string is complete
int raw = 0;
int rawL = 600;
float h;
float t;

void setup() {
  Serial.begin(9600);
  pinMode( pinPhoto, INPUT );
  pinMode( led, OUTPUT );
  dht.begin();
  // reserve 200 bytes for the inputString:
  inputString.reserve(200);
}

void loop() {
  raw = analogRead( pinPhoto );
  if (isnan(raw)) {
    Serial.println("Failed to read from VT83N1 sensor!");
    return;
  }

  if (stringComplete) {
    if(inputString == "DATA") {
        sendData();
    } else if(inputString.indexOf("SET") != -1) {
        int commaIndex = inputString.indexOf(',');
        int secondCommaIndex = inputString.indexOf(',', commaIndex + 1);
        //String firstValue = inputString.substring(0, commaIndex);
        String secondValue = inputString.substring(commaIndex + 1, secondCommaIndex);
        String thirdValue = inputString.substring(secondCommaIndex + 1);
        setData(secondValue, thirdValue);
    } else if(inputString == "CONF_STATE"){
        sendConfState();
    }
    // clear the string:
    inputString = "";
    stringComplete = false;
  }

 if( raw < rawL)
    digitalWrite( led, HIGH );
  else
    digitalWrite( led, LOW );
}

void setData(String parName, String val){
  // добавляем настройки
  if(parName == "illum"){
    rawL = val.toInt();
  }
  // отправляем отчет
  Serial.println("rs={\"parametr\": \""+parName+"\", \"value\": \""+val+"\"}");
}

void sendConfState(){
  // отправляем отчет
  Serial.println("cs={\"tempMax\": 0, \"tempMin\": 0, \"humMax\": 0, \"humMin\": 0, \"illumMin\": " + String(rawL) + "}");
}

void sendData(){
  // считывание температуры или влажности занимает примерно 250 мс!
  // считанные показания могут отличаться от актуальных примерно на 2 секунды (это очень медленный датчик)
  h = dht.readHumidity();
  // Считывание температуры в цельсиях
  t = dht.readTemperature();
  // проверяем, были ли ошибки при считывании и, если были, начинаем заново
  if (isnan(h) || isnan(t)) {
    Serial.println("Failed to read from DHT sensor!");
    return;
  }
  //выводим информацию в Монитор последовательного порта
  Serial.println("ro={\"temperature\": "+String(t)+", \"humidity\": "+String(h)+", \"illumination\": "+raw+"}");
}

void serialEvent() {
  while (Serial.available()) {
    // get the new byte:
    char inChar = (char)Serial.read();
    // if the incoming character is a newline, set a flag
    // so the main loop can do something about it:
    if (inChar == '\n') {
      stringComplete = true;
      Serial.flush();
    } else {
      // add it to the inputString:
      inputString += inChar;  
    }
  }
}

/*String readFromSerial(){
  String rv = "";
  rv.reserve(200);
  while (Serial.available()) {
    // get the new byte:
    char inChar = (char)Serial.read();
    // if the incoming character is a newline, set a flag
    // so the main loop can do something about it:
    if (inChar == '\n') {
      Serial.flush();
      return rv;
    } else {
      // add it to the inputString:
      rv += inChar;  
    }
  }
  return rv;
}*/

