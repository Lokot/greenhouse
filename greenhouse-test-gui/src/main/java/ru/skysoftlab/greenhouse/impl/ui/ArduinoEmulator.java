package ru.skysoftlab.greenhouse.impl.ui;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import ru.skysoftlab.greenhouse.impl.ui.greenhouse.IArduinoGreenHouse;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class ArduinoEmulator {

	public static final byte START_BYTE = 0x12;
	public static final byte STOP_BYTE = 0x13;
	public static final byte ESCAPE_BYTE = 0x7D;
	public static final int DEFAULT_BAUD = 9600;
	protected SerialPort serialPort;
	protected InputStream in;
	protected OutputStream out;

	private TestSerialReader reader;

	private IArduinoGreenHouse arduinoGreenHouse;

	public ArduinoEmulator() {
		serialPort = SerialPort.getCommPort("COM3");
		serialPort.openPort();
		// com5.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100,
		// 0);
		serialPort.setComPortParameters(9600, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
		in = serialPort.getInputStream();
		out = serialPort.getOutputStream();
		reader = new TestSerialReader();
		serialPort.addDataListener(reader);
	}

	public static void main(String[] args) {
		new ArduinoEmulator();
	}

	public class TestSerialReader implements SerialPortDataListener, Listener {

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
					if (state == RCV_WAIT) {
						// it should be a start byte or we just ignore it
						if (data == START_BYTE) {
							state = RCV_MSG;
							buffer_idx = 0;
						}
					} else if (state == RCV_MSG) {
						if (data == ESCAPE_BYTE) {
							state = RCV_ESC;
						} else if (data == STOP_BYTE) {
							// We got a complete frame
							parseIncommingMessage(buffer, buffer_idx);
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

		private void parseIncommingMessage(byte[] data, int size) {

			if (size < 5)
				return; // ignore incomplete packets
			switch (data[4]) // command code
			{
			// case 2: receivepinMode(data[5], data[6]); break;
			case 4:
				receivedigitalRead(data[5]);
				break;
			case 3:
				receivedigitalWrite(data[5], data[6]);
				break;
			// case 6: receiveanalogReference(data[5]); break;
			case 7:
				receiveanalogRead(data[5]);
				break;
			// case 9: receiveanalogWrite(data[5], data[6]); break;
			// case 10: receivetone(data[5], (data[6] << 8) + data[7], (data[8]
			// << 8) + data[9]); break;
			// case 11: receivenoTone(data[5]); break;
			// case 12: receivepulseIn(data[5], data[6]); break;
			// case 66: receiveping(); break;
			// case 21: receiveattachInterrupt(data[5], data[6]); break;
			// case 22: receivedetachInterrupt(data[5]); break;
			// case 31: receiveeeprom_read((data[5] << 8) + data[6]); break;
			// case 34: receiveeeprom_sync_write((data[5] << 8) + data[6],
			// data[7]); break;
			// case 33: receiveeeprom_write((data[5] << 8) + data[6], data[7]);
			// break;
			case 51:
				receivedigitalSensorRead(data[5], data[6], data[7]);
				break;
			default:
				break;
			}

		}

		private void receivedigitalWrite(final byte pin, final byte state) {
			if (state > 0) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						switch (pin) {
						case 4: // OPEN_SIGNAL_PIN
							arduinoGreenHouse.gableOpen(state);
							System.out.println("Открывается");
							break;
						case 3: // STOP_SIGNAL_PIN
							arduinoGreenHouse.gableStop(state);
							System.out.println("Стоп");
							break;
						case 2: // CLOSE_SIGNAL_PIN
							arduinoGreenHouse.gableClose(state);
							System.out.println("Закрывается");
							break;
						}
					}
				});
			}
		}

		private void receiveanalogRead(byte pin) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					int value = arduinoGreenHouse.getIllum();
					byte[] payload = new byte[32];
					payload[0] = 0x01; // source addr (not used)
					payload[1] = 0x00; // target addr (not used)
					payload[2] = 0x00; // frame num (not used)
					payload[3] = 2; // length of the params
					payload[4] = 8; // command code
					// set params here
					payload[5] = (byte) (value >> 8 & 0x00ff);
					payload[6] = (byte) (value & 0x00ff);
					// send the message
					try {
						sendOutgoingMessage(payload, 16);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		}

		private void receivedigitalSensorRead(byte pin, byte sensor, final byte type) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					String value = null;
					switch (type) {
					case 0:
						value = arduinoGreenHouse.getTemp();
						break;

					case 1:
						value = arduinoGreenHouse.getHum();
						break;

					default:
						value = "No Data";
						break;
					}
					byte[] payload = new byte[32];
					payload[0] = 0x01; // source addr (not used)
					payload[1] = 0x00; // target addr (not used)
					payload[2] = 0x00; // frame num (not used)
					payload[3] = (byte) value.length(); // length of the params
					payload[4] = 52; // command code
					byte[] vals = value.getBytes();
					for (int i = 0; i < vals.length; i++) {
						payload[i + 5] = vals[i];
					}
					// send the message
					try {
						sendOutgoingMessage(payload, 16);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		}

		private void receivedigitalRead(final byte pin) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					byte val = 0;
					switch (pin) {
					case 13: // OPEN_PIN
						val = arduinoGreenHouse.getGableState(3);
						break;
					case 12: // OPEN_60_PIN
						val = arduinoGreenHouse.getGableState(2);
						break;
					case 11: // OPEN_30_PIN
						val = arduinoGreenHouse.getGableState(1);
						break;
					case 10: // CLOSE_PIN
						val = arduinoGreenHouse.getGableState(0);
						break;
					}
					byte[] payload = new byte[32];
					payload[0] = 0x01; // source addr (not used)
					payload[1] = 0x00; // target addr (not used)
					payload[2] = 0x00; // frame num (not used)
					payload[3] = 1; // length of the params
					payload[4] = 5; // command code
					// set params here
					payload[5] = val;
					// send the message
					try {
						sendOutgoingMessage(payload, 16);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		}

		// public void sendinterruptNotification(byte interrupt) {
		// byte[] payload = new byte[32];
		// payload[0] = 0x01; // source addr (not used)
		// payload[1] = 0x00; // target addr (not used)
		// payload[2] = 0x00; // frame num (not used)
		// payload[3] = 1; // length of the params
		// payload[4] = 23; // command code
		// // set params here
		// payload[5] = interrupt;
		// // send the message
		// try {
		// sendOutgoingMessage(payload, 16);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }

		public void sendpinStateNotification(byte pin, byte state) {
			byte[] payload = new byte[32];
			payload[0] = 0x01; // source addr (not used)
			payload[1] = 0x00; // target addr (not used)
			payload[2] = 0x00; // frame num (not used)
			payload[3] = 2; // length of the params
			payload[4] = 53; // command code
			// set params here
			payload[5] = pin;
			payload[6] = state;
			// send the message
			try {
				sendOutgoingMessage(payload, 16);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private void sendOutgoingMessage(byte[] data, int size) throws IOException {
			int i = 0;
			// send the start byte
			out.write(START_BYTE);
			// send data
			for (i = 0; i < size; i++) {
				// escape special bytes
				if (data[i] == START_BYTE || data[i] == STOP_BYTE || data[i] == ESCAPE_BYTE) {
					out.write(ESCAPE_BYTE);
				}
				out.write(data[i]);
			}
			// send the stop byte
			out.write(STOP_BYTE);
		}

		@Override
		public int getListeningEvents() {
			return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
		}

		@Override
		public void handleEvent(Event event) {
			Button button = (Button) event.widget;
			if (button.getSelection()) {
				switch (Byte.parseByte(button.getData("state").toString())) {
				case 3: // OPEN_PIN
					sendpinStateNotification((byte) 13, (byte) 1);
					break;
				case 2: // OPEN_60_PIN
					sendpinStateNotification((byte) 12, (byte) 1);
					break;
				case 1: // OPEN_30_PIN
					sendpinStateNotification((byte) 11, (byte) 1);
					break;
				case 0: // CLOSE_PIN
					sendpinStateNotification((byte) 10, (byte) 1);
					break;
				}
			}
		}

	}

	public void setArduinoGreenHouse(IArduinoGreenHouse arduinoGreenHouse) {
		this.arduinoGreenHouse = arduinoGreenHouse;
	}

	public Listener getListener() {
		return reader;
	}

}
