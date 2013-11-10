package com.zeroage.squarez;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.zeroage.squarez.model.BasicBlock;
import com.zeroage.squarez.model.Board;

import static java.lang.Math.max;


public class GameScreen implements Screen
{
    private OrthographicCamera camera;
    private SpriteBatch batch;

    private ShapeRenderer renderer = new ShapeRenderer();

    private GameController gameController;


    @Override
    public void show()
    {
        camera = new OrthographicCamera();
        batch = new SpriteBatch();

//        Gdx.input.setInputProcessor(new GestureDetector(new MyGestureListener()));
        Gdx.input.setInputProcessor(new MyInputProcessor());

        setupBoardSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void resize(int width, int height)
    {

    }

    private void setupBoardSize(int width, int height)
    {
        int w = Math.min(width, height);
        int h = max(width, height);

        float viewportWidth = 16;
        float pixelsPerBlock = w / viewportWidth;
        float viewportHeight = h / pixelsPerBlock;

        int boardWidth = Math.round(viewportWidth) - 1;
        int boardHeight = Math.round(viewportHeight) - 7;

        Board board = new Board(boardWidth, boardHeight);

        board.set(5, 5, new BasicBlock());
        board.set(11, 11, new BasicBlock());
        board.set(3, 13, new BasicBlock());
        board.set(10, 2, new BasicBlock());

        camera.setToOrtho(true, viewportWidth, viewportHeight);
        camera.update();

        gameController = new GameController(board, viewportWidth, viewportHeight);
    }

    @Override
    public void render(float delta)
    {
        gameController.update(delta);
        draw(delta);
    }

    private void draw(float delta)
    {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL10.GL_BLEND);
        Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        renderer.setProjectionMatrix(camera.combined);

        gameController.renderDebug(renderer, delta);

        drawBoard(delta);
        Gdx.gl.glDisable(GL10.GL_BLEND);
    }

    private void drawBoard(float delta)
    {
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
}
