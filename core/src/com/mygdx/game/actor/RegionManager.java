package com.mygdx.game.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.actor.bonus.BulletAddBonus;
import com.mygdx.game.actor.bonus.LifeBonus;
import com.mygdx.game.actor.bonus.SuperBulletBonus;

import java.util.HashMap;
import java.util.Map;

public class RegionManager {
    Texture atlas;
    Texture bonus;
    private Map<String, TextureRegion> bonusTextureMap = new HashMap<>();

    public RegionManager(Texture atlas, Texture bonus) {
        this.atlas = atlas;
        this.bonus = bonus;
        init();
    }

    private void init() {
        bonusTextureMap.put(Constants.BONUS_TYPE_LIFE, new TextureRegion(bonus, 16 * 6, 0, 16, 16));
        bonusTextureMap.put(Constants.BONUS_TYPE_ADD_BULLET, new TextureRegion(bonus, 16, 16 * 4, 16, 16));
        bonusTextureMap.put(Constants.BONUS_TYPE_SUPER_BULLET, new TextureRegion(bonus, 0, 16 * 4, 16, 16));

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
        return bonusTextureMap.get(type);
    }
}
