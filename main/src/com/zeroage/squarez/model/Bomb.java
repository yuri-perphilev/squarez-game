package com.zeroage.squarez.model;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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
        Set<int[]> blocksToExplode = new HashSet<int[]>();
        for (int i = 0; i < 100; i++) {
            // triangular distribution
            int x1 = x + (int) ((random.nextDouble() + random.nextDouble() + random.nextDouble() + random.nextDouble()) * diameter / 4) - diameter / 2;
            int y1 = y + (int) ((random.nextDouble() + random.nextDouble() + random.nextDouble() + random.nextDouble()) * diameter / 4) - diameter / 2;
            blocksToExplode.add(new int[]{x1, y1});
        }

        for (int[] b : blocksToExplode) {
            Block block = board.get(b[0], b[1]);
            board.set(b[0], b[1], null);
            if (BlockType.MISSILE.isOfType(block) || BlockType.BOMB.isOfType(block)) {
                block.act(b[0], b[1], board);
            }
        }

        board.getListener().bomb(blocksToExplode);
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
