package com.cormicopiastudios.asteroidblaster.GameEngine.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameOver implements Screen {

    private Stage stage;
    private Skin skin;


    public GameOver(int score) {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("skin/shade/uiskin.json"));
        Gdx.app.log("Game Over screen", String.valueOf(score));
        
    }
    @Override
    public void show() {

        stage.clear();
        Gdx.input.setInputProcessor(stage);
        Label gameOver = new Label("Game Over", skin);

        Table table = new Table();
        table.setFillParent(true);
        table.add(gameOver);
        table.row();

        stage.addActor(table);


    }

    @Override
    public void render(float delta) {
        // first, we should clear the image before drawing the next one,
        // this is to prevent 'flickering' or 'ghosting'
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Actually render the scene you described in the show() method above.
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
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
