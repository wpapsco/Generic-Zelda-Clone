package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Projectile extends WorldObject {
	public int width;
	public int height;
	
	public Vector2 vel;
	public Vector2 pos;
	public Texture texture;
    public boolean destroyOnContact;
    public boolean isDestroyed = false;
    protected Sound sound;

    public Projectile(float x, float y, boolean isFlammable, boolean destroyOnContact) {
        super(new Texture(Gdx.files.internal("data/BigBooty.png")), Projectile.createBody(x, y), isFlammable);
        this.destroyOnContact = destroyOnContact;
        sound = Gdx.audio.newSound(Gdx.files.internal("data/fireballnoise.mp3"));
        createBody(x, y);
		vel = body.getLinearVelocity();
		pos = body.getPosition();
    }
	
	private static Body createBody(float x, float y) {
		BodyDef def = new BodyDef();
		def.position.set(x * Values.PIXEL_BOX, y * Values.PIXEL_BOX);
		def.type = BodyType.DynamicBody;
        def.bullet = true;
		
		CircleShape circle = new CircleShape();
		circle.setRadius(4 * Values.PIXEL_BOX);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 0.5f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 1f;
        fixtureDef.filter.groupIndex = -1;
		
		Body body = Values.world.createBody(def);
		body.createFixture(fixtureDef);
		
		circle.dispose();

        return body;
	}

    @Override
	public void draw(SpriteBatch spriteBatch) {
        super.draw(spriteBatch);
		//spriteBatch.draw(texture, this.getX(), this.getY());
	}

    @Override
    public void update() {
        super.update();
//        setX((this.body.getPosition().x * Values.BOX_PIXEL) - (getWidth() / 2));
//        setY((this.body.getPosition().y * Values.BOX_PIXEL) - (getHeight() / 2));
    }

    public void destroy() {
        //texture.dispose();
        isDestroyed = true;
    }
}
