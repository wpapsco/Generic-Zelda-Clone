package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by William on 1/25/14.
 */
public class NudeDude extends Enemy {
    private float flameDamageTimer = 0;
    public NudeDude(GameScreen screen, Vector2 position) {
        super(Player.createBody(position.x, position.y, 10, 10, (short) 0), true, screen, 100, 1);
    }

    @Override
    public void create() {
        super.create();
        //sprite = new Sprite(new Texture(Gdx.files.internal("data/Orc_Sheet1.png")));
        sprite = new Sprite(currentAnim.getKeyFrame(stateTime));
        sprite.setRegion(currentAnim.getKeyFrame(stateTime));
    }
    
    @Override
    public void draw(SpriteBatch spriteBatch) {
    	super.draw(spriteBatch);
    	sprite.setRegion(currentAnim.getKeyFrame(stateTime));
    }

    @Override
    public void update() {
        super.update();
        if (this.isFlaming) {
            flameDamageTimer += Gdx.graphics.getDeltaTime();
            this.takeDamage(1f);
        }
        if (flameDamageTimer >= 5.0f) {
            this.isFlaming = false;
            flameDamageTimer = 0;
        }
        stateTime += Gdx.graphics.getDeltaTime();
        animate();
    }
}