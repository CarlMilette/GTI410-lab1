

public class RGBConversion {

    private int r;
    private int g;
    private int b;

    public RGBConversion() {

        this.r = 0;
        this.g = 0;
        this.b = 0;
    }

    /********************
     * CMYKtoRGB
     ****************************************************/
    public void cmyk2rgb(double c, double m, double y, double k) {

        this.r = (int) (255 * (1 - c) * (1 - k));
        this.g = (int) (255 * (1 - m) * (1 - k));
        this.b = (int) (255 * (1 - y) * (1 - k));
    }

    public double getR() {
        return this.r;
    }

    public double getG() {
        return this.g;
    }

    public double getB() {
        return this.b;
    }

    /********************
     * HSVtoRGB
     ***************************************************/

    public void hsv2rgb(double hue, double saturation, double value) {

        double c, x, m;

        // Make sure our arguments stay in-range
        double h = Math.max(0, Math.min(360, hue));
        double s = Math.max(0, Math.min(100, saturation));
        double v = Math.max(0, Math.min(100, value));

        // We accept saturation and value arguments from 0 to 100 because that's
        // how Photoshop represents those values. Internally, however, the
        // saturation and value are calculated from a range of 0 to 1. We make
        // That conversion here.
        s /= 100;
        v /= 100;

        c = v * s;
        x = c * (1 - Math.abs(((h / 60) % 2) - 1));
        m = v - c;

        if (0 <= h && h < 60) {
            this.r = (int) ((c + m) * 255);
            this.g = (int) ((x + m) * 255);
            this.b = (int) ((0 + m) * 255);
        } else if (60 <= h && h < 120) {
            this.r = (int) ((x + m) * 255);
            this.g = (int) ((c + m) * 255);
            this.b = (int) ((0 + m) * 255);
        } else if (120 <= h && h < 180) {
            this.r = (int) ((0 + m) * 255);
            this.g = (int) ((c + m) * 255);
            this.b = (int) ((x + m) * 255);
        } else if (180 <= h && h < 240) {
            this.r = (int) ((0 + m) * 255);
            this.g = (int) ((x + m) * 255);
            this.b = (int) ((c + m) * 255);
        } else if (240 <= h && h < 300) {
            this.r = (int) ((x + m) * 255);
            this.g = (int) ((0 + m) * 255);
            this.b = (int) ((c + m) * 255);
        } else { // 300 <= h < 360
            this.r = (int) ((c + m) * 255);
            this.g = (int) ((0 + m) * 255);
            this.b = (int) ((x + m) * 255);
        }
    }
}
