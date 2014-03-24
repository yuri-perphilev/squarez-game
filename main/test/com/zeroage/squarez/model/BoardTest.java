package com.zeroage.squarez.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class BoardTest extends BaseTest {

    @Test
    public void testBoardSingleCell() throws Exception
    {
        assertThat(board, notNullValue());

        board.set(1, 1, BlockFactory.make(BlockType.BASIC));
        assertThatBlockHasType(board.get(1, 1), BlockType.BASIC);
    }

    @Test
    public void testFill() throws Exception {
        board.fill(1, 1, 3, 3, BlockType.BASIC);

        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                if (i >= 1 && i <= 3  && j >= 1 && j <= 3) {
                    assertThatBlockHasType(board.get(i, j), BlockType.BASIC);
                }
                else {
                    assertThat(board.get(i, j), nullValue());
                }
            }
        }
    }

    @Test
    public void testGetSquareArea() throws Exception {
        board.fill(1, 1, 3, 3, BlockType.BASIC);
        board.fill(7, 7, 2, 2, BlockType.BASIC);

        assertThat(board.isSolidArea(1, 1, 3, 3), is(true));
        assertThat(board.isSolidArea(7, 7, 3, 3), is(false));
        assertThat(board.isSolidArea(10, 10, 3, 3), is(false));
    }

    @Test
    public void testGetMaxSolidAreasForSimpleRectangular() throws Exception {
        board.fill(1, 1, 4, 3, BlockType.BASIC);
        board.fill(7, 7, 3, 4, BlockType.BASIC);

        assertThat(board.getMaxSolidArea(1, 1).getArea(), is(12));
    }

    @Test
    public void testGetMaxSolidAreaForIntersectingRectangular() throws Exception {
        board.fill(1, 1, 6, 3, BlockType.BASIC);
        board.fill(1, 1, 3, 5, BlockType.BASIC);

        assertThat(board.getMaxSolidArea(1, 1).getArea(), is(18));
    }

    @Test
    public void testGetMaxSolidAreaForComplexAreaWithHole() throws Exception {
        board.fill(1, 1, 6, 3, BlockType.BASIC);
        board.fill(2, 2, 3, 5, BlockType.BASIC);

        board.set(4, 2, null);

        assertThat(board.getMaxSolidArea(1, 1).getArea(), is(9));
    }

    @Test
    public void testGetMaxSolidAreaForEmptyBoard() throws Exception {
        assertThat(board.getMaxSolidArea(0, 0).getArea(), is(0));
    }

    @Test
    public void testGetMaxSolidAreaForFilledBoard() throws Exception {
        board.fill(0, 0, 15, 15, BlockType.BASIC);
        assertThat(board.getMaxSolidArea(0, 0).getArea(), is(225));
    }

    @Test
    public void testMergeOverlappedAreas() throws Exception
    {
        List<Board.Area> areas = new ArrayList<Board.Area>();
        areas.add(new Board.Area(1, 1, 4, 4));
        areas.add(new Board.Area(2, 2, 3, 3));
        areas = board.mergeAreas(areas);

        assertThat(areas.size(), is(1));
        assertThat(areas.iterator().next().getArea(), is(16));
    }

    @Test
    public void testMergeMultipleAreas() throws Exception
    {
        List<Board.Area> areas = new ArrayList<Board.Area>();
        areas.add(new Board.Area(1, 1, 4, 4));
        areas.add(new Board.Area(2, 2, 3, 3));
        areas.add(new Board.Area(3, 3, 1, 1));
        areas.add(new Board.Area(10, 10, 3, 3));
        areas.add(new Board.Area(10, 10, 3, 3));
        areas.add(new Board.Area(10, 10, 2, 2));
        areas.add(new Board.Area(12, 12, 2, 2));

        areas = board.mergeAreas(areas);

        assertThat(areas, hasSize(3));

        assertThat(new ArrayList<Object>(areas), hasItem(hasProperty("area", equalTo(16))));
        assertThat(new ArrayList<Object>(areas), hasItem(hasProperty("area", equalTo(9))));
        assertThat(new ArrayList<Object>(areas), hasItem(hasProperty("area", equalTo(4))));
    }


    @Test
    public void testFindAllSolidAreas() throws Exception {
        board.fill(1, 1, 3, 3, BlockType.BASIC);
        board.fill(7, 7, 2, 2, BlockType.BASIC);

        List<Board.Area> areas = board.findAreas();
        assertThat(areas, notNullValue());
        assertThat(areas.size(), is(1));
    }

    @Test
    public void testSimpleDissolve() throws Exception {
        board.fill(1, 1, 3, 3, BlockType.BASIC);
        board.fill(7, 7, 2, 2, BlockType.BASIC);

        board.action();

        board.iterate(new Board.Callback()
        {
            @Override
            public void cell(int x, int y, Block block)
            {
                if (x >= 7 && x <= 8  && y >= 7 && y <= 8) {
                    assertThat("Cell (" + x + "," + y + ") must be BLOCK", block, instanceOf(BasicBlock.class));
                }
                else {
                    assertThat("Cell (" + x + "," + y + ") must be empty", block, nullValue());
                }
            }
        });
    }

    @Test
    public void testIntersectingAreasDissolve() throws Exception {
        board.fill(1, 1, 3, 3, BlockType.BASIC);
        board.fill(5, 1, 3, 3, BlockType.BASIC);
        board.fill(1, 5, 3, 3, BlockType.BASIC);
        board.fill(5, 5, 3, 3, BlockType.BASIC);
        board.fill(3, 3, 3, 3, BlockType.BASIC);

        board.action();

        board.iterate(new Board.Callback()
        {
            @Override
            public void cell(int x, int y, Block block)
            {
                assertThat("Cell (" + x + "," + y + ") must be empty", block, nullValue());
            }
        });
    }

    @Test
    public void testIntersectingAreasWithHoleDissolve() throws Exception {
        board.fill(1, 1, 3, 3, BlockType.BASIC);
        board.fill(5, 1, 3, 3, BlockType.BASIC);
        board.fill(1, 5, 3, 3, BlockType.BASIC);
        board.fill(5, 5, 3, 3, BlockType.BASIC);
        board.fill(3, 3, 3, 3, BlockType.BASIC);

        board.set(4, 4, null);

        board.action();

        board.iterate(new Board.Callback()
        {
            @Override
            public void cell(int x, int y, Block block)
            {
                if (x == 4 && y == 3 ||
                    x == 4 && y == 5 ||
                    x == 3 && y == 4 ||
                    x == 5 && y == 4) {
                    assertThat("Cell (" + x + "," + y + ") must be BLOCK", block, instanceOf(BasicBlock.class));
                }
                else {
                    assertThat("Cell (" + x + "," + y + ") must be empty", block, nullValue());
                }
            }
        });
    }

    @Test
    public void testSmallFigureRotationNearWalls() throws Exception
    {
        Figure f = board.getFigure();
        f.clear();
        f.set(0, 1, new BasicBlock());
        f.set(1, 1, new BasicBlock());
        f.set(2, 1, new BasicBlock());

        for (int i = 0; i < board.getHeight() + 3; i++) {
            board.moveFigureUp();
        }

        board.rotateFigureLeft();

        assertThat("Figure rotated 1", f.get(1, 0), notNullValue());
        assertThat("Figure rotated 1", f.get(1, 1), notNullValue());
        assertThat("Figure rotated 1", f.get(1, 2), notNullValue());
        assertThat("Figure rotated 4", f.get(0, 1), nullValue());
        assertThat("Figure rotated 4", f.get(2, 1), nullValue());

        board.moveFigureLeft();
        board.moveFigureLeft();
        board.moveFigureLeft();

        board.rotateFigureLeft();

        assertThat("Figure rotated 2", f.get(0, 1), notNullValue());
        assertThat("Figure rotated 2", f.get(1, 1), notNullValue());
        assertThat("Figure rotated 2", f.get(2, 1), notNullValue());
        assertThat("Figure rotated 4", f.get(1, 0), nullValue());
        assertThat("Figure rotated 4", f.get(1, 2), nullValue());

        for (int i = 0; i < board.getWidth() + 3; i++) {
            board.moveFigureRight();
        }

        board.rotateFigureLeft();

        assertThat("Figure rotated 3", f.get(1, 0), notNullValue());
        assertThat("Figure rotated 3", f.get(1, 1), notNullValue());
        assertThat("Figure rotated 3", f.get(1, 2), notNullValue());
        assertThat("Figure rotated 4", f.get(0, 1), nullValue());
        assertThat("Figure rotated 4", f.get(2, 1), nullValue());

        for (int i = 0; i < board.getHeight() + 3; i++) {
            board.moveFigureDown();
        }

        board.rotateFigureLeft();

        assertThat("Figure rotated 4", f.get(0, 1), notNullValue());
        assertThat("Figure rotated 4", f.get(1, 1), notNullValue());
        assertThat("Figure rotated 4", f.get(2, 1), notNullValue());
        assertThat("Figure rotated 4", f.get(1, 0), nullValue());
        assertThat("Figure rotated 4", f.get(1, 2), nullValue());
    }

    @Test
    public void testWallDestroysBlocks() throws Exception
    {
        int width = board.getWidth();
        int height = board.getHeight();
        board.fill(width - 1, 0, 1, height, BlockType.BASIC);
        board.fill(0, height - 1, width, 1, BlockType.BASIC);
        board.set(width - 1, 1, new SteelPyramid());
        board.set(width - 1, 3, new Bomb());
        board.set(width - 1, 5, new Missile());
        board.set(width - 1, 7, new AcidContainer());
        board.set(width - 1, 9, new SplodgeContainer());
        board.set(width - 1, 11, new Acid());
        board.set(width - 1, 13, new Splodge());

        board.set(1, height - 1,  new SteelPyramid());
        board.set(3, height - 1,  new Bomb());
        board.set(5, height - 1,  new Missile());
        board.set(7, height - 1,  new AcidContainer());
        board.set(9, height - 1,  new SplodgeContainer());
        board.set(11, height - 1, new Acid());
        board.set(13, height - 1, new Splodge());

        System.out.println(board);

        board.moveWallLeft();
        board.moveWallDown();

        System.out.println(board);

        assertThatBoardIsEmpty();
    }

    @Test
    public void testFigureDoesNotMoveBehindTheWall() throws Exception
    {
        board.moveWallLeft();
        board.moveWallLeft();
        board.moveWallLeft();

        board.moveWallDown();
        board.moveWallDown();
        board.moveWallDown();

        assertThat(board.getVerticalWallX(), is(board.getWidth() - 3));
        assertThat(board.getHorizontalWallY(), is(board.getHeight() - 3));

        figure.fill(0, 0, figure.width, figure.height, BlockType.BASIC);

        for (int i = 0; i < board.getHeight(); i++) {
            board.moveFigureUp();
        }

        for (int i = 0; i < board.getWidth(); i++) {
            board.moveFigureRight();
        }

        assertThat(board.getFigureX(), is(board.getWidth() - 3 - figure.getWidth()));
        assertThat(board.getFigureY(), is(board.getHeight() - 3 - figure.getHeight()));

        board.moveWallRight();
        board.moveWallRight();
        board.moveWallRight();

        board.moveWallUp();
        board.moveWallUp();
        board.moveWallUp();

        for (int i = 0; i < board.getHeight(); i++) {
            board.moveFigureUp();
        }

        for (int i = 0; i < board.getWidth(); i++) {
            board.moveFigureRight();
        }

        assertThat(board.getFigureX(), is(board.getWidth() - figure.getWidth()));
        assertThat(board.getFigureY(), is(board.getHeight() - figure.getHeight()));

    }
}
