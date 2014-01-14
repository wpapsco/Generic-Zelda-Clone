package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

public class Player extends WorldObject {
	
	private int coin;
    protected boolean isWasd;
	protected TextureRegion[] frameRegions;
	protected Animation nAnimation;
	protected Animation sAnimation;
	protected Animation eAnimation;
	protected Animation wAnimation;
	protected Animation currentAnim;
	protected OrthographicCamera camera;
	protected OrthographicCamera lightCamera;
	protected OrthographicCamera hudCam;
	public HUD hud;
	protected float stateTime;
	public int width;
	public int height;
	public boolean xDown; //temp iskeydown X
	public boolean zDown;
	public ArrayList<Projectile> projectiles;
	public long shotTime;
	
	public Player(int x, int y, OrthographicCamera camera, OrthographicCamera lightCamera, boolean isWasd) {
        super(Player.createBody(x, y, 8, 13), true);
		this.camera = camera;
		this.lightCamera = lightCamera;
        this.isWasd = isWasd;
        this.setRegion(currentAnim.getKeyFrame(stateTime));
        coin = 100;
        hudCam = new OrthographicCamera(Gdx.graphics.getWidth() / GZCGame.scale, Gdx.graphics.getHeight() / GZCGame.scale);
        hud = new HUD(hudCam);
	}

	private static Body createBody(int x, int y, int tWidth, int tHeight) {
		BodyDef def = new BodyDef();
		def.fixedRotation = true;
		def.position.set(x * Values.PIXEL_BOX, y * Values.PIXEL_BOX);
		def.type = BodyType.DynamicBody;

		//creates a diamond shape for collision
		PolygonShape shape = new PolygonShape();
		Vector2[] verts = new Vector2[4];
		float boxW = tWidth * Values.PIXEL_BOX;
		float boxH = tHeight * Values.PIXEL_BOX;
		verts[0] = new Vector2(0, boxH / 2);
		verts[1] = new Vector2(boxW / 2, 0);
		verts[2] = new Vector2(0, -(boxH / 2));
		verts[3] = new Vector2(-(boxW / 2), 0);
		shape.set(verts);

		Body body = Values.world.createBody(def);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = 1f;
        fixDef.filter.groupIndex = -1;
		body.createFixture(fixDef);

        return body;
	}

    @Override
    public void create() {
        super.create();
        width = 8;
        height = 13;
        TextureRegion[][] tRegions = Sprite.split(new Texture(Gdx.files.internal("data/mage.png")), width, height);
        frameRegions = new TextureRegion[tRegions.length * tRegions[0].length];
        int index = 0;
        for (TextureRegion[] tRegion : tRegions) {
            for (TextureRegion aTRegion : tRegion) {
                frameRegions[index++] = aTRegion;
            }
        }
        float animSpeed = .2f;
        nAnimation = new Animation(animSpeed, frameRegions[4], frameRegions[5], frameRegions[6], frameRegions[7]);
        sAnimation = new Animation(animSpeed, frameRegions[0], frameRegions[1], frameRegions[2], frameRegions[3]);
        eAnimation = new Animation(animSpeed, frameRegions[8], frameRegions[9], frameRegions[10], frameRegions[11]);
        wAnimation = new Animation(animSpeed, frameRegions[12], frameRegions[13], frameRegions[14], frameRegions[15]);
        nAnimation.setPlayMode(Animation.LOOP);
        sAnimation.setPlayMode(Animation.LOOP);
        eAnimation.setPlayMode(Animation.LOOP);
        wAnimation.setPlayMode(Animation.LOOP);
        currentAnim = nAnimation;
        stateTime = 0;
        projectiles = new ArrayList<Projectile>();
        xDown = false;
    }

    @Override
	public void draw(SpriteBatch spriteBatch) {
		spriteBatch.draw(currentAnim.getKeyFrame(stateTime), this.getX(), this.getY());
        for (int i = 0; i < projectiles.size(); i++) {
            projectiles.get(i).draw(spriteBatch);
        }
	}
	
	public void createProjectile() {
		ParticleProjectile projectile = new ParticleProjectile(this.getX() + (width / 2), this.getY() + (height / 2), Values.fireWandPool);
        float speed = .2f;
		if (currentAnim == nAnimation) {
			projectile.body.applyLinearImpulse(0, speed, projectile.pos.x, projectile.pos.y, true);
		}else if (currentAnim == sAnimation) {
			projectile.body.applyLinearImpulse(0, -speed, projectile.pos.x, projectile.pos.y, true);
		}else if (currentAnim == wAnimation) {
			projectile.body.applyLinearImpulse(-speed, 0, projectile.pos.x,  projectile.pos.y, true);
		}else if (currentAnim == eAnimation) {
			projectile.body.applyLinearImpulse(speed, 0, projectile.pos.x, projectile.pos.y, true);
		}
		projectiles.add(projectile);
		shotTime = TimeUtils.nanoTime();
	}

    @Override
    public void update() {
        super.update();
        hud.setCoin(coin);
        ArrayList<Projectile> itemsToRemove = new ArrayList<Projectile>();
        for (Projectile projectile : projectiles) {
            projectile.update();
            if (projectile.isDestroyed) {
                itemsToRemove.add(projectile);
            }
        }
        projectiles.removeAll(itemsToRemove);
        float speed = 3f;
        xDown = Gdx.input.isKeyPressed(Keys.X);
        if (!isWasd) {
            if (Gdx.input.isKeyPressed(Keys.UP)) {
                currentAnim = nAnimation;
                this.body.setLinearVelocity(new Vector2(0, speed));
                stateTime += Gdx.graphics.getDeltaTime();
            } else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
                currentAnim = sAnimation;
                this.body.setLinearVelocity(new Vector2(0, -speed));
                stateTime += Gdx.graphics.getDeltaTime();
            } else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
                currentAnim = wAnimation;
                this.body.setLinearVelocity(new Vector2(-speed, 0));
                stateTime += Gdx.graphics.getDeltaTime();
            } else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
                this.body.setLinearVelocity(new Vector2(speed, 0));
                currentAnim = eAnimation;
                stateTime += Gdx.graphics.getDeltaTime();
            } else {
                this.body.setLinearVelocity(0, 0);
            }
        }
        if (isWasd) {
            if (Gdx.input.isKeyPressed(Keys.W)) {
                currentAnim = nAnimation;
                this.body.setLinearVelocity(new Vector2(0, speed));
                stateTime += Gdx.graphics.getDeltaTime();
            } else if (Gdx.input.isKeyPressed(Keys.S)) {
                currentAnim = sAnimation;
                this.body.setLinearVelocity(new Vector2(0, -speed));
                stateTime += Gdx.graphics.getDeltaTime();
            } else if (Gdx.input.isKeyPressed(Keys.A)) {
                currentAnim = wAnimation;
                this.body.setLinearVelocity(new Vector2(-speed, 0));
                stateTime += Gdx.graphics.getDeltaTime();
            } else if (Gdx.input.isKeyPressed(Keys.D)) {
                this.body.setLinearVelocity(new Vector2(speed, 0));
                currentAnim = eAnimation;
                stateTime += Gdx.graphics.getDeltaTime();
            } else {
                this.body.setLinearVelocity(0, 0);
            }
        }
        setX((this.body.getPosition().x * Values.BOX_PIXEL) - (width / 2));
        setY((this.body.getPosition().y * Values.BOX_PIXEL) - (height / 2));
        camera.position.x = this.getX();
        camera.position.y = this.getY();
        lightCamera.position.x = this.getX() * Values.PIXEL_BOX;
        lightCamera.position.y = this.getY() * Values.PIXEL_BOX;
    }

    public int getCoin() {
		return coin;
	}

	public void setCoin(int coin) {
		this.coin = coin;
	}
	
	public void addCoin(float coin) {
		this.coin += coin;
		System.out.println(this.coin);
	}
}
