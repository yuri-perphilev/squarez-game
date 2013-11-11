package com.zeroage.squarez.model;

import java.util.List;

public interface GameEventListener
{
    void dissolving(List<Board.Area> areas);
}
