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
    /** CMYK Pixel value */
    private int valueCMYK;
    /** HSV Pixel value*/
    private int valueHSV;
    
    /**
     * Pixel default constructor
     */
    public Pixel() {
		valueARGB = 0;
    }
    
	/**
	 * Pixel constructor with a specified ARGB value
	 * @param valueARGB the pixel's ARGB value
	 */
    public Pixel(int valueARGB) {
        this.valueARGB = valueARGB;
    }
    
    public Pixel(int rValue, int gValue, int bValue) {
    	setRed(rValue);
    	setGreen(gValue);
    	setBlue(bValue);
    	setAlpha(255);
    	RGB2CMYK(rValue, gValue, bValue);
    }
    
	public Pixel(int rValue, int gValue, int bValue, int alpha) {
		setRed(rValue);
		setGreen(gValue);
		setBlue(bValue);
		setAlpha(alpha);
		RGB2CMYK(rValue, gValue, bValue);
		RGB2HSV(rValue, gValue, bValue);
	}
    
    public Pixel(PixelDouble pixel) {
		setRed((int)pixel.getRed());
		setGreen((int)pixel.getGreen());
		setBlue((int)pixel.getBlue());
		setAlpha((int)pixel.getAlpha());
    }
    
	/**
	 * Returns an attribute of the pixel
	 * @return the pixel's ARGB value
	 */    
    public int getARGB() { 
    	return (valueARGB); 
    }
    
    /**
	 * Returns an attribute of the pixel
	 * @return the pixel's CMYK value
	 */ 
    public int getCMYK() {
    	return (valueCMYK);
    }
    
    /**
	 * Returns an attribute of the pixel
	 * @return the pixel's HSV value
	 */ 
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
    
    /**
	 * Returns an attribute of the pixel
	 * @return the pixel's CYAN value
	 */   
    public int getCyan() {
    	return ((valueCMYK >> 24) & 0xff);
    }
    
    /**
	 * Returns an attribute of the pixel
	 * @return the pixel's MAGENTA value
	 */   
    public int getMagenta() {
    	return ((valueCMYK >> 16) & 0xff);
    }
    
    /**
	 * Returns an attribute of the pixel
	 * @return the pixel's YELLOW value
	 */   
    public int getYellow() {
    	return ((valueCMYK >> 8 ) & 0xff);
    }
    
    /**
	 * Returns an attribute of the pixel
	 * @return the pixel's BLACK value
	 */   
    public int getBlack() {
    	return ((valueCMYK) & 0xff);
    }
    
    /**
	 * Returns an attribute of the pixel
	 * @return the pixel's HUE value
	 */   
    public int getHue() {
    	return ((valueHSV >> 16) & 0xff);
    }
    
    /**
	 * Returns an attribute of the pixel
	 * @return the pixel's SATURATION value
	 */   
    public int getSat() {
    	return ((valueHSV >> 8) & 0xff);
    }
    
    /**
	 * Returns an attribute of the pixel
	 * @return the pixel's VALUE value
	 */   
    public int getValue() {
    	return ((valueHSV) & 0xff);
    }
    
	/**
	 * Sets an attribute of the pixel
	 * @param valueARGB the pixel's ARGB value
	 */            
    public void setARGB(int valueARGB) { 
		this.valueARGB = valueARGB; 
		RGB2CMYK(getRed(), getGreen(), getBlue());
		RGB2HSV(getRed(), getGreen(), getBlue());
		
		//add conversion to HSV
    }
    
    /**
     * Sets an attribute of the pixel
     * @param value CMYK the pixel's CMYK value
     */
    public void setCMYK(int valueCMYK) {
    	this.valueCMYK = valueCMYK;
    	//CMYK2RGB
    	//CMYK2HSV
    }
    
    /**
     * Sets CMYK attribute of the pixel
     * @param value of Cyan
     * @param value of Magenta
     * @param value of Yellow
     * @param value of Black
     */
    public void setCMYK(int cyan, int magenta, int yellow, int black) {
    	setCyan(cyan);
    	setMagenta(magenta);
    	setYellow(yellow);
    	setBlack(black);
    	CMYK2RBG(cyan, magenta, yellow, black);
    	//CMYK2HSV
    }
    
    /**
     * Sets an attribute of the pixel
     * @param value HSV the pixel's CMYK value
     */
    public void setHSV(int valueHSV) {
    	this.valueHSV = valueHSV;
    	//HSV2RGB
    	//HSV2CMYK
    }
    
    /**
     * Sets HSV attribute of the pixel
     * @param value of Hue
     * @param value of Saturation
     * @param value of Value
     */
    public void setHSV(int hue, int saturation, int value) {
    	setHue(hue);
    	setSat(saturation);
    	setValue(value);
    	//HSV2RGB
    	//HSV2CMYK
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
	 *  Sets an attribute of the pixel
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
	
	/**
	 * Returns an attribute of the pixel
	 * @return the pixel's CYAN value
	 */   
	public void setCyan(int valueCyan) {
		valueCMYK = (valueCMYK & 0x00ffffff) | ((valueCyan & 0xff) << 24);
	}
	
	/**
	 * Returns an attribute of the pixel
	 * @return the pixel's MAGENTA value
	 */   
	public void setMagenta(int valueMagenta) {
		valueCMYK = (valueCMYK & 0xff00ffff) | ((valueMagenta & 0xff) << 16);
	}
	
	/**
	 * Returns an attribute of the pixel
	 * @return the pixel's YELLOW value
	 */   
	public void setYellow(int valueYellow) {
		valueCMYK = (valueCMYK & 0xffff00ff) | ((valueYellow & 0xff) << 8);
	}
	
	/**
	 * Returns an attribute of the pixel
	 * @return the pixel's BLACK value
	 */   
	public void setBlack(int valueBlack) {
		valueCMYK = (valueCMYK & 0xffffff00) | ((valueBlack & 0xff));
	}
	
	/**
	 * Returns an attribute of the pixel
	 * @return the pixel's HUE value
	 */   
	public void setHue(int valueHue) {
		/* The Hue is expressed with an angle value from 0 to 360 so we need the 3 first
		 * bytes to have the possibility to save it.
		 * to stay as close as possible to the other color space method we save the HSV
		 * on 7 bytes instead of 8 because we don't need the 8th bytes
		 */
		valueHSV = (valueHSV & 0x000ffff) | ((valueHue & 0xff) << 16);
	}
	
	/**
	 * Returns an attribute of the pixel
	 * @return the pixel's SATURATION value
	 */   
	public void setSat(int valueSat) {
		valueHSV = (valueHSV & 0xfff00ff) | ((valueSat & 0xff) << 8);
	}
	
	/**
	 * Returns an attribute of the pixel
	 * @return the pixel's VALUE value
	 */   
	public void setValue(int valueValue) {
		valueHSV = (valueHSV & 0xfffff00) | ((valueValue & 0xff));
	}

	/**
	 * Convert from RGB color space to CMYK
	 * @param red
	 * @param green
	 * @param blue
	 */
	private void RGB2CMYK(int red, int green, int blue) {
		CMYKConversion CMYK = new CMYKConversion();
		CMYK.rgb2cmyk(red, green, blue);
		setCyan((int)CMYK.getCyan());
		setMagenta((int)CMYK.getMagenta());
		setYellow((int)CMYK.getYellow());
		setBlack((int)CMYK.getK());
	}
	
	/**
	 * Convert from RGB color space to HSV
	 * @param red
	 * @param green
	 * @param blue
	 */
	private void RGB2HSV(int red, int green, int blue) {
		HSVConversion HSV = new HSVConversion();
		HSV.rgb2Hsv(red, green, blue);
		setHue((int)HSV.getH());
		setSat((int)HSV.getS());
		setValue((int)HSV.getV());
	}
	
	/**
	 * Convert from CMYK color space to RGB
	 * @param cyan
	 * @param magenta
	 * @param yellow
	 * @param black
	 */
	private void CMYK2RBG(int cyan, int magenta, int yellow, int black) {
		RGBConversion RGB = new RGBConversion();
		RGB.cmyk2rgb(cyan, magenta, yellow, black);
		setRed((int)RGB.getR1());
		setGreen((int)RGB.getG1());
		setBlue((int)RGB.getB1());
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
}