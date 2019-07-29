package com.mygdx.game;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;



public class TiledMapUtils {
    private TiledMapUtils() {
    }

    public static Vector2 getCellPosition(TiledMapTileLayer layer, int x, int y) {
        float tileHeight = layer.getTileHeight();
        float tileWidth = layer.getTileWidth();
        Vector2 vector2 = new Vector2();
        vector2.x = x * tileWidth;
        vector2.y = y * tileHeight;
        return vector2;
    }

    public static Vector2 convert2LDZPosition(int mapHeight, int cellHeight, int x, int y) {
        Vector2 vector2 = new Vector2();
        vector2.x = x;
        vector2.y = mapHeight * cellHeight - y - cellHeight;
        return vector2;
    }

    /**
     * tiled editor 里面编辑的时候坐标是从左上角开始的
     * libgdx 是从左下角开始的
     * @param map
     * @param layerName
     * @param object
     */
    public static void removeTiled(TiledMap map, String layerName, MapObject object) {
        Rectangle rectangle = CollisionUtils.getRectangle(object);
        TiledMapTileLayer mapLayer = (TiledMapTileLayer) (map.getLayers().get(layerName));
        int width = map.getProperties().get("width", Integer.class);
        int height = map.getProperties().get("height", Integer.class);
        int tilewidth = map.getProperties().get("tilewidth", Integer.class);
        int row = (int) (rectangle.x / tilewidth);
        int col = (int) (rectangle.y / tilewidth);
         mapLayer.getCell(row, col).setTile(null);

        System.out.println("test");
    }
}
