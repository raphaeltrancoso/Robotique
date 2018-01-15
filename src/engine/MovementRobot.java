package engine;

import lejos.hardware.BrickFinder;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class MovementRobot {
	
	EV3LargeRegulatedMotor motorLeft;
	EV3LargeRegulatedMotor motorRight;
	int speedLeft, speedRight, accLeft, accRight;
	
	public MovementRobot(String leftMotorPort , String rightMotorPort) {
		motorLeft = new EV3LargeRegulatedMotor(BrickFinder.getDefault().getPort(leftMotorPort));
		motorRight = new EV3LargeRegulatedMotor(BrickFinder.getDefault().getPort(rightMotorPort));
	}
	
	public EV3LargeRegulatedMotor getMotorEv3Left(){
		return motorLeft;
	}
	
	public EV3LargeRegulatedMotor getMotorEv3Right(){
		return motorRight;
	}
	
	public void forward(long time) throws InterruptedException{
		Thread f = new Thread(){
			public void run(){
				motorLeft.forward();
				motorRight.forward();
			}
		};
		f.run();
		Thread.sleep(time);
	}

	public void forward() throws InterruptedException{
		Thread f = new Thread(){
			public void run(){
				motorLeft.forward();
				motorRight.forward();
			}
		};
		f.run();
	}
	
	public void forward(final int speedLeft, final int speedRight) throws InterruptedException{
		motorLeft.setSpeed(speedLeft);
		motorRight.setSpeed(speedRight);
		motorLeft.forward();
		motorRight.forward();
	}
	
	public void forwardAcc(int speedLeft,int speedRight,int accLeft,int accRight){
			this.speedLeft=speedLeft;
			this.speedRight=speedRight;
			this.accLeft=accLeft;
			this.accRight=accRight;
			int newVitLeft = this.speedLeft=+accLeft;
			int newVitRight = this.speedRight=+accRight;
			motorLeft.setSpeed(newVitLeft);
			motorRight.setSpeed(newVitRight);
			motorLeft.forward();
			motorRight.forward();
			
	}
	
	public void forward(final int speedLeft, final int speedRight, final int incr) throws InterruptedException{
		Thread f = new Thread(){
			public void run(){
				for(int i=0; i<incr; i+=10){
					motorLeft.setSpeed(speedLeft);
					motorRight.setSpeed(speedRight);
					motorLeft.forward();
					motorRight.forward();
				}
			}
		};
		f.run();
	}	
	
	public void backward(long time) throws InterruptedException{
		Thread b = new Thread(){
			public void run(){
				motorLeft.backward();
				motorRight.backward();
			}
		};
		b.run();
		Thread.sleep(time);
	}
	
	public void turnLeft(final int degrees){
		Thread l = new Thread(){
			public void run(){
				motorLeft.rotate(degrees);
			}
		};
		l.run();
	}
	
	public void turnRight(final int degrees){
		Thread r = new Thread(){
			public void run(){
				motorRight.rotate(degrees);
			}
		};
		r.run();
	}
	
	public void comeBack(final int speedLeft, final int speedRight) throws InterruptedException{
		Thread f = new Thread(){
			public void run(){
				motorLeft.setSpeed(speedLeft);
				motorRight.setSpeed(speedRight);
				motorLeft.forward();
				motorRight.backward();
			}
		};
		f.run();
	}
	
	public void comeBack(final int speedLeft, final int speedRight, long time) throws InterruptedException{
		Thread f = new Thread(){
			public void run(){
				motorLeft.setSpeed(speedLeft);
				motorRight.setSpeed(speedRight);
				motorLeft.forward();
				motorRight.backward();
			}
		};
		f.run();
		Thread.sleep(time);
	}
	
	public void close(){
		motorLeft.stop();
		motorRight.stop();
	}

}
