package Doctrina;

public abstract class ControllableEntity extends MovableEntity {
    protected final MovementController controller;

    public ControllableEntity(MovementController controller) {
        this.controller = controller;
    }

    public void moveWithController() {
        if (controller.isMoving()) {
            move(controller.getDirection());
        }
    }
}
