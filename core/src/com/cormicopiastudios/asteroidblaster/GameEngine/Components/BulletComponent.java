package com.cormicopiastudios.asteroidblaster.GameEngine.Components;

import com.badlogic.ashley.core.Component;

public class BulletComponent implements Component {
    public float xVel = 0.f;
    public float yVel = 0.f;
    public boolean isDead = false;
}
