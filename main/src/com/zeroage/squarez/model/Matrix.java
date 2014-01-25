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
                Matrix.this.set(x, y, BlockFactory.copy(block));
            }
        });
    }

    public void set(int x, int y, Block block)
    {
        if (x >= 0 && y >= 0 && x < width && y < height) {
            board[x][y] = block;
        }
    }

    public Block get(int x, int y)
    {
        return (x >= 0 && y >= 0 && x < width && y < height) ?  board[x][y] : null;
    }

    public void fill(int x, int y, int width, int height, BlockType blockType)
    {
        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                set(i, j, BlockFactory.make(blockType));
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
                Block block = get(i, j);
                if ((block == null && blockType != BlockType.EMPTY) || (block != null && block.getType() != blockType)) {
                    return false;
                }
            }
        }
        return true;
    }


    public <T> T iterate(Callback<T> callback) {
        if (callback instanceof ValueReturningCallback) {
            ValueReturningCallback<T> valueReturningCallback = (ValueReturningCallback<T>) callback;
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    valueReturningCallback.cell(i, j, get(i,j));
                    if (valueReturningCallback.isValueReturned()) {
                        return valueReturningCallback.getValue();
                    }
                }
            }
        }
        else {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    callback.cell(i, j, get(i,j));
                }
            }
        }
        return null;
    }

    public <M extends Matrix<T>> M copy()
    {
        return (M) new Matrix<T>(this);
    }

    public static interface Callback<T>
    {
        public void cell(int x, int y, Block block);
    }

    public static interface ValueReturningCallback<T> extends Callback<T>
    {
        boolean isValueReturned();
        T getValue();
    }

    public static abstract class  AbstractValueReturningCallback<T> implements ValueReturningCallback<T>
    {
        private T value;
        private T defaultValue;

        protected AbstractValueReturningCallback(T defaultValue)
        {
            this.defaultValue = defaultValue;
        }

        protected void returnValue(T value)
        {
            this.value = value;
        }

        @Override
        public boolean isValueReturned()
        {
            return value != null;
        }

        @Override
        public T getValue()
        {
            return value != null ? value : defaultValue;
        }
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    @Override
    public String toString()
    {
        StringBuilder b = new StringBuilder((width + 1) * height);
        b.append("\n");
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                b.append(get(i, j) == null ? "." : get(i, j));
            }
            b.append("\n");
        }
        b.append("\n");

        return b.toString();
    }

    @Override
    public boolean equals(Object obj)
    {
        Boolean isEquals = false;
        if (obj instanceof Matrix) {
            final Matrix that = (Matrix) obj;
            if (that.width == this.width && that.height == this.height) {
                isEquals = iterate(new AbstractValueReturningCallback<Boolean>(false)
                {
                    @Override
                    public void cell(int x, int y, Block block)
                    {
                        if ((block == null && that.get(x, y) == null)  ||
                            (block != null && that.get(x, y) != null && block.getType() == that.get(x, y).getType())) {
                            returnValue(true);
                        }
                    }
                });
            }
        }

        return isEquals;
    }

    @Override
    public int hashCode()
    {
        StringBuilder b = new StringBuilder((width + 1) * height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                b.append(get(i, j) == null ? "." : "x");
            }
        }

        return b.toString().hashCode();
    }
}
