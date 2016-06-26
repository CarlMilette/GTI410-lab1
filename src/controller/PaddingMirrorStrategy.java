package controller;

import model.ImageDouble;
import model.ImageX;
import model.Pixel;
import model.PixelDouble;

public class PaddingMirrorStrategy extends PaddingStrategy{

	//abstract method from PaddingStrategy
	public Pixel pixelAt(ImageX image, int x, int y) {
		// manage x value
		if (x < 0 )
		{
			x = 1;
		}
		
		if (x >= image.getImageWidth())
		{
			x = image.getImageWidth() - 1;
		}
		
		
		//manage y value
		if (y < 0 )
		{
			y = 1;
		}
		
		if (y >= image.getImageHeight())
		{
			y = image.getImageHeight() - 1;
		}
		
		return image.getPixel(x, y);
	}

	//abstract method from PaddingStrategy
	public PixelDouble pixelAt(ImageDouble image, int x, int y) {
		// manage x value
		if (x < 0 )
		{
			x = 1;
		}
		
		if (x >= image.getImageWidth())
		{
			x = image.getImageWidth() - 1;
		}
		
		
		//manage y value
		if (y < 0 )
		{
			y = 1;
		}
		
		if (y >= image.getImageHeight())
		{
			y = image.getImageHeight() - 1;
		}
		return image.getPixel(x, y);
	}
	
	

}
