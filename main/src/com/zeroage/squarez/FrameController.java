package com.zeroage.squarez;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class FrameController extends BaseController
{
    protected FrameController(GameController gameController)
    {
        super(gameController);
    }

    @Override
    public void renderDebug(ShapeRenderer renderer, float delta)
    {
        Rectangle r = getGameController().getBoardRectangle();

        renderer.begin(ShapeRenderer.ShapeType.Line);

        renderer.setColor(new Color(1, 0, 0, 1));

        renderer.rect(r.x, r.y, r.width, r.height);
        renderer.rect(r.x, r.height + r.y, 3, 3);

        for (int i = 0; i < r.width; i++) {
            for (int j = 0; j < r.height; j++) {
                renderer.rect(r.x + i, r.y + j, 1, 1);
            }
        }

//        debugRenderer.setColor(new Color(0, 0, 1, 0.1f));
//        debugRenderer.rect(vec.x, vec.y, 1, 1);

        renderer.end();
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
}
