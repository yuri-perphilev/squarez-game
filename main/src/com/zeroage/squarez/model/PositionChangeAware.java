package com.zeroage.squarez.model;

public interface PositionChangeAware
{
    public void positionChanged(int x, int y, int boardX, int boardY, Board board);
}
