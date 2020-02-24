package com.cormicopiastudios.asteroidblaster.GameEngine.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.cormicopiastudios.asteroidblaster.AsteroidBlaster;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.PlayerComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.TextureComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.TransformComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.Components.TypeComponent;
import com.cormicopiastudios.asteroidblaster.GameEngine.GameMaster;
import com.cormicopiastudios.asteroidblaster.GameEngine.Views.Hud;
import com.cormicopiastudios.asteroidblaster.GameEngine.Views.PlayScreen;

import java.util.Comparator;

public class RenderingSystem extends SortedIteratingSystem {


    static final float PPM = 32.f; // Sets the amount of pixels each meter of box2d object contain (will probably need to readjust this after the fact)


    // This gets the height and width of our camera frutsum based off the width and height of the screen and our pixel ration
    static final float FRUTSUM_W = Gdx.graphics.getWidth()/PPM;
    static final float FRUTSUM_H = Gdx.graphics.getHeight()/PPM;

    public static final float PIXELS_TO_METERS = 1.f /PPM; // conversion ratio

    // static way of getting screen dims
    private static Vector2 meterDims = new Vector2();
    private static Vector2 pixelDims = new Vector2();
    public static Vector2 getScreenMeterDim(){
        meterDims.set(Gdx.graphics.getWidth()*PIXELS_TO_METERS,
                Gdx.graphics.getHeight()*PIXELS_TO_METERS);
        return meterDims;
    }

    public static Vector2 getScreenPixelDim() {
        pixelDims.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        return pixelDims;
    }

    // covert pixels to meters
    public static float PixelsToMeters(float pixelValue) {
        return pixelValue * PIXELS_TO_METERS;
    }

    private SpriteBatch batch; // ref
    private Array<Entity> renderQueue; // array used to allow sorting of images allowing us to draw images on top of each other
    private Comparator<Entity> comparator; // comparator to sort images based on the z position of the TransformComponent
    private OrthographicCamera cam; // reference to our gamecam

    // component mappers to get compoents from entities

    private ComponentMapper<TextureComponent> textureM;
    private ComponentMapper<TransformComponent> transformM;


    private Hud hud;
    private PlayScreen parent;


    @SuppressWarnings("unchecked")
    public RenderingSystem(SpriteBatch batch, PlayScreen parent) {
        super(Family.all(TransformComponent.class, TextureComponent.class).get(), new ZComparator());
        this.parent = parent;
        // create component mappers
        textureM = ComponentMapper.getFor(TextureComponent.class);
        transformM = ComponentMapper.getFor(TransformComponent.class);

        comparator = new ZComparator();

        renderQueue = new Array<Entity>();

        this.batch = batch;
        cam = new OrthographicCamera(FRUTSUM_W, FRUTSUM_H);
        cam.position.set(FRUTSUM_W/2.f, FRUTSUM_H/2.f,0);
//        hud = new Hud(this, batch);

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(entity);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // sort on z index
        renderQueue.sort(comparator);

        // update cam and batch
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        batch.enableBlending();
        batch.begin();

        // loop through everything
        for (Entity entity : renderQueue) {
            TextureComponent tex = textureM.get(entity);
            TransformComponent trans = transformM.get(entity);

            if (trans.isHidden) {
                continue;
            }

            float width = tex.region.getRegionWidth();
            float height = tex.region.getRegionHeight();

            float originX = width/2.f;
            float originY = height/2.f;

            if (entity.getComponent(TypeComponent.class).type == TypeComponent.PLAYER &&
            entity.getComponent(PlayerComponent.class).offScreen) {
                TextureRegion arrow = parent.getAssetController().manager.get(parent.getAssetController().arrowPix, TextureAtlas.class).findRegion("Arrow");
                if (trans.position.y > 25) {
                    // to far up
                    batch.draw(arrow,
                            trans.position.x - originX, 8,
                            originX, 16,
                            width, 32,
                            PixelsToMeters(trans.scale.x), PixelsToMeters(trans.scale.y),
                            trans.rotation-180);
                } else if (trans.position.x > 30) {
                    // to far right
                    batch.draw(arrow,
                            30 - originX, trans.position.y - originY,
                            originX, originY,
                            width, 32,
                            PixelsToMeters(trans.scale.x), PixelsToMeters(trans.scale.y),
                            trans.rotation-180);
                } else if (trans.position.x < 0) {
                    // to far left
                    batch.draw(arrow,
                            -16, trans.position.y - originY,
                            originX, originY,
                            width, 32,
                            PixelsToMeters(trans.scale.x), PixelsToMeters(trans.scale.y),
                            trans.rotation-180);
                } else if (trans.position.y < 0) {
                    // to far down
                    batch.draw(arrow,
                            trans.position.x - originX, -32,
                            originX, originY,
                            width, 32,
                            PixelsToMeters(trans.scale.x), PixelsToMeters(trans.scale.y),
                            trans.rotation-180);
                }


            } else {

                batch.draw(tex.region,
                        trans.position.x - originX, trans.position.y - originY,
                        originX, originY,
                        width, height,
                        PixelsToMeters(trans.scale.x), PixelsToMeters(trans.scale.y),
                        trans.rotation);
            }
        }

        batch.end();
//        hud.stage.draw();

        renderQueue.clear();
    }

    // convenience method to get camera
    public OrthographicCamera getCamera() {
        return cam;
    }
}
