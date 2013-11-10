package com.zeroage.squarez;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class TimerController extends BaseController
{

    private float figureChangeTimer = 0f;

    protected TimerController(GameController gameController)
    {
        super(gameController);
    }

    @Override
    public void renderDebug(ShapeRenderer renderer, float delta)
    {
        float boardWidth = getGameController().getBoardRectangle().width;

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(0.2f, 0.2f, 0.2f, 1);
        renderer.rect(0.5f, 1.6f, boardWidth, 0.4f);
        renderer.setColor(0.6f, 0.6f, 0.6f, 1);
        float w = boardWidth * (1 - figureChangeTimer / GameController.FIGURE_LIFETIME);
        renderer.rect(0.5f, 1.6f, w, 0.4f);
        renderer.end();
    }

    @Override
    public void render(float delta)
    {

    }

    @Override
    public void update(float delta)
    {
        figureChangeTimer += delta;
        if (figureChangeTimer > GameController.FIGURE_LIFETIME) {
            reset();
            getGameController().action();
        }
    }

    @Override
    public void touchDown(float x, float y, int pointer, int button)
    {

    }

    @Override
    public void touchUp(float x, float y, int pointer, int button)
    {

    }

    @Override
    public void touchDragged(float x, float y, int pointer)
    {

    }

    public void reset()
    {
        figureChangeTimer = 0;
    }
}
