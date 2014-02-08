package com.zeroage.squarez.model;

public class Sticky extends AbstractBlock implements Interactable
{
    @Override
    public BlockType getType()
    {
        return BlockType.STICKY;
    }

    @Override
    public boolean collidesWith(Block block)
    {
        return false;
    }

    @Override
    public InteractedBlocks interactWith(Block block, int x, int y, int boardX, int boardY, Board board)
    {
        if (block != null) {
            return new InteractedBlocks(block, null);
        }
        else {
            return new InteractedBlocks(this, null);
        }
    }

    @Override
    public String toString()
    {
        return "=";
    }
}
