package com.zeroage.squarez;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
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
    public void render(SpriteBatch batch, float delta)
    {
        Board b = getGameController().getBoard();
        final Rectangle r = getGameController().getBoardRectangle();

        renderFigure(batch, r, b.getFigure(), b.getFigureX(), b.getFigureY());

        if (!b.figureInPocket()) {
            renderFigure(batch, r, b.getNextFigure(), b.getNextFigureX(), b.getNextFigureY());
        }
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
                    float blockX = r.x + figureX + x;
                    float blockY = r.y + (r.height - figureY) - y - 1;
                    renderer.rect(blockX, blockY, 1, 1);
                }
            }
        });

        renderer.end();
    }
    private void renderFigure(final SpriteBatch batch,
                              final Rectangle r,
                              Figure figure,
                              final int figureX,
                              final int figureY)
    {

        figure.iterate(new Matrix.Callback()
        {
            @Override
            public void cell(int x, int y, Block block)
            {
                if (block != null) {
                    float blockX = r.x + figureX + x;
                    float blockY = r.y + (r.height - figureY) - y - 1;
                    TextureRegion texture = getGameController().getTexture(block.getType());
                    if (texture != null) {
                        batch.draw(texture, blockX, blockY, 1, 1);
                    }
                    else {
                        Gdx.app.error("SQZ", "Texture for block " + block.getType() + " not found!");
                    }
                }
            }
        });
    }

    @Override
    public void touchDown(float x, float y, int pointer, int button)
    {
        boolean nextFigureTouched = isNextFigureTouched(x, y);
        if (nextFigureTouched) {
            getGameController().action();
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
        private Rectangle r = getGameController().getBoardRectangle();

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
                float blockX = r.x + figureX + x;
                float blockY = r.y + (r.height - figureY) - y - 1;

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
