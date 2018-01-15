package engine;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.internal.ev3.EV3Battery;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.MoveListener;
import lejos.robotics.navigation.MoveProvider;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;


public class Calibration extends Thread {
	
//	private static int currentSelectionMainY = 0;
	private static EV3ColorSensor colorSensor ;
	private MovementRobot mvRobot;
	private Sample sampleStart, sampleEnd, sampleMain, sampleOut;
	private MeasureRGB startMin, startMax, endMin, endMax, mainMin, mainMax, outMin, outMax;
	private int ec;
	private boolean running, jaune;
//	private Pose pose = new Pose();
	private MemoryWay memWay;
	
//	private Pose pose;
//	private DifferentialPilot pilot;
//	private Navigator navigator;;
//	private PoseProvider pp;
//	private MoveProvider mp;
//	private Scanner scanner = new Scanner(System.in);
//    private static FileOutputStream fos ;
//    private static DataOutputStream dos ;

	public Calibration(final EV3ColorSensor colorSensor){
        this.colorSensor = colorSensor;
        mvRobot = new MovementRobot("A", "B");
        memWay = new MemoryWay(mvRobot);
        sampleStart = new Sample();
        sampleMain = new Sample();
        sampleEnd = new Sample();
        sampleOut = new Sample();
        startMin = new MeasureRGB();
        ec = 6;
        startMax = new MeasureRGB();
        endMin = new MeasureRGB();
        endMax = new MeasureRGB();
        mainMin = new MeasureRGB();
        mainMax = new MeasureRGB();
        outMin = new MeasureRGB();
        outMax = new MeasureRGB();
        running = true;
        jaune = false;
//        memWay.getPilot().addMoveListener(memWay.getPosePro());
//        pilot = new DifferentialPilot(6.3f, 9.7f, Motor.A, Motor.B, false);
//        navigator = new Navigator(pilot);
//        pose = new Pose();
//        pp = new OdometryPoseProvider(mp);
//        mp.addMoveListener((MoveListener) pp);
        
	}
	
	public void run(){
		
		int i = 0;
		while(true){
			running = true;
			final SensorMode colorRGB = colorSensor.getRGBMode();
			mvRobot.close();
			
			// COND. 1 : ETALONNAGE
			if(Button.UP.isDown()){
//				System.out.print("ETALONNAGE : CHEMIN\n");
				LCD.drawString("ETALONNAGE: CHEMIN", 0, 1);
				while(Button.UP.isUp()){
					if(i==0){
						// COULEUR CHEMIN (VERT OU NOIR)
						if(Button.LEFT.isDown()){
//							System.out.print("\nCHEMIN .. < PR EXT");
							LCD.drawString("CHEMIN .. < PR EXT", 0, 1);
							do{
								int sampleSize = colorRGB.sampleSize();   
								float[] sampleRGB = new float[sampleSize];
								colorRGB.fetchSample(sampleRGB, 0);
								MeasureRGB m = new MeasureRGB((double) sampleRGB[0],(double) sampleRGB[1],(double) sampleRGB[2]);
								Sound.beep();
								m.setRed(m.denormalize(m.getRed(), 0, 255));
								m.setGreen(m.denormalize(m.getGreen(), 0, 255));
								m.setBlue(m.denormalize(m.getBlue(), 0, 255));
								sampleMain.addMeasureRGB(m);
								mainMin.setRed(sampleMain.getSampleAvg().getRed() - sampleMain.getEcartType().getRed() - ec);
								mainMax.setRed(sampleMain.getSampleAvg().getRed() + sampleMain.getEcartType().getRed() + ec);
								mainMin.setGreen(sampleMain.getSampleAvg().getGreen() - sampleMain.getEcartType().getGreen() - ec);
								mainMax.setGreen(sampleMain.getSampleAvg().getGreen() + sampleMain.getEcartType().getGreen() + ec);
								mainMin.setBlue(sampleMain.getSampleAvg().getBlue() - sampleMain.getEcartType().getBlue() - ec);
								mainMax.setBlue(sampleMain.getSampleAvg().getBlue() + sampleMain.getEcartType().getBlue() + ec);
							}while(Button.RIGHT.isUp());
							i++;
						}
				}else if(i==1){
					// COULEUR EXTERIEUR CHEMIN (BLANC)
					if(Button.LEFT.isDown()){
//						System.out.print("\nEXT CHEMIN .. < PR DEMI-TOUR");
						LCD.drawString("EXT CHEMIN .. < PR DEMI-TOUR", 0, 1);

						do{
							Sound.beep();
							int sampleSize = colorRGB.sampleSize();   
							float[] sampleRGB = new float[sampleSize];
							colorRGB.fetchSample(sampleRGB, 0);
							MeasureRGB m = new MeasureRGB((double) sampleRGB[0],(double) sampleRGB[1],(double) sampleRGB[2]);
							
							m.setRed(m.denormalize(m.getRed(), 0, 255));
							m.setGreen(m.denormalize(m.getGreen(), 0, 255));
							m.setBlue(m.denormalize(m.getBlue(), 0, 255));
							
							sampleOut.addMeasureRGB(m);
							outMin.setRed(sampleOut.getSampleAvg().getRed() - sampleOut.getEcartType().getRed()- ec);
							outMax.setRed(sampleOut.getSampleAvg().getRed() + sampleOut.getEcartType().getRed()+ ec);
							outMin.setGreen(sampleOut.getSampleAvg().getGreen() - sampleOut.getEcartType().getGreen()- ec);
							outMax.setGreen(sampleOut.getSampleAvg().getGreen() + sampleOut.getEcartType().getGreen()+ ec);
							outMin.setBlue(sampleOut.getSampleAvg().getBlue() - sampleOut.getEcartType().getBlue()- ec);
							outMax.setBlue(sampleOut.getSampleAvg().getBlue() + sampleOut.getEcartType().getBlue()+ ec);
						}while(Button.RIGHT.isUp());
						i++;
					}
					
				}else if(i==2){
					// COULEUR DEMI-TOUR (ROUGE)
					if(Button.LEFT.isDown()){
//						System.out.print("\nDEMI-TOUR .. < PR DEP/ARR");
						LCD.drawString("DEMI-TOUR .. < PR DEP/ARR", 0, 1);

						do{
							Sound.beep();
							int sampleSize = colorRGB.sampleSize();   
							float[] sampleRGB = new float[sampleSize];
							colorRGB.fetchSample(sampleRGB, 0);
							MeasureRGB m = new MeasureRGB((double) sampleRGB[0],(double) sampleRGB[1],(double) sampleRGB[2]);
							m.setRed(m.denormalize(m.getRed(), 0, 255));
							m.setGreen(m.denormalize(m.getGreen(), 0, 255));
							m.setBlue(m.denormalize(m.getBlue(), 0, 255));
							sampleEnd.addMeasureRGB(m);
							endMin.setRed(sampleEnd.getSampleAvg().getRed() - sampleEnd.getEcartType().getRed()- ec);
							endMax.setRed(sampleEnd.getSampleAvg().getRed() + sampleEnd.getEcartType().getRed()+ ec);
							endMin.setGreen(sampleEnd.getSampleAvg().getGreen() - sampleEnd.getEcartType().getGreen()- ec);
							endMax.setGreen(sampleEnd.getSampleAvg().getGreen() + sampleEnd.getEcartType().getGreen()+ ec);
							endMin.setBlue(sampleEnd.getSampleAvg().getBlue() - sampleEnd.getEcartType().getBlue()- ec);
							endMax.setBlue(sampleEnd.getSampleAvg().getBlue() + sampleEnd.getEcartType().getBlue()+ ec);
						}while(Button.RIGHT.isUp());
						i++;
					}
					
				}else if(i==3){
					// COULEUR DEPART/ARRIVEE (JAUNE)
					if(Button.LEFT.isDown()){
//						System.out.print("\nDEP/ARR ET ^ QD FINI");
						LCD.drawString("DEP/ARR ET ^ QD FINI", 0, 1);

						do{
							Sound.beep();
							int sampleSize = colorRGB.sampleSize();   
							float[] sampleRGB = new float[sampleSize];
							colorRGB.fetchSample(sampleRGB, 0);
							MeasureRGB m = new MeasureRGB((double) sampleRGB[0],(double) sampleRGB[1],(double) sampleRGB[2]);
							
							m.setRed(m.denormalize(m.getRed(), 0, 255));
							m.setGreen(m.denormalize(m.getGreen(), 0, 255));
							m.setBlue(m.denormalize(m.getBlue(), 0, 255));
							
							sampleStart.addMeasureRGB(m);
							startMin.setRed(sampleStart.getSampleAvg().getRed() - sampleStart.getEcartType().getRed()- ec);
							startMax.setRed(sampleStart.getSampleAvg().getRed() + sampleStart.getEcartType().getRed()+ ec);
							startMin.setGreen(sampleStart.getSampleAvg().getGreen() - sampleStart.getEcartType().getGreen()- ec);
							startMax.setGreen(sampleStart.getSampleAvg().getGreen() + sampleStart.getEcartType().getGreen()+ ec);
							startMin.setBlue(sampleStart.getSampleAvg().getBlue() - sampleStart.getEcartType().getBlue()- ec);
							startMax.setBlue(sampleStart.getSampleAvg().getBlue() + sampleStart.getEcartType().getBlue()+ ec);
						}while(Button.UP.isUp());
						i=0;
						break;
					}
				}
			}
			// COND. 2 : PARCOURS
		}else if(Button.RIGHT.isDown()){
				
			while(running){
				int sampleSize = colorRGB.sampleSize();
				float[] sampleRGB = new float[sampleSize];
				colorRGB.fetchSample(sampleRGB, 0);
				MeasureRGB m = new MeasureRGB((double) sampleRGB[0],(double) sampleRGB[1],(double) sampleRGB[2]);
				m.setRed(m.denormalize(m.getRed(), 0, 255));
				m.setGreen(m.denormalize(m.getGreen(), 0, 255));
				m.setBlue(m.denormalize(m.getBlue(), 0, 255));
				
				// Enregistre chaque point du  parcours				
				memWay.newWaypoint((int) memWay.reportPose().getX(), (int) memWay.reportPose().getY());
//				System.out.println((int) memWay.reportPose().getX() + "," +  (int) memWay.reportPose().getY());						
				LCD.drawString((int) memWay.reportPose().getX() + "," +  (int) memWay.reportPose().getY(), 0, 2);

				
//				Thread t = new Thread(){
//					public void run(){
//						memWay.newWaypoint((int) memWay.reportPose().getX(), (int) memWay.reportPose().getY());
//						System.out.println(memWay.reportPose().getX() + "," +  memWay.reportPose().getY());						
////						System.out.println((int) memWay.reportPose().getX() + "," +  (int) memWay.reportPose().getY());						
//					}
//				};
//				t.run();
				
				// si couleur Vert ou Noir
				if((m.getRed()>=mainMin.getRed() && m.getRed()<=mainMax.getRed())
					&& (m.getGreen()>=mainMin.getGreen() && m.getGreen()<=mainMax.getGreen())
					&& (m.getBlue()>=mainMin.getBlue() && m.getBlue()<=mainMax.getBlue()))
				{
					Button.LEDPattern(1);
	            	try {
//						System.out.println("CHEMIN");
						LCD.drawString("CHEMIN", 0, 3);

	            		mvRobot.forward(127, 300); // 85, 200
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            	
	            // si couleur Rouge
				}else if((m.getRed()>=endMin.getRed() && m.getRed()<=endMax.getRed())
						&& (m.getGreen()>=endMin.getGreen() && m.getGreen()<=endMax.getGreen())
						&& (m.getBlue()>=endMin.getBlue() && m.getBlue()<=endMax.getBlue()))
				{
					
					Button.LEDPattern(2);							
//					System.out.println("ROUGE");
					LCD.drawString("ROUGE", 0, 3);

		            try {
		            	mvRobot.forward(700);
						mvRobot.comeBack(150, 150, 2500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            
		        // si couleur Jaune debut de parcours
				}else if((m.getRed()>=startMin.getRed() && m.getRed()<=startMax.getRed())
						&& (m.getGreen()>=startMin.getGreen() && m.getGreen()<=startMax.getGreen())
						&& (m.getBlue()>=startMin.getBlue() && m.getBlue()<=startMax.getBlue()) && jaune==false)
				{
				
					Button.LEDPattern(4);
					jaune=true;
					try {
						mvRobot.forward(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				// si couleur Jaune fin de parcours
				}else if((m.getRed()>=startMin.getRed() && m.getRed()<=startMax.getRed())
						&& (m.getGreen()>=startMin.getGreen() && m.getGreen()<=startMax.getGreen())
						&& (m.getBlue()>=startMin.getBlue() && m.getBlue()<=startMax.getBlue()) && jaune==true)
				{
				
					Button.LEDPattern(3);
					running = false;
					jaune = false;
					// si couleur arrive tend plus vers le Blanc
				}
					else if((m.getRed()>((outMin.getRed()+mainMin.getRed())/2) && m.getRed()<((outMax.getRed()+mainMax.getRed())/2))
						&& (m.getGreen()>((outMin.getGreen()+mainMin.getGreen())/2) && m.getGreen()<((outMax.getGreen()+mainMax.getGreen())/2))
						&& (m.getBlue()>((outMin.getBlue()+mainMin.getBlue())/2) && m.getBlue()<((outMax.getBlue()+mainMax.getBlue())/2)))
				{		
					Button.LEDPattern(3);
					try {
//						System.out.println("CENTRE");
						LCD.drawString("CENTRE", 0, 3);
						mvRobot.forward(300,300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				// si couleur tend plus vers le chemin principal. 
				}
				// si couleur Blanche
				else if((m.getRed()>=outMin.getRed() && m.getRed()<=outMax.getRed())
				&& (m.getGreen()>=outMin.getGreen() && m.getGreen()<=outMax.getGreen())
				&& (m.getBlue()>=outMin.getBlue() && m.getBlue()<=outMax.getBlue())){
					try {
						Button.LEDPattern(2);
//						System.out.println("EXTERIEUR");
						LCD.drawString("EXTERIEUR", 0, 3);
						mvRobot.forward(300, 127);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(Button.ESCAPE.isDown()){
					System.exit(0);
				}
			}
			mvRobot.close();
			//colorSensor.close();
			
			}else if(Button.ESCAPE.isDown()){
				System.exit(0);
			}
			
			else if(Button.ENTER.isDown()){
				memWay.navigate();
			}
		}
			//mvRobot.close();
			//colorSensor.close();
		
	}
}