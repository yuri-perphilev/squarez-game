package com.zeroage.squarez.model;

import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

public class StickyTest extends BaseTest
{
    @Test
    public void testStickyCellInFigure() throws Exception
    {
        board.getFigure().clear();
        board.getFigure().set(0, 2, BlockFactory.make(BlockType.STICKY));
        board.getFigure().set(0, 1, BlockFactory.make(BlockType.BASIC));

        board.set(0, 0, BlockFactory.make(BlockType.BASIC));

        board.moveFigureUp();

        assertThat(board.getFigureX(), is(0));
        assertThat(board.getFigureY(), is(-2));

        assertThatBlockHasType(board.getFigure().get(0, 2), BlockType.BASIC);
        assertThat("Block on board is absent",  board.get(0, 0), nullValue());

        board.moveFigureUp();

        assertThat(board.getFigureX(), is(0));
        assertThat(board.getFigureY(), is(-1));
    }

    @Test
    public void testStickyCellOnBoard() throws Exception
    {
        board.getFigure().clear();
        board.getFigure().set(0, 2, BlockFactory.make(BlockType.BASIC));
        board.getFigure().set(0, 1, BlockFactory.make(BlockType.BASIC));

        board.set(0, 0, BlockFactory.make(BlockType.STICKY));

        board.moveFigureUp();

        assertThat(board.getFigureX(), is(0));
        assertThat(board.getFigureY(), is(-2));

        assertThat("Block on figure is empty", board.getFigure().get(0, 2), nullValue());
        assertThatBlockHasType(board.get(0, 0), BlockType.BASIC);

        board.moveFigureUp();

        // the second block at 0,1 will not allow to move up

        assertThat(board.getFigureX(), is(0));
        assertThat(board.getFigureY(), is(-2));
    }

}
