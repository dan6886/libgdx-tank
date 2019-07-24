package com.mygdx.game;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
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

}
