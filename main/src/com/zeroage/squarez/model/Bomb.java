package com.zeroage.squarez.model;

import java.util.*;

public class Bomb extends AbstractBlock
{
    private int diameter = 9;

    @Override
    public BlockType getType()
    {
        return BlockType.BOMB;
    }

    @Override
    public void act(int x, int y, Board board, float delay)
    {
        BombCallback bombCallback = null;
        GameCallbacks callbacks = board.getCallbacks();
        if (callbacks != null) {
            bombCallback = callbacks.bomb(x, y);
        }

        Random random = new Random();
        Set<int[]> cellsToExplode = new HashSet<int[]>();
        for (int i = 0; i < 100; i++) {
            // triangular distribution
            int x1 = x + (int) ((random.nextDouble() + random.nextDouble() + random.nextDouble() + random.nextDouble()) * diameter / 4) - diameter / 2;
            int y1 = y + (int) ((random.nextDouble() + random.nextDouble() + random.nextDouble() + random.nextDouble()) * diameter / 4) - diameter / 2;
            if (x1 >= 0 && y1 >= 0 && x1 < board.getWidth() && y1 < board.getHeight()) {
                cellsToExplode.add(new int[]{x1, y1});
            }
        }

        List<PositionedBlock> blocksToExplode = new ArrayList<PositionedBlock>();
        for (int[] b : cellsToExplode) {
            Block block = board.get(b[0], b[1]);
            blocksToExplode.add(new PositionedBlock(block, b[0], b[1]));
            if (block != null && block.isExplosive()) {
                board.set(b[0], b[1], null);
                if (block.actsOnExplode()) {

                    float blastTime = bombCallback != null ? bombCallback.getBlastTime(x, y) : 0;
                    block.act(b[0], b[1], board, delay + blastTime);
                }
            }
        }

        if (bombCallback != null) {
            bombCallback.explode(blocksToExplode, delay);
        }
    }

    @Override
    public String toString()
    {
        return "@";
    }

    @Override
    public boolean actsOnExplode()
    {
        return true;
    }
}
