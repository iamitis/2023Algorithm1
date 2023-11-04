/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96;
    private double[] fractionOfOpenSites;
    private int sizeOfGrid;
    private int numberOfTrials;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        sizeOfGrid = n;
        numberOfTrials = trials;
        fractionOfOpenSites = new double[numberOfTrials];
        for (int i = 0; i < trials; ++i) {
            fractionOfOpenSites[i] = experiment();
        }
    }

    private double experiment() {
        Percolation p = new Percolation(sizeOfGrid);
        while (!p.percolates()) {
            int row = StdRandom.uniformInt(1, sizeOfGrid + 1);
            int col = StdRandom.uniformInt(1, sizeOfGrid + 1);
            p.open(row, col);
        }
        return p.numberOfOpenSites() * 1.0 / (sizeOfGrid * sizeOfGrid);
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(fractionOfOpenSites);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return numberOfTrials == 1 ? Double.NaN : StdStats.stddev(fractionOfOpenSites);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - CONFIDENCE_95 * stddev() / Math.sqrt(numberOfTrials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + CONFIDENCE_95 * stddev() / Math.sqrt(numberOfTrials);
    }

    // test client (see below)
    public static void main(String[] args) {
        PercolationStats p = new PercolationStats(Integer.parseInt(args[0]),
                                                  Integer.parseInt(args[1]));
        StdOut.println("mean = " + p.mean());
        StdOut.println("stddev = " + p.stddev());
        StdOut.println(
                "95% confidence interval = [" + p.confidenceLo() + ", " + p.confidenceHi() + "]");
    }
}
