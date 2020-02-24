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

    Label scoreLabel;
    Label starLabel;

    Label scoreHolderLabel;
    Label starHolderLabel;

    public Hud(RenderingSystem rs, SpriteBatch sb) {
        this.rs = rs;
        score = 0;
        stars = 0;
        viewport = new FitViewport(rs.getCamera().viewportWidth, rs.getCamera().viewportHeight,
                new OrthographicCamera());

        stage = new Stage(viewport, sb);
        stage.setDebugAll(true);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        scoreLabel = new Label("SCORE: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel.setFontScale(.1f);
        starHolderLabel = new Label(String.format("%03d", stars), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        starHolderLabel.setFontScale(.1f);

        scoreHolderLabel = new Label(String.format("%03d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreHolderLabel.setFontScale(.1f);


        starLabel = new Label("STARS: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        starLabel.setFontScale(.1f);
//        scoreLabel.
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

    public void addScore() {
        this.score += 1;
        scoreHolderLabel.setText(String.format("%03d", score));
    }

    public void updateStars(int num) {
        this.stars = num;
        starHolderLabel.setText(String.format("%03d", stars));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
