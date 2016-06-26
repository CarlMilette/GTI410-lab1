package controller;

import model.*;
import controller.Coordinates;

public class CustomFilter3x3 extends Filter {
	private double matrix[][] = null;

	public CustomFilter3x3(PaddingStrategy paddingStrategy,
			ImageConversionStrategy conversionStrategy) {
		super(paddingStrategy, conversionStrategy);
		matrix = new double[3][3];
		
		matrix[0][0] = matrix[1][0] = matrix[2][0] = 
		matrix[0][1] = matrix[1][1] = matrix[2][1] =
		matrix[0][2] = matrix[1][2] = matrix[2][2] = (1.0/9.0);
	}
	
	/**
	 * Filters an ImageX and returns a ImageDouble.
	 */
	public ImageDouble filterToImageDouble(ImageX image) {
		return filter(conversionStrategy.convert(image));
	}
	
	/**
	 * Filters an ImageDouble and returns a ImageDouble.
	 */	
	public ImageDouble filterToImageDouble(ImageDouble image) {
		return filter(image);
	}
	
	/**
	 * Filters an ImageX and returns an ImageX.
	 */	
	public ImageX filterToImageX(ImageX image) {
		ImageDouble filtered = filter(conversionStrategy.convert(image)); 
		return conversionStrategy.convert(filtered);
	}
	
	/**
	 * Filters an ImageDouble and returns a ImageX.
	 */	
	public ImageX filterToImageX(ImageDouble image) {
		ImageDouble filtered = filter(image); 
		return conversionStrategy.convert(filtered);		
	}
	
	//algo for the customfilter
	private double getCustomFilter(){
		double result = 0;
		double total = 0;
		
		for (int i = 0 ; i <= 2 ; i++){
			for(int j = 0 ; j <= 2 ; j++){
				total += matrix[i][j];
			}
		}
		
		if ( total != 0 )
		{
			result = 1/total;
		}else{
			result = 1;
		}
		
		return result;
	}
	
	//apply the filter, call from filteringTransformer
	//This method is not in filteringTransformer because we need to modify the value
	//from the matrix and she's not accessible from the FilteringTransfermer
	public void setCustomFilter(Coordinates coordinates, float value){
		matrix[coordinates.getRow() - 1][coordinates.getColumn() - 1] = value;
	}
	
	/*
	 * Filter Implementation 
	 */
	private ImageDouble filter(ImageDouble image) {
		
		//calling the method that actually contain the filter
		double customFilter = getCustomFilter();
		
		int imageWidth = image.getImageWidth();
		int imageHeight = image.getImageHeight();
		ImageDouble newImage = new ImageDouble(imageWidth, imageHeight);
		PixelDouble newPixel = null;
		double result = 0; 
	
		for (int x = 0; x < imageWidth; x++) {
			for (int y = 0; y < imageHeight; y++) {
				newPixel = new PixelDouble();
			
				//*******************************
				// RED
				for (int i = 0; i <= 2; i++) {
					for (int j = 0; j <= 2; j++) {
						result += customFilter * matrix[i][j] * getPaddingStrategy().pixelAt(image, 
																				    x+(i-1), 
																				    y+(j-1)).getRed();
					}
				}
				
				newPixel.setRed(result);
				result = 0;
						
				//*******************************
				// Green
				for (int i = 0; i <= 2; i++) {
					for (int j = 0; j <= 2; j++) {
						result += customFilter * matrix[i][j] * getPaddingStrategy().pixelAt(image, 
																					x+(i-1), 
																					y+(j-1)).getGreen();
					}
				}
				
				newPixel.setGreen(result);
				result = 0;
							  
				//*******************************
				// Blue
				for (int i = 0; i <= 2; i++) {
					for (int j = 0; j <= 2; j++) {
						result += customFilter * matrix[i][j] * getPaddingStrategy().pixelAt(image,
																					x+(i-1), 
																					y+(j-1)).getBlue();
					}
				}
				
				newPixel.setBlue(result);
				result = 0;
							
				//*******************************
				// Alpha - Untouched in this filter
				newPixel.setAlpha(getPaddingStrategy().pixelAt(image, x,y).getAlpha());
							 
				//*******************************
				// Done
				newImage.setPixel(x, y, newPixel);
			}
		}
		
		return newImage;
	}
}
