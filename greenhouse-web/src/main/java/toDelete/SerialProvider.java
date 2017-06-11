package toDelete;

import static ru.skysoftlab.greenhouse.impl.ConfigurationNames.SERIAL_PORT;

import java.io.IOException;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.skysoftlab.greenhouse.jpa.entitys.Readout;
import ru.skysoftlab.skylibs.annatations.AppProperty;
import ru.skysoftlab.skylibs.events.ConfigurationListener;
import ru.skysoftlab.skylibs.events.SystemConfigEvent;

//import com.fasterxml.jackson.core.JsonParseException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Взаимодействует с Arduino.
 * 
 * @author Lokot
 *
 */
//@Singleton
public class SerialProvider {
//
//implements IArduino, ConfigurationListener {
//
//	private static final long serialVersionUID = -708569752941424667L;
//
//	private static final char END = '\n';
//
//	private static final String GET_DATA = "DATA" + END;
//	private static final String SET_ILLUMINATION = "SET,illum,%d" + END;
//	private static final String SET_TEMPERATURE = "SET,temp,%f" + END;
//	private static final String SET_HUMIDITY = "SET,hum,%f" + END;
//
//	private Logger LOG = LoggerFactory.getLogger(SerialProvider.class);
//
//	@Inject
//	private DataBaseProvider dataBaseProvider;
//
//	@Inject
//	@AppProperty(SERIAL_PORT)
//	private String portName;
//
//	private SerialPort serialPort;
//
//	@PostConstruct
//	private void init() {
//		openSerialPort();
//	}
//
//	@PreDestroy
//	private void deInit() {
//		closeSerialPort();
//	}
//
//	private void openSerialPort() {
//		// Передаём в конструктор имя порта
//		serialPort = new SerialPort(portName);
//		try {
//			serialPort.openPort();
//			if (serialPort.isOpened()) {
//				try {
//					serialPort.setParams(SerialPort.BAUDRATE_9600,
//							SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
//							SerialPort.PARITY_NONE, false, true);
//					serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
//					serialPort.addEventListener(new PortReader(),
//							SerialPort.MASK_RXCHAR);
//				} catch (SerialPortException e) {
//					LOG.error("Set serial port options error", e);
//				}
//			} else {
//				LOG.error("SerialPort not open.");
//			}
//		} catch (SerialPortException e) {
//			LOG.error("Open SerialPort error", e);
//		}
//	}
//
//	private void closeSerialPort() {
//		if (serialPort.isOpened()) {
//			try {
//				serialPort.closePort();
//			} catch (SerialPortException e) {
//				LOG.error("Close SerialPort error", e);
//			}
//		}
//		serialPort = null;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see ru.skysoftlab.greenhouse.impl.IArduino#getReadout()
//	 */
//	@Override
//	public void getReadout() {
//		if (serialPort.isOpened()) {
//			try {
//				serialPort.writeString(GET_DATA);
//			} catch (SerialPortException e) {
//				LOG.error("Send request error", e);
//			}
//		} else {
//			LOG.error("SerialPort not open.");
//		}
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see ru.skysoftlab.greenhouse.impl.IArduino#setIllumination(int)
//	 */
//	@Override
//	public void setIllumination(int val) {
//		if (serialPort.isOpened()) {
//			try {
//				serialPort.writeString(String.format(SET_ILLUMINATION, val));
//				LOG.info(String.format(SET_ILLUMINATION, val));
//			} catch (SerialPortException e) {
//				LOG.error("Send set illumination request error", e);
//			}
//		} else {
//			LOG.error("SerialPort not open.");
//		}
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see ru.skysoftlab.greenhouse.impl.IArduino#setTemperature(float)
//	 */
//	@Override
//	public void setTemperature(float val) {
//		if (serialPort.isOpened()) {
//			try {
//				serialPort.writeString(String.format(SET_TEMPERATURE, val));
//			} catch (SerialPortException e) {
//				LOG.error("Send set illumination request error", e);
//			}
//		} else {
//			LOG.error("SerialPort not open.");
//		}
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see ru.skysoftlab.greenhouse.impl.IArduino#setHumidity(float)
//	 */
//	@Override
//	public void setHumidity(float val) {
//		if (serialPort.isOpened()) {
//			try {
//				serialPort.writeString(String.format(SET_HUMIDITY, val));
//			} catch (SerialPortException e) {
//				LOG.error("Send set illumination request error", e);
//			}
//		} else {
//			LOG.error("SerialPort not open.");
//		}
//	}
//
//	/**
//	 * @author Lokot
//	 *
//	 */
//	private class PortReader implements SerialPortEventListener {
//
//		private ObjectMapper mapper = new ObjectMapper();
//
//		private StringBuilder message = new StringBuilder();
//
//		private static final String READOUT = "ro";
//		private static final String CONFIG_STATE = "cs";
//		private static final String RESPONCE = "rs";
//
//		private <T> T readDto(Class<T> clazz, String jsonInString)
//				throws JsonParseException, JsonMappingException, IOException {
//			return mapper.readValue(jsonInString, clazz);
//		}
//
//		/**
//		 * Обработка сообщения.
//		 * 
//		 * @param msg
//		 * @param jsonInString
//		 * @throws JsonParseException
//		 * @throws JsonMappingException
//		 * @throws IOException
//		 */
//		private void processMessage(String msg, String jsonInString)
//				throws JsonParseException, JsonMappingException, IOException {
//			switch (msg) {
//			case READOUT:
//				ReadOutDto readoutDto = readDto(ReadOutDto.class, jsonInString);
//				Readout readout = new Readout(round(
//						readoutDto.getTemperature(), 1), round(
//						readoutDto.getHumidity(), 1),
//						readoutDto.getIllumination(), new Date());
//				try {
//					dataBaseProvider.saveReadout(readout);
//				} catch (Exception e) {
//					LOG.error("Save temp error", e);
//				}
//				break;
//
//			case CONFIG_STATE:
//				ConfigStateDto stateDto = readDto(ConfigStateDto.class,
//						jsonInString);
//				LOG.info(stateDto.toString());
//				break;
//
//			case RESPONCE:
//				ResponceDto respDto = readDto(ResponceDto.class, jsonInString);
//				LOG.info(respDto.toString());
//				break;
//
//			default:
//				LOG.error("Input type message error: " + msg);
//				break;
//			}
//		}
//
//		/*
//		 * (non-Javadoc)
//		 * 
//		 * @see jssc.SerialPortEventListener#serialEvent(jssc.SerialPortEvent)
//		 */
//		@Override
//		public void serialEvent(SerialPortEvent event) {
//			if (event.isRXCHAR() && event.getEventValue() > 0) {
//				try {
//					byte buffer[] = serialPort.readBytes();
//					for (byte b : buffer) {
//						if ((b == '\r' || b == END) && message.length() > 0) {
//							String inString = message.toString().replaceAll(
//									"\n", "");
//							LOG.info(inString);
//							String[] msgArr = inString.split("=");
//							if (msgArr.length == 2) {
//								try {
//									processMessage(msgArr[0], msgArr[1]);
//								} catch (IOException e) {
//									LOG.error("Read data error", e);
//								}
//							} else {
//								LOG.error("Input data format error");
//							}
//							message.setLength(0);
//						} else {
//							message.append((char) b);
//						}
//					}
//				} catch (SerialPortException ex) {
//					LOG.error("Read data error", ex);
//				}
//			}
//		}
//	}
//
//	/**
//	 * Округлить.
//	 * 
//	 * @param number
//	 * @param scale
//	 * @return
//	 */
//	private float round(float number, int scale) {
//		int pow = 10;
//		for (int i = 1; i < scale; i++)
//			pow *= 10;
//		float tmp = number * pow;
//		return (float) (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) / pow;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see
//	 * ru.skysoftlab.skylibs.events.ConfigurationListener#editIntervalEvent(
//	 * ru.skysoftlab.skylibs.events.SystemConfigEvent)
//	 */
//	@Override
//	public void editIntervalEvent(@Observes SystemConfigEvent event) {
//		String newSerialPortName = event.getParam(SERIAL_PORT);
//		if (newSerialPortName != null && newSerialPortName.length() > 0
//				&& !newSerialPortName.equals(portName)) {
//			closeSerialPort();
//			portName = newSerialPortName;
//			openSerialPort();
//		}
////		Double newIllumMinVal = event.getParam(ILLUM_MIN);
////		if (newIllumMinVal != null
////				&& (newIllumMinVal >= 0 && newIllumMinVal <= 1000)) {
////			setIllumination(newIllumMinVal.intValue());
////		}
//	}

}
