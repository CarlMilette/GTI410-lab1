/*
   This file is part of j2dcg.
   j2dcg is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.
   j2dcg is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
   You should have received a copy of the GNU General Public License
   along with j2dcg; if not, write to the Free Software
   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package model;

import java.awt.Color;
import controller.RGBConversion;
import controller.CMYKConversion;
import controller.HSVConversion;

/**
 * <p>Title: Pixel</p>
 * <p>Description: Class that handles Pixel data in various formats</p>
 * <p>Copyright: Copyright (c) 2003 Colin Barré-Brisebois, Eric Paquette</p>
 * <p>Company: ETS - École de Technologie Supérieure</p>
 * @author Colin Barré-Brisebois
 * @version $Revision: 1.11 $
 */
public class Pixel {
    /** ARGB Pixel value */
    private int valueARGB;
    private int valueCMYK;
    private int valueHSV;
    
    /**
     * Pixel default constructor
     */
    public Pixel() {
		valueARGB = 0;
		RGB2CMYK();
		RGB2HSV();
    }
    
	/**
	 * Pixel constructor with a specified ARGB value
	 * @param valueARGB the pixel's ARGB value
	 */
    public Pixel(int valueARGB) {
        this.valueARGB = valueARGB;
        RGB2CMYK();
        RGB2HSV();
    }
    
    public Pixel(int rValue, int gValue, int bValue) {
    	setRed(rValue);
    	setGreen(gValue);
    	setBlue(bValue);
    	setAlpha(255);
    	RGB2CMYK();
    	RGB2HSV();
    }
    
	public Pixel(int rValue, int gValue, int bValue, int alpha) {
		setRed(rValue);
		setGreen(gValue);
		setBlue(bValue);
		setAlpha(alpha);
		RGB2CMYK();
		RGB2HSV();
	}    
    
    public Pixel(PixelDouble pixel) {
		setRed((int)pixel.getRed());
		setGreen((int)pixel.getGreen());
		setBlue((int)pixel.getBlue());
		setAlpha((int)pixel.getAlpha());
		RGB2CMYK();
		RGB2HSV();
    }
    
	/**
	 * Returns an attribute of the pixel
	 * @return the pixel's ARGB value
	 */    
    public int getARGB() { 
    	return (valueARGB); 
    }
    
    public int getCMYK() {
    	return (valueCMYK);
    }
    
    public int getHSV() {
    	return (valueHSV);
    }

	/**
	 * Returns an attribute of the pixel
	 * @return the pixel's ALPHA value
	 */        
    public int getAlpha() { 
    	return ((valueARGB >> 24) & 0xff); 
    }

	/**
	 * Returns an attribute of the pixel
	 * @return the pixel's RED value
	 */            
    public int getRed() { 
    	return ((valueARGB >> 16) & 0xff); 
    }
    
	/**
	 * Returns an attribute of the pixel
	 * @return the pixel's GREEN value
	 */            
    public int getGreen() { 
    	return ((valueARGB >> 8) & 0xff); 
    }
    
	/**
	 * Returns an attribute of the pixel
	 * @return the pixel's BLUE value
	 */            
    public int getBlue() { 
    	return ((valueARGB) & 0xff); 
    }
    
    public int getCyan() {
    	return ((valueCMYK >> 24) & 0xff);
    }
    
    
    public int getMagenta() {
    	return ((valueCMYK >> 16) & 0xff);
    }
    
    public int getYellow() {
       	return ((valueCMYK >> 8) & 0xff);
    }
    
    public int getBlack() {
       	return ((valueCMYK) & 0xff);
    }
    
    public int getHue() {
    	return ((valueHSV >> 24) & 0xff);
    }
    
    public int getSaturation() {
    	return ((valueHSV >> 8) & 0xff);
    }
    
    public int getValue() {
    	return ((valueHSV) & 0xff);
    }
    
	/**
	 * Sets an attribute of the pixel
	 * @param valueARGB the pixel's ARGB value
	 */            
    public void setARGB(int valueARGB) { 
		this.valueARGB = valueARGB; 
    }
    
    public void setCMYK(int valueCMYK) {
    	this.valueCMYK = valueCMYK;
    }
    
    public void setHSV(int valueHSV) {
    	this.valueHSV = valueHSV;
    }
    
    public void setCMYK(int cyan, int magenta, int yellow, int black) {
		setCyan(cyan);
		setMagenta(magenta);
		setYellow(yellow);
		setBlack(black);
		CMYK2RGB();
	}
    
    public void setHSV(int hue, int saturation, int value) {
    	setHue(hue);
    	setSaturation(saturation);
    	setValue(value);
    }

	/** Sets the color, ignores null pixel. */
    public void setColor(Pixel p) {
	    if (p == null) return;
	    setARGB(p.valueARGB);
	}
	
	/**
	 * Sets an attribute of the pixel
	 * @param valueAlpha the pixel's ALPHA value
	 */               	
    public void setAlpha(int valueAlpha) { 
    	valueARGB = (valueARGB & 0x00ffffff) | ((valueAlpha & 0xff) << 24);
    }

	/**
	 * Sets an attribute of the pixel
	 * @param valueRed the pixel's RED value
	 */               
	public void setRed(int valueRed) {
		valueARGB = (valueARGB & 0xff00ffff) | ((valueRed & 0xff) << 16);
	}

	/**
	 * Sets an attribute of the pixel
	 * @param valueGreen the pixel's GREEN value
	 */               
	public void setGreen(int valueGreen) { 
		valueARGB = (valueARGB & 0xffff00ff) | ((valueGreen & 0xff) << 8);
	}

	/**
	 * Sets an attribute of the pixel
	 * @param valueBlue the pixel's BLUE value
	 */               
	public void setBlue(int valueBlue) { 
		valueARGB = (valueARGB & 0xffffff00) | ((valueBlue & 0xff));
	}
	
	public void setCyan(int valueCyan) {
		valueCMYK = (valueCMYK & 0x00ffffff) | ((valueCyan & 0xff) << 24);
	}
	
	public void setMagenta(int valueMagenta) {
		valueCMYK = (valueCMYK & 0xff00ffff) | ((valueMagenta & 0xff) << 16);
	}
	
	public void setYellow(int valueYellow) {
		valueCMYK = (valueCMYK & 0xffff00ff) | ((valueYellow & 0xff) << 8);
	}
	
	public void setBlack(int valueBlack) {
		valueCMYK = (valueCMYK & 0xffffff00) | ((valueBlack & 0xff));
	}
	
	public void setHue(int valueHue) {
		valueHSV = (valueHSV & 0x0000ffff) | ((valueHue & 0xff));
	}
	
	public void setSaturation(int valueSaturation) {
		valueHSV = (valueHSV & 0xffff00ff) | ((valueSaturation & 0xff));
	}
	
	public void setValue(int valueValue) {
		valueHSV = (valueHSV & 0xffffff00) | ((valueValue & 0xff));
	}
	
	private void RGB2CMYK() {
		CMYKConversion CMYK = new CMYKConversion();
		CMYK.rgb2cmyk(this.getRed(), this.getGreen(), this.getBlue());
		setCyan(percent2bytes(CMYK.getCyan()));
		setMagenta(percent2bytes(CMYK.getMagenta()));
		setYellow(percent2bytes(CMYK.getYellow()));
		setBlack(percent2bytes(CMYK.getK()));
	}
	
	private void RGB2HSV() {
		HSVConversion HSV = new HSVConversion();
		HSV.rgb2Hsv(this.getRed(), this.getGreen(), this.getBlue());
		setHue((int)HSV.getH());
		setSaturation(percent2bytes(HSV.getS()));
		setValue(percent2bytes(HSV.getV()));
	}
	
	private void CMYK2RGB() {
		RGBConversion RGB = new RGBConversion();
		RGB.cmyk2rgb(this.getCyan(), this.getMagenta(), this.getYellow(), this.getBlack());
		setRed((int)RGB.getR1());
		setGreen((int)RGB.getG1());
		setBlue((int)RGB.getB1());
	}
	
	private void CMYK2HSV() {
		//there's no direct conversion from CMYK to HSV 
		//so we use CMYK to RGB and RGB to CMYK
		//We're not calling existing method to not modify
		//the value already good
		RGBConversion RGB = new RGBConversion();
		RGB.cmyk2rgb(this.getCyan(), this.getMagenta(), this.getYellow(), this.getBlack());
		//conversion from CMYK to RGB done
		//now we take the result and send it to RGB to HSV
		HSVConversion HSV = new HSVConversion();
		HSV.rgb2Hsv((int)RGB.getR1(), (int)RGB.getG1(), (int)RGB.getB1());
		setHue((int)HSV.getH());
		setSaturation(percent2bytes(HSV.getS()));
		setValue(percent2bytes(HSV.getV()));
	}
	
	private void HSV2RGB() {
		RGBConversion RGB = new RGBConversion();
		RGB.hsv2rgb(this.getHue(), this.getSaturation(), this.getValue());
		setRed((int)RGB.getR2());
		setGreen(percent2bytes(RGB.getG2()));
		setBlue(percent2bytes(RGB.getB2()));
	}
	
	private void HSV2CMYK() {
		//There's not straight conversion we need to pass by RGB conversion
		RGBConversion RGB = new RGBConversion();
		RGB.hsv2rgb(this.getHue(), this.getSaturation(), this.getValue());
		
		CMYKConversion CMYK = new CMYKConversion();
		CMYK.rgb2cmyk((int)RGB.getR1(), (int)RGB.getG1(), (int)RGB.getB1());
		setCyan(percent2bytes(CMYK.getCyan()));
		setMagenta(percent2bytes(CMYK.getMagenta()));
		setYellow(percent2bytes(CMYK.getYellow()));
		setBlack(percent2bytes(CMYK.getK()));
		
	}

	/**
	 * Object's toString() method redefinition
	 */               
    public String toString() {
        return new String("(R-" + getRed() + 
                          " G-" + getGreen() + 
                          " B-" + getBlue() + 
                          " A-" + getAlpha() + 
                          ")");
    }

	//Temp/Will see if keeping    
    /**
     * Convert pixel to Color
     * @return color value
     */
    public Color toColor() {
		return new Color((float)getRed() / 255.0F, 
		             	 (float)getGreen() / 255.0F,
		             	 (float)getBlue() / 255.0F);    	
    }
    
    /* 
     * Compute if two colors are the same, based on their ARGB values.
     */ 
    public boolean equals(Object o) {
    	if (o instanceof Pixel) {
    		return (((Pixel)o).getARGB() == getARGB());
    	}
    	return false;
    }
    
    private int percent2bytes(double a) {
    	int x = (int) Math.floor(a*255);
    	return x;
    }
}