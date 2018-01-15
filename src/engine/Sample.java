package engine;

import java.util.ArrayList;
import java.lang.Math.*;

public class Sample {

	private ArrayList<MeasureRGB> measuresRGB;
	private double red_sum = 0, green_sum = 0, blue_sum = 0;
	
	public Sample() {
		measuresRGB = new ArrayList<MeasureRGB>();
	}
	
	public void addMeasureRGB(MeasureRGB m) {
		measuresRGB.add(m);
		red_sum += m.getRed();
		green_sum += m.getGreen();
		blue_sum += m.getBlue();
	}
	
	public int getCurrentRGBCount() {
		return measuresRGB.size();
	}
	
	public MeasureRGB getSampleAvg() {
		MeasureRGB mAvg = new MeasureRGB();
		mAvg.setRed(red_sum /(double) getCurrentRGBCount());
		mAvg.setGreen(green_sum /(double) getCurrentRGBCount());
		mAvg.setBlue(blue_sum /(double) getCurrentRGBCount());
		return mAvg; 
	}
	
	public void addMeasure(double red, double green, double blue){
		this.addMeasureRGB(new MeasureRGB(red, green, blue));
    }

    public MeasureRGB getMeasureRGB(int i){
    	return measuresRGB.get(i);
    }
    
    public MeasureRGB getEcartType(){  	
    	MeasureRGB sampleAvg = this.getSampleAvg();
    	int i;
    	double tmpR = 0, tmpG = 0, tmpB = 0;
    	for(i=0; i <getCurrentRGBCount(); i++){
    		tmpR += Math.pow(getMeasureRGB(i).getRed() - sampleAvg.getRed(), 2);
    		tmpG += Math.pow(getMeasureRGB(i).getGreen() - sampleAvg.getGreen(), 2);
    		tmpB += Math.pow(getMeasureRGB(i).getBlue() - sampleAvg.getBlue(), 2);  	
    	}
    	return new MeasureRGB(Math.sqrt(tmpR/(double) getCurrentRGBCount()),
    					Math.sqrt(tmpG/(double) getCurrentRGBCount()),
    					Math.sqrt(tmpB/(double) getCurrentRGBCount()));
    }
    
    public String toString(){
    	if (getCurrentRGBCount() == 0) {
			return "No measure RGB in the Sample \n";
		} else {
			String result = "";
			for (int i = 0; i <= measuresRGB.size() - 1; i++) {
				result += measuresRGB.get(i).toString() + "\n";
			}
			return result;
		}

    }

}
