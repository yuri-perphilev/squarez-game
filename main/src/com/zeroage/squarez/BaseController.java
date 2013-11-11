package com.zeroage.squarez;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class BaseController implements Controller
{
    private GameController gameController;

    protected BaseController(GameController gameController)
    {
        this.gameController = gameController;
    }

    public GameController getGameController()
    {
        return gameController;
    }

    @Override
    public void renderDebug(ShapeRenderer renderer, float delta)
    {

    }

    @Override
    public void render(float delta)
    {

    }

    @Override
    public void update(float delta)
    {

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

    public void removeMyself()
    {
        gameController.removeController(this);
    }
}
