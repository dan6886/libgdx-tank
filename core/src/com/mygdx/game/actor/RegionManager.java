package com.mygdx.game.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class RegionManager {
    Texture atlas;
    Texture bonus;

    public RegionManager(Texture atlas, Texture bonus) {
        this.atlas = atlas;
        this.bonus = bonus;
    }

    public TextureRegion getPlayer() {
        return new TextureRegion(atlas, 0, 64, 16, 16);
    }

    public TextureRegion getEnemy(String type) {
        return new TextureRegion(atlas, 0, 96, 16, 16);

    }

    public TextureRegion getBullet() {
        return new TextureRegion(atlas, 192, 32, 8, 8);
    }

    public TextureRegion getBonus(String type) {
        return new TextureRegion(bonus, 0, 0, 16, 16);
    }
}
