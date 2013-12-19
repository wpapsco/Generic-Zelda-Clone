package com.me.mygdxgame;

import java.util.ArrayList;

import box2dLight.PointLight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;

public class GameScreen implements Screen, InputProcessor {

	protected GZCGame game;
	protected ArrayList<Sprite> sprites;
	protected OrthogonalTiledMapRenderer renderer;
	protected TiledMap map;
	protected ArrayList<Body> bodyWalls;
	protected ArrayList<FlickeringLight> lights;
	protected PointLight light;
	
	public GameScreen(GZCGame game) {
		Gdx.input.setInputProcessor(this);
		this.game = game;
		bodyWalls = new ArrayList<Body>();
		sprites = new ArrayList<Sprite>();
		lights = new ArrayList<FlickeringLight>();
		TmxMapLoader loader = new TmxMapLoader();
		map = loader.load("data/testMap1.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, game.batch);
		renderer.setView(game.camera);
		createBox2dWorld();
		Values.handler.setAmbientLight(0, 0, 0, 0f);
		light = new PointLight(Values.handler, 300, new Color(0f, 0f, 0f, 1f), 200 * Values.PIXEL_BOX, 320 * Values.PIXEL_BOX, 320 * Values.PIXEL_BOX);
		light.setSoft(true);
		Player player = new Player(132, 100, game.camera, game.lightCamera);
		light.attachToBody(player.body, 0, 0);
		add(player);
	}
	
	private void createBox2dWorld() {
		for (int i = 0; i < map.getLayers().getCount(); i++) {
			if (map.getLayers().get(i) instanceof TiledMapTileLayer) {
				TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(i);
				if (Boolean.parseBoolean(layer.getProperties().get("collide").toString())) {
					for (int x = 0; x < layer.getWidth(); x++) {
						for (int y = 0; y < layer.getHeight(); y++) {
							//create a box2d body
							if (layer.getCell(x, y) != null) {
								createWallCube(x, y, layer.getTileWidth(), layer.getTileHeight(), true);
							}
						}
					}
				} else if (layer.getProperties().get("casts_shadow") != null && Boolean.parseBoolean(layer.getProperties().get("casts_shadow").toString())) {
					for (int x = 0; x < layer.getWidth(); x++) {
						for (int y = 0; y < layer.getHeight(); y++) {
							//create a box2d body
							if (layer.getCell(x, y) != null) {
								createWallCube(x, y, layer.getTileWidth(), layer.getTileHeight(), false);
							}
						}
					}
				}
			} else { // layer is object layer
				
			}
		}
	}

	private void createWallCube(int x, int y, float tileWidth, float tileHeight, boolean collide) {
		BodyDef def = new BodyDef();
		def.position.x = ((x * tileWidth) + (tileWidth / 2)) * Values.PIXEL_BOX;
		def.position.y = ((y * tileHeight) + (tileHeight / 2)) * Values.PIXEL_BOX; //THIS MIGHT CAUSE A PROBLEM (INVERTED Y)
		def.type = BodyType.StaticBody;
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox((tileWidth / 2) * Values.PIXEL_BOX, (tileHeight / 2) * Values.PIXEL_BOX);
		
		FixtureDef fixDef = new FixtureDef();
		if (!collide) {
			fixDef.filter.maskBits = 0;
		}
		fixDef.shape = shape;
		fixDef.density = 0;
		
		Body bod = Values.world.createBody(def);
		bod.createFixture(fixDef);
		bodyWalls.add(bod);
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		game.camera.update();
		game.lightCamera.update();
		game.batch.setProjectionMatrix(game.camera.combined);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		renderer.setView(game.camera);
		//renderer.render();
		for (int i = 0; i < map.getLayers().getCount(); i++) {
			if (map.getLayers().get(i).getName().equals("Characters")) {
				for (int j = 0; j < sprites.size(); j++) {
					game.batch.begin();
					sprites.get(j).draw(game.batch);
					game.batch.end();
				}
				renderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get(i));
			} else {
				MapLayer layer = map.getLayers().get(i);
				if (layer instanceof TiledMapTileLayer) {
					game.batch.begin();
					renderer.renderTileLayer((TiledMapTileLayer) layer);
					game.batch.end();
				}
			}
			if (map.getLayers().get(i).getName().equals("Lights")) {
				Values.handler.setCombinedMatrix(game.lightCamera.combined, game.lightCamera.position.x, game.lightCamera.position.y, game.lightCamera.viewportWidth, game.lightCamera.viewportHeight);
				Values.handler.updateAndRender();
			}
		}
		for (int i = 0; i < lights.size(); i++) {
			lights.get(i).updateToFlicker();
		}
		//Vector3 thing = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		//game.lightCamera.unproject(thing);
		//light.setPosition(thing.x, thing.y);
		Values.world.step(1f/30f, 10, 10);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		renderer.dispose();
	}
	
	public void add(Sprite sprite) {
		sprites.add(sprite);
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
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
		lights.add(new FlickeringLight(Values.handler, 300, light, 1f, .01f));
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
}
