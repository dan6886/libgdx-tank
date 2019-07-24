package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.awt.*;

public class CollisionUtils {

    public static boolean isCollision(Rectangle rect1, Rectangle rect2) {
        float x1 = rect1.x, y1 = rect1.y, w1 = rect1.width, h1 = rect1.height;
        float x2 = rect2.x, y2 = rect2.y, w2 = rect2.width, h2 = rect2.height;

//        return x1 < x2 + w2 && x2 < x1 + w1 && y1 < y2 + h2 && y2 < y1 + h1;
        return rect1.intersects(rect2);
    }

    public static boolean isCollision(MapObject object1, MapObject object2) {
        return CollisionUtils.isCollision(
                getRectangle(object1),
                getRectangle(object2));

    }

    public static Rectangle getRectangle(MapObject object) {
        return new Rectangle(
                object.getProperties().get("x", Float.class).intValue(),
                object.getProperties().get("y", Float.class).intValue(),
                object.getProperties().get("width", Float.class).intValue(),
                object.getProperties().get("height", Float.class).intValue());
    }


    public static Rectangle getRectangle(Actor actor) {
        return new Rectangle(
                (int) (actor.getX()),
                (int) (actor.getY()),
                (int) (actor.getWidth()),
                (int) (actor.getHeight()));
    }

    public static int getContactDirection(Vector2[] vector2, Rectangle rectangle) {
        Vector2 v1 = vector2[0];
        Vector2 v2 = vector2[1];

        if (v1.x == v2.x) {
            // 左右碰撞
            if (Math.abs(v1.x - rectangle.x) < Math.abs(v1.x - (rectangle.x + rectangle.width))) {
                // 左
                return Input.Keys.A;
            } else {
                // 右
                return Input.Keys.D;
            }
        } else if (v1.y == v2.y) {
            // 上下碰撞
            if (Math.abs(v1.y - rectangle.y) < Math.abs(v1.y - (rectangle.y + rectangle.height))) {
                // 上
                return Input.Keys.S;
            } else {
                // 下
                return Input.Keys.W;
            }
        }

        return -1;

    }
}
