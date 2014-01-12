package com.zeroage.squarez.model;

/**
 * Default implementation of the block: simple dissoluble, does not act on explode, collides with anything
 */
public abstract class AbstractBlock implements Block
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
    public boolean actsOnExplode()
    {
        return false;
    }
}
