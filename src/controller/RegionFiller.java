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

    public void boundaryFill(int x, int y, Pixel boundCoul, Pixel nouvCoul){
        System.out.println("Boundary Filling method");
        Stack pile = new Stack();
        Point p = new Point(x,y);
        if(!currentImage.getPixel(p.x,p.y).equals(boundCoul) && !currentImage.getPixel(p.x, p.y+1).equals(nouvCoul)) {
            pile.push(p);
            while(!pile.isEmpty()) {
                Point n = (Point)pile.pop();
                currentImage.setPixel(n.x, n.y, nouvCoul);
                if (!currentImage.getPixel(n.x, n.y+1).equals(boundCoul) && !currentImage.getPixel(n.x, n.y+1).equals(nouvCoul)) {
                    pile.push(new Point(n.x, n.y+1));
                }
                if (!currentImage.getPixel(n.x, n.y-1).equals(boundCoul) && !currentImage.getPixel(n.x, n.y-1).equals(nouvCoul)) {
                    pile.push(new Point(n.x, n.y-1));
                }
                if (!currentImage.getPixel(n.x+1, n.y).equals(boundCoul) && !currentImage.getPixel(n.x+1, n.y).equals(nouvCoul)) {
                    pile.push(new Point(n.x+1, n.y));
                }
                if (!currentImage.getPixel(n.x-1, n.y).equals(boundCoul) && !currentImage.getPixel(n.x-1, n.y).equals(nouvCoul)) {
                    pile.push(new Point(n.x-1, n.y));
                }
            }
        }
    }
}
