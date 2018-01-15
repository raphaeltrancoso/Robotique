package engine;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;

public class MainV1 {
	public static void main(String[] args) {	
		final EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S1);
		final NavigatorCalibrationV1 calibration = new NavigatorCalibrationV1(colorSensor);
		calibration.start();
	}
}