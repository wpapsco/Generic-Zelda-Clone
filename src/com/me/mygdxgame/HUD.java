package com.me.mygdxgame;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class HUD extends Sprite {
	protected Stage stage;
	protected Table table;
	protected Label label;
	protected Dialog dialog;
	protected ArrayList<Image> images;
	protected WindowStyle style;
	protected Texture heartTexture;
	protected Texture deadHeartTexture;
	protected Player playerd;
	public String dialogText;	
	public int coin;
	public int hearts;
	
	public HUD(OrthographicCamera camera, Player player) {
		this.playerd = player;
		hearts = ((int) player.health);
		coin = 0;
		images = new ArrayList<Image>();
		heartTexture = new Texture(Gdx.files.internal("data/heart.png"));
		deadHeartTexture = new Texture(Gdx.files.internal("data/heartdead.png"));
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		//stage = new Stage();
		stage.setCamera(camera);
		stage.setViewport(Gdx.graphics.getWidth() / GZCGame.scale, Gdx.graphics.getHeight() / GZCGame.scale);
		table = new Table();
		stage.addActor(table);
		table.setFillParent(true);
		table.top();
		label = new Label("Clams: " + coin, new LabelStyle(new BitmapFont(), Color.WHITE));
		label.setPosition(0, Gdx.graphics.getHeight() / GZCGame.scale - 30);
		
		style = new WindowStyle();
		style.titleFont = new BitmapFont();
		style.titleFontColor = Color.WHITE;
		table.debugTable();
		
		
		table.addActor(label);
		
		updateHearts();
	}

	public void updateHearts() {
		int x = 0;
		Image image;
        hearts = ((int) playerd.health);
        //images.clear();
		for(int i = 0; i < 3; i++) {
			if (i < hearts) {
				image = new Image(heartTexture);
			}else {
				image = new Image(deadHeartTexture);
			}
			images.add(image);
			image.setPosition(x, Gdx.graphics.getHeight() / GZCGame.scale - 10);
			table.addActor(images.get(i));
			x += 15;
		}
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch) {
		label.setText("Clams: " + coin);
		if (dialogText != null) {
			dialog = new Dialog("Dialog", style);
			dialog.text(new Label(dialogText, new LabelStyle(new BitmapFont(), Color.WHITE)));
			dialog.fadeDuration = 10;
			dialog.setHeight(50);
			dialog.setWidth(1000);
			dialog.setPosition(100, 10);
			//dialog.setBackground(new Image(new Texture(Gdx.files.internal("data/ui_block_test1.png"))));
			stage.addActor(dialog);
			dialog.hide();
			dialogText = null;
		}
		stage.act();
		stage.draw();
	}
	
	public void setCoin(int coins) {
		coin = coins;
	}
	
	public void setDialog(String string) {
		dialogText = string;
	}
	
	public void loseHeart() {
		images.clear();
		this.updateHearts();
	}
	
	
}
