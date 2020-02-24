package com.cormicopiastudios.asteroidblaster.GameEngine.Systems;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.B2BodyComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.StarComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.StateComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.TransformComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Factories.LevelFactory;

public class StarSystem extends IteratingSystem {

    private LevelFactory lvlF;
    private ComponentMapper<StarComponent> sm;
    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<B2BodyComponent> bm;
    private ComponentMapper<StateComponent> stm;

    @SuppressWarnings("unchecked")
    public StarSystem(LevelFactory lvlf) {
        super(Family.all(StarComponent.class).get());
        sm = ComponentMapper.getFor(StarComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
        bm = ComponentMapper.getFor(B2BodyComponent.class);
        stm = ComponentMapper.getFor(StateComponent.class);
        this.lvlF = lvlf;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        StarComponent star = sm.get(entity);
        B2BodyComponent body = bm.get(entity);
        TransformComponent position = tm.get(entity);
        StateComponent state = stm.get(entity);



        state.time += deltaTime;

        if (position.position.x < 0 || position.position.y < 0 || position.position.x > 30 || position.position.y > 35) {
            star.timeOffScreen += deltaTime;
        } else {
            star.timeOffScreen = 0f;
        }

        float dist = star.startingPosition.dst(new Vector2(position.position.x,position.position.y));

        if (state.time > 15 || dist > 40 || star.timeOffScreen >= 4) {
            star.isDead = true;
        }
        if (star.isDead) {
            body.isDead = true;
            lvlF.removeStar();
            return;
        }

    }
}
