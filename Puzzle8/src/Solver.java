import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public final class Solver {
  private final List<Board> solution = new LinkedList<>();
  private final List<Board> twinSolution = new LinkedList<>();
  private int moves = 0;
  private boolean solvable = false;

  /**
   * find a solution to the initial board (using the A* algorithm)
   * 
   * @param initial
   */
  public Solver(Board initial) {
    if (initial == null) {
      throw new IllegalArgumentException("Initial board can't be null");
    }
    this.solution.add(initial);

    if (initial.isGoal()) {
      this.solvable = true;
      return;
    }

    this.twinSolution.add(initial.twin());
    if (initial.twin().isGoal()) {
      setUnsolved();
      return;
    }

    final MinPQ<Board> pq = new MinPQ<>();
    final MinPQ<Board> twinPQ = new MinPQ<>();

    pq.insert(initial);
    twinPQ.insert(initial.twin());
    trySolve(pq, twinPQ);
  }

  private void trySolve(MinPQ<Board> pq, MinPQ<Board> twinPQ) {

    Board searchNode = moveBlock(pq, solution, null);
 //   Board twinSearchNode = moveBlock(twinPQ, twinSolution, null);
    System.out.println(searchNode);


    while (!(searchNode.isGoal() /*|| twinSearchNode.isGoal()*/)) {
      this.moves++;
      searchNode = moveBlock(pq, solution, searchNode);
      //twinSearchNode = moveBlock(twinPQ, twinSolution, twinSearchNode);
      System.out.println(searchNode);

      try {
        System.in.read();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    if (searchNode.isGoal()) {
      this.solvable = true;
      return;
    }
//    } else if (twinSearchNode.isGoal()) {
//      setUnsolved();
//      return;
//    }
  }

  private Board moveBlock(MinPQ<Board> workingQueue, List<Board> workingSolution, Board previous) {
    Board searchNode = workingQueue.delMin();
    
    if (searchNode.isGoal()) {
      return searchNode;
    }

    Iterator<Board> iterator = searchNode.neighbors().iterator();

    Board prefferedNeighbor = null;
    while (iterator.hasNext()) {
      Board neighbor = iterator.next();

      // check for previous
      if (neighbor.equals(previous)) {
        continue;
      }

      if (prefferedNeighbor == null) {
        prefferedNeighbor = neighbor;
        continue;
      }

      int currentPriority = this.moves + neighbor.manhattan();
      int minPriority = this.moves + prefferedNeighbor.manhattan();
      System.out.println(neighbor);
      System.out.println(currentPriority);
      if (currentPriority < minPriority) {
        prefferedNeighbor = neighbor;
      }
    }
    workingQueue.insert(prefferedNeighbor);
    workingSolution.add(prefferedNeighbor);

    return searchNode;

  }

  private void setUnsolved() {
    this.solvable = false;
    this.solution.clear();
    this.moves = -1;
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
    return this.moves;
  }

  /**
   * sequence of boards in a shortest solution; null if unsolvable
   * 
   * @return
   */
  public Iterable<Board> solution() {
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