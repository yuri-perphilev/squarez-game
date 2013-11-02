package com.zeroage.squarez.model;

import java.util.*;

public class Board extends Matrix
{
    private int minAreaWidth = 3;
    private int minAreaHeight = 3;
    private Figure figure;
    private int figureX;
    private int figureY;

    public Board(int boardWidth, int boardHeight)
    {
        super(boardWidth, boardHeight);
    }

    public void fill(Area area, BlockType blockType)
    {
        fill(area.getX(), area.getY(), area.getW(), area.getH(), blockType);
    }

    public void action()
    {
        List<Area> areas = findAreas();
        for (Area area : areas) {
            dissolve(area);
        }
    }

    public void dissolve(Area area)
    {
        for (int x = area.getX(); x < area.getX() + area.getW(); x++) {
            for (int y = area.getY(); y < area.getY() + area.getH(); y++) {
                dissolveSingleBlock(x, y);
            }
        }
    }

    private void dissolveSingleBlock(int x, int y)
    {
        System.out.printf("Dissolving at (%d, %d)%n", x, y);
        Block block = get(x, y);
        if (block != null) {
            block.act(x, y, this);
            set(x, y, block.dissolve());
            notifyNeighbourBlocks(x, y);
        }
    }

    private void notifyNeighbourBlocks(int x, int y)
    {
        int neighbours[][] = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] point : neighbours) {
            int newX = x + point[0];
            int newY = y + point[1];
            Block block = get(newX, newY);
            if (block != null && block instanceof NeighbourAware) {
                NeighbourAware neighbourAware = (NeighbourAware) block;
                NeighbourAction action = neighbourAware.neighbourDissolvedAction();
                switch (action) {
                    case IGNORE:
                        break;
                    case DISSOLVE:
                        dissolveSingleBlock(newX, newY);
                }
            }
        }
    }

    public List<Area> findAreas()
    {
        List<Area> areas = new ArrayList<Area>();

        for (int i = 0; i <= width - minAreaWidth; i++) {
            for (int j = 0; j <= height - minAreaHeight; j++) {
                if (get(i, j) != null) {
                    if (isSolidArea(i, j, minAreaWidth, minAreaHeight)) {
                        areas.add(getMaxSolidArea(i, j));
                    }
                }
            }
        }
        return areas;
    }

    public boolean isSolidArea(int x, int y, int w, int h)
    {
        for (int i = x; i < x + w; i++) {
            for (int j = y; j < y + h; j++) {
                if (get(i, j) == null || !get(i, j).canDissolve()) {
                    return false;
                }
            }
        }
        return true;
    }

    public Area getMaxSolidArea(int x, int y)
    {
        int w = 0, h = 0, a = 0;
        for (int j = minAreaHeight; j <= height - y; j++) {
            for (int i = minAreaWidth; i <= width - x; i++) {
                if (isSolidArea(x, y, i, j)) {
                    if (i * j > a) {
                        w = i;
                        h = j;
                        a = w * h;
                    }
                }
            }
        }
        return new Area(x, y, w, h);
    }

    public List<Area> mergeAreas(List<Area> areas)
    {
        List<Area> sortedAreas = new ArrayList<Area>(areas);
        Collections.sort(sortedAreas, Collections.reverseOrder(new Comparator<Area>()
        {
            @Override
            public int compare(Area lhs, Area rhs)
            {
                return lhs.getArea() - rhs.getArea();
            }
        }));

        List<Area> result = new ArrayList<Area>(areas.size());


        for (Area area : sortedAreas) {
            boolean contains = false;
            for (Area a : result) {
                if (a.contains(area)) {
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                result.add(area);
            }
        }

        return result;
    }

    public void put(Figure figure)
    {
        this.figure = figure;
        this.figureX = 0;
        this.figureY = -3; // figure is in the pocket
    }

    public Figure getFigure()
    {
        return figure;
    }

    public int getFigureX()
    {
        return figureX;
    }

    public int getFigureY()
    {
        return figureY;
    }

    public void moveFigureUp()
    {
        if (isMoveValid(figureX, figureY, figureX, figureY + 1)) {
            figureY++;
            applyNewFigurePosition();
        }
    }

    public void moveFigureDown()
    {
        if (isMoveValid(figureX, figureY, figureX, figureY - 1)) {
            figureY--;
            applyNewFigurePosition();
        }
    }

    public void moveFigureLeft()
    {
        if (isMoveValid(figureX, figureY, figureX - 1, figureY)) {
            figureX--;
            applyNewFigurePosition();
        }
    }

    public void moveFigureRight()
    {
        if (isMoveValid(figureX, figureY, figureX + 1, figureY)) {
            figureX++;
            applyNewFigurePosition();
        }
    }

    public void rotateFigureLeft() {
        Figure f = new Figure(figure);
        f.rotateLeft();
        if (noCollisionsDetected(f, figureX, figureY)) {
            figure.rotateLeft();
            applyNewFigurePosition();
        }
    }

    public void rotateFigureRight() {
        Figure f = new Figure(figure);
        f.rotateRight();
        if (noCollisionsDetected(f, figureX, figureY)) {
            figure.rotateRight();
            applyNewFigurePosition();
        }
    }

    public boolean isMoveValid(int x, int y, int newX, int newY)
    {
//        if (newX < 0 || newX + figure.width > width || newY + figure.height > height) {
//            return false;
//        }
        if (figureInPocket() && newY < y) {
            // in pocket move up is the only allowed move
            return false;
        }

        return noCollisionsDetected(figure, newX, newY);
    }

    private boolean noCollisionsDetected(Figure f, final int newX, final int newY)
    {
        final boolean inPocket = figureInPocket();

        class CollisionCounter implements Callback
        {
            public int collisions;

            @Override
            public void cell(int x, int y, Block block)
            {
                int bx = x + newX;
                int by = y + newY;
                Block boardBlock = Board.this.get(bx, by);
                if (block != null) {

                    if (inPocket) {
                        if (bx < 0 || bx >= 3 || by < -3) {
                            collisions++;
                        }
                    }
                    else {
                        if (bx < 0 || bx >= width || by < 0 || by >= height) {
                            collisions++;
                        }
                    }
                }

/*
                // old version
                if (block != null && by >= 0 && (bx < 0 || bx >= width || by >= height)) {
                    collisions++;
                }

                if (block != null && by < 0 && (bx < 0 || bx >= 3 || by < -3)) {
                    collisions++;
                }
*/

                if (block != null && boardBlock != null) {
                    if (block.collidesWith(boardBlock) && boardBlock.collidesWith(block)) {
                        collisions++;
                    }
                }

            }
        }

        CollisionCounter collisionCounter = new CollisionCounter();
        f.iterate(collisionCounter);

        return collisionCounter.collisions == 0;
    }

    public boolean figureInPocket()
    {
        class PocketDetector extends AbstractValueReturningCallback<Boolean> {

            protected PocketDetector()
            {
                super(false);
            }

            @Override
            public void cell(int x, int y, Block block)
            {
                int bx = x + figureX;
                int by = y + figureY;
                if (block != null && (bx >= 0 && bx < 3 && by >= -3 && by < 0)) {
                    returnValue(true);
                }
            }
        }

        PocketDetector pocketDetector = new PocketDetector();
        figure.iterate(pocketDetector);
        return pocketDetector.getValue();
    }

    public void applyNewFigurePosition() {
        figure.iterate(new Callback()
        {
            @Override
            public void cell(int x, int y, Block block)
            {
                if (block instanceof PositionChangeAware) {
                    PositionChangeAware positionChangeAware = (PositionChangeAware) block;
                    positionChangeAware.positionChanged(x, y, figureX + x, figureY + y, Board.this);
                }
            }
        });
    }

    public void installFigure()
    {
        if (figureX >= 0 && figureY >= 0) {
            figure.iterate(new Callback()
            {
                @Override
                public void cell(int x, int y, Block block)
                {
                    Board.this.set(figureX + x, figureY + y, block);
                }
            });
        }
    }


    public static class Area
    {
        private final int x;
        private final int y;
        private final int w;
        private final int h;
        private final int area;

        public Area(int x, int y, int w, int h)
        {

            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.area = w * h;
        }

        public int getX()
        {
            return x;
        }

        public int getY()
        {
            return y;
        }

        public int getW()
        {
            return w;
        }

        public int getH()
        {
            return h;
        }

        public int getArea()
        {
            return area;
        }

        public boolean contains(Area area)
        {
            return getX() <= area.getX() &&
                   getY() <= area.getY() &&
                   getX() + getW() >= area.getX() + area.getW() &&
                   getY() + getH() >= area.getY() + area.getH();
        }

        @Override
        public String toString()
        {
            return String.format("{x:%d, y:%d, w:%d, h:%d, area:%d}", x, y, w, h, area);
        }
    }

}
