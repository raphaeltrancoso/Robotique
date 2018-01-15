package engine;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;

public class MainV2 {
	public static void main(String[] args) {	
		final EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S1);
		final NavigatorCalibrationV2 calibration = new NavigatorCalibrationV2(colorSensor);
		calibration.start();
	}
}
