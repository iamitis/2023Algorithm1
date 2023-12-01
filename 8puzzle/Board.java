/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author iamitis
 */

public class Board {

    private int[][] puzzle;
    private int dimension;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        dimension = tiles[0].length;
        puzzle = new int[dimension][dimension];
        // 没搞懂java复制二维数组的规则……
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                puzzle[i][j] = tiles[i][j];
            }
        }
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dimension);
        for (int i = 0; i < dimension; ++i) {
            s.append(System.lineSeparator());
            for (int j = 0; j < dimension; ++j) {
                s.append(" ").append(puzzle[i][j]);
            }
        }
        return s.toString();
    }

    public int dimension() {
        return dimension;
    }

    // number of tiles out of place
    public int hamming() {
        int numOfOut = 0;
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                numOfOut += (puzzle[i][j] == 0 || puzzle[i][j] == properNum(i, j)) ? 0 : 1;
            }
        }
        return numOfOut;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int distance = 0;
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                if (puzzle[i][j] == 0) {
                    continue;
                }
                distance += Math.abs(i - properPos(puzzle[i][j])[0]) + Math.abs(
                        j - properPos(puzzle[i][j])[1]);
            }
        }
        return distance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                if (puzzle[i][j] != properNum(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }
        if (this == y) {
            return true;
        }
        if (y.getClass() != this.getClass()) {
            return false;
        }
        Board that = (Board) y;
        if (that.dimension != dimension) {
            return false;
        }
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                if (that.puzzle[i][j] != puzzle[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public Iterable<Board> neighbors() {
        return new NghbIterable();
    }

    public Board twin() {
        int[][] twin = new int[dimension][dimension];
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                twin[i][j] = puzzle[i][j];
            }
        }
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension - 1; ++j) {
                if (twin[i][j] != 0 && twin[i][j + 1] != 0) {
                    exch(twin, i, j, i, j + 1);
                    return new Board(twin);
                }
            }
        }
        return null;
    }

    private class NghbIterable implements Iterable<Board> {
        private ArrayList<int[][]> neighbours = getNeighbours();

        public Iterator<Board> iterator() {
            return new NghbIterator();
        }

        class NghbIterator implements Iterator<Board> {
            @Override
            public boolean hasNext() {
                return !neighbours.isEmpty();
            }

            @Override
            public Board next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return new Board(neighbours.remove(neighbours.size() - 1));
            }
        }
    }

    private int properNum(int row, int col) {
        if (row == dimension - 1 && col == dimension - 1) {
            return 0;
        }
        return row * dimension + col + 1;
    }

    private int[] properPos(int num) {
        if (num == 0) {
            return new int[] { dimension - 1, dimension - 1 };
        }
        return new int[] { (num - 1) / dimension, (num - 1) % dimension };
    }

    private ArrayList<int[][]> getNeighbours() {
        ArrayList<int[][]> nghbs = new ArrayList<>();
        int[] pos0 = pos0();
        int[][] arround = new int[][] { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };
        for (int i = 0; i < 4; ++i) {
            int nghbPosR = pos0[0] + arround[i][0];
            int nghbPosC = pos0[1] + arround[i][1];
            if (nghbPosR >= 0 && nghbPosR < dimension
                    && nghbPosC >= 0 && nghbPosC < dimension) {
                int[][] nghb = new int[dimension][dimension];
                for (int j = 0; j < dimension; ++j) {
                    for (int k = 0; k < dimension; ++k) {
                        nghb[j][k] = puzzle[j][k];
                    }
                }
                exch(nghb, nghbPosR, nghbPosC, pos0[0], pos0[1]);
                nghbs.add(nghb);
            }
        }
        return nghbs;
    }

    private int[] pos0() {
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                if (puzzle[i][j] == 0) {
                    return new int[] { i, j };
                }
            }
        }
        return new int[] { -1, -1 };
    }

    private void exch(int[][] p, int r1, int c1, int r2, int c2) {
        int temp = p[r1][c1];
        p[r1][c1] = p[r2][c2];
        p[r2][c2] = temp;
    }

    public static void main(String[] args) {
        ArrayList<Integer> a = new ArrayList<>();
        a.add(0);
        a.add(1);
        StdOut.println(a.contains(1));
    }
}
