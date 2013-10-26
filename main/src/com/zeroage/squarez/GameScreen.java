package com.zeroage.squarez;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.zeroage.squarez.model.Block;
import com.zeroage.squarez.model.Board;
import com.zeroage.squarez.model.Figure;
import com.zeroage.squarez.model.Matrix;

public class GameScreen implements Screen
{

    private final float boardX = 0.5f;
    private final float boardY = 2.5f;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private ShapeRenderer debugRenderer = new ShapeRenderer();

    private float x = 0;
    private float y = 0;
    private float width;
    private float height;
    private float viewportWidth;
    private float pixelsPerBlock;
    private float viewportHeight;
    private int boardWidth;
    private int boardHeight;

    boolean figureTouched = false;

    private Board board;
    private Figure figure;

    private float countdown = 1;

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
        int h = Math.max(width, height);

        viewportWidth = 16;
        pixelsPerBlock = w / viewportWidth;
        viewportHeight = h / pixelsPerBlock;

        boardWidth = Math.round(viewportWidth) - 1;
        boardHeight = Math.round(viewportHeight) - 7; // 2 (score line) + 3 (pocket) + 2 (spacers)

        board = new Board(boardWidth, boardHeight);
        figure = new Figure(3);
        board.put(figure);

        Gdx.app.log("SQZ", String.format("p: %f, vW: %f, vH: %f, bW: %d, bH: %d", pixelsPerBlock, viewportWidth, viewportHeight, boardWidth, boardHeight));

        camera.setToOrtho(true, viewportWidth, viewportHeight);
        camera.update();


    }

    @Override
    public void render(float delta)
    {
        update(delta);
        draw(delta);
    }

    private void update(float delta)
    {
/*
        countdown -= delta;
        if (countdown <= 0) {
            figure = new Figure(3);
            board.put(figure);
            countdown = 5;
        }
*/

        // test figure is touched

        Vector3 vec = new Vector3(x, y, 0);
        camera.unproject(vec);

        final float touchX = vec.x;
        final float touchY = vec.y;

        final int figureX = board.getFigureX();
        final int figureY = board.getFigureY();

        figureTouched = false;
        figure.iterate(new Matrix.Callback()
        {
            @Override
            public void cell(int x, int y, Block block)
            {
                if (block != null) {
                    float blockX = boardX + figureX + x;
                    float blockY = boardY + (boardHeight - figureY) - y - 1;

                    if (touchX >= blockX && touchX < blockX + 1 &&
                        touchY >= blockY && touchY < blockY + 1) {
                        figureTouched = true;
                    }
                }
            }
        });

    }

    private void draw(float delta)
    {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        debugRenderer.setProjectionMatrix(camera.combined);

        drawBoard(delta);
        drawFigure(delta);
    }

    private void drawFigure(float delta)
    {
        debugRenderer.begin(ShapeRenderer.ShapeType.Filled);

        Figure fig = board.getFigure();
        final int figureX = board.getFigureX();
        final int figureY = board.getFigureY();

        if (figureTouched) {
            debugRenderer.setColor(new Color(0, 1, 0.5f, 0.5f));
        }
        else {
            debugRenderer.setColor(new Color(0.2f, 0.2f, 0.2f, 0.5f));
        }

        fig.iterate(new Matrix.Callback()
        {
            @Override
            public void cell(int x, int y, Block block)
            {
                if (block != null) {
                    float blockX = boardX + figureX + x;
                    float blockY = boardY + (boardHeight - figureY) - y - 1;
                    debugRenderer.rect(blockX, blockY, 1, 1);
                }
            }
        });

        debugRenderer.end();
    }

    private void drawBoard(float delta)
    {
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);

        debugRenderer.setColor(new Color(1, 0, 0, 1));

        debugRenderer.rect(boardX, boardY, boardWidth, boardHeight);
        debugRenderer.rect(boardX, boardHeight + boardY, 3, 3);

        for (int i = 0; i < boardWidth; i++) {
            for (int j = 0; j < boardHeight; j++) {
                debugRenderer.rect(boardX + i, boardY + j, 1, 1);
            }
        }

//        debugRenderer.setColor(new Color(0, 0, 1, 0.1f));
//        debugRenderer.rect(vec.x, vec.y, 1, 1);

        debugRenderer.end();
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
            GameScreen.this.x = screenX;
            GameScreen.this.y = screenY;
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button)
        {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer)
        {
            float deltaX = screenX - x;
            float deltaY = screenY - y;

            Gdx.app.log("squarez", String.format("x: %f, y:%f", deltaX, deltaY));
            if (figureTouched) {

                if (deltaX <= -pixelsPerBlock) {
                    board.moveFigureLeft();
                    x = screenX;
                }
                else if (deltaX >= pixelsPerBlock) {
                    board.moveFigureRight();
                    x = screenX;
                }
                else if (deltaY <= -pixelsPerBlock) {
                    board.moveFigureUp();
                    y = screenY;
                }
                else if (deltaY >= pixelsPerBlock) {
                    board.moveFigureDown();
                    y = screenY;
                }
            }
            return true;
        }
    }

    private class MyGestureListener extends GestureDetector.GestureAdapter
    {

        @Override
        public boolean tap(float x, float y, int count, int button)
        {
            GameScreen.this.x = x;
            GameScreen.this.y = y;
            Gdx.app.log("squarez", String.format("x: %f, y:%f", x, y));
            return false;
        }

        @Override
        public boolean pan(float x, float y, float deltaX, float deltaY)
        {
            GameScreen.this.x = x;
            GameScreen.this.y = y;

            if (figureTouched) {
                if (deltaX <= -1) {
                    board.moveFigureLeft();
                }
                else if (deltaX >= 1) {
                    board.moveFigureRight();
                }
                else if (deltaY <= -1) {
                    board.moveFigureUp();
                }
                else if (deltaY >= 1) {
                    board.moveFigureDown();
                }
            }

            Gdx.app.log("squarez", String.format("x: %f, y:%f", deltaX, deltaY));
            return true;
        }
    }
}
