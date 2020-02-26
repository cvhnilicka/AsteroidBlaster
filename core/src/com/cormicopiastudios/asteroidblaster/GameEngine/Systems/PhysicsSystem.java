package com.cormicopiastudios.asteroidblaster.GameEngine.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IntervalIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.AsteroidComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.B2BodyComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.PlayerComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.StarComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.StateComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.TextureComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.TransformComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.TypeComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Controllers.InputController;
import com.cormicopiastudios.asteroidblaster.GameEngine.Factories.LevelFactory;
import com.cormicopiastudios.asteroidblaster.GameEngine.Views.PlayScreen;

public class PhysicsSystem extends IntervalIteratingSystem {

    private static final float MAX_STEP_TIME = 1/45f;

    private World world;
    private InputController controller;
    private Array<Entity> bodiesQueue;

    private ComponentMapper<B2BodyComponent> bm = ComponentMapper.getFor(B2BodyComponent.class);
    private ComponentMapper<TransformComponent> tm = ComponentMapper.getFor(TransformComponent.class);
    private PooledEngine engine;
    private LevelFactory lvlf;
    private PlayScreen parent;


    @SuppressWarnings("unchecked")
    public PhysicsSystem(World world, InputController controller, PooledEngine en, LevelFactory lvlf, PlayScreen parent) {
        super(Family.all().get(), MAX_STEP_TIME);
        this.world = world;
        this.controller = controller;
        this.bodiesQueue = new Array<Entity>();
        this.engine = en;
        this.lvlf = lvlf;
        this.parent = parent;
    }

    @Override
    public void updateInterval(){
        super.updateInterval();
        world.step(MAX_STEP_TIME, 6, 2);
        for (Entity ent : bodiesQueue) {
            TransformComponent tfm = tm.get(ent);
            B2BodyComponent bodyComp = bm.get(ent);
            Vector2 pos = bodyComp.body.getPosition();
            tfm.position.x = pos.x;
            tfm.position.y = pos.y;
            // **********************************************************
            // I might need to move this chunk to the playercontrolsystem
            if (ent.getComponent(TypeComponent.class).type == TypeComponent.PLAYER) {
                // get mouse location
                Vector3 mosLoc = new Vector3(controller.mouseLocation.x, controller.mouseLocation.y, 0);
                ent.getComponent(PlayerComponent.class).cam.unproject(mosLoc);

                float rot = (float) Math.toDegrees(
                        Math.atan2(mosLoc.y - pos.y,
                                mosLoc.x-pos.x));
                if (rot < 0) {
                    rot += 360;
                }
                tfm.rotation = rot-90f;
                bodyComp.body.setTransform(pos, MathUtils.degreesToRadians * tfm.rotation);

                if (tfm.position.x < 0 || tfm.position.y < 0 || tfm.position.x > 30 || tfm.position.y > 25) {
                    ent.getComponent(PlayerComponent.class).offscreenTimer += MAX_STEP_TIME;
                    ent.getComponent(PlayerComponent.class).offScreen = true;
                    ent.getComponent(TextureComponent.class).region = parent.getAssetController().manager.get(parent.getAssetController().arrowPix, TextureAtlas.class).findRegion("Arrow");
                } else {
                    ent.getComponent(PlayerComponent.class).offscreenTimer = 0.f;
                    ent.getComponent(PlayerComponent.class).offScreen = false;
                }

                if (ent.getComponent(PlayerComponent.class).offscreenTimer > 3) {
                    Gdx.app.log("Off screen", "Should be dead");
                    this.parent.setGameOver();
                }

            } else if (ent.getComponent(TypeComponent.class).type == TypeComponent.ENEMY &&
                    Math.abs(bodyComp.body.getLinearVelocity().x) < Math.abs(ent.getComponent(AsteroidComponent.class).speed.x) &&
                    Math.abs(bodyComp.body.getLinearVelocity().y) < Math.abs(ent.getComponent(AsteroidComponent.class).speed.y)
            ) {
                bodyComp.body.applyLinearImpulse(ent.getComponent(AsteroidComponent.class).speed, bodyComp.body.getWorldCenter(), true);
//                bodyComp.body.setLinearVelocity(ent.getComponent(AsteroidComponent.class).speed);
            } else if (ent.getComponent(TypeComponent.class).type == TypeComponent.BULLET) {
                bodyComp.body.setAngularVelocity(tfm.rotation);
            } else if (ent.getComponent(TypeComponent.class).type == TypeComponent.SHOOTINGSTAR) {
                bodyComp.body.setLinearVelocity(ent.getComponent(StarComponent.class).velocity);
                bodyComp.body.setAngularVelocity(tfm.rotation);
            }
            if(bodyComp.isDead){
                world.destroyBody(bodyComp.body);
                engine.removeEntity(ent);
            }

        }
        bodiesQueue.clear();

    }

    @Override
    protected void processEntity(Entity entity) {
        bodiesQueue.add(entity);
    }
}
