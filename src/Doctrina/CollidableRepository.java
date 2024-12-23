package Doctrina;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CollidableRepository implements Iterable<StaticEntity> {
    private static CollidableRepository instance;

    private final List<StaticEntity> registeredEntities;

    private CollidableRepository() {
        registeredEntities = new ArrayList<>();
    }

    public static CollidableRepository getInstance() {
        if (instance == null) {
            instance = new CollidableRepository();
        }
        return instance;
    }

    public void registerEntity(StaticEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null.");
        }
        registeredEntities.add(entity);
    }

    public void unregisterEntity(StaticEntity entity) {
        if (entity == null || !registeredEntities.contains(entity)) {
            throw new IllegalArgumentException("Entity not found in the repository.");
        }
        registeredEntities.remove(entity);
    }

    public void registerEntities(Collection<StaticEntity> entities) {
        if (entities == null || entities.contains(null)) {
            throw new IllegalArgumentException("Entities collection cannot contain null values.");
        }
        registeredEntities.addAll(entities);
    }

    public void unregisterEntities(Collection<StaticEntity> entities) {
        if (entities == null || entities.stream().anyMatch(e -> !registeredEntities.contains(e))) {
            throw new IllegalArgumentException("Some entities not found in the repository.");
        }
        registeredEntities.removeAll(entities);
    }

    public int count() {
        return registeredEntities.size();
    }

    @Override
    public Iterator<StaticEntity> iterator() {
        return registeredEntities.iterator();
    }

    public void clear() {
        registeredEntities.clear();
    }


    public StaticEntity add(StaticEntity staticEntity) {
        return staticEntity;
    }
}
