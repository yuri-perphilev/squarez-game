package com.zeroage.squarez.model;

import org.junit.Test;

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

        printBoard();
        board.action();
        printBoard();

        assertThatBoardIsEmpty();
    }
}
