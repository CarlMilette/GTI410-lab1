package view;

import java.awt.image.BufferedImage;

import controller.HSVConversion;
import controller.RGBConversion;
import model.ObserverIF;
import model.Pixel;

public class HSVColorMediator extends Object implements SliderObserver, ObserverIF{
	
	final int HUE = 0;
	final int SATURATION = 1;
	final int VALUE = 2;
	final int RED = 0;
	final int GREEN = 1;
	final int BLUE = 2;
	
	ColorSlider hueCS;
	ColorSlider saturationCS;
	ColorSlider valueCS;
	
	BufferedImage hueImage;
	BufferedImage saturationImage;
	BufferedImage valueImage;
	
	int red;
	int green;
	int blue;
	
	int hue;
	int saturation;
	int value;
	
	int imagesWidth;
	int imagesHeight;
	
	ColorDialogResult result;
	
	public HSVColorMediator(ColorDialogResult result, int imagesWidth, int imagesHeight) {
		this.imagesWidth = imagesWidth;
		this.imagesHeight = imagesHeight;
		this.red = result.getPixel().getRed();
		this.green = result.getPixel().getGreen();
		this.blue = result.getPixel().getBlue();
		this.result = result;
		result.addObserver(this);
		
		int[] hsvArray = RGB2HSV(red, green, blue);
		this.hue = hsvArray[HUE];
		this.saturation = hsvArray[SATURATION];
		this.value = hsvArray[VALUE];
		
		hueImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		saturationImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		valueImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		
		computeHueImage(hue, saturation, value);
		computeSaturationImage(hue, saturation, value);
		computeValueImage(hue, saturation, value);
	}
	
	public void update(ColorSlider s , int v) {
		boolean updateHue = false;
		boolean updateSaturation = false;
		boolean updateValue = false;
		if (s == hueCS && v != hue) {
			hue = v;
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
		
		int[] rgbArray = HSV2RGB(hue,saturation,value);
		Pixel pixel = new Pixel(rgbArray[RED], rgbArray[GREEN], rgbArray[BLUE]);
		result.setPixel(pixel);
	}

	private void computeHueImage(int hue, int saturation, int value) {
		int[] rgbArray = HSV2RGB(hue,saturation,value);
		Pixel p=new Pixel(rgbArray[RED],rgbArray[GREEN],rgbArray[BLUE]);
		
		for (int i = 0; i<imagesWidth; ++i) {
			//comme un seul slider gère la couleur on prend la valeur du slider pour
			//recalculer tous les couleur
			int tmpHue = (int) Math.floor(((double)i/(double)imagesWidth)*360.0);
			//on passe le slider en parametre
			int[] tmpRGB = HSV2RGB(tmpHue,saturation,value);
			p.setRed(tmpRGB[RED]);
			p.setGreen(tmpRGB[GREEN]);
			p.setBlue(tmpRGB[BLUE]);
			
			int rgb = p.getARGB();
			//set le resultat de la couleur pour chaque pixel en hauteur(ligne verticale)
			for (int j = 0; j<imagesHeight; ++j) {
				hueImage.setRGB(i, j, p.getARGB());
			}
		}
	}

	private void computeSaturationImage(int hue, int saturation, int value) {
		int[] rgbArray = HSV2RGB(hue,saturation,value);
		Pixel p=new Pixel(rgbArray[RED],rgbArray[GREEN],rgbArray[BLUE]);
		for (int i = 0; i<imagesWidth; ++i) {
			int tmpSaturation = (int)Math.floor(((double)i/(double)imagesWidth)*100.0);
			
			int[] tmpRGB = HSV2RGB(hue,tmpSaturation,value);
			p.setRed(tmpRGB[RED]); 
			p.setGreen(tmpRGB[GREEN]); 
			p.setBlue(tmpRGB[BLUE]); 
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				saturationImage.setRGB(i, j, rgb);
			}
		}
		if (saturationCS != null) {
			saturationCS.update(saturationImage);
		}
	}

	private void computeValueImage(int hue, int saturation, int value) {
		int[] rgbArray = HSV2RGB(hue,saturation,value);
		Pixel p=new Pixel(rgbArray[RED],rgbArray[GREEN],rgbArray[BLUE]);
		for (int i = 0; i<imagesWidth; ++i) {
			int tmpValue = (int)Math.floor(((double)i/(double)imagesWidth)*100.0);
			
			int[] tmpRGB = HSV2RGB(hue,saturation,tmpValue);
			p.setRed(tmpRGB[RED]); 
			p.setGreen(tmpRGB[GREEN]); 
			p.setBlue(tmpRGB[BLUE]); 
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				saturationImage.setRGB(i, j, rgb);
			}
		}
		if (valueCS != null) {
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
		int[] rgbArray = HSV2RGB(hue,saturation,value);
		Pixel currentColor = new Pixel(rgbArray[RED], rgbArray[GREEN], rgbArray[BLUE], 255);
		if(currentColor.getARGB() == result.getPixel().getARGB()) return;

		red = result.getPixel().getRed();
		green = result.getPixel().getGreen();
		blue = result.getPixel().getBlue();
		
		int[] hsvArray = RGB2HSV(red, green, blue);
		
		this.hue = hsvArray[HUE];
		this.saturation = hsvArray[SATURATION];
		this.value = hsvArray[VALUE];
		
		hueCS.setValue(hsvArray[HUE]);
		saturationCS.setValue(hsvArray[SATURATION]);
		valueCS.setValue(hsvArray[VALUE]);
		
		computeHueImage(hue, saturation, value);
		computeSaturationImage(hue, saturation, value);
		computeValueImage(hue, saturation, value);
	}
	
	private int[] RGB2HSV(int red, int green, int blue) {
		HSVConversion HSV = new HSVConversion();
		HSV.rgb2Hsv(red, green, blue);
		int[] tmpHSV = new int [3];
		tmpHSV[HUE] = (int)(HSV.getH());
		tmpHSV[SATURATION] = (int)(HSV.getS()*255);
		tmpHSV[VALUE] = (int)(HSV.getV()*255);
		
		return tmpHSV;
	}
	
	private int[] HSV2RGB(int hue, int saturation, int value) {
		RGBConversion RGB = new RGBConversion();
		int[] rgb = new int[3];
		
		double[] tmpHSV = new double[3];
		tmpHSV[HUE] = ((double)hue/360);
		tmpHSV[SATURATION] = ((double)saturation/100);
		tmpHSV[VALUE] = ((double)value/100);
		
		RGB.hsv2rgb(tmpHSV[HUE], tmpHSV[SATURATION], tmpHSV[VALUE]);
		int[] tmpRGB = new int [3];
		tmpRGB[RED] = (int)RGB.getR2();
		tmpRGB[GREEN] = (int)RGB.getG2();
		tmpRGB[BLUE] = (int)RGB.getB2();
		return tmpRGB;
	}

}	
