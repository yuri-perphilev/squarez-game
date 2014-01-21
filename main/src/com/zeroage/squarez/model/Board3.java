package com.zeroage.squarez.model;

public class Board3 extends Board
{
    public Board3(int boardWidth, int boardHeight)
    {
        super(boardWidth, boardHeight);
    }

    public Board3(int boardWidth, int boardHeight, GameCallbacks callbacks)
    {
        super(boardWidth, boardHeight, callbacks);
    }

    @Override
    protected Figure makeFigure()
    {
        return new Figure3();
    }
}
