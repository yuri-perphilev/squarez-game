package com.zeroage.squarez.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

public class CrackedTest extends BaseTest
{
    @Test
    public void testCracked() throws Exception
    {
        board.fill(4, 2, 1, 7, BlockType.CRACKED);
        board.fill(6, 2, 1, 7, BlockType.CRACKED);
        board.fill(2, 4, 7, 1, BlockType.CRACKED);
        board.fill(2, 6, 7, 1, BlockType.CRACKED);
        board.fill(4, 4, 3, 3, BlockType.BASIC);

        MyGameCallbacks callbacks = new MyGameCallbacks();
        board.setCallbacks(callbacks);

        printBoard();
        board.action();
        printBoard();

        assertThatBoardIsEmpty();

        List<PositionedBlock> blocks = callbacks.getDissolvedBlocks();
        assertThat("Dissolved blocks not null", blocks, notNullValue());
        assertThat("There are 16 dissolved blocks", blocks, hasSize(25));
    }

    @Test
    public void testCracked2() throws Exception
    {
        board.fill(2, 2, 7, 7, BlockType.CRACKED);
        board.fill(4, 4, 3, 3, BlockType.BASIC);
        board.set(3, 3, null);
        board.set(3, 5, null);
        board.set(3, 7, null);
        board.set(5, 3, null);
        board.set(5, 7, null);
        board.set(7, 3, null);
        board.set(7, 5, null);
        board.set(7, 7, null);

        MyGameCallbacks callbacks = new MyGameCallbacks();
        board.setCallbacks(callbacks);

        printBoard();
        board.action();
        printBoard();

        assertThatBoardIsEmpty();

        List<PositionedBlock> blocks = callbacks.getDissolvedBlocks();
        assertThat("Dissolved blocks not null", blocks, notNullValue());
        assertThat("There are 41 dissolved blocks", blocks, hasSize(41));
    }

    private static class MyGameCallbacks implements GameCallbacks
    {
        List<PositionedBlock> dissolvedBlocks;

        @Override
        public void dissolving(List<PositionedBlock> blocks)
        {
            this.dissolvedBlocks = new ArrayList<PositionedBlock>(blocks);
        }

        @Override
        public BombCallback bomb(int x, int y)
        {
            return null;
        }

        @Override
        public MissileCallback missile(int fromX, int fromY, int dX, int dY)
        {
            return null;
        }

        public List<PositionedBlock> getDissolvedBlocks()
        {
            return dissolvedBlocks;
        }
    }
}
