package view;

import java.awt.image.BufferedImage;

import controller.HSVConversion;
import controller.RGBConversion;
import model.ObserverIF;
import model.Pixel;

public class HSVColorMediator extends Object implements SliderObserver, ObserverIF{
	
	final int hue = 0;
	final int saturation = 1;
	final int value = 2;
	final int red = 0;
	final int green = 1;
	final int blue = 2;
	
	ColorSlider hueCS;
	ColorSlider saturationCS;
	ColorSlider valueCS;
	
	BufferedImage hueImage;
	BufferedImage saturationImage;
	BufferedImage valueImage;
	
	int imagesWidth;
	int imagesHeight;
	
	int[] rgbArray = new int[3];
	int[] hsvArray = new int[3];
	
	ColorDialogResult result;
	
	public HSVColorMediator(ColorDialogResult result, int imagesWidth, int imagesHeight) {
		this.imagesWidth = imagesWidth;
		this.imagesHeight = imagesHeight;
		this.rgbArray[red] = result.getPixel().getRed();
		this.rgbArray[green] = result.getPixel().getGreen();
		this.rgbArray[blue] = result.getPixel().getBlue();
		this.result = result;
		result.addObserver(this);
		
		RGB2HSV();
		
		hueImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		saturationImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		valueImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		
		computeHueImage();
		computeSaturationImage();
		computeValueImage();
	}
	
	public void update(ColorSlider s , int v) {
		boolean updateHue = false;
		boolean updateSaturation = false;
		boolean updateValue = false;
		if (s == hueCS && v != hsvArray[hue]) {
			hsvArray[hue] = v;
			updateSaturation = true;
			updateValue = true;
		}
		if (s == saturationCS && v != hsvArray[saturation]) {
			hsvArray[saturation] = v;
			updateHue = true;
			updateValue = true;
		}
		if (s == valueCS && v != hsvArray[value]) {
			hsvArray[value] = v;
			updateHue = true;
			updateSaturation = true;
		}
		if (updateHue) {
			computeHueImage();
		}
		if (updateSaturation) {
			computeSaturationImage();
		}
		if (updateValue) {
			computeValueImage();
		}
		
		HSV2RGB();
		Pixel pixel = new Pixel(rgbArray[red], rgbArray[green], rgbArray[blue]);
		result.setPixel(pixel);
	}

	private void computeHueImage() {
		HSV2RGB();
		int C;
		int X;
		int M;
		int redTemp;
		int greenTemp;
		int blueTemp;
		
		for (int i = 0; i<imagesWidth; ++i) {
			//Determine le hue selon l'etat de la boucle puis on convertit le HSV vers le RGB pour etablir le pixel
			hsvArray[hue]= (int)((float)i*(360/(float)imagesWidth));
			C = (value*saturation)*255;
			X = ((value*saturation) * (1 - Math.abs(((hue / 60)%2) - 1)))*255;
			M = (value - (value*saturation))*255;
			if(0<=hue && hue<60){redTemp=C;greenTemp=X;blueTemp=0;}
			else if(60<=hue && hue<120){redTemp=X;greenTemp=C;blueTemp=0;}
			else if(120<=hue && hue<180){redTemp=0;greenTemp=C;blueTemp=X;}
			else if(180<=hue && hue<240){redTemp=0;greenTemp=X;blueTemp=C;}
			else if(240<=hue && hue<300){redTemp=X;greenTemp=0;blueTemp=C;}
			else{ redTemp=C;greenTemp=0;blueTemp=X;}
			Pixel p=new Pixel((int)(redTemp+M),(int)(greenTemp+M),(int)(blueTemp+M));
			//set le resultat de la couleur pour chaque pixel en hauteur(ligne verticale)
			for (int j = 0; j<imagesHeight; ++j) {
				hueImage.setRGB(i, j, p.getARGB());
			}
		}
	}

	private void computeSaturationImage() {
		HSV2RGB();
		Pixel p = new Pixel(rgbArray[red],rgbArray[green], rgbArray[blue], 255); 
		for (int i = 0; i<imagesWidth; ++i) {
			p.setRed((int)(((double)i / (double)imagesWidth)*255.0)); 
			p.setGreen((int)(((double)i / (double)imagesWidth)*255.0)); 
			p.setBlue((int)(((double)i / (double)imagesWidth)*255.0)); 
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				saturationImage.setRGB(i, j, rgb);
			}
		}
		if (saturationCS != null) {
			saturationCS.update(saturationImage);
		}
	}

	private void computeValueImage() {
		HSV2RGB();
		Pixel p = new Pixel(rgbArray[red],rgbArray[green], rgbArray[blue], 255); 
		for (int i = 0; i<imagesWidth; ++i) {
			p.setRed((int)(((double)i / (double)imagesWidth)*255.0)); 
			p.setGreen((int)(((double)i / (double)imagesWidth)*255.0)); 
			p.setBlue((int)(((double)i / (double)imagesWidth)*255.0)); 
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				valueImage.setRGB(i, j, rgb);
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
		return hsvArray[hue];
	}

	public int getSaturation() {
		return hsvArray[saturation];
	}

	public int getValue() {
		return hsvArray[value];
	}
	
	public void update() {
		HSV2RGB();
		Pixel currentColor = new Pixel(rgbArray[red], rgbArray[green], rgbArray[blue], 255);
		if(currentColor.getARGB() == result.getPixel().getARGB()) return;

		rgbArray[red] = result.getPixel().getRed();
		rgbArray[green] = result.getPixel().getGreen();
		rgbArray[blue] = result.getPixel().getBlue();
		
		RGB2HSV();
		
		hueCS.setValue(hsvArray[hue]);
		saturationCS.setValue(hsvArray[saturation]);
		valueCS.setValue(hsvArray[value]);
		
		computeHueImage();
		computeSaturationImage();
		computeValueImage();
	}
	
	private void RGB2HSV() {
		HSVConversion HSV = new HSVConversion();
		HSV.rgb2Hsv(rgbArray[red], rgbArray[green], rgbArray[blue]);
		hsvArray[hue] = (int)(HSV.getH());
		hsvArray[saturation] = (int)(HSV.getS()*255);
		hsvArray[value] = (int)(HSV.getV()*255);
	}
	
	private void HSV2RGB() {
		RGBConversion RGB = new RGBConversion();
		RGB.hsv2rgb(hsvArray[hue], hsvArray[saturation], hsvArray[value]);
		rgbArray[red] = (int)RGB.getR2();
		rgbArray[green] = (int)RGB.getG2();
		rgbArray[blue] = (int)RGB.getB2();
	}

}	
