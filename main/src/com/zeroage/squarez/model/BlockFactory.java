package com.zeroage.squarez.model;

import java.util.Random;

public class BlockFactory
{
    private static final BlockType[] EXTRA_BLOCKS = {
            BlockType.BOMB, BlockType.CRACKED, BlockType.MISSILE, BlockType.SHIELD, BlockType.STEEL_PYRAMID, BlockType.STICKY,
            BlockType.SPLODGE_CONTAINER, BlockType.ACID_CONTAINER, BlockType.WALL_MOVE,
    };

    public static final int EXTRA_BLOCK_RATE = 10;

    private static Random random = new Random();

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
            case WALL_MOVE:
                return new WallMove();
            default:
                return null;
        }
    }

    public static Block makeRandomBlock()
    {
        if (prob(EXTRA_BLOCK_RATE)) {
            return makeRandomExtraBlock();
        }
        else {
            return BlockFactory.make(BlockType.BASIC);
        }
    }

    public static Block makeRandomExtraBlock()
    {
        BlockType extraBlock = EXTRA_BLOCKS[random.nextInt(EXTRA_BLOCKS.length)];
        return BlockFactory.make(extraBlock);
    }


    private static boolean prob(int rate)
    {
        return random.nextInt(100) < rate;
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
