package view;

import java.awt.image.BufferedImage;

import model.ObserverIF;
import model.Pixel;

public class HSVColorMediator extends Object implements SliderObserver, ObserverIF {
	ColorSlider hueCS;
	ColorSlider	saturationCS;
	ColorSlider	valueCS;
	int red;
	int green;
	int blue;
	int hue;
	int saturation;
	int value;
	BufferedImage hueImage;
	BufferedImage saturationImage;
	BufferedImage valueImage;
	int imagesWidth;
	int imagesHeight;
	ColorDialogResult result;
	
	HSVColorMediator (ColorDialogResult result, int imagesWidth, int imagesHeight) {
		this.imagesWidth = imagesWidth;
		this.imagesHeight = imagesHeight;
		this.hue = result.getPixel().getHue();
		this.saturation = result.getPixel().getSaturation();
		this.value = result.getPixel().getValue();
		this.result = result;
		result.addObserver(this);
		
		hueImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		saturationImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		valueImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		
		
		computeHueImage(hue, saturation, value);
		computeSaturationImage(hue, saturation, value);
		computeValueImage(hue, saturation, value);
	}
	
	public void update(ColorSlider s, int v) {
		boolean updateHue = false;
		boolean updateSaturation = false;
		boolean updateValue = false;
		
		if (s == hueCS && v != hue) {
			hue= v;
			updateSaturation = true;
			updateValue = true;
		}
		if (s == saturationCS && v != saturation) {
			saturation = v;
			updateHue = true;
			updateValue = true;
		}
		if (s == valueCS && v != value) {
			value = v;
			updateHue = true;
			updateSaturation = true;
		}
		if (updateHue) {
			computeHueImage(hue, saturation, value);
		}
		if (updateSaturation) {
			computeSaturationImage(hue, saturation, value);
		}
		if (updateValue) {
			computeValueImage(hue, saturation, value);
		}
		
		Pixel pixel = new Pixel();
		pixel.setHue(hue);
		pixel.setSaturation(saturation);
		pixel.setValue(value);
		result.setPixel(pixel);		
	}

	public void computeHueImage(int hue, int saturation, int value) { 
		getAllValue();
		Pixel tmp = new Pixel();
		tmp.setHSV(hue,saturation,value);
		Pixel p = new Pixel(tmp.getRed(), tmp.getGreen(), tmp.getBlue(), 255); 
		for (int i = 0; i<imagesWidth; ++i) {
			//hue is not set on bytes value 255 but on angle value 360
			p.setHue((int)(((double)i / (double)imagesWidth)*360.0)); 
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				hueImage.setRGB(i, j, rgb);
			}
		}
		if (hueCS != null) {
			hueCS.update(hueImage);
		}
	}
	
	public void computeSaturationImage(int hue, int saturation, int value) {
		Pixel tmp = new Pixel();
		tmp.setHSV(hue,saturation,value);
		Pixel p = new Pixel(tmp.getRed(), tmp.getGreen(), tmp.getBlue(), 255); 
		for(int i = 0; i<imagesWidth; ++i) {
			p.setSaturation((int)(((double)i / (double)imagesWidth)*255.0));
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				saturationImage.setRGB(i, j, rgb);
			}
		}
		if (saturationCS != null ) {
			saturationCS.update(saturationImage);
		
		}		
	}
	
	public void computeValueImage(int hue, int saturation, int value) {
		Pixel tmp = new Pixel();
		tmp.setHSV(hue,saturation,value);
		Pixel p = new Pixel(tmp.getRed(), tmp.getGreen(), tmp.getBlue(), 255); 
		for(int i = 0; i<imagesWidth; ++i) {
			p.setValue((int)(((double)i / (double)imagesWidth)*255.0));
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				valueImage.setRGB(i, j, rgb);
			}
		}
		if (valueCS != null ) {
			valueCS.update(valueImage);
		
		}		
	}
	
	
	public BufferedImage getHueImage() {
		return hueImage;
	}

	public BufferedImage getSaturationImage() {
		return saturationImage;
	}

	public BufferedImage getValueImage() {
		return valueImage;
	}
	
	public void setHueCS(ColorSlider slider) {
		hueCS = slider;
		slider.addObserver(this);
	}

	public void setSaturationCS(ColorSlider slider) {
		saturationCS = slider;
		slider.addObserver(this);
	}

	public void setValueCS(ColorSlider slider) {
		valueCS = slider;
		slider.addObserver(this);
	}
	
	public int getHue() {
		return hue;
	}

	public int getSaturation() {
		return saturation;
	}

	public int getValue() {
		return value;
	}

	public void update() {
		Pixel tmp = new Pixel();
		tmp.setHSV(hue, saturation, value);
		Pixel currentColor = new Pixel(tmp.getRed(), tmp.getGreen(), tmp.getBlue(), 255);
		if(currentColor.getHSV() == result.getPixel().getHSV())return;
		
		hue = result.getPixel().getHue();
		saturation = result.getPixel().getSaturation();
		value = result.getPixel().getValue();
		
		hueCS.setValue(hue);
		saturationCS.setValue(saturation);
		valueCS.setValue(value);
		computeHueImage(hue, saturation, value);
		computeSaturationImage(hue, saturation, value);
		computeValueImage(hue, saturation, value);
		
	}

	private void getAllValue() {
		System.out.println("H : " + hue + " S : " + saturation + " V : " + value);
	}
	
	

}
