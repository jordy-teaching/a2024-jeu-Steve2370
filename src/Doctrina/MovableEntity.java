package Doctrina;

import java.awt.*;

public abstract class MovableEntity extends StaticEntity {
    private int speed = 1;
    private Direction direction = Direction.DOWN;
    private final Collision collision;

    private int lastX = Integer.MIN_VALUE;
    private int lastY = Integer.MIN_VALUE;
    private boolean moved;

    private int previousX;
    private int previousY;

    private boolean isAttacking = false;
    private long attackStartTime;
    private static final int ATTACK_DURATION = 500;

    public void update() {
        moved = false;

        if (isAttacking && System.currentTimeMillis() - attackStartTime > ATTACK_DURATION) {
            isAttacking = false;
        }
    }

    public MovableEntity() {
        collision = new Collision(this);
        previousX = x;
        previousY = y;
    }

    public void move() {
        previousX = x;
        previousY = y;

        int allowedSpeed = collision.getAllowedSpeed(direction);
        int velocityX = direction.calculateVelocityX(allowedSpeed);
        int velocityY = direction.calculateVelocityY(allowedSpeed);

        x += velocityX;
        y += velocityY;

        if (!hasMoved()) {
            int pushBackAmount = 2;
            switch (direction) {
                case UP -> y += pushBackAmount;
                case DOWN -> y -= pushBackAmount;
                case LEFT -> x += pushBackAmount;
                case RIGHT -> x -= pushBackAmount;
            }
        }

        moved = (x != lastX || y != lastY);
        lastX = x;
        lastY = y;
    }


    public boolean hasMoved() {
        return moved;
    }

    public void move(Direction direction) {
        this.direction = direction;
        move();
    }

    public void attack() {
        if (!isAttacking) {
            isAttacking = true;
            attackStartTime = System.currentTimeMillis();
        }
    }

    public Rectangle getAttackHitBox() {
        if (!isAttacking) {
            return new Rectangle(0, 0, 0, 0);
        }

        int boxWidth = width / 2;
        int boxHeight = height / 2;

        return switch (direction) {
            case UP -> new Rectangle(x, y - boxHeight, width, boxHeight);
            case DOWN -> new Rectangle(x, y + height, width, boxHeight);
            case LEFT -> new Rectangle(x - boxWidth, y, boxWidth, height);
            case RIGHT -> new Rectangle(x + width, y, boxWidth, height);
        };
    }

    public void drawAttackHitBox(Canvas canvas) {
        if (!isAttacking) return;
        Rectangle attackBox = getAttackHitBox();
        canvas.drawRectangle(attackBox.x, attackBox.y, attackBox.width, attackBox.height, Color.RED);
    }

    public boolean attackHitBoxIntersectsWith(StaticEntity other) {
        if (other == null || !isAttacking) {
            return false;
        }
        return getAttackHitBox().intersects(other.getCollisionBox());
    }

    public void draw(Canvas canvas) {
        // On dessine la boîte d'attaque si l'entité attaque
        drawAttackHitBox(canvas);
    }

    public void moveUp() {
        move(Direction.UP);
    }

    public void moveDown() {
        move(Direction.DOWN);
    }

    public void moveLeft() {
        move(Direction.LEFT);
    }

    public void moveRight() {
        move(Direction.RIGHT);
    }


    public Rectangle getCollisionBox() {
        return new Rectangle(x, y, width, height);
    }


    public Rectangle getHitBox() {
        return switch (direction) {
            case UP -> getUpperHitBox();
            case DOWN -> getLowerHitBox();
            case LEFT -> getLeftHitBox();
            case RIGHT -> getRightHitBox();
        };
    }

    private Rectangle getUpperHitBox() {
        return new Rectangle(x, y - speed, width, speed);
    }

    private Rectangle getLowerHitBox() {
        return new Rectangle(x, y + height, width, speed);
    }

    private Rectangle getLeftHitBox() {
        return new Rectangle(x - speed, y, speed, height);
    }

    private Rectangle getRightHitBox() {
        return new Rectangle(x + width, y, speed, height);
    }

    public boolean hitBoxIntersectWith(StaticEntity other) {
        if (other == null) {
            return false;
        }
        return getHitBox().intersects(other.getCollisionBox());
    }

    public void drawHitBox(Canvas canvas) {
        Rectangle rect = getHitBox();
        canvas.drawRectangle(rect.x, rect.y, rect.width, rect.height, Color.BLUE);
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void drawHitBoxA(Canvas canvas) {
        Rectangle rect = getHitBox();
        canvas.drawRectangle(rect.x, rect.y, rect.width, rect.height, Color.BLUE); // Boîte de collision en bleu
    }
}
