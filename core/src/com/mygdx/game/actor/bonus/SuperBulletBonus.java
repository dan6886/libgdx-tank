package com.mygdx.game.actor.bonus;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.actor.Bullet;
import com.mygdx.game.actor.tank.Player;

public class SuperBulletBonus extends BaseBonus {

    private int damage = Bullet.BULLET_LEVEL2;

    public SuperBulletBonus(TextureRegion region, MyGdxGame game) {
        super(region, game);
    }

    @Override
    public void applyToPlayer(Player player) {
        player.setBulletDamage(damage);
    }
}
