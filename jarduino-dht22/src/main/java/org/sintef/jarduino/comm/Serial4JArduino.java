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
 * Authors: Franck Fleurey, Brice Morin and Francois Fouqet
 * Company: SINTEF IKT, Oslo, Norway & INRIA
 * Date: 2011
 */
package org.sintef.jarduino.comm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import org.sintef.jarduino.observer.JArduinoClientObserver;
import org.sintef.jarduino.observer.JArduinoObserver;
import org.sintef.jarduino.observer.JArduinoSubject;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class Serial4JArduino implements JArduinoClientObserver, JArduinoSubject {

	public static final byte START_BYTE = 0x12;
	public static final byte STOP_BYTE = 0x13;
	public static final byte ESCAPE_BYTE = 0x7D;
	public static final int DEFAULT_BAUD = 9600;
	protected String port;
	protected SerialPort serialPort;
	protected InputStream in;
	protected OutputStream out;

	public Serial4JArduino(String port) {
		this(port, new SerialConfiguration(DEFAULT_BAUD));
	}

	public Serial4JArduino(String port, SerialConfiguration conf) {
		this.port = port;
		connect(port);
	}

	void connect(String portName) {
		this.connect(portName, new SerialConfiguration(DEFAULT_BAUD));
	}

	void connect(String portName, SerialConfiguration conf) {
		// registerPort(portName);
		try {
			serialPort = SerialPort.getCommPort(portName);
			serialPort.openPort();
			// serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING,
			// 100, 0);
			serialPort.setComPortParameters(conf.getBaudRate(), 8,
					SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);

			in = serialPort.getInputStream();
			out = serialPort.getOutputStream();

			serialPort.addDataListener(new SerialReader());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
			if (serialPort != null) {
				serialPort.closePort();
			}
		} catch (Exception e) {
		}
	}

	/* ***********************************************************************
	 * Implementation of the CoffeeSensorClientObserver interface. The
	 * receiveMsg method gets called with packets to send.
	 * ***********************************************************************
	 */
	@Override
	public void receiveMsg(byte[] msg) {
		sendData(msg);
	}

	/* ***********************************************************************
	 * Implementation of the CoffeeSensorSubject interface. The CoffeeSensor
	 * Observers get notified for each incoming packet.
	 * ***********************************************************************
	 */
	Set<JArduinoObserver> observers = new HashSet<JArduinoObserver>();

	@Override
	public void register(JArduinoObserver observer) {
		observers.add(observer);
	}

	@Override
	public void unregister(JArduinoObserver observer) {
		observers.remove(observer);
	}

	/* ***********************************************************************
	 * Serial Port data send operation
	 * ***********************************************************************
	 */
	protected void sendData(byte[] payload) {
		try {
			// send the start byte
			out.write((int) START_BYTE);
			// send data
			for (int i = 0; i < payload.length; i++) {
				// escape special bytes
				if (payload[i] == START_BYTE || payload[i] == STOP_BYTE
						|| payload[i] == ESCAPE_BYTE) {
					out.write((int) ESCAPE_BYTE);
				}
				out.write((int) payload[i]);
			}
			// send the stop byte
			out.write((int) STOP_BYTE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* ***********************************************************************
	 * Serial Port Listener - reads packets from the serial line and notifies
	 * listeners of incoming packets
	 * ***********************************************************************
	 */
	public class SerialReader implements SerialPortDataListener {

		public static final int RCV_WAIT = 0;
		public static final int RCV_MSG = 1;
		public static final int RCV_ESC = 2;
		private byte[] buffer = new byte[256];
		protected int buffer_idx = 0;
		protected int state = RCV_WAIT;

		public void serialEvent(SerialPortEvent arg0) {

			int data;

			try {
				while ((data = in.read()) > -1) {
					// we got a byte from the serial port
					if (state == RCV_WAIT) { // it should be a start byte or we
												// just ignore it
						if (data == START_BYTE) {
							state = RCV_MSG;
							buffer_idx = 0;
						}
					} else if (state == RCV_MSG) {
						if (data == ESCAPE_BYTE) {
							state = RCV_ESC;
						} else if (data == STOP_BYTE) {
							// We got a complete frame
							byte[] packet = new byte[buffer_idx];
							for (int i = 0; i < buffer_idx; i++) {
								packet[i] = buffer[i];
							}
							for (JArduinoObserver o : observers) {
								o.receiveMsg(packet);
							}
							state = RCV_WAIT;
						} else if (data == START_BYTE) {
							// Should not happen but we reset just in case
							state = RCV_MSG;
							buffer_idx = 0;
						} else { // it is just a byte to store
							buffer[buffer_idx] = (byte) data;
							buffer_idx++;
						}
					} else if (state == RCV_ESC) {
						// Store the byte without looking at it
						buffer[buffer_idx] = (byte) data;
						buffer_idx++;
						state = RCV_MSG;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public int getListeningEvents() {
			return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
		}
	}

}
