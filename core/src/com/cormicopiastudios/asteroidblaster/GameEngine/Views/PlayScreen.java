package com.cormicopiastudios.asteroidblaster.GameEngine.Views;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.cormicopiastudios.asteroidblaster.GameEngine.Controllers.AssetController;
import com.cormicopiastudios.asteroidblaster.GameEngine.Controllers.B2ContactListener;
import com.cormicopiastudios.asteroidblaster.GameEngine.Controllers.InputController;
import com.cormicopiastudios.asteroidblaster.GameEngine.Factories.LevelFactory;
import com.cormicopiastudios.asteroidblaster.GameEngine.Factories.BodyFactory;
import com.cormicopiastudios.asteroidblaster.GameEngine.GameMaster;
import com.cormicopiastudios.asteroidblaster.GameEngine.Systems.AnimationSystem;
import com.cormicopiastudios.asteroidblaster.GameEngine.Systems.AsteroidSystem;
import com.cormicopiastudios.asteroidblaster.GameEngine.Systems.BulletSystem;
import com.cormicopiastudios.asteroidblaster.GameEngine.Systems.CollisionSystem;
import com.cormicopiastudios.asteroidblaster.GameEngine.Systems.PhysicsDebugSystem;
import com.cormicopiastudios.asteroidblaster.GameEngine.Systems.PhysicsSystem;
import com.cormicopiastudios.asteroidblaster.GameEngine.Systems.PlayerControlSystem;
import com.cormicopiastudios.asteroidblaster.GameEngine.Systems.RenderingSystem;
import com.cormicopiastudios.asteroidblaster.GameEngine.Systems.StarSystem;

public class PlayScreen implements Screen {

    private GameMaster gameMaster;
    private World world;
    private BodyFactory bodyFactory;
    private PooledEngine engine;

    // camera
    private OrthographicCamera gamecam;

    private InputController inputController;
    private AssetController assetController;
    private LevelFactory levelFactory;


    private Hud hud;


    private SpriteBatch batch;


    public PlayScreen(GameMaster gameMaster) {
        this.gameMaster = gameMaster;
        // need to add/setup Keyboard Controller
        inputController = new InputController();
        world = new World(new Vector2(0,-0.f),true);
        world.setContactListener(new B2ContactListener(this));
        bodyFactory = BodyFactory.getInstance(world);
        // need to get assetmanager from asteroid blaster
        assetController = gameMaster.getAssetController();

        batch = new SpriteBatch();
        RenderingSystem renderingSystem = new RenderingSystem(batch);
        hud = new Hud(renderingSystem, batch);
        gamecam = renderingSystem.getCamera();
        batch.setProjectionMatrix(gamecam.combined);

        // create pooled engine
        engine = new PooledEngine();

        levelFactory = new LevelFactory(world, (PooledEngine)engine, this);

        // lets add the systems. they run in the order you add them
        engine.addSystem(new AnimationSystem());
        engine.addSystem(renderingSystem);
        engine.addSystem(new PhysicsSystem(world, inputController, (PooledEngine)engine, levelFactory));
        engine.addSystem(new PhysicsDebugSystem(world, renderingSystem.getCamera()));
        engine.addSystem(new CollisionSystem(this));
        AsteroidSystem as = new AsteroidSystem(levelFactory);
        engine.addSystem(as);
        engine.addSystem(new PlayerControlSystem(inputController, levelFactory, engine, hud));
        engine.addSystem(new BulletSystem(levelFactory.getPlayer()));
        engine.addSystem(new StarSystem(levelFactory));


    }

    public AssetController getAssetController() { return this.assetController; }
    public OrthographicCamera getGamecam() { return this.gamecam; }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputController);

    }

    public Hud getHud() { return this.hud; }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        engine.update(delta);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
