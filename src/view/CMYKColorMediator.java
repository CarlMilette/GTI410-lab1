package view;

import java.awt.image.BufferedImage;

import model.ObserverIF;
import model.Pixel;
import controller.RGBConversion;
import controller.CMYKConversion;

public class CMYKColorMediator extends Object implements SliderObserver, ObserverIF {
	
    final int CYAN = 0;
    final int MAGENTA = 1;
    final int YELLOW = 2;
    final int BLACK = 3;
    final int RED = 0;
    final int GREEN = 1;
    final int BLUE = 2;

	ColorSlider cyanCS;
	ColorSlider magentaCS;
	ColorSlider yellowCS;
	ColorSlider blackCS;
	
	BufferedImage cyanImage;
	BufferedImage magentaImage;
	BufferedImage yellowImage;
	BufferedImage blackImage;
	
	int red;
	int green;
	int blue;
	
	int cyan;
	int magenta;
	int yellow;
	int black;
	
	int imagesWidth;
	int imagesHeight;
	ColorDialogResult result;
	
		public CMYKColorMediator(ColorDialogResult result, int imagesWidth, int imagesHeight) {
			this.imagesWidth = imagesWidth;
			this.imagesHeight = imagesHeight;
			this.red = result.getPixel().getRed();
			this.green = result.getPixel().getGreen();
			this.blue = result.getPixel().getBlue();
			this.result = result;
			result.addObserver(this);
			
			int[] cmykArray = RGB2CMYK(red,green,blue);
			this.cyan = cmykArray[CYAN];
			this.magenta = cmykArray[MAGENTA];
			this.yellow = cmykArray[YELLOW];
			this.black = cmykArray[BLACK];
			
			cyanImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
			magentaImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
			yellowImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
			blackImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
			
			computeCyanImage(cyan,magenta,yellow,black);
			computeMagentaImage(cyan,magenta,yellow,black);
			computeYellowImage(cyan,magenta,yellow,black);
			computeBlackImage(cyan,magenta,yellow,black);
		}
		
	public void update(ColorSlider s, int v) {
		boolean updateCyan = false;
		boolean updateMagenta = false;
		boolean updateYellow = false;
		boolean updateBlack = false;
		if (s == cyanCS && v != cyan) {
			cyan = v;
			updateMagenta = true;
			updateYellow = true;
			updateBlack = true;
		}
		if (s == magentaCS && v != magenta) {
			magenta = v;
			updateCyan = true;
			updateYellow = true;
			updateBlack = true;
		}
		if (s == yellowCS && v != yellow) {
			yellow = v;
			updateCyan = true;
			updateMagenta = true;
			updateBlack = true;
		}
		if (s == blackCS && v != black) {
			black = v;
			updateCyan = true;
			updateMagenta = true;
			updateYellow = true;
		}
		if (updateCyan) {
			computeCyanImage(cyan,magenta,yellow,black);
		}
		if (updateMagenta) {
			computeMagentaImage(cyan,magenta,yellow,black);
		}
		if (updateYellow) {
			computeYellowImage(cyan,magenta,yellow,black);
		}
		if (updateBlack) {
			computeBlackImage(cyan,magenta,yellow,black);
		}
		

		int[] rgbArray = CMYK2RGB(cyan,magenta,yellow,black);
		Pixel pixel = new Pixel(rgbArray[RED],rgbArray[GREEN],rgbArray[BLUE],255);
		result.setPixel(pixel);
		
	}
	
	private void computeCyanImage(int cyan, int magenta, int yellow, int black) {
		//System.out.println("C: " + cyan + " M:" + magenta + " Y:" + yellow + " B:" + black);
		int rgbArray[] = CMYK2RGB(cyan,magenta,yellow,black);
		Pixel p = new Pixel(rgbArray[RED], rgbArray[GREEN], rgbArray[BLUE], 255); 
		for (int i = 0; i<imagesWidth; ++i) {
			p.setRed((int)(255 - black - (((double)i / (double)imagesWidth)*(255.0 - black)))); 
			//p.setRed((int)(((double)i / (double)imagesWidth)*255.0));
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				cyanImage.setRGB(i, j, rgb);
			}
		}
		if (cyanCS != null) {
			cyanCS.update(cyanImage);
		}
	}
	
	private void computeMagentaImage(int cyan, int magenta, int yellow, int black) {
		int rgbArray[] = CMYK2RGB(cyan,magenta,yellow,black);
		Pixel p = new Pixel(rgbArray[RED], rgbArray[GREEN], rgbArray[BLUE], 255); 
		for (int i = 0; i<imagesWidth; ++i) {
			p.setGreen((int)(255 - black - (((double)i / (double)imagesWidth)*(255.0 - black)))); 
			//p.setGreen((int)(((double)i / (double)imagesWidth)*255.0)); 
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				magentaImage.setRGB(i, j, rgb);
			}
		}
		if (magentaCS != null) {
			magentaCS.update(magentaImage);
		}
		
	}
	
	private void computeYellowImage(int cyan, int magenta, int yellow, int black) {
		int rgbArray[] = CMYK2RGB(cyan,magenta,yellow,black);
		Pixel p = new Pixel(rgbArray[RED], rgbArray[GREEN], rgbArray[BLUE], 255); 
		for (int i = 0; i<imagesWidth; ++i) {
			p.setBlue((int)(255 - black - (((double)i / (double)imagesWidth)*(255.0 - black)))); 
			//p.setBlue((int)(((double)i / (double)imagesWidth)*255.0));
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				yellowImage.setRGB(i, j, rgb);
			}
		}
		if (yellowCS != null) {
			yellowCS.update(yellowImage);
		}
			
	}
	
	private void computeBlackImage(int cyan, int magenta, int yellow, int black) {
		int rgbArray[] = CMYK2RGB(cyan,magenta,yellow,black);
		Pixel p = new Pixel(rgbArray[RED], rgbArray[GREEN], rgbArray[BLUE], 255); 
		for (int i = 0; i<imagesWidth; ++i) {
			
			int blackValue = (int)Math.round((((double)i/(double)imagesWidth)*255.0));
			
			int[] rgbTmp = CMYK2RGB(cyan,magenta,yellow,blackValue);
			p.setRed(rgbTmp[RED]); 
			p.setGreen(rgbTmp[GREEN]);
			p.setBlue(rgbTmp[BLUE]);
			
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
		cyanCS = slider;
		slider.addObserver(this);
	}

	public void setMagentaCS(ColorSlider slider) {
		magentaCS = slider;
		slider.addObserver(this);
	}

	public void setYellowCS(ColorSlider slider) {
		yellowCS = slider;
		slider.addObserver(this);
	}

	public void setBlackCS(ColorSlider slider) {
		blackCS = slider;
		slider.addObserver(this);
	}
	
	public int getCyan() {
		return cyan;
	}

	public int getMagenta() {
		return magenta;
	}

	public int getYellow() {
		return yellow;
	}

	public int getBlack() {
		return black;
	}

	@Override
	public void update() {
		int[] rgbArray = CMYK2RGB(cyan,magenta,yellow,black);
		Pixel currentColor = new Pixel(rgbArray[RED],rgbArray[GREEN], rgbArray[BLUE], 255);
		if(currentColor.getARGB() == result.getPixel().getARGB()) return;
		
		red = result.getPixel().getRed();
		green = result.getPixel().getGreen();
		blue = result.getPixel().getBlue();
		
		int[] cmykArray = RGB2CMYK(this.red, this.green, this.blue);
		
		this.cyan = cmykArray[CYAN];
		this.magenta = cmykArray[MAGENTA];
		this.yellow = cmykArray[YELLOW];
		this.black = cmykArray[BLACK];
		
		cyanCS.setValue(cmykArray[CYAN]);
		magentaCS.setValue(cmykArray[MAGENTA]);
		yellowCS.setValue(cmykArray[YELLOW]);
		blackCS.setValue(cmykArray[BLACK]);
	
		computeCyanImage(cyan,magenta,yellow,black);
		computeMagentaImage(cyan,magenta,yellow,black);
		computeYellowImage(cyan,magenta,yellow,black);
		computeBlackImage(cyan,magenta,yellow,black);
		
	}
	
	private int[] RGB2CMYK(int red, int green, int blue) {
		CMYKConversion CMYK = new CMYKConversion();
		int[] cmyk = new int[4];
		System.out.println("R:" + red + "G:" + green + "B:" + blue);
		CMYK.rgb2cmyk(red, green, blue);
		System.out.println("C: " + (int)(CMYK.getCyan()*255) +" M:"+ (int)(CMYK.getMagenta()*255) +" Y:"+ (int)(CMYK.getYellow()*255) +" K:"+ (int)(CMYK.getK()*255));
		cmyk[CYAN] = (int)(CMYK.getCyan()*255);
		cmyk[MAGENTA] = (int)(CMYK.getMagenta()*255);
		cmyk[YELLOW] = (int)(CMYK.getYellow()*255);
		cmyk[BLACK] = (int)(CMYK.getK()*255);
		return cmyk;
	}
	
	private int[] CMYK2RGB(int cyan, int magenta, int yellow, int black) {
		RGBConversion RGB = new RGBConversion();
		int[] rgb = new int[3];
		
		//conversion du CMYK qui est en int vers des double accepter par l'algo de conversion
		double[] tmpCmyk = new double [4];
		tmpCmyk[CYAN] = ((double)cyan/255);
		tmpCmyk[MAGENTA] = ((double)magenta/255);
		tmpCmyk[YELLOW] = ((double)yellow/255);
		tmpCmyk[BLACK] = ((double)black/255);
		//System.out.println("In CMYK2RGB C:" + cyan + " M:" + magenta + " Y:" + yellow + " B:" +black);
		//System.out.println("In CMYK2RGB C:" + tmpCmyk[CYAN] + " M:" + tmpCmyk[MAGENTA] + " Y:" + tmpCmyk[YELLOW] + " B:" + tmpCmyk[BLACK]);

		RGB.cmyk2rgb(tmpCmyk[CYAN], tmpCmyk[MAGENTA], tmpCmyk[YELLOW], tmpCmyk[BLACK]);
		rgb[RED] = (RGB.getR());
		rgb[GREEN] = (RGB.getG());
		rgb[BLUE] = (RGB.getB());
		return rgb;
	}
}
