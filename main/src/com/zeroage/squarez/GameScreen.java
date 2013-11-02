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

import java.util.Arrays;
import java.util.Random;

public class GameScreen implements Screen
{

    public static final int FIGURE_LIFETIME = 10;
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

    private Vector3[] trail = new Vector3[15];
    private int trailPointer = 0;
    private float trailReduceCounter = 0f;

    private Random random = new Random();

    private float figureChangeTimer = 0f;


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

        // Gdx.app.log("SQZ", String.format("p: %f, vW: %f, vH: %f, bW: %d, bH: %d", pixelsPerBlock, viewportWidth, viewportHeight, boardWidth, boardHeight));

        camera.setToOrtho(true, viewportWidth, viewportHeight);
        camera.update();

        for (int i = 0; i < trail.length; i++) {
            trail[i] = new Vector3(random.nextInt(boardWidth), random.nextInt(boardHeight), 0);
        }
    }

    @Override
    public void render(float delta)
    {
        update(delta);
        draw(delta);
    }

    private void update(float delta)
    {
        figureTimerTick(delta);
        reduceTrail(delta);
    }

    private void figureTimerTick(float delta)
    {
        figureChangeTimer += delta;
        if (figureChangeTimer > FIGURE_LIFETIME) {
            figureChangeTimer = 0;
            action();
        }
    }

    private void reduceTrail(float delta)
    {
        trailReduceCounter += delta;

        if (trailReduceCounter > .02) {
            trailReduceCounter = 0;
            trail[trailPointer] = null;

            trailPointer++;
            if (trailPointer >= trail.length) {
                trailPointer = 0;
            }
        }
    }

    private void updateTrail(Vector3 v)
    {
        trail[trailPointer] = v;
        trailPointer++;
        if (trailPointer >= trail.length) {
            trailPointer = 0;
        }
    }

    private void draw(float delta)
    {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL10.GL_BLEND);
        Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        debugRenderer.setProjectionMatrix(camera.combined);

        drawFrame(delta);
        drawBoard(delta);
        drawFigure(delta);
        drawNextFigure(delta);
        drawTrail(delta);
        drawTimer(delta);
        Gdx.gl.glDisable(GL10.GL_BLEND);
    }

    private void drawTimer(float delta)
    {
        debugRenderer.begin(ShapeRenderer.ShapeType.Filled);
        debugRenderer.setColor(0.2f, 0.2f, 0.2f, 1);
        debugRenderer.rect(0.5f, 1.6f, boardWidth, 0.4f);
        debugRenderer.setColor(0.6f, 0.6f, 0.6f, 1);
        float w = boardWidth * (1 - figureChangeTimer / FIGURE_LIFETIME);
        debugRenderer.rect(0.5f, 1.6f, w, 0.4f);
        debugRenderer.end();
    }

    private void drawTrail(float delta)
    {
        debugRenderer.begin(ShapeRenderer.ShapeType.Filled);

        int ptr = trailPointer;

        float a = .75f;
        float d = (a - .1f) / trail.length;

        for (int i = 0; i < trail.length; i++) {
            ptr--;
            if (ptr < 0) {
                ptr = trail.length - 1;
            }

            Vector3 v = trail[ptr];
            if (v != null) {
                debugRenderer.setColor(1, 1, 1, a);
                debugRenderer.circle(v.x, v.y, .5f, 10);
            }
            a -= d;
        }

        debugRenderer.end();
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
        Figure fig = board.getFigure();

        debugRenderer.begin(ShapeRenderer.ShapeType.Filled);

        final int figureX = board.getFigureX();
        final int figureY = board.getFigureY();

        if (figureTouched) {
            debugRenderer.setColor(new Color(0, 1, 0.5f, 0.9f));
        }
        else {
            debugRenderer.setColor(new Color(0.2f, 0.2f, 0.2f, 0.9f));
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

    private void drawNextFigure(float delta)
    {
        if (board.figureInPocket()) {
            return;
        }

        Figure fig = board.getNextFigure();

        debugRenderer.begin(ShapeRenderer.ShapeType.Filled);

        final int figureX = board.getNextFigureX();
        final int figureY = board.getNextFigureY();

        debugRenderer.setColor(new Color(0.3f, 0.3f, 0.3f, 0.9f));

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

    private void action()
    {
        board.action();
        figureChangeTimer = 0;
        figureTouched = false;
        figureMoving = false;
    }

    private class MyInputProcessor extends InputAdapter
    {
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button)
        {
            Vector3 v = unproject(screenX, screenY);
            boolean nextFigureTouched = isNextFigureTouched(v);
            if (nextFigureTouched) {
                action();
            }
            if (isFigureTouched(v) || nextFigureTouched) {
                figureTouched = true;
                figureTouchX = v.x;
                figureTouchY = v.y;
                //Gdx.app.debug("SQZ", String.format("Touch x: %f, y: %f", figureTouchX, figureTouchY));
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
                    board.rotateFigureLeft();
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

                updateTrail(v);

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
        FigureTouchDetector touchDetector = new FigureTouchDetector(board.getFigureX(), board.getFigureY(), v.x, v.y);
        board.getFigure().iterate(touchDetector);
        return touchDetector.isTouched();
    }

    private boolean isNextFigureTouched(Vector3 v)
    {
        FigureTouchDetector touchDetector = new FigureTouchDetector(board.getNextFigureX(), board.getNextFigureY(), v.x, v.y);
        board.getNextFigure().iterate(touchDetector);
        return touchDetector.isTouched();
    }

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

}
