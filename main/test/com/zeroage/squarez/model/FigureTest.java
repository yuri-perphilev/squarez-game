package com.zeroage.squarez.model;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

public class FigureTest extends BaseTest
{
    @Test
    public void testCreateBlock() throws Exception
    {
        figure.clear();
        figure.set(1, 1, BlockType.BASIC.make());
        assertThatBlockHasType(figure.get(1, 1), BlockType.BASIC);
    }

    @Test
    public void testFindFreeNeighbours() throws Exception
    {
        figure.clear();
        figure.set(0, 0, BlockType.BASIC.make());
        figure.set(0, 1, BlockType.BASIC.make());
        figure.set(1, 0, BlockType.BASIC.make());
        figure.set(1, 2, BlockType.BASIC.make());

        List<int[]> freeNeighbours1 = figure.findFreeNeighbours(0, 0);
        assertThat(freeNeighbours1, hasSize(0));


        List<int[]> freeNeighbours2 = figure.findFreeNeighbours(1, 0);
        assertThat(freeNeighbours2, hasSize(2));
        assertThat(freeNeighbours2, hasItems(new int[]{1, 1}, new int[]{2, 0}));

        List<int[]> freeNeighbours3 = figure.findFreeNeighbours(0, 1);
        assertThat(freeNeighbours3, hasSize(2));
        assertThat(freeNeighbours3, hasItems(new int[]{1, 1}, new int[]{0, 2}));

        List<int[]> freeNeighbours4 = figure.findFreeNeighbours(1, 2);
        assertThat(freeNeighbours4, hasSize(3));
        assertThat(freeNeighbours4, hasItems(new int[]{0, 2}, new int[]{2, 2}, new int[]{1, 1}));
    }

    @Test
    public void testRandomFigure() throws Exception
    {
        for (int i = 0; i < 20; i++) {
            Figure f = new Figure(FIGURE_SIZE);
            System.out.println("===========================");
            System.out.println(f);
            System.out.println("===========================");
            System.out.println("\n");
        }
    }

    @Test
    public void testRotateFigureRight() throws Exception
    {
        figure.clear();
        figure.set(0, 0, BlockType.BASIC.make());
        figure.set(1, 0, BlockType.BASIC.make());
        figure.set(2, 0, BlockType.BASIC.make());
        figure.set(1, 1, BlockType.BASIC.make());

        figure.rotateRight();

        assertThat(figure.get(0, 0), nullValue());
        assertThat(figure.get(1, 0), nullValue());

        assertThatBlockHasType(figure.get(2, 0), BlockType.BASIC);
        assertThatBlockHasType(figure.get(2, 1), BlockType.BASIC);
        assertThatBlockHasType(figure.get(2, 2), BlockType.BASIC);
        assertThatBlockHasType(figure.get(1, 1), BlockType.BASIC);

        figure.rotateRight();
        figure.rotateRight();
        figure.rotateRight();

        assertThatBlockHasType(figure.get(0, 0), BlockType.BASIC);
        assertThatBlockHasType(figure.get(1, 0), BlockType.BASIC);
        assertThatBlockHasType(figure.get(2, 0), BlockType.BASIC);
        assertThatBlockHasType(figure.get(1, 1), BlockType.BASIC);

    }

    @Test
    public void testRotateFigureLeft() throws Exception
    {
        figure.clear();
        figure.set(0, 0, BlockType.BASIC.make());
        figure.set(1, 0, BlockType.BASIC.make());
        figure.set(2, 0, BlockType.BASIC.make());
        figure.set(1, 1, BlockType.BASIC.make());

        figure.rotateLeft();

        assertThat(figure.get(1, 0), nullValue());
        assertThat(figure.get(2, 0), nullValue());

        assertThatBlockHasType(figure.get(0, 0), BlockType.BASIC);
        assertThatBlockHasType(figure.get(0, 1), BlockType.BASIC);
        assertThatBlockHasType(figure.get(0, 2), BlockType.BASIC);
        assertThatBlockHasType(figure.get(1, 1), BlockType.BASIC);

        figure.rotateLeft();
        figure.rotateLeft();
        figure.rotateLeft();

        assertThatBlockHasType(figure.get(0, 0), BlockType.BASIC);
        assertThatBlockHasType(figure.get(1, 0), BlockType.BASIC);
        assertThatBlockHasType(figure.get(2, 0), BlockType.BASIC);
        assertThatBlockHasType(figure.get(1, 1), BlockType.BASIC);
    }

    @Test
    public void testFigureCopy() throws Exception
    {
        Figure newFigure = new Figure(figure);
        assertThat(newFigure, notNullValue());

        newFigure.iterate(new Matrix.Callback()
        {
            @Override
            public void cell(int x, int y, Block block)
            {
                assertThat(figure.get(x, y), is(block));
            }
        });
    }

    @Test
    @Ignore
    public void generateAllFigures()
    {
        Set<Figure> figs = new HashSet<Figure>();
        for (int i = 0; i < 50000; i++) {
            Figure fig = new Figure(FIGURE_SIZE);
            figs.add(new Figure(fig));
            fig.rotateLeft();
            figs.add(new Figure(fig));
            fig.rotateLeft();
            figs.add(new Figure(fig));
            fig.rotateLeft();
            figs.add(new Figure(fig));
        }

        Set<Figure> uniqueFigs = new HashSet<Figure>();

        for (Figure fig : figs) {
            Set<Figure> mutationFigs = mutateFigure(fig);
            boolean contains = false;
            for (Figure mf : mutationFigs) {
                if (uniqueFigs.contains(mf)) {
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                uniqueFigs.add(fig);
            }
        }

        System.out.println(uniqueFigs.size());
        System.out.println(uniqueFigs);

        System.out.println();
        System.out.println();

        Set<Figure> figs2 = new HashSet<Figure>();

        for (Figure f : uniqueFigs) {
            System.out.println(f.toCode());
            Figure ff = new Figure(3);
            ff.fromCode(f.toCode());
            System.out.println(ff);
        }
    }

    private Set<Figure> mutateFigure(Figure fig)
    {
        Set<Figure> figs = new HashSet<Figure>();

        addAllShifts(fig, figs);

        fig.flipHorizontally();

        addAllShifts(fig, figs);

        fig.flipVertically();

        addAllShifts(fig, figs);

        return figs;
    }

    private void addAllShifts(Figure fig, Set<Figure> figs)
    {
        addAllRotations(fig, figs);
        fig.shiftLeft();
        addAllRotations(fig, figs);
        fig.shiftLeft();
        addAllRotations(fig, figs);
        fig.shiftUp();
        addAllRotations(fig, figs);
        fig.shiftUp();
        addAllRotations(fig, figs);
        fig.shiftRight();
        addAllRotations(fig, figs);
        fig.shiftRight();
        addAllRotations(fig, figs);
        fig.shiftDown();
        addAllRotations(fig, figs);
        fig.shiftDown();
        addAllRotations(fig, figs);
    }

    private void addAllRotations(Figure fig, Set<Figure> figs)
    {
        figs.add(new Figure(fig));
        fig.rotateLeft();
        figs.add(new Figure(fig));
        fig.rotateLeft();
        figs.add(new Figure(fig));
        fig.rotateLeft();
        figs.add(new Figure(fig));
        fig.rotateLeft();
    }


    @Test
    public void testShiftFigure() throws Exception
    {
        Figure f = new Figure(FIGURE_SIZE);
        f.clear();
        f.set(0, 0, new BasicBlock());
        System.out.println(f);
        f.shiftRight();
        System.out.println(f);
        f.shiftRight();
        System.out.println(f);
        f.shiftRight();
        System.out.println(f);
        f.shiftUp();
        System.out.println(f);
        f.shiftUp();
        System.out.println(f);
        f.shiftUp();
        System.out.println(f);
        f.shiftUp();
        System.out.println(f);
    }
}

