package com.cormicopiastudios.asteroidblaster.GameEngine.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class PlayerComponent implements Component {
    // used to identify player
    public OrthographicCamera cam = null;
    public float timeSinceLastShot = 0.f;
    public float shootDelay = 0.3f;
    public float numStars = 0f;
}
