package com.cormicopiastudios.asteroidblaster.GameEngine.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class AsteroidComponent implements Component, Pool.Poolable {
    // asteroid component information
    public Vector2 speed = new Vector2();
    public  Vector2 intialPos = new Vector2();
    public boolean isDead = false;
    public float timeOffScreen = 0.f;

    @Override
    public void reset() {
        speed = new Vector2();
        intialPos = new Vector2();
        isDead = false;
        timeOffScreen = 0.f;
    }

}
