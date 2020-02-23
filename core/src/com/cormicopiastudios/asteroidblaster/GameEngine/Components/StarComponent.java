package com.cormicopiastudios.asteroidblaster.GameEngine.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class StarComponent implements Component, Pool.Poolable {
    public boolean isDead = false;
    public float xVel = 0;
    public  float yVel = 0;
    public Vector2 velocity = new Vector2();
    public Vector2 startingPosition = new Vector2();
    @Override
    public void reset() {
        isDead = false;
        xVel = 0;
        yVel = 0;
    }
}
