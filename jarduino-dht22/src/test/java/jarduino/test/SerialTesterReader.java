package jarduino.test;

import com.fazecast.jSerialComm.SerialPort;

public class SerialTesterReader {

	public static void main(String[] args) {
//		SerialPort[] ports = SerialPort.getCommPorts();
//		for (SerialPort serialPort : ports) {
//			System.out.println(serialPort.getSystemPortName());
//		}
		
		SerialPort com5 = SerialPort.getCommPort("COM5");
		com5.openPort();
		com5.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);
		com5.setComPortParameters(9600, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
		try{
			while (true) {
				if(com5.bytesAvailable() > 0){
					byte[] readBuffer = new byte[12];
					int numRead = com5.readBytes(readBuffer, readBuffer.length);
					System.out.println("Read "+ numRead + " bytes.");	
					System.out.println(new String(readBuffer));
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		com5.closePort();
	}
	
}
