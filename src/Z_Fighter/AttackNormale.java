package Z_Fighter;

import Doctrina.Canvas;
import Doctrina.Direction;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AttackNormale {
    private int x, y;
    private int width, height;
    private int speed;
    private boolean goingRight;
    private Animation attackRightAnimation;
    private Animation attackLeftAnimation;

    // Définir la taille de la boîte d'attaque
    private static final int HITBOX_WIDTH = 90;
    private static final int HITBOX_HEIGHT = 90;

    // Liste des attaques normales
    private List<AttackNormale> attackNormales = new ArrayList<>();

    public AttackNormale(int startX, int startY, boolean goingRight, Animation attackRightAnimation, Animation attackLeftAnimation) {
        this.x = startX;
        this.y = startY;
        this.goingRight = goingRight;
        this.attackRightAnimation = attackRightAnimation;
        this.attackLeftAnimation = attackLeftAnimation;
        this.speed = 10;  // Vitesse de déplacement de l'attaque
        this.width = attackRightAnimation.getCurrentFrame().getWidth(null);
        this.height = attackRightAnimation.getCurrentFrame().getHeight(null);
    }

    // Mise à jour de l'attaque : déplace l'attaque dans la direction appropriée
    public void update() {
        // Déplacement de l'attaque
        x += goingRight ? speed : -speed;

        // Mise à jour de l'animation de l'attaque
        if (goingRight) attackRightAnimation.update();
        else attackLeftAnimation.update();
    }

    // Dessine l'attaque à l'écran
    public void draw(Canvas canvas, int camX, int camY) {
        Image frame = goingRight ? attackRightAnimation.getCurrentFrame() : attackLeftAnimation.getCurrentFrame();
        if (frame != null) {
            canvas.drawImage(frame, x - camX, y - camY);
        }
    }

    // Récupère la boîte de collision (hitbox) de l'attaque
    public Rectangle getCollisionBox() {
        int offsetX = x + (goingRight ? width : -HITBOX_WIDTH);
        int offsetY = y + height / 2 - HITBOX_HEIGHT / 2;  // Centrer la hitbox à la hauteur du personnage
        return new Rectangle(offsetX, offsetY, HITBOX_WIDTH, HITBOX_HEIGHT);
    }

    // Vérifie si l'attaque est hors de l'écran
    public boolean isOutOfScreen(int levelWidth, int levelHeight) {
        return x < 0 || x > levelWidth || y < 0 || y > levelHeight;
    }

    // Vérifie si l'attaque entre en collision avec l'adversaire et inflige des dégâts
    public void checkCollisionWithOpponent(Players opponent) {
        if (getCollisionBox().intersects(opponent.getCollisionBox())) {
            opponent.recevoirAttack();  // Applique les dégâts à l'adversaire
            System.out.println("L'attaque normale a touché l'adversaire !");
        }
    }

    // Ajoute une attaque normale à la liste d'attaques
    public void addAttackNormale(AttackNormale attack) {
        attackNormales.add(attack);
    }

    // Récupère la liste des attaques normales
    public List<AttackNormale> getAttackNormales() {
        return attackNormales;
    }
}
