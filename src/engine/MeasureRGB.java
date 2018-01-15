package engine;

public class MeasureRGB {

    private double red,green,blue;

	public MeasureRGB(double red, double green, double blue){
		this.red = red;
		this.green = green;
		this.blue = blue;
    }
	
	public MeasureRGB(int red, int green, int blue){
		this.red = red;
		this.green = green;
		this.blue = blue;
    }

    public MeasureRGB() {
    	red = 0;
    	green = 0;
    	blue = 0;
    }
	
	public MeasureRGB(MeasureRGB m){
    	new MeasureRGB(m.red,m.green,m.blue);
    }

    public double getRed() {
		return red;
	}

	public void setRed(double red) {
		this.red = red;
	}

	public double getGreen() {
		return green;
	}

	public void setGreen(double green) {
		this.green = green;
	}

	public double getBlue() {
		return blue;
	}

	public void setBlue(double blue) {
		this.blue = blue;
	}
	
	public String toString(){
		return "RGB[r=" + red + ", g=" + green + ", b=" + blue + "] \n";
	}
	
	public int normalize(double val, int min, int max) {
		int normalized = (int) (val - min) / (max - min);
		return normalized;
	}
	
	public int denormalize(double val, int min, int max) {
		int denormalized = (int) (val * (max - min) + min);
		return denormalized;
	}


}
