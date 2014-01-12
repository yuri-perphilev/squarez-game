package com.zeroage.squarez.model;

public class SteelPyramid extends AbstractBlock
{
    @Override
    public boolean canDissolve()
    {
        return false;
    }

    @Override
    public Block dissolve()
    {
        return this;
    }

    @Override
    public BlockType getType()
    {
        return BlockType.STEEL_PYRAMID;
    }

    @Override
    public String toString()
    {
        return "X";
    }
}
