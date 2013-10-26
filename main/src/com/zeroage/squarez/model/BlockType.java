package com.zeroage.squarez.model;

public enum BlockType
{
    EMPTY {
        @Override
        protected Block make()
        {
            return null;
        }

        @Override
        protected boolean isOfType(Block block)
        {
            return block == null;
        }
    },
    BASIC
            {
                @Override
                protected Block make()
                {
                    return new BasicBlock();
                }

                @Override
                protected boolean isOfType(Block block)
                {
                    return block instanceof BasicBlock;
                }
            },
    STEEL_PYRAMID
            {
                @Override
                protected Block make()
                {
                    return new SteelPyramid();
                }

                @Override
                protected boolean isOfType(Block block)
                {
                    return block instanceof SteelPyramid;
                }
            },
    SHIELD
            {
                @Override
                protected Block make()
                {
                    return new Shield();
                }

                @Override
                protected boolean isOfType(Block block)
                {
                    return block instanceof Shield;
                }
            },
    MISSILE {
        @Override
        protected Block make()
        {
            return new Missile();
        }

        @Override
        protected boolean isOfType(Block block)
        {
            return block instanceof Missile;
        }
    },
    BOMB {
        @Override
        protected Block make()
        {
            return new Bomb();
        }

        @Override
        protected boolean isOfType(Block block)
        {
            return block instanceof Bomb;
        }
    },
    CRACKED
            {
                @Override
                protected Block make()
                {
                    return new Cracked();
                }

                @Override
                protected boolean isOfType(Block block)
                {
                    return block instanceof Cracked;
                }
            },
    STICKY {
        @Override
        protected Block make()
        {
            return new Sticky();
        }

        @Override
        protected boolean isOfType(Block block)
        {
            return block instanceof Sticky;
        }
    };


    protected abstract Block make();
    protected abstract boolean isOfType(Block block);
}
