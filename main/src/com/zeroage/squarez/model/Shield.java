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
            return null;
        }
    }

    @Override
    public BlockType getType()
    {
        return BlockType.SHIELD;
    }

    @Override
    public BlockTexture getTexture()
    {
        switch (level) {
            case 3:
                return BlockTexture.SHIELD_THREE;
            case 2:
                return BlockTexture.SHIELD_TWO;
            case 1:
                return BlockTexture.SHIELD_ONE;
            default:
                return null;
        }
    }

    @Override
    public String toString()
    {
        return "" + level;
    }
}
