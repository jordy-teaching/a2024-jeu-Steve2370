package Z_Fighter;
import Doctrina.Canvas;

public class Camera {
    private int x;
    private int y;
    private int vueWidth;
    private int vueHeight;
    private int levelWidth;
    private int levelHeight;
    private Landscape landscape;

    public Camera(int viewWidth, int viewHeight, int levelWidth, int levelHeight, Landscape landscape) {
        this.vueWidth = viewWidth;
        this.vueHeight = viewHeight;
        this.levelWidth = levelWidth;
        this.levelHeight = levelHeight;
        this.landscape = landscape;
    }

    public void update(int player1X, int player1Y, int player2X, int player2Y) {
        int centerX = (player1X + player2X) / 2;
        int centerY = (player1Y + player2Y) / 2;
        x = centerX - vueWidth / 2;
        y = centerY - vueHeight / 2;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x + vueWidth > levelWidth) x = levelWidth - vueWidth;
        if (y + vueHeight > levelHeight) y = levelHeight - vueHeight;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    public void draw(Canvas canvas) {
        landscape.draw(canvas, x);
    }
}
