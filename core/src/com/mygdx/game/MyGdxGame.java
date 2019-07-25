package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.actor.Bullet;
import com.mygdx.game.actor.Enemy;
import com.mygdx.game.actor.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;
    private OrthographicCamera camera;
    private TmxMapLoader loader;
    private OrthogonalTiledMapRenderer renderer;
    private TiledMap map;
    private List<MapObject> brickList = new ArrayList<MapObject>();
    private Player player;
    private Stage stage;
    TiledMapTileLayer mapLayer;
    public Pool<Bullet> pool;
    public List<Bullet> runningBullet = new ArrayList<Bullet>();
    private List<Enemy> enemyList = new ArrayList<>();

    @Override
    public void create() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        batch = new SpriteBatch();
        img = new Texture("tank_atlas.png");
        final TextureRegion b = new TextureRegion(img, 192, 32, 8, 8);
        int width = Gdx.graphics.getWidth();
//        int width = 160;
        int height = Gdx.graphics.getHeight();
//        int height = 160;

        camera = new OrthographicCamera(width, height);
//        camera.setToOrtho(true);
//        camera.setToOrtho(true, 160, 160 * (height * 1.0f / width));
        camera.position.set(width / 2, height / 2, 0);

        camera.update();
        loader = new TmxMapLoader();
        map = loader.load("110.tmx");


        renderer = new OrthogonalTiledMapRenderer(map);

        init();
        pool = new Pool<Bullet>(8) {
            @Override
            protected Bullet newObject() {
                System.out.println("新建了一个对象");
                return new Bullet(b);
            }
        };

    }

    private void init() {
        TextureRegion region = map.getTileSets().getTileSet(0).getTile(100).getTextureRegion();
        player = new Player(region, this);
        player.setPosition(60, 320);
        stage.addActor(player);

        TextureRegion region2 = map.getTileSets().getTileSet(0).getTile(108).getTextureRegion();
        Enemy enemy = new Enemy(region2, this);
        enemy.setPosition(100, 320);
        stage.addActor(enemy);
        enemy.startAttack();
        enemyList.add(enemy);
        mapLayer = (TiledMapTileLayer) map.getLayers().get("wall");

        MapObjects objects = map.getLayers().get("iron").getObjects();
        for (MapObject object : objects) {
            String name = object.getProperties().get("name", String.class);
            brickList.add(object);

        }
        TiledMapUtils.removeTiled(map, "wall", brickList.get(0));
//        checkCollision(mapLayer);

    }

    public Player getPlayer() {
        return player;
    }

    /**
     * libgdx 会自动折算tiledmap 的坐标为左下角的
     */
    private void checkCollision(TiledMapTileLayer layer) {

        for (MapObject brick : brickList) {
            boolean collision = CollisionUtils.isCollision(
                    CollisionUtils.getRectangle(brick),
                    new Rectangle((int) (player.getX()), (int) (player.getY()), 16, 16));
            if (collision) {
                System.out.println(brick.getName());
            }
        }
        for (Bullet bullet : runningBullet) {
            if (Bullet.ENEMY_BULLET.equals(bullet.getType())) {
                // 敌人子弹
                if (CollisionUtils.isCollision(bullet.getRectangle(), player.getRectangle())) {
                    // 敌人子弹，子弹命中玩家
                    bullet.setActive(false);
                    // 命中玩家
                    continue;
                }

            } else if (Bullet.PLAYER_BULLET.equals(bullet.getType())) {
                // todo  玩家子弹 这里有敌人的检测，先保留
                List<Enemy> dieEnemy = new ArrayList<>();
                // 子弹和敌人全部检测
                for (Enemy enemy : enemyList) {
                    if (CollisionUtils.isCollision(bullet.getRectangle(), enemy.getRectangle())) {
                        enemy.hitted(bullet);
                        if (enemy.isDie()) {
                            dieEnemy.add(enemy);
                        }
                        bullet.setActive(false);
                        // 这里子弹检测一次就跳出
                        break;
                    }
                }
                if (!dieEnemy.isEmpty()) {
                    enemyList.removeAll(dieEnemy);
                }
            }
            // 子弹打中墙面
            List<MapObject> hitted = new ArrayList<MapObject>();
            for (MapObject brick : brickList) {
                boolean collision = CollisionUtils.isCollision(
                        CollisionUtils.getRectangle(brick),
                        bullet.getRectangle());
                if (collision) {
                    // 子弹打中墙面，这里要根据子弹类型来判断墙体是否消失
                    hitted.add(brick);
                    TiledMapUtils.removeTiled(map, "wall", brick);
                }
            }
            if (!hitted.isEmpty()) {
                // 确实击中了砖块
                bullet.setActive(false);
                brickList.removeAll(hitted);
            }
        }
    }

    private void bulletHitWall(Bullet bullet, MapObject brick) {
        String type = brick.getProperties().get("type", String.class);
        if (bullet.getDamage() == Bullet.BULLET_LEVEL1) {
            // 普通子弹
            if ("clay".equals(type)) {
                // 销毁对应砖块
            }
        } else if (bullet.getDamage() == Bullet.BULLET_LEVEL2) {
            // 强力子弹，销毁对应砖块

        }
    }

    public boolean isCanMove(Actor actor, int direction) {
        Rectangle rectangle = CollisionUtils.getRectangle(actor);

        switch (direction) {
            case Input.Keys.W:
                rectangle.translate(0, 2);
                break;
            case Input.Keys.S:
                rectangle.translate(0, -2);
                break;
            case Input.Keys.A:
                rectangle.translate(-2, 0);
                break;
            case Input.Keys.D:
                rectangle.translate(2, 0);
                break;
        }


        if (rectangle.x < 0 || rectangle.y < 0 || rectangle.x + rectangle.width > 640 || rectangle.y + rectangle.height > 480) {
            return false;
        }
        for (MapObject brick : brickList) {
            boolean collision = CollisionUtils.isCollision(
                    CollisionUtils.getRectangle(brick),
                    rectangle);
            if (collision) {
                System.out.println("不能移动");
                return false;
            }
        }

        return true;
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		batch.begin();
//		batch.draw(img, 150, 0);
//		batch.end();
//        System.out.println("render");
        renderer.setView(camera);
        renderer.render();

        stage.act();
        stage.draw();
        checkCollision(mapLayer);
        checkDisposeBullet();
        System.out.println(pool.getFree());
    }

    private void checkDisposeBullet() {
        List<Bullet> list = new ArrayList<Bullet>();
        for (Bullet bullet : runningBullet) {
            boolean outSpace = bullet.getX() < 0 || bullet.getX() > 640 | bullet.getY() < 0 || bullet.getY() > 600;
            if (outSpace) {
                bullet.setActive(false);
                bullet.remove();
                list.add(bullet);
                pool.free(bullet);
            } else if (!bullet.isActive()) {
                bullet.remove();
                list.add(bullet);
                pool.free(bullet);
            }
        }
        runningBullet.removeAll(list);

    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }

    @Override
    public void pause() {
        super.pause();
        System.out.println("pause");

    }

    @Override
    public void resume() {
        super.resume();
        System.out.println("resume");
    }
}
