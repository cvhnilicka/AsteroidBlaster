package com.cormicopiastudios.asteroidblaster.GameEngine.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.AnimationComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.AsteroidComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.StateComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.TextureComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.TypeComponent;

public class AnimationSystem extends IteratingSystem {

    ComponentMapper<TextureComponent> tm;
    ComponentMapper<AnimationComponent> am;
    ComponentMapper<StateComponent> sm;

    @SuppressWarnings("unchecked")
    public AnimationSystem(){
        super(Family.all(TextureComponent.class,AnimationComponent.class,
                StateComponent.class).get());

        tm = ComponentMapper.getFor(TextureComponent.class);
        am = ComponentMapper.getFor(AnimationComponent.class);
        sm = ComponentMapper.getFor(StateComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AnimationComponent ani = am.get(entity);
        StateComponent state = sm.get(entity);
        TextureComponent tex = tm.get(entity);
        if (entity.getComponent(TypeComponent.class).type == TypeComponent.PLAYER) {
            if (ani.animations.size > 0) {
                tex.region = (TextureRegion) ani.animations.get(0).getKeyFrame(state.time, state.isLooping);
            }
        } else if (entity.getComponent(TypeComponent.class).type == TypeComponent.ENEMY) {
            if (ani.animations.size > 0 && state.get() == StateComponent.ASTEROID_DEAD) {
                tex.region = (TextureRegion) ani.animations.get(0).getKeyFrame(state.time, state.isLooping);
            }
            if (ani.animations.get(0).isAnimationFinished(state.time) && state.get() == StateComponent.ASTEROID_DEAD) {
                ComponentMapper.getFor(AsteroidComponent.class).get(entity).isDead = true;
            }
        }
        state.time += deltaTime;

    }

}
