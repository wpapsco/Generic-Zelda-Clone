package com.me.mygdxgame;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.utils.TimeUtils;

public class Player extends Character implements InputProcessor, ControllerListener {
	
	private int coin;
	private int keys;
    protected boolean isWasd;

	protected OrthographicCamera camera;
	protected OrthographicCamera lightCamera;
	protected OrthographicCamera hudCam;
	public HUD hud;

	public boolean xDown; //temp iskeydown X
	public boolean zDown;
	
	public ArrayList<Projectile> projectiles;
	public long shotTime;
	public float health;
	private float flameDamageTimer = 0;
	protected int tempdir;
	private GameScreen screen;
	private WeldJoint boxJoint;
	private Sound fireballSound;
	private Sound letGoSound;
	private Sound grabSound;
	private Controller controller;
	
	
	protected String attackTexturePath; //28x42
	protected TextureRegion[] attackRegions;
	protected Animation nAttack;
	protected Animation sAttack;
	protected Animation eAttack;
	protected Animation wAttack;
	public boolean isAttacking;
	
	public Player(int x, int y, OrthographicCamera camera, OrthographicCamera lightCamera, boolean isWasd, String texturePath, GameScreen screen, Controller controller) {
        super(Player.createBody(x, y, 16, 28, (short) -1), true, "data/Bully_Sheet1.png");
        Controllers.addListener(this);
        this.controller = controller;
        this.screen = screen;
		this.camera = camera;
		this.lightCamera = lightCamera;
        this.isWasd = isWasd;
        keys = 0;
        coin = 100;
        hudCam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        health = 3;
        isUp = false;
        isRight = true;
        fireballSound = Gdx.audio.newSound(Gdx.files.internal("data/fireball.wav"));
        grabSound = Gdx.audio.newSound(Gdx.files.internal("data/grabBlock.wav"));
        letGoSound = Gdx.audio.newSound(Gdx.files.internal("data/letgoBlock.wav"));
        tempdir = 0; //0=up,1=right,2=down,3=left
        hud = new HUD(hudCam, this);
        if (currentAnim == nAttack || currentAnim == wAttack || currentAnim == sAttack || currentAnim == eAttack) {
        	if (currentAnim.isAnimationFinished(stateTime)) {
        		isAttacking = false;
        	}
        }
        
	}

	public static Body createBody(float x, float y, int tWidth, int tHeight, short gIndex) {
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
        fixDef.filter.groupIndex = gIndex;
		body.createFixture(fixDef);

        return body;
	}

    @Override
    public void create() {
        super.create();
        attackTexturePath = "data/BullyAttack.png";
        loadAttackAnim(attackTexturePath, .2f);
        projectiles = new ArrayList<Projectile>();
        xDown = false;
        sprite = new Sprite(currentAnim.getKeyFrame(stateTime));

        sprite.setRegion(currentAnim.getKeyFrame(stateTime));
    }
    
    @Override
	public void draw(SpriteBatch spriteBatch) {
        super.draw(spriteBatch);
        sprite.setRegion(currentAnim.getKeyFrame(stateTime));
        sprite.setScale(currentAnim.getKeyFrame(stateTime).getRegionWidth()*Values.PIXEL_BOX*2, currentAnim.getKeyFrame(stateTime).getRegionWidth()*Values.PIXEL_BOX*2);
        for (int i = 0; i < projectiles.size(); i++) {
            projectiles.get(i).draw(spriteBatch);
        }
	}
	
	public void createProjectile() {
		ParticleProjectile projectile = new ParticleProjectile(sprite.getX() + (width / 2), sprite.getY() + (height / 2), Values.fireWandPool);
        float speed = .2f;
		if (tempdir == 0) {
			projectile.body.applyLinearImpulse(0, speed, projectile.pos.x, projectile.pos.y, true);
		}else if (tempdir == 2) {
			projectile.body.applyLinearImpulse(0, -speed, projectile.pos.x, projectile.pos.y, true);
		}else if (tempdir == 3) {
			projectile.body.applyLinearImpulse(-speed, 0, projectile.pos.x,  projectile.pos.y, true);
		}else if (tempdir == 1) {
			projectile.body.applyLinearImpulse(speed, 0, projectile.pos.x, projectile.pos.y, true);
		} else {
			projectile.body.applyLinearImpulse(0, speed, projectile.pos.x, projectile.pos.y, true);
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
            if (projectile.destroyed) {
                itemsToRemove.add(projectile);
            }
        }
        if (this.isFlaming) {
            flameDamageTimer += Gdx.graphics.getDeltaTime();
                this.takeDamage(.005f, new Vector2(0,0));
        }
        if (flameDamageTimer >= 5.0f) {
            this.isFlaming = false;
            flameDamageTimer = 0;
        }
        projectiles.removeAll(itemsToRemove);
        float speed = 3f;
        float xAxis = 0;
        float yAxis = 0;
        Vector2 linV;
        animate();
        if (isAttacking == true) {
        	meleeAttack();
        }
        if (currentAnim != nAttack || currentAnim != wAttack || currentAnim != sAttack || currentAnim != eAttack) {
        	isAttacking = false;
        }
        xDown = Gdx.input.isKeyPressed(Keys.X);
//        if(!xDown) {
//        	//*********************ALL WILL CHANGE FOR OUYA, THIS IS TEST STUFFS************************
//	        if (!isWasd) {
//	            if (Gdx.input.isKeyPressed(Keys.UP)) {
//	            	isUp = true;
//	            	yAxis = 1;
//	                stateTime += Gdx.graphics.getDeltaTime();
//	                tempdir = 0;
//	            }
//	            if (Gdx.input.isKeyPressed(Keys.DOWN)) {
//	            	isUp = false;
//	            	yAxis = -1;
//	                stateTime += Gdx.graphics.getDeltaTime();
//	                tempdir = 2;
//	            }
//	            if (Gdx.input.isKeyPressed(Keys.LEFT)) {
//	            	isRight = false;
//	            	xAxis = -1;
//	                stateTime += Gdx.graphics.getDeltaTime();
//	                tempdir = 3;
//	            }
//	            if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
//	            	isRight = true;
//	            	xAxis = 1;
//	                stateTime += Gdx.graphics.getDeltaTime();
//	                tempdir = 1;
//	            }
//	            
//	            if (!Gdx.input.isKeyPressed(Keys.UP)&&!Gdx.input.isKeyPressed(Keys.DOWN)&&!Gdx.input.isKeyPressed(Keys.LEFT)&&!Gdx.input.isKeyPressed(Keys.RIGHT)) {
//	            	xAxis = 0;
//	            	yAxis = 0;
//	                stateTime += Gdx.graphics.getDeltaTime();
//	            }
//	            linV = new Vector2(xAxis*speed, yAxis*speed);
//	            this.body.setLinearVelocity(linV);
//	        }
//	        if (isWasd) {
//	        	if (Gdx.input.isKeyPressed(Keys.W)) {
//	            	isUp = true;
//	            	yAxis = 1;
//	                stateTime += Gdx.graphics.getDeltaTime();
//	                tempdir = 0;
//	            }
//	            if (Gdx.input.isKeyPressed(Keys.S)) {
//	            	isUp = false;
//	            	yAxis = -1;
//	                stateTime += Gdx.graphics.getDeltaTime();
//	                tempdir = 2;
//	            }
//	            if (Gdx.input.isKeyPressed(Keys.A)) {
//	            	isRight = false;
//	            	xAxis = -1;
//	                stateTime += Gdx.graphics.getDeltaTime();
//	                tempdir = 3;
//	            }
//	            if (Gdx.input.isKeyPressed(Keys.D)) {
//	            	isRight = true;
//	            	xAxis = 1;
//	                stateTime += Gdx.graphics.getDeltaTime();
//	                tempdir = 1;
//	            }
//	            if (!Gdx.input.isKeyPressed(Keys.W)&&!Gdx.input.isKeyPressed(Keys.S)&&!Gdx.input.isKeyPressed(Keys.A)&&!Gdx.input.isKeyPressed(Keys.D)) {
//	            	xAxis = 0;
//	            	yAxis = 0;
//	                stateTime += Gdx.graphics.getDeltaTime();
//	            }
//	            linV = new Vector2(xAxis*speed, yAxis*speed);
//	            this.body.setLinearVelocity(linV);
//	    //***********************END TEST STUFFS*******************************************
//        //testing git! Woo this is totally a test!
//        }
//        }
//        if (!Gdx.input.isKeyPressed(Keys.UP)&&!Gdx.input.isKeyPressed(Keys.DOWN)&&!Gdx.input.isKeyPressed(Keys.LEFT)&&!Gdx.input.isKeyPressed(Keys.RIGHT)) {
//			isRest = true;
//		} else {
//			isRest = false;
//		}
        xAxis = controller.getAxis(Ouya.AXIS_LEFT_X);
        yAxis = controller.getAxis(Ouya.AXIS_LEFT_Y);
        yAxis = -yAxis;
        linV = new Vector2(xAxis*speed, yAxis*speed);
        float deadzone = .2f;
        if (linV.len() > deadzone * speed) {
        	this.body.setLinearVelocity(linV);
        } else {
        	this.body.setLinearVelocity(0, 0);
        }
        if (xAxis > deadzone) {
        	isRight = true;
        	tempdir = 1;
        }
        if (xAxis < -deadzone) {
        	isRight = false;
        	tempdir = 3;
        }
        if (yAxis > deadzone) {
        	isUp = true;
        	tempdir = 0;
        }
        if (yAxis < -deadzone) {
        	isUp = false;
        	tempdir = 2;
        }
        if (controller.getButton(Ouya.BUTTON_Y)) {
        	isAttacking = true;
        }
        if (!isAttacking) {
        	linV = new Vector2(xAxis*speed, yAxis*speed);
        } else {
        	linV = new Vector2(0,0);
        }
        isRest = false;
        if (Math.abs(yAxis) + Math.abs(xAxis) <= deadzone) {
        	isRest = true;
        }
        stateTime+=Gdx.graphics.getDeltaTime();
        
        camera.position.x = sprite.getX();
        camera.position.y = sprite.getY();
        lightCamera.position.x = sprite.getX() * Values.PIXEL_BOX;
        lightCamera.position.y = sprite.getY() * Values.PIXEL_BOX;
        System.out.println(currentAnim);
        
    }
    
    public void setPosition(Vector2 position) {
    	this.body.setTransform(position.cpy().scl(Values.PIXEL_BOX), body.getAngle());
    }

    public void setHearts(float health) {
    	this.health = health;
    	this.hud.loseHeart();
    }
    
    public float getHearts() {
    	return health;
    }
    
    public void loadAttackAnim(String path, float animSpeed) {
		TextureRegion[][] tRegions2 = Sprite.split(new Texture(Gdx.files.internal(attackTexturePath)), 28, 42);
        attackRegions = new TextureRegion[tRegions2.length * tRegions2[0].length];
        int index2 = 0;
        for (TextureRegion[] tRegion2 : tRegions2) {
            for (TextureRegion aTRegion2 : tRegion2) {
                attackRegions[index2++] = aTRegion2;
            }
        }
        sAttack = new Animation(animSpeed, attackRegions[8], attackRegions[9], attackRegions[10], attackRegions[11]);
        nAttack = new Animation(animSpeed, attackRegions[4], attackRegions[5], attackRegions[6], attackRegions[7]);
        eAttack = new Animation(animSpeed, attackRegions[0], attackRegions[1], attackRegions[2], attackRegions[3]);
        wAttack = new Animation(animSpeed, attackRegions[12], attackRegions[13], attackRegions[14], attackRegions[15]);
        nAttack.setPlayMode(Animation.LOOP);
        sAttack.setPlayMode(Animation.LOOP);
        eAttack.setPlayMode(Animation.LOOP);
        wAttack.setPlayMode(Animation.LOOP);
        
	}
    
    public void meleeAttack() {
    	if (isRight && !isUp) {
        	currentAnim = eAttack;
        } else if (!isRight && !isUp) {
        	currentAnim = wAttack;
        } else if (isRight && isUp) {
        	currentAnim = nAttack;
        } else {
        	currentAnim = sAttack;
        }
    	stateTime += Gdx.graphics.getDeltaTime();
    }

    public int getCoin() {
		return coin;
	}

	public void setCoin(int coin) {
		this.coin = coin;
	}
	
	public void addCoin(float coin) {
		this.coin += coin;
		System.out.println("Coin: " + this.coin);
	}
	
	public boolean addKeys(int keys) {
		if (this.keys + keys >= 0) {
			this.keys += keys;
			return true;
		} else {
			return false;
		}
	}
	
	public void takeDamage(float damage, Vector2 pos) {
		System.out.println("Health: " + health);
        health-=damage;
        if (health <= 0) {
        	this.die();
        }
        hud.loseHeart();
    }
	
	@Override
	public void destroy() {
		
	}

    public void die() {
        //TODO: add dying!
        System.out.println("you died somehow!");
    }

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		if (keycode == Keys.SPACE) {
            screen.onInteraction(this);
        }
		if (keycode == Keys.C) {
			for (MovableBlock block : screen.blocks) {
				if (MathThing.getDistance(this.getPosition(), block.getPosition()) < 35 && this.boxJoint == null) {
					grabSound.play();
					System.out.println("you grabbed a block!");
					WeldJointDef def = new WeldJointDef();
					def.initialize(block.body, this.body, block.body.getWorldCenter());
					block.body.setFixedRotation(false);
					boxJoint = (WeldJoint) Values.world.createJoint(def);
					break;
				}
			}
		}
		if (keycode == Keys.X) {
			if (this.getCoin() >= 10) {
				//float pitch = (float) ((Math.random() * 1.5f) + .5f);
				float pitch = 1f;
				fireballSound.play(1f, pitch, 0f);
				createProjectile();
				this.addCoin(-10);
			}
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		if (keycode == Keys.C) {
			if (this.boxJoint != null) {
				letGoSound.play();
				boxJoint.getBodyA().setFixedRotation(true);
				boxJoint.getBodyA().setTransform(boxJoint.getBodyA().getPosition(), 0.0f);
				Values.world.destroyJoint(boxJoint);
				boxJoint = null;
			}
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	public int getKeys() {
		// TODO Auto-generated method stub
		return this.keys;
	}

	public void setKeys(int keys) {
		this.keys = keys;
	}

	@Override
	public boolean accelerometerMoved(Controller arg0, int arg1, Vector3 arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean axisMoved(Controller arg0, int arg1, float arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean buttonDown(Controller arg0, int arg1) {
		// TODO Auto-generated method stub
		if (arg0 == controller) {
			if (arg1 == Ouya.BUTTON_O) {
	            screen.onInteraction(this);
	        }
			if (arg1 == Ouya.BUTTON_A) {
				for (MovableBlock block : screen.blocks) {
					if (MathThing.getDistance(this.getPosition(), block.getPosition()) < 35 && this.boxJoint == null) {
						grabSound.play();
						System.out.println("you grabbed a block!");
						WeldJointDef def = new WeldJointDef();
						def.initialize(block.body, this.body, block.body.getWorldCenter());
						block.body.setFixedRotation(false);
						boxJoint = (WeldJoint) Values.world.createJoint(def);
						break;
					}
				}
			}
			if (arg1 == Ouya.BUTTON_U) {
				if (this.getCoin() >= 10) {
					//float pitch = (float) ((Math.random() * 1.5f) + .5f);
					float pitch = 1f;
					fireballSound.play(1f, pitch, 0f);
					createProjectile();
					this.addCoin(-10);
				}
			}
		}
		return false;
	}

	@Override
	public boolean buttonUp(Controller arg0, int arg1) {
		// TODO Auto-generated method stub
		if (arg1 == Ouya.BUTTON_A) {
			if (this.boxJoint != null) {
				letGoSound.play();
				boxJoint.getBodyA().setFixedRotation(true);
				boxJoint.getBodyA().setTransform(boxJoint.getBodyA().getPosition(), 0.0f);
				Values.world.destroyJoint(boxJoint);
				boxJoint = null;
			}
		}
		return false;
	}

	@Override
	public void connected(Controller arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disconnected(Controller arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean povMoved(Controller arg0, int arg1, PovDirection arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean xSliderMoved(Controller arg0, int arg1, boolean arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean ySliderMoved(Controller arg0, int arg1, boolean arg2) {
		// TODO Auto-generated method stub
		return false;
	}
}
