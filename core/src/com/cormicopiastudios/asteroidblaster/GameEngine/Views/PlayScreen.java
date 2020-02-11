package com.cormicopiastudios.asteroidblaster.GameEngine.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cormicopiastudios.asteroidblaster.AsteroidBlaster;
import com.cormicopiastudios.asteroidblaster.GameEngine.Controllers.AssetController;
import com.cormicopiastudios.asteroidblaster.GameEngine.Controllers.InputController;
import com.cormicopiastudios.asteroidblaster.GameEngine.Controllers.ModelController;
import com.cormicopiastudios.asteroidblaster.GameEngine.GameMaster;

public class PlayScreen implements Screen {

    private GameMaster gameMaster;

    // camera
    private OrthographicCamera gamecam;
    private Viewport viewport;
    private Box2DDebugRenderer debugRenderer;

    private InputController inputController;
    private ModelController modelController;
    private AssetController assetController;


    private SpriteBatch batch;


    public PlayScreen(GameMaster gameMaster) {
        this.gameMaster = gameMaster;
        batch = new SpriteBatch();
        gamecam = new OrthographicCamera(GameMaster.V_WIDTH, GameMaster.V_HIEGHT);
        batch.setProjectionMatrix(gamecam.combined);

        // need to add/setup Keyboard Controller
        inputController = new InputController();

        // need to get assetmanager from asteroid blaster
        assetController = gameMaster.getAssetController();
        // need to add/setup b2 model controller
        modelController = new ModelController(this);


        debugRenderer = new Box2DDebugRenderer(true,true,true,true,true,true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputController);

    }

    @Override
    public void render(float delta) {

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
