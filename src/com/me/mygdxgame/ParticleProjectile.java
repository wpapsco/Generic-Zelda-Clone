package com.me.mygdxgame;

import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

/**
 * Created by William on 1/10/14.
 */

public class ParticleProjectile extends Projectile {

    private ParticleEffectPool.PooledEffect effect;
    private PointLight light;

    public ParticleProjectile(float x, float y, ParticleEffectPool pool) {
        super(x, y);
        effect = pool.obtain();
        effect.setPosition(x, y);
        light = new PointLight(Values.handler, 200, new Color(1, 0, 0, .5f), 5, x, y);
        light.attachToBody(this.body, 0, 0);
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        super.draw(spriteBatch);
        effect.setPosition(this.getX() + (texture.getWidth() / 2), this.getY() + (texture.getWidth() / 2));
        effect.update(Gdx.graphics.getDeltaTime());
        effect.draw(spriteBatch);
    }
}
