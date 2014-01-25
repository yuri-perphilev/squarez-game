package com.zeroage.squarez.model;

import java.util.ArrayList;
import java.util.List;
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
        return direction.getTexture();
    }

    public Direction getDirection()
    {
        return direction;
    }

    @Override
    public void act(int x, int y, Board board, float delay)
    {
        MissileCallback missileCallback = null;
        GameCallbacks callbacks = board.getCallbacks();
        if (callbacks != null) {
            missileCallback = callbacks.missile(x, y, direction.dx, direction.dy);
        }

        List<PositionedBlock> blocksToHit = new ArrayList<PositionedBlock>();

        while (x >= 0 && x < board.width && y>= 0 && y < board.height) {
            Block block = board.get(x, y);
            board.set(x, y, null);
            if (block != null && block != this) {
                blocksToHit.add(new PositionedBlock(block, x, y));

                float hitTime = missileCallback != null ? missileCallback.getHitTime(x, y) : 0;
                block.act(x, y, board, delay + hitTime);
            }
            x += direction.dx;
            y += direction.dy;
        }

        if (missileCallback != null) {
            missileCallback.fire(blocksToHit, delay);
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
    public Block copy()
    {
        return new Missile(direction);
    }

    @Override
    public String toString()
    {
        return "^";
    }

    public static enum Direction
    {
        INC_Y(0, 1, BlockTexture.MISSILE_DOWN),
        INC_X(1, 0, BlockTexture.MISSILE_RIGHT),
        DEC_Y(0, -1, BlockTexture.MISSILE_UP),
        DEC_X(-1, 0, BlockTexture.MISSILE_LEFT);

        private final int dx;
        private final int dy;
        private final BlockTexture texture;

        private Direction(int dx, int dy, BlockTexture texture)
        {
            this.dx = dx;
            this.dy = dy;
            this.texture = texture;
        }

        public int getDx()
        {
            return dx;
        }

        public int getDy()
        {
            return dy;
        }

        public BlockTexture getTexture()
        {
            return texture;
        }
    }
}
