package com.zeroage.squarez.model;

public class Shield implements Block
{
    private int level = 3;

    @Override
    public boolean canDissolve()
    {
        return true;
    }

    @Override
    public Block dissolve()
    {
        if (level > 1) {
            level--;
            return this;
        }
        else {
            System.out.println("Dissolving!!!");
            return null;
        }
    }

    @Override
    public BlockType getType()
    {
        return BlockType.SHIELD;
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
        return "" + level;
    }
}
