package com.zeroage.squarez.model;

import java.util.*;

public class Board extends Matrix
{
    private int minAreaWidth = 3;
    private int minAreaHeight = 3;
    private Figure figure;
    private Figure nextFigure;
    private int figureX;
    private int figureY;
    private int nextFigureX;
    private int nextFigureY;
    private int figureSize = 3;
    private int horizontalWallY;
    private int verticalWallX;

    private GameCallbacks callbacks;

    public Board(int boardWidth, int boardHeight)
    {
        this(boardWidth, boardHeight, null);
    }

    public Board(int boardWidth, int boardHeight, GameCallbacks callbacks)
    {
        super(boardWidth, boardHeight);
        horizontalWallY = boardHeight;
        verticalWallX = boardWidth;
        this.callbacks = callbacks;
        nextFigure();
        nextFigure();
    }


    public void fill(Area area, BlockType blockType)
    {
        fill(area.getX(), area.getY(), area.getW(), area.getH(), blockType);
    }

    public void action()
    {
        installFigure();
        List<Area> areas = findAreas();


        if (areas != null && !areas.isEmpty()) {
            List<PositionedBlock> dissolvedBlocks = new ArrayList<PositionedBlock>();
            for (Area area : areas) {
                dissolvedBlocks.addAll(dissolve(area));
            }

            for (PositionedBlock dissolvedBlock : dissolvedBlocks) {
                if (dissolvedBlock.getBlock() instanceof PostActionBlock) {
                    PostActionBlock postActionBlock = (PostActionBlock) dissolvedBlock.getBlock();
                    postActionBlock.postAction(this);
                }
            }

            if (callbacks != null) {
                callbacks.dissolving(dissolvedBlocks);
            }
        }
    }

    public List<PositionedBlock> dissolve(Area area)
    {
        List<PositionedBlock> result = new ArrayList<PositionedBlock>();

        for (int x = area.getX(); x < area.getX() + area.getW(); x++) {
            for (int y = area.getY(); y < area.getY() + area.getH(); y++) {
                result.addAll(dissolveSingleBlock(x, y));
            }
        }
        return result;
    }

    private List<PositionedBlock> dissolveSingleBlock(int x, int y)
    {
        List<PositionedBlock> result = new ArrayList<PositionedBlock>();

        Block block = get(x, y);
        if (block != null) {
            result.add(new PositionedBlock(block, x, y));

            block.act(x, y, this, 0);
            set(x, y, block.dissolve());
            result.addAll(notifyNeighbourBlocks(x, y));
        }

        return result;
    }

    private List<PositionedBlock> notifyNeighbourBlocks(int x, int y)
    {
        List<PositionedBlock> result = new ArrayList<PositionedBlock>();

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
                        result.addAll(dissolveSingleBlock(newX, newY));
                }
            }
        }

        return result;
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

    public void nextFigure()
    {
        this.figure = nextFigure;
        this.figureX = nextFigureX;
        this.figureY = nextFigureY;
        nextFigureX = 0;
        nextFigureY = -figureSize; // next figure sits in the pocket
        nextFigure = makeFigure();
    }

    protected Figure makeFigure()
    {
        return new Figure(figureSize);
    }

    public Figure getFigure()
    {
        return figure;
    }

    public Figure getNextFigure()
    {
        return nextFigure;
    }

    public int getFigureX()
    {
        return figureX;
    }

    public int getFigureY()
    {
        return figureY;
    }

    public int getNextFigureX()
    {
        return nextFigureX;
    }

    public int getNextFigureY()
    {
        return nextFigureY;
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
        Figure f = figure.copy();
        f.rotateLeft();
        if (noCollisionsDetected(f, figureX, figureY)) {
            figure.rotateLeft();
            applyNewFigurePosition();
        }
        else {
            // try to move rotated figure away from the wall
            int newX = adjustFigureNearWallXPos(figureX);
            int newY = adjustFigureNearWallYPos(figureY);
            if (noCollisionsDetected(f, newX, newY)) {
                figure.rotateLeft();
                figureX = newX;
                figureY = newY;
                applyNewFigurePosition();
            }
        }
    }

    public void rotateFigureRight() {
        Figure f = figure.copy();
        f.rotateRight();
        if (noCollisionsDetected(f, figureX, figureY)) {
            figure.rotateRight();
            applyNewFigurePosition();
        }
        else {
            // try to move rotated figure away from the wall
            int newX = adjustFigureNearWallXPos(figureX);
            int newY = adjustFigureNearWallYPos(figureY);
            if (noCollisionsDetected(f, newX, newY)) {
                figure.rotateRight();
                figureX = newX;
                figureY = newY;
                applyNewFigurePosition();
            }
        }
    }

    private int adjustFigureNearWallXPos(int x)
    {
        return x <= 2 ? x + 1 : x >= width - 2 ? x - 1 : x;
    }

    private int adjustFigureNearWallYPos(int y)
    {
        return y <= 2 ? y + 1 : y >= height - 2 ? y - 1 : y;
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

    public GameCallbacks getCallbacks()
    {
        return callbacks;
    }

    public void setCallbacks(GameCallbacks callbacks)
    {
        this.callbacks = callbacks;
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
                        // test pocket coordinates
                        if (bx < 0 || bx >= figureSize || by < -figureSize) {
                            collisions++;
                        }
                    }
                    else {
                        if (bx < 0 || bx >= width || bx >= verticalWallX || by < 0 || by >= height || by >= horizontalWallY) {
                            collisions++;
                        }
                    }
                }

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
                if (block != null && (bx >= 0 && bx < figureSize && by >= -figureSize && by < 0)) {
                    returnValue(true);
                }
            }
        }
        if (figureX >=0 && figureY >= 0) {
            return false;
        }
        else {
            PocketDetector pocketDetector = new PocketDetector();
            figure.iterate(pocketDetector);
            return pocketDetector.getValue();
        }
    }

    public void applyNewFigurePosition() {
        figure.iterate(new Callback()
        {
            @Override
            public void cell(int x, int y, Block block)
            {
                int boardX = figureX + x;
                int boardY = figureY + y;
                if (block instanceof PositionChangeAware) {
                    PositionChangeAware positionChangeAware = (PositionChangeAware) block;
                    positionChangeAware.positionChanged(x, y, boardX, boardY, Board.this);
                }
                Block boardBlock = get(boardX, boardY);
                if (boardBlock instanceof Interactable) {
                    Interactable.InteractedBlocks blocks =
                            ((Interactable) boardBlock).interactWith(block, x, y, boardX, boardY, Board.this);

                    set(boardX, boardY, blocks.getActiveBlock());
                    figure.set(x, y, blocks.getPassiveBlock());
                }
                else if (block instanceof Interactable) {
                    Interactable.InteractedBlocks blocks =
                            ((Interactable) block).interactWith(boardBlock, x, y, boardX, boardY, Board.this);

                    figure.set(x, y, blocks.getActiveBlock());
                    set(boardX, boardY, blocks.getPassiveBlock());
                }
            }
        });
    }

    public void installFigure()
    {
        if (!figureInPocket()) {
            figure.iterate(new Callback()
            {
                @Override
                public void cell(int x, int y, Block block)
                {
                    if (block != null) {
                        Board.this.set(figureX + x, figureY + y, block);
                    }
                }
            });

            nextFigure();
        }
    }

    public int getHorizontalWallY()
    {
        return horizontalWallY;
    }

    public int getVerticalWallX()
    {
        return verticalWallX;
    }

    public void moveWallLeft()
    {
        if (verticalWallX > 0) {
            verticalWallX--;
            for (int y = 0; y < height; y++) {
                set(verticalWallX, y, null);
            }
        }
    }

    public void moveWallRight()
    {
        if (verticalWallX < width) {
            verticalWallX++;
        }
    }

    public void moveWallDown()
    {
        if (horizontalWallY > 0) {
            horizontalWallY--;
            for (int x = 0; x < width; x++) {
                set(x, horizontalWallY, null);
            }
        }
    }

    public void moveWallUp()
    {
        if (horizontalWallY < height) {
            horizontalWallY++;
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
