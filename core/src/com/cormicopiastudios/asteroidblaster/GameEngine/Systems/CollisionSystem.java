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
import com.cormicopiastudios.asteroidblaster.GameEngine.Views.PlayScreen;

public class CollisionSystem extends IteratingSystem {

    // component mappers
    ComponentMapper<CollisionComponent> cm;
    ComponentMapper<PlayerComponent> pm;

    private PlayScreen parent;

    @SuppressWarnings("unchecked")
    public CollisionSystem(PlayScreen parent) {
        // only need to worry about play collision for now
        super(Family.all(CollisionComponent.class).get());
        this.parent = parent;
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
        // do player collisions
        if (thisType.type == TypeComponent.PLAYER) {
            if (collidedEntity != null) {
                TypeComponent type = collidedEntity.getComponent(TypeComponent.class);
                if (type != null) {
                    switch (type.type) {
                        case TypeComponent.ENEMY:
                            entity.getComponent(B2BodyComponent.class).body.applyLinearImpulse(calculateBounce(entity, collidedEntity),
                                    entity.getComponent(B2BodyComponent.class).body.getWorldCenter(), true);
                            entity.getComponent(PlayerComponent.class).health -= 1;
                            this.parent.getHud().setHealth(entity.getComponent(PlayerComponent.class).health);
                            if (entity.getComponent(PlayerComponent.class).health <= 0) {
                                this.parent.setGameOver();
                            }
                            break;
                        case TypeComponent.SHOOTINGSTAR:
                            if (entity.getComponent(PlayerComponent.class).numStars < 3) {
                                entity.getComponent(PlayerComponent.class).numStars += 1;
                                this.parent.getHud().updateStars((int) entity.getComponent(PlayerComponent.class).numStars);
                            }
                            collidedEntity.getComponent(StarComponent.class).isDead = true;
                    }
                    cc.collisionEntity = null; // reset
                }
            }
        } else if (thisType.type == TypeComponent.ENEMY) {
            if (collidedEntity != null) {
                AsteroidComponent asteroid = ComponentMapper.getFor(AsteroidComponent.class).get(entity);
                TypeComponent type = collidedEntity.getComponent(TypeComponent.class);
                if (type != null) {
                    switch (type.type) {
                        case TypeComponent.ENEMY:
                            B2BodyComponent body = ComponentMapper.getFor(B2BodyComponent.class).get(entity);
                            Vector2 newSpeedA = calculateReflection(collidedEntity, entity);
                            collidedEntity.getComponent(AsteroidComponent.class).speed = calculateReflection(entity, collidedEntity);
                            asteroid.speed = newSpeedA;
                            break;
                        case TypeComponent.BULLET:
                            StateComponent asteroidState = ComponentMapper.getFor(StateComponent.class).get(entity);
                            asteroidState.set(StateComponent.ASTEROID_DEAD);
                            asteroid.wasShot = true;
                            collidedEntity.getComponent(BulletComponent.class).isDead = true;
                            this.parent.getHud().addScore();
                            break;
                        default:
                            System.out.println("No matching type found");
                    }
                    cc.collisionEntity = null; // collision handled reset component
                } else {
                    System.out.println("type == null");
                }
            }
        } else if (thisType.type == TypeComponent.BULLET) {
            if (collidedEntity != null) {
                TypeComponent collidedType = collidedEntity.getComponent(TypeComponent.class);

                if (collidedType != null) {
                    switch (collidedType.type) {
                        case TypeComponent.ENEMY:
                            Gdx.app.log("Collision", "Bullet w asteroid");
                            StateComponent asteroidState = ComponentMapper.getFor(StateComponent.class).get(collidedEntity);
                            asteroidState.set(StateComponent.ASTEROID_DEAD);
                            collidedEntity.getComponent(AsteroidComponent.class).wasShot = true;
                            entity.getComponent(BulletComponent.class).isDead = true;
                            this.parent.getHud().addScore();
                    }
                }
            }
        }
    }


    private Vector2 calculateBounce(Entity player, Entity asteroid) {

        TransformComponent playerTrans = ComponentMapper.getFor(TransformComponent.class).get(player);
        B2BodyComponent playerBody = ComponentMapper.getFor(B2BodyComponent.class).get(player);

        TransformComponent collidedWTrans = ComponentMapper.getFor(TransformComponent.class).get(asteroid);
        AsteroidComponent collidedWAsteroid = ComponentMapper.getFor(AsteroidComponent.class).get(asteroid);

        Vector2 normal = new Vector2(-(collidedWTrans.position.y - playerTrans.position.y),
                collidedWTrans.position.x - playerTrans.position.x);

        float d = (float) Math.sqrt(normal.x * normal.x + normal.y * normal.y);
        normal = new Vector2(normal.x / d, normal.y / d);
        double velDot = Vector2.dot(normal.x, normal.y, playerBody.body.getLinearVelocity().x,
                playerBody.body.getLinearVelocity().y);

        Vector2 reflectionVelocity = new Vector2(playerBody.body.getLinearVelocity().x - 2 * (float) velDot * normal.x,
                playerBody.body.getLinearVelocity().y - 2 * (float) velDot * normal.y);

        return reflectionVelocity;
    }


    private Vector2 calculateReflection(Entity collider, Entity collidedW) {

        TransformComponent colliderTrans = ComponentMapper.getFor(TransformComponent.class).get(collider);
        AsteroidComponent colliderAsteroid = ComponentMapper.getFor(AsteroidComponent.class).get(collider);

        TransformComponent collidedWTrans = ComponentMapper.getFor(TransformComponent.class).get(collidedW);
        AsteroidComponent collidedWAsteroid = ComponentMapper.getFor(AsteroidComponent.class).get(collidedW);

        Vector2 normal = new Vector2(-(collidedWTrans.position.y - colliderTrans.position.y),
                collidedWTrans.position.x - colliderTrans.position.x);

        float d = (float) Math.sqrt(normal.x * normal.x + normal.y * normal.y);
        normal = new Vector2(normal.x / d, normal.y / d);
        double velDot = Vector2.dot(normal.x, normal.y, colliderAsteroid.speed.x,
                colliderAsteroid.speed.y);

        Vector2 reflectionVelocity = new Vector2(colliderAsteroid.speed.x - 2 * (float) velDot * normal.x,
                colliderAsteroid.speed.y - 2 * (float) velDot * normal.y);

        return reflectionVelocity;
    }
}
