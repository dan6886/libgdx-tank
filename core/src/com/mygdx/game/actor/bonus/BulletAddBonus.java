package com.mygdx.game.actor.bonus;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.actor.Bullet;
import com.mygdx.game.actor.tank.Player;

public class BulletAddBonus extends BaseBonus {

    public BulletAddBonus(TextureRegion region, MyGdxGame game) {
        super(region, game);
    }

    @Override
    public void applyToPlayer(Player player) {
        player.increaseBulletActiveCount();
    }
}
