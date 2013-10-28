package com.zeroage.squarez.model;

import org.junit.Before;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class BaseTest
{
    public static final int BOARD_WIDTH = 15;
    public static final int BOARD_HEIGHT = 15;
    public static final int FIGURE_SIZE = 3;

    public static final boolean DEBUG = true;

    protected Board board;
    protected Figure figure;

    @Before
    public void setUp() throws Exception
    {
        board = new Board(BOARD_WIDTH, BOARD_HEIGHT);
        figure = new Figure(FIGURE_SIZE);

        board.put(figure);
    }

    public void assertThatBoardIsEmpty()
    {
        assertThat(board.isFilledWith(0, 0, BoardTest.BOARD_WIDTH, BoardTest.BOARD_HEIGHT, BlockType.EMPTY), is(true));
    }

    public void assertThatBlockHasType(Block block, BlockType blockType)
    {
        assertThat("Block is of type " + blockType, blockType.isOfType(block), is(true));
    }

    public void printBoard()
    {
        if (DEBUG) {
            System.out.println(board);
        }
    }
}
