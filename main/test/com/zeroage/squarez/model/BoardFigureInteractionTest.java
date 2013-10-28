package com.zeroage.squarez.model;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;


public class BoardFigureInteractionTest extends BaseTest
{
    @Test
    public void testFigurePlacement() throws Exception
    {
        assertThat(board.getFigure(), notNullValue());
        assertThat(board.getFigureX(), is(0));
        assertThat(board.getFigureY(), is(-3));
    }

    @Test
    public void testFigureMoveValid() throws Exception
    {
        assertThat(board.isMoveValid(0, -1, 0, 0), is(true));
        assertThat(board.isMoveValid(0, 0, 0, -1), is(false)); // cannot go back to pocket
        assertThat(board.isMoveValid(0, 0, -1, 0), is(false));
        assertThat(board.isMoveValid(0, 0, 1, 0), is(true));

        assertThat(board.isMoveValid(11, 0, 12, 0), is(true));
        assertThat(board.isMoveValid(12, 0, 13, 0), is(false));

        assertThat(board.isMoveValid(0, 11, 0, 12), is(true));
        assertThat(board.isMoveValid(0, 12, 0, 13), is(false));
    }

    @Test
    public void testMoveOutOfPocket() throws Exception
    {
        // test can move only up
        board.moveFigureDown();
        board.moveFigureLeft();
        board.moveFigureRight();

        assertThat(board.getFigureX(), is(0));
        assertThat(board.getFigureY(), is(-3));

        board.moveFigureUp();
        board.moveFigureUp();
        board.moveFigureUp();

        assertThat(board.getFigureX(), is(0));
        assertThat(board.getFigureY(), is(0));

        // test that figure cannot hide in the pocket

        board.moveFigureDown();

        assertThat(board.getFigureX(), is(0));
        assertThat(board.getFigureY(), is(0));
    }

    @Test
    public void testSimpleCollision() throws Exception
    {
        board.getFigure().clear();
        board.getFigure().set(0, 2, BlockType.BASIC.make());

        board.set(0, 0, BlockType.BASIC.make());

        board.moveFigureUp();

        assertThat(board.getFigureX(), is(0));
        assertThat(board.getFigureY(), is(-3));
    }

    @Test
    public void testSimpleCollision2() throws Exception
    {
        board.getFigure().clear();
        board.getFigure().set(0, 2, BlockType.BASIC.make());

        board.set(0, 1, BlockType.BASIC.make());

        board.moveFigureUp();
        board.moveFigureUp();
        board.moveFigureUp();

        assertThat(board.getFigureX(), is(0));
        assertThat(board.getFigureY(), is(-2));
    }

    @Test
    public void testSimpleCollisionWithRotation() throws Exception
    {
        board.getFigure().clear();
        board.getFigure().set(0, 1, BlockType.BASIC.make());
        board.getFigure().set(1, 1, BlockType.BASIC.make());
        board.getFigure().set(2, 1, BlockType.BASIC.make());

        board.set(0, 0, BlockType.BASIC.make());
        board.set(2, 0, BlockType.BASIC.make());

        board.moveFigureUp();
        board.moveFigureUp();
        board.moveFigureUp();
        board.moveFigureUp();

        assertThat(board.getFigureX(), is(0));
        assertThat(board.getFigureY(), is(-2));

        board.rotateFigureLeft();

        board.moveFigureUp();
        board.moveFigureUp();

        assertThat(board.getFigureX(), is(0));
        assertThat(board.getFigureY(), is(0));
    }

    @Test
    public void testRotateCollision() throws Exception
    {
        board.getFigure().clear();
        board.getFigure().set(1, 0, BlockType.BASIC.make());
        board.getFigure().set(1, 1, BlockType.BASIC.make());
        board.getFigure().set(1, 2, BlockType.BASIC.make());

        board.set(0, 1, BlockType.BASIC.make());
        board.set(2, 1, BlockType.BASIC.make());

        board.moveFigureUp();
        board.moveFigureUp();
        board.moveFigureUp();

        assertThat(board.getFigureX(), is(0));
        assertThat(board.getFigureY(), is(0));

        board.rotateFigureLeft();

        assertThatBlockHasType(board.getFigure().get(1, 0), BlockType.BASIC);
        assertThatBlockHasType(board.getFigure().get(1, 1), BlockType.BASIC);
        assertThatBlockHasType(board.getFigure().get(1, 2), BlockType.BASIC);
        assertThat(board.getFigure().get(0, 1), nullValue());
        assertThat(board.getFigure().get(2, 1), nullValue());
    }

    @Test
    public void testInstallFigureOnBoard() throws Exception
    {
        board.getFigure().fill(0, 0, FIGURE_SIZE, FIGURE_SIZE, BlockType.BASIC);

        board.installFigure();
        assertThat(board.isFilledWith(0, 0, FIGURE_SIZE, FIGURE_SIZE, BlockType.BASIC), is(false));

        board.moveFigureUp();

        board.installFigure();
        assertThat(board.isFilledWith(0, 0, FIGURE_SIZE, FIGURE_SIZE, BlockType.BASIC), is(false));

        board.moveFigureUp();

        board.installFigure();
        assertThat(board.isFilledWith(0, 0, FIGURE_SIZE, FIGURE_SIZE, BlockType.BASIC), is(false));

        board.moveFigureUp();

        board.installFigure();
        assertThat(board.isFilledWith(0, 0, FIGURE_SIZE, FIGURE_SIZE, BlockType.BASIC), is(true));

        board.action();
        assertThat(board.isFilledWith(0, 0, FIGURE_SIZE, FIGURE_SIZE, BlockType.EMPTY), is(true));
    }
}
