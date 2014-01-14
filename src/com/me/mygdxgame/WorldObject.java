package com.me.mygdxgame;

import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by William on 1/10/14.
 */
public abstract class WorldObject extends Sprite {
    protected boolean isFlammable;
    protected boolean isFlaming;
    protected ParticleEffect flamingEffect;
    protected PointLight flameLight;
    protected Body body;

    public WorldObject(Body body, boolean isFlammable) {
        this.isFlammable = isFlammable;
        this.body = body;
        create();
    }

    public WorldObject(Texture texture, Body body, boolean isFlammable) {
        super(texture);
        this.body = body;
        this.isFlammable = isFlammable;
        create();
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        super.draw(spriteBatch);
        if (isFlaming && isFlammable) {
            flamingEffect.draw(spriteBatch);
        }
    }

    public void update() {
        if (isFlammable) {
            flamingEffect.setPosition(this.getX() + (getRegionWidth() / 2), this.getY() + (getRegionHeight() / 2));
            flamingEffect.update(Gdx.graphics.getDeltaTime());
        }
    }

    @Override
    public void setRegion(TextureRegion region) {
        super.setRegion(region);
        for (ParticleEmitter emitter : flamingEffect.getEmitters()) {
            emitter.getSpawnWidth().setHigh(getRegionWidth());
            emitter.getSpawnHeight().setHigh(getRegionHeight());
            flameLight.setDistance(getRegionWidth() * 4 * Values.PIXEL_BOX);
        }
        flamingEffect.start();
    }

    @Override
    public void setTexture(Texture texture) {
        super.setTexture(texture);
        setRegionHeight(texture.getHeight());
        setRegionWidth(texture.getWidth());
    }

    public void setFlaming(boolean flaming) {
        this.isFlaming = flaming;
        flameLight.setActive(true);
    }

    public void create() {
        body.setUserData(this);
        if (isFlammable) {

            flameLight = new PointLight(Values.handler, 200, Values.fireColor, 30 * Values.PIXEL_BOX, body.getPosition().x * Values.BOX_PIXEL, body.getPosition().y * Values.BOX_PIXEL);
            flameLight.attachToBody(this.body, 0, 0);
            flameLight.setActive(false);

            flamingEffect = Values.flamingThingsPool.obtain();
            for (ParticleEmitter emitter : flamingEffect.getEmitters()) {
                emitter.getSpawnWidth().setHigh(getRegionWidth());
                emitter.getSpawnHeight().setHigh(getRegionHeight());
            }
            flamingEffect.start();
        }
    }
}
