package com.cormicopiastudios.asteroidblaster.GameEngine.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Pool;

public class AnimationComponent implements Component, Pool.Poolable {
    // Store animation information
    public IntMap<Animation> animations = new IntMap<Animation>();


    @Override
    public void reset() {
        animations = new IntMap<Animation>();
    }
}
