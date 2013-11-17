package com.zeroage.squarez;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
    public void render(SpriteBatch batch, float delta)
    {

    }

    @Override
    public boolean update(float delta)
    {
        return false;
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

    private void loadTextures(TextureAtlas atlas)
    {

    }
}
