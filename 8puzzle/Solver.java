/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.LinkedList;

/**
 * @author iamitis
 */
public class Solver {

    private GameNode tail;
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
        LinkedList<Board> solution = new LinkedList<>();
        GameNode head = tail;
        while (head != null) {
            solution.addFirst(head.board);
            head = head.prev;
        }
        return solution;
    }

    private class GameNode {
        int moves;
        Board board;
        GameNode prev;

        GameNode(Board b, GameNode prev, int moves) {
            board = b;
            this.moves = moves;
            this.prev = prev;
        }

        int priority() {
            return moves + board.manhattan();
        }
    }

    private Comparator<GameNode> priorOrder() {
        return new PriorCmp();
    }

    private class PriorCmp implements Comparator<GameNode> {
        public int compare(GameNode o1, GameNode o2) {
            return Integer.compare(o1.priority(), o2.priority());
        }
    }

    /**
     * 对this和twin做A* Search，检查是否可解。
     * GameTree中可能不止有一条路径，其中包含最短路径。
     * 因为pq中可能包含priority相同的节点，所以pq.delMin()出来的可能不是上一个searchNode的子节点，
     * 而可能是另一条路径的子节点。因而要把moves和prev作为GameNode的成员变量而非局部变量。
     *
     * @param initial initial board
     */
    private void checkAndSolve(Board initial) {
        MinPQ<GameNode>[] pq = new MinPQ[] {
                new MinPQ<GameNode>(priorOrder()), new MinPQ<GameNode>(priorOrder())
        };
        Board twin = initial.twin();
        pq[0].insert(new GameNode(initial, null, 0));
        pq[1].insert(new GameNode(twin, null, 0));
        GameNode[] searchNode = new GameNode[] { pq[0].delMin(), pq[1].delMin() };
        int checking = 0;
        while (!searchNode[checking].board.isGoal()) {
            for (Board neighbour : searchNode[checking].board.neighbors()) {
                if (searchNode[checking].prev == null || !neighbour.equals(
                        searchNode[checking].prev.board)) {
                    pq[checking].insert(new GameNode(neighbour, searchNode[checking],
                                                     searchNode[checking].moves + 1));
                }
            }
            searchNode[checking] = pq[checking].delMin();
            checking = 1 - checking;
        }
        if (checking == 0) {    // this is solvable
            moves = searchNode[checking].moves;
            tail = searchNode[0];
        }
        else {                // twin is solvable and this is unsolvable
            moves = -1;
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
