package Viking;

import Doctrina.MovementController;

import java.awt.event.KeyEvent;

public class GamePad extends MovementController {
    private int quitKey = KeyEvent.VK_Q;
    private int fireKey = KeyEvent.VK_SPACE;
    private int attack_1 = KeyEvent.VK_U;
    private int attack_2 = KeyEvent.VK_I;
    private int attack_3 = KeyEvent.VK_O;
    private int attack_4 = KeyEvent.VK_J;
    private int attack_5 = KeyEvent.VK_K;
    private int attack_special = KeyEvent.VK_L;

    public GamePad() {
        configureKeysJoueur2();
        bindAllKeys();
    }

    public GamePad(int nbJoueur) {
        if (nbJoueur == 1) {
            configureKeysJoueur1();
        } else {
            configureKeysJoueur2();
        }
        bindAllKeys();
    }

    private void configureKeysJoueur1() {
        quitKey = KeyEvent.VK_Q;
        fireKey = KeyEvent.VK_SPACE;
        attack_1 = KeyEvent.VK_R;
        attack_2 = KeyEvent.VK_T;
        attack_3 = KeyEvent.VK_Y;
        attack_4 = KeyEvent.VK_F;
        attack_5 = KeyEvent.VK_G;
        attack_special = KeyEvent.VK_H;
    }

    private void configureKeysJoueur2() {
        quitKey = KeyEvent.VK_Q;
        fireKey = KeyEvent.VK_SPACE;
        attack_1 = KeyEvent.VK_U;
        attack_2 = KeyEvent.VK_I;
        attack_3 = KeyEvent.VK_O;
        attack_4 = KeyEvent.VK_J;
        attack_5 = KeyEvent.VK_K;
        attack_special = KeyEvent.VK_L;
    }

    private void bindAllKeys() {
        bindKey(quitKey);
        bindKey(fireKey);
        bindKey(attack_1);
        bindKey(attack_2);
        bindKey(attack_3);
        bindKey(attack_4);
        bindKey(attack_5);
        bindKey(attack_special);
    }

    public boolean isQuitPressed() {
        return isKeyPressed(quitKey);
    }

    public boolean isFirePressed() {
        return isKeyPressed(fireKey);
    }

    public boolean isAttack_1() {return isKeyPressed(attack_1);}

    public boolean isAttack_2() {return isKeyPressed(attack_2);}

    public boolean isAttack_3() {return isKeyPressed(attack_3);}

    public boolean isAttack_4() {return isKeyPressed(attack_4);}

    public boolean isAttack_5() {return isKeyPressed(attack_5);}

    public boolean isAttack_special() {return isKeyPressed(attack_special);}
}
