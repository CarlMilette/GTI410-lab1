package view;

import java.awt.image.BufferedImage;

import model.ObserverIF;
import model.Pixel;

class CMYKColorMediator extends Object implements SliderObserver, ObserverIF {
	ColorSlider cyanCS;
	ColorSlider magentaCS;
	ColorSlider yellowCS;
	ColorSlider	blackCS;
	int red;
	int green;
	int blue;
	int cyan;
	int magenta;
	int yellow;
	int black;
	BufferedImage redImage;
	BufferedImage greenImage;
	BufferedImage blueImage;
	BufferedImage cyanImage;
	BufferedImage magentaImage;
	BufferedImage yellowImage;
	BufferedImage blackImage;
	int imagesWidth;
	int imagesHeight;
	ColorDialogResult result;
	
	CMYKColorMediator (ColorDialogResult result, int imagesWidth, int imagesHeight) {
			this.imagesWidth = imagesWidth;
			this.imagesHeight = imagesHeight;
			this.cyan = result.getPixel().getCyan();
			this.magenta = result.getPixel().getMagenta();
			this.yellow = result.getPixel().getYellow();
			this.black = result.getPixel().getBlack();
			this.result = result;
			result.addObserver(this);
			
			//redImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
			//greenImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
			//blueImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
			
			cyanImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
			magentaImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
			yellowImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
			
			//place result in acording *Image variable
			computeCyanImage(cyan, magenta, yellow, black);
			computeMagentaImage(cyan, magenta, yellow, black);
			computeYellowImage(cyan, magenta, yellow, black);
			

	}
	
	public void update(ColorSlider s, int v) {
		boolean updateCyan = false;
		boolean updateMagenta = false;
		boolean updateYellow = false;
		if (s == cyanCS && v != cyan) {
			cyan = v;
			updateMagenta = true;
			updateYellow = true;
		}
		if (s == magentaCS && v != magenta) {
			magenta = v;
			updateCyan = true;
			updateYellow = true;
		}
		if (s == yellowCS && v != yellow) {
			yellow = v;
			updateCyan = true;
			updateMagenta = true;
		}
		if (updateCyan) {
			computeCyanImage(cyan, magenta, yellow, black);
		}
		if (updateMagenta) {
			computeMagentaImage(cyan, magenta, yellow, black);
		}
		if (updateYellow) {
			computeYellowImage(cyan, magenta, yellow, black);
		}
		
		Pixel pixel = new Pixel();
		pixel.setCyan(cyan);
		pixel.setMagenta(magenta);
		pixel.setYellow(yellow);
		pixel.setBlack(black);
		result.setPixel(pixel);
		
	}
	
	public void computeCyanImage(int cyan, int magenta, int yellow, int black) {
		Pixel tmp = new Pixel();
		tmp.setCMYK(cyan,magenta,yellow,black);
		Pixel p = new Pixel(tmp.getRed(), tmp.getGreen(), tmp.getBlue(), 255); 
		for (int i = 0; i<imagesWidth; ++i) {
			p.setCyan((int)(((double)i / (double)imagesWidth)*255.0));
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				cyanImage.setRGB(i, j, rgb);
			}
		}
		if (cyanCS != null) {
			cyanCS.update(cyanImage);
		}
	}
	
	public void computeMagentaImage(int cyan, int magenta, int yellow, int black) {
		Pixel tmp = new Pixel();
		tmp.setCMYK(cyan,magenta,yellow,black);
		int red = tmp.getRed();
		int green = tmp.getGreen();
		int blue = tmp.getBlue();
		Pixel p = new Pixel(red, green, blue, 255);
		for(int i = 0; i<imagesWidth; ++i) {
			p.setCyan((int)(((double)i / (double)imagesWidth)*255.0));
			p.setMagenta((int)(((double)i / (double)imagesWidth)*255.0));
			p.setYellow((int)(((double)i / (double)imagesWidth)*255.0));
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				magentaImage.setRGB(i, j, rgb);
			}
		}
		if (magentaCS != null ) {
			magentaCS.update(magentaImage);
		
		}		
	}
	
	public void computeYellowImage(int cyan, int magenta, int yellow, int black) {
		Pixel tmp = new Pixel();
		tmp.setCMYK(cyan,magenta,yellow,black);
		int red = tmp.getRed();
		int green = tmp.getGreen();
		int blue = tmp.getBlue();
		Pixel p = new Pixel(red, green, blue, 255);
		for(int i = 0; i<imagesWidth; ++i) {
			p.setCyan((int)(((double)i / (double)imagesWidth)*255.0));
			p.setMagenta((int)(((double)i / (double)imagesWidth)*255.0));
			p.setYellow((int)(((double)i / (double)imagesWidth)*255.0));
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				yellowImage.setRGB(i, j, rgb);
			}
		}
		if (yellowCS != null ) {
			yellowCS.update(yellowImage);
		
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

	public void update() {
		Pixel currentColor = new Pixel(red, green, blue, 255);
		if(currentColor.getARGB() == result.getPixel().getARGB())return;
		
		cyan = result.getPixel().getCyan();
		magenta = result.getPixel().getMagenta();
		yellow = result.getPixel().getYellow();
		black = result.getPixel().getBlack();
		
		cyanCS.setValue(cyan);
		magentaCS.setValue(magenta);
		yellowCS.setValue(yellow);
		computeCyanImage(cyan, magenta, yellow, black);
		computeMagentaImage(cyan, magenta, yellow, black);
		computeYellowImage(cyan, magenta, yellow, black);
		
	}

	

}
