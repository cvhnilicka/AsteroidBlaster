package com.cormicopiastudios.asteroidblaster.GameEngine.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.B2BodyComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.BulletComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.PlayerComponent;

public class BulletSystem extends IteratingSystem {
    private Entity player;

    @SuppressWarnings("unchecked")
    public BulletSystem(Entity plater) {
        super(Family.all(BulletComponent.class).get());
        this.player = plater;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // get bullet body comp
        B2BodyComponent body = ComponentMapper.getFor(B2BodyComponent.class).get(entity);
        BulletComponent bullet = ComponentMapper.getFor(BulletComponent.class).get(entity);

        body.body.setLinearVelocity(bullet.xVel,bullet.yVel);

        B2BodyComponent playerPos = ComponentMapper.getFor(B2BodyComponent.class).get(player);
        float px = playerPos.body.getPosition().x;
        float py = playerPos.body.getPosition().y;

        //get bullet pos
        float bx = body.body.getPosition().x;
        float by = body.body.getPosition().y;

        // if bullet is 20 units away from player on any axis then it is probably off screen
        if(bx - px > 20 || by - py > 20){
            bullet.isDead = true;
        }
        if (bullet.isDead) {
            body.isDead = true;
        }

    }
}
