package com.cormicopiastudios.asteroidblaster.GameEngine.Systems;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.B2BodyComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.StarComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.TransformComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Factories.LevelFactory;

public class StarSystem extends IteratingSystem {

    private LevelFactory lvlF;
    private ComponentMapper<StarComponent> sm;
    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<B2BodyComponent> bm;

    @SuppressWarnings("unchecked")
    public StarSystem(LevelFactory lvlf) {
        super(Family.all(StarComponent.class).get());
        sm = ComponentMapper.getFor(StarComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
        bm = ComponentMapper.getFor(B2BodyComponent.class);
        this.lvlF = lvlf;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        StarComponent star = sm.get(entity);
        B2BodyComponent body = bm.get(entity);
        TransformComponent position = tm.get(entity);

        float dist = star.startingPosition.dst(new Vector2(position.position.x,position.position.y));

        if (dist > 40) {
            star.isDead = true;
            body.isDead = true;
        }

    }
}
