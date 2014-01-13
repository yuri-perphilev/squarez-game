package com.zeroage.squarez.model;

import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

public class MissileTest extends BaseTest
{
    @Test
    public void testMissile() throws Exception
    {
        board.fill(1, 1, 3, 3, BlockType.BASIC);
        board.fill(1, 1, 10, 1, BlockType.BASIC);

        board.set(1, 1, new Missile(Missile.Direction.INC_X));

        printBoard();

        board.action();

        printBoard();

        assertThatBoardIsEmpty();

    }

    @Test
    public void testMissilesCascade() throws Exception
    {
        board.fill(0, 0, 3, 3, BlockType.BASIC);

        board.fill(0, 0, 11, 1, BlockType.BASIC);
        board.fill(0, 10, 11, 1, BlockType.BASIC);
        board.fill(0, 0, 1, 11, BlockType.BASIC);
        board.fill(10, 0, 1, 11, BlockType.BASIC);
        board.fill(5, 0, 1, 11, BlockType.BASIC);
        board.fill(0, 5, 11, 1, BlockType.BASIC);

        printBoard();

        board.set(0, 0, new Missile(Missile.Direction.INC_X));
        board.set(10, 0, new Missile(Missile.Direction.INC_Y));
        board.set(10, 10, new Missile(Missile.Direction.DEC_X));
        board.set(10, 5, new Missile(Missile.Direction.DEC_X));
        board.set(0, 10, new Missile(Missile.Direction.DEC_Y));
        board.set(5, 10, new Missile(Missile.Direction.DEC_Y));

        printBoard();

        board.action();

        printBoard();

        assertThatBoardIsEmpty();
    }

    @Test
    public void testMissileRotation() throws Exception
    {
        figure.clear();
        figure.set(0, 0, new Missile(Missile.Direction.INC_Y));

        figure.rotateLeft();
        assertThat(((Missile) figure.get(0, 2)).getDirection(), is(Missile.Direction.INC_X));
        assertThat(figure.get(0, 2).getTexture(), is(BlockTexture.MISSILE_RIGHT));

        figure.rotateLeft();
        assertThat(((Missile) figure.get(2, 2)).getDirection(), is(Missile.Direction.DEC_Y));
        assertThat(figure.get(2, 2).getTexture(), is(BlockTexture.MISSILE_DOWN));

        figure.rotateLeft();
        assertThat(((Missile) figure.get(2, 0)).getDirection(), is(Missile.Direction.DEC_X));
        assertThat(figure.get(2, 0).getTexture(), is(BlockTexture.MISSILE_LEFT));

        figure.rotateRight();
        assertThat(((Missile) figure.get(2, 2)).getDirection(), is(Missile.Direction.DEC_Y));
        assertThat(figure.get(2, 2).getTexture(), is(BlockTexture.MISSILE_DOWN));

        figure.rotateRight();
        assertThat(((Missile) figure.get(0, 2)).getDirection(), is(Missile.Direction.INC_X));
        assertThat(figure.get(0, 2).getTexture(), is(BlockTexture.MISSILE_RIGHT));

        figure.rotateRight();
        assertThat(((Missile) figure.get(0, 0)).getDirection(), is(Missile.Direction.INC_Y));
        assertThat(figure.get(0, 0).getTexture(), is(BlockTexture.MISSILE_UP));

    }
}
