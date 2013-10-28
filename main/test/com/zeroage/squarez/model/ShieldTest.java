package com.zeroage.squarez.model;

import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

public class ShieldTest extends BaseTest
{
    @Test
    public void testShield() throws Exception
    {
        board.fill(1, 1, 3, 3, BlockType.SHIELD);

        printBoard();

        assertThat("Not solid area", board.isSolidArea(1, 1, 3, 3), is(true));

        board.action();
        assertThat("1-st pass", board.isFilledWith(1, 1, 3, 3, BlockType.SHIELD), is(true));

        printBoard();

        board.action();
        assertThat("2-nd pass", board.isFilledWith(1, 1, 3, 3, BlockType.SHIELD), is(true));

        printBoard();

        board.action();
        printBoard();
        assertThat("3-rd pass", board.isFilledWith(1, 1, 3, 3, BlockType.EMPTY), is(true));
    }

    @Test
    public void testShieldAndBasic() throws Exception
    {
        board.fill(1, 1, 3, 3, BlockType.BASIC);
        board.set(1, 1, new Shield());

        printBoard();

        board.action();

        printBoard();

        board.iterate(new Matrix.Callback()
        {
            @Override
            public void cell(int x, int y, Block block)
            {
                if (x == 1 && y == 1) {
                    assertThat("The only block remains", block, instanceOf(Shield.class));
                }
                else {
                    assertThat("Everything else is clean", block, nullValue());
                }
            }
        });
    }
}
