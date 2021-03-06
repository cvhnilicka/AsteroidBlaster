package com.cormicopiastudios.asteroidblaster;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.cormicopiastudios.asteroidblaster.GameEngine.Controllers.AssetController;
import com.cormicopiastudios.asteroidblaster.GameEngine.GameMaster;
import com.cormicopiastudios.asteroidblaster.GameEngine.Views.GameOver;
import com.cormicopiastudios.asteroidblaster.Menus.Instructions;
import com.cormicopiastudios.asteroidblaster.Menus.LoadingScreen;
import com.cormicopiastudios.asteroidblaster.Menus.MainMenu;

public class AsteroidBlaster extends Game {


	public final boolean debugmode = true;

	private LoadingScreen loadingScreen;
	public final static int LOADING = 1;

	private MainMenu mainMenu;
	public final static int MAINMENU = 2;

	private GameMaster gameMaster;
	public final static int GAME = 3;

	private GameOver gameOver;
	public final static int GAMEOVER = 4;

	private Instructions instructions;
	public final static int INSTRUCTIONS = 5;

	private AssetController assetController;


	public void changeScreen(int screen) {
		switch (screen) {
			// need to add cases
			case LOADING: if (loadingScreen == null) loadingScreen = new LoadingScreen(this);
			this.setScreen(loadingScreen);
			break;
			case MAINMENU: if (mainMenu == null) mainMenu = new MainMenu(this);
			this.setScreen(mainMenu);
			break;
			case GAME: gameMaster = new GameMaster(this);
			break;
			case INSTRUCTIONS: instructions = new Instructions(this);
			this.setScreen(instructions);
			break;
		}
	}

	public void gameOverScreen(int score) {
		gameOver = new GameOver(score, assetController, this);
		this.setScreen(gameOver);
	}


	
	@Override
	public void create () {
		Gdx.graphics.setWindowedMode(960,800);
		assetController = new AssetController();
		// need to add asset manager/load assets
		assetController.queueAddImages();
		assetController.queueMenuButtons();
		assetController.manager.finishLoading();
//		this.background = assetController.manager.get()
		// set screen to loading screen
		changeScreen(LOADING);
	}

	@Override
	public void render () {
		super.render();

	}
	
	@Override
	public void dispose () {

	}

	public AssetController getAssetController() { return this.assetController; }

}
