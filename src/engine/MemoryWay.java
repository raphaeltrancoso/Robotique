package engine;

import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;

public class MemoryWay {
	
	private DifferentialPilot pilot;
	private OdometryPoseProvider posePro;
	private Navigator navigator;
    private Waypoint next;
    private MovementRobot mvRobot;
    
    public MemoryWay(MovementRobot mvRobot){
    	this.mvRobot = mvRobot;
    	pilot = new DifferentialPilot(5.5f, 14.0f, mvRobot.getMotorEv3Right(), mvRobot.getMotorEv3Left(), false);
    	navigator = new Navigator(pilot);
    	posePro = new OdometryPoseProvider(pilot);
        pilot.addMoveListener(posePro);
    }
    
    
    public void newWaypoint(int x, int y){
    	navigator.addWaypoint(x, y);
    }
    
    public void newPath(Path path){
    	navigator.setPath(path);
    }
    
    public void newPose(Pose pose){
    	posePro.setPose(pose);
    }
    
    public Pose reportPose(){
    	return posePro.getPose();
    }
    
    public DifferentialPilot getPilot() {
		return pilot;
	}

	public void setPilot(DifferentialPilot pilot) {
		this.pilot = pilot;
	}
	 public OdometryPoseProvider getPosePro() {
			return posePro;
	}

	public void setPosePro(OdometryPoseProvider posePro) {
		this.posePro = posePro;
	}

	public Navigator getNavigator() {
		return navigator;
	}

	public void setNavigator(Navigator navigator) {
		this.navigator = navigator;
	}

	public Waypoint getNext() {
		return next;
	}

	public void setNext(Waypoint next) {
		this.next = next;
	}

    public void navigate(){
		while(!navigator.pathCompleted()){
		    navigator.followPath();
		    next = navigator.getWaypoint();
		    LCD.drawString("(" + (int)next.getX() + "," + (int)next.getY() + ")", 0, 4);
		}
    }
}
