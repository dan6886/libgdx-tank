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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.actor.*;
import com.mygdx.game.actor.bonus.BaseBonus;
import com.mygdx.game.actor.tank.Enemy;
import com.mygdx.game.actor.tank.Player;
import com.mygdx.game.actor.tank.TankActor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;
    private OrthographicCamera camera;
    private TmxMapLoader loader;
    private OrthogonalTiledMapRenderer renderer;
    private TiledMap map;
    private List<MapObject> brickList = new ArrayList<MapObject>();
    private List<TiledMapTileLayer.Cell> brick2List = new ArrayList<TiledMapTileLayer.Cell>();
    private Player player;
    private Stage stage;
    TiledMapTileLayer mapLayer;
    public Pool<Bullet> pool;
    public List<Bullet> runningBullet = new ArrayList<>();
    private List<Enemy> enemyList = new ArrayList<>();
    private List<BaseBonus> bonusList = new ArrayList<>();
    private Viewport viewport;
    private MapObject base;
    private int width = 640;
    private int height = 480;
    private Texture bonus;
    private TankSpawner tankSpawner;
    private RegionManager regionManager;
    private BonusSpawner bonusSpawner;

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("tank_atlas.png");
        bonus = new Texture("bonus.png");

        regionManager = new RegionManager(img, bonus);
        tankSpawner = new TankSpawner(regionManager);
        bonusSpawner = new BonusSpawner(regionManager);
        final TextureRegion bonus_1 = new TextureRegion(bonus, 0, 0, 16, 16);
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.position.set(width / 2, height / 2, 0);

        camera.update();
        loader = new TmxMapLoader();
        map = loader.load("110.tmx");


        viewport = new FitViewport(640, 480, camera);
        renderer = new OrthogonalTiledMapRenderer(map);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        init();

    }

    public BaseBonus spawnBonus() {
        BaseBonus bonus = bonusSpawner.randomSpawn(this);
        bonusList.add(bonus);
        stage.addActor(bonus);
        bonus.active();
        return bonus;
    }

    public void removeBonus(BaseBonus bonus) {
        this.bonusList.remove(bonus);
    }

    private void init() {
        spawnBonus();
        player = (Player) tankSpawner.spawn(Constants.TANK_TYPE_PLAYER, this, 60, 320);
        stage.addActor(player);

        mapLayer = (TiledMapTileLayer) map.getLayers().get("wall");

        MapObjects iron_objects = map.getLayers().get("iron").getObjects();
        for (MapObject object : iron_objects) {
            brickList.add(object);
        }
        MapObjects clay_objects = map.getLayers().get("clay").getObjects();
        for (MapObject object : clay_objects) {
            brickList.add(object);
        }
        MapObjects base_objects = map.getLayers().get("base").getObjects();
        for (MapObject object : base_objects) {
            base = object;
        }
//        TiledMapUtils.removeTiled(map, "wall", brickList.get(0));
        pool = new Pool<Bullet>(8) {
            @Override
            protected Bullet newObject() {
                System.out.println("新建了一个对象");
                return new Bullet(regionManager.getBullet());
            }
        };
        Timer timer = new Timer();
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                spawnEnemy();
            }
        }, 2, 2, 10);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private void spawnEnemy() {
        Enemy enemy = (Enemy) (tankSpawner.randomSpawn( this));
        enemy.setBonus(true);
        stage.addActor(enemy);
        enemy.startAttack();
        enemyList.add(enemy);
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
                System.out.println("玩家和砖块碰撞");
            }
        }

        for (Bullet bullet : runningBullet) {
            if (!bullet.isActive()) {
                continue;
            }
            if (Bullet.ENEMY_BULLET.equals(bullet.getType())) {
                // 敌人子弹
                if (CollisionUtils.isCollision(bullet.getRectangle(), player.getRectangle())) {
                    // 敌人子弹，子弹命中玩家
                    bullet.recycle();
                    // 命中玩家
                    continue;
                }
            } else if (Bullet.PLAYER_BULLET.equals(bullet.getType())) {
                // todo  玩家子弹 这里有敌人的检测，先保留
                boolean isHitEnemy = false;
                Iterator<Enemy> iterator = enemyList.iterator();
                while (iterator.hasNext()) {
                    Enemy enemy = iterator.next();
                    if (CollisionUtils.isCollision(bullet.getRectangle(), enemy.getRectangle())) {
                        enemy.hitted(bullet);
                        if (enemy.isDie()) {
                            iterator.remove();
                        }
                        bullet.recycle();
                        isHitEnemy = true;
                        // 这里子弹检测一次就跳出
                        break;
                    }
                }

                if (isHitEnemy) {
                    continue;
                }

            }

            // 子弹打中墙面
            Iterator<MapObject> iterator = brickList.iterator();
            while (iterator.hasNext()) {
                MapObject brick = iterator.next();
                boolean collision = CollisionUtils.isCollision(
                        CollisionUtils.getRectangle(brick),
                        bullet.getRectangle());
                if (collision) {
                    // 子弹打中墙面，这里要根据子弹类型来判断墙体是否消失
                    if (bulletHitWall(bullet, brick)) {
                        iterator.remove();
                    }
                    bullet.recycle();
                }

            }

            // 基地
            boolean collision = CollisionUtils.isCollision(bullet.getRectangle(), CollisionUtils.getRectangle(base));
            if (collision) {
                bullet.recycle();
                System.out.println("基地被攻击了");
            }

        }

        checkBonus();
    }

    private boolean bulletHitWall(Bullet bullet, MapObject brick) {
        boolean isDestoryWall = false;
        String type = brick.getProperties().get("type", String.class);
        if (bullet.getDamage() == Bullet.BULLET_LEVEL1) {
            // 普通子弹
            if ("clay".equals(type)) {
                // 销毁对应砖块
                TiledMapUtils.removeTiled(map, "wall", brick);
                isDestoryWall = true;
            }
        } else if (bullet.getDamage() == Bullet.BULLET_LEVEL2) {
            // 强力子弹，销毁对应砖块
            TiledMapUtils.removeTiled(map, "wall", brick);
            isDestoryWall = true;
        }
        return isDestoryWall;
    }

    public boolean isCanMove(Actor actor, int direction) {
        Rectangle rectangle = CollisionUtils.getRectangle(actor);
        int w = 1;
        int offset = 0;
        switch (direction) {
            case Input.Keys.W:
                rectangle = new Rectangle(rectangle.x, rectangle.y + rectangle.height + offset, rectangle.width, w);
//                rectangle.merge(rectangle.x, rectangle.y + rectangle.height + 1);
                break;
            case Input.Keys.S:
                rectangle = new Rectangle(rectangle.x, rectangle.y - offset - w, rectangle.width, w);
//                rectangle.merge(rectangle.x, rectangle.y - 1);
                break;
            case Input.Keys.A:
                rectangle = new Rectangle(rectangle.x - offset - w, rectangle.y, w, rectangle.height);
//                rectangle.merge(rectangle.x - 1, rectangle.y);
                break;
            case Input.Keys.D:
                rectangle = new Rectangle(rectangle.x + rectangle.width + offset, rectangle.y, w, rectangle.height);
//                rectangle.merge(rectangle.x + rectangle.width + 1, rectangle.y);
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
        renderer.setView((OrthographicCamera) stage.getCamera());
        renderer.render();

        stage.act();
        stage.draw();
        checkCollision(mapLayer);
        checkDisposeBullet();
//        System.out.println(pool.getFree());
    }

    private void checkBonus() {
        Iterator<BaseBonus> iterator = bonusList.iterator();
        while (iterator.hasNext()) {
            BaseBonus bonus = iterator.next();
            Rectangle rectangle1 = bonus.getRectangle();
            Rectangle rectangle2 = player.getRectangle();
            boolean collision = CollisionUtils.isCollision(rectangle1, rectangle2);
            if (collision) {
                player.onBonus(bonus);
                bonus.remove();
                iterator.remove();
                break;
            }
        }
    }

    private void checkDisposeBullet() {
        List<Bullet> list = new ArrayList<>();
        for (Bullet bullet : runningBullet) {
            if (!bullet.isActive()) {
                bullet.remove();
                list.add(bullet);
                pool.free(bullet);
                continue;
            }
            boolean outSpace = bullet.getX() < 0 || bullet.getX() > 640 | bullet.getY() < 0 || bullet.getY() > 600;
            if (outSpace) {
                bullet.recycle();
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
