package Z_Fighter;

import Doctrina.Canvas;
import Doctrina.Direction;
import Doctrina.MovementController;
import Doctrina.StaticEntity;
import Viking.GamePad;

import java.util.Random;

public class Ia_Controller extends StaticEntity {
    private Players character;
    private long lastUpdateTime;
    private Direction currentDirection;
    private boolean isMoving;
    private boolean isAttacking;
    private boolean isJumping;
    private static final int MOVE_INTERVAL = 1000;
    private static final int ATTACK_INTERVAL = 2500;
    private static final int JUMP_CHANCE = 10;
    private static final int EVASION_CHANCE = 15;
    private static final Random random = new Random();

    private static final Class<? extends Players>[] characterOptions = new Class[] {
            Leon_B.class, Annie_B.class, Baragan.class, Wonderwise.class, Oz.class,
            Ace.class, Yashamaru.class, Schneizel.class, Manon_C.class, Tufi.class
    };

    public Ia_Controller() {
        int choice = random.nextInt(characterOptions.length);
        System.out.println("L'IA a choisi l'index : " + choice);

        try {
            this.character = characterOptions[choice].getConstructor(MovementController.class).newInstance(new GamePad(2));
            System.out.println("Personnage de l'IA : " + this.character.getClass().getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la création du personnage de l'IA.");
        }

        this.lastUpdateTime = System.currentTimeMillis();
        this.currentDirection = Direction.RIGHT;
        this.isMoving = true;
    }

    public void update() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUpdateTime > MOVE_INTERVAL) {
            adjustMovement();
            lastUpdateTime = currentTime;
        }

        if (character.getOpponent() != null && character.getOpponent().isAttacking()) {
            if (random.nextInt(100) < EVASION_CHANCE) {
                evade();
            }
        } else {
            isAttacking = false;
        }

        if (random.nextInt(100) < 20 && !isAttacking && currentTime - lastUpdateTime > ATTACK_INTERVAL) {
            isAttacking = true;
            lastUpdateTime = currentTime;
        }

        if (random.nextInt(100) < JUMP_CHANCE && !isJumping) {
            isJumping = true;
        }

        character.update(isMoving, isAttacking, isJumping, currentDirection, random.nextInt(2) + 1);

        if (isJumping) {
            isJumping = false;
        }
    }

    private void adjustMovement() {
        if (character.getOpponent() != null) {
            int distanceToPlayer = Math.abs(character.getX() - character.getOpponent().getX());

            if (distanceToPlayer > 200) {
                isMoving = true;
                currentDirection = character.getOpponent().getX() > character.getX() ? Direction.RIGHT : Direction.LEFT;
            } else if (distanceToPlayer < 50) {
                isMoving = true;
                currentDirection = character.getOpponent().getX() < character.getX() ? Direction.RIGHT : Direction.LEFT;
            } else {
                isMoving = random.nextBoolean();
            }
        } else {
            System.out.println("Erreur : L'adversaire est null.");
        }
    }

    private void evade() {
        isJumping = true;
        currentDirection = currentDirection == Direction.LEFT ? Direction.RIGHT : Direction.LEFT;
        isMoving = true;
    }

    @Override
    public void draw(Canvas canvas) {
    }

    public Players getCharacter() {
        return this.character;
    }
}
