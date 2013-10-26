package com.zeroage.squarez.model;

public class Matrix<T>
{
    protected final int width;
    protected final int height;

    protected Block[][] board;

    public Matrix(int width, int height)
    {
        this.height = height;
        this.width = width;

        board = new Block[width][height];
    }

    public Matrix(Matrix matrix)
    {
        this(matrix.width, matrix.height);

        matrix.iterate(new Callback()
        {
            @Override
            public void cell(int x, int y, Block block)
            {
                Matrix.this.set(x, y, block);
            }
        });
    }

    public void set(int x, int y, Block block)
    {
        if (x >= 0 && y >= 0 && x < width && y < width) {
            board[x][y] = block;
        }
    }

    public Block get(int x, int y)
    {
        return (x >= 0 && y >= 0 && x < width && y < width) ?  board[x][y] : null;
    }

    public void fill(int x, int y, int width, int height, BlockType blockType)
    {
        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                set(i, j, blockType.make());
            }
        }
    }

    public void clear() {
        board = new Block[width][height];
    }

    public boolean isFilledWith(int x, int y, int w, int h, BlockType blockType)
    {
        for (int i = x; i < x + w; i++) {
            for (int j = y; j < y + h; j++) {
                if (!blockType.isOfType(get(i, j))) {
                    return false;
                }
            }
        }
        return true;
    }


    public void iterate(Callback callback) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                callback.cell(i, j, get(i,j));
            }
        }
    }

    public static interface Callback
    {
        public void cell(int x, int y, Block block);
    }

    @Override
    public String toString()
    {
        StringBuilder b = new StringBuilder((width + 1) * height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                b.append(get(i, j) == null ? "." : get(i, j));
            }
            b.append("\n");
        }

        return b.toString();
    }
}
