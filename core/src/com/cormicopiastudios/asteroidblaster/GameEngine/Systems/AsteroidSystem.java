package com.cormicopiastudios.asteroidblaster.GameEngine.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.AsteroidComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.B2BodyComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.TransformComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Factories.LevelFactory;

public class AsteroidSystem extends IteratingSystem {
    private ComponentMapper<AsteroidComponent> am;
    private ComponentMapper<B2BodyComponent> bm;
    private ComponentMapper<TransformComponent> tm;
    private LevelFactory lvlf;
    private Entity player;

    @SuppressWarnings("unchecked")
    public AsteroidSystem(LevelFactory lvlf) {
        super(Family.all(AsteroidComponent.class).get());
        am = ComponentMapper.getFor(AsteroidComponent.class);
        bm = ComponentMapper.getFor(B2BodyComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
        this.lvlf = lvlf;
        this.player = lvlf.getPlayer();

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AsteroidComponent asteroid = am.get(entity);
        B2BodyComponent body = bm.get(entity);
        TransformComponent position = tm.get(entity);

        float dist = asteroid.intialPos.dst(new Vector2(position.position.x, position.position.y));
        if (dist > 28) {
//            Gdx.app.log("Asteroid System", String.valueOf(new Vector2(asteroid.intialPos.x,asteroid.intialPos.y)));
            position.position.set(asteroid.intialPos.x,asteroid.intialPos.y, 0);
            body.body.setTransform(new Vector2(asteroid.intialPos.x,asteroid.intialPos.y), body.body.getAngle());

        }
        if (asteroid.isDead) {
            body.isDead = true;
        }


    }
}
