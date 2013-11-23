package com.zeroage.squarez.model;

import java.util.List;
import java.util.Set;

public interface GameEventListener
{
    void dissolving(List<Board.Area> areas);

    void bomb(Set<int[]> blocksToExplode);

    void missile(int fromX, int fromY, int toX, int toY, int dX, int dY);
}
