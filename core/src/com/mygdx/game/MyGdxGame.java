package com.mygdx.game;

import com.badlogic.gdx.*;
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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.actor.*;
import com.mygdx.game.actor.bonus.BaseBonus;
import com.mygdx.game.actor.tank.Enemy;
import com.mygdx.game.actor.tank.Player;

import java.util.*;
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
    private Stage gameStage;
    private Stage stage2;
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

    public static final int GAME_RUNNING = 1;
    public static final int GAME_ENEMY_WIN = 2;
    public static final int GAME_PLAYER_WIN = 3;
    private int gameState = GAME_RUNNING;
    private int enemyCount = 5;
    private Window window;
    private Timer timer;
    private Rectangle bounds = new Rectangle(0, 0, 320, 320);
    private Map<String, Vector2> enemySpawnPosition = new HashMap<>();
    private Map<String, Vector2> playerSpawnPosition = new HashMap<>();
    private KillCounter counter;

    @Override
    public void create() {
        counter = new KillCounter();
        batch = new SpriteBatch();
        img = new Texture("tank_atlas.png");
        bonus = new Texture("bonus.png");

        regionManager = new RegionManager(img, bonus);
        tankSpawner = new TankSpawner(regionManager);
        tankSpawner.setCount(enemyCount);
        bonusSpawner = new BonusSpawner(regionManager);
        final TextureRegion bonus_1 = new TextureRegion(bonus, 0, 0, 16, 16);
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.position.set(width / 2, height / 2, 0);

        camera.update();
        loader = new TmxMapLoader();
        map = loader.load("110.tmx");
//        map = loader.load("level-2.tmx");


        viewport = new FitViewport(640, 480, camera);
        renderer = new OrthogonalTiledMapRenderer(map);
        gameStage = new Stage(viewport);
        stage2 = new Stage(viewport);
        Gdx.input.setInputProcessor(new InputMultiplexer());
        InputMultiplexer inputProcessor = (InputMultiplexer) Gdx.input.getInputProcessor();
        inputProcessor.addProcessor(stage2);
        init();
    }

    private void startInit() {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                reset();
                map = loader.load("level-2.tmx");
                renderer = new OrthogonalTiledMapRenderer(map);
                init();
            }
        });
    }

    private void reset() {
        counter.clear();
        timer.clear();
        brickList.clear();
        tankSpawner.setCount(enemyCount);
        player.remove();
        player.dispose();
        for (Enemy enemy : enemyList) {
            enemy.remove();
        }
        enemyList.clear();

        for (Bullet b : runningBullet) {
            b.remove();
        }
        runningBullet.clear();

        for (BaseBonus bonus : bonusList) {
            bonus.remove();
        }
        bonusList.clear();

        pool.clear();
        gameState = GAME_RUNNING;
    }

    public BaseBonus spawnBonus() {
        BaseBonus bonus = bonusSpawner.randomSpawn(this);
        bonusList.add(bonus);
        gameStage.addActor(bonus);
        bonus.active();
        return bonus;
    }

    public void removeBonus(BaseBonus bonus) {
        this.bonusList.remove(bonus);
    }

    private void init() {


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

        MapObjects spawn = map.getLayers().get("spawn").getObjects();
        for (MapObject object : spawn) {
            String type = object.getProperties().get("type", String.class);
            Float x = object.getProperties().get("x", Float.class);
            Float y = object.getProperties().get("y", Float.class);
            if (type.contains("enemy")) {
                enemySpawnPosition.put(type, new Vector2(x, y));
            } else if (type.contains("player")) {
                playerSpawnPosition.put(type, new Vector2(x, y));
            }
            System.out.println(type + "|" + x + "|" + y);
        }
        tankSpawner.setEnemySpawnPosition(enemySpawnPosition);
        tankSpawner.setPlayerSpawnPosition(playerSpawnPosition);
        pool = new Pool<Bullet>(8) {
            @Override
            protected Bullet newObject() {
                System.out.println("新建了一个对象");
                return new Bullet(regionManager.getBullet());
            }
        };
        timer = new Timer();
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                spawnEnemy();
            }
        }, 2, 2, enemyCount - 1);
//        showWindow();
        player = (Player) tankSpawner.spawn(Constants.TANK_TYPE_PLAYER, this, 60, 320);
        gameStage.addActor(player);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    private void spawnEnemy() {
        Enemy enemy = (Enemy) (tankSpawner.randomSpawn(this));
        enemy.setBonus(true);
        gameStage.addActor(enemy);
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
                            counter.killOne(enemy);
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
//                gameState = GAME_ENEMY_WIN;
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

        // 检测是否出界
        bounds.contains(rectangle);
//        if (rectangle.x < 0 || rectangle.y < 0 || rectangle.x + rectangle.width > 640 || rectangle.y + rectangle.height > 480) {
//            return false;
//        }
        if (!bounds.contains(rectangle)) {
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
        renderer.setView((OrthographicCamera) gameStage.getCamera());
        renderer.render();
        if (isGameFinish()) {
            stage2.act();
            stage2.draw();
            return;
        }
        stage2.act();
        stage2.draw();
        gameStage.act();
        gameStage.draw();
        checkCollision(mapLayer);
        checkDisposeBullet();
        checkGameFinish();
    }

    private void showWindow() {
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        window = new Window("WindowTest", skin);
        // 默认文字是在左边显示，需要手动设置居中
        window.getTitleLabel().setAlignment(Align.center);
        // 默认window的位置是在左下角，需重新设置
        window.setX(Gdx.graphics.getWidth() / 2 - window.getWidth() / 2);
        window.setY(Gdx.graphics.getHeight() / 2 - window.getHeight() / 2);
        // 拖动TitleLabel，window会移动
        window.setMovable(true);

        Label label = new Label(getTips(), skin);
        label.setX(0);
        label.setY(30);
        TextButton tbOk = new TextButton("OK", skin);
        TextButton tbCancel = new TextButton("CANCEL", skin);
        tbOk.setSize(tbCancel.getPrefWidth(), tbCancel.getPrefHeight());
        tbCancel.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                window.remove();
            }

        });
        tbOk.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("TAG", "dialog ok button is clicked");
                startInit();

            }

        });
        tbOk.setX(window.getPrefWidth() / 2 - tbOk.getWidth() / 2 - 10);
        tbOk.setY(10);
        tbCancel.setX(window.getWidth() / 2 + 10);
        tbCancel.setY(10);
        // 这个地方用addActor方法，不能使用add方法，后面将讲解Table的时候会涉及到
        window.addActor(label);
        window.addActor(tbOk);
        window.addActor(tbCancel);
        Gdx.app.log("TAG", "window preWidth=" + window.getPrefWidth() + "window width=" + window.getWidth());
//      window.pack();
        stage2.addActor(window);

    }

    private String getTips() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hit:").append("\n").append(counter.getKillMessage());

        return sb.toString();
    }

    private boolean isGameFinish() {
        return gameState == GAME_PLAYER_WIN || gameState == GAME_ENEMY_WIN;
    }

    private void checkGameFinish() {

        if (enemyList.isEmpty() && tankSpawner.getCount() == 0) {
            gameState = GAME_PLAYER_WIN;
        }
        if (gameState == GAME_ENEMY_WIN) {
            showWindow();
        }

        if (gameState == GAME_PLAYER_WIN) {
            showWindow();

        }
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
            // 检测子弹是否出界
            if (!bounds.contains(bullet.getRectangle())) {
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
