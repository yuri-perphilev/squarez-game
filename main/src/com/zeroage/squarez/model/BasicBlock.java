package com.zeroage.squarez.model;

public class BasicBlock extends AbstractBlock
{
    @Override
    public BlockType getType()
    {
        return BlockType.BASIC;
    }

    @Override
    public String toString()
    {
        return "#";
    }
}
