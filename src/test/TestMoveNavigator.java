package test;

import lejos.utility.Delay;
import engine.MemoryWay;
import engine.MovementRobot;

public class TestMoveNavigator {
	public static void main(String[] args) {	
		
		MovementRobot mvR = new MovementRobot("A", "B");
		final MemoryWay memWay = new MemoryWay(mvR);
		System.out.println("begin:" + (int) memWay.reportPose().getX() + "," + (int)  memWay.reportPose().getY());
//		while(true){
//			memWay.getPilot().forward();
//			System.out.println("end:" + (int) memWay.reportPose().getX() + "," + (int) memWay.reportPose().getY());
//		}
		Thread t = new Thread(){
			public void run(){
		 memWay.getPilot().setTravelSpeed(20);  // cm per second
		 memWay.getPilot().setRotateSpeed(10);  // degree per second
        System.out.println("TravelSpeed:" + memWay.getPilot().getTravelSpeed());
        System.out.println("RotateSpeed:" + memWay.getPilot().getRotateSpeed());


//		 memWay.getPilot().travel(100);         // cm
//		 Delay.msDelay(1000);
//		 System.out.println("end:" + (int) memWay.reportPose().getX() + "," + (int) memWay.reportPose().getY());
////		 
//		 memWay.getPilot().travelArc(10, 100);         // cm
//		 Delay.msDelay(1000);
//		 System.out.println("end:" + (int) memWay.reportPose().getX() + "," + (int) memWay.reportPose().getY());
//		 int curve = 20;
//		 memWay.getPilot().steer(curve);
//		 curve += 5;
		 int rotate = 10;
		 int curve = 5;
//		 memWay.getPilot().steer(curve);
		 while(true){
			 while(curve < 60){
				 memWay.getPilot().steer(curve);
//				 memWay.getPilot().setRotateSpeed(rotate);  // cm per second
//				 rotate += 1;
				 curve += 5;
			        System.out.println("RotateSpeed:" + memWay.getPilot().getRotateSpeed());
			        System.out.println("Curve:" + curve);
			 }
//			 memWay.getPilot().steer(curve);

		 }
//		 Delay.msDelay(1000);
		 
//		 memWay.getPilot().setTravelSpeed(20);
//	        System.out.println("TravelSpeed:" + memWay.getPilot().getTravelSpeed());
//
//			 memWay.getPilot().travel(-100, false);
//  

//		 memWay.getPilot().forward();
//		 memWay.getPilot().rotate(-90);        // degree clockwise
//		 Delay.msDelay(1000);
//		 System.out.println("end:" + (int) memWay.reportPose().getX() + "," + (int) memWay.reportPose().getY());
 		 
//		 memWay.getPilot().travel(-100,true);  //  move backward for 50 cm
//		 Delay.msDelay(1000);
//		 System.out.println("end:" + (int) memWay.reportPose().getX() + "," + (int) memWay.reportPose().getY());
//		 
//		 while(memWay.getPilot().isMoving())Thread.yield();
//		 memWay.getPilot().rotate(-90);
//		 Delay.msDelay(1000);
//		 System.out.println("end:" + (int) memWay.reportPose().getX() + "," + (int) memWay.reportPose().getY());
//		 
//		 memWay.getPilot().rotate(270);
//		 Delay.msDelay(1000);
//		 System.out.println("end:" + (int) memWay.reportPose().getX() + "," + (int) memWay.reportPose().getY());
//		 
//		 memWay.getPilot().steer(-100,180,true);	// turn 180 degrees to the right
//		 Delay.msDelay(1000);
//		 System.out.println("end:" + (int) memWay.reportPose().getX() + "," + (int) memWay.reportPose().getY());
//		 
////		 waitComplete();            // returns when previous method is complete
//		 memWay.getPilot().steer(100);          // turns with left wheel stationary
//		 Delay.msDelay(1000);
//		 System.out.println("end:" + (int) memWay.reportPose().getX() + "," + (int) memWay.reportPose().getY());
//		 
			}
		};
		while(true){
		t.run();
		}
	}
	
}
