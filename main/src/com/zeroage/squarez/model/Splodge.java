package com.zeroage.squarez.model;

public class Splodge extends AbstractBlock implements Interactable
{
    @Override
    public BlockType getType()
    {
        return BlockType.SPLODGE;
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
            return new InteractedBlocks(block, null);
        }
        else {
            return new InteractedBlocks(this, null);
        }
    }

    @Override
    public String toString()
    {
        return "~";
    }
}
