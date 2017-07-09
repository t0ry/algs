import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public final class Solver {
  private List<Board> solution = null;
  private boolean solvable = false;
  private SearchNode goalSearchNode;

  /**
   * find a solution to the initial board (using the A* algorithm)
   * 
   * @param initial
   */
  public Solver(Board initial) {
    if (initial == null) {
      throw new IllegalArgumentException("Initial board can't be null");
    }

    if (initial.isGoal()) {
      this.goalSearchNode = new SearchNode(initial);
      this.solvable = true;
      return;
    }

    if (initial.twin().isGoal()) {
      setUnsolved();
      return;
    }

    trySolve(initial);
  }

  private void trySolve(Board initial) {
    final MinPQ<SearchNode> pq = new MinPQ<>();
    final MinPQ<SearchNode> twinPQ = new MinPQ<>();

    pq.insert(new SearchNode(initial));
    twinPQ.insert(new SearchNode(initial.twin()));

    SearchNode searchNode = moveBlock(pq);
    SearchNode twinSearchNode = moveBlock(twinPQ);
    
    while (!(searchNode.board.isGoal() || twinSearchNode.board.isGoal())) {
      searchNode = moveBlock(pq);
      twinSearchNode = moveBlock(twinPQ);
    }
    if (searchNode.board.isGoal()) {
      this.goalSearchNode = searchNode;
      this.solvable = true;

    } else if (twinSearchNode.board.isGoal()) {
      this.setUnsolved();
    }
  }

  private SearchNode moveBlock(MinPQ<SearchNode> workingQueue) {
    SearchNode searchNode = workingQueue.delMin();
    if (searchNode.board.isGoal()) {
      return searchNode;
    }

    Iterator<Board> iterator = searchNode.board.neighbors().iterator();
    while (iterator.hasNext()) {
      Board neighbor = iterator.next();

      if (searchNode.isNeighborAccptable(neighbor)) {
        workingQueue.insert(new SearchNode(neighbor, searchNode));
      }
    }

    return searchNode;

  }

  private void setUnsolved() {
    this.solvable = false;
    this.solution = null;
    this.goalSearchNode = null;

  }

  private class SearchNode implements Comparable<SearchNode> {
    private final Board board;
    private SearchNode previous;
    private int moves;

    public SearchNode(Board board, SearchNode previous) {
      this.board = board;
      this.previous = previous;
      this.moves = this.previous.moves + 1;

    }

    public SearchNode(Board board) {
      this.board = board;

    }

    public boolean isNeighborAccptable(Board neighbor) {
      if (this.previous == null) {
        return true;
      }

      return !this.previous.board.equals(neighbor);
    }

    @Override
    public int compareTo(SearchNode searchNodeToCompare) {
      if (this.board.manhattan() + this.moves < searchNodeToCompare.board.manhattan() + searchNodeToCompare.moves) {
        return -1;
      }
      if (this.board.manhattan() + this.moves > searchNodeToCompare.board.manhattan() + searchNodeToCompare.moves) {
        return 1;
      }
      return 0;
    }

  }

  /**
   * is the initial board solvable?
   * 
   * @return
   */
  public boolean isSolvable() {
    return this.solvable;
  }

  /**
   * min number of moves to solve initial board; -1 if unsolvable
   * 
   * @return
   */
  public int moves() {
    if (!isSolvable()) {
      return -1;
    }
    return this.goalSearchNode.moves;
  }

  /**
   * sequence of boards in a shortest solution; null if unsolvable
   * 
   * @return
   */
  public Iterable<Board> solution() {
    if (!isSolvable()) {
      return null;
    }

    if (this.solution == null) {
      this.solution = new LinkedList<>();

      SearchNode currentBW = this.goalSearchNode;
      this.solution.add(currentBW.board);

      while (currentBW.previous != null) {
        currentBW = currentBW.previous;
        this.solution.add(0, currentBW.board);
      }
    }

    List<Board> toReturn = new LinkedList<>(this.solution);
    return toReturn;
  }

  /**
   * solve a slider puzzle (given below)
   * 
   * @param args
   */
  public static void main(String[] args) {
    // create initial board from file
    In in = new In(args[0]);
    int n = in.readInt();
    int[][] blocks = new int[n][n];
    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++)
        blocks[i][j] = in.readInt();
    Board initial = new Board(blocks);

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