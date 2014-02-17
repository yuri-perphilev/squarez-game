package com.zeroage.squarez.model;

import java.util.ArrayList;
import java.util.List;

public class AcidContainer extends AbstractBlock implements PostActionBlock
{
    private int x;
    private int y;
    private float delay;

    @Override
    public BlockType getType()
    {
        return BlockType.ACID_CONTAINER;
    }

    @Override
    public boolean actsOnExplode()
    {
        return true;
    }

    @Override
    public void act(int x, int y, Board board, float delay)
    {
        this.x = x;
        this.y = y;
        this.delay = delay;
    }

    @Override
    public void postAction(Board board)
    {
        List<PositionedBlock> blocks = new ArrayList<PositionedBlock>();
        for (int bx = x - 1; bx < x + 2; bx++) {
            for (int by = y - 1; by < y + 2; by++) {
                if (board.get(bx, by) == null) {
                    Acid acid = new Acid();
                    board.set(bx, by, acid);
                    blocks.add(new PositionedBlock(acid, bx, by));
                }
            }
        }

        GameCallbacks callbacks = board.getCallbacks();
        if (callbacks != null) {
            callbacks.releaseAcid(x, y, blocks, delay);
        }
    }

    @Override
    public String toString()
    {
        return "A";
    }
}
