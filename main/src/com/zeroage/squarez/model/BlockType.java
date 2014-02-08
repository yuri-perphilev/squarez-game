package com.zeroage.squarez.model;

public enum BlockType
{
    EMPTY(BlockTexture.EMPTY),
    BASIC(BlockTexture.BASIC),
    STEEL_PYRAMID(BlockTexture.STEEL_PYRAMID),
    SHIELD(BlockTexture.SHIELD_THREE),
    MISSILE(null),
    BOMB(BlockTexture.BOMB),
    CRACKED(BlockTexture.CRACKED),
    STICKY(BlockTexture.STICKY),
    SPLODGE(BlockTexture.SPLODGE);

    private BlockTexture defaultTexture;

    BlockType(BlockTexture defaultTexture)
    {
        this.defaultTexture = defaultTexture;
    }

    public BlockTexture getDefaultTexture()
    {
        return defaultTexture;
    }
}
