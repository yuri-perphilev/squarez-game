package com.zeroage.squarez.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Figure extends Matrix
{

    public static final int MIN_FIG_BLOCKS = 3;

    protected final Random random = new Random();

    private boolean useUniformDistribution = false;


    public Figure(int size)
    {
        super(size, size);
        generate();
    }

    public Figure(int size, boolean useUniformDistribution)
    {
        super(size, size);
        this.useUniformDistribution = useUniformDistribution;
        generate();
    }


    public Figure(Figure figure)
    {
        super(figure);
    }

    public void generate()
    {
        List<int[]> list = new ArrayList<int[]>();

        // random from range 3..9

        int max = width * height - MIN_FIG_BLOCKS;

        int n = useUniformDistribution ?
                random.nextInt(max) + MIN_FIG_BLOCKS : // uniform distribution
                (int) ((random.nextDouble() + random.nextDouble()) * max / 2 + MIN_FIG_BLOCKS); // triangular distribution

        for (int i = 0; i < n; i++) {
            if (list.isEmpty()) {
                int[] p = randomPoint();
                list.add(p);
                set(p[0], p[1], makeBlock());
            }
            else {
                List<int[]> neighbours;
                do {
                    int m = random.nextInt(list.size());
                    int[] p = list.get(m);
                    neighbours = findFreeNeighbours(p[0], p[1]);
                }
                while (neighbours.isEmpty());

                int m = random.nextInt(neighbours.size());
                int[] p = neighbours.get(m);

                list.add(p);
                set(p[0], p[1], makeBlock());
            }
        }
    }

    protected Block makeBlock()
    {
        return BlockFactory.make(BlockType.BASIC);
    }

    public List<int[]> findFreeNeighbours(int x, int y)
    {
        int[][] offsets = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        List<int[]> result = new ArrayList<int[]>();

        for (int[] offset : offsets) {
            int x1 = x + offset[0];
            int y1 = y + offset[1];
            if (x1 < width && x1 >= 0 &&
                y1 < height && y1 >= 0 &&
                get(x1, y1) == null)
            {
                result.add(new int[]{x1, y1});
            }
        }

        return result;
    }

    private int[] randomPoint()
    {
        return new int[]{random.nextInt(width), random.nextInt(height)};
    }

    public void rotateRight()
    {
        final Block[][] fig = new Block[width][height];
        iterate(new Callback()
        {
            @Override
            public void cell(int x, int y, Block block)
            {
                if (block != null) {
                    block.rotateRight();
                }
                fig[(width - 1) - y][x] = block;
            }
        });
        this.board = fig;
    }

    public void rotateLeft()
    {
        final Block[][] fig = new Block[width][height];
        iterate(new Callback()
        {
            @Override
            public void cell(int x, int y, Block block)
            {
                if (block != null) {
                    block.rotateLeft();
                }
                fig[y][(width - 1) - x] = block;
            }
        });
        this.board = fig;
    }

    public void shiftLeft()
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
    }

    public void shiftUp()
    {
        rotateRight();
        shiftLeft();
        rotateLeft();
    }

    public void shiftRight()
    {
        rotateRight();
        rotateRight();
        shiftLeft();
        rotateLeft();
        rotateLeft();
    }

    public void shiftDown()
    {
        rotateLeft();
        shiftLeft();
        rotateRight();
    }

    public void flipHorizontally()
    {
        final Block[][] fig = new Block[width][height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                fig[x][y] = board[width - x - 1][y];
            }
        }

        board = fig;
    }

    public void flipVertically()
    {
        final Block[][] fig = new Block[width][height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                fig[x][y] = board[x][height- y - 1];
            }
        }

        board = fig;
    }

    public int toCode() {
        StringBuilder b = new StringBuilder((width + 1) * height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                b.append(get(i, j) == null ? "0" : "1");
            }
        }

        return Integer.parseInt(b.toString(), 2);
    }

    public void fromCode(int n)
    {
        final Block[][] fig = new Block[width][height];

        for (int y = height - 1; y >= 0; y--) {
            for (int x = width - 1; x >= 0; x--) {
                if ((n & 1) == 1) {
                    fig[x][y] = makeBlock();
                }
                n >>= 1;
            }
        }
        board = fig;
    }
}
