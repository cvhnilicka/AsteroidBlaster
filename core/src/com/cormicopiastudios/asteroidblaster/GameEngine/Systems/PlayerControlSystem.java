package com.cormicopiastudios.asteroidblaster.GameEngine.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.B2BodyComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.PlayerComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.StateComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Controllers.InputController;
import com.cormicopiastudios.asteroidblaster.GameEngine.Factories.LevelFactory;

public class PlayerControlSystem extends IteratingSystem {

    ComponentMapper<PlayerComponent> pm;
    ComponentMapper<B2BodyComponent> b2m;
    ComponentMapper<StateComponent> sm;
    private LevelFactory lvlF;

    InputController controller;

    @SuppressWarnings("unchecked")
    public PlayerControlSystem(InputController controller, LevelFactory lvlF) {
        super(Family.all(PlayerComponent.class).get());
        this.controller = controller;
        pm = ComponentMapper.getFor(PlayerComponent.class);
        b2m = ComponentMapper.getFor(B2BodyComponent.class);
        sm = ComponentMapper.getFor(StateComponent.class);
        this.lvlF = lvlF;
    }
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        B2BodyComponent b2body = b2m.get(entity);
        StateComponent state = sm.get(entity);
        PlayerComponent player = pm.get(entity);

        int maxSpeed = 5;
        if(controller.left && b2body.body.getLinearVelocity().x > -maxSpeed){
            b2body.body.applyLinearImpulse(new Vector2(-.5f,0), b2body.body.getWorldCenter(), true);
        }
        if(controller.right && b2body.body.getLinearVelocity().x < maxSpeed){
            b2body.body.applyLinearImpulse(new Vector2(.5f,0), b2body.body.getWorldCenter(), true);

        }

        if(controller.up && b2body.body.getLinearVelocity().y < maxSpeed){
            b2body.body.applyLinearImpulse(new Vector2(0,.5f), b2body.body.getWorldCenter(), true);
        }
        if(controller.down && b2body.body.getLinearVelocity().y > -maxSpeed){

            b2body.body.applyLinearImpulse(new Vector2(0,-.5f), b2body.body.getWorldCenter(), true);

        }


        // Shooting Stuff
        if (controller.isMouse1Down) {
            // add a shoot delay later
            if (player.timeSinceLastShot <= 0) {

                Vector3 mPos = new Vector3(controller.mouseLocation.x, controller.mouseLocation.y, 0);
                player.cam.unproject(mPos);

                float speed = 15.f;
                float sx = b2body.body.getPosition().x;
                float sy = b2body.body.getPosition().y;
                float vx = mPos.x - sx;
                float vy = mPos.y - sy;

                float dist = (float) Math.sqrt(vx * vx + vy * vy);

                if (dist != 0) {
                    vx = vx / dist;
                    vy = vy / dist;
                }

                // create bullet in the level factory here
                lvlF.createBullet(sx, sy, vx * speed, vy * speed, mPos);
                player.timeSinceLastShot = player.shootDelay;

            }

        }
        player.timeSinceLastShot -= deltaTime;
    }
}
