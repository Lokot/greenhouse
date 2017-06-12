package jarduino.test;

import com.fazecast.jSerialComm.SerialPort;

public class SerialTesterWriter {

	public static void main(String[] args) {
		SerialPort com6 = SerialPort.getCommPort("COM6");
		com6.openPort();
		com6.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);
		com6.setComPortParameters(9600, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
		try{
			while (true) {
				byte[] writeBuffer = new String("Привет").getBytes();
				com6.writeBytes(writeBuffer, writeBuffer.length);
				System.out.println("Write bytes.");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		com6.closePort();
	}
	
}
