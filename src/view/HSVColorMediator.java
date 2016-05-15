package view;

import java.awt.image.BufferedImage;

import model.ObserverIF;
import model.Pixel;

public class HSVColorMediator extends Object implements SliderObserver, ObserverIF {
	ColorSlider hueCS;
	ColorSlider	satCS;
	ColorSlider	valueCS;
	int red;
	int green;
	int blue;
	int hue;
	int sat;
	int value;
	BufferedImage redImage;
	BufferedImage greenImage;
	BufferedImage blueImage;
	BufferedImage hueImage;
	BufferedImage satImage;
	BufferedImage valueImage;
	int imagesWidth;
	int imagesHeight;
	ColorDialogResult result;
	
	HSVColorMediator (ColorDialogResult result, int imagesWidth, int imagesHeight) {
		this.imagesWidth = imagesWidth;
		this.imagesHeight = imagesHeight;
		this.red = result.getPixel().getRed();
		this.green = result.getPixel().getGreen();
		this.blue = result.getPixel().getBlue();
		this.result = result;
		result.addObserver(this);
		
		redImage = new BufferedImage(imagesWidth, imagesWidth, BufferedImage.TYPE_INT_ARGB);
		greenImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		blueImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		
		//call to convert to HSV
		
		computeHueImage(red, green, blue);
		computeSatImage(red, green, blue);
		computeValueImage(red, green, blue);
	}
	
	public void update(ColorSlider s, int v) {
		boolean updateHue = false;
		boolean updateSat = false;
		boolean updateValue = false;
		
		if (s == hueCS && v != hue) {
			hue= v;
			updateSat = true;
			updateValue = true;
		}
		if (s == satCS && v != sat) {
			sat = v;
			updateHue = true;
			updateValue = true;
		}
		if (s == valueCS && v != value) {
			value = v;
			updateHue = true;
			updateSat = true;
		}
		if (updateHue) {
			computeHueImage(red, green, blue);
		}
		if (updateSat) {
			//computeSatImage(red, green, blue);
		}
		if (updateValue) {
			//computeValueImage(red, green, blue);
		}
		
		Pixel pixel = new Pixel(red, green, blue, 255);
		result.setPixel(pixel);		
	}

	public void computeHueImage(int red, int green, int blue) { 
		Pixel p = new Pixel(red, green, blue, 255); 
		for (int i = 0; i<imagesWidth; ++i) {
			p.setRed((int)(((double)i / (double)imagesWidth)*255.0)); 
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				hueImage.setRGB(i, j, rgb);
			}
		}
		if (hueCS != null) {
			hueCS.update(hueImage);
		}
	}
	
	public void computeSatImage(int red, int green, int blue) {
		Pixel p = new Pixel(red, green, blue, 255); //da fuq is 255 alpha value
		for(int i = 0; i<imagesWidth; ++i) {
			p.setGreen((int)(((double)i / (double)imagesWidth)*255.0));
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				satImage.setRGB(i, j, rgb);
			}
		}
		if (satCS != null ) {
			satCS.update(satImage);
		
		}		
	}
	
	public void computeValueImage(int red, int green, int blue) {
		Pixel p = new Pixel(red, green, blue, 255); //da fuq is 255 alpha value
		for(int i = 0; i<imagesWidth; ++i) {
			p.setRed((int)(((double)i / (double)imagesWidth)*255.0));
			p.setGreen((int)(((double)i / (double)imagesWidth)*255.0));
			p.setBlue((int)(((double)i / (double)imagesWidth)*255.0));
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				valueImage.setRGB(i, j, rgb);
			}
		}
		if (valueCS != null ) {
			valueCS.update(valueImage);
		
		}		
	}
	
	public void update() {
		Pixel currentColor = new Pixel(red, green, blue, 255);
		if(currentColor.getARGB() == result.getPixel().getARGB())return;
		
		red = result.getPixel().getRed();
		green = result.getPixel().getGreen();
		blue = result.getPixel().getBlue();
		
		hueCS.setValue(red);
		satCS.setValue(green);
		valueCS.setValue(blue);
		computeHueImage(red, green, blue);
		computeSatImage(red, green, blue);
		computeValueImage(red, green, blue);
		
	}

	
	

}
