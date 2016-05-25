

public class application {

	public application() {}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	 CMYKConversion test1 = new CMYKConversion();
    	
	 test1.rgb2cmyk(0, 255, 0);
	
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
	test2.rgb2Hsv(0, 255, 0);
	
	System.out.print("H:" +test2.getH());
	System.out.println();
	System.out.print("S:" +test2.getS());
	System.out.println();
	System.out.print("V:" +test2.getV());
	System.out.println();
	System.out.println();
	
	System.out.println("CMYK TO RGB===================");
	
	
	RGBConversion test3 = new RGBConversion();
	
	test3.cmyk2rgb(1, 0, 1, 0);
	
	System.out.print("R:" +test3.getR());
	System.out.println();
	System.out.print("G:" +test3.getG());
	System.out.println();
	System.out.print("B:" +test3.getB());
	System.out.println();
	System.out.println();

System.out.println("HSV TO RGB===================");
	
	
	test3.hsv2rgb(120, 100, 100);
	
	System.out.print("R:" +test3.getR());
	System.out.println();
	System.out.print("G:" +test3.getG());
	System.out.println();
	System.out.print("B:" +test3.getB());
	System.out.println();
	
	}

}
