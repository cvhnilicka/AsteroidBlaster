package com.cormicopiastudios.asteroidblaster.GameEngine.Components;

import com.badlogic.ashley.core.Component;

public class StateComponent implements Component {
    // will contain state information
    public static final int STATE_NORMAL = 0;
    public static final int STATE_JUMPING = 1;
    public static final int STATE_FALLING = 2;
    public static final int STATE_MOVING = 3;
    public static final int STATE_HIT = 4;
    public static final int ASTEROID_DEAD = 5;
    public static final int ASTEROID_ALIVE = 6;

    private int state = 0;
    public float time = 0.0f;
    public boolean isLooping = false;

    public void set(int newState){
        state = newState;
        time = 0.0f;
    }

    public int get(){
        return state;
    }
}
