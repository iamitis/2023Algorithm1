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
        MinPQ<GameNode> pq = new MinPQ<>(priorOrder());
        pq.insert(new GameNode(initial, 0));
        headOfGT = pq.delMin();
        GameNode tailOfGT = headOfGT;

        // solve
        moves = 0;
        GameNode previous = headOfGT;
        while (!tailOfGT.board.isGoal()) {
            for (Board neighbour : tailOfGT.board.neighbors()) {
                if (!neighbour.equals(previous.board)) {
                    pq.insert(new GameNode(neighbour, moves + 1));
                }
            }
            tailOfGT.next = pq.delMin();
            tailOfGT = tailOfGT.next;
            if (moves >= 2) {
                previous = tailOfGT;
            }
            while (!pq.isEmpty()) {
                pq.delMin();
            }
            ++moves;
            if (moves > 2 * (tailOfGT.board.dimension() + 1) * (tailOfGT.board.dimension() - 1)) {
                moves = -1;
                break;
            }
        }
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

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
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
