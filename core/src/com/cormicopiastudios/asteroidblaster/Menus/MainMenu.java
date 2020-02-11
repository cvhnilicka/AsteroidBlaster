package com.cormicopiastudios.asteroidblaster.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.cormicopiastudios.asteroidblaster.AsteroidBlaster;

public class MainMenu implements Screen {
    private AsteroidBlaster parent;
    private Stage stage;
    private Skin skin; // will temp use a skin

    public MainMenu(AsteroidBlaster parent) {
        this.parent = parent;
        stage = new Stage(new ScreenViewport());
    }

    @Override
    public void show() {
        stage.clear();
        // set as input
        Gdx.input.setInputProcessor(stage);

        // Set the stage up

        Table table = new Table(); // table for menu
        table.setFillParent(true);
        if (parent.debugmode == true)
            table.setDebug(true);

        stage.addActor(table);

        skin = new Skin(Gdx.files.internal("skin/shade/uiskin.json"));

        TextButton newGame = new TextButton("New Game", skin);
        TextButton preferences = new TextButton("Preferences", skin);
        TextButton exit = new TextButton("Exit", skin);

        table.add(newGame).height(Value.percentHeight(0.20f, table)).fillX().uniform();
        table.row().pad(10,0,10,0);
        table.add(preferences).height(Value.percentHeight(0.20f, table)).fillX().uniform();
        table.row().pad(10,0,10,0);
        table.add(exit).height(Value.percentHeight(0.20f, table)).fillX().uniform();

        newGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
//                parent.changeScreen(AsteroidBlaster.GAME);
            }
        });

        preferences.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
//                parent.changeScreen(AsteroidBlaster.PREFERENCES);
            }
        });

        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
//                parent.changeScreen(AsteroidBlaster.EXIT);
            }
        });



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
