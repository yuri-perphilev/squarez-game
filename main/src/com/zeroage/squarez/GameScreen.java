package com.zeroage.squarez;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;

import static java.lang.Math.max;


public class GameScreen implements Screen
{
    private OrthographicCamera camera;
    private SpriteBatch batch;

    private ShapeRenderer renderer = new ShapeRenderer();

    private GameController gameController;
    private TextureAtlas atlas;
    private float pixelsPerBlock;
    private float viewportWidth;
    private float viewportHeight;
    private RenderUtils renderUtils = new RenderUtils();


    @Override
    public void show()
    {
        camera = new OrthographicCamera();
        batch = new SpriteBatch();

        Gdx.input.setInputProcessor(new MyInputProcessor());

        setupView(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void resize(int width, int height)
    {

    }

    private void setupView(int width, int height)
    {
        int w = Math.min(width, height);
        int h = max(width, height);

        viewportWidth = 16;
        pixelsPerBlock = w / viewportWidth;
        viewportHeight = h / pixelsPerBlock;

        camera.setToOrtho(true, viewportWidth, viewportHeight);
        camera.update();

        atlas = new TextureAtlas(Gdx.files.internal("images/textures/textures.pack"));

        gameController = new GameController(viewportWidth, viewportHeight, atlas);
    }

    @Override
    public void render(float delta)
    {
        gameController.update(delta);

        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL10.GL_BLEND);
        Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        renderer.setProjectionMatrix(camera.combined);
        gameController.renderDebug(renderer, delta);
        Gdx.gl.glDisable(GL10.GL_BLEND);
        
        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        gameController.render(batch, delta, renderUtils);
        batch.end();
    }

    @Override
    public void hide()
    {

    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void dispose()
    {
        atlas.dispose();
    }

    private class MyInputProcessor extends InputAdapter
    {
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button)
        {
            Vector3 v = unproject(screenX, screenY);
            gameController.touchDown(v.x, v.y, pointer, button);
            return true;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button)
        {
            Vector3 v = unproject(screenX, screenY);
            gameController.touchUp(v.x, v.y, pointer, button);
            return true;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer)
        {
            Vector3 v = unproject(screenX, screenY);
            gameController.touchDragged(v.x, v.y, pointer);
            return true;
        }

        private Vector3 unproject(int screenX, int screenY)
        {
            Vector3 vec = new Vector3(screenX, screenY, 0);
            camera.unproject(vec);
            return vec;
        }
    }

    public class RenderUtils
    {
        public Rectangle getScissors(Rectangle clipBounds)
        {
            Rectangle scissors = new Rectangle();
            ScissorStack.calculateScissors(camera, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), batch.getTransformMatrix(), clipBounds, scissors);
            return scissors;
        }
    }
}
