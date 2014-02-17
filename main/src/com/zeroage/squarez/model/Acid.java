package com.zeroage.squarez.model;

public class Acid extends AbstractBlock implements Interactable
{
    @Override
    public BlockType getType()
    {
        return BlockType.ACID;
    }

    @Override
    public boolean collidesWith(Block block)
    {
        return false;
    }

    @Override
    public boolean canDissolve()
    {
        return false;
    }

    @Override
    public InteractedBlocks interactWith(Block block, int x, int y, int boardX, int boardY, Board board)
    {
        if (block != null) {
            block.act(boardX, boardY, board, 0f);
            return new InteractedBlocks(null, null);
        }
        else {
            return new InteractedBlocks(this, null);
        }
    }

    @Override
    public boolean isExplosive()
    {
        return false;
    }

    @Override
    public String toString()
    {
        return "&";
    }
}
