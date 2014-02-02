package com.zeroage.squarez;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.zeroage.squarez.model.*;

import java.util.List;

public class DissolvingAreaController extends BaseController
{
    private final List<PositionedBlock> blocks;
    private float dissolveProgress = 100;
    private float dissolveTime = 0;

    // todo: use list of PositionedBlock-s instead of areas
    protected DissolvingAreaController(GameController gameController, List<PositionedBlock> blocks)
    {
        super(gameController);
        this.blocks = blocks;
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
                float blockX = r.x + x;
                float blockY = r.y + (r.height) - y - 1;

                if (isBlockDissolving(x, y)) {
                    renderer.setColor(1, 0, 0, dissolveProgress / 100);
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
        final Color color = batch.getColor();

        batch.setColor(color.r, color.g, color.b, dissolveProgress / 100);
        for (PositionedBlock block : blocks) {
            getGameController().drawBlock(batch, block.getBlock(), block.getX(), block.getY());
        }

        batch.setColor(color);
    }

    @Override
    public boolean update(float delta)
    {
        dissolveProgress -= delta * 100 * 2; // should dissolve completely in 1/2 sec
        return dissolveProgress < 0;
    }
}
