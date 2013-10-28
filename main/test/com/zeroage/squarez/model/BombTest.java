package com.zeroage.squarez.model;

import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

public class BombTest extends BaseTest
{
    @Test
    public void testBomb() throws Exception
    {
        board.fill(0, 0, BOARD_WIDTH, BOARD_HEIGHT, BlockType.STEEL_PYRAMID);
        board.fill(6, 6, 3, 3, BlockType.BASIC);
        board.set(7, 7, new Bomb());

        printBoard();
        board.action();
        printBoard();

        BombVictimCounter callback = new BombVictimCounter();
        board.iterate(callback);

        assertThat(callback.getCounter(), greaterThanOrEqualTo(20));
    }

    @Test
    public void testBombTogglesBomb() throws Exception
    {
        board.fill(0, 0, BOARD_WIDTH, BOARD_HEIGHT, BlockType.STEEL_PYRAMID);
        board.fill(6, 6, 3, 3, BlockType.BASIC);

        for (int i = 0; i < BOARD_WIDTH; i++) {
            board.set(i, i, new Bomb());
            board.set(BOARD_WIDTH - i, i, new Bomb());
            board.set(BOARD_WIDTH / 2, i, new Bomb());
            board.set(i, BOARD_HEIGHT / 2, new Bomb());
            board.set(BOARD_WIDTH - 1, i, new Bomb());
            board.set(i, BOARD_HEIGHT - 1, new Bomb());
            board.set(0, i, new Bomb());
            board.set(i, 0, new Bomb());
        }

        printBoard();
        board.action();
        printBoard();

        assertThatBoardIsEmpty();
    }

    @Test
    public void testTogglesMissile() throws Exception
    {
        board.fill(0, 0, BOARD_WIDTH, BOARD_HEIGHT, BlockType.STEEL_PYRAMID);
        board.fill(6, 6, 3, 3, BlockType.BASIC);
        board.set(7, 7, new Bomb());

        board.set(6, 4, new Missile(Missile.Direction.DEC_Y));
        board.set(7, 4, new Missile(Missile.Direction.DEC_Y));
        board.set(8, 4, new Missile(Missile.Direction.DEC_Y));

        board.set(6, 10, new Missile(Missile.Direction.INC_Y));
        board.set(7, 10, new Missile(Missile.Direction.INC_Y));
        board.set(8, 10, new Missile(Missile.Direction.INC_Y));

        board.set(4, 6, new Missile(Missile.Direction.DEC_X));
        board.set(4, 7, new Missile(Missile.Direction.DEC_X));
        board.set(4, 8, new Missile(Missile.Direction.DEC_X));

        board.set(10, 6, new Missile(Missile.Direction.INC_X));
        board.set(10, 7, new Missile(Missile.Direction.INC_X));
        board.set(10, 8, new Missile(Missile.Direction.INC_X));

        printBoard();
        board.action();
        printBoard();

        BombVictimCounter callback = new BombVictimCounter();
        board.iterate(callback);

        assertThat(callback.getCounter(), greaterThanOrEqualTo(30));
    }

    private static class BombVictimCounter implements Matrix.Callback
    {
        private int counter;

        @Override
        public void cell(int x, int y, Block block)
        {
            if (block == null) {
                counter++;
            }
        }

        public int getCounter()
        {
            return counter;
        }
    }
}
