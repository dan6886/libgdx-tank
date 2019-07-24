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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
    private Box2DDebugRenderer debugRenderer;
    private World world;

    @Override
    public void create() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
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
        world = new World(new Vector2(0, 0), false);
        debugRenderer = new Box2DDebugRenderer();
        init();

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                System.out.println("begin");
                UserData userDataA = (UserData) contact.getFixtureA().getBody().getUserData();
                UserData userDataB = (UserData) contact.getFixtureB().getBody().getUserData();
                System.out.println(userDataA + "|" + userDataB);
                Vector2[] points = contact.getWorldManifold().getPoints();
                if ("wall".equals(userDataA.name) && "Player".equals(userDataB.name)) {
                    Player player = userDataB.map.get("obj", Player.class);
                    int direction = CollisionUtils.getContactDirection(points, player.getRectangle());
                    player.setForbidden(direction);
                    System.out.println(direction);
                } else if ("wall".equals(userDataB.name) && "Player".equals(userDataA.name)) {
                    Player player = userDataA.map.get("obj", Player.class);
                    int direction = CollisionUtils.getContactDirection(points, player.getRectangle());
                    player.setForbidden(direction);
                    System.out.println(direction);
                }
            }

            @Override
            public void endContact(Contact contact) {
                UserData userDataA = (UserData) contact.getFixtureA().getBody().getUserData();
                UserData userDataB = (UserData) contact.getFixtureB().getBody().getUserData();
                System.out.println(userDataA + "|" + userDataB);
                Vector2[] points = contact.getWorldManifold().getPoints();
                if ("wall".equals(userDataA.name) && "Player".equals(userDataB.name)) {
                    Player player = userDataB.map.get("obj", Player.class);
                    int direction = CollisionUtils.getContactDirection(points, player.getRectangle());
                    player.setPass(direction);
                    System.out.println(direction);
                } else if ("wall".equals(userDataB.name) && "Player".equals(userDataA.name)) {
                    Player player = userDataA.map.get("obj", Player.class);
                    int direction = CollisionUtils.getContactDirection(points, player.getRectangle());
                    player.setPass(direction);
                    System.out.println(direction);
                }
                System.out.println("end");
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
//                System.out.println("presolve");
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
//                System.out.println("postsolve");

            }
        });
    }

    private void init() {

        new Rectangle(10, 10, 100, 10);
        new Rectangle(10, 10, 100, 10);

        TextureRegion region = map.getTileSets().getTileSet(0).getTile(100).getTextureRegion();
        player = new Player(region, this);
        player.setPosition(60, 320);
        stage.addActor(player);
        player.makeBody(world);


        BodyDef bodyGroundDef = new BodyDef();
        bodyGroundDef.type = BodyDef.BodyType.StaticBody;
        bodyGroundDef.position.set(320, 240);
        Body bodyGround = world.createBody(bodyGroundDef);

        PolygonShape shapeGround = new PolygonShape();
        shapeGround.setAsBox(300, 16);
        bodyGround.createFixture(shapeGround, 1);
        bodyGround.setUserData(new UserData.Builder().setName("wall").build());

        mapLayer = (TiledMapTileLayer) map.getLayers().get("wall");

        MapObjects objects = map.getLayers().get("iron").getObjects();
        for (MapObject object : objects) {
            String name = object.getProperties().get("name", String.class);
//            System.out.println(name);
            brickList.add(object);
//            buildBody(object);

        }
//        checkCollision(mapLayer);

    }

    private void buildBody(MapObject object) {
        Rectangle rectangle = CollisionUtils.getRectangle(object);
        BodyDef bodyGroundDef = new BodyDef();
        bodyGroundDef.type = BodyDef.BodyType.StaticBody;
        bodyGroundDef.position.set(rectangle.x, rectangle.y);
        Body bodyGround = world.createBody(bodyGroundDef);

        PolygonShape shapeGround = new PolygonShape();
        shapeGround.setAsBox(rectangle.width, rectangle.height);
        bodyGround.createFixture(shapeGround, 1);
        bodyGround.setUserData(object);
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
//        renderer.setView(camera);
//        renderer.render();

        stage.act();
        stage.draw();
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
//        checkCollision(mapLayer);
        debugRenderer.render(world, camera.combined);
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }


}
