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
    public void testSmallestFigureMoving() {

        board.getFigure().clear();
        board.getFigure().set(1, 1, new BasicBlock());

        board.moveFigureUp();
        board.moveFigureUp();

        assertThat("X", board.getFigureX(), is(0));
        assertThat("Y", board.getFigureY(), is(-1));

        board.moveFigureLeft();

        assertThat("X", board.getFigureX(), is(-1));
        assertThat("Y", board.getFigureY(), is(-1));

        board.moveFigureUp();

        assertThat("X", board.getFigureX(), is(-1));
        assertThat("Y", board.getFigureY(), is(0));

        // move figure by the board perimeter

        for (int i = 0; i < BOARD_HEIGHT + 5; i++) {
            board.moveFigureUp();
        }

        assertThat("X", board.getFigureX(), is(-1));
        assertThat("Y", board.getFigureY(), is(BOARD_HEIGHT - 2));

        for (int i = 0; i < BOARD_WIDTH + 5; i++) {
            board.moveFigureRight();
        }

        assertThat("X", board.getFigureX(), is(BOARD_WIDTH - 2));
        assertThat("Y", board.getFigureY(), is(BOARD_HEIGHT - 2));

        for (int i = 0; i < BOARD_HEIGHT + 5; i++) {
            board.moveFigureDown();
        }

        assertThat("X", board.getFigureX(), is(BOARD_WIDTH - 2));
        assertThat("Y", board.getFigureY(), is(-1));

        for (int i = 0; i < BOARD_WIDTH + 5; i++) {
            board.moveFigureLeft();
        }

        assertThat("X", board.getFigureX(), is(-1));
        assertThat("Y", board.getFigureY(), is(-1));

        board.moveFigureDown();

        assertThat("X", board.getFigureX(), is(-1));
        assertThat("Y", board.getFigureY(), is(-1));
    }

    @Test
    public void testFigureInPocket() throws Exception
    {
        assertThat("Initial figure position is fully in pocket", board.figureInPocket(), is(true));
        board.moveFigureDown();
        assertThat("After moving down figure still in pocket", board.figureInPocket(), is(true));
        board.moveFigureUp();
        assertThat("After moving up 1 time figure still in pocket", board.figureInPocket(), is(true));
        board.moveFigureUp();
        assertThat("After moving up 2 times figure still in pocket", board.figureInPocket(), is(true));
        board.moveFigureUp();
        assertThat("After moving up 3 times figure is out of pocket", board.figureInPocket(), is(false));
        board.moveFigureDown();
        assertThat("Cannot go back to the pocket", board.figureInPocket(), is(false));
    }

    @Test
    public void testSimpleCollision() throws Exception
    {
        board.getFigure().clear();
        board.getFigure().set(0, 2, BlockFactory.make(BlockType.BASIC));

        board.set(0, 0, BlockFactory.make(BlockType.BASIC));

        board.moveFigureUp();

        assertThat(board.getFigureX(), is(0));
        assertThat(board.getFigureY(), is(-3));
    }

    @Test
    public void testSimpleCollision2() throws Exception
    {
        board.getFigure().clear();
        board.getFigure().set(0, 2, BlockFactory.make(BlockType.BASIC));

        board.set(0, 1, BlockFactory.make(BlockType.BASIC));

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
        board.getFigure().set(0, 1, BlockFactory.make(BlockType.BASIC));
        board.getFigure().set(1, 1, BlockFactory.make(BlockType.BASIC));
        board.getFigure().set(2, 1, BlockFactory.make(BlockType.BASIC));

        board.set(0, 0, BlockFactory.make(BlockType.BASIC));
        board.set(2, 0, BlockFactory.make(BlockType.BASIC));

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
        board.getFigure().set(1, 0, BlockFactory.make(BlockType.BASIC));
        board.getFigure().set(1, 1, BlockFactory.make(BlockType.BASIC));
        board.getFigure().set(1, 2, BlockFactory.make(BlockType.BASIC));

        board.set(0, 1, BlockFactory.make(BlockType.BASIC));
        board.set(2, 1, BlockFactory.make(BlockType.BASIC));

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

    @Test
    public void testAction() throws Exception
    {
        board.getFigure().clear();
        board.getFigure().fill(0, 0, 1, FIGURE_SIZE, BlockType.BASIC);
        board.moveFigureUp();
        board.moveFigureUp();
        board.moveFigureUp();

        board.action();

        System.out.println(board);

        board.getFigure().clear();
        board.getFigure().fill(1, 0, 1, FIGURE_SIZE, BlockType.BASIC);
        board.moveFigureUp();
        board.moveFigureUp();
        board.moveFigureUp();

        board.action();

        System.out.println(board);

        board.getFigure().clear();
        board.getFigure().fill(2, 0, 1, FIGURE_SIZE, BlockType.BASIC);
        board.moveFigureUp();
        board.moveFigureUp();
        board.moveFigureUp();

        board.action();

        System.out.println(board);

        assertThat(board.isFilledWith(0, 0, FIGURE_SIZE, FIGURE_SIZE, BlockType.EMPTY), is(true));
    }
}
