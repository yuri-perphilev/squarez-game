package com.zeroage.squarez.model;

public class SteelPyramid implements Block
{
    @Override
    public boolean canDissolve()
    {
        return false;
    }

    @Override
    public Block dissolve()
    {
        return this;
    }

    @Override
    public BlockType getType()
    {
        return BlockType.STEEL_PYRAMID;
    }

    @Override
    public void act(int x, int y, Board board)
    {

    }

    @Override
    public void rotateRight()
    {

    }

    @Override
    public void rotateLeft()
    {

    }

    @Override
    public boolean collidesWith(Block block)
    {
        return true;
    }

    @Override
    public String toString()
    {
        return "X";
    }
}
