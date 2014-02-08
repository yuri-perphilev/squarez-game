package com.zeroage.squarez.model;

public interface Interactable
{
    InteractedBlocks interactWith(Block block, int x, int y, int boardX, int boardY, Board board);

    class InteractedBlocks
    {
        private Block activeBlock;
        private Block passiveBlock;

        protected InteractedBlocks(Block activeBlock, Block passiveBlock)
        {
            this.activeBlock = activeBlock;
            this.passiveBlock = passiveBlock;
        }

        public Block getActiveBlock()
        {
            return activeBlock;
        }

        public Block getPassiveBlock()
        {
            return passiveBlock;
        }
    }
}
