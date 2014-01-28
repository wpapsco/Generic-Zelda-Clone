package com.me.mygdxgame;

import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

/**
 * Created by William on 1/10/14.
 */

public class ParticleProjectile extends Projectile {

    private ParticleEffectPool.PooledEffect effect;
    private PointLight light;

    public ParticleProjectile(float x, float y, ParticleEffectPool pool) {
        super(x, y, false, true);
        this.isFlaming = true;
        effect = pool.obtain();
        effect.setPosition(x, y);
        light = new PointLight(Values.handler, 200, Values.fireColor, 5, x, y);
        light.attachToBody(this.body, 0, 0);
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        super.draw(spriteBatch);
        effect.draw(spriteBatch);
    }

    @Override
    public void update() {
        super.update();
        effect.setPosition(sprite.getX() + (sprite.getWidth() / 2), sprite.getY() + (sprite.getWidth() / 2));
        effect.update(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void destroy() {
        super.destroy();
        for (ParticleEmitter emitter : effect.getEmitters()) {
            emitter.setContinuous(false);
        }
        Values.bodiesToDelete.add(this.body);
    }
}
