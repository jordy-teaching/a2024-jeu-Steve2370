package Z_Fighter;

import Doctrina.*;
import Doctrina.Canvas;
import Viking.GamePad;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Annie_B extends Players {

    private static final String SPRITE_PATH_WALK = "images/Brawler_Girl_1/walk.png";
    private static final String SPRITE_PATH_IDLE = "images/Brawler_Girl_1/idle.png";
    private static final String SPRITE_TOUCHER =   "images/Brawler_Girl_1/hurt.png";
    private static final String SPRITE_PATH_ATTACK_1 = "images/Brawler_Girl_1/kick.png";
    private static final String SPRITE_PATH_ATTACK_2 = "images/Brawler_Girl_1/jab.png";
    private static final String SPRITE_PATH_ATTACK_3 = "images/Brawler_Girl_1/punch.png";
    private static final String SPRITE_PATH_ATTACK_4 = "images/Brawler_Girl_1/jump_kick.png";
    private static final String SPRITE_PATH_ATTACK_5 = "images/Brawler_Girl_1/dive_kick.png";
    private static final String SPRITE_PATH_BARRIERE = "images/Brawler_Girl_1/punch.png";
    private static final String SPRITE_PATH_TOUCHER = "images/Brawler_Girl_1/hurt.png";
    private static final String SPRITE_PATH_DIE =  "images/Brawler_Girl_1/hurt.png";
    private Animation attackSpecialRight;
    private Animation attackSpecialLeft;

    private static final int FRAME_COUNT = 10;
    private static final int FRAME_COUNT_IDLE = 4;
    private static final int FRAME_COUNT_ATTACK_1 = 5;
    private static final int FRAME_COUNT_ATTACK_2 = 3;
    private static final int FRAME_COUNT_ATTACK_3 = 3;
    private static final int FRAME_COUNT_ATTACK_4 = 3;
    private static final int FRAME_COUNT_ATTACK_5 = 5;
    private static final int FRAME_COUNT_TOUCHER = 2;
    private static final int FRAME_COUNT_BARRIERE = 3;
    private static final int FRAME_COUNT_DIE = 2;

    private static final int FRAME_WIDTH = 96;
    private static final int FRAME_HEIGHT = 63;
    private static final int IDLE_FRAME_DELAY = 200;
    private static final int ATTACK_FRAME_DELAY = 100;
    private int niveauSante = 100;
    private int niveauEnergie = 0;
    private boolean barrierActiver = false;
    private boolean attackHit = false;
    private boolean isDead = false;
    private Players adversaire;

    private enum AttackType {
        NONE, ATTACK_1, ATTACK_2, ATTACK_3, ATTACK_4, ATTACK_5, BARRIERE
    }

    private AttackType currentAttack = AttackType.NONE;

    private Image[] walkFramesRight, walkFramesLeft;
    private Image[] idleFramesRight, idleFramesLeft;
    private Image[] attack1_FramesRight, attack1_FramesLeft;
    private Image[] attack2_FramesRight, attack2_FramesLeft;
    private Image[] attack3_FramesRight, attack3_FramesLeft;
    private Image[] attack4_FramesRight, attack4_FramesLeft;
    private Image[] attack5_FramesRight, attack5_FramesLeft;
    private Image[] toucher_FrameRight, toucher_FrameLeft;
    private Image[] barriere_FramesRight, barriere_FramesLeft;
    private Image[] die_FramesRight, die_FramesLeft;

    private int attackFrameIndex = 0;
    private long lastAttackFrameTime = 0;

    private GamePad gamePad;
    private BarreVie barreSante;
    private BarreVie barreEnergie;
    private List<Kameha> kamehas = new ArrayList<>();

    private boolean isToucher = false;
    private long toucherTimer = 0;
    private static final long TOUCHER_DURATION = 500;

    public Annie_B(MovementController controller) {
        super(controller);
        this.gamePad = (GamePad) controller;

        setDimension(FRAME_WIDTH, FRAME_HEIGHT);
        setSpeed(3);
        loadAllAnimations();

        barreSante = new BarreVie(580, 10, 200, 20, 100, Color.GRAY, Color.YELLOW);
        barreEnergie = new BarreVie(630, 40, 150, 10, 100, Color.GRAY, Color.GREEN);

        CollidableRepository.getInstance().registerEntity(this);
        updateBares();
    }


    private void loadAllAnimations() {
        walkFramesRight = loadAnimation(SPRITE_PATH_WALK, FRAME_COUNT);
        walkFramesLeft = flipFrames(walkFramesRight);

        idleFramesRight = loadAnimation(SPRITE_PATH_IDLE, FRAME_COUNT_IDLE);
        idleFramesLeft = flipFrames(idleFramesRight);

        attack1_FramesRight = loadAnimation(SPRITE_PATH_ATTACK_1, FRAME_COUNT_ATTACK_1);
        attack1_FramesLeft = flipFrames(attack1_FramesRight);

        attack2_FramesRight = loadAnimation(SPRITE_PATH_ATTACK_2, FRAME_COUNT_ATTACK_2);
        attack2_FramesLeft = flipFrames(attack2_FramesRight);

        attack3_FramesRight = loadAnimation(SPRITE_PATH_ATTACK_3, FRAME_COUNT_ATTACK_3);
        attack3_FramesLeft = flipFrames(attack3_FramesRight);

        attack4_FramesRight = loadAnimation(SPRITE_PATH_ATTACK_4, FRAME_COUNT_ATTACK_4);
        attack4_FramesLeft = flipFrames(attack4_FramesRight);

        attack5_FramesRight = loadAnimation(SPRITE_PATH_ATTACK_5, FRAME_COUNT_ATTACK_5);
        attack5_FramesLeft = flipFrames(attack5_FramesRight);

        toucher_FrameRight = loadAnimation(SPRITE_TOUCHER, FRAME_COUNT_TOUCHER);
        toucher_FrameLeft = flipFrames(toucher_FrameRight);

        barriere_FramesRight = loadAnimation(SPRITE_PATH_BARRIERE, FRAME_COUNT_BARRIERE);
        barriere_FramesLeft = flipFrames(barriere_FramesRight);

        die_FramesRight = loadAnimation(SPRITE_PATH_DIE, FRAME_COUNT_DIE);
        die_FramesLeft = flipFrames(die_FramesRight);

        attackSpecialRight = new Animation(loadAnimationFrames("images/Technique_Eau/Water1/1/water1", 6), 50);
        attackSpecialLeft = new Animation(createFlippedFrames(attackSpecialRight.getFrames()), 50);
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
            System.out.println("Chargement de " + fileName);
        }
        return frames;
    }

    private Image[] createFlippedFrames(Image[] originalFrames) {
        Image[] flippedFrames = new Image[originalFrames.length];
        for (int i = 0; i < originalFrames.length; i++) {
            flippedFrames[i] = getFlippedImage(originalFrames[i]);
        }
        return flippedFrames;
    }

    private Image[] loadAnimation(String path, int frameCount) {
        try {
            BufferedImage spriteSheet = ImageIO.read(getClass().getClassLoader().getResourceAsStream(path));
            int frameWidth = spriteSheet.getWidth() / frameCount;
            int frameHeight = spriteSheet.getHeight();
            Image[] frames = new Image[frameCount];
            for (int i = 0; i < frameCount; i++) {
                frames[i] = spriteSheet.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
            }
            return frames;
        } catch (IOException | RasterFormatException e) {
            System.err.println("Erreur lors du chargement: " + path + " : " + e.getMessage());
            return new Image[0];
        }
    }

    private Image[] flipFrames(Image[] originalFrames) {
        Image[] flipped = new Image[originalFrames.length];
        for (int i = 0; i < originalFrames.length; i++) {
            flipped[i] = getFlippedImage(originalFrames[i]);
        }
        return flipped;
    }

    private Image getFlippedImage(Image image) {
        BufferedImage flipped = new BufferedImage(FRAME_WIDTH, FRAME_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = flipped.createGraphics();
        g.drawImage(image, FRAME_WIDTH, 0, -FRAME_WIDTH, FRAME_HEIGHT, null);
        g.dispose();
        return flipped;
    }

    private void updateBares() {
        barreSante.updateValue(niveauSante);
        barreEnergie.updateValue(niveauEnergie);
    }

    public void recevoirAttack() {
        if (isDead) return;

        int degat = barrierActive ? 1 : 3;
        niveauSante -= degat;
        if (niveauSante < 0) niveauSante = 0;
        updateBares();

        isToucher = true;
        toucherTimer = System.currentTimeMillis();

        if (niveauSante <= 0) {
            isDead = true;
        }
    }

    public void normalAttack() {
        if (isDead || isAttacking) return;
        isAttacking = true;

        niveauEnergie += 5;
        if (niveauEnergie > 100) niveauEnergie = 100;
        updateBares();
        updateNormalAttack();
        isAttacking = false;
    }

    public boolean attackSpecial() {
        if (isDead) return false;
        if (niveauEnergie >= 60) {
            niveauEnergie -= 60;
            if (niveauEnergie < 0) niveauEnergie = 0;
            updateBares();
            return true;
        }
        return false;
    }

    public void setBarrierActiver(boolean active) {
        if (isDead) return;
        this.barrierActive = active;
    }

    @Override
    public Rectangle getCollisionBox() {
        int adjustedWidth = 30;
        int adjustedHeight = FRAME_HEIGHT;
        int offsetX = getX() + (FRAME_WIDTH / 2) - (adjustedWidth / 2);
        int offsetY = getY();
        return new Rectangle(offsetX, offsetY, adjustedWidth, adjustedHeight);
    }

    @Override
    public void update() {
        super.update();
        if (isDead) {
            return;
        }

        handleInputs();
        handleAttacks();

        if (isToucher && System.currentTimeMillis() - toucherTimer > TOUCHER_DURATION) {
            isToucher = false;
        }
        updateKameha();
        attackHit = false;

        if (attackSpecial()) {
            boolean goingRight = (getDirection() == Direction.RIGHT);
            int startX = getX();
            int startY = getY();

            Kameha p = new Kameha(startX, startY, goingRight, attackSpecialRight, attackSpecialLeft);
            kamehas.add(p);
            System.out.println("Kameha");
        }

        checkForCollisions();
    }


    public void updateKameha() {
        for (int i = 0; i < kamehas.size(); i++) {
            Kameha p = kamehas.get(i);
            p.update();

            if (p.isOutOfScreen(2000, 600)) {
                kamehas.remove(i);
                i--;
                continue;
            }
            Rectangle projBox = p.getCollisionBox();
            Rectangle opponentBox = adversaire.getCollisionBox();
            if (projBox.intersects(opponentBox)) {
                adversaire.recevoirAttack();
                kamehas.remove(i--);
            }
        }
    }

    private boolean isAttackRealise = false;

    public void updateNormalAttack() {
        if (!isAttackRealise && (currentAttack == AttackType.ATTACK_1 || currentAttack == AttackType.ATTACK_2 || currentAttack == AttackType.ATTACK_3)) {
            Rectangle attackBox = getHitBox();
            Rectangle opponentBox = adversaire.getCollisionBox();
            if (attackBox.intersects(opponentBox)) {
                adversaire.recevoirAttack();
                if (adversaire.niveauSante < 0) adversaire.niveauSante = 0;
                adversaire.updateBarres();
                isAttackRealise = true;
            }
        } else {
            isAttackRealise = false;
        }
    }

    private void handleInputs() {
        setBarrierActiver(gamePad.isAttack_4());
        if (gamePad.isAttack_1() || gamePad.isAttack_2() || gamePad.isAttack_3() || gamePad.isAttack_4() || gamePad.isAttack_5()) {
            normalAttack();
        }
        if (gamePad.isAttack_special()) {
            attackSpecial();
        }
    }

    public java.util.List<StaticEntity> checkForCollisions() {
        java.util.List<StaticEntity> collidedEntities = new ArrayList<>();
        Rectangle thisBox = this.getCollisionBox();
        for (StaticEntity entity : CollidableRepository.getInstance()) {
            if (thisBox.intersects(entity.getCollisionBox())) {
                collidedEntities.add(entity);
            }
        }
        return collidedEntities;
    }

    private void handleAttacks() {
        if (currentAttack != AttackType.NONE) {
            updateAttack();
        } else {
            if (((GamePad)getController()).isAttack_1()) startAttack(AttackType.ATTACK_1, FRAME_COUNT_ATTACK_1);
            else if (((GamePad)getController()).isAttack_2()) startAttack(AttackType.ATTACK_2, FRAME_COUNT_ATTACK_2);
            else if (((GamePad)getController()).isAttack_3()) startAttack(AttackType.ATTACK_3, FRAME_COUNT_ATTACK_3);
            else if (((GamePad)getController()).isAttack_4()) startAttack(AttackType.ATTACK_4, FRAME_COUNT_ATTACK_4);
            else if (((GamePad)getController()).isAttack_5()) startAttack(AttackType.ATTACK_5, FRAME_COUNT_ATTACK_5);
        }
    }

    private void startAttack(AttackType type, int frameCount) {
        currentAttack = type;
        attackFrameIndex = 0;
        lastAttackFrameTime = System.currentTimeMillis();
    }

    public void updateAttack() {
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

    private int getFrameCountForAttack(AttackType type) {
        return switch (type) {
            case ATTACK_1 -> FRAME_COUNT_ATTACK_1;
            case ATTACK_2 -> FRAME_COUNT_ATTACK_2;
            case ATTACK_3 -> FRAME_COUNT_ATTACK_3;
            case ATTACK_4 -> FRAME_COUNT_ATTACK_4;
            case ATTACK_5 -> FRAME_COUNT_ATTACK_5;
            case BARRIERE -> FRAME_COUNT_BARRIERE;
            default -> 0;
        };
    }

    private boolean isPlayerIdle() {
        return !getController().isRightPressed() && !getController().isLeftPressed() && !isJumping() && currentAttack == AttackType.NONE;
    }

    private Image getCurrentIdleFrame() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - getLastIdleFrameTime() >= IDLE_FRAME_DELAY) {
            setIdleFrameIndex((getIdleFrameIndex() + 1) % FRAME_COUNT_IDLE);
            setLastIdleFrameTime(currentTime);
        }
        return getDirection() == Direction.RIGHT ? idleFramesRight[getIdleFrameIndex()] : idleFramesLeft[getIdleFrameIndex()];
    }


    protected Image getCurrentFrame() {
        if (currentAttack != AttackType.NONE) {
            Image[] frames = getAttackFrames(currentAttack);
            int frameCount = getFrameCountForAttack(currentAttack);
            return frames[Math.min(attackFrameIndex, frameCount - 1)];
        }

        if (isJumping()) {
            Image[] currentFrames = getDirection() == Direction.RIGHT ? walkFramesRight : walkFramesLeft;
            return currentFrames[Math.min(getCurrentFrameIndex(), FRAME_COUNT - 1)];
        }

        if (getController().isMoving()) {
            Image[] currentFrames = getDirection() == Direction.RIGHT ? walkFramesRight : walkFramesLeft;
            return currentFrames[getCurrentFrameIndex()];
        }

        if (isDead) {
            int index = Math.min(getCurrentFrameIndex(), FRAME_COUNT_DIE - 1);
            return getDirection() == Direction.RIGHT ? die_FramesRight[index] : die_FramesLeft[index];
        }

        if (isToucher) {
            int index = Math.min(getCurrentFrameIndex() % FRAME_COUNT_TOUCHER, FRAME_COUNT_TOUCHER - 1);
            return getDirection() == Direction.RIGHT ? toucher_FrameRight[index] : toucher_FrameLeft[index];
        }

        if (barrierActiver) {
            int index = Math.min(getCurrentFrameIndex() % FRAME_COUNT_BARRIERE, FRAME_COUNT_BARRIERE - 1);
            return getDirection() == Direction.RIGHT ? barriere_FramesRight[index] : barriere_FramesLeft[index];
        }

        if (isPlayerIdle()) {
            return getCurrentIdleFrame();
        }

        return getDirection() == Direction.RIGHT ? walkFramesRight[getCurrentFrameIndex()] : walkFramesLeft[getCurrentFrameIndex()];
    }

    private Image[] getAttackFrames(AttackType type) {
        return switch (type) {
            case ATTACK_1 -> (getDirection() == Direction.RIGHT ? attack1_FramesRight : attack1_FramesLeft);
            case ATTACK_2 -> (getDirection() == Direction.RIGHT ? attack2_FramesRight : attack2_FramesLeft);
            case ATTACK_3 -> (getDirection() == Direction.RIGHT ? attack3_FramesRight : attack3_FramesLeft);
            case ATTACK_4 -> (getDirection() == Direction.RIGHT ? attack4_FramesRight : attack4_FramesLeft);
            case ATTACK_5 -> (getDirection() == Direction.RIGHT ? attack5_FramesRight : attack5_FramesLeft);
            case BARRIERE -> (getDirection() == Direction.RIGHT ? barriere_FramesRight : barriere_FramesLeft);
            default -> null;
        };
    }

    @Override
    public void draw(Canvas canvas, int camX, int camY) {
        Image frameToDraw = getCurrentFrame();
        if (frameToDraw != null) {
            canvas.drawImage(frameToDraw, getX(), getY());
        }
        barreSante.draw(canvas);
        barreEnergie.draw(canvas);

        for (Kameha p : kamehas) {
            p.draw(canvas, camX, camY);
        }
    }

    public BarreVie getBarreSante() {
        return barreSante;
    }

    public BarreVie getBarreEnergie() {
        return barreEnergie;
    }

    public void setOpponent(Players adversaire) {
        this.adversaire = adversaire;
    }
}