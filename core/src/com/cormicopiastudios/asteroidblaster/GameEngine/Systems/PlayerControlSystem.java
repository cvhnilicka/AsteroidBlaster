package com.cormicopiastudios.asteroidblaster.GameEngine.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.B2BodyComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.PlayerComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.StateComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Controllers.InputController;

public class PlayerControlSystem extends IteratingSystem {

    ComponentMapper<PlayerComponent> pm;
    ComponentMapper<B2BodyComponent> b2m;
    ComponentMapper<StateComponent> sm;

    InputController controller;

    @SuppressWarnings("unchecked")
    public PlayerControlSystem(InputController controller) {
        super(Family.all(PlayerComponent.class).get());
        this.controller = controller;
        pm = ComponentMapper.getFor(PlayerComponent.class);
        b2m = ComponentMapper.getFor(B2BodyComponent.class);
        sm = ComponentMapper.getFor(StateComponent.class);
    }
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        B2BodyComponent b2body = b2m.get(entity);
        StateComponent state = sm.get(entity);

        if(b2body.body.getLinearVelocity().y > 0){
            state.set(StateComponent.STATE_FALLING);
        }

        if(b2body.body.getLinearVelocity().y == 0){
            if(state.get() == StateComponent.STATE_FALLING){
                state.set(StateComponent.STATE_NORMAL);
            }
            if(b2body.body.getLinearVelocity().x != 0){
                state.set(StateComponent.STATE_MOVING);
            }
        }

        if(controller.left){
            b2body.body.setLinearVelocity(MathUtils.lerp(b2body.body.getLinearVelocity().x, -1f, 0.2f),b2body.body.getLinearVelocity().y);
        }
        if(controller.right){
            b2body.body.setLinearVelocity(MathUtils.lerp(b2body.body.getLinearVelocity().x, 1f, 0.2f),b2body.body.getLinearVelocity().y);
        }

        if(!controller.left && ! controller.right){
            b2body.body.setLinearVelocity(MathUtils.lerp(b2body.body.getLinearVelocity().x, 0, 0.1f),b2body.body.getLinearVelocity().y);
        }

        if(controller.up &&
                (state.get() == StateComponent.STATE_NORMAL || state.get() == StateComponent.STATE_MOVING)){
            //b2body.body.applyForceToCenter(0, 3000,true);
            b2body.body.setLinearVelocity(b2body.body.getLinearVelocity().x,MathUtils.lerp(b2body.body.getLinearVelocity().y, 1, 0.1f));
            state.set(StateComponent.STATE_JUMPING);
        }
    }
}
