package com.cormicopiastudios.asteroidblaster.GameEngine.Factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.AnimationComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.AsteroidComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.B2BodyComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.BulletComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.CollisionComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.PlayerComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.StarComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.StateComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.TextureComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.TransformComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.TypeComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Controllers.AssetController;
import com.cormicopiastudios.asteroidblaster.GameEngine.Views.PlayScreen;

import java.util.Random;

public class LevelFactory {

    private int level = 0;
    private int numAsteroids;

    private int stars;

    private World world;
    private PooledEngine engine;
    private BodyFactory bodyFactory;
    private AssetController assetController;
    private PlayScreen parent;
    private Entity player;
    private TextureAtlas shipAt;
    private TextureAtlas asteroidAt;
    private TextureAtlas bulletAt;
    private TextureAtlas starAt;

    private float screenYMax;
    private float screenXMax;

    private String[] bucketMappings;

    private Array<Entity> roids;

    private ObjectMap<Integer, Array<Entity>> asteroidBuckets;

    public LevelFactory(World world, PooledEngine en, PlayScreen parent) {
        this.parent = parent;
        this.world = world;
        this.engine = en;
        this.assetController = parent.getAssetController();
        this.shipAt = assetController.manager.get(assetController.redShipPix, TextureAtlas.class);
        this.asteroidAt = assetController.manager.get(assetController.asteroidPix, TextureAtlas.class);
        this.bulletAt = assetController.manager.get(assetController.bulletPix, TextureAtlas.class);
        this.starAt = assetController.manager.get(assetController.starPix, TextureAtlas.class);
        bodyFactory = BodyFactory.getInstance(world);
        asteroidBuckets = new ObjectMap<>();
        bucketMappings = new String[360];
        this.screenXMax = this.parent.getGamecam().viewportWidth;
        this.screenYMax = this.parent.getGamecam().viewportHeight;
        numAsteroids = 0;
        stars = 0;
        roids = new Array<Entity>();
        createPlayer();
        initialAsteroids();
        createStar();

    }

    private int getNumSections() {
        return level + 3;
    }


    public void initialAsteroids() {
//        createAsteroid(0,0);
//        createAsteroid(0,25);
//        createAsteroid(30,0);
//        createAsteroid(30,25);
//        createAsteroid(0,35);
        createAsteroid(getAsteroidSpawn().x, getAsteroidSpawn().y);
        createAsteroid(getAsteroidSpawn().x, getAsteroidSpawn().y);
        createAsteroid(getAsteroidSpawn().x, getAsteroidSpawn().y);

    }

    private void setBucketMappings() {
        int offset = (int)Math.floor(360/getNumSections());
        int offsetCounter = 0;
        for (int i = 0; i < 360;  i++) {
            if (offsetCounter < offset) {
            }
        }
    }



    public Entity createBullet(float x, float y, float xVel, float yVel, Vector3 mPos) {
        Entity entity = engine.createEntity();
        AnimationComponent animComp = engine.createComponent(AnimationComponent.class);
        B2BodyComponent b2dbody = engine.createComponent(B2BodyComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);
        CollisionComponent colComp = engine.createComponent(CollisionComponent.class);
        BulletComponent bul = engine.createComponent(BulletComponent.class);
        StateComponent stateComponent = engine.createComponent(StateComponent.class);

        b2dbody.body = bodyFactory.makeCirclePolyBody(x, y, 0.5f,
                BodyFactory.FIXTURE_TYPE.STONE, BodyDef.BodyType.DynamicBody, true);
        b2dbody.body.setBullet(true); // increase physics computation to limit body travelling through other objects
        bodyFactory.makeAllFixturesSensors(b2dbody.body); // make bullets sensors so they don't move player
        position.position.set(x, y, 0);
        Animation anim = new Animation(0.1f, bulletAt.findRegions("bullet"));
        anim.setPlayMode(Animation.PlayMode.LOOP);
        animComp.animations.put(0,anim);
        texture.region = bulletAt.findRegion("bullet");
        texture.texture = assetController.manager.get(assetController.bullet);
        type.type = TypeComponent.BULLET;
        b2dbody.body.setUserData(entity);
        bul.xVel = xVel;
        bul.yVel = yVel;
        position.scale.x = 1f;
        position.scale.y = 1f;
        position.rotation = (float) Math.atan2((double) mPos.y - y, (double) mPos.x-x )*180f/(float)Math.PI;
        position.rotation -= 90f;
        entity.add(stateComponent);
        entity.add(animComp);
        entity.add(bul);
        entity.add(colComp);
        entity.add(b2dbody);
        entity.add(position);
        entity.add(texture);
        entity.add(type);
        engine.addEntity(entity);
        return entity;
    }

    public void createStar() {

        Vector2 spawnloc = getStarSpawn();

        float sX, sY;
        sX = spawnloc.x;
        sY = spawnloc.y;
        Entity entity = engine.createEntity();

        B2BodyComponent body = engine.createComponent(B2BodyComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        StarComponent starComp = engine.createComponent(StarComponent.class);
        CollisionComponent collisionCom = engine.createComponent(CollisionComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);
        AnimationComponent animComp = engine.createComponent(AnimationComponent.class);
        StateComponent state = engine.createComponent(StateComponent.class);

        body.body = bodyFactory.makeCirclePolyBody(sX,sY,1, BodyFactory.FIXTURE_TYPE.RUBBER,
                BodyDef.BodyType.DynamicBody, true);

        position.position.set(sX,sY,1);
        Animation anim = new Animation(0.1f, this.starAt.findRegions("Star"));
        anim.setPlayMode(Animation.PlayMode.LOOP);
        animComp.animations.put(0,anim);
        state.set(StateComponent.STAR_SHOOTING);
        texture.region = starAt.findRegion("Star");
        type.type = TypeComponent.SHOOTINGSTAR;
        body.body.setUserData(entity);
        starComp.startingPosition.x = sX;
        starComp.startingPosition.y = sY;
        position.scale.x = 2f;
        position.scale.y = 2f;
        starComp.velocity = getLaunchSpeed(sX,sY);
        position.rotation = (float) Math.atan2((double)starComp.velocity.y - sY,
                (double)starComp.velocity.x - sX)*180f/(float)Math.PI;
         position.rotation -= 90f;


        entity.add(body);
        entity.add(position);
        entity.add(texture);
        entity.add(starComp);
        entity.add(collisionCom);
        entity.add(type);
        entity.add(animComp);
        entity.add(state);

        engine.addEntity(entity);
        stars += 1;

    }

    private void createAsteroid(float posx, float posy) {
        // logic to add an asteroid
        Entity entity = engine.createEntity();
        B2BodyComponent b2body = engine.createComponent(B2BodyComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        AsteroidComponent asteroid = engine.createComponent(AsteroidComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);
        CollisionComponent collision = engine.createComponent(CollisionComponent.class);
        AnimationComponent animCom = engine.createComponent(AnimationComponent.class);
        StateComponent stateComponent = engine.createComponent(StateComponent.class);
        // set data
        b2body.body = bodyFactory.makeAsteroidBody(posx,posy,1, BodyFactory.FIXTURE_TYPE.STONE,
                BodyDef.BodyType.DynamicBody, true);

        position.position.set(posx,posy,1);
        Animation anim = new Animation(0.1f, asteroidAt.findRegions("asteroidblow"));
        animCom.animations.put(0,anim);
        stateComponent.set(StateComponent.ASTEROID_ALIVE);
        String region = (new Random().nextFloat() > 0.5) ? "asteroidOne" : "asteroidTwo";
        texture.region = asteroidAt.findRegion(region);
        texture.texture = assetController.manager.get(assetController.asteroid);
        type.type = TypeComponent.ENEMY;
        b2body.body.setUserData(entity);
        asteroid.intialPos.x = posx;
        asteroid.intialPos.y = posy;
        position.scale.x = 1f;
        position.scale.y = 1f;
        asteroid.speed = getLaunchSpeed(posx, posy);
//        b2body.body.setAwake(true);

        entity.add(stateComponent);
        entity.add(animCom);
        entity.add(b2body);
        entity.add(position);
        entity.add(texture);
        entity.add(asteroid);
        entity.add(type);
        entity.add(collision);

        engine.addEntity(entity);
        numAsteroids += 1;

    }

    public void removeStar() {
        this.stars -= 1;
    }
    public void removeAsteroid() {
        this.numAsteroids -= 1;
    }

    public void createAsteroid(Vector2 location) {
        replaceAsteroid();
        if (engine.getEntitiesFor(Family.all(AsteroidComponent.class).get()).size() < 12) {
            createAsteroid(getAsteroidSpawn().x,getAsteroidSpawn().y);
        }
        this.level = this.parent.getHud().getScore();
        if (this.level % 7 == 0 && this.level > 0 && this.stars < 1) {
            createStar();
            this.level = 0;
        }
    }

    public void replaceAsteroid() {
        if (engine.getEntitiesFor(Family.all(AsteroidComponent.class).get()).size()  < 12) {
            createAsteroid(getAsteroidSpawn().x, getAsteroidSpawn().y);
        }

    }

    private void createPlayer() {

        float posx = 15;
        float posy = 10;
        // create the entity and all the components in it
        player = engine.createEntity();
        AnimationComponent animComp = engine.createComponent(AnimationComponent.class);
        B2BodyComponent b2BodyComponent = engine.createComponent(B2BodyComponent.class);
        TransformComponent transformComponent = engine.createComponent(TransformComponent.class);
        TextureComponent textureComponent = engine.createComponent(TextureComponent.class);
        PlayerComponent playerComponent = engine.createComponent(PlayerComponent.class);
        CollisionComponent collisionComponent = engine.createComponent(CollisionComponent.class);
        TypeComponent typeComponent = engine.createComponent(TypeComponent.class);
        StateComponent stateComponent = engine.createComponent(StateComponent.class);
//
        // create the data for the components
        b2BodyComponent.body = bodyFactory.makeBoxPolyBody(posx,posy,1,2,
                BodyFactory.FIXTURE_TYPE.STEEL, BodyDef.BodyType.DynamicBody,true);

        // set object pos
        transformComponent.position.set(posx,posy,0);
        transformComponent.scale.x = 2f;
        transformComponent.scale.y = 2f;
        textureComponent.texture = assetController.manager.get(assetController.redShip);
        Animation anim = new Animation(0.1f, shipAt.findRegions("redShip"));
        anim.setPlayMode(Animation.PlayMode.LOOP);
        animComp.animations.put(0,anim);
        textureComponent.region = shipAt.findRegion("redShip");
        typeComponent.type = TypeComponent.PLAYER;
        stateComponent.set(StateComponent.STATE_NORMAL);
        b2BodyComponent.body.setUserData(player);
        playerComponent.cam = parent.getGamecam();

        // add components to entity
        player.add(animComp);
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


        float dist = (float)Math.sqrt(velX*velX+velY*velY);

        if (dist != 0) {
            velX = velX/dist;
            velY = velY/dist;
        }

        Vector2 launchSpeed = new Vector2(speed*velX, speed*velY);

        return launchSpeed;
    }

    public Vector2 getAsteroidSpawn() {
        return getObjectSpawn();

    }

    public Vector2 getObjectSpawn(){
        Random ran = new Random();

        float xmin = -5,
                xmax = this.screenXMax+5,
                yTopFinal = this.screenYMax+5,
                yBotFinal = -5;
        float xLoc = xmin + ran.nextFloat() * (xmax-xmin);
        float yLoc;

        if (xLoc > 0 && xLoc < screenXMax) {
            yLoc = (ran.nextFloat() > 0.5f) ? yTopFinal : yBotFinal;
        } else {
            yLoc = yBotFinal + ran.nextFloat() * (yTopFinal - yBotFinal);
        }
        return new Vector2(xLoc,yLoc);
    }

    public Vector2 getStarSpawn() {
        return getObjectSpawn();
    }



}
