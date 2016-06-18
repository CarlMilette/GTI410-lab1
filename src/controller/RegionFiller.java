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
    private int hueThreshold = 1;
    private int saturationThreshold = 2;
    private int valueThreshold = 3;

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
                    // TODO Change to boundary or flood fill
                    if(isFloodFill()) {
                        floodFill(ptTransformed.x, ptTransformed.y, currentImage.getPixel(ptTransformed.x, ptTransformed.y), fillColor);
                    } else {
                        boundaryFill();
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

    public void floodFill(int x, int y, Pixel coulInterieur, Pixel nouvCoul){
        System.out.println("Flood Fillin method");
        if(currentImage.getPixel(x,y).equals(coulInterieur)) {
            currentImage.setPixel(x, y, nouvCoul);
            floodFill(x, y + 1, coulInterieur, nouvCoul);
            floodFill(x, y - 1, coulInterieur, nouvCoul);
            floodFill(x + 1, y, coulInterieur, nouvCoul);
            floodFill(x - 1, y, coulInterieur, nouvCoul);
        }
    }

    public void boundaryFill(){
        System.out.println("Boundary Filling method");
    }
}
