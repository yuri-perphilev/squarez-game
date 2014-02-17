package com.zeroage.squarez.model;

public class BlockFactory
{
    public static Block make(BlockType type)
    {
        switch (type) {
            case EMPTY:
                return null;
            case BASIC:
                return new BasicBlock();
            case CRACKED:
                return new Cracked();
            case MISSILE:
                return new Missile();
            case BOMB:
                return new Bomb();
            case SHIELD:
                return new Shield();
            case STEEL_PYRAMID:
                return new SteelPyramid();
            case STICKY:
                return new Sticky();
            case SPLODGE:
                return new Splodge();
            case SPLODGE_CONTAINER:
                return new SplodgeContainer();
            case ACID:
                return new Acid();
            case ACID_CONTAINER:
                return new AcidContainer();
            default:
                return null;
        }
    }

    public static Block copy(Block block)
    {
        if (block != null) {
            Block copyBlock = block.copy();
            if (copyBlock == null) {
                copyBlock = make(block.getType());
            }
            return copyBlock;
        }
        return null;
    }
}
