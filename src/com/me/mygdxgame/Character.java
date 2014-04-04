package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;


public class Character extends WorldObject {
	
	protected int width, height; 
	protected float stateTime;
	protected String path;
	protected TextureRegion[] frameRegions;
	protected Animation nAnimation;
	protected Animation sAnimation;
	protected Animation eAnimation;
	protected Animation wAnimation;
	protected Animation nRest;
	protected Animation sRest;
	protected Animation eRest;
	protected Animation wRest;
	protected Animation currentAnim;
	
	public boolean isUp;
	public boolean isRight;
	public boolean isRest;
	
	
	public Character(Body body, boolean isFlammable, String texturePath) {
		super(body, isFlammable);
		this.path = texturePath;
		create();
		postCreate();
	}
	
	@Override
	public void create() {
		super.create();
		//path = "data/Bully_Sheet1.png";
		width = 16;
        height = 28;
        setTexturePath(path);
		TextureRegion[][] tRegions = Sprite.split(new Texture(Gdx.files.internal(path)), width, height);
        frameRegions = new TextureRegion[tRegions.length * tRegions[0].length];
        int index = 0;
        for (TextureRegion[] tRegion : tRegions) {
            for (TextureRegion aTRegion : tRegion) {
                frameRegions[index++] = aTRegion;
            }
        }
		
		float animSpeed = .2f;
		
		nAnimation = new Animation(animSpeed, frameRegions[7], frameRegions[6], frameRegions[5], frameRegions[4]);
        sAnimation = new Animation(animSpeed, frameRegions[0], frameRegions[1], frameRegions[2], frameRegions[3]);
        eAnimation = new Animation(animSpeed, frameRegions[8], frameRegions[9], frameRegions[10], frameRegions[11]);
        wAnimation = new Animation(animSpeed, frameRegions[15], frameRegions[14], frameRegions[13], frameRegions[12]);
        nRest = new Animation(animSpeed, frameRegions[24], frameRegions[25], frameRegions[26], frameRegions[27]);
        sRest = new Animation(animSpeed, frameRegions[31], frameRegions[30], frameRegions[29], frameRegions[28]);
        eRest = new Animation(animSpeed, frameRegions[16], frameRegions[17], frameRegions[18],frameRegions[19]);
        wRest = new Animation(animSpeed, frameRegions[23], frameRegions[22], frameRegions[21], frameRegions[20]);
        nAnimation.setPlayMode(Animation.LOOP);
        sAnimation.setPlayMode(Animation.LOOP);
        eAnimation.setPlayMode(Animation.LOOP);
        wAnimation.setPlayMode(Animation.LOOP);
        nRest.setPlayMode(Animation.LOOP);
        sRest.setPlayMode(Animation.LOOP);
        eRest.setPlayMode(Animation.LOOP);
        wRest.setPlayMode(Animation.LOOP);
        currentAnim = nAnimation;
        stateTime = 0;
        isUp = false;
        isRight = true;
        isRest = true;
	}
	
	public Animation getAnimation() {
		return currentAnim;
	}
	
	public void setTexturePath(String texturePath) {
		this.path = texturePath;
	}
	
	protected void animate() {
		//System.out.println("Animating: "+isUp+","+isRight+","+isRest);
		if (isRest == true) {
			if (isRight && !isUp) {
            	currentAnim = eRest;
            } else if (!isRight && !isUp) {
            	currentAnim = wRest;
            } else if (isRight && isUp) {
            	currentAnim = nRest;
            } else {
            	currentAnim = sRest;
            }
		} else {
			if (isRight && !isUp) {
            	currentAnim = eAnimation;
            } else if (!isRight && !isUp) {
            	currentAnim = wAnimation;
            } else if (isRight && isUp) {
            	currentAnim = sAnimation;
            } else {
            	currentAnim = nAnimation;
            }
		}
	}
}


