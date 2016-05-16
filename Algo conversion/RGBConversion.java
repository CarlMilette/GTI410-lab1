

public class RGBConversion {

	private int R1;
	private int G1;
	private int B1;
	
	private int R2;
	private int G2;
	private int B2;
	
	public RGBConversion() {
		this.R1 = 0;
		this.G1 = 0;
		this.B1 = 0;
		
		this.R2 = 0;
		this.G2 = 0;
		this.B2 = 0;
	}
	/******************** CMYKtoRGB****************************************************/
	public void cmyk2rgb(double c, double m, double y, double k) {
		
		this.R1 = (int) (255 * (1-c) * (1-k));
		this.G1 = (int) (255 * (1-m) * (1-k));
		this.B1 = (int) (255 * (1-y) * (1-k));
	
	}
	
	public double getR1() {
		return this.R1;
	}
	
	public double getG1() {
		return this.G1;
	}
	
	public double getB1() {
		return this.B1;
	}
	
	/******************** HSVtoRGB***************************************************/
	
	public void hsv2rgb(double hue, double saturation, double value) { 
	
		
	    int i;
	    int f, p, q, t;
	     
	    // Make sure our arguments stay in-range
	    double h = Math.max(0, Math.min(360, hue));
	    double s = Math.max(0, Math.min(100, saturation));
	    double v = Math.max(0, Math.min(100, value));
	     
	    // We accept saturation and value arguments from 0 to 100 because that's
	    // how Photoshop represents those values. Internally, however, the
	    // saturation and value are calculated from a range of 0 to 1. We make
	    // That conversion here.
	    s /= 100;
	    v /= 100;
	     
	    if(s == 0) {
	        // Achromatic (grey)
	        this.R2 = this.G2 = this.B2 = (int)v;
	        /*return [
	            Math.round(r * 255), 
	            Math.round(g * 255), 
	            Math.round(b * 255)
	        ];*/
	    }
	     
	    h /= 60; // sector 0 to 5
	    i = (int) Math.floor(h);
	    f = (int) (h - i); // factorial part of h
	    p = (int) (v * (1 - s));
	    q = (int) (v * (1 - s * f));
	    t = (int) (v * (1 - s * (1 - f)));
	     
	    switch(i) {
	        case 0:
	            this.R2 = (int) v;
	            this.G2 = t;
	            this.B2 = p;
	            break;
	     
	        case 1:
	        	this.R2 = q;
	        	this.G2 = (int) v;
	        	this.B2 = p;
	            break;
	     
	        case 2:
	        	this.R2 = p;
	        	this.G2 = (int) v;
	        	this.B2 = t;
	            break;
	     
	        case 3:
	        	this.R2 = p;
	        	this.G2 = q;
	        	this.B2 = (int) v;
	            break;
	     
	        case 4:
	        	this.R2 = t;
	        	this.G2 = p;
	        	this.B2 = (int) v;
	            break;
	     
	        default: // case 5:
	        	this.R2 = (int) v;
	            this.G2 = p;
	            this.B2 = q;
	    }
	
	}

	public double getR2() {
		return this.R2;
	}
	
	public double getG2() {
		return this.G2;
	}
	
	public double getB2() {
		return this.B2;
	}
}
