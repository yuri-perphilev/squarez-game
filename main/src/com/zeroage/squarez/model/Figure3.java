package com.zeroage.squarez.model;

public class Figure3 extends Figure
{
    private static int[] figures = {
            463, 434, 1054, 506, 214, 509, 467, 1063,
            411, 94, 510, 159, 443, 1057, 1047,
            247, 1180, 250, 56, 95, 1178, 189, 466,
            486, 186, 79, 471, 1052, 319, 179
    };

    private static int RING_OF_SHIELD = 495; // 491 also may be used

    public static final int EXTRA_BLOCK_RATE = 10;

    private static final BlockType[] EXTRA_BLOCKS = {
            BlockType.BOMB, BlockType.CRACKED, BlockType.MISSILE, BlockType.SHIELD, BlockType.STEEL_PYRAMID
    };

    private boolean shiftOnRotate;

    public Figure3()
    {
        super(3);
    }

    public Figure3(Figure3 figure3)
    {
        super(figure3);
        this.setShiftOnRotate(figure3.isShiftOnRotate());
    }

    @Override
    public void generate()
    {
        int figureCode = figures[random.nextInt(figures.length)];
        if (figureCode > 1000) {
            shiftOnRotate = true;
            figureCode -= 1000;
        }

        fromCode(figureCode);

        if (prob(50)) {
            flipHorizontally();
        }
        if (prob(50)) {
            flipVertically();
        }
        if (prob(50)) {
            rotateLeft();
        }
        if (shiftOnRotate) {
            shift();
            shift();
        }
    }

    @Override
    public void rotateRight()
    {
        super.rotateRight();
        if (shiftOnRotate) {
            shift();
            shift();
        }
    }

    @Override
    public void rotateLeft()
    {
        super.rotateLeft();
        if (shiftOnRotate) {
            shift();
            shift();
        }
    }

    @Override
    public Figure3 copy()
    {
        return new Figure3(this);
    }

    private void shift()
    {
        boolean emptyRow = true;
        for (int y = 0; y < height; y++) {
            if (board[0][y] != null) {
                emptyRow = false;
            }
        }

        if (emptyRow) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width - 1; x++) {
                    board[x][y] = board[x + 1][y];
                }
                board[width - 1][y] = null;
            }
        }

        boolean emptyCol = true;
        for (int x = 0; x < width; x++) {
            if (board[x][0] != null) {
                emptyCol = false;
            }
        }

        if (emptyCol) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height - 1; y++) {
                    board[x][y] = board[x][y + 1];
                }
                board[x][height - 1] = null;
            }
        }
    }

    @Override
    protected Block makeBlock()
    {
        if (prob(EXTRA_BLOCK_RATE)) {
            BlockType extraBlock = EXTRA_BLOCKS[random.nextInt(EXTRA_BLOCKS.length)];
            return BlockFactory.make(extraBlock);
        }
        else {
            return BlockFactory.make(BlockType.BASIC);
        }
    }

    private boolean prob(int rate)
    {
        return random.nextInt(100) < rate;
    }

    public boolean isShiftOnRotate()
    {
        return shiftOnRotate;
    }

    public void setShiftOnRotate(boolean shiftOnRotate)
    {
        this.shiftOnRotate = shiftOnRotate;
    }
}
