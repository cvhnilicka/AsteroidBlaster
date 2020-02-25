package com.cormicopiastudios.asteroidblaster.GameEngine.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cormicopiastudios.asteroidblaster.GameEngine.Systems.RenderingSystem;

public class Hud implements Disposable {

    public Stage stage;
    private Viewport viewport;

    private RenderingSystem rs;

    private int score;
    private int stars;
    private int health;

    Label scoreLabel;
    Label starLabel;
    Label healthLabel;

    Label scoreHolderLabel;
    Label starHolderLabel;
    Label healthHolderLabel;

    public Hud(RenderingSystem rs, SpriteBatch sb) {
        this.rs = rs;
        score = 0;
        health = 10;
        stars = 0;
        viewport = new FitViewport(rs.getCamera().viewportWidth, rs.getCamera().viewportHeight,
                new OrthographicCamera());

        stage = new Stage(viewport, sb);
        stage.setDebugAll(true);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        healthLabel = new Label("HEALTH: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        healthLabel.setFontScale(0.1f);
        healthHolderLabel = new Label(String.valueOf(health), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        healthHolderLabel.setFontScale(0.1f);


        scoreLabel = new Label("SCORE: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel.setFontScale(.1f);
        scoreHolderLabel = new Label(String.valueOf(score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreHolderLabel.setFontScale(.1f);


        starHolderLabel = new Label(String.valueOf(stars), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        starHolderLabel.setFontScale(.1f);
        starLabel = new Label("STARS: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        starLabel.setFontScale(.1f);
//        scoreLabel.
        table.add(healthLabel).expandX();
        table.add(healthHolderLabel).expandX();
        table.add(scoreLabel).expandX();
        table.add(scoreHolderLabel).expandX();
        table.add(starLabel).expandX();
        table.add(starHolderLabel).expandX();
        table.row();
        stage.addActor(table);

    }

//    public void update(float dt) {
//
//    }


    public void setHealth(int health) {
        this.health = health;
    }

    public int getScore() {
        return this.score;
    }

    public void addScore() {
        this.score += 1;
//        scoreHolderLabel.setText(String.format("%03d", score));
        scoreHolderLabel.setText(score);
    }

    public void updateStars(int num) {
        this.stars = num;
//        starHolderLabel.setText(String.format("%03d", stars));
        this.starHolderLabel.setText(stars);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
