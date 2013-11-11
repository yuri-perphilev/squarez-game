package com.zeroage.squarez;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.zeroage.squarez.model.Block;
import com.zeroage.squarez.model.Board;
import com.zeroage.squarez.model.Matrix;

import java.util.List;

public class DissolvingAreaController extends BaseController
{
    private float dissolveProgress = 100;
    private float dissolveTime = 0;
    private List<Board.Area> areas;

    protected DissolvingAreaController(GameController gameController, List<Board.Area> areas)
    {
        super(gameController);
        this.areas = areas;
        Gdx.app.log("SQZ", "Dissolving " + areas.size() + " areas");
        for (Board.Area area : areas) {
            Gdx.app.log("SQZ", String.format("Area x: %d y: %d w: %d h: %d", area.getX(), area.getY(), area.getW(), area.getH()));
        }
    }

    @Override
    public void renderDebug(final ShapeRenderer renderer, float delta)
    {
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

    }

    private boolean isBlockDissolving(int x, int y)
    {
        if (areas != null) {
            Board.Area blockArea = new Board.Area(x, y, 1, 1);
            for (Board.Area area : areas) {
                if (area.contains(blockArea)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void update(float delta)
    {
        dissolveProgress -= delta * 100 / 2; // should dissolve completely in 3 sec
        if (dissolveProgress < 0) {
            removeMyself();
        }
    }
}
