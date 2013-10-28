package com.zeroage.squarez;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.zeroage.squarez.model.*;

public class GameScreen implements Screen
{

    private final float boardX = 0.5f;
    private final float boardY = 2.5f;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private ShapeRenderer debugRenderer = new ShapeRenderer();

    private float viewportWidth;
    private float pixelsPerBlock;
    private float viewportHeight;
    private int boardWidth;
    private int boardHeight;

    private float figureTouchX;
    private float figureTouchY;
    boolean figureTouched = false;
    boolean figureMoving = false;

    private Board board;
    private Figure figure;


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

        board.set(5, 5, new BasicBlock());
        board.set(11, 11, new BasicBlock());
        board.set(3, 13, new BasicBlock());
        board.set(10, 2, new BasicBlock());

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

    }

    private void draw(float delta)
    {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        debugRenderer.setProjectionMatrix(camera.combined);

        drawFrame(delta);
        drawBoard(delta);
        drawFigure(delta);
    }

    private void drawBoard(float delta)
    {
        debugRenderer.begin(ShapeRenderer.ShapeType.Filled);


        board.iterate(new Matrix.Callback()
        {
            @Override
            public void cell(int x, int y, Block block)
            {
                if (block != null) {
                    float blockX = boardX  + x;
                    float blockY = boardY + (boardHeight ) - y - 1;
                    debugRenderer.rect(blockX, blockY, 1, 1);
                }
            }
        });

        debugRenderer.end();


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

    private void drawFrame(float delta)
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
            Vector3 v = unproject(screenX, screenY);
            if (isFigureTouched(v)) {
                figureTouched = true;
                figureTouchX = v.x;
                figureTouchY = v.y;
                Gdx.app.debug("SQZ", String.format("Touch x: %f, y: %f", figureTouchX, figureTouchY));
            }
            return true;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button)
        {
            if (figureTouched) {
                if (figureMoving) {
                    figureMoving = false;
                }
                else {
                    board.rotateFigureRight();
                }

                figureTouched = false;
            }
            return true;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer)
        {
            if (figureTouched || figureMoving) {
                Vector3 v = unproject(screenX, screenY);

                float deltaX = v.x - figureTouchX;
                float deltaY = v.y - figureTouchY;

                Gdx.app.debug("SQZ", String.format("Drag dx: %f, dy: %f", deltaX, deltaY));

                if (deltaX <= -1) {
                    board.moveFigureLeft();
                    figureTouchX = v.x;
                }
                else if (deltaX >= 1) {
                    board.moveFigureRight();
                    figureTouchX = v.x;
                }
                else if (deltaY <= -1) {
                    board.moveFigureUp();
                    figureTouchY = v.y;
                }
                else if (deltaY >= 1) {
                    board.moveFigureDown();
                    figureTouchY = v.y;
                }
                figureMoving = true;
            }
            return true;
        }

        private Vector3 unproject(int screenX, int screenY)
        {
            Vector3 vec = new Vector3(screenX, screenY, 0);
            camera.unproject(vec);
            return vec;
        }
    }

    private boolean isFigureTouched(Vector3 v)
    {
        class FigureTouchDetector implements Matrix.Callback {
            private int figureX;
            private int figureY;
            private float touchX;
            private float touchY;
            private boolean touched = false;

            FigureTouchDetector(int figureX, int figureY, float touchX, float touchY)
            {
                this.figureX = figureX;
                this.figureY = figureY;
                this.touchX = touchX;
                this.touchY = touchY;
            }

            @Override
            public void cell(int x, int y, Block block)
            {
                if (block != null) {
                    float blockX = boardX + figureX + x;
                    float blockY = boardY + (boardHeight - figureY) - y - 1;

                    if (touchX >= blockX && touchX < blockX + 1 &&
                        touchY >= blockY && touchY < blockY + 1) {
                        touched = true;
                    }
                }
            }

            boolean isTouched()
            {
                return touched;
            }
        }

        FigureTouchDetector touchDetector = new FigureTouchDetector(board.getFigureX(), board.getFigureY(), v.x, v.y);
        figure.iterate(touchDetector);
        return touchDetector.isTouched();
    }
}
