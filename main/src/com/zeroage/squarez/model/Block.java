package com.zeroage.squarez.model;

public interface Block
{
    /**
     * Determines whether a block will be dissolved when a group if 3x3 or more blocks is organized
     * @return true to dissolve block
     */
    boolean canDissolve();

    /**
     * Returns a new block that will appear instead of dissolved block (if block supports dissolving)
     * @return a new block after dissolve
     */
    Block dissolve();

    /**
     * Returns a type of block
     * @return type of block
     */
    BlockType getType();

    /**
     * Performs an action before block dissolving
     * @param x x-coordinate of the block on board
     * @param y y-coordinate of the block on board
     * @param board board
     * @param delay
     */
    void act(int x, int y, Board board, float delay);

    /**
     * Update block state upon figure right rotation
     */
    void rotateRight();

    /**
     * Update block state upon figure left rotation
     */
    void rotateLeft();

    /**
     * Determines whether a block collides with another block
     * @param block a block to collide with
     * @return true if current block collides with another block
     */
    boolean collidesWith(Block block);

    /**
     * Determines whether a block acts when exploded by missile or bomb
     * @return true if block perform additional action when exploded
     */
    boolean actsOnExplode();

    /**
     * Returns block layout corresponding to the current block state
     * @return block texture
     */
    BlockTexture getTexture();

    /**
     * Creates a copy of block, cloning its internal state
     * @return a new clone of block
     */
    Block copy();
}
