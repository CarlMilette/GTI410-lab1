package view;

import java.awt.image.BufferedImage;

import model.ObserverIF;
import model.Pixel;
import controller.RGBConversion;
import controller.CMYKConversion;

public class CMYKColorMediator extends Object implements SliderObserver, ObserverIF {

	final int cyan = 0;
	final int magenta = 1;
	final int yellow = 2;
	final int black = 3;
	final int red = 0;
	final int green = 1;
	final int blue = 2;
	ColorSlider cyanCS;
	ColorSlider magentaCS;
	ColorSlider yellowCS;
	ColorSlider blackCS;
	BufferedImage cyanImage;
	BufferedImage magentaImage;
	BufferedImage yellowImage;
	BufferedImage blackImage;
	int imagesWidth;
	int imagesHeight;
	int[] rgbArray = new int[3];
	int[] cmykArray = new int[4];
	ColorDialogResult result;
	
		public CMYKColorMediator(ColorDialogResult result, int imagesWidth, int imagesHeight) {
			this.imagesWidth = imagesWidth;
			this.imagesHeight = imagesHeight;
			this.rgbArray[red] = result.getPixel().getRed();
			this.rgbArray[green] = result.getPixel().getGreen();
			this.rgbArray[blue] = result.getPixel().getBlue();
			this.result = result;
			result.addObserver(this);
			
			RGB2CMYK();
			
			cyanImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
			magentaImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
			yellowImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
			blackImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
			
			computeCyanImage();
			computeMagentaImage();
			computeYellowImage();
			//computeBlackImage();
		}
		
	public void update(ColorSlider s, int v) {
		boolean updateCyan = false;
		boolean updateMagenta = false;
		boolean updateYellow = false;
		//boolean updateBlack = false;
		if (s == cyanCS && v != cmykArray[cyan]) {
			cmykArray[cyan] = v;
			updateMagenta = true;
			updateYellow = true;
		//	updateBlack = true;
		}
		if (s == magentaCS && v != cmykArray[magenta]) {
			cmykArray[magenta] = v;
			updateCyan = true;
			updateYellow = true;
		//	updateBlack = true;
		}
		if (s == yellowCS && v != cmykArray[yellow]) {
			cmykArray[yellow] = v;
			updateCyan = true;
			updateMagenta = true;
		//	updateBlack = true;
		}
//		if (s == blackCS && v != cmykArray[black]) {
//			cmykArray[black] = v;
//			updateCyan = true;
//			updateMagenta = true;
//			updateYellow = true;
//		}
		if (updateCyan) {
			computeCyanImage();
		}
		if (updateMagenta) {
			computeMagentaImage();
		}
		if (updateYellow) {
			computeYellowImage();
		}
//		if (updateBlack) {
//			computeBlackImage();
//		}
		
		System.out.println("C:" + cmykArray[cyan] + "M:" + cmykArray[magenta] + "Y:" + cmykArray[yellow]);
		
		CMYK2RGB();
		Pixel pixel = new Pixel(rgbArray[red], rgbArray[green], rgbArray[blue], 255);
		result.setPixel(pixel);
		
	}
	
	private void computeCyanImage() {
		CMYK2RGB();
		Pixel p = new Pixel(this.rgbArray[red],this.rgbArray[green], this.rgbArray[blue], 255); 
		for (int i = 0; i<imagesWidth; ++i) {
			//p.setRed((int)(255 - cmykArray[black] - (((double)i / (double)imagesWidth)*(255.0 - cmykArray[black])))); 
			p.setRed((int)(255 - (((double)i / (double)imagesWidth)*255.0)));
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				cyanImage.setRGB(i, j, rgb);
			}
		}
		if (cyanCS != null) {
			cyanCS.update(cyanImage);
		}
	}
	
	private void computeMagentaImage() {
		CMYK2RGB();
		Pixel p = new Pixel(this.rgbArray[red],this.rgbArray[green], this.rgbArray[blue], 255); 
		for (int i = 0; i<imagesWidth; ++i) {
			//p.setGreen((int)(255 - cmykArray[black] - (((double)i / (double)imagesWidth)*(255.0 - cmykArray[black])))); 
			p.setGreen((int)(255 - (((double)i / (double)imagesWidth)*255.0))); 
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				magentaImage.setRGB(i, j, rgb);
			}
		}
		if (magentaCS != null) {
			magentaCS.update(magentaImage);
		}
		
	}
	
	private void computeYellowImage() {
		CMYK2RGB();
		Pixel p = new Pixel(this.rgbArray[red],this.rgbArray[green], this.rgbArray[blue], 255); 
		for (int i = 0; i<imagesWidth; ++i) {
			//p.setBlue((int)(255 - cmykArray[black] - (((double)i / (double)imagesWidth)*(255.0 - cmykArray[black])))); 
			p.setBlue((int)(255 - (((double)i / (double)imagesWidth)*255.0)));
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				yellowImage.setRGB(i, j, rgb);
			}
		}
		if (yellowCS != null) {
			yellowCS.update(yellowImage);
		}
			
	}
	
	private void computeBlackImage() {
		CMYK2RGB();
		int blackCSValue;
		int[] rgbTmp = new int[3];
		RGBConversion RGB = new RGBConversion();
		
		Pixel p = new Pixel(this.rgbArray[red],this.rgbArray[green], this.rgbArray[blue], 255); 
		for (int i = 0; i<imagesWidth; ++i) {
			
			blackCSValue = ((int)Math.floor(((double)i / (double)imagesWidth)*255.0));
			RGB.cmyk2rgb(cmykArray[cyan], cmykArray[magenta], cmykArray[yellow], blackCSValue);

			rgbTmp[red] = (int)RGB.getR1();
			rgbTmp[green] = (int)RGB.getG1();
			rgbTmp[blue] = (int)RGB.getB1();
			
			p.setRed(rgbTmp[red]); 
			p.setGreen(rgbTmp[green]);
			p.setBlue(rgbTmp[blue]);
			
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				blackImage.setRGB(i, j, rgb);
			}
		}
		if (blackCS != null) {
			blackCS.update(blackImage);
		}
	}
	
	

	public BufferedImage getCyanImage() {
		return cyanImage;
	}

	public BufferedImage getMagentaImage() {
		return magentaImage;
	}

	public BufferedImage getYellowImage() {
		return yellowImage;
	}

	public BufferedImage getBlackImage() {
		return blackImage;
	}
	
	public void setCyanCS(ColorSlider slider) {
		this.cyanCS = slider;
		slider.addObserver(this);
	}

	public void setMagentaCS(ColorSlider slider) {
		this.magentaCS = slider;
		slider.addObserver(this);
	}

	public void setYellowCS(ColorSlider slider) {
		this.yellowCS = slider;
		slider.addObserver(this);
	}

	public void setBlackCS(ColorSlider slider) {
		this.blackCS = slider;
		slider.addObserver(this);
	}
	
	public int getCyan() {
		return cmykArray[cyan];
	}

	public int getMagenta() {
		return cmykArray[magenta];
	}

	public int getYellow() {
		return cmykArray[yellow];
	}

	public int getBlack() {
		return cmykArray[black];
	}

	@Override
	public void update() {
		CMYK2RGB();
		Pixel currentColor = new Pixel(rgbArray[red], rgbArray[green], rgbArray[blue], 255);
		if(currentColor.getARGB() == result.getPixel().getARGB()) return;
		
		rgbArray[red] = result.getPixel().getRed();
		rgbArray[green] = result.getPixel().getGreen();
		rgbArray[blue] = result.getPixel().getBlue();
		
		RGB2CMYK();
		
		cyanCS.setValue(cmykArray[cyan]);
		magentaCS.setValue(cmykArray[magenta]);
		yellowCS.setValue(cmykArray[yellow]);
		//blackCS.setValue(cmykArray[black]);
		
		computeCyanImage();
		computeMagentaImage();
		computeYellowImage();
		//computeBlackImage();
		
	}
	
	private void RGB2CMYK() {
		CMYKConversion CMYK = new CMYKConversion();
		CMYK.rgb2cmyk(rgbArray[red], rgbArray[green], rgbArray[blue]);
		this.cmykArray[cyan] = (int)(Math.round(CMYK.getCyan()*255));
		this.cmykArray[magenta] = (int)(Math.round(CMYK.getMagenta()*255));
		this.cmykArray[yellow] = (int)(Math.round(CMYK.getYellow()*255));
		this.cmykArray[black] = (int)(Math.round(CMYK.getK()*255));
	}
	
	private void CMYK2RGB() {
		RGBConversion RGB = new RGBConversion();
		RGB.cmyk2rgb(cmykArray[cyan], cmykArray[magenta], cmykArray[yellow], cmykArray[black]);
		this.rgbArray[red]= (int)RGB.getR1();
		this.rgbArray[green] = (int)RGB.getG1();
		this.rgbArray[blue] = (int)RGB.getB1();
	}
	
	
	

}
