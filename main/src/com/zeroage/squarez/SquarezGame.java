package com.zeroage.squarez;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SquarezGame extends Game
{
    private SpriteBatch spriteBatch;
    private Texture texture;
    private Ship ship;
    private EnemyShip enemyShip;

    private long frameTime = System.nanoTime();
    private long lastFrameTime = System.nanoTime();
    private float delta;

    private OrthographicCamera camera;
    private PhysicsSystem physicsSystem;
    private ParticleSystem particleSystem;


/*
    public void resize(int width, int height)
    {
        camera = new OrthographicCamera(320, 480);
        camera.position.set(160, 240, 0); //default
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);
    }
*/

    @Override
    public void create()
    {
        setScreen(new GameScreen());

/*
        physicsSystem = new PhysicsSystem();
        spriteBatch = new SpriteBatch();
        texture = new Texture(Gdx.files.internal("ship.png"));
        ship = new Ship(physicsSystem, texture);

        ParticleSystem.loadTexture(new Texture(Gdx.files.internal("bullet.png")));
        particleSystem = new ParticleSystem(100);

        EnemyShip.loadTexture(new Texture(Gdx.files.internal("enemy.png")));
        enemyShip = new EnemyShip(physicsSystem, particleSystem);
        ShipBullet.loadTexture(new Texture(Gdx.files.internal("bullet.png")));
*/
    }

/*
    @Override
    public void render()
    {
        frameTime = System.nanoTime();
        delta = (frameTime - lastFrameTime) / 1.0E09f;
        lastFrameTime = System.nanoTime();

        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        physicsSystem.update(delta);
        ship.update(delta);
        enemyShip.update(delta);
        particleSystem.update(delta);

        spriteBatch.begin();
        ship.draw(spriteBatch);
        enemyShip.draw(spriteBatch);
        particleSystem.draw(spriteBatch);
        spriteBatch.end();
    }
*/

}
