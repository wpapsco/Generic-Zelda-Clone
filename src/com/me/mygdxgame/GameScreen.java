package com.me.mygdxgame;

import java.util.ArrayList;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen, InputProcessor {

	protected GZCGame game;
	protected ArrayList<Sprite> sprites;
	protected OrthogonalTiledMapRenderer renderer;
	protected TiledMap map;
	protected ArrayList<Body> bodyWalls;
	protected ArrayList<FlickeringLight> lights;
	protected PointLight light;
	protected MapObjects objects;
	protected Player player;
	
	public GameScreen(GZCGame game, String mapLocation) {
		Gdx.input.setInputProcessor(this);
		this.game = game;
		bodyWalls = new ArrayList<Body>();
		sprites = new ArrayList<Sprite>();
		lights = new ArrayList<FlickeringLight>();
		objects = new MapObjects();
		TmxMapLoader loader = new TmxMapLoader();
		map = loader.load(mapLocation);
		renderer = new OrthogonalTiledMapRenderer(map, game.batch);
		renderer.setView(game.camera);
		createBox2dWorld(true);
		String[] vals;
		if (map.getProperties().get("LightColor") != null) {
			vals = map.getProperties().get("LightColor").toString().split(",");
		} else {
			vals = new String[]{"0", "0", "0", "1"};
		}
		if (map.getProperties().get("AmbientLight") != null) {
			Values.handler.setAmbientLight(Float.parseFloat(map.getProperties().get("AmbientLight").toString()));
		} else {
			Values.handler.setAmbientLight(0);
		}
		light = new PointLight(Values.handler, 300, new Color(Float.parseFloat(vals[0]), Float.parseFloat(vals[1]), Float.parseFloat(vals[2]), Float.parseFloat(vals[3])), 200 * Values.PIXEL_BOX, 320 * Values.PIXEL_BOX, 320 * Values.PIXEL_BOX);
		light.setSoft(true);
		player = new Player(
				Math.round(Float.parseFloat(map.getProperties().get("StartX").toString()) * Integer.parseInt(map.getProperties().get("TileWidth").toString())), 
				Math.round(Float.parseFloat(map.getProperties().get("StartY").toString()) * Integer.parseInt(map.getProperties().get("TileHeight").toString())), 
				game.camera, game.lightCamera);
		light.attachToBody(player.body, 0, 0);
		add(player);
		//add(new HUD(game.camera));
	}
	
	private void createBox2dWorld(boolean test) {
		Values.world = new World(new Vector2(), true);
		Values.handler = new RayHandler(Values.world);

		for (int i = 0; i < map.getLayers().getCount(); i++) {
			if (map.getLayers().get(i) instanceof TiledMapTileLayer) {
				TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(i);
				boolean shadow;
				boolean collide;
				boolean movable;
				if (layer.getProperties().get("casts_shadow") != null) {
					shadow = Boolean.parseBoolean(layer.getProperties().get("casts_shadow").toString());
				} else {
					shadow = false;
				}
				if (layer.getProperties().get("collide") != null) {
					collide = Boolean.parseBoolean(layer.getProperties().get("collide").toString());
				} else {
					collide = false;
				}
				if (layer.getProperties().get("moveable") != null) {
					movable = Boolean.parseBoolean(layer.getProperties().get("moveable").toString());
				} else {
					movable = false;
				}
				if (shadow || collide) {
					for (int x = 0; x < layer.getWidth(); x++) {
						for (int y = 0; y < layer.getHeight(); y++) {
							if (layer.getCell(x, y) != null) {
								createWallCube(x, y, layer.getTileWidth(), layer.getTileHeight(), collide, movable, layer.getCell(x, y).getTile().getTextureRegion());
							}
						}
					}
				}
			} else {
				MapObjects objects = map.getLayers().get(i).getObjects();
				for (int j = 0; j < objects.getCount(); j++) {
					this.objects.add(objects.get(j));
					if (objects.get(j).getProperties().get("enabled") == null) {
						objects.get(j).getProperties().put("enabled", "true");
					}
					if (objects.get(j) instanceof EllipseMapObject) {
						EllipseMapObject ellipse = (EllipseMapObject) objects.get(j);
						float radius = (ellipse.getEllipse().height + ellipse.getEllipse().width) / 2;
						float centerX = ellipse.getEllipse().x + (ellipse.getEllipse().width / 2);
						float centerY = ellipse.getEllipse().y + (ellipse.getEllipse().height / 2);
						
						Color lightColor;
						if (ellipse.getProperties().get("LightColor") != null) {
							String[] colorVals = ellipse.getProperties().get("LightColor").toString().split(",");
							lightColor = new Color(Float.parseFloat(colorVals[0]), Float.parseFloat(colorVals[1]), Float.parseFloat(colorVals[2]), Float.parseFloat(colorVals[3]));
						} else {
							lightColor = new Color(0, 0, 0, 1);
						}
                        lights.add(new FlickeringLight(Values.handler, 300, lightColor, radius * Values.PIXEL_BOX, centerX * Values.PIXEL_BOX, centerY * Values.PIXEL_BOX, 0, Float.MAX_VALUE));

                    }
				}
			}
		}
	}
	
	
	private void createWallCube(int x, int y, float tileWidth, float tileHeight, boolean collide, boolean moveable, TextureRegion region) {
		BodyDef def = new BodyDef();
		def.position.x = ((x * tileWidth) + (tileWidth / 2)) * Values.PIXEL_BOX;
		def.position.y = ((y * tileHeight) + (tileHeight / 2)) * Values.PIXEL_BOX; //THIS MIGHT CAUSE A PROBLEM (INVERTED Y)
		def.fixedRotation = true;
		
		PolygonShape shape = new PolygonShape();
		
		FixtureDef fixDef = new FixtureDef();
		if (!collide) {
			fixDef.filter.maskBits = 0;
		}
		fixDef.shape = shape;
		fixDef.density = 0;
		
		Body bod;
		
		if (!moveable) {
			shape.setAsBox(((tileWidth) / 2) * Values.PIXEL_BOX, ((tileHeight) / 2) * Values.PIXEL_BOX);
			def.type = BodyType.StaticBody;
			fixDef.density = 0;
			bod = Values.world.createBody(def);
		} else {
			float boxWidth = (tileWidth - 1) * Values.PIXEL_BOX;
			float boxHeight = (tileHeight - 1) * Values.PIXEL_BOX;
			float presentTruncation = .05f;
			Vector2[] points = new Vector2[] {
					//making a truncated square
				    new Vector2(-(boxWidth / 2) + (boxWidth * presentTruncation), boxHeight / 2),     //top left
					new Vector2((boxWidth / 2) - (boxWidth * presentTruncation), boxHeight / 2),      //top right
					new Vector2(boxWidth / 2, (boxHeight / 2) - (boxHeight * presentTruncation)),     //right top
					new Vector2(boxWidth / 2, -(boxHeight / 2) + (boxHeight * presentTruncation)),    //right bottom
					new Vector2(-(boxWidth / 2) + (boxWidth * presentTruncation), -(boxHeight / 2)),  //bottom left
					new Vector2((boxWidth / 2) - (boxWidth * presentTruncation), -(boxHeight / 2)),   //bottom right
					new Vector2(-(boxWidth / 2), (boxHeight / 2) - (boxHeight * presentTruncation)),  //right top
					new Vector2(-(boxWidth / 2), -(boxHeight / 2) + (boxHeight * presentTruncation)), //right bottom
			};
			shape.set(points);
			def.type = BodyType.DynamicBody;
            def.linearDamping = .7f;
			fixDef.density = 1;
			bod = Values.world.createBody(def);
			add(new MoveableBlock(region, bod));
		}
		
		bod.createFixture(fixDef);
		bodyWalls.add(bod);
	}

	@Override
	public void render(float delta) {
		game.camera.update();
		game.lightCamera.update();
		game.batch.setProjectionMatrix(game.camera.combined);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		renderer.setView(game.camera);
		//renderer.render();
		for (int i = 0; i < map.getLayers().getCount(); i++) {
			if (map.getLayers().get(i).getName().equals("Characters")) {
				game.batch.begin();
				for (int j = 0; j < sprites.size(); j++) {
					sprites.get(j).draw(game.batch);
				}
				if(player.xDown && (TimeUtils.nanoTime() - player.shotTime > 500000000)) {
					player.createProjectile();
				}
				game.batch.end();
				renderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get(i));
			} else {
				MapLayer layer = map.getLayers().get(i);
				if (layer instanceof TiledMapTileLayer) {
					if (layer.getProperties().get("moveable") == null || !Boolean.parseBoolean(layer.getProperties().get("moveable").toString())) {
						game.batch.begin();
						renderer.renderTileLayer((TiledMapTileLayer) layer);
						game.batch.end();
					}
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
		Values.world.step(Gdx.graphics.getDeltaTime(), 10, 10);
	}

	public void onInteraction(Player player) {
		//oh god why what do not touch this
		//holy shit what the fuck
		for (int j = 0; j < objects.getCount(); j++) {
			for (int i = 0; i < objects.getCount(); i++) {
				if (objects.get(i) instanceof RectangleMapObject && 
					((RectangleMapObject) objects.get(i)).getRectangle().contains(player.getX(), player.getY())) {		
					RectangleMapObject rect = (RectangleMapObject) objects.get(i);
					if (rect.getRectangle().overlaps(player.getBoundingRectangle()) && 
						rect.getProperties().get("enabled").equals("true")) {
						int order = 0;
						if (rect.getProperties().get("call_order") != null) {
							order = Integer.parseInt(rect.getProperties().get("call_order").toString());
						}
						if (order == j) {
//**************************ADD PROPERTY PROCESSING HERE**************************
							if (rect.getProperties().get("display_text") != null) {
								System.out.println(rect.getProperties().get("display_text").toString());
							}
							if (rect.getProperties().get("enable_object") != null) {
								objects.get(rect.getProperties().get("enable_object").toString()).getProperties().put("enabled", "true");
							}
							if (rect.getProperties().get("disable_object") != null) {
								objects.get(rect.getProperties().get("disable_object").toString()).getProperties().put("enabled", "false");
							}
							if (rect.getProperties().get("add_coin") != null) {
								player.addCoin(Integer.parseInt(rect.getProperties().get("add_coin").toString()));
							}
							if (rect.getProperties().get("change_map") != null) {
								game.setScreen(new GameScreen(this.game, rect.getProperties().get("change_map").toString()));
							}
//**************************END PROPERTY PROCESSING**************************
						}
					}
				}
			}
		}
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
		if (keycode == Keys.SPACE) {
			onInteraction(player);
		}
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
