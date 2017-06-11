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

import java.nio.charset.Charset;

import ru.skysoftlab.jarduino.JArduinoSensorMessageHandler;
import ru.skysoftlab.jarduino.JArduinoSensorProtocol;
import ru.skysoftlab.jarduino.JArduinoSensorProtocolPacket;

public class DigitalSensorReadResultMsg extends JArduinoSensorProtocolPacket {

	private String value;

	public DigitalSensorReadResultMsg(String value) {
		setCommandID(JArduinoSensorProtocol.DIGITAL_SENSOR_READ_RESULT);
		byte[] bytes = value.getBytes( Charset.forName("UTF-8" ));
		buffer.put(bytes);
		this.value = value;
	}

	public DigitalSensorReadResultMsg(byte[] packet) {
		setPacketData(packet);
		value = new String(getRawData(), Charset.forName("UTF-8"));
	}

	@Override
	public void acceptHandler(JArduinoSensorMessageHandler v) {
		v.handleDigitalSensorReadResult(this);
	}

	@Override
	public String toString() {
		String myString = "digitalSensorReadResult:";
		myString += " [value: " + value + "]";
		return myString;
	}

	public String getValue() {
		return value;
	}

}