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
    public final String flameAsteroid = "flaming_asteroid.png";
    public final String bullet = "bulletv1.png";

    // texturepacks
    public final String redShipPix = "redship/redship.atlas";
    public final String asteroidPix = "asteroids/asteroid.atlas";
    public final String bulletPix  = "bullet/bullet.atlas";
    public final String starPix = "star/star.atlas";
    public final String newGamePix = "buttons/newgame.atlas";
    public final String arrowPix = "arrow/arrow.atlas";
    public final String backgroundPix = "background/background.atlas";
    public final String buttonsPix = "buttons/buttons.atlas";
    public final String instructionsPix = "instructions/instructions.atlas";



    public void queueAddImages() {
        manager.load(player, Texture.class);
        manager.load(bullet, Texture.class);
        manager.load(asteroid, Texture.class);
        manager.load(leftWall, Texture.class);
        manager.load(rightWall, Texture.class);
        manager.load(flameAsteroid, Texture.class);



    }

    public void queueMenuButtons(){


        manager.load(whiteShip, Texture.class);
        manager.load(redShip, Texture.class);
        manager.load(redShipPix, TextureAtlas.class);
        manager.load(asteroidPix, TextureAtlas.class);
        manager.load(bulletPix, TextureAtlas.class);
        manager.load(starPix, TextureAtlas.class);
        manager.load(newGamePix, TextureAtlas.class);
        manager.load(arrowPix, TextureAtlas.class);
        manager.load(backgroundPix, TextureAtlas.class);
        manager.load(buttonsPix, TextureAtlas.class);
        manager.load(instructionsPix, TextureAtlas.class);




    }

}
