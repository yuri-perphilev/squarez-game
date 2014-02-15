package com.zeroage.squarez.model;

import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

public class SplodgeContainerTest extends BaseTest
{
    @Test
    public void testCenterSplodgeContainer() throws Exception
    {
        board.fill(5, 5, 3, 3, BlockType.BASIC);
        board.set(6, 6, new SplodgeContainer());

        board.action();

        assertThat("Splodge is splashed out", board.isFilledWith(5, 5, 3, 3, BlockType.SPLODGE), is(true));
    }

    @Test
    public void testCornerSplodgeContainer() throws Exception
    {
        board.fill(5, 5, 3, 3, BlockType.BASIC);
        board.set(5, 5, new SplodgeContainer());
        board.set(5, 4, new BasicBlock());
        board.set(4, 5, new BasicBlock());

        board.action();

        assertThatBlockHasType(board.get(5, 4), BlockType.BASIC);
        assertThatBlockHasType(board.get(4, 5), BlockType.BASIC);
        assertThatBlockHasType(board.get(5, 5), BlockType.SPLODGE);
        assertThatBlockHasType(board.get(4, 4), BlockType.SPLODGE);
    }
}
