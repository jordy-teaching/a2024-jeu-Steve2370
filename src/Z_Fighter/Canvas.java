package Z_Fighter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Canvas extends JPanel {
    private BufferedImage buffer;

    public Canvas(int width, int height) {
        buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        setPreferredSize(new Dimension(width, height));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(buffer, 0, 0, null);
    }

    public Graphics getGraphics() {
        return buffer.getGraphics();
    }

    public void clear() {
        Graphics g = buffer.getGraphics();
        g.setColor(Color.WHITE); // Couleur de fond par défaut
        g.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
        g.dispose();
        repaint();
    }

    public void drawImage(Image frame, int i, int i1) {
    }
}

