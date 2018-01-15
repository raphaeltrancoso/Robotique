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

public class NavigatorCalibrationV2 extends Thread {
	
	private static EV3ColorSensor colorSensor ;
	private MovementRobot mvRobot;
	private Sample sampleStart, sampleEnd, sampleMain, sampleOut, orange, code1, code2;
	private MeasureRGB startMin, startMax, endMin, endMax, mainMin, mainMax, outMin, outMax, orangeMin, orangeMax, code1Min, code1Max, code2Min, code2Max;
	private int ec;
	private boolean running, jaune;
	private MemoryWay memWay;
	int codeCouleur;
	boolean b, n;
	int vitesse;
	int vitesseVirage;

	public NavigatorCalibrationV2(final EV3ColorSensor colorSensor){
        this.colorSensor = colorSensor;
        mvRobot = new MovementRobot("A", "B");
        memWay = new MemoryWay(mvRobot);
        sampleStart = new Sample();
        sampleMain = new Sample();
        sampleEnd = new Sample();
        sampleOut = new Sample();
        orange = new Sample();
        code1 = new Sample();
        code2 = new Sample();
        startMin = new MeasureRGB();
        ec = 6;
        startMax = new MeasureRGB();
        endMin = new MeasureRGB();
        endMax = new MeasureRGB();
        mainMin = new MeasureRGB();
        mainMax = new MeasureRGB();
        outMin = new MeasureRGB();
        outMax = new MeasureRGB();
        orangeMin = new MeasureRGB();
        orangeMax = new MeasureRGB();
        code1Min = new MeasureRGB();
        code1Max = new MeasureRGB();
        code2Min = new MeasureRGB();
        code2Max = new MeasureRGB();
        running = true;
        jaune = false;
        b = false;
        n = false;
        // 300; 127
//        vitesse = 270;
//        vitesseVirage = 75;
        vitesse = 310;
        vitesseVirage = 90;
        
	}
	
	public void run(){
		
		int i = 0;
		while(true){
			running = true;
			final SensorMode colorRGB = colorSensor.getRGBMode();
			mvRobot.close();
			
			// COND. 1 : ETALONNAGE
						if(Button.UP.isDown()){
							LCD.drawString("ETALONNAGE: CHEMIN", 0, 3);
//							System.out.print("ETALONNAGE : CHEMIN\n");
							while(Button.UP.isUp()){
								if(i==0){
									// COULEUR CHEMIN (VERT OU NOIR)
									if(Button.LEFT.isDown()){
										LCD.drawString("CHEMIN .. < PR EXT", 0, 3);
//										System.out.print("\nCHEMIN .. < PR EXT");
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
									LCD.drawString("EXT CHEMIN .. < PR DEMI-TOUR", 0, 3);
//									System.out.print("\nEXT CHEMIN .. < PR DEMI-TOUR");
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
									LCD.drawString("DEMI-TOUR .. < PR DEP/ARR", 0, 3);
//									System.out.print("\nDEMI-TOUR .. < PR DEP/ARR");
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
									LCD.drawString("DEP/ARR < PR ORANGE", 0, 3);
//									System.out.print("\n DEP/ARR ET ^ QD FINI ");
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
									}while(Button.RIGHT.isUp());
									i++;
								}
							}else if(i==4){
								// ORANGE
								if(Button.LEFT.isDown()){
									LCD.drawString("ORANGE < PR CODE1", 0, 3);
//									System.out.print("\nCHEMIN .. < PR EXT");
									do{
										int sampleSize = colorRGB.sampleSize();   
										float[] sampleRGB = new float[sampleSize];
										colorRGB.fetchSample(sampleRGB, 0);
										MeasureRGB m = new MeasureRGB((double) sampleRGB[0],(double) sampleRGB[1],(double) sampleRGB[2]);
										Sound.beep();
										m.setRed(m.denormalize(m.getRed(), 0, 255));
										m.setGreen(m.denormalize(m.getGreen(), 0, 255));
										m.setBlue(m.denormalize(m.getBlue(), 0, 255));
										orange.addMeasureRGB(m);
										orangeMin.setRed(orange.getSampleAvg().getRed() - orange.getEcartType().getRed() - ec);
										orangeMax.setRed(orange.getSampleAvg().getRed() + orange.getEcartType().getRed() + ec);
										orangeMin.setGreen(orange.getSampleAvg().getGreen() - orange.getEcartType().getGreen() - ec);
										orangeMax.setGreen(orange.getSampleAvg().getGreen() + orange.getEcartType().getGreen() + ec);
										orangeMin.setBlue(orange.getSampleAvg().getBlue() - orange.getEcartType().getBlue() - ec);
										orangeMax.setBlue(orange.getSampleAvg().getBlue() + orange.getEcartType().getBlue() + ec);
									}while(Button.RIGHT.isUp());
									i++;
								}
							}
							else if(i==5){
								// CODE1
								if(Button.LEFT.isDown()){
									LCD.drawString("CODE1 .. < CODE2", 0, 3);
//									System.out.print("\nCHEMIN .. < PR EXT");
									do{
										int sampleSize = colorRGB.sampleSize();   
										float[] sampleRGB = new float[sampleSize];
										colorRGB.fetchSample(sampleRGB, 0);
										MeasureRGB m = new MeasureRGB((double) sampleRGB[0],(double) sampleRGB[1],(double) sampleRGB[2]);
										Sound.beep();
										m.setRed(m.denormalize(m.getRed(), 0, 255));
										m.setGreen(m.denormalize(m.getGreen(), 0, 255));
										m.setBlue(m.denormalize(m.getBlue(), 0, 255));
										code1.addMeasureRGB(m);
										code1Min.setRed(code1.getSampleAvg().getRed() - code1.getEcartType().getRed() - ec);
										code1Max.setRed(code1.getSampleAvg().getRed() + code1.getEcartType().getRed() + ec);
										code1Min.setGreen(code1.getSampleAvg().getGreen() - code1.getEcartType().getGreen() - ec);
										code1Max.setGreen(code1.getSampleAvg().getGreen() + code1.getEcartType().getGreen() + ec);
										code1Min.setBlue(code1.getSampleAvg().getBlue() - code1.getEcartType().getBlue() - ec);
										code1Max.setBlue(code1.getSampleAvg().getBlue() + code1.getEcartType().getBlue() + ec);
									}while(Button.RIGHT.isUp());
									i++;
								}
							}
							else if(i==6){
								// CODE2
								if(Button.LEFT.isDown()){
									LCD.drawString("CODE2 ET v QD FINI", 0, 3);
//									System.out.print("\nCHEMIN .. < PR EXT");
									do{
										int sampleSize = colorRGB.sampleSize();   
										float[] sampleRGB = new float[sampleSize];
										colorRGB.fetchSample(sampleRGB, 0);
										MeasureRGB m = new MeasureRGB((double) sampleRGB[0],(double) sampleRGB[1],(double) sampleRGB[2]);
										Sound.beep();
										m.setRed(m.denormalize(m.getRed(), 0, 255));
										m.setGreen(m.denormalize(m.getGreen(), 0, 255));
										m.setBlue(m.denormalize(m.getBlue(), 0, 255));
										code2.addMeasureRGB(m);
										code2Min.setRed(code2.getSampleAvg().getRed() - code2.getEcartType().getRed() - ec);
										code2Max.setRed(code2.getSampleAvg().getRed() + code2.getEcartType().getRed() + ec);
										code2Min.setGreen(code2.getSampleAvg().getGreen() - code2.getEcartType().getGreen() - ec);
										code2Max.setGreen(code2.getSampleAvg().getGreen() + code2.getEcartType().getGreen() + ec);
										code2Min.setBlue(code2.getSampleAvg().getBlue() - code2.getEcartType().getBlue() - ec);
										code2Max.setBlue(code2.getSampleAvg().getBlue() + code2.getEcartType().getBlue() + ec);
									}while(Button.DOWN.isUp());
									i = 0;
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
//				Thread t = new Thread(){
//					public void run(){
//						memWay.newWaypoint((int) memWay.reportPose().getX(), (int) memWay.reportPose().getY());
//						System.out.println((int) memWay.reportPose().getX() + "," +  (int) memWay.reportPose().getY());						
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
						System.out.println("CHEMIN");
	            		mvRobot.forward(vitesseVirage, vitesse); // 85, 200
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
					System.out.println("ROUGE");
					Button.LEDPattern(3);
					running = false;
					jaune = false;
//		            try {
//		            	mvRobot.forward(700);
//						mvRobot.comeBack(150, 150, 2700);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
		            
		        // si couleur Jaune debut de parcours
				}else if((m.getRed()>=startMin.getRed() && m.getRed()<=startMax.getRed())
						&& (m.getGreen()>=startMin.getGreen() && m.getGreen()<=startMax.getGreen())
						&& (m.getBlue()>=startMin.getBlue() && m.getBlue()<=startMax.getBlue()) && jaune==false)
				{
				
					Button.LEDPattern(4);
					jaune=true;
					try {
						System.out.println("CHEMIN");
	            		mvRobot.forward(vitesse, vitesse); // 85, 200
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
						System.out.println("CENTRE");
						mvRobot.forward(vitesse,vitesse);
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
						System.out.println("EXTERIEUR");
						mvRobot.forward(vitesse, vitesseVirage);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// si code couleur bleu
				else if((m.getRed()>=code1Min.getRed() && m.getRed()<=code1Max.getRed())
						&& (m.getGreen()>=code1Min.getGreen() && m.getGreen()<=code1Max.getGreen())
						&& (m.getBlue()>=code1Min.getBlue() && m.getBlue()<=code1Max.getBlue()))
				{
					b = true;
					if(b && !n){
						codeCouleur = 1;
					}if(b && n){
						codeCouleur = 2;
					}else if(!b && n){
						codeCouleur = 3;
					}
					
					Button.LEDPattern(1);
	            	try {
						System.out.println("CHEMIN");
	            		mvRobot.forward(vitesseVirage, vitesse); // 85, 200
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if((m.getRed()>=code2Min.getRed() && m.getRed()<=code2Max.getRed())
						&& (m.getGreen()>=code2Min.getGreen() && m.getGreen()<=code2Max.getGreen())
						&& (m.getBlue()>=code2Min.getBlue() && m.getBlue()<=code2Max.getBlue()))
				{
					n = true;
					if(b && !n){
						codeCouleur = 1;
					}if(b && n){
						codeCouleur = 2;
					}else if(!b && n){
						codeCouleur = 3;
					}
					Button.LEDPattern(1);
	            	try {
						System.out.println("CHEMIN");
	            		mvRobot.forward(vitesseVirage, vitesse);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if((m.getRed()>=orangeMin.getRed() && m.getRed()<=orangeMax.getRed())
						&& (m.getGreen()>=orangeMin.getGreen() && m.getGreen()<=orangeMax.getGreen())
						&& (m.getBlue()>=orangeMin.getBlue() && m.getBlue()<=orangeMax.getBlue())){
					LCD.drawString("CODE COULEUR : " + codeCouleur, 0, 3);
					if(codeCouleur == 1){
						try {
							Button.LEDPattern(2);
							mvRobot.forward(vitesse, 0);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else if(codeCouleur == 2){
						try {
							Button.LEDPattern(2);
							mvRobot.forward(vitesse, vitesse);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else if(codeCouleur == 3){
						try {
							Button.LEDPattern(2);
							mvRobot.forward(0, vitesse);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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