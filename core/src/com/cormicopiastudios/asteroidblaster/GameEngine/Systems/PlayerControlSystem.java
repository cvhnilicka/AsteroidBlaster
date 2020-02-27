package com.cormicopiastudios.asteroidblaster.GameEngine.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.AsteroidComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.B2BodyComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.PlayerComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.StarComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.StateComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.TextureComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Controllers.AssetController;
import com.cormicopiastudios.asteroidblaster.GameEngine.Controllers.InputController;
import com.cormicopiastudios.asteroidblaster.GameEngine.Factories.LevelFactory;
import com.cormicopiastudios.asteroidblaster.GameEngine.Views.Hud;
import com.cormicopiastudios.asteroidblaster.GameEngine.Views.PlayScreen;

public class PlayerControlSystem extends IteratingSystem {

    ComponentMapper<PlayerComponent> pm;
    ComponentMapper<B2BodyComponent> b2m;
    ComponentMapper<StateComponent> sm;
    private LevelFactory lvlF;
    private PooledEngine en;

    InputController controller;
    private Hud hud;
    private AssetController assetController;

    @SuppressWarnings("unchecked")
    public PlayerControlSystem(InputController controller, LevelFactory lvlF, PooledEngine en, Hud hud, AssetController assetController) {
        super(Family.all(PlayerComponent.class).get());
        this.controller = controller;
        this.en = en;
        this.assetController = assetController;
        pm = ComponentMapper.getFor(PlayerComponent.class);
        b2m = ComponentMapper.getFor(B2BodyComponent.class);
        sm = ComponentMapper.getFor(StateComponent.class);
        this.lvlF = lvlF;
        this.hud = hud;
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

        if (controller.space && player.numStars > 0 && player.timeSinceLastStar <= 0) {
            Gdx.app.log("Player Controller", "Using Star!");
            player.timeSinceLastStar = player.starDelay;
            for (Entity roid : en.getEntitiesFor(Family.all(AsteroidComponent.class).get())) {
                sm.get(roid).set(StateComponent.ASTEROID_DEAD);
                roid.getComponent(AsteroidComponent.class).starUsed = true;
            }
            player.numStars -= 1;
            hud.updateStars((int)player.numStars);
        }


        // Shooting Stuff
        if (controller.isMouse1Down) {
            // add a shoot delay later
            if (player.timeSinceLastShot <= 0) {

                Vector3 mPos = new Vector3(controller.mouseLocation.x, controller.mouseLocation.y, 0);
                player.cam.unproject(mPos);

                float speed = 15f;
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
        player.timeSinceLastStar -= deltaTime;
    }
}
