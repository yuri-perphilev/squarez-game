package com.zeroage.squarez.model;

public class Shield extends AbstractBlock
{
    private int level = 3;

    @Override
    public Block dissolve()
    {
        if (level > 1) {
            level--;
            return this;
        }
        else {
            System.out.println("Dissolving!!!");
            return null;
        }
    }

    @Override
    public BlockType getType()
    {
        return BlockType.SHIELD;
    }

    @Override
    public String toString()
    {
        return "" + level;
    }
}
