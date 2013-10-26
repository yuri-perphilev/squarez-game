package com.zeroage.squarez.model;

import java.util.Random;

public class Bomb implements Block
{
    private int diameter = 9;

    @Override
    public boolean canDissolve()
    {
        return true;
    }

    @Override
    public Block dissolve()
    {
        return null;
    }

    @Override
    public BlockType getType()
    {
        return BlockType.BOMB;
    }

    @Override
    public void act(int x, int y, Board board)
    {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            // triangular distribution
            int x1 = x + (int) ((random.nextDouble() + random.nextDouble() + random.nextDouble() + random.nextDouble()) * diameter / 4) - diameter / 2;
            int y1 = y + (int) ((random.nextDouble() + random.nextDouble() + random.nextDouble() + random.nextDouble()) * diameter / 4) - diameter / 2;
            Block block = board.get(x1, y1);
            board.set(x1, y1, null);
            if (BlockType.MISSILE.isOfType(block) || BlockType.BOMB.isOfType(block)) {
                block.act(x1, y1, board);
            }
        }
    }

    @Override
    public void rotateRight()
    {

    }

    @Override
    public void rotateLeft()
    {

    }

    @Override
    public boolean collidesWith(Block block)
    {
        return true;
    }

    @Override
    public String toString()
    {
        return "@";
    }
}
