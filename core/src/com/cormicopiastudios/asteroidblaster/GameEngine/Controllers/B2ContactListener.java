package com.cormicopiastudios.asteroidblaster.GameEngine.Controllers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.CollisionComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Views.PlayScreen;


public class B2ContactListener implements ContactListener {

    private PlayScreen parent;

    public B2ContactListener(PlayScreen parent) {
        this.parent = parent;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        Gdx.app.log("Contact Listener", "CONTACT");

        if (fa.getBody().getUserData() instanceof Entity) {
            Entity ent = (Entity)fa.getBody().getUserData();
            entityCollision(ent, fb);
        } else if (fb.getBody().getUserData() instanceof Entity) {
            Entity ent = (Entity)fb.getBody().getUserData();
            entityCollision(ent, fa);
        }

    }

    private void entityCollision(Entity ent, Fixture fix) {
        if (fix.getBody().getUserData() instanceof Entity) {
            Gdx.app.log("Contact Listener", "ENTITY COLLISION");
            Entity colEnt = (Entity) fix.getBody().getUserData();

            CollisionComponent col = ent.getComponent(CollisionComponent.class);
            CollisionComponent colb = colEnt.getComponent(CollisionComponent.class);

            if(col != null){
                col.collisionEntity = colEnt;
            }else if(colb != null){
                colb.collisionEntity = ent;
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
