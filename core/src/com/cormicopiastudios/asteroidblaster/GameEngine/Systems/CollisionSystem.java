package com.cormicopiastudios.asteroidblaster.GameEngine.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.AsteroidComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.CollisionComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.PlayerComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.TypeComponent;

public class CollisionSystem extends IteratingSystem {

    // component mappers
    ComponentMapper<CollisionComponent> cm;
    ComponentMapper<PlayerComponent> pm;

    @SuppressWarnings("unchecked")
    public CollisionSystem() {
        // only need to worry about play collision for now
        super (Family.all(CollisionComponent.class).get());

        cm = ComponentMapper.getFor(CollisionComponent.class);
        pm = ComponentMapper.getFor(PlayerComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        // get player collision comp
        CollisionComponent cc = cm.get(entity);
        // collided entity
        Entity collidedEntity = cc.collisionEntity;

        TypeComponent thisType = entity.getComponent(TypeComponent.class);
//        Gdx.app.log("Collision System", " This type: " + thisType.type);
        // do player collisions
        if (thisType.type == TypeComponent.PLAYER) {
            if (collidedEntity != null) {
                TypeComponent type = collidedEntity.getComponent(TypeComponent.class);
                if (type != null) {
                    switch (type.type) {
                        case TypeComponent.ENEMY:
//                            Gdx.app.log("Collision System", "Collided with enemy type");
                            break;
                        case TypeComponent.SCENERY:
//                            Gdx.app.log("Collision System", "Collided with scenery type");
                            break;
                        case TypeComponent.OTHER:
//                            Gdx.app.log("Collision System", "Collided with other type");
                            break;
                    }
                    cc.collisionEntity = null; // reset
                }
            }
        } else if (thisType.type == TypeComponent.ENEMY) {
//            Gdx.app.log("CollisionSystem", "ENEMYYYYY");
            if(collidedEntity != null){
                TypeComponent type = collidedEntity.getComponent(TypeComponent.class);
                if(type != null){
                    switch(type.type){
                        case TypeComponent.PLAYER:
                            System.out.println("enemy hit player");
                            break;
                        case TypeComponent.ENEMY:
                            System.out.println("enemy hit enemy");
                            break;
                        case TypeComponent.SCENERY:
                            System.out.println("enemy hit scenery");
                            break;
                        case TypeComponent.OTHER:
                            System.out.println("enemy hit other");
                            break;
                        case TypeComponent.BULLET:
                            AsteroidComponent asteroid = ComponentMapper.getFor(AsteroidComponent.class).get(entity);
                            asteroid.isDead = true;
                            System.out.println("enemy got shot");
                        default:
                            System.out.println("No matching type found");
                    }
                    cc.collisionEntity = null; // collision handled reset component
                }else{
                    System.out.println("type == null");
                }
            }
        }




    }
}
