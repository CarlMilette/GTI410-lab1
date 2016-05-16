package controller;

public class CMYKConversion {
	
	private double cyan;
	private double magenta;
	private double yellow;
	private double k;
	
	public CMYKConversion() {
		this.cyan = 0.0;
		this.magenta = 0.0;
		this.yellow = 0.0;
		this.k = 0.0;
	}
	
	public void rgb2cmyk(int red, int green, int blue) {
		double r = ((double)red)/(double)255;
		double g = ((double)green)/(double)255;
		double b = ((double)blue)/(double)255;
		
		this.k = 1 - max(r, g, b);
		this.cyan = (1 - r - this.k)/(1 - this.k);
		this.magenta = (1 - g - this.k)/(1 - this.k);
		this.yellow = (1 - b - this.k)/(1 - this.k);
		
	}
	
	private double max(double color1, double color2, double color3) {
		double max = color1;
		
		if(max < color2 && color2 > color3) {
			max = color2;
		} else if(max < color2 && color2 < color3) {
			max = color3;
		}
		
		return max;
	}
	
	public double getCyan() {
		return this.cyan;
	}
	
	public double getMagenta() {
		return this.magenta;
	}
	
	public double getYellow() {
		return this.yellow;
	}
	
	public double getK() {
		return this.k;
	}

}
