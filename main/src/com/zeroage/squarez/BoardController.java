package com.zeroage.squarez;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.zeroage.squarez.model.Block;
import com.zeroage.squarez.model.Matrix;

public class BoardController extends BaseController
{
    protected BoardController(GameController gameController)
    {
        super(gameController);
    }

    @Override
    public void renderDebug(final ShapeRenderer renderer, float delta)
    {
/*
        final Rectangle r = getGameController().getBoardRectangle();

        renderer.begin(ShapeRenderer.ShapeType.Filled);

        getGameController().getBoard().iterate(new Matrix.Callback()
        {
            @Override
            public void cell(int x, int y, Block block)
            {
                if (block != null) {
                    float blockX = r.x + x;
                    float blockY = r.y + (r.height) - y - 1;
                    renderer.rect(blockX, blockY, 1, 1);
                }
            }
        });

        renderer.end();
*/
    }

    @Override
    public void render(final SpriteBatch batch, float delta, GameScreen.RenderUtils renderUtils)
    {
        getGameController().getBoard().iterate(new Matrix.Callback()
        {
            @Override
            public void cell(int x, int y, Block block)
            {
                getGameController().drawBlock(batch, block, x, y);
            }
        });
    }
}
