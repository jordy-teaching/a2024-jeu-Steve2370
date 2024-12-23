package Z_Fighter;

import java.awt.*;

public class StaticBlock {
    private Rectangle collisionBox;

    public void StaticEntity(Rectangle collisionBox) {
        this.collisionBox = collisionBox;
    }

    public StaticBlock(Rectangle collisionBox) {
        this.collisionBox = collisionBox;
    }

    public Rectangle getCollisionBox() {
        return collisionBox;
    }
}