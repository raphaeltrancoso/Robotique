package engine;

import java.util.LinkedList;
import java.util.List;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;


public class NavigatorCalibrationV1 extends Thread {

	private static EV3ColorSensor colorSensor ;
	private MovementRobot mvRobot;
	private Sample sampleStart, sampleEnd, sampleMain, sampleOut;
	private MeasureRGB startMin, startMax, endMin, endMax, mainMin, mainMax, outMin, outMax;
	private int ec;
	private boolean running, jaune;
	private MemoryWay memWay;
	private MovementFile mf;
	private int travelSpeed;
	private int rotateSpeed;
	private int curveSpeed;
	private boolean append;
	private List<String> list = new LinkedList(); 
	private boolean directionRight = false;
	private boolean directionLeft = false;
	private boolean directionStraight = false;
	private float coefDirecteur;
	private String path = "zigzag.txt";
	
	public NavigatorCalibrationV1(final EV3ColorSensor colorSensor){
        this.colorSensor = colorSensor;
        mvRobot = new MovementRobot("A", "B");
        memWay = new MemoryWay(mvRobot);
        mf = new MovementFile(path);
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
	}
	
	public void run(){
		travelSpeed = 15;
        curveSpeed = 5;	
        int curveMax = 60;
        double coef = 20;
		int ec1 = 3;
		double inc = 0.8;
		double c1 = 0;
		double c2 = 0;
		int i = 0;
		
        memWay.getPilot().setTravelSpeed(travelSpeed);

		while(true){
			running = true;
			final SensorMode colorRGB = colorSensor.getRGBMode();
			mvRobot.close();
			
			// COND. 1 : ETALONNAGE
			if(Button.UP.isDown()){
				LCD.drawString("ETALONNAGE: CHEMIN", 0, 0);
				while(Button.UP.isUp()){
					if(i==0){
						// COULEUR CHEMIN (VERT OU NOIR)
						if(Button.LEFT.isDown()){
							LCD.drawString("CHEMIN .. < PR EXT", 0, 0);
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
						LCD.drawString("EXT CHEMIN .. < PR DEMI-TOUR", 0, 0);
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
						LCD.drawString("DEMI-TOUR .. < PR DEP/ARR", 0, 0);
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
						LCD.drawString("DEP/ARR ET ^ QD FINI", 0, 0);
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
						}while(Button.DOWN.isUp());
						i=0;
						break;
					}
				}
			}
			// COND. 2 : PARCOURS
		}else if(Button.RIGHT.isDown()){
			append = false;
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
				mf.save((int) memWay.reportPose().getX() + "," + (int) memWay.reportPose().getY() , append);
				append = true;
//				LCD.drawString("(X=" + (int) memWay.reportPose().getX() + ",Y=" +  (int) memWay.reportPose().getY()+")", 0, 4);
				Thread t = new Thread(){
					public void run(){
				
				//Garde les 10 dernieres valeur du deplacement du robot
				list.add((int) memWay.reportPose().getX() + "," + (int) memWay.reportPose().getY());
				if(list.size() > 10){
					list.remove(0);
				}
				//Calcul le coefficient directeur du premier et du dernier point enregistré
				String fieldsA[], fieldsB[];
				String A = (String) ((LinkedList<String>) list).getFirst();
				String B = (String) ((LinkedList<String>) list).getLast();
				fieldsA = A.split(",");
				fieldsB = B.split(",");
				if(list.size() >= 2){
					if(Integer.parseInt(fieldsB[1]) - Integer.parseInt(fieldsA[1]) == 0){
						coefDirecteur = 0;
					}
					else {
						try{
							coefDirecteur = (Integer.parseInt(fieldsB[0])- Integer.parseInt(fieldsA[0])) 
											/ (Integer.parseInt(fieldsB[1]) - Integer.parseInt(fieldsA[1]));
						} catch(ArithmeticException e){
							LCD.drawString("Division par 0", 0, 2);
						}
					}
//					LCD.drawString("coef="+coefDirecteur, 0, 2);
				}
				//Condition pour savoir dans la direction du robot 
				if(coefDirecteur < 1 && coefDirecteur > -1){
					directionStraight = true;
					directionLeft = false;
					directionRight = false;
				}
				if(coefDirecteur < 1){
					directionStraight = false;
					directionLeft = false;
					directionRight = true;
				}
				if(coefDirecteur > 1){
					directionStraight = false;
					directionLeft = true;
					directionRight = false;
				}
				
				LCD.drawString("TOUT DROIT="+directionStraight, 0, 1);
				LCD.drawString("DROITE="+directionRight, 0, 2);
				LCD.drawString("GAUCHE="+directionLeft, 0, 3);


				LCD.drawString("coef="+coefDirecteur, 0, 7);
					}
				};
				t.run();
				// si couleur Vert ou Noir (couleur chemin)
				if((m.getRed()>=mainMin.getRed() && m.getRed()<=mainMax.getRed())
					&& (m.getGreen()>=mainMin.getGreen() && m.getGreen()<=mainMax.getGreen())
					&& (m.getBlue()>=mainMin.getBlue() && m.getBlue()<=mainMax.getBlue()))
				{
					c1 = c1+inc;
					c2 = 0;
					Button.LEDPattern(1);
					LCD.drawString("CHEMIN", 0, 5);
//					tourne a droite
					int curveRight = (int) (curveSpeed + (coef*c1));
					if(curveRight < curveMax){
						memWay.getPilot().steer(-curveRight);
//						LCD.drawString("curveRightV:"+curveRight, 0, 0);
//						LCD.drawString("je rentre V", 0, 7);
					}
					else{
						memWay.getPilot().steer(-curveMax);
					}
	            // si couleur Rouge (couleur demi-tour)
				}else if((m.getRed()>=endMin.getRed() && m.getRed()<=endMax.getRed())
						&& (m.getGreen()>=endMin.getGreen() && m.getGreen()<=endMax.getGreen())
						&& (m.getBlue()>=endMin.getBlue() && m.getBlue()<=endMax.getBlue()))
				{
					
					Button.LEDPattern(2);							
//					System.out.println("ROUGE");
					LCD.drawString("ROUGE", 0, 5);
//					memWay.getPilot().rotate(170);
		        // si couleur Jaune debut de parcours
				}else if((m.getRed()>=startMin.getRed() && m.getRed()<=startMax.getRed())
						&& (m.getGreen()>=startMin.getGreen() && m.getGreen()<=startMax.getGreen())
						&& (m.getBlue()>=startMin.getBlue() && m.getBlue()<=startMax.getBlue()) && jaune==false)
				{
				
					Button.LEDPattern(4);
					jaune=true;
					memWay.getPilot().travel(10);
//						mvRobot.forward(300);
					
				
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
//					LCD.drawString("CENTRE BLANC", 0, 5);
					memWay.getPilot().forward();
//					memWay.getPilot().steer(4);
				}
				
				// si couleur arrive tend plus vers couleur chemin
//					else if((m.getRed()<((outMin.getRed()+mainMin.getRed())/2) && m.getRed()<((outMin.getRed()+(mainMin.getRed())/2)+ec))
//							&& (m.getGreen()<((outMin.getGreen()+mainMin.getGreen())/2) && m.getGreen()<((outMin.getGreen()+(mainMin.getGreen())/2)+ec))
//							&& (m.getBlue()<((outMin.getBlue()+mainMin.getBlue())/2) && m.getBlue()<((outMin.getBlue()+(mainMin.getBlue())/2)+ec)))
//					{		
//						Button.LEDPattern(3);
//						LCD.drawString("CENTRE CHEMIN", 0, 5);
//						memWay.getPilot().steer(-4);
//					}
				
				
				// si couleur Blanche (couleur extérieur)
				else if((m.getRed()>=outMin.getRed() && m.getRed()<=outMax.getRed())
				&& (m.getGreen()>=outMin.getGreen() && m.getGreen()<=outMax.getGreen())
				&& (m.getBlue()>=outMin.getBlue() && m.getBlue()<=outMax.getBlue())){
					Button.LEDPattern(2);
//					LCD.drawString("EXTERIEUR", 0, 5);
//					tourne à gauche
					c1 = 0;
					c2 = c2+inc;
					int curveLeft = (int) (curveSpeed + (coef*c2)); 
					if(curveLeft < curveMax){
						memWay.getPilot().steer(curveLeft);
//						LCD.drawString("curveLeftB:"+curveLeft, 0, 0);
					}
					else{
						memWay.getPilot().steer(curveMax);
					}
				}
				if(Button.ESCAPE.isDown()){
					System.exit(0);
				}
			}
			mvRobot.close();
			
			}else if(Button.ESCAPE.isDown()){
				System.exit(0);
			}
			
			else if(Button.ENTER.isDown()){
				memWay.navigate();
			}
		}
	}
}