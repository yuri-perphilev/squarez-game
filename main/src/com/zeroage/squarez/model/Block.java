package com.zeroage.squarez.model;

public interface Block
{
    boolean canDissolve();

    Block dissolve();

    BlockType getType();

    void act(int x, int y, Board board);

    void rotateRight();

    void rotateLeft();

    boolean collidesWith(Block block);
}
