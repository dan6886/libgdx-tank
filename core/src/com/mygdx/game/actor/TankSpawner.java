package com.mygdx.game.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.actor.tank.Enemy;
import com.mygdx.game.actor.tank.Player;
import com.mygdx.game.actor.tank.TankActor;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TankSpawner {

    RegionManager manager;
    private int count = 0;
    private Map<String, Vector2> enemySpawnPosition = new HashMap<>();
    private Map<String, Vector2> playerSpawnPosition = new HashMap<>();
    public TankSpawner(RegionManager manager) {
        this.manager = manager;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setEnemySpawnPosition(Map<String, Vector2> positions) {
        this.enemySpawnPosition = positions;
    }

    public void setPlayerSpawnPosition(Map<String, Vector2> playerSpawnPosition) {
        this.playerSpawnPosition = playerSpawnPosition;
    }

    public TankActor spawn(String type, MyGdxGame game, float x, float y) {
        TankActor actor = null;
        switch (type) {
            case Constants.TANK_TYPE_PLAYER:
                actor = makePlayer(game);
                Vector2 v = playerSpawnPosition.get("player");
                actor.setPosition(v.x, v.y);
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
        count--;
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
        int i = random.nextInt(enemySpawnPosition.size());
        System.out.println(i);
        return enemySpawnPosition.get("enemy" + i);
    }
}
