package Z_Fighter;

import Doctrina.*;
import Doctrina.Canvas;
import Viking.GameConfig;
import Viking.GamePad;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class Players extends ControllableEntity {

    private static final String DEFAULT_SPRITE_PATH = "images/player.png";

    private static final int ANIMATION_SPEED = 8;
    private static final int GRAVITY = 1;
    private static final int JUMP_FORCE = -25;
    private static final int GROUND_LEVEL = 380;
    protected static final int FRAME_COUNT_ATTACK_1 = 10;
    protected static final int FRAME_COUNT_ATTACK_2 = 4;
    protected static final int FRAME_COUNT_ATTACK_3 = 6;
    protected static final int FRAME_COUNT_BARRIERE = 3;
    protected static final int FRAME_COUNT_SPECIAL = 10;

    protected int niveauSante = 100;
    protected int niveauEnergie = 0;
    protected boolean barrierActive = false;
    private boolean attackHit = false;
    private Players opponent;
    private Collision collision;
    private AttackNormale attackNormale;
    private Kameha kameha;
    protected boolean isAttacking = false;
    protected boolean dead;
    private int attackFrameIndex = 0;
    private long lastAttackFrameTime = 0;
    private static final int ATTACK_FRAME_DELAY = 100;

    protected BarreVie barreSante;
    protected BarreVie barreEnergie;

    public void update(boolean isMoving, boolean isAttacking, boolean isJumping, Direction currentDirection, int i) {
    }

    protected enum AttackType {
        NONE, NORMAL, SPECIAL, BARRIERE
    }

    protected AttackType currentAttack = AttackType.NONE;

    protected int FRAME_WIDTH = 32;
    protected int FRAME_HEIGHT = 32;

    public abstract void draw(Canvas canvas, int camX, int camY);

    private enum PlayerState {
        IDLE, WALK, JUMP, FALL
    }

    private PlayerState currentState = PlayerState.IDLE;

    private BufferedImage spriteSheet;
    private Image[] rightFrames;
    private Image[] leftFrames;
    private Image[] upFrames;

    private int currentAnimationFrame = 1;
    private int nextFrameDelay = ANIMATION_SPEED;

    private boolean isJumping = false;
    private boolean isFalling = false;
    private int verticalVelocity = 0;

    public Players(MovementController controller) {
        super(controller);
        setDimension(32, 32);
        setSpeed(3);
        barreSante = new BarreVie(10, 10, 200, 20, 100, Color.GRAY, Color.YELLOW);
        barreEnergie = new BarreVie(10, 40, 200, 20, 100, Color.GRAY, Color.GREEN);
        updateBarres();

        loadDefaultSpriteSheet();
        loadDefaultAnimationFrames();
    }

    protected void updateBarres() {
        barreSante.updateValue(niveauSante);
        barreEnergie.updateValue(niveauEnergie);
    }

    public void recevoirAttack() {
        if (dead) return;
        int domage = barrierActive ? 1 : 2;
        niveauSante -= domage;
        if (niveauSante < 0) niveauSante = 0;
        updateBarres();
        if (niveauSante <= 0) {
            dead = true;
        }
    }

    public boolean attackSpecial() {
        if (niveauEnergie >= 60) {
            niveauEnergie -= 60;
            if (niveauEnergie < 0) niveauEnergie = 0;
            currentAttack = AttackType.SPECIAL;
            isAttacking = true;
            updateBarres();
            return true;
        }
        return false;
    }

    public void setBarrierActive(boolean active) {
        this.barrierActive = active;
    }

    @Override
    public void update() {
        long currentTime = System.currentTimeMillis();
        super.update();
        if (isAttacking) {
          if  (currentTime - lastAttackFrameTime >= ATTACK_FRAME_DELAY) {
              attackFrameIndex++;
              lastAttackFrameTime = currentTime;
          }
          currentAttack = AttackType.NONE;
          isAttacking = false;
        }
        if (attackNormale != null) {
            attackNormale.update();
        }
        moveWithController();
        handleJump();
        applyGravity();
        updateState();
        handleAnimation();
        handleCollisions();
    }

    public Rectangle getAttackHitBox() {
        if (currentAttack == AttackType.NONE) {
            return new Rectangle(0, 0, 0, 0);
        }
        int hitboxWidth = 20;
        int hitboxHeight = 20;
        int offsetX = getX() + (getDirection() == Direction.RIGHT ? FRAME_WIDTH : -hitboxWidth);
        int offsetY = getY() + FRAME_HEIGHT / 2 - hitboxHeight / 2;
        System.out.println("Attaque - X: " + offsetX + ", Y: " + offsetY + ", Width: " + hitboxWidth + ", Height: " + hitboxHeight);

        return new Rectangle(offsetX, offsetY, hitboxWidth, hitboxHeight);
    }

    protected void handleAttacks(GamePad gamePad) {
        if (currentAttack != AttackType.NONE) {
            updateAttack();
        } else {
            if (gamePad.isAttack_1()) startAttack(AttackType.NORMAL, FRAME_COUNT_ATTACK_1);
            else if (gamePad.isAttack_2()) startAttack(AttackType.NORMAL, FRAME_COUNT_ATTACK_2);
            else if (gamePad.isAttack_3()) startAttack(AttackType.NORMAL, FRAME_COUNT_ATTACK_3);
            else if (gamePad.isAttack_4()) startAttack(AttackType.BARRIERE, FRAME_COUNT_BARRIERE);
            else if (gamePad.isAttack_5() && attackSpecial()) {

                startAttack(AttackType.SPECIAL, FRAME_COUNT_SPECIAL);
            }
        }
    }

    public boolean isDead() {
        return dead;
    }

    protected void startAttack(AttackType type, int frameCount) {
        currentAttack = type;
        attackFrameIndex = 0;
        lastAttackFrameTime = System.currentTimeMillis();
    }

    protected void updateAttack() {
        long currentTime = System.currentTimeMillis();
        int frameCount = getFrameCountForAttack(currentAttack);

        if (currentTime - lastAttackFrameTime >= ATTACK_FRAME_DELAY) {
            attackFrameIndex++;
            lastAttackFrameTime = currentTime;

            if (attackFrameIndex >= frameCount) {
                currentAttack = AttackType.NONE;
                attackFrameIndex = 0;
            }
        }
    }

    protected int getFrameCountForAttack(AttackType type) {
        return switch (type) {
            case NORMAL -> FRAME_COUNT_ATTACK_1;
            case SPECIAL -> FRAME_COUNT_SPECIAL;
            case BARRIERE -> FRAME_COUNT_BARRIERE;
            default -> 0;
        };
    }


    @Override
    public void draw(Canvas canvas) {
        Image frameToDraw = getCurrentFrame();
        if (frameToDraw != null) {
            canvas.drawImage(frameToDraw, x, y);
        }
        barreSante.draw(canvas);
        barreEnergie.draw(canvas);
        if (attackNormale != null) {
            attackNormale.draw(canvas, getX(), getY());
        }

        if (GameConfig.isDebugEnabled()) {
            drawAttackHitBox(canvas);
        }
    }

    public void moveWithController() {
        if (controller.isLeftPressed()) {
            move(Direction.LEFT);
        } else if (controller.isRightPressed()) {
            move(Direction.RIGHT);
        }
        else {
            currentAnimationFrame = 1;
        }
    }

    private void handleJump() {
        if (controller.isUpPressed() && !isJumping && !isFalling) {
            isJumping = true;
            verticalVelocity = JUMP_FORCE;
        }
    }

    private void applyGravity() {
        if (isJumping || isFalling) {
            y += verticalVelocity;
            verticalVelocity += GRAVITY;

            if (y >= GROUND_LEVEL) {
                y = GROUND_LEVEL;
                isJumping = false;
                isFalling = false;
                verticalVelocity = 0;
            } else if (verticalVelocity > 0) {
                isFalling = true;
            }
        }
    }

    private void updateState() {
        if (isJumping) {
            currentState = PlayerState.JUMP;
        } else if (isFalling) {
            currentState = PlayerState.FALL;
        } else if (hasMoved()) {
            currentState = PlayerState.WALK;
        } else {
            currentState = PlayerState.IDLE;
        }
    }

    private void handleAnimation() {
        if (currentState == PlayerState.WALK || currentState == PlayerState.JUMP || currentState == PlayerState.FALL) {
            nextFrameDelay--;
            if (nextFrameDelay == 0) {
                currentAnimationFrame++;
                if (leftFrames != null && currentAnimationFrame >= leftFrames.length) {
                    currentAnimationFrame = 0;
                }
                nextFrameDelay = ANIMATION_SPEED;
            }
        } else {
            currentAnimationFrame = 1;
        }
    }

    public boolean hasMoved() {
        return controller.isLeftPressed() || controller.isRightPressed();
    }

    public void handleCollisions() {
        for (StaticEntity other : CollidableRepository.getInstance()) {
            if (this != other && getCollisionBox().intersects(other.getCollisionBox())) {
                onCollision(other);
            }
            if (opponent != null && collision.isHitByAttack(this, opponent)) {
                opponent.recevoirAttack();
                System.out.println("L'attaque a touché l'adversaire !");
            }
        }

        if (currentAttack != AttackType.NONE && opponent != null) {
            Rectangle attackHitBox = getAttackHitBox();
            if (attackHitBox.intersects(opponent.getCollisionBox())) {
                opponent.recevoirAttack();
            }
        }
    }


    protected void onCollision(StaticEntity other) {
        System.out.println("Collision detected with: " + other.getClass().getSimpleName());
    }

    private Image getCurrentFrame() {
        if (isJumping || isFalling) {
            return upFrames != null ? upFrames[Math.min(currentAnimationFrame, upFrames.length - 1)] : null;
        } else {
            Image[] currentFrames = getDirection() == Direction.RIGHT ? rightFrames : leftFrames;
            return currentFrames != null ? currentFrames[Math.min(currentAnimationFrame, currentFrames.length - 1)] : null;
        }
    }

    public void drawAttackHitBox(Canvas canvas) {
        Graphics2D g2d = (Graphics2D) canvas.getGraphics();
        if (g2d == null) return;

        Rectangle hitBox = getAttackHitBox();
        g2d.setColor(Color.RED);
        g2d.drawRect(hitBox.x, hitBox.y, hitBox.width, hitBox.height);
    }

    private void loadDefaultSpriteSheet() {
        try {
            spriteSheet = ImageIO.read(getClass().getClassLoader().getResourceAsStream(DEFAULT_SPRITE_PATH));
        } catch (IOException e) {
            System.err.println("Erreur chargement sprite par défaut: " + e.getMessage());
        }
    }

    private void loadDefaultAnimationFrames() {
        if (spriteSheet == null) return;
        rightFrames = loadFrames(spriteSheet, 192);
        leftFrames = loadFrames(spriteSheet, 160);
        upFrames = loadFrames(spriteSheet, 224);
    }

    private Image[] loadFrames(BufferedImage img, int yOffset) {
        Image[] frames = new Image[3];
        frames[0] = img.getSubimage(0, yOffset, width, height);
        frames[1] = img.getSubimage(32, yOffset, width, height);
        frames[2] = img.getSubimage(64, yOffset, width, height);
        return frames;
    }

    protected int idleFrameIndex = 0;
    protected long lastIdleFrameTime = 0;

    protected int getIdleFrameIndex() {
        return idleFrameIndex;
    }

    protected void setIdleFrameIndex(int index) {
        idleFrameIndex = index;
    }

    protected long getLastIdleFrameTime() {
        return lastIdleFrameTime;
    }

    protected void setLastIdleFrameTime(long time) {
        lastIdleFrameTime = time;
    }

    protected boolean isJumping() {
        return this.isJumping;
    }

    protected boolean isFalling() {
        return this.isFalling;
    }

    protected int getCurrentFrameIndex() {
        return this.currentAnimationFrame;
    }

    protected MovementController getController() {
        return this.controller;
    }

    public BarreVie getBarreSante() {
        return barreSante;
    }

    public BarreVie getBarreEnergie() {
        return barreEnergie;
    }

    public void setOpponent(Players players) {
        this.opponent = players;
    }

    public Players getOpponent() {
        return this.opponent;
    }
}
