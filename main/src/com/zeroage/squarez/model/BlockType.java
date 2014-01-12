package com.zeroage.squarez.model;

public enum BlockType
{
    EMPTY
            {
                @Override
                protected Block make()
                {
                    return null;
                }

            },
    BASIC
            {
                @Override
                protected Block make()
                {
                    return new BasicBlock();
                }

            },
    STEEL_PYRAMID
            {
                @Override
                protected Block make()
                {
                    return new SteelPyramid();
                }

            },
    SHIELD
            {
                @Override
                protected Block make()
                {
                    return new Shield();
                }

            },
    MISSILE
            {
                @Override
                protected Block make()
                {
                    return new Missile();
                }

            },
    BOMB
            {
                @Override
                protected Block make()
                {
                    return new Bomb();
                }

            },
    CRACKED
            {
                @Override
                protected Block make()
                {
                    return new Cracked();
                }

            },
    STICKY
            {
                @Override
                protected Block make()
                {
                    return new Sticky();
                }

            };


    protected abstract Block make();
}
