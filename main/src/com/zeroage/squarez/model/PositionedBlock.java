package com.zeroage.squarez.model;

public class PositionedBlock
{
    private final Block block;
    private final int x;
    private final int y;

    public PositionedBlock(Block block, int x, int y)
    {
        this.block = block;
        this.x = x;
        this.y = y;
    }

    public Block getBlock()
    {
        return block;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }
}
