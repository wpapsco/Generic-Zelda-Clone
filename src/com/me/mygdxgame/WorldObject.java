package com.me.mygdxgame;

import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by William on 1/10/14.
 */
public abstract class WorldObject {
    protected boolean isFlammable;
    protected boolean isFlaming;
    protected ParticleEffect flamingEffect;
    protected PointLight flameLight;
    protected Body body;
    protected Sprite sprite;
    public boolean destroyed = false;

    public WorldObject(Body body, boolean isFlammable) {
        this.isFlammable = isFlammable;
        this.body = body;
        create();
        postCreate();
    }

    public WorldObject(Texture texture, Body body, boolean isFlammable) {
        sprite = new Sprite(texture);
        this.body = body;
        this.isFlammable = isFlammable;
        create();
        postCreate();
    }

    public WorldObject(TextureRegion region, Body body, boolean isFlammable) {
        sprite = new Sprite(region);
        this.body = body;
        this.isFlammable = isFlammable;
        create();
        postCreate();
    }

    private void postCreate() {
        body.setUserData(this);
        if (isFlammable) {

            flameLight = new PointLight(Values.handler, 200, Values.fireColor, 30 * Values.PIXEL_BOX, body.getPosition().x * Values.BOX_PIXEL, body.getPosition().y * Values.BOX_PIXEL);
            flameLight.attachToBody(this.body, 0, 0);
            flameLight.setActive(false);

            flamingEffect = Values.flamingThingsPool.obtain();
            for (ParticleEmitter emitter : flamingEffect.getEmitters()) {
                emitter.getSpawnWidth().setHigh(sprite.getWidth());
                emitter.getSpawnHeight().setHigh(sprite.getHeight());
            }
            flamingEffect.start();
        }
    }

    protected void destroy() {
        if (!this.destroyed) {
            Values.bodiesToDelete.add(this.body);
            this.destroyed = true;
            Values.handler.lightList.removeValue(this.flameLight, true);
        }
    }

    public void draw(SpriteBatch spriteBatch) {
        sprite.draw(spriteBatch);
        if (isFlaming && isFlammable) {
            flamingEffect.draw(spriteBatch);
        }
    }

    public void update() {
        if (sprite != null) {
            if (isFlammable) {
                flamingEffect.setPosition(sprite.getX() + (sprite.getWidth() / 2), sprite.getY() + (sprite.getHeight() / 2));
                flamingEffect.update(Gdx.graphics.getDeltaTime());
            }
            sprite.setPosition((body.getPosition().x * Values.BOX_PIXEL) - (sprite.getWidth() / 2), (body.getPosition().y * Values.BOX_PIXEL) - (sprite.getHeight() / 2));
        }
        if (isFlaming == false && flameLight != null) {
            Values.handler.lightList.removeValue(flameLight, true);
        }
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setFlaming(boolean flaming) {
        this.isFlaming = flaming;
        flameLight.setActive(true);
    }

    public void create() {

    }

    public Vector2 getPosition() {
        return new Vector2(sprite.getX(), sprite.getY());
    }
}
