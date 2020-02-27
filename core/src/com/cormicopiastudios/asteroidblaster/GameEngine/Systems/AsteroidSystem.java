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


        if (position.position.x < 0 || position.position.y < 0 || position.position.x > 30 || position.position.y > 35) {
            asteroid.timeOffScreen += deltaTime;
        } else {
            asteroid.timeOffScreen = 0.f;
        }

        if (asteroid.timeOffScreen > 2) {
            asteroid.isDead = true;
        }

        if (asteroid.isDead && !asteroid.wasShot) {
            Gdx.app.log("REPLACE", "Natural causes");
            body.isDead = true;
            lvlf.replaceAsteroid();
        } else if (asteroid.isDead && asteroid.wasShot) {
            body.isDead = true;
            lvlf.createAsteroid(lvlf.getAsteroidSpawn());
        } else if (asteroid.isDead && asteroid.starUsed) {
            body.isDead = true;
            lvlf.replaceAsteroid();
        }


    }

}
