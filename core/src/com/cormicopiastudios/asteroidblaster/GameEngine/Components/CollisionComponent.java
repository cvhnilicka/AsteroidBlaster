package com.cormicopiastudios.asteroidblaster.GameEngine.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

public class CollisionComponent implements Component, Pool.Poolable {
    // Store collision data such as entity that this has collided with
    public Entity collisionEntity;

    @Override
    public void reset() {
        collisionEntity = null;
    }
}
