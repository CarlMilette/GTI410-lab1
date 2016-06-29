package controller;

import model.ImageX;
import model.Pixel;
import model.Shape;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.util.List;
import java.util.Stack;

/**
 * Created by david on 6/18/2016.
 */
public class RegionFiller extends AbstractTransformer {

    private ImageX currentImage;
    private int currentImageWidth;
    private Pixel fillColor = new Pixel(0xFF00FFFF);
    private Pixel borderColor = new Pixel(0xFFFFFF00);
    private boolean floodFill = true;
    private int hueThreshold = 0;
    private int saturationThreshold = 0;
    private int valueThreshold = 0;

    /**
     * Constructeur par défaut.
     * **/
    public RegionFiller(){}

    /**
     * Transformer ID getter
     * */
    public int getID() {
        return ID_FLOODER;
    }

    protected boolean mouseClicked(MouseEvent e){
        List intersectedObjects = Selector.getDocumentObjectsAtLocation(e.getPoint());

        if (!intersectedObjects.isEmpty()) {
            Shape shape = (Shape)intersectedObjects.get(0);
            if (shape instanceof ImageX) {
                currentImage = (ImageX)shape;
                currentImageWidth = currentImage.getImageWidth();

                Point pt = e.getPoint();
                Point ptTransformed = new Point();
                try {
                    shape.inverseTransformPoint(pt, ptTransformed);
                } catch (NoninvertibleTransformException e1) {
                    e1.printStackTrace();
                    return false;
                }
                ptTransformed.translate(-currentImage.getPosition().x, -currentImage.getPosition().y);
                if (0 <= ptTransformed.x && ptTransformed.x < currentImage.getImageWidth() &&
                        0 <= ptTransformed.y && ptTransformed.y < currentImage.getImageHeight()) {
                    currentImage.beginPixelUpdate();
                    //horizontalLineFill(ptTransformed);
                    if(isFloodFill()) {
                        floodFillNR(ptTransformed.x, ptTransformed.y, currentImage.getPixel(ptTransformed.x, ptTransformed.y), fillColor);
                    } else {
                        boundaryFill(ptTransformed.x, ptTransformed.y, borderColor, fillColor);
                    }
                    currentImage.endPixelUpdate();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @return
     */
    public Pixel getBorderColor() {
        return borderColor;
    }

    /**
     * @return
     */
    public Pixel getFillColor() {
        return fillColor;
    }

    /**
     * @param pixel
     */
    public void setBorderColor(Pixel pixel) {
        borderColor = pixel;
        System.out.println("new border color");
    }

    /**
     * @param pixel
     */
    public void setFillColor(Pixel pixel) {
        fillColor = pixel;
        System.out.println("new fill color");
    }
    /**
     * @return true if the filling algorithm is set to Flood Fill, false if it is set to Boundary Fill.
     */
    public boolean isFloodFill() {
        return floodFill;
    }

    /**
     * @param b set to true to enable Flood Fill and to false to enable Boundary Fill.
     */
    public void setFloodFill(boolean b) {
        floodFill = b;
        if (floodFill) {
            System.out.println("now doing Flood Fill");
        } else {
            System.out.println("now doing Boundary Fill");
        }
    }

    /**
     * @return
     */
    public int getHueThreshold() {
        return hueThreshold;
    }

    /**
     * @return
     */
    public int getSaturationThreshold() {
        return saturationThreshold;
    }

    /**
     * @return
     */
    public int getValueThreshold() {
        return valueThreshold;
    }

    /**
     * @param i
     */
    public void setHueThreshold(int i) {
        hueThreshold = i;
        System.out.println("new Hue Threshold " + i);
    }

    /**
     * @param i
     */
    public void setSaturationThreshold(int i) {
        saturationThreshold = i;
        System.out.println("new Saturation Threshold " + i);
    }

    /**
     * @param i
     */
    public void setValueThreshold(int i) {
        valueThreshold = i;
        System.out.println("new Value Threshold " + i);
    }


    /**
     * Références https://fr.wikipedia.org/wiki/Algorithme_de_remplissage_par_diffusion
     * */
    public void floodFillNR(int x, int y, Pixel coulInterieur, Pixel nouvCoul){
        Stack pile = new Stack();
        Point p = new Point(x,y);
        if(currentImage.getPixel(p.x,p.y).equals(coulInterieur)){
            pile.push(p);
            while(!pile.isEmpty()){
                Point n = (Point) pile.pop();
                currentImage.setPixel(n.x,n.y,nouvCoul);
                if(currentImage.getPixel(n.x,n.y+1).equals(coulInterieur)){
                    pile.push(new Point(n.x,n.y+1));
                }
                if(currentImage.getPixel(n.x,n.y-1).equals(coulInterieur)){
                    pile.push(new Point(n.x,n.y-1));
                }
                if(currentImage.getPixel(n.x+1,n.y).equals(coulInterieur)){
                    pile.push(new Point(n.x+1,n.y));
                }
                if(currentImage.getPixel(n.x-1,n.y).equals(coulInterieur)){
                    pile.push(new Point(n.x-1,n.y));
                }
            }
        }
    }

    /**
     * Références https://fr.wikipedia.org/wiki/Algorithme_de_remplissage_par_diffusion
     * */
    public void boundaryFill(int x, int y, Pixel boundCoul, Pixel nouvCoul) {
        //System.out.println("Boundary Filling method");
        Stack pile = new Stack();
        Point p = new Point(x, y);
        /*System.out.println(currentImage.getPixel(p.x,p.y).getRed());
        System.out.println(currentImage.getPixel(p.x,p.y).getGreen());
        System.out.println(currentImage.getPixel(p.x,p.y).getBlue());*/
        RGBConversion converter = new RGBConversion();
        double currentImageHue = ((double) getHueThreshold());
        double currentImageSat = ((double) getSaturationThreshold() / 255) * 100;
        double currentImageVal = ((double) getValueThreshold() / 255) * 100;
        converter.hsv2rgb(currentImageHue, currentImageSat, currentImageVal);
        int thresholdR = converter.getR();
        int thresholdG = converter.getG();
        int thresholdB = converter.getB();
        System.out.print("Red: " + thresholdR + "\nGreen: " + thresholdG + "\nBlue: " + thresholdB);
        /*HSVConversion converter = new HSVConversion();
        converter.rgb2Hsv(currentImage.getPixel(p.x,p.y).getRed(),currentImage.getPixel(p.x,p.y).getGreen(),currentImage.getPixel(p.x,p.y).getBlue());
        double currentImageHue = converter.getH();
        double currentImageSat = converter.getS() * 255;
        double currentImageVal = converter.getV() * 255;*/
        // TODO comparer valeurs des seuils et de la bordure avec valeur du pixel germe. Les seuils ont été convertis en RGB
        // Exemple: if(currentImage.getPixel(p.x,p.y).getRed() < Math.min(thresholdR, bordercolor.getRed()
        //          || currentImage.getPixel(p.x,p.y).getRed() > Math.max(thresholdR, bordercolor.getRed()));
        try {
            if ((currentImage.getPixel(p.x, p.y).getRed() <= Math.min(thresholdR, borderColor.getRed()) ||
                    currentImage.getPixel(p.x, p.y).getRed() >= Math.max(thresholdR, borderColor.getRed())) &&
                    (currentImage.getPixel(p.x, p.y).getGreen() <= Math.min(thresholdG, borderColor.getGreen()) ||
                            currentImage.getPixel(p.x, p.y).getGreen() >= Math.min(thresholdG, borderColor.getGreen())) &&
                    (currentImage.getPixel(p.x, p.y).getBlue() <= Math.min(thresholdB, borderColor.getBlue()) ||
                            currentImage.getPixel(p.x, p.y).getBlue() >= Math.min(thresholdB, borderColor.getBlue()))) {
                //System.out.print("Pixel germe different!");
                if ((!currentImage.getPixel(p.x, p.y).equals(boundCoul) && !currentImage.getPixel(p.x, p.y + 1).equals(nouvCoul))) {
                    System.out.println(currentImage.getPixel(p.x, p.y));
                    pile.push(p);
                    while (!pile.isEmpty()) {
                        Point n = (Point) pile.pop();
                        currentImage.setPixel(n.x, n.y, nouvCoul);
                        if (!currentImage.getPixel(n.x, n.y + 1).equals(boundCoul) && !currentImage.getPixel(n.x, n.y + 1).equals(nouvCoul)) {
                            pile.push(new Point(n.x, n.y + 1));
                        }
                        if (!currentImage.getPixel(n.x, n.y - 1).equals(boundCoul) && !currentImage.getPixel(n.x, n.y - 1).equals(nouvCoul)) {
                            pile.push(new Point(n.x, n.y - 1));
                        }
                        if (!currentImage.getPixel(n.x + 1, n.y).equals(boundCoul) && !currentImage.getPixel(n.x + 1, n.y).equals(nouvCoul)) {
                            pile.push(new Point(n.x + 1, n.y));
                        }
                        if (!currentImage.getPixel(n.x - 1, n.y).equals(boundCoul) && !currentImage.getPixel(n.x - 1, n.y).equals(nouvCoul)) {
                            pile.push(new Point(n.x - 1, n.y));
                        }
                    }
                }
            }
        } catch(Exception ex) {
            System.err.println("Boundary fill error");
        }
    }
}
