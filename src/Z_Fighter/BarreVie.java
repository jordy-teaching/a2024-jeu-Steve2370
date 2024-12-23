package Z_Fighter;

import Doctrina.Canvas;
import java.awt.*;

public class BarreVie {
    private int x, y;
    private int width, height;
    private int maxValue;
    private int currentValue;
    private Color backgroundColor;
    private Color foregroundColor;

    public BarreVie (int x, int y, int width, int height, int maxValue, Color backgroundColor, Color foregroundColor) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.maxValue = maxValue;
        this.currentValue = maxValue;
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
    }

    public void draw(Canvas canvas) {
        Graphics g = canvas.getGraphics();

        g.setColor(backgroundColor);
        g.fillRect(x, y, width, height);

        int filledWidth = (int) ((currentValue / (float) maxValue) * width);

        g.setColor(foregroundColor);
        g.fillRect(x, y, filledWidth, height);

        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
    }

    public void updateValue(int newValue) {
        this.currentValue = Math.max(0, Math.min(newValue, maxValue));
    }

    public void reset() {
        this.currentValue = maxValue;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getCurrentValue() {
        return currentValue;
    }
}
