package com.zeroage.squarez.model;

import java.util.Random;

public class WallMove extends AbstractBlock
{
    private final Direction direction;

    public WallMove()
    {
        Random random = new Random();
        Direction[] directions = Direction.values();
        this.direction = directions[random.nextInt(directions.length)];
    }

    @Override
    public BlockTexture getTexture()
    {
        return super.getTexture();
    }

    @Override
    public BlockType getType()
    {
        return BlockType.WALL_MOVE;
    }

    public static enum Direction
    {
        INC_Y(0, 1, BlockTexture.WALL_DOWN),
        INC_X(1, 0, BlockTexture.WALL_RIGHT),
        DEC_Y(0, -1, BlockTexture.WALL_UP),
        DEC_X(-1, 0, BlockTexture.WALL_LEFT);

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
