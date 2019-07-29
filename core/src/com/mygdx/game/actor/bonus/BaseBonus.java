package com.mygdx.game.actor.bonus;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.actor.BaseActor;
import com.mygdx.game.actor.tank.Player;

public abstract class BaseBonus extends BaseActor {

    private TextureRegion region;
    private MyGdxGame game;

    public BaseBonus(TextureRegion region, MyGdxGame game) {
        this.region = region;
        this.game = game;
        setSize(region.getRegionWidth(), region.getRegionHeight());
    }

    public void active() {
        DelayAction delay = Actions.delay(8);
        AlphaAction alpha0 = Actions.alpha(0, 0.3f);
        AlphaAction alpha1 = Actions.alpha(1, 0.3f);

        SequenceAction changeAlpha = Actions.sequence(alpha0, alpha1);

        SequenceAction sequence1 = Actions.sequence(delay,
                Actions.repeat(5, changeAlpha),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        remove();
                        // 这里移除会不会引起数据不同步的问题？
                        game.removeBonus(BaseBonus.this);
                    }
                }));

        addAction(sequence1);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Color tempBatchColor = batch.getColor();
        // 获取演员的 Color
        Color color = getColor();
        // 将演员的 Color 结合 parentAlpha 设置到 batch

        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(region, getX(), getY());
//        batch.setColor(tempBatchColor);

    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public abstract void applyToPlayer(Player player);
}
