package controller;

public class HSVConversion {
	
	private double h;
	private double s;
	private double v;
	
	public HSVConversion() {
		this.h = 0.0;
		this.s = 0.0;
		this.v = 0.0;
	}
	
	public void rgb2Hsv(int red, int green, int blue){
		
		double r = ((double)red)/(double)255;
		double g = ((double)green)/(double)255;
		double b = ((double)blue)/(double)255;
		
		double max = Math.max(Math.max(r, g) , b);
		double min = Math.min(Math.min(r, g), b);
		
		//V
		this.v = max;
		
		//S
		if(this.v == 0)
		{
			this.s = 0;
		}
		else
		{
			this.s = (this.v - min)/this.v;
		}
		
		if((0 < this.h && this.h < 360) && (0 <= this.s && this.s <= 1) && (0 <= this.v && this.v <=1)) {
            if (r == max && g == min) {
                this.h = 5 + (r - b) / (r - g);
            } else if (r == max && b == min) {
                this.h = 1 - (r - g) / (r - b);
            } else if (g == max && b == min) {
                this.h = 1 + (g - red) / (g - b);
            } else if (g == max && r == min) {
                this.h = 3 - (g - b) / (g - r);
            } else if (b == max && r == min) {
                this.h = 3 + (b - g) / (b - r);
            } else if (b == max && g == min) {
                this.h = 5 - (b - r) / (b - g);
            }
        }
		this.h = this.h * 60;
		if(this.h < 0){
			this.h += 360;
		}
		
	}
	
	public double getH() {
		return this.h;
	}
	
	public double getS() {
		return this.s;
	}
	
	public double getV() {
		return this.v;
	}
}
