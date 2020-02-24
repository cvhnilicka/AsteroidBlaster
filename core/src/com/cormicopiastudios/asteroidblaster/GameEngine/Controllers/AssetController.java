package com.cormicopiastudios.asteroidblaster.GameEngine.Controllers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetController {

    public final AssetManager manager = new AssetManager();


    // player
    public final String player = "whiteShip.png";
    public final String whiteShip = "whiteShip.png";
    public final String redShip = "redShip.png";
    public final String asteroid = "asteroid.png";
    public final String leftWall = "LeftRocks.png";
    public final String rightWall = "RightRocks.png";
    public final String star_background = "star_background1.png";
    public final String plain_background = "background.png";
    public final String flameAsteroid = "flaming_asteroid.png";
    public final String bullet = "bulletv1.png";

    // texturepacks
    public final String redShipPix = "redship/redship.atlas";
    public final String asteroidPix = "asteroids/asteroid.atlas";
    public final String bulletPix  = "bullet/bullet.atlas";
    public final String starPix = "star/star.atlas";
    public final String newGamePix = "buttons/newgame.atlas";
    public final String arrowPix = "arrow/arrow.atlas";


    // numbers
    public final String one = "Numbers/one_norm.png";
    public final String two = "Numbers/two_norm.png";
    public final String three = "Numbers/three_norm.png";
    public final String four = "Numbers/four_norm.png";
    public final String five = "Numbers/five_norm.png";
    public final String six = "Numbers/six_norm.png";
    public final String seven = "Numbers/seven_norm.png";
    public final String eight = "Numbers/eight_norm.png";
    public final String nine = "Numbers/nine_norm.png";
    public final String zero = "Numbers/zero_norm.png";

    // buttons
    public final String newgame = "buttons/newgame.png";
    public final String leaderboard = "buttons/leaderboard.png";
    public final String exit = "buttons/exit.png";
    public final String returnButton = "buttons/return.png";
    public final String preferences = "buttons/preferences.png";


    public void queueAddImages() {
        manager.load(player, Texture.class);
        manager.load(bullet, Texture.class);
        manager.load(asteroid, Texture.class);
        manager.load(leftWall, Texture.class);
        manager.load(rightWall, Texture.class);
        manager.load(flameAsteroid, Texture.class);



    }

    public void queueMenuButtons(){
        manager.load(star_background, Texture.class);
        manager.load(plain_background, Texture.class);
        manager.load(newgame, Texture.class);
        manager.load(leaderboard, Texture.class);
        manager.load(exit, Texture.class);
        manager.load(returnButton, Texture.class);
        manager.load(preferences, Texture.class);


        manager.load(one, Texture.class);
        manager.load(two, Texture.class);
        manager.load(three, Texture.class);
        manager.load(four, Texture.class);
        manager.load(five, Texture.class);
        manager.load(six, Texture.class);
        manager.load(seven, Texture.class);
        manager.load(eight, Texture.class);
        manager.load(nine, Texture.class);
        manager.load(zero, Texture.class);

        manager.load(whiteShip, Texture.class);
        manager.load(redShip, Texture.class);
        manager.load(redShipPix, TextureAtlas.class);
        manager.load(asteroidPix, TextureAtlas.class);
        manager.load(bulletPix, TextureAtlas.class);
        manager.load(starPix, TextureAtlas.class);
        manager.load(newGamePix, TextureAtlas.class);
        manager.load(arrowPix, TextureAtlas.class);



    }

}
