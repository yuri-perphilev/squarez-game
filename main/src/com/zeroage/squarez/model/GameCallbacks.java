package com.zeroage.squarez.model;

import java.util.List;
import java.util.Set;

public interface GameCallbacks
{
    void dissolving(List<Board.Area> areas);

    BombCallback bomb(int x, int y);

    MissileCallback missile(int fromX, int fromY, int dX, int dY);
}
