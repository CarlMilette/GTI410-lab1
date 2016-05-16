

public class application {

	public application() {}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	 CMYKConversion test1 = new CMYKConversion();
    	
	 test1.rgb2cmyk(40, 10, 30);
	
	 System.out.println("RGB TO CMYK===================");
	System.out.print("Cyan:" +test1.getCyan());
	System.out.println();
	System.out.print("Magenta:" +test1.getMagenta());
	System.out.println();
	System.out.print("Yellow:" +test1.getYellow());
	System.out.println();
	System.out.print("K:" +test1.getK());
		
	System.out.println();
	System.out.println();
	
	
	System.out.println("RGB TO HSV===================");
		
	HSVConversion test2 = new HSVConversion();
	test2.rgb2Hsv(40, 10, 30);
	
	System.out.print("H:" +test2.getH());
	System.out.println();
	System.out.print("S:" +test2.getS());
	System.out.println();
	System.out.print("V:" +test2.getV());
	System.out.println();
	System.out.println();
	
	System.out.println("CMYK TO RGB===================");
	
	
	RGBConversion test3 = new RGBConversion();
	
	test3.cmyk2rgb(0.0, 0.7500000000000002, 0.24999999999999983, 0.8431372549019608);
	
	System.out.print("R:" +test3.getR1());
	System.out.println();
	System.out.print("G:" +test3.getG1());
	System.out.println();
	System.out.print("B:" +test3.getB1());
	System.out.println();
	System.out.println();

System.out.println("HSV TO RGB===================");
	
	
	test3.hsv2rgb(320.0, 0.75, 0.1568627450980392);
	
	System.out.print("R:" +test3.getR2());
	System.out.println();
	System.out.print("G:" +test3.getG2());
	System.out.println();
	System.out.print("B:" +test3.getB2());
	System.out.println();
	
	
	
	
	
	
	}

}
