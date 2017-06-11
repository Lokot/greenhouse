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
package ru.skysoftlab.jarduino.msg;

import org.sintef.jarduino.DigitalPin;

import ru.skysoftlab.jarduino.JArduinoSensorMessageHandler;
import ru.skysoftlab.jarduino.JArduinoSensorProtocol;
import ru.skysoftlab.jarduino.JArduinoSensorProtocolPacket;
import ru.skysoftlab.jarduino.sensors.Sensor;
import ru.skysoftlab.jarduino.sensors.SensorParametr;

public class DigitalSensorReadMsg extends JArduinoSensorProtocolPacket {

	private DigitalPin pin;
	private Sensor sensor;
	private SensorParametr parametr;

	public DigitalSensorReadMsg(DigitalPin pin, Sensor sensor,
			SensorParametr parametr) {
		setCommandID(JArduinoSensorProtocol.DIGITAL_SENSOR_READ);
		setByteValue(pin.getValue());
		setByteValue(sensor.getValue());
		setByteValue(parametr.getValue());
		this.pin = pin;
		this.sensor = sensor;
		this.parametr = parametr;
	}

	public DigitalSensorReadMsg(byte[] packet) {
		setPacketData(packet);
		pin = DigitalPin.fromValue(buffer.get());
		sensor = Sensor.fromValue(buffer.get());
		parametr = sensor.getParametrByVal(buffer.get());
	}

	@Override
	public void acceptHandler(JArduinoSensorMessageHandler v) {
		v.handleDigitalSensorRead(this);
	}

	@Override
	public String toString() {
		String myString = "digitalSensorRead:";
		myString += " [pin: " + pin + ", sensor: " + sensor + ", parametr: "
				+ parametr + "]";
		return myString;
	}

	public DigitalPin getPin() {
		return pin;
	}

	public Sensor getSensor() {
		return sensor;
	}

	public SensorParametr getParametr() {
		return parametr;
	}

}