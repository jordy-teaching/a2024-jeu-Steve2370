package Doctrina;

import java.awt.*;

public class Collision {
    private final MovableEntity entity;

    public Collision(MovableEntity entity) {
        this.entity = entity;
    }

    public int getAllowedSpeed(Direction direction) {
        int distance = switch (direction) {
            case LEFT -> getAllowedLeftSpeed();
            case RIGHT -> getAllowedRightSpeed();
            case UP -> getAllowedUpSpeed();
            case DOWN -> getAllowedDownSpeed();
        };
        return Math.max(0, Math.min(distance, entity.getSpeed()));
    }

    private int getAllowedUpSpeed() {
        return distance(other -> entity.y - (other.y + other.height));
    }

    private int getAllowedDownSpeed() {
        return distance(other -> other.y - (entity.y + entity.height));
    }

    private int getAllowedLeftSpeed() {
        return distance(other -> entity.x - (other.x + other.width));
    }

    private int getAllowedRightSpeed() {
        return distance(other -> other.x - (entity.x + entity.width));
    }

    private int distance(DistanceCalculator calculator) {
        Rectangle futureHitBox = entity.getHitBox();
        int allowedDistance = entity.getSpeed();
        for (StaticEntity other : CollidableRepository.getInstance()) {
            Rectangle otherCollisionBox = other.getCollisionBox();

            if (futureHitBox.intersects(otherCollisionBox)) {
                int distance = calculator.calculateWith(other);
                if (distance <= 0) {
                    return 0;
                }
                allowedDistance = Math.min(allowedDistance, distance);
            }
        }
        return allowedDistance;
    }

    public boolean isHitByAttack(MovableEntity attacker, StaticEntity other) {
        if (other == null || !attacker.isAttacking()) {
            return false;
        }
        return attacker.getAttackHitBox().intersects(other.getCollisionBox());
    }


    public interface DistanceCalculator {
        int calculateWith(StaticEntity other);
    }
}
