package test;

import lejos.hardware.Button;
import lejos.hardware.motor.*;
import lejos.hardware.port.MotorPort;


public class TestRotateMotor {
	public static void main(String[] args) {
		// Test run one motor
	      BaseRegulatedMotor myMotor = new EV3LargeRegulatedMotor(MotorPort.A);
	      myMotor.setSpeed(100);
	      myMotor.forward();
	      Button.waitForAnyPress();
	      while(true){
	    	  myMotor.forward();
	      }
		
		

     //Test Touch Sensor
//      TouchSensor touch = new TouchSensor(SensorPort.S1);
//      if (touch.isPressed()) {
//    
//      }
	}
}
