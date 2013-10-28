package com.zeroage.squarez.model;

import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

public class StickyTest extends BaseTest
{
    @Test
    public void testEmptyCell() throws Exception
    {
        board.getFigure().clear();
        board.getFigure().set(0, 2, BlockType.STICKY.make());

        board.set(0, 0, BlockType.BASIC.make());

        board.moveFigureUp();

        assertThat(board.getFigureX(), is(0));
        assertThat(board.getFigureY(), is(-2));

        assertThatBlockHasType(board.getFigure().get(0, 2), BlockType.BASIC);
        assertThatBlockHasType(board.get(0, 0), BlockType.EMPTY);
    }
}
