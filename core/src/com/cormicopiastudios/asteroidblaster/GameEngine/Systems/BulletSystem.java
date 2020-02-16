package com.cormicopiastudios.asteroidblaster.GameEngine.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.B2BodyComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.BulletComponent;

public class BulletSystem extends IteratingSystem {

    @SuppressWarnings("unchecked")
    public BulletSystem() {
        super(Family.all(BulletComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // get bullet body comp
        B2BodyComponent body = ComponentMapper.getFor(B2BodyComponent.class).get(entity);
        BulletComponent bullet = ComponentMapper.getFor(BulletComponent.class).get(entity);

        body.body.setLinearVelocity(bullet.xVel,bullet.yVel);
        if (bullet.isDead) {
            body.isDead = true;
        }

    }
}
