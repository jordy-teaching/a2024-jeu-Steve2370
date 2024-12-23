package Doctrina;

import java.awt.*;

public abstract class StaticEntity {
    protected int x;
    protected int y;
    protected int width;
    protected int height;

    public abstract void draw(Canvas canvas);

    public void teleport(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setDimension(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Rectangle getCollisionBox() {
        return new Rectangle(x, y, width, height);
    }

    public boolean intersectWith(StaticEntity entity) {
        return getCollisionBox().intersects(entity.getCollisionBox());
    }

    public void drawCollisionBox(Canvas canvas) {
        Graphics2D g2d = (Graphics2D) canvas.getGraphics();

        Color originalColor = g2d.getColor();
        Stroke originalStroke = g2d.getStroke();

        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(2));

        Rectangle collisionBox = getCollisionBox();
        g2d.drawRect(collisionBox.x, collisionBox.y, collisionBox.width, collisionBox.height);

        g2d.setColor(originalColor);
        g2d.setStroke(originalStroke);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}