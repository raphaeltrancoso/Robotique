package test;

import engine.MovementRobot;
import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;

public class TestMoveMemory {
	
	public static void main(String[] args) {	

		DifferentialPilot pilot = new DifferentialPilot(6.3f, 9.7f, Motor.A, Motor.B, true);
	    Navigator navigator = new Navigator(pilot);
		PoseProvider pp;
		Pose pose = new Pose();
//		pose = pp.getPose();
		System.out.println(pose.getLocation());
//	    navigator.addWaypoint(new Waypoint(pose.getLocation()));
//	    navigator.addWaypoint(new Waypoint(100, 000));
//	    navigator.addWaypoint(new Waypoint(100, 50));
//	    navigator.addWaypoint(new Waypoint(0, 50));
//	    navigator.addWaypoint(new Waypoint(0,0));
	    navigator.addWaypoint(new Waypoint(100, 000));
	    navigator.addWaypoint(new Waypoint(0, 50));
	    navigator.addWaypoint(new Waypoint(-100, 0));
	    navigator.addWaypoint(new Waypoint(0,-50));
	    navigator.followPath();
        Button.ENTER.waitForPressAndRelease();

	}
}
