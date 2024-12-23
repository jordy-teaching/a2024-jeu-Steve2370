package Z_Fighter;

import Doctrina.CollidableRepository;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CollisionHandler {
    private Leon_B leon;
    private Annie_B annie;

    public CollisionHandler(Leon_B leon, Annie_B annie) {
        this.leon = leon;
        this.annie = annie;
    }


//
//    public List<StaticEntityZ> checkForCollisions() {
//        List<StaticEntityZ> collidedEntities = new ArrayList<>();
//        Rectangle thisBox = leon.getCollisionBox();
//
//        for (StaticEntityZ entity : CollidableRepository.getInstance()) {
//            if (thisBox.intersects(entity.getCollisionBox())) {
//                collidedEntities.add(entity);
//            }
//        }
//        return collidedEntities;
//    }

//    public void handleCollisions() {
//        List<StaticEntityZ> collisions = checkForCollisions();
//        for (StaticEntityZ entity : collisions) {
//            // Appliquez les effets de collision ici (ex : stop le mouvement, appliquer des dégâts, etc.)
//            System.out.println("Collision détectée avec " + entity);
//        }
//    }
}

