package com.zeroage.squarez.model;

import java.util.List;
import java.util.Set;

public interface GameCallbacks
{
    void dissolving(List<PositionedBlock> blocks);

    BombCallback bomb(int x, int y);

    MissileCallback missile(int fromX, int fromY, int dX, int dY);

    void releaseSplodge(int x, int y, List<PositionedBlock> blocks, float delay);
}
