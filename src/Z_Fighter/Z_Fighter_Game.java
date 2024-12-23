package Z_Fighter;

import Doctrina.*;
import Viking.GameConfig;
import Viking.GamePad;
import Doctrina.Canvas;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.util.Random;
import java.util.Scanner;

import static java.awt.Color.BLUE;

public class Z_Fighter_Game extends Game {

    private GamePad gamePad;
    private GamePad gamePad2;
    private Camera camera;

    private Players selectedPlayer1;
    private Players selectedPlayer2;
    private Ia_Controller iaController;

    private Landscape landscape;
    public boolean isDead;
    private int sound;

    @Override
    protected void initialize() {
        GameConfig.setDebugEnabled(false);
        initGamePads();
        camera = new Camera(800, 600, 2000, 600, landscape);
        initLandscape();
        initAudio();
        displayMainMenu();
    }

    private void initGamePads() {
        gamePad = new GamePad(1);
        gamePad2 = new GamePad(2);
        gamePad2.useWasdKeys();
    }

    private void initLandscape() {
        landscape = new Landscape();
    }

    private void initAudio() {
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                    getClass().getClassLoader().getResourceAsStream("audios/My.wav")
            );
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int choixDuJoueur(){
        Scanner scanner = new Scanner(System.in);
        int choix;
        do {
            System.out.println("Veuillez sélectionner votre choix: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Vous devez saisir un chiffre entre 1 et 10: ");
                scanner.next();
            }
            choix = scanner.nextInt();
        } while (choix < 1 || choix > 10);
        return choix;
    }

    public static void displayNameGame() {
        System.out.println("8888888888P                                            \n" +
                "      d88P                                             \n" +
                "     d88P                                              \n" +
                "    d88P                                               \n" +
                "   d88P                                                \n" +
                "  d88P                                                 \n" +
                " d88P                                                  \n" +
                "d8888888888                                            \n" +
                "                                                       \n" +
                "                                                       \n" +
                "                                                       \n" +
                "8888888888 d8b          888      888                   \n" +
                "888        Y8P          888      888                   \n" +
                "888                     888      888                   \n" +
                "8888888    888  .d88b.  88888b.  888888 .d88b.  888d888\n" +
                "888        888 d88P\"88b 888 \"88b 888   d8P  Y8b 888P\"  \n" +
                "888        888 888  888 888  888 888   88888888 888    \n" +
                "888        888 Y88b 888 888  888 Y88b. Y8b.     888    \n" +
                "888        888  \"Y88888 888  888  \"Y888 \"Y8888  888    \n" +
                "                    888                                \n" +
                "               Y8b d88P                                \n" +
                "                \"Y88P\"");
    }

    private void displayMainMenu() {
        Scanner scanner = new Scanner(System.in);
        displayNameGame();
        System.out.println("Choisissez votre mode de jeu :");
        System.out.println("1. You vs Computer");
        System.out.println("2. You vs Player 2");

        int modeDuJeu = -1;
        while (modeDuJeu != 1 && modeDuJeu != 2) {
            while (!scanner.hasNextInt()) {
                System.out.println("Choix invalide.");
                scanner.next();
            }
            modeDuJeu = scanner.nextInt();
            if (modeDuJeu != 1 && modeDuJeu != 2) {
                System.out.println("Choix invalide.");
            }
        }

        if (modeDuJeu == 1) {
            displayCharacterSelection(true);
            iaController = new Ia_Controller();
            selectedPlayer2 = iaController.getCharacter();
            System.out.println("L'IA a choisi son personnage.");
        } else if (modeDuJeu == 2) {
            System.out.println("Sélection du personnage pour le joueur 2...");
            displayCharacterSelection(false);
        }

        if (selectedPlayer1 != null && selectedPlayer2 != null) {
            selectedPlayer1.setOpponent(selectedPlayer2);
            selectedPlayer2.setOpponent(selectedPlayer1);
            System.out.println("Adversaire correctement attribué.");
        } else {
            System.out.println("Erreur : L'adversaire n'a pas été défini correctement.");
        }
        RenderingEngine.getInstance().getScreen().setSize(800, 600);
        RenderingEngine.getInstance().getScreen().show();
        RenderingEngine.getInstance().getScreen().fullscreen();
    }

    private void displayCharacterSelection(boolean isAI) {
        System.out.println("Choisissez un personnage pour le joueur 1 :");
        afficherListePersonnages();
        selectedPlayer1 = choisirPersonnage(gamePad, false);

        if (!isAI) {
            System.out.println("Choisissez un personnage pour le joueur 2 :");
            afficherListePersonnages();
            selectedPlayer2 = choisirPersonnage(gamePad2, false);
        } else {
            selectedPlayer2 = choisirPersonnage(gamePad2, true);
            System.out.println("L'IA a choisi : " + selectedPlayer2.getClass().getSimpleName());
        }

        if (selectedPlayer1 != null && selectedPlayer2 != null) {
            selectedPlayer1.teleport(250, 380);
            selectedPlayer2.teleport(400, 380);
            selectedPlayer1.getBarreSante().setPosition(10, 10);
            selectedPlayer1.getBarreEnergie().setPosition(10, 40);
            selectedPlayer2.getBarreSante().setPosition(600, 10);
            selectedPlayer2.getBarreEnergie().setPosition(600, 40);
            selectedPlayer1.setOpponent(selectedPlayer2);
            selectedPlayer2.setOpponent(selectedPlayer1);
        } else {
            System.out.println("Erreur : Les personnages n'ont pas été sélectionnés correctement.");
        }
    }

    private void afficherListePersonnages() {
        System.out.println("1. Leon Beltran");
        System.out.println("2. Annie Beauregard");
        System.out.println("3. Baragan");
        System.out.println("4. Wonderwise");
        System.out.println("5. Oz");
        System.out.println("6. Ace");
        System.out.println("7. Yashamaru");
        System.out.println("8. Schneizel");
        System.out.println("9. Manon.C");
        System.out.println("10. Tufi");
    }

    private Players choisirPersonnage(GamePad gamePad, boolean isRandom) {
        if (!isRandom) {
            switch (choixDuJoueur()) {
                case 1: return new Leon_B(gamePad);
                case 2: return new Annie_B(gamePad);
                case 3: return new Baragan(gamePad);
                case 4: return new Wonderwise(gamePad);
                case 5: return new Oz(gamePad);
                case 6: return new Ace(gamePad);
                case 7: return new Yashamaru(gamePad);
                case 8: return new Schneizel(gamePad);
                case 9: return new Manon_C(gamePad);
                case 10: return new Tufi(gamePad);
                default:
                    System.out.println("Choix invalide, sélection de Leon_B par défaut.");
                    return new Leon_B(gamePad);
            }
        } else {
            Random rand = new Random();
            int choix = rand.nextInt(10);
            switch (choix) {
                case 0: return new Leon_B(gamePad);
                case 1: return new Annie_B(gamePad);
                case 2: return new Baragan(gamePad);
                case 3: return new Wonderwise(gamePad);
                case 4: return new Oz(gamePad);
                case 5: return new Ace(gamePad);
                case 6: return new Yashamaru(gamePad);
                case 7: return new Schneizel(gamePad);
                case 8: return new Manon_C(gamePad);
                case 9: return new Tufi(gamePad);
                default:
                    System.out.println("Choix invalide.");
                    return new Leon_B(gamePad);
            }
        }
    }



    @Override
    protected void update() {
        if (isDead) return;
        handleInput();
        updateCharacters();
        updateSoundCooldown();
        if (iaController != null) {
            iaController.update();
        }
        checkConditionVictoire();
        int player1X = selectedPlayer1.getX();
        int player1Y = selectedPlayer1.getY();
        int player2X = selectedPlayer2.getX();
        int player2Y = selectedPlayer2.getY();

        camera.update(player1X, player1Y, player2X, player2Y);
    }

    private void checkConditionVictoire() {
        if (selectedPlayer1.isDead()) {
            System.out.println("Le joueur 2 a gagné !");
            rejouer();
        } else if (selectedPlayer2.isDead()) {
            System.out.println("Le joueur 1 a gagné !");
            rejouer();
        }
    }

    private void rejouer() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Voulez-vous rejouer ? (O/N)");

        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("O")) {
            restartGame();
        } else {
            System.out.println("Merci d'avoir joué !");
            System.exit(0);
        }
    }

    private void restartGame() {
        selectedPlayer1 = null;
        selectedPlayer2 = null;
        iaController = null;

        initialize();
        displayMainMenu();
    }

    private void updateSoundCooldown() {
        sound--;
        if (sound < 0) {
            sound = 0;
        }
    }

    private void handleInput() {
        if (gamePad.isQuitPressed()) {
            stop();
        }

        if (gamePad.isFirePressed() && sound == 0) {
            sound = 100;
            GameConfig.setDebugEnabled(true);
        }
    }

    private void updateCharacters() {
        selectedPlayer1.update();
        selectedPlayer2.update();
    }

    @Override
    protected void draw(Canvas canvas) {
        int camX = camera.getX();
        int camY = camera.getY();

        drawBackground(canvas, camX, camY);
        drawCharacters(canvas, camX, camY);

        selectedPlayer1.getBarreSante().draw(canvas);
        selectedPlayer1.getBarreEnergie().draw(canvas);

        selectedPlayer2.getBarreSante().draw(canvas);
        selectedPlayer2.getBarreEnergie().draw(canvas);
    }

    @Override
    protected void draw(Z_Fighter.Canvas canvas) {

    }

    private void drawBackground(Canvas canvas, int camX, int camY) {
        Graphics2D g2d = (Graphics2D) canvas.getGraphics();
        if (g2d != null && landscape.getBackgroundGif() != null) {
            g2d.drawImage(landscape.getBackgroundGif(), -camX, -camY, null);
        }
    }

    private void drawCharacters(Canvas canvas, int camX, int camY) {
        selectedPlayer1.draw(canvas, camX, camY);
        selectedPlayer2.draw(canvas, camX, camY);
    }
}