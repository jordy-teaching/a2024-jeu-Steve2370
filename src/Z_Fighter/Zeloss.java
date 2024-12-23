package Z_Fighter;

import Doctrina.Canvas;
import Doctrina.CollidableRepository;
import Doctrina.Direction;
import Doctrina.StaticEntity;
import Viking.GamePad;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Zeloss extends StaticEntity {
    private int xPosition;
    private int yPosition;
    private int yVelocity;
    private int width = 62;
    private Rectangle collisionBox;
    private boolean isJumping = false;
    private Direction direction;
    private boolean isMoving, isAttacking, isIdle;
    private boolean debugMode = true;

    private Animation walkAnimationRight;
    private Animation walkAnimationLeft;
    private Animation idleAnimationRight;
    private Animation idleAnimationLeft;
    private Animation attackAnimation1Right;
    private Animation attackAnimation1Left;
    private Animation attackAnimation2Right;
    private Animation attackAnimation2Left;
    private Animation attackAnimation3Right;
    private Animation attackAnimation3Left;
    private Animation attackAnimation4Right;
    private Animation attackAnimation4Left;
    private Animation currentAttackAnimation;


    private BarreVie barreSante;
    private BarreVie barreEnergie;

    private static final int GRAVITY = 1;
    private static final int JUMP_FORCE = -25;
    private static final int MOVE_SPEED = 5;
    private static final int GROUND_LEVEL = 300;

    private GamePad gamePad;

    private long lastAttackFrameTime;
    private int attackFrameIndex;
    private final int ATTACK_FRAME_DELAY = 100;

    public Zeloss(int xPosition) {
        this.xPosition = xPosition;
        this.yPosition = GROUND_LEVEL - getCollisionBox().height;
        this.direction = Direction.RIGHT;
        this.collisionBox = new Rectangle(xPosition, yPosition, 30, 30);
        this.isIdle = true;
        gamePad = new GamePad();
        barreSante = new BarreVie(10, 10, 200, 20, 100, Color.GRAY, Color.YELLOW);
        barreEnergie = new BarreVie(10, 40, 150, 10, 100, Color.GRAY, Color.GREEN);
        loadAnimations();

//        CollidableRepository.getInstance().registerEntity(this);
    }

    public void prendreDegat(int degat) {
        barreSante.updateValue(barreSante.getCurrentValue() - degat);
    }

    public void utiliserEnergie(int quantite) {
        barreEnergie.updateValue(barreEnergie.getCurrentValue() - quantite);
    }

    public void toggleDebugMode() {
        this.debugMode = !this.debugMode;
    }

    public  java.util.List<StaticEntity> checkForCollisions() {
        List<StaticEntity> colliedEntities = new ArrayList<>();
        Rectangle thisBox = this.getCollisionBox();

        for (StaticEntity entity : CollidableRepository.getInstance()) {
            if (thisBox.intersects(entity.getCollisionBox())) {
                colliedEntities.add(entity);
            }
        }
        return colliedEntities;
    }

    @Override
    public Rectangle getCollisionBox() {
        return new Rectangle(x, y, width, height);
    }

    private void loadAnimations() {
        walkAnimationRight = new Animation(loadAnimationFrames("images/Boss/PNG/Boss3/Walk/Walk1", 2), 100);
        walkAnimationLeft = new Animation(createFlippedFrames(walkAnimationRight.getFrames()), 100);

        idleAnimationRight = new Animation(loadAnimationFrames("images/Boss/PNG/Boss3/Idle/Idle1", 3), 200);
        idleAnimationLeft = new Animation(createFlippedFrames(idleAnimationRight.getFrames()), 200);

        attackAnimation1Right = new Animation(loadAnimationFrames("images/Boss/PNG/Boss3/Attack/Attack1", 7), 50);
        attackAnimation1Left = new Animation(createFlippedFrames(attackAnimation1Right.getFrames()), 50);

        attackAnimation2Right = new Animation(loadAnimationFrames("images/Boss/PNG/Boss3/Attack_Blade/Magic_blade1", 5), 50);
        attackAnimation2Left = new Animation(createFlippedFrames(attackAnimation2Right.getFrames()), 50);

        attackAnimation3Right = new Animation(loadAnimationFrames("images/Boss/PNG/Boss3/Attack_Fire/Magic_fire1", 5), 50);
        attackAnimation3Left = new Animation(createFlippedFrames(attackAnimation3Right.getFrames()), 50);

        attackAnimation4Right = new Animation(loadAnimationFrames("images/Boss/PNG/Boss3/Attack_Lighting/Magic_lightning1", 5), 50);
        attackAnimation4Left = new Animation(createFlippedFrames(attackAnimation4Right.getFrames()), 50);
    }

    private Image[] loadAnimationFrames(String basePath, int frameCount) {
        Image[] frames = new Image[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String fileName = String.format("%s_%d.png", basePath, i);
            try {
                frames[i] = ImageIO.read(getClass().getClassLoader().getResourceAsStream(fileName));
                if (frames[i] == null) {
                    throw new IOException("Image introuvable : " + fileName);
                }
            } catch (IOException e) {
                System.out.println("Erreur lors du chargement de : " + fileName);
                e.printStackTrace();
            }
        }
        return frames;
    }



    private void handleVerticalCollision(StaticEntity entity) {
        Rectangle entityBox = entity.getCollisionBox();
        Rectangle thisBox = getCollisionBox();

        if (thisBox.intersects(entityBox)) {
            if (yPosition + getCollisionBox().height <= entityBox.y) {
                yPosition = entityBox.y - getCollisionBox().height;
                yVelocity = 0;
                isJumping = false;
            } else if (yPosition >= entityBox.y + entityBox.height) {
                yPosition = entityBox.y + entityBox.height;
                yVelocity = 0;
            }
        }
    }

    private void handleHorizontalCollision(StaticEntity entity) {
        Rectangle entityBox = entity.getCollisionBox();
        if (xPosition < entityBox.x + entityBox.width && xPosition + getCollisionBox().width > entityBox.x) {
            if (direction == Direction.RIGHT) {
                xPosition = entityBox.x - getCollisionBox().width;
            } else if (direction == Direction.LEFT) {
                xPosition = entityBox.x + entityBox.width;
            }
        }
    }


    private void endAttack() {
        isAttacking = false;
        System.out.println("Attack 1 Finished");
    }

    private Image[] createFlippedFrames(Image[] originalFrames) {
        Image[] flippedFrames = new Image[originalFrames.length];
        for (int i = 0; i < originalFrames.length; i++) {
            flippedFrames[i] = getFlippedImage(originalFrames[i]);
        }
        return flippedFrames;
    }

    private Image getFlippedImage(Image image) {
        if (image.getWidth(null) <= 0 || image.getHeight(null) <= 0) {
            throw new IllegalArgumentException("Image invalide ou non chargée correctement.");
        }

        BufferedImage original = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = original.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        int width = original.getWidth();
        int height = original.getHeight();
        BufferedImage flipped = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = flipped.createGraphics();
        g2.drawImage(original, width, 0, -width, height, null);
        g2.dispose();

        return flipped;
    }

    public void update(boolean moving, boolean attacking, boolean jump, Direction direction, int attackType) {
        this.isMoving = moving;

        if (moving) {
            this.direction = direction;
            if (direction == Direction.RIGHT) {
                xPosition += MOVE_SPEED;
                walkAnimationRight.update();
            } else {
                xPosition -= MOVE_SPEED;
                walkAnimationLeft.update();
            }
        }

        if (attacking && !isAttacking) {
            startAttack(attackType);
        }

        if (isAttacking) {
            continueAttack();
        }

        if (jump && !isJumping) {
            isJumping = true;
            yVelocity = JUMP_FORCE;
        }
        if (isJumping) {
            yPosition += yVelocity;
            yVelocity += GRAVITY;
            if (yPosition >= 380) {
                yPosition = 380;
                yVelocity = 0;
                isJumping = false;
            }
        }

        if (!moving && !attacking) {
            if (this.direction == Direction.RIGHT) {
                idleAnimationRight.update();
            } else if (this.direction == Direction.LEFT){
                idleAnimationLeft.update();
            }
            checkForCollisions();
        }
    }

    private void startAttack(int attackType) {
//        System.out.println("Debut de l'attaque = " + attackType);
        isAttacking = true;
        attackFrameIndex = 0;

        switch (attackType) {
            case 1:
                currentAttackAnimation = (direction == Direction.RIGHT) ? attackAnimation1Right : attackAnimation1Left;
                break;
            case 2:
                currentAttackAnimation = (direction == Direction.RIGHT) ? attackAnimation2Right : attackAnimation2Left;
                break;
            case 3:
                currentAttackAnimation = (direction == Direction.RIGHT) ? attackAnimation3Right : attackAnimation3Left;
                break;
            case 4:
                currentAttackAnimation = (direction == Direction.RIGHT) ? attackAnimation4Right : attackAnimation4Left;
                break;
            default:
                return;
        }
        resetAndStartAnimation(currentAttackAnimation);
    }

    private void continueAttack() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAttackFrameTime >= ATTACK_FRAME_DELAY && currentAttackAnimation != null) {

            currentAttackAnimation.update();

            lastAttackFrameTime = currentTime;

            if (currentAttackAnimation.isFinished()) {
                endAttack();
            }
        }
    }

    private void resetAndStartAnimation(Animation animation) {
        animation.reset();
        animation.setLoop(false);
    }

    public void draw(Canvas canvas) {
        Image frameToDraw = null;

        if (isAttacking) {
            frameToDraw = currentAttackAnimation.getCurrentFrame();
        } else if (isMoving) {
            frameToDraw = (direction == Direction.RIGHT) ? walkAnimationRight.getCurrentFrame() : walkAnimationLeft.getCurrentFrame();
        } else if (isIdle) {
            frameToDraw = (direction == Direction.RIGHT) ? idleAnimationRight.getCurrentFrame() : idleAnimationLeft.getCurrentFrame();
        }

        if (frameToDraw != null) {
            canvas.drawImage(frameToDraw, xPosition, yPosition);
        }

        barreSante.draw(canvas);
        barreEnergie.draw(canvas);

    }

    public void setYPosition(int yPosition) {
        this.yPosition = yPosition;
    }
}