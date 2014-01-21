package com.zeroage.squarez.model;

import java.util.List;
import java.util.Set;

public interface GameCallbacks
{
    void dissolving(List<Board.Area> areas);

    void bomb(int x, int y, Set<int[]> blocksToExplode);

    void missile(int fromX, int fromY, int toX, int toY, int dX, int dY, List<PositionedBlock> blocksToHit);
}