import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
  private  int virtualTopIndex = 0;
  private int virtualBottomIndex;

  private int[] sites;
  private int initialSize;
  private int openedNumber = 0;

  private final WeightedQuickUnionUF alg;

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
    // and virtual bottom with index n*n + 1 this.sites = new int[n * n + 2];
    this.sites = new int [this.initialSize * this.initialSize + 2];
    this.virtualBottomIndex = this.sites.length - 1;

    // init all but virtual top, virtual bottom as blocked: -100
    for (int i = 1; i <= this.sites.length - 2; i++) {
      this.sites[i] = -100;
    }

    // open virtual bottom as connected to itself
    this.sites[virtualBottomIndex] = virtualBottomIndex;

    this.alg = new WeightedQuickUnionUF(this.sites.length);

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

    this.sites[currentIndex] = -1; // open current site
    this.openedNumber++;

    // automatically connect top row to virtual top and bottom row to bottom
    if (row == 1) {
      this.alg.union(currentIndex, virtualTopIndex);
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
      this.alg.union(topIndex, currentIndex);
    }

    // connect bottom site if exists and open
    if (row < this.initialSize && isOpen(row + 1, col)) {
      this.alg.union(bottomIndex, currentIndex);
    }

    // connect left site if exists and open
    if (col > 1 && isOpen(row, col - 1)) {
      this.alg.union(leftIndex, currentIndex);
    }

    // connect right site if exists and open
    if (col < initialSize && isOpen(row, col + 1)) {
      this.alg.union(rightIndex, currentIndex);
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

    return this.sites[this.calcIndex(row, col)] > -100;
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

    return this.alg.connected(this.calcIndex(row, col), virtualTopIndex);
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
    return this.alg.connected(virtualTopIndex, virtualBottomIndex);
  }

}
