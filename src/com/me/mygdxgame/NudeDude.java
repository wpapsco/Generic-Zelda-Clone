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
    private float wanderTimer = 0f;
    private float wanderTime = 2f;
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
    public void idle() {
    	// TODO Auto-generated method stub
    	super.idle();
    	wanderTimer += Gdx.graphics.getDeltaTime();
    	if (wanderTimer >= wanderTime) {
	    	Vector2 newVel = new Vector2();
	    	newVel.x = (float) Math.random() - .5f;
	    	newVel.y = (float) Math.random() - .5f;
	    	newVel.nor();
	    	newVel.scl(speed);
	    	this.body.setLinearVelocity(newVel);
	    	
	    	if (newVel.x > 0) {
	        	isRight = true;
	        } else {
	        	isRight = false;
	        }
	        if (newVel.y > 0) {
	        	isUp = true;
	        } else {
	        	isUp = false;
	        }
	        wanderTimer = 0f;
    	}
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