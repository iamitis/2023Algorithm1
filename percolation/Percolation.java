/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] sites;
    private int sizeOfGrid;
    private int countOfOpenSites;
    private WeightedQuickUnionUF ufArray;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("size of grid " + n + " should be positive.");
        }
        sites = new boolean[n + 2][n + 2];
        sizeOfGrid = n;
        countOfOpenSites = 0;
        ufArray = new WeightedQuickUnionUF(n * n + 2);  // 多两个留给 top 和 bottom 以便后续判断上下连通
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);
        if (isOpen(row, col))
            return;
        sites[row][col] = true;
        countOfOpenSites += 1;
        // 若此 site 在第一排或最后一排，将其与 ufArray[0] 或 ufArray[end] 连通
        connectTopOrBottom(row, col);
        // 若相邻的 site isOpen，将它们连通
        connectNeighbour(row, col);
    }

    private void connectNeighbour(int row, int col) {
        if (sites[row - 1][col])
            ufArray.union(getUFArrayIndex(row, col), getUFArrayIndex(row - 1, col));
        if (sites[row + 1][col])
            ufArray.union(getUFArrayIndex(row, col), getUFArrayIndex(row + 1, col));
        if (sites[row][col - 1])
            ufArray.union(getUFArrayIndex(row, col), getUFArrayIndex(row, col - 1));
        if (sites[row][col + 1])
            ufArray.union(getUFArrayIndex(row, col), getUFArrayIndex(row, col + 1));
    }

    private void connectTopOrBottom(int row, int col) {
        if (row == 1)
            ufArray.union(getUFArrayIndex(row, col), 0);
        if (row == sizeOfGrid)
            ufArray.union(getUFArrayIndex(row, col), sizeOfGrid * sizeOfGrid + 1);
    }

    // 获取二维 grid 中的 site 在一维 ufArray 中对应的 index
    private int getUFArrayIndex(int row, int col) {
        return sizeOfGrid * (row - 1) + col;
    }

    // 判断合法坐标
    private void validate(int row, int col) {
        if (row <= 0 || row > sizeOfGrid || col <= 0 || col > sizeOfGrid) {
            throw new IllegalArgumentException(
                    "index (" + row + ", " + col + ") is not between 0 and " + (sizeOfGrid - 1));
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return sites[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);
        return ufArray.find(getUFArrayIndex(row, col)) == ufArray.find(0);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return countOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return ufArray.find(0) == ufArray.find(sizeOfGrid * sizeOfGrid + 1);
    }
}
