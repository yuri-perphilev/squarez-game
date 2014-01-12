package com.zeroage.squarez.model;

public class Sticky extends AbstractBlock
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
}
