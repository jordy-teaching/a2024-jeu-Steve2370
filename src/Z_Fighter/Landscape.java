package Z_Fighter;

import Doctrina.Canvas;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Landscape {
    private static final String[] BACKGROUND_PATHS = {
            "resources/images/Fond_1.png",
            "resources/images/Fond_2.png",
            "resources/images/Fond_3.png",
            "resources/images/Fond_4.png",
            "resources/images/Fond_5.png",
            "resources/images/Fond_6.png"
    };

    private Image backgroundGif;
    private int gifWidth;
    private int gifHeight;
    private Camera camera;

    public Landscape() {
        loadRandomBackground();
    }

    public static Image scale(Image source, int width, int height) {
        if (width <= 0 || height <= 0) {
            System.out.println("Dimensions invalides pour la méthode scale : " + width + "x" + height);
            return source;
        }

        BufferedImage buf = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = buf.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(source, 0, 0, width, height, null);
        g.dispose();
        return buf;
    }

    private void loadRandomBackground() {
        Random rand = new Random();
        String chosenPath = BACKGROUND_PATHS[rand.nextInt(BACKGROUND_PATHS.length)];
        try {
            backgroundGif = new ImageIcon(chosenPath).getImage();

            gifWidth = backgroundGif.getWidth(null);
            gifHeight = backgroundGif.getHeight(null);

            if (gifWidth <= 0 || gifHeight <= 0) {
                throw new Exception("Dimensions invalides pour l'image de fond.");
            }
            System.out.println("Chargement du fond réussi: " + chosenPath + " (" + gifWidth + " x " + gifHeight + ")");
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement de l'image GIF : " + e.getMessage());
            backgroundGif = null;
            gifWidth = 800;
            gifHeight = 600;
        }
    }

    public int getWidth() {
        return gifWidth;
    }

    public int getHeight() {
        return gifHeight;
    }

    public Image getBackgroundGif() {
        return backgroundGif;
    }

    public void draw(Canvas canvas, int cameraX) {
        int canvasWidth = 800;
        int canvasHeight = 600;

        if (canvasWidth <= 0 || canvasHeight <= 0) {
            return;
        }

        if (backgroundGif != null) {
            Image scaled = scale(backgroundGif, canvasWidth, canvasHeight);
            int xOffset = - (cameraX % canvasWidth);
            canvas.drawImage(scaled, xOffset, 0);
            canvas.drawImage(scaled, xOffset + canvasWidth, 0);
        } else {
            Graphics2D g2d = (Graphics2D) canvas.getGraphics();
            if (g2d != null) {
                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, canvasWidth, canvasHeight);
            }
        }
    }
}
