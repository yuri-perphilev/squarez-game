package com.zeroage.squarez.model;

public class Cracked implements Block, NeighbourAware
{
    @Override
    public boolean canDissolve()
    {
        return true;
    }

    @Override
    public Block dissolve()
    {
        return null;
    }

    @Override
    public BlockType getType()
    {
        return BlockType.CRACKED;
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
        return "%";
    }

    @Override
    public NeighbourAction neighbourDissolvedAction()
    {
        return NeighbourAction.DISSOLVE;
    }
}
