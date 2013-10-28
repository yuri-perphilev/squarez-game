package com.zeroage.squarez.model;

import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

public class SteelPyramidTest extends BaseTest
{
    @Test
    public void testSteelPyramid() throws Exception
    {
        board.fill(1, 1, 3, 3, BlockType.BASIC);
        board.set(3, 3, BlockType.STEEL_PYRAMID.make());

        printBoard();
        assertThat("Not solid area", board.isSolidArea(1, 1, 3, 3), is(false));

        board.action();
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                assertThat("Steel pyramid does not allow area to dissolve", board.get(i, j), notNullValue());
            }
        }

    }

    @Test
    public void testSteelPyramidRemainsAfterDissolve() throws Exception
    {
        board.fill(0, 0, 4, 4, BlockType.BASIC);

        board.set(3, 3, BlockType.STEEL_PYRAMID.make());

        assertThat("Not solid area", board.isSolidArea(0, 0, 4, 4), is(false));
        assertThat("Solid area", board.isSolidArea(0, 0, 3, 3), is(true));

        board.action();

        for (int i = 0; i <= 3; i++) {
            for (int j = 0; j <= 3; j++) {
                if (i == 3 && j == 3) {
                    assertThat(String.format("Board at (%d, %d) is not empty", i, j), board.get(i, j), notNullValue());
                }
                else {
                    assertThat(String.format("Board at (%d, %d) is empty", i, j), board.get(i, j), nullValue());
                }
            }
        }

    }
}
