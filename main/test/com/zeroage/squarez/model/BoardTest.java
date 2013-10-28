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

        board.set(1, 1, BlockType.BASIC.make());
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


}
