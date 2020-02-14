package com.cormicopiastudios.asteroidblaster.GameEngine.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalIteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.AsteroidComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.B2BodyComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.PlayerComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.TransformComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.TypeComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Controllers.InputController;

public class PhysicsSystem extends IntervalIteratingSystem {

    private static final float MAX_STEP_TIME = 1/45f;

    private World world;
    private InputController controller;
    private Array<Entity> bodiesQueue;

    private ComponentMapper<B2BodyComponent> bm = ComponentMapper.getFor(B2BodyComponent.class);
    private ComponentMapper<TransformComponent> tm = ComponentMapper.getFor(TransformComponent.class);


    @SuppressWarnings("unchecked")
    public PhysicsSystem(World world, InputController controller) {
        super(Family.all().get(), MAX_STEP_TIME);
        this.world = world;
        this.controller = controller;
        this.bodiesQueue = new Array<Entity>();
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
            } else if (ent.getComponent(TypeComponent.class).type == TypeComponent.ENEMY) {
                bodyComp.body.setLinearVelocity(ent.getComponent(AsteroidComponent.class).speed);
            }

//            else {
//                tfm.rotation = bodyComp.body.getAngle() * MathUtils.radiansToDegrees;
//            }
            // **********************************************************
        }
        bodiesQueue.clear();

    }

    @Override
    protected void processEntity(Entity entity) {
        bodiesQueue.add(entity);
    }
}
