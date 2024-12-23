package Doctrina;

import java.awt.*;
import java.awt.image.ImageObserver;

public class Canvas {

    private Graphics2D graphics;

    public Canvas(Graphics2D graphics) {
        this.graphics = graphics;
    }

    public void drawString(String text, int x, int y, Paint paint) {
        graphics.setPaint(paint);
        graphics.drawString(text, x, y);
    }

    public void drawCircle(int x, int y, int radius, Paint paint) {
        graphics.setPaint(paint);
        graphics.fillOval(x, y, radius * 2, radius * 2);
    }

    public void drawRectangle(StaticEntity entity, Paint paint) {
        drawRectangle(entity.x, entity.y, entity.width, entity.height, paint);
    }

    public void drawRectangle(int x, int y, int width, int height, Paint paint) {
        graphics.setPaint(paint);
        graphics.drawRect(x, y, width, height);
    }

    public int getWidth() {
        return graphics.getDeviceConfiguration().getBounds().width;
    }

    public int getHeight() {
        return graphics.getDeviceConfiguration().getBounds().height;
    }

    public void drawImage(Image image, int x, int y) {
        graphics.drawImage(image, x, y, null);
    }

    public void drawImageIdle(Image image, int x, int y, ImageObserver observer) {

        graphics.drawImage(image, x, y, null);
    }

    public void setColor(Color color) {
        graphics.setColor(color);
    }

    public void fillRect(int i, int i1, int width, int height) {
        return ;
    }

    public Graphics getGraphics() {
        return graphics;
    }

}