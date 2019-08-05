package com.mygdx.game.actor;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.actor.bonus.BaseBonus;
import com.mygdx.game.actor.bonus.BulletAddBonus;
import com.mygdx.game.actor.bonus.LifeBonus;
import com.mygdx.game.actor.bonus.SuperBulletBonus;
import com.mygdx.game.actor.tank.Enemy;
import com.mygdx.game.actor.tank.Player;
import com.mygdx.game.actor.tank.TankActor;

import java.time.Year;
import java.util.Random;

public class BonusSpawner {

    RegionManager manager;

    public BonusSpawner(RegionManager manager) {
        this.manager = manager;
    }

    public BaseBonus spawn(String type, MyGdxGame game, float x, float y) {
        BaseBonus actor = null;
        TextureRegion region = manager.getBonus(type);
        switch (type) {
            case Constants.BONUS_TYPE_LIFE:
                actor = new LifeBonus(region, game);
                actor.setPosition(x, y);
                break;
            case Constants.BONUS_TYPE_SUPER_BULLET:
                actor = new SuperBulletBonus(region, game);
                actor.setPosition(x, y);
                break;
            case Constants.BONUS_TYPE_ADD_BULLET:
                actor = new BulletAddBonus(region, game);
                break;
        }
        return actor;
    }

    public BaseBonus randomSpawn(MyGdxGame game) {
        Random random = new Random();
        int w = (int) (game.getBounds().x) + random.nextInt((int) (game.getBounds().width) - 16);
        int h = (int) (game.getBounds().y) + random.nextInt((int) (game.getBounds().height) - 16);
        return spawn(randType(), game, w, h);
    }

    public String randType() {
        Random random = new Random();
        int i = random.nextInt(Constants.BONUS_TYPE_LIST.length);
        return Constants.BONUS_TYPE_LIST[i];
    }

}
