package com.zeroage.squarez.model;

import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

public class SplodgeTest extends BaseTest
{
    @Test
    public void testSplodgeArea() throws Exception
    {
        board.fill(0, 0, 3, 3, BlockType.SPLODGE);

        Figure fig = board.getFigure();

        fig.fill(0, 0, 3, 3, BlockType.EMPTY);

        fig.set(0, 2, BlockFactory.make(BlockType.STICKY));
        fig.set(1, 2, BlockFactory.make(BlockType.BASIC));
        fig.set(2, 2, BlockFactory.make(BlockType.STEEL_PYRAMID));

        fig.set(0, 1, BlockFactory.make(BlockType.CRACKED));

        board.moveFigureUp();

        assertThat(board.getFigureX(), is(0));
        assertThat(board.getFigureY(), is(-2));

        assertThatBlockHasType(board.get(0, 0), BlockType.STICKY);
        assertThatBlockHasType(board.get(1, 0), BlockType.BASIC);
        assertThatBlockHasType(board.get(2, 0), BlockType.STEEL_PYRAMID);

        assertThat(fig.get(0, 2), nullValue());
        assertThat(fig.get(1, 2), nullValue());
        assertThat(fig.get(2, 2), nullValue());

        board.moveFigureUp();

        assertThat(board.getFigureX(), is(0));
        assertThat(board.getFigureY(), is(-1));

        assertThatBlockHasType(board.get(0, 0), BlockType.CRACKED);

    }
}
