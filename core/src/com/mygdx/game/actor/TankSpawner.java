package com.mygdx.game.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.actor.bonus.BaseBonus;
import com.mygdx.game.actor.tank.Enemy;
import com.mygdx.game.actor.tank.Player;
import com.mygdx.game.actor.tank.TankActor;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.Random;

public class TankSpawner {

    RegionManager manager;

    public TankSpawner(RegionManager manager) {
        this.manager = manager;
    }

    public TankActor spawn(String type, MyGdxGame game, float x, float y) {
        TankActor actor = null;
        switch (type) {
            case Constants.TANK_TYPE_PLAYER:
                actor = makePlayer(game);
                actor.setPosition(x, y);
                break;
            case Constants.TANK_TYPE_ENEMY1:
                actor = makeEnemy(type, game);
                actor.setPosition(x, y);
                break;
            default:
                actor = makeEnemy(type, game);
                actor.setPosition(x, y);
                break;

        }
        return actor;
    }

    private Player makePlayer(MyGdxGame game) {
        TextureRegion playerRegion = manager.getPlayer();
        Player player = new Player(playerRegion, game);
        return player;
    }

    private Enemy makeEnemy(String type, MyGdxGame game) {
        TextureRegion playerRegion = manager.getEnemy(type);
        Enemy enemy = new Enemy(playerRegion, game);
        return enemy;
    }

    public TankActor randomSpawn(MyGdxGame game) {
        Random random = new Random();
        int w = random.nextInt(game.getWidth() - 16);
        int h = random.nextInt(game.getHeight() - 16);
        Vector2 v = randPosition();
        System.out.println(v.toString());
        System.out.println(Gdx.graphics.getWidth());
        return spawn(randType(), game, v.x, v.y);
    }

    public String randType() {
        Random random = new Random();
        int i = random.nextInt(Constants.ENEMY_TANK_TYPE_LIST.length);
        return Constants.ENEMY_TANK_TYPE_LIST[i];
    }

    public Vector2 randPosition() {
        Random random = new Random();
        int i = random.nextInt(Constants.ENEMY_SPAWN_POSITION.length);
        return Constants.ENEMY_SPAWN_POSITION[i];
    }
}
