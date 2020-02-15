package com.cormicopiastudios.asteroidblaster.GameEngine.Factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
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
    private Entity player;

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
//        createAsteroid(10,10);
//        createAsteroid(25,20);
//        createAsteroid(25f,0);
//        createAsteroid(0,0);
        createAsteroid(0,5);
    }

    /**
     * I will need to add the code for adding in:
     * - Asteroids
     * - Walls
     * - Shooting Stars
     * */
    // TODO private void addAsteroid()
    // TODO private void addShootingStar()
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
        b2body.body = bodyFactory.makeAsteroidBody(posx,posy,1, BodyFactory.FIXTURE_TYPE.STONE,
                BodyDef.BodyType.DynamicBody, true);

        position.position.set(posx,posy,1);
        texture.texture = assetController.manager.get(assetController.asteroid);
        type.type = TypeComponent.ENEMY;
        b2body.body.setUserData(entity);
        asteroid.intialPos.x = posx;
        asteroid.intialPos.y = posy;
        asteroid.speed = getLaunchSpeed(posx, posy);

        entity.add(b2body);
        entity.add(position);
        entity.add(texture);
        entity.add(asteroid);
        entity.add(type);
        entity.add(collision);

        engine.addEntity(entity);

    }


    private void createPlayer() {

        float posx = 15;
        float posy = 10;
        // create the entity and all the components in it
        player = engine.createEntity();
        B2BodyComponent b2BodyComponent = engine.createComponent(B2BodyComponent.class);
        TransformComponent transformComponent = engine.createComponent(TransformComponent.class);
        TextureComponent textureComponent = engine.createComponent(TextureComponent.class);
        PlayerComponent playerComponent = engine.createComponent(PlayerComponent.class);
        CollisionComponent collisionComponent = engine.createComponent(CollisionComponent.class);
        TypeComponent typeComponent = engine.createComponent(TypeComponent.class);
        StateComponent stateComponent = engine.createComponent(StateComponent.class);

        // create the data for the components
        b2BodyComponent.body = bodyFactory.makeBoxPolyBody(posx,posy,1,2,BodyFactory.FIXTURE_TYPE.STEEL, BodyDef.BodyType.DynamicBody,true);

        // set object pos
        transformComponent.position.set(posx,posy,0);
        textureComponent.texture = assetController.manager.get(assetController.redShip);
        typeComponent.type = TypeComponent.PLAYER;
        stateComponent.set(StateComponent.STATE_NORMAL);
        b2BodyComponent.body.setUserData(player);
        playerComponent.cam = parent.getGamecam();

        // add components to entity
        player.add(b2BodyComponent);
        player.add(transformComponent);
        player.add(textureComponent);
        player.add(playerComponent);
        player.add(collisionComponent);
        player.add(typeComponent);
        player.add(stateComponent);

        // add to engine
        engine.addEntity(player);
    }

    public Entity getPlayer() {
        return player;
    }

    public Vector2 getLaunchSpeed(float posx, float posy) {
        float speed = 5f;
        float velX = player.getComponent(TransformComponent.class).position.x-posx;
        float velY = player.getComponent(TransformComponent.class).position.y-posy;
        Gdx.app.log("velx", ": " + velX);
        Gdx.app.log("vely", ": " + velY);
        Gdx.app.log("player pos", ": " + player.getComponent(TransformComponent.class).position);




        float dist = (float)Math.sqrt(velX*velX+velY*velY);

        if (dist != 0) {
            velX = velX/dist;
            velY = velY/dist;
        }

        Vector2 launchSpeed = new Vector2(speed*velX, speed*velY);

        Gdx.app.log("Launch Speed", ": " + launchSpeed);

        return launchSpeed;
    }

    // TODO updateLevel()
    // TODO getLevel()


}
