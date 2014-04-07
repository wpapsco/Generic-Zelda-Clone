package com.me.mygdxgame;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

abstract class ContextualChangeListener extends ChangeListener {
		
	GZCGame game;
	Object information;
	
	public ContextualChangeListener(GZCGame game, Object information) {
		this.game = game;
		this.information = information;
	}
}