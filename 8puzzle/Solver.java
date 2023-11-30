/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Solver {

    private GameNode headOfGT;
    private int moves;

    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        checkAndSolve(initial);
    }


    public boolean isSolvable() {
        return moves != -1;
    }

    public int moves() {
        return moves;
    }

    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }
        return new SolIterable();
    }

    private class SolIterable implements Iterable<Board> {
        public Iterator<Board> iterator() {
            return new SolIterator();
        }

        class SolIterator implements Iterator<Board> {
            private GameNode current = headOfGT;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public Board next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Board board = current.board;
                current = current.next;
                return board;
            }
        }
    }

    private class GameNode {
        int priority;
        Board board;
        GameNode next;

        GameNode(Board b, int moves) {
            board = b;
            priority = moves + b.manhattan();
            next = null;
        }
    }

    private Comparator<GameNode> priorOrder() {
        return new PriorCmp();
    }

    private class PriorCmp implements Comparator<GameNode> {
        public int compare(GameNode o1, GameNode o2) {
            return Integer.compare(o1.priority, o2.priority);
        }
    }

    private void checkAndSolve(Board initial) {
        MinPQ<GameNode>[] pq = new MinPQ[] {
                new MinPQ<GameNode>(priorOrder()), new MinPQ<GameNode>(priorOrder())
        };
        Board twin = initial.twin();
        pq[0].insert(new GameNode(initial, 0));
        pq[1].insert(new GameNode(twin, 0));
        GameNode[] heads = new GameNode[] { pq[0].delMin(), pq[1].delMin() };
        GameNode[] tails = new GameNode[] { heads[0], heads[1] };
        int checking = 0;
        GameNode[] previous = new GameNode[] { heads[0], heads[1] };
        int[] steps = new int[] { 0, 0 };
        while (!tails[checking].board.isGoal()) {
            for (Board neighbour : tails[checking].board.neighbors()) {
                if (!neighbour.equals(previous[checking].board)) {
                    pq[checking].insert(new GameNode(neighbour, steps[checking] + 1));
                }
            }
            tails[checking].next = pq[checking].delMin();
            tails[checking] = tails[checking].next;
            if (steps[checking] >= 2) {
                previous[checking] = previous[checking].next;
            }
            while (!pq[checking].isEmpty()) {
                pq[checking].delMin();
            }
            ++steps[checking];
            checking = 1 - checking;
        }
        if (checking == 0) {    // this is solvable
            moves = steps[checking];
            headOfGT = heads[0];
        }
        else {                // twin is solvable and this is unsolvable
            moves = -1;
        }
    }


    public static void main(String[] args) {

        // create initial board from file
        In in = new In("./input/puzzle3x3-unsolvable.txt");
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
