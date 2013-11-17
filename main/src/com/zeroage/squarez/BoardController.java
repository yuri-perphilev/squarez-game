package com.zeroage.squarez;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.zeroage.squarez.model.Block;
import com.zeroage.squarez.model.BlockType;
import com.zeroage.squarez.model.Board;
import com.zeroage.squarez.model.Matrix;

import java.util.List;

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
    public void render(final SpriteBatch batch, float delta)
    {
        final Rectangle r = getGameController().getBoardRectangle();

        getGameController().getBoard().iterate(new Matrix.Callback()
        {
            @Override
            public void cell(int x, int y, Block block)
            {
                if (block != null) {
                    float blockX = r.x + x;
                    float blockY = r.y + (r.height) - y - 1;
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
}
