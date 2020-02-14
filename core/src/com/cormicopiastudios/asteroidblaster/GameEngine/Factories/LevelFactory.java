package com.cormicopiastudios.asteroidblaster.GameEngine.Factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.AsteroidComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.B2BodyComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.CollisionComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.PlayerComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.StateComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.TextureComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.TransformComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.TypeComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Controllers.AssetController;
import com.cormicopiastudios.asteroidblaster.GameEngine.Views.PlayScreen;

public class LevelFactory {

    private int level = 0;
    private int numAsteroids = 3;

    private World world;
    private PooledEngine engine;
    private BodyFactory bodyFactory;
    private AssetController assetController;
    private PlayScreen parent;

    public LevelFactory(World world, PooledEngine en, PlayScreen parent) {
        this.parent = parent;
        this.world = world;
        this.engine = en;
        this.assetController = parent.getAssetController();
        bodyFactory = BodyFactory.getInstance(world);
        createPlayer();
        initialAsteroids();

    }

    private int getNumSections() {
        return level + 3;
    }


    public void initialAsteroids() {
        createAsteroid(10,10);
        createAsteroid(25,20);
        createAsteroid(25f,0);
        createAsteroid(0,0);
        createAsteroid(0,20);
    }

    /**
     * I will need to add the code for adding in:
     * - Asteroids
     * - Walls
     * - Shooting Stars
     * */
    // TODO private void addAsteroid()
    // TODO private void assShootingStar()
    // TODO private void addWalls()


//    public Vector2 getNextSpawn() {
//        int angleOffset = 360/getNumSections();
//
//    }

    public void createAsteroid(float posx, float posy) {
        // logic to add an asteroid
        Entity entity = engine.createEntity();
        B2BodyComponent b2body = engine.createComponent(B2BodyComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        AsteroidComponent asteroid = engine.createComponent(AsteroidComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);
        CollisionComponent collision = engine.createComponent(CollisionComponent.class);

        // set data
        b2body.body = bodyFactory.makeAsteroidBody(posx,posy,3, BodyFactory.FIXTURE_TYPE.STONE,
                BodyDef.BodyType.DynamicBody, true);

        position.position.set(posx,posy,1);
        texture.texture = assetController.manager.get(assetController.asteroid);
        type.type = TypeComponent.ENEMY;
        b2body.body.setUserData(entity);
        asteroid.speed = 10.f;

        entity.add(b2body);
        entity.add(position);
        entity.add(texture);
        entity.add(asteroid);
        entity.add(type);
        entity.add(collision);

        engine.addEntity(entity);

    }

    private void createPlayer() {
        // create the entity and all the components in it
        Entity entity = engine.createEntity();
        B2BodyComponent b2BodyComponent = engine.createComponent(B2BodyComponent.class);
        TransformComponent transformComponent = engine.createComponent(TransformComponent.class);
        TextureComponent textureComponent = engine.createComponent(TextureComponent.class);
        PlayerComponent playerComponent = engine.createComponent(PlayerComponent.class);
        CollisionComponent collisionComponent = engine.createComponent(CollisionComponent.class);
        TypeComponent typeComponent = engine.createComponent(TypeComponent.class);
        StateComponent stateComponent = engine.createComponent(StateComponent.class);

        // create the data for the components
        b2BodyComponent.body = bodyFactory.makeBoxPolyBody(12.5f,10,1,2,BodyFactory.FIXTURE_TYPE.STEEL, BodyDef.BodyType.DynamicBody,true);

        // set object pos
        transformComponent.position.set(5,5,0);
        textureComponent.texture = assetController.manager.get(assetController.redShip);
        typeComponent.type = TypeComponent.PLAYER;
        stateComponent.set(StateComponent.STATE_NORMAL);
        b2BodyComponent.body.setUserData(entity);
        playerComponent.cam = parent.getGamecam();

        // add components to entity
        entity.add(b2BodyComponent);
        entity.add(transformComponent);
        entity.add(textureComponent);
        entity.add(playerComponent);
        entity.add(collisionComponent);
        entity.add(typeComponent);
        entity.add(stateComponent);

        // add to engine
        engine.addEntity(entity);
    }

    // TODO updateLevel()
    // TODO getLevel()


}
