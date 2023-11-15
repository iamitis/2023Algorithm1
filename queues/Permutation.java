/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue<String> test = new RandomizedQueue<>();
        int n = Integer.parseInt(String.valueOf(args[0]));
        while (!StdIn.isEmpty()) {
            test.enqueue(StdIn.readString());
        }
        Iterator<String> iter = test.iterator();
        for (int i = 0; i < n; i++) {
            StdOut.println(iter.next());
        }
    }
}
