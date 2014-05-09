package com.me.mygdxgame;

import java.util.ArrayList;
import box2dLight.ConeLight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.ScaledNumericValue;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Boss extends WorldObject {

	public ArrayList<BossLocation> locations;
	private ParticleEffect batEffect;
	private ConeLight light;
	private GameScreen screen;
	private Vector2 initAngle;
	private float attackInterval = 5.0f;
	private float attackCounter = 0f;
	private boolean isLit = false;

	public Boss(BossLocation location, GameScreen screen) {
		super(new Texture(Gdx.files.internal("data/BossMouthClosed.png")), Boss.makeBossBody(location.location), false);
		// TODO Auto-generated constructor stub
		this.screen = screen;
		this.locations = new ArrayList<BossLocation>();
		locations.add(location);
		batEffect = new ParticleEffect();
		batEffect.load(Gdx.files.internal("data/bats.p"), Gdx.files.internal("data/"));
		initAngle = new Vector2(batEffect.getEmitters().get(0).getAngle().getHighMax(), batEffect.getEmitters().get(0).getAngle().getHighMin());
		ScaledNumericValue angle = batEffect.getEmitters().get(0).getAngle();
		angle.setHighMax(initAngle.x + location.direction);
		angle.setHighMin(initAngle.y + location.direction);
		this.sprite.setRotation(location.direction - 90);
		light = new ConeLight(Values.handler, 100, Color.RED, 400 * Values.PIXEL_BOX, body.getPosition().x, body.getPosition().y, location.direction + 90, 30);
		//light.getColor().a = 1f;
		attack();
	}
	
	public void attack() {
		BossLocation location = locations.get((int) Math.floor(Math.random() * locations.size()));
		this.sprite.setRotation(location.direction - 180);
		this.body.setTransform(location.location.x * Values.PIXEL_BOX, location.location.y * Values.PIXEL_BOX, location.direction * .0174532925f);
		this.light.setPosition(body.getPosition().x, body.getPosition().y);
		this.sprite.setPosition(location.location.x - (this.getSprite().getWidth() / 2), location.location.y - (this.getSprite().getHeight() / 2));
		
		this.sprite.setTexture(new Texture(Gdx.files.internal("data/BossMouthOpen.png")));
		ScaledNumericValue angle = batEffect.getEmitters().get(0).getAngle();
		light.setDirection(location.direction + 90);
		angle.setHighMax(initAngle.x + location.direction);
		angle.setHighMin(initAngle.y + location.direction);
		this.batEffect.start();
		
		float rad = (location.direction + 90) * .01745f;
		Vector2 dir = new Vector2((float)Math.cos(rad), (float)Math.sin(rad));
		dir.scl(40);
		System.out.println(dir);
		
		for (int i = 0; i < 3; i++) {
			Bat b = new Bat(this.screen, new Vector2(this.sprite.getX() + dir.x, this.sprite.getY() + dir.y));
			screen.add(b);
		}
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		super.update();
		if (batEffect.isComplete()) {
			attackCounter += Gdx.graphics.getDeltaTime();
			this.sprite.setTexture(new Texture(Gdx.files.internal("data/BossMouthClosed.png")));
			if (attackCounter >= attackInterval) {
				attack();
				attackCounter = 0;
			}
		}
		batEffect.setPosition(this.getPosition().x + this.getSprite().getWidth() / 2, this.getPosition().y + this.getSprite().getHeight() / 2);
		light.setDistance((1 - (batEffect.getEmitters().get(0).durationTimer / batEffect.getEmitters().get(0).duration)) * 400 * Values.PIXEL_BOX);
		isLit = false;
		//if the player takes damage
		//System.out.println(light.contains(screen.getPlayers().get(0).body.getPosition().x + 1, screen.getPlayers().get(0).body.getPosition().y + 1) || light.contains(screen.getPlayers().get(0).body.getPosition().x - 1, screen.getPlayers().get(0).body.getPosition().y - 1));
		for (MovableBlock block : screen.blocks) {
			if (MathThing.getDistance(block.getPosition(), getPosition()) <= block.getLightDistance() && block.isFlaming) {
				isLit = true;
				break;
			}
		}
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch) {
		// TODO Auto-generated method stub
		batEffect.draw(spriteBatch, Gdx.graphics.getDeltaTime());
		super.draw(spriteBatch);
	}
	
	public static Body makeBossBody(Vector2 pixelLocation) {
		BodyDef def = new BodyDef();
		def.fixedRotation = true;
		def.type = BodyType.StaticBody;
		def.position.set(pixelLocation.x * Values.PIXEL_BOX, pixelLocation.y * Values.PIXEL_BOX);
		
		PolygonShape bossShape = new PolygonShape();
		bossShape.setAsBox(32 * Values.PIXEL_BOX, 16 * Values.PIXEL_BOX);
		
		FixtureDef fixDef = new FixtureDef();
		fixDef.density = 0f;
		fixDef.shape = bossShape;
		
		Body body = Values.world.createBody(def);
		body.createFixture(fixDef);
		
		return body;
	}
}
