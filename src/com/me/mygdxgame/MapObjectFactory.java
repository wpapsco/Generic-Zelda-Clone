package com.me.mygdxgame;

import box2dLight.Light;
import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by William on 3/31/14.
 */
public class MapObjectFactory {
    public Door Door(MapObject object, Object context) {
        GameScreen screen = (GameScreen) context;
        Rectangle r = ((RectangleMapObject) object).getRectangle();
        TiledMapTile openTile = screen.map.getTileSets().getTileSet(0).getTile(Integer.parseInt((String) object.getProperties().get("opened")));
        TiledMapTile closedTile = screen.map.getTileSets().getTileSet(0).getTile(Integer.parseInt((String) object.getProperties().get("closed")));
        Door door = new Door(
                openTile.getTextureRegion(), closedTile.getTextureRegion(),
                false, new Vector2(r.x * Values.PIXEL_BOX, r.y * Values.PIXEL_BOX), new Vector2(r.getWidth() * Values.PIXEL_BOX, r.getHeight() * Values.PIXEL_BOX), object.getName());
        screen.doors.add(door);
        return door;
    }

    public MapLight MapLight(MapObject object, Object context) {
        GameScreen screen = (GameScreen) context;
        EllipseMapObject ellipse = (EllipseMapObject) object;
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
        //screen.lights.add(new FlickeringLight(Values.handler, 300, lightColor, radius * Values.PIXEL_BOX, centerX * Values.PIXEL_BOX, centerY * Values.PIXEL_BOX, 0, Float.MAX_VALUE));

        return new MapLight(new PointLight(Values.handler, 300, lightColor, radius * Values.PIXEL_BOX, centerX * Values.PIXEL_BOX, centerY * Values.PIXEL_BOX));
    }


}
