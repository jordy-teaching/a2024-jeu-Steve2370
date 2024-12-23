package Z_Fighter;

import java.awt.*;
import Doctrina.Canvas;

public class Kameha {
    private int x, y;
    private int width, height;
    private int speed;
    private boolean goingRight;
    private Animation animRight;
    private Animation animLeft;

    public Kameha(int startX, int startY, boolean goingRight, Animation animRight, Animation animLeft) {
        this.x = startX;
        this.y = startY;
        this.goingRight = goingRight;
        this.animRight = animRight;
        this.animLeft = animLeft;
        this.speed = 5;
        this.width = animRight.getCurrentFrame().getWidth(null);
        this.height = animRight.getCurrentFrame().getHeight(null);
    }

    public void update() {
        x += goingRight ? speed : -speed;
        if (goingRight) animRight.update();
        else animLeft.update();
    }

    public void draw(Canvas canvas, int camX, int camY) {
        Image frame = goingRight ? animRight.getCurrentFrame() : animLeft.getCurrentFrame();
        if (frame == null) {
            System.out.println("La frame du projectile est null.");
            return;
        }
        canvas.drawImage(frame, x - camX, y - camY);
    }



    public Rectangle getCollisionBox() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public boolean isOutOfScreen(int levelWidth, int levelHeight) {
        return x < 0 || x > levelWidth || y < 0 || y > levelHeight;
    }

}
