package Doctrina;

import java.awt.event.KeyEvent;

public class MovementController extends Controller {

    private int upKey = KeyEvent.VK_UP;
    private int downKey = KeyEvent.VK_DOWN;
    private int leftKey = KeyEvent.VK_LEFT;
    private int rightKey = KeyEvent.VK_RIGHT;

//    private int iaRightKey = KeyEvent.VK_D;
//    private int iaLeftKey = KeyEvent.VK_A;
//    private int iaUpKey = KeyEvent.VK_W;
//    private int iaDownKey = KeyEvent.VK_S;

    public MovementController() {
        bindKey(upKey);
        bindKey(downKey);
        bindKey(leftKey);
        bindKey(rightKey);

//        bindKey(iaUpKey);
//        bindKey(iaDownKey);
//        bindKey(iaLeftKey);
//        bindKey(iaRightKey);
    }

    public void useWasdKeys() {
        setUpKey(KeyEvent.VK_W);
        setDownKey(KeyEvent.VK_S);
        setLeftKey(KeyEvent.VK_A);
        setRightKey(KeyEvent.VK_D);
    }

    public Direction getDirection() {
        if (isLeftPressed()) {
            return Direction.LEFT;
        }
        if (isRightPressed()) {
            return Direction.RIGHT;
        }
        if (isUpPressed()) {
            return Direction.UP;
        }
        if (isDownPressed()) {
            return Direction.DOWN;
        }
        return null;
    }

//    public Direction getDirection2() {
//        if (isIaLeftPressed()) {
//            return Direction.LEFT;
//        }
//        if (isIaRightPressed()) {
//            return Direction.RIGHT;
//        }
//        if (isIaUpPressed()) {
//            return Direction.UP;
//        }
//        if (isIaDownPressed()) {
//            return Direction.DOWN;
//        }
//        return null;
//    }

    public boolean isLeftPressed() {
        return isKeyPressed(leftKey);
    }

    public boolean isRightPressed() {
        return isKeyPressed(rightKey);
    }

    public boolean isUpPressed() {
        return isKeyPressed(upKey);
    }

    public boolean isDownPressed() {
        return isKeyPressed(downKey);
    }

    public boolean isMoving() {
        return isRightPressed() || isLeftPressed() || isUpPressed() || isDownPressed();
    }

//    public boolean isIaLeftPressed() {return isKeyPressed(iaLeftKey);}
//
//    public boolean isIaRightPressed() {return isKeyPressed(iaRightKey);}
//
//    public boolean isIaDownPressed() {return isKeyPressed(iaDownKey);}
//
//    public boolean isIaUpPressed() {return isKeyPressed(iaUpKey);}

//    public boolean isIaMoving() {
//        return isIaRightPressed() || isIaLeftPressed() || isIaUpPressed() || isIaDownPressed();
//    }





    public void setDownKey(int keyCode) {
        unbindKey(downKey);
        bindKey(keyCode);
        this.downKey = keyCode;
    }

    public void setUpKey(int keyCode) {
        unbindKey(upKey);
        bindKey(keyCode);
        this.upKey = keyCode;
    }

    public void setLeftKey(int keyCode) {
        unbindKey(leftKey);
        bindKey(keyCode);
        this.leftKey = keyCode;
    }

    public void setRightKey(int keyCode) {
        unbindKey(rightKey);
        bindKey(keyCode);
        this.rightKey = keyCode;
    }
}
