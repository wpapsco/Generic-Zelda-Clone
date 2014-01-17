package com.me.mygdxgame;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;


public class Character extends WorldObject {
	
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
	
	public Character(Body body, boolean isFlammable, String path) {
		super(body, isFlammable);
		
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
		
	}
	
	public Animation getAnimation() {
		return currentAnim;
	}
}


