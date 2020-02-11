package com.cormicopiastudios.asteroidblaster.Menus;

import com.badlogic.gdx.Screen;
import com.cormicopiastudios.asteroidblaster.AsteroidBlaster;

public class LoadingScreen implements Screen {


    private AsteroidBlaster parent;


    public LoadingScreen(AsteroidBlaster parent) {
        this.parent = parent;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        this.parent.changeScreen(AsteroidBlaster.MAINMENU);
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
