package Z_Fighter;

import java.awt.*;

public class StaticEntityZ implements Entity {
    private int x, y, width, height;

    public StaticEntityZ(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle getCollisionBox() {
        return new Rectangle(x, y, width, height);
    }
}

