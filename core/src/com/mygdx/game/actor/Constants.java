package com.mygdx.game.actor;

import com.badlogic.gdx.math.Vector2;

public class Constants {

    private Constants() {
    }

    public static final String BONUS_TYPE_LIFE = "life";
    public static final String BONUS_TYPE_SUPER_BULLET = "superbullet";
    public static final String BONUS_TYPE_ADD_BULLET = "addbullet";
    public static final String[] BONUS_TYPE_LIST = new String[]{BONUS_TYPE_LIFE, BONUS_TYPE_SUPER_BULLET, BONUS_TYPE_ADD_BULLET};

    public static final String TANK_TYPE_PLAYER = "player";
    public static final String TANK_TYPE_ENEMY1 = "enemy1";
    public static final String TANK_TYPE_ENEMY2 = "enemy2";
    public static final String TANK_TYPE_ENEMY3 = "enemy3";
    public static final String TANK_TYPE_ENEMY4 = "enemy4";
    public static final String[] ENEMY_TANK_TYPE_LIST = new String[]{TANK_TYPE_ENEMY1, TANK_TYPE_ENEMY2, TANK_TYPE_ENEMY3, TANK_TYPE_ENEMY4};

    public static final Vector2 LEFTTOP = new Vector2(0, 480 - 32);
    public static final Vector2 RIGHTTOP = new Vector2(640 - 32, 480 - 32);
    public static final Vector2 CENTERTOP = new Vector2(320 - 8, 480 - 32);
    public static final Vector2[] ENEMY_SPAWN_POSITION = new Vector2[]{LEFTTOP, CENTERTOP, RIGHTTOP};
}
