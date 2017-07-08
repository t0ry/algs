import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public final class Board {

  private final int[] blocks;
  private final int dimension;
  private final List<Board> neighboars = new LinkedList<>();
  private int hamming = -1; // initial values means not calculated yet
  private int manhattan = -1;
  private Board twin;

  /**
   * construct a board from an n-by-n array of blocks (where blocks[i][j] = block in row i, column j)
   * 
   * @param blocks
   */
  public Board(int[][] blocks) {
    if (blocks == null) {
      throw new IllegalArgumentException("blocks can't be null");
    }
    this.dimension = blocks.length;
    this.blocks = new int[this.dimension * this.dimension];
    for (int i = 0; i < this.blocks.length; i++) {
      this.blocks[i] = blocks[i / this.dimension][i % this.dimension];
    }

  }

  private int getRow(int i) {
    return i / this.dimension;

  }

  private int getCol(int i) {
    return i % this.dimension;

  }

  private int[][] getCopyAsBlocks() {
    int[][] newBlocks = new int[this.dimension][this.dimension];
    for (int i = 0; i < this.dimension; i++) {
      newBlocks[i] = Arrays.copyOfRange(this.blocks, i * this.dimension, i * this.dimension + this.dimension);
    }

    return newBlocks;
  }

  private void embarkNeighbor(int fromRow, int fromCol, int blankRow, int blankCol) {
    if (fromRow < 0 || fromRow > this.dimension - 1 || fromCol < 0 || fromCol > this.dimension - 1) {
      return;
    }

    int[][] newBlocks = this.getCopyAsBlocks();

    newBlocks[blankRow][blankCol] = newBlocks[fromRow][fromCol];
    newBlocks[fromRow][fromCol] = 0;
   
    Board newBoard = new Board(newBlocks);
    this.neighboars.add(newBoard);
   
  }

  /**
   * board dimension n
   * 
   * @return
   */
  public int dimension() {
    return this.dimension;
  }

  /**
   * number of blocks out of place
   * 
   * @return
   */
  public int hamming() {
    if (this.hamming > -1) { // already calculated
      return this.hamming;
    }
    this.hamming = 0;

    for (int i = 0; i < this.blocks.length - 1; i++) {
      if (i + 1 != this.blocks[i]) {
        this.hamming++;
      }
    }

    return this.hamming;
  }

  /**
   * sum of Manhattan distances between blocks and goal
   * 
   * @return
   */
  public int manhattan() {
    if (this.manhattan > -1) { // already calculated
      return this.manhattan;
    }
    this.manhattan = 0;

    for (int i = 0; i < this.blocks.length; i++) {
      if (this.blocks[i] == 0) {
        continue;
      }

      int iRow = this.getRow(i);
      int iCol = this.getCol(i);

      int goalRow = this.getRow(this.blocks[i] - 1);
      int goalCol = this.getCol(this.blocks[i] - 1);

      int verticalDistance = Math.abs(iRow - goalRow);
      int horizintalDistance = Math.abs(iCol - goalCol);
      
      this.manhattan += verticalDistance + horizintalDistance;
    }

    return this.manhattan;
  }

  /**
   * is this board the goal board?
   * 
   * @return
   */
  public boolean isGoal() {
    return this.hamming() == 0;
  }

  /**
   * a board that is obtained by exchanging any pair of blocks
   * 
   * @return
   */
  public Board twin() {
    if (this.twin != null) {
      return this.twin;
    }

    int[][] copyAsBlocks = this.getCopyAsBlocks();

    int iRow = -1;
    int iCol = -1;

    int nextRow = -1;
    int nextCol = -1;

    for (int i = 0; i < this.blocks.length - 1; i++) {
      if (this.blocks[i] != 0 && this.blocks[i + 1] != 0) {
        iRow = this.getRow(i);
        iCol = this.getCol(i);

        nextRow = this.getRow(i + 1);
        nextCol = this.getCol(i + 1);

        break;
      }
    }

    int temp = copyAsBlocks[iRow][iCol];
    copyAsBlocks[iRow][iCol] = copyAsBlocks[nextRow][nextCol];
    copyAsBlocks[nextRow][nextCol] = temp;

    this.twin = new Board(copyAsBlocks);
    return twin;

  }

  /**
   * does this board equal y?
   */
  @Override
  public boolean equals(Object y) {
    if (y == null) {
      return false;
    }

    if (!(y.getClass().equals(Board.class))) {
      return false;
    }

    if (this.dimension() != ((Board) y).dimension()) {
      return false;
    }

    Board that = (Board) y;
    for (int i = 0; i < this.blocks.length; i++) {
      if (this.blocks[i] != that.blocks[i]) {
        return false;
      }
    }

    return true;
  }

  /**
   * all neighboring boards
   * 
   * @return
   */
  public Iterable<Board> neighbors() {
    if (this.neighboars.size() > 0) {
      return this.neighboars;
    }

    int blankRow = -1;
    int blankCol = -1;

    int i = 0;
    while (i < this.blocks.length && this.blocks[i] != 0) {
      i++;
    }
    blankRow = this.getRow(i);
    blankCol = this.getCol(i);
    
    this.embarkNeighbor(blankRow - 1, blankCol, blankRow, blankCol);
    this.embarkNeighbor(blankRow, blankCol - 1, blankRow, blankCol);
    this.embarkNeighbor(blankRow + 1, blankCol, blankRow, blankCol);
    this.embarkNeighbor(blankRow, blankCol + 1, blankRow, blankCol);

    return this.neighboars;

  }

  /**
   * string representation of this board (in the output format specified below)
   */
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("\n " + this.dimension() + "\n");
    for (int i = 0; i < this.blocks.length; i++) {

      sb.append(" ");
      sb.append(this.blocks[i]);
      sb.append(" ");
      if (this.getCol(i) == this.dimension - 1) {
        sb.append("\n");
      }
    }

    return sb.toString();
  }

}
