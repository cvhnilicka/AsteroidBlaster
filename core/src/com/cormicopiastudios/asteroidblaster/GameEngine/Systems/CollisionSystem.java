package com.cormicopiastudios.asteroidblaster.GameEngine.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.AsteroidComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.B2BodyComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.BulletComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.CollisionComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.PlayerComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.StarComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.StateComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.TransformComponent;
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
                            case TypeComponent.SHOOTINGSTAR:
                                Gdx.app.log("Collision System", "Player Hit star");
                                if (entity.getComponent(PlayerComponent.class).numStars < 3)
                                    entity.getComponent(PlayerComponent.class).numStars += 1;
                                collidedEntity.getComponent(StarComponent.class).isDead = true;
                    }
                    cc.collisionEntity = null; // reset
                }
            }
        } else if (thisType.type == TypeComponent.ENEMY) {
//            Gdx.app.log("CollisionSystem", "ENEMYYYYY");
            if(collidedEntity != null){
                AsteroidComponent asteroid = ComponentMapper.getFor(AsteroidComponent.class).get(entity);
                TypeComponent type = collidedEntity.getComponent(TypeComponent.class);
                if(type != null){
                    switch(type.type){
                        case TypeComponent.PLAYER:
                            System.out.println("enemy hit player");
                            break;
                        case TypeComponent.ENEMY:
                            System.out.println("enemy hit enemy");
                            B2BodyComponent body = ComponentMapper.getFor(B2BodyComponent.class).get(entity);
                            Vector2 vel = body.body.getLinearVelocity();
//                            asteroid.speed = new Vector2(vel.x*-1, vel.y*-1);
                            asteroid.speed = calculateReflection(collidedEntity,entity);
                            collidedEntity.getComponent(AsteroidComponent.class).speed = calculateReflection(entity,collidedEntity);

                            break;
                        case TypeComponent.SCENERY:
                            System.out.println("enemy hit scenery");
                            break;
                        case TypeComponent.OTHER:
                            System.out.println("enemy hit other");
                            break;
                        case TypeComponent.BULLET:
//                            asteroid.isDead = true;
                            StateComponent asteroidState = ComponentMapper.getFor(StateComponent.class).get(entity);
                            asteroidState.set(StateComponent.ASTEROID_DEAD);
                            collidedEntity.getComponent(BulletComponent.class).isDead = true;
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

    private Vector2 calculateReflection(Entity collider, Entity collidedW) {

        TransformComponent colliderTrans = ComponentMapper.getFor(TransformComponent.class).get(collider);
        AsteroidComponent colliderAsteroid = ComponentMapper.getFor(AsteroidComponent.class).get(collider);

        TransformComponent collidedWTrans = ComponentMapper.getFor(TransformComponent.class).get(collidedW);
        AsteroidComponent collidedWAsteroid = ComponentMapper.getFor(AsteroidComponent.class).get(collidedW);

        Vector2 normal = new Vector2(-(collidedWTrans.position.y - colliderTrans.position.y),
                collidedWTrans.position.x - colliderTrans.position.x);

        float d = (float)Math.sqrt(normal.x*normal.x+normal.y*normal.y);
        normal = new Vector2(normal.x/d,normal.y/d);
        double velDot = Vector2.dot(normal.x,normal.y,colliderAsteroid.speed.x,
                colliderAsteroid.speed.y);

        Vector2 reflectionVelocity = new Vector2(colliderAsteroid.speed.x-2*(float)velDot*normal.x,
                colliderAsteroid.speed.y-2*(float)velDot*normal.y);

        return reflectionVelocity;
    }
}
