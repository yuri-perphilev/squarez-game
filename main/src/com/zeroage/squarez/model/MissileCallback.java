package com.zeroage.squarez.model;

import java.util.List;

public interface MissileCallback
{
    float getHitTime(int x, int y);

    void fire(List<PositionedBlock> blocksToHit);
}
