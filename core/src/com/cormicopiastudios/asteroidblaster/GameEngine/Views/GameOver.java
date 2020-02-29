package com.cormicopiastudios.asteroidblaster.GameEngine.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.cormicopiastudios.asteroidblaster.AsteroidBlaster;
import com.cormicopiastudios.asteroidblaster.GameEngine.Controllers.AssetController;

import java.util.Collections;
import java.util.LinkedList;

public class GameOver implements Screen {

    private Stage stage;
    private Skin skin;

    private int score;
    Image[] scoreTextures;
    private AssetController assetController;
    private TextureAtlas btnAtlas;
    private TextureAtlas backgroundAtlas;
    private Animation backgroundAnim;
    float stateTime;
    SpriteBatch spriteBatch;
    private AsteroidBlaster parent;

    public GameOver(int score, AssetController assetController, AsteroidBlaster parent) {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("skin/shade/uiskin.json"));
        this.assetController = assetController;
        btnAtlas = assetController.manager.get(assetController.buttonsPix, TextureAtlas.class);
        backgroundAtlas = assetController.manager.get(assetController.backgroundPix, TextureAtlas.class);
        scoreSetup(score);
        this.parent = parent;
        this.backgroundAnim = new Animation(0.1f, backgroundAtlas.findRegions("Background"));
        backgroundAnim.setPlayMode(Animation.PlayMode.LOOP);
        Gdx.app.log("Game Over screen", String.valueOf(score));
        
    }
    @Override
    public void show() {
        stateTime = 0f;
        spriteBatch = new SpriteBatch();
        stage.clear();
        Gdx.input.setInputProcessor(stage);
        scoreSetup(this.score);

        Image gameOverImage = new Image();
        TextureRegionDrawable dr = new TextureRegionDrawable(btnAtlas.findRegion("GameOver"));
        dr.setMinSize(150,75);
        gameOverImage.setDrawable(dr);


        TextureRegionDrawable ret = new TextureRegionDrawable(btnAtlas.findRegion("return"));
        ret.setMinSize(150,75);
        ImageButton returnBtn = new ImageButton(ret);

        Table table = new Table();
        Table scoreTable = new Table();

        scoreTable.add(scoreTextures[3]).uniform();
        scoreTable.add(scoreTextures[2]).uniform();
        scoreTable.add(scoreTextures[1]).uniform();
        scoreTable.add(scoreTextures[0]).uniform();



        table.setDebug(true);
        table.setFillParent(true);
        table.add(gameOverImage);
        table.row();
        table.add(scoreTable).padTop(10);
        table.row();
        table.add(returnBtn);
        table.row();

        stage.addActor(table);

        returnBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(AsteroidBlaster.MAINMENU);
            }
        });


    }

    private void scoreSetup(int score) {
        scoreTextures = new Image[4];

        for (int j = 0; j < scoreTextures.length; j ++) {
            TextureRegionDrawable dr = new TextureRegionDrawable(btnAtlas.findRegion("zero"));
            dr.setMinSize(25,25);
            scoreTextures[j] = new Image(dr);
        }


        this.score = score;
        LinkedList<Integer> digits = new LinkedList<>();
        while(score > 0) {
            digits.add(score%10);
            score/=10;
        }

        int pad = 4-digits.size();
        Collections.reverse(digits);

        int i = 4-pad;
        while(!digits.isEmpty()) {
            TextureRegionDrawable dr = new TextureRegionDrawable();
            switch (digits.pop()) {
                case 1: dr.setRegion(btnAtlas.findRegion("one"));
                    break;
                case 2: dr.setRegion(btnAtlas.findRegion("two"));
                    break;
                case 3: dr.setRegion(btnAtlas.findRegion("three"));
                    break;
                case 4: dr.setRegion(btnAtlas.findRegion("four"));
                    break;
                case 5: dr.setRegion(btnAtlas.findRegion("five"));
                    break;
                case 6: dr.setRegion(btnAtlas.findRegion("six"));
                    break;
                case 7: dr.setRegion(btnAtlas.findRegion("seven"));
                    break;
                case 8: dr.setRegion(btnAtlas.findRegion("eight"));
                    break;
                case 9: dr.setRegion(btnAtlas.findRegion("nine"));
                    break;
                case 0: dr.setRegion(btnAtlas.findRegion("zero"));
                    break;
                    default: dr.setRegion(btnAtlas.findRegion("zero"));
            }
            dr.setMinSize(25,25);
            scoreTextures[i-1].setDrawable(dr);
            i--;
        }

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
