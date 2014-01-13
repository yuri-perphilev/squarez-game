package com.zeroage.squarez.model;

import java.util.Random;

public class Missile extends AbstractBlock
{
    private Direction direction;

    public Missile()
    {
        Random random = new Random();
        Direction[] directions = Direction.values();
        this.direction = directions[random.nextInt(directions.length)];
    }

    public Missile(Direction direction)
    {
        this.direction = direction;
    }

    @Override
    public BlockType getType()
    {
        return BlockType.MISSILE;
    }

    @Override
    public BlockTexture getTexture()
    {
        switch (direction) {
            case INC_Y:
                return BlockTexture.MISSILE_UP;
            case INC_X:
                return BlockTexture.MISSILE_RIGHT;
            case DEC_Y:
                return BlockTexture.MISSILE_DOWN;
            case DEC_X:
                return BlockTexture.MISSILE_LEFT;
            default:
                return null;
        }
    }

    public Direction getDirection()
    {
        return direction;
    }

    @Override
    public void act(int x, int y, Board board)
    {
        // System.out.printf("Acting missile at %d, %d%n", x, y);
        int xorig = x;
        int yorig = y;
        while (x >= 0 && x < board.width && y>= 0 && y < board.height) {
            Block block = board.get(x, y);
            // System.out.printf("Clearing at %d, %d%n", x, y);
            board.set(x, y, null);
            if (block != null && block != this) {
                block.act(x, y, board);
            }
            x += direction.getDx();
            y += direction.getDy();
        }

        GameEventListener listener = board.getListener();
        if (listener != null) {
            listener.missile(xorig, yorig, x - direction.getDx(), y - direction.getDy(), direction.dx, direction.dy);
        }
    }

    @Override
    public void rotateRight()
    {
        int n = direction.ordinal();
        n--;
        if (n < 0) {
            n = Direction.values().length - 1;
        }
        direction = Direction.values()[n];
    }

    @Override
    public void rotateLeft()
    {
        int n = direction.ordinal();
        n++;
        if (n >= Direction.values().length) {
            n = 0;
        }
        direction = Direction.values()[n];
    }

    @Override
    public boolean actsOnExplode()
    {
        return true;
    }

    @Override
    public String toString()
    {
        return "^";
    }

    public static enum Direction
    {
        INC_Y(0, 1),
        INC_X(1, 0),
        DEC_Y(0, -1),
        DEC_X(-1, 0);

        private final int dx;
        private final int dy;

        private Direction(int dx, int dy)
        {
            this.dx = dx;
            this.dy = dy;
        }

        public int getDx()
        {
            return dx;
        }

        public int getDy()
        {
            return dy;
        }
    }
}
