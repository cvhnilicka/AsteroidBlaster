package com.cormicopiastudios.asteroidblaster.GameEngine;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cormicopiastudios.asteroidblaster.AsteroidBlaster;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.AsteroidComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Controllers.AssetController;
import com.cormicopiastudios.asteroidblaster.GameEngine.Views.PlayScreen;

public class GameMaster extends Game {

    private AsteroidBlaster parent;

    public final static int V_WIDTH = 48;
    public final static int V_HIEGHT = 48;
//    public final static int V_WIDTH = 128;
//    public final static int V_HIEGHT = 128;

    private AssetController assetController;


    // collision bits
    public static final short PLATFORM_BIT = 1;
    public static final short PLAYER_BIT = 2;
    public static final short ASTEROID_BIT = 4;

    public GameMaster(AsteroidBlaster parent) {
        this.parent = parent;
        assetController = parent.getAssetController();
        parent.setScreen(new PlayScreen(this));
    }

    public AssetController getAssetController() {
        return assetController;
    }

    public void gameOver(int score) {
        this.parent.gameOverScreen(score);
    }

    @Override
    public void create() {

    }
}
