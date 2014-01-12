package com.zeroage.squarez.model;

public class Cracked extends AbstractBlock implements NeighbourAware
{
    @Override
    public BlockType getType()
    {
        return BlockType.CRACKED;
    }

    @Override
    public String toString()
    {
        return "%";
    }

    @Override
    public NeighbourAction neighbourDissolvedAction()
    {
        return NeighbourAction.DISSOLVE;
    }
}
