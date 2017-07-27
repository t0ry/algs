import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
  private static final int VIRTUAL_TOP_INDEX = 0;
  private final int virtualBottomIndex;

  private boolean[] sites;
  private final int initialSize;
  private int openedNumber = 0;

  private final WeightedQuickUnionUF alg;
  private final WeightedQuickUnionUF algFull;

  /**
   * Constructor create n-by-n grid, with all sites blocked
   * 
   * @param n
   */
  public Percolation(int n) {
    if (n <= 0) {
      throw new IllegalArgumentException("[n] must be > 0");
    }

    this.initialSize = n;

    // two elements are added: virtual top with index 0
    // and virtual bottom with index n*n + 1 this.sites = new boolean[n * n + 2];
    this.sites = new boolean[this.initialSize * this.initialSize + 2];
    this.virtualBottomIndex = this.sites.length - 1;

    // open virtual bottom as connected to itself
    // this.sites[virtualBottomIndex] = true;
    // this.sites[VIRTUAL_TOP_INDEX] = true;

    this.alg = new WeightedQuickUnionUF(this.sites.length);
    this.algFull = new WeightedQuickUnionUF(this.sites.length - 1);

  }

  private void validateRowCol(int row, int col) {
    if (row <= 0 || col <= 0 || row > this.initialSize || col > this.initialSize) {
      throw new IllegalArgumentException("[row] and [col] must be >= 1 and <= n. n=" + this.initialSize);
    }
  }

  private int calcIndex(int row, int col) {
    return (row - 1) * this.initialSize + col;
  }

  /**
   * open site (row, col) if it is not open already
   * 
   * @param row
   * @param col
   */
  public void open(int row, int col) {
    this.validateRowCol(row, col);

    if (isOpen(row, col)) {
      return;
    }

    final int currentIndex = this.calcIndex(row, col);

    this.sites[currentIndex] = true; // open current site
    this.openedNumber++;

    // automatically connect top row to virtual top and bottom row to bottom
    if (row == 1) {
      this.alg.union(currentIndex, VIRTUAL_TOP_INDEX);
      this.algFull.union(currentIndex, VIRTUAL_TOP_INDEX);
    }
    if (row == this.initialSize) {
      this.alg.union(currentIndex, virtualBottomIndex);
    }

    final int topIndex = this.calcIndex(row - 1, col);
    final int bottomIndex = this.calcIndex(row + 1, col);
    final int leftIndex = this.calcIndex(row, col - 1);
    final int rightIndex = this.calcIndex(row, col + 1);

    // connect top site if exists and open
    if (row > 1 && isOpen(row - 1, col)) {
      if (!this.alg.connected(topIndex, currentIndex)) {
        this.alg.union(topIndex, currentIndex);
      }
      if (!this.algFull.connected(topIndex, currentIndex)) {
        this.algFull.union(topIndex, currentIndex);
      }
    }

    // connect left site if exists and open
    if (col > 1 && isOpen(row, col - 1)) {
      if (!this.alg.connected(leftIndex, currentIndex)) {
        this.alg.union(leftIndex, currentIndex);
      }
      if (!this.algFull.connected(leftIndex, currentIndex)) {
        this.algFull.union(leftIndex, currentIndex);
      }
    }

    // connect right site if exists and open
    if (col < initialSize && isOpen(row, col + 1)) {
      if (!this.alg.connected(rightIndex, currentIndex)) {
        this.alg.union(rightIndex, currentIndex);
      }
      if (!this.algFull.connected(rightIndex, currentIndex)) {
        this.algFull.union(rightIndex, currentIndex);
      }
    }

    // connect bottom site if exists and open
    if (row < this.initialSize && isOpen(row + 1, col)) {
      if (!this.alg.connected(bottomIndex, currentIndex)) {
        this.alg.union(bottomIndex, currentIndex);
      }
      if (!this.algFull.connected(bottomIndex, currentIndex)) {
        this.algFull.union(bottomIndex, currentIndex);
      }
    }

  }

  /**
   * open site (row, col) if it is not open already
   * 
   * @param row
   * @param col
   * @return
   */
  public boolean isOpen(int row, int col) {
    this.validateRowCol(row, col);

    return this.sites[this.calcIndex(row, col)];
  }

  /**
   * is site (row, col) full?
   * 
   * @param row
   * @param col
   * @return
   */
  public boolean isFull(int row, int col) {
    this.validateRowCol(row, col);

    return isOpen(row, col) && this.algFull.connected(this.calcIndex(row, col), VIRTUAL_TOP_INDEX);
  }

  /**
   * number of open sites
   * 
   * @return
   */
  public int numberOfOpenSites() {
    return this.openedNumber;
  }

  /**
   * does the system percolate?
   * 
   * @return
   */
  public boolean percolates() {
    return this.alg.connected(VIRTUAL_TOP_INDEX, virtualBottomIndex);
  }

}
