package com.zeroage.squarez;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.zeroage.squarez.model.*;

import static com.badlogic.gdx.math.MathUtils.round;
import static java.lang.Math.abs;
import static java.lang.Math.max;

@SuppressWarnings("SuspiciousNameCombination")
public class FigureController extends BaseController
{
    private float figureTouchX;
    private float figureTouchY;
    boolean figureTouched = false;
    boolean figureMoving = false;

    public static final float FIGURE_TOUCH_DELTA = 0.5f; // board units

    protected FigureController(GameController gameController)
    {
        super(gameController);
    }

    @Override
    public void renderDebug(final ShapeRenderer debugRenderer, float delta)
    {
/*
        Board b = getGameController().getBoard();
        final Rectangle r = getGameController().getBoardRectangle();

        Color figColor = figureTouched ? new Color(0, 1, 0.5f, 0.9f) : new Color(0.2f, 0.2f, 0.2f, 0.9f);
        renderFigure(debugRenderer, r, b.getFigure(), figColor, b.getFigureX(), b.getFigureY());

        if (!b.figureInPocket()) {
            Color nextFigColor = new Color(0.3f, 0.3f, 0.3f, 0.9f);
            renderFigure(debugRenderer, r, b.getNextFigure(), nextFigColor, b.getNextFigureX(), b.getNextFigureY());
        }
*/
    }

    @Override
    public void render(SpriteBatch batch, float delta, GameScreen.RenderUtils renderUtils)
    {
        Rectangle scissors = renderUtils.getScissors(getGameController().getSceneRectangle());
        ScissorStack.pushScissors(scissors);

        Board b = getGameController().getBoard();

        renderFigure(batch, b.getFigure(), b.getFigureX(), b.getFigureY());

        if (!b.figureInPocket()) {
            renderFigure(batch, b.getNextFigure(), b.getNextFigureX(), b.getNextFigureY());
        }

        batch.flush();
        ScissorStack.popScissors();
    }

    private void renderFigure(final ShapeRenderer renderer, final Rectangle r, Figure figure, Color color, final int figureX, final int figureY)
    {
        renderer.begin(ShapeRenderer.ShapeType.Filled);

        renderer.setColor(color);

        figure.iterate(new Matrix.Callback()
        {
            @Override
            public void cell(int x, int y, Block block)
            {
                if (block != null) {
                    Vector2 pos = getGameController().toGameCoords(figureX + x, figureY + y, 1);
                    renderer.rect(pos.x, pos.y, 1, 1);
                }
            }
        });

        renderer.end();
    }
    private void renderFigure(final SpriteBatch batch,
                              Figure figure,
                              final int figureX,
                              final int figureY)
    {

        figure.iterate(new Matrix.Callback()
        {
            @Override
            public void cell(int x, int y, Block block)
            {
                getGameController().drawBlock(batch, block, figureX + x, figureY + y);
            }
        });
    }

    @Override
    public void touchDown(float x, float y, int pointer, int button)
    {
        boolean nextFigureTouched = isNextFigureTouched(x, y);
        if (nextFigureTouched) {
            getGameController().nextFigureTouchedOrTimeout();
        }
        if (isFigureTouched(x, y) || nextFigureTouched) {
            figureTouched = true;
            figureTouchX = x;
            figureTouchY = y;
        }
    }

    @Override
    public void touchUp(float x, float y, int pointer, int button)
    {
        if (figureTouched) {
            if (figureMoving) {
                figureMoving = false;
            }
            else {
                getGameController().getBoard().rotateFigureLeft();
            }

            figureTouched = false;
        }
    }

    @Override
    public void touchDragged(float x, float y, int pointer)
    {
        if (figureTouched || figureMoving) {
            if (moveFigureTo(x, y)) {
                figureTouchX = x;
                figureTouchY = y;
            }

            figureMoving = true;
        }
    }

    public void stopMoving()
    {
        figureTouched = false;
        figureMoving = false;
    }

    private boolean moveFigureTo(float x, float y)
    {
        float deltaX = x - figureTouchX;
        float deltaY = y - figureTouchY;

        Vector3 from = new Vector3(MathUtils.floor(figureTouchX), MathUtils.floor(figureTouchY), 0);
        Vector3 to = new Vector3(MathUtils.floor(x), MathUtils.floor(y), 0);


        long steps = round(max(abs(deltaX), abs(deltaY)));
        float dx = (to.x - from.x) / steps;
        float dy = (to.y - from.y) / steps;

        float oldX = from.x;
        float oldY = from.y;

        if (steps > 0) {
            for (int i = 0; i < steps; i++) {
                float newX = oldX + dx;
                float newY = oldY + dy;
                moveFigure(round(newX) - round(oldX), round(newY) - round(oldY));

                oldX = newX;
                oldY = newY;
            }
        }
        return steps > 0;
    }

    private void moveFigure(int dx, int dy)
    {
        Board board = getGameController().getBoard();
        if (dx > 0) {
            board.moveFigureRight();
        }
        else if (dx < 0) {
            board.moveFigureLeft();
        }
        if (dy > 0) {
            board.moveFigureDown();
        }
        else if (dy < 0) {
            board.moveFigureUp();
        }
    }

    private boolean isFigureTouched(float x, float y)
    {
        Board board = getGameController().getBoard();
        FigureTouchDetector touchDetector = new FigureTouchDetector(board.getFigureX(), board.getFigureY(), x, y);
        board.getFigure().iterate(touchDetector);
        return touchDetector.isTouched();
    }

    private boolean isNextFigureTouched(float x, float y)
    {
        Board board = getGameController().getBoard();
        FigureTouchDetector touchDetector = new FigureTouchDetector(board.getNextFigureX(), board.getNextFigureY(), x, y);
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
                Vector2 pos = getGameController().toGameCoords(figureX + x, figureY + y, 1);

                if (touchX >= pos.x - FIGURE_TOUCH_DELTA && touchX <= pos.x + 1 + FIGURE_TOUCH_DELTA &&
                    touchY >= pos.y - FIGURE_TOUCH_DELTA && touchY <= pos.y + 1 + FIGURE_TOUCH_DELTA) {
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
