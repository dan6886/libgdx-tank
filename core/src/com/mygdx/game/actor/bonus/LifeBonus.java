package com.mygdx.game.actor.bonus;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.actor.tank.Player;

public class LifeBonus extends BaseBonus {

    private int life = 1;

    public LifeBonus(TextureRegion region, MyGdxGame game) {
        super(region,game);
    }

    @Override
    public void applyToPlayer(Player player) {
        player.increaseLife();
    }
}
