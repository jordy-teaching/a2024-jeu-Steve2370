package Z_Fighter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SpriteUtils {

    /**
     * Calcule les dimensions des frames dans un sprite sheet.
     *
     * @param spriteSheet L'image du sprite sheet.
     * @param frameCount  Le nombre total de frames sur la largeur du sprite sheet.
     * @return Un tableau contenant la largeur [0] et la hauteur [1] des frames.
     */
    public static int[] calculateSpriteDimensions(BufferedImage spriteSheet, int frameCount) {
        if (spriteSheet == null || frameCount <= 0) {
            throw new IllegalArgumentException("Sprite sheet invalide ou nombre de frames incorrect.");
        }

        int frameWidth = spriteSheet.getWidth() / frameCount;
        int frameHeight = spriteSheet.getHeight();

        return new int[]{frameWidth, frameHeight};
    }

    public static void main(String[] args) {
        try {
            // Exemple de chargement d'une image sprite sheet
//            BufferedImage spriteSheet = ImageIO.read(SpriteUtils.class.getResourceAsStream("images/Samurai/Idle.png"));
            BufferedImage spriteSheet = ImageIO.read(SpriteUtils.class.getClassLoader().getResourceAsStream("images/Technique_Eau/Water1/1/water1_0.png"));
            // Calculer les dimensions des frames
            int[] dimensions = calculateSpriteDimensions(spriteSheet, 6);

            // Afficher les résultats
            System.out.println("Largeur d'une frame: " + dimensions[0]);
            System.out.println("Hauteur d'une frame: " + dimensions[1]);
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de l'image: " + e.getMessage());
        }
    }
}
