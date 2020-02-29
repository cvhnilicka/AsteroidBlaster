package com.cormicopiastudios.asteroidblaster.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.cormicopiastudios.asteroidblaster.AsteroidBlaster;

public class Instructions implements Screen {
    private AsteroidBlaster parent;
    private Stage stage;

    private TextureAtlas atlas;
    private TextureAtlas backgroundAtlas;
    private TextureAtlas btnAtlas;

    SpriteBatch spriteBatch;
    float stateTime;

    private Animation backgroundAnim;


    public Instructions(AsteroidBlaster parent) {
        this.parent = parent;
        stage = new Stage(new ScreenViewport());
        this.atlas = this.parent.getAssetController().manager.get(parent.getAssetController().instructionsPix, TextureAtlas.class);
        this.backgroundAtlas = parent.getAssetController().manager.get(
                parent.getAssetController().backgroundPix, TextureAtlas.class);
        this.btnAtlas = this.parent.getAssetController().manager.get(this.parent.getAssetController().buttonsPix, TextureAtlas.class);

        this.backgroundAnim = new Animation(0.1f, backgroundAtlas.findRegions("Background"));
        backgroundAnim.setPlayMode(Animation.PlayMode.LOOP);
    }

    @Override
    public void show() {

        stage.clear();
        // set as input
        spriteBatch = new SpriteBatch();
        stateTime = 0.f;
        Gdx.input.setInputProcessor(stage);


        // Set the stage up

        Table table = new Table(); // table for menu
        table.setFillParent(true);

        stage.addActor(table);

        TextureRegionDrawable instDr = new TextureRegionDrawable(atlas.findRegion("instructionsScreen"));
        instDr.setMinSize(650,650);
        Image instImag = new Image(instDr);

        table.add(instImag);

        TextureRegionDrawable ret = new TextureRegionDrawable(btnAtlas.findRegion("return"));
        ret.setMinSize(150,75);
        ImageButton returnBtn = new ImageButton(ret);

        table.row();
        table.add(returnBtn);


        returnBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(AsteroidBlaster.MAINMENU);
            }
        });

    }

    @Override
    public void render(float delta) {
        // first, we should clear the image before drawing the next one,
        // this is to prevent 'flickering' or 'ghosting'
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        TextureRegion toDraw = (TextureRegion)backgroundAnim.getKeyFrame(stateTime, true);


        stateTime+=delta;
        spriteBatch.begin();

        spriteBatch.draw(toDraw,0,0,stage.getWidth(),stage.getHeight());
        spriteBatch.end();
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
