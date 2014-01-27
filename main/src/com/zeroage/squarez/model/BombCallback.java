package com.zeroage.squarez.model;

import java.util.List;

public interface BombCallback
{
    float getBlastTime(int x, int y);

    void explode(List<PositionedBlock> blocksToHit, float delay);
}
