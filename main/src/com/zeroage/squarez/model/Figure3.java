package com.zeroage.squarez.model;

public class Figure3 extends Figure
{
    private static int[] figures = {
            463, 434, 54, 506, 214, 509, 467, 63,
            495, 411, 94, 510, 159, 443, 57, 47,
            247, 180, 250, 7, 95, 178, 189, 466,
            486, 186, 79, 471, 491, 52, 319, 179
    };

    public static final int EXTRA_BLOCK_RATE = 10;

    private static final BlockType[] EXTRA_BLOCKS = {
            BlockType.BOMB, BlockType.CRACKED, BlockType.MISSILE, BlockType.SHIELD, BlockType.STEEL_PYRAMID
    };

    public Figure3()
    {
        super(3);
    }

    @Override
    public void generate()
    {
        fromCode(figures[random.nextInt(figures.length)]);
        if (prob(50)) {
            flipHorizontally();
        }
        if (prob(50)) {
            flipVertically();
        }
    }

    @Override
    protected Block makeBlock()
    {
        if (prob(EXTRA_BLOCK_RATE)) {
            BlockType extraBlock = EXTRA_BLOCKS[random.nextInt(EXTRA_BLOCKS.length)];
            return extraBlock.make();
        }
        else {
            return BlockType.BASIC.make();
        }
    }

    private boolean prob(int rate)
    {
        return random.nextInt(100) < rate;
    }
}
