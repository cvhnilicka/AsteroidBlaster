package com.cormicopiastudios.asteroidblaster.GameEngine.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class AsteroidComponent implements Component {
    // asteroid component information
    public Vector2 speed = new Vector2();
    public final Vector2 intialPos = new Vector2();
    public boolean isDead = false;
}
