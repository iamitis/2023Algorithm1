/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

public class Test {
    public static void main(String[] args) {
        int n = 200;
        int trials = 100;
        PercolationStats p = new PercolationStats(n, trials);
        StdOut.println("mean = " + p.mean());
        StdOut.println("stddev = " + p.stddev());
        StdOut.println(
                "95% confidence interval = [" + p.confidenceLo() + ", " + p.confidenceHi() + "]");
    }
}
