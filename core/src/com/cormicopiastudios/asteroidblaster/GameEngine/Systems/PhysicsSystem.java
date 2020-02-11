package com.cormicopiastudios.asteroidblaster.GameEngine.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalIteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.B2BodyComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.TransformComponent;

public class PhysicsSystem extends IntervalIteratingSystem {

    private static final float MAX_STEP_TIME = 1/45f;

    private World world;
    private Array<Entity> bodiesQueue;

    private ComponentMapper<B2BodyComponent> bm = ComponentMapper.getFor(B2BodyComponent.class);
    private ComponentMapper<TransformComponent> tm = ComponentMapper.getFor(TransformComponent.class);


    @SuppressWarnings("unchecked")
    public PhysicsSystem(World world) {
        super(Family.all().get(), MAX_STEP_TIME);
        this.world = world;
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
            tfm.rotation = bodyComp.body.getAngle()* MathUtils.radiansToDegrees;
        }
        bodiesQueue.clear();

    }

    @Override
    protected void processEntity(Entity entity) {
        bodiesQueue.add(entity);
    }
}
