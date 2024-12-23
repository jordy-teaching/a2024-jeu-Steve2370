package Z_Fighter;

import Doctrina.Canvas;
import Doctrina.Game;
import Viking.GamePad;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Un écran de sélection de personnage.
 * Le joueur peut choisir parmi 11 personnages, affichés par leur nom.
 */
public class CharacterSelectionScreen extends Game {

    private GamePad gamePad;
    private List<CharacterInfo> characters;
    private int selectedIndex = 0;
    private BarreVie barreSante;
    private BarreVie barreEnergie;

    // Classe interne pour stocker les infos d’un personnage
    public static class CharacterInfo {
        public String name;
        public Class<? extends Players> characterClass;

        public CharacterInfo(String name, Class<? extends Players> cls) {
            this.name = name;
            this.characterClass = cls;
        }
    }

    /**
     * Constructeur de l’écran de sélection
     * @param gamePad Le contrôleur utilisé pour naviguer dans le menu
     */
    public CharacterSelectionScreen(GamePad gamePad) {
        this.gamePad = gamePad;
        this.characters = new ArrayList<>();

        // Ajouter vos 11 personnages par leur nom
        // Exemple :
        characters.add(new CharacterInfo("Leon", Leon_B.class));
        characters.add(new CharacterInfo("Annie", Annie_B.class));
//        characters.add(new CharacterInfo("Zeloss", Zeloss.class));
        characters.add(new CharacterInfo("Baragan", Baragan.class));
        characters.add(new CharacterInfo("Ace", Ace.class));
        characters.add(new CharacterInfo("Schneizel", Schneizel.class));
        characters.add(new CharacterInfo("Tufi", Tufi.class));
        characters.add(new CharacterInfo("Oz", Oz.class));
        characters.add(new CharacterInfo("Yashamaru", Yashamaru.class));
        characters.add(new CharacterInfo("Wonderwise", Wonderwise.class));
    }

    @Override
    protected void initialize() {
        // Rien de spécial
    }

    @Override
    protected void update() {
        handleInput();
    }

    private void handleInput() {
        // Navigation gauche/droite
        if (gamePad.isLeftPressed()) {
            selectedIndex--;
            if (selectedIndex < 0) {
                selectedIndex = characters.size() - 1;
            }
        } else if (gamePad.isRightPressed()) {
            selectedIndex++;
            if (selectedIndex >= characters.size()) {
                selectedIndex = 0;
            }
        }

        // Valider le choix (ex: Attack_1)
        if (gamePad.isAttack_1()) {
            stop(); // On ferme l’écran de sélection, prêt à récupérer le choix
        }

        // Quitter sans choisir
        if (gamePad.isQuitPressed()) {
            stop();
        }
    }

    @Override
    protected void draw(Canvas canvas) {
        Graphics2D g2d = (Graphics2D) canvas.getGraphics();
        if (g2d == null) return;

        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        // Fond uni
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(0, 0, canvasWidth, canvasHeight);

        // Positionnement
        int centerX = canvasWidth / 2;
        int centerY = canvasHeight / 2;
        int spacing = 150; // Espace entre les noms

        int startX = centerX - (characters.size() * spacing) / 2;

        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics fm = g2d.getFontMetrics();

        for (int i = 0; i < characters.size(); i++) {
            CharacterInfo cinfo = characters.get(i);
            int x = startX + i * spacing;
            int y = centerY;

            String name = cinfo.name;
            int nameWidth = fm.stringWidth(name);
            int nameHeight = fm.getAscent();

            // Si le perso est sélectionné, on le met en évidence
            if (i == selectedIndex) {
                g2d.setColor(Color.YELLOW);
                // Dessiner un rectangle autour du texte
                int rectX = x - nameWidth/2 - 10;
                int rectY = y - nameHeight - 10;
                int rectWidth = nameWidth + 20;
                int rectHeight = nameHeight + 20;
                g2d.drawRect(rectX, rectY, rectWidth, rectHeight);
            }

            g2d.setColor(Color.WHITE);
            g2d.drawString(name, x - nameWidth / 2, y);
        }

        // Indication de contrôle
        g2d.setColor(Color.WHITE);
        String info = "Utilisez les flèches gauche/droite pour choisir, Attaque_1 pour valider, Quit pour sortir";
        int infoWidth = fm.stringWidth(info);
        g2d.drawString(info, centerX - infoWidth / 2, canvasHeight - 50);
    }

    @Override
    protected void draw(Z_Fighter.Canvas canvas) {

    }

    /**
     * Retourne la classe du personnage choisi après l’arrêt de l’écran
     */
    public Class<? extends Players> getSelectedCharacterClass() {
        return characters.get(selectedIndex).characterClass;
    }

    public BarreVie getBarreSante() {
        return barreSante;
    }

    public BarreVie getBarreEnergie() {
        return barreEnergie;
    }
}
