import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
 private final double[] fractions;
 private final double mean;
 private final double stddev;

  /**
   * perform trials independent experiments on an n-by-n grid
   * 
   * @param n
   * @param trials
   */
  public PercolationStats(int n, int trials) {
    if (n <= 0 || trials <= 0) {
      throw new IllegalArgumentException("[n] and [trails] must be > 0");
    }

    this.fractions = new double[trials];

    for (int i = 0; i < trials; i++) {
      Percolation percolation = new Percolation(n);

      do {
        int row = StdRandom.uniform(1, n + 1);
        int col = StdRandom.uniform(1, n + 1);
        percolation.open(row, col);
      } while (!percolation.percolates());

      fractions[i] = (double) percolation.numberOfOpenSites() / (n * n);

    }
    
    this.mean =  StdStats.mean(fractions);
    this.stddev = StdStats.stddev(fractions);
  }

  /**
   * sample mean of percolation threshold
   * 
   * @return
   */
  public double mean() {
    return this.mean;
  }

  /**
   * sample standard deviation of percolation threshold
   * 
   * @return
   */
  public double stddev() {
    return this.stddev;
  }

  /**
   * low endpoint of 95% confidence interval
   * 
   * @return
   */
  public double confidenceLo() {
    return this.mean - 1.96 * this.stddev / Math.sqrt(this.fractions.length);
  }

  /**
   * high endpoint of 95% confidence interval
   * 
   * @return
   */
  public double confidenceHi() {
    return this.mean + 1.96 * this.stddev / Math.sqrt(this.fractions.length);
  }

  /**
   * Main
   * 
   * @param args
   */
  public static void main(String[] args) {
    int n = Integer.parseInt(args[0]);
    int trials = Integer.parseInt(args[1]);

    PercolationStats stats = new PercolationStats(n, trials);

    StdOut.printf("mean: %f\n", stats.mean());
    StdOut.printf("stddev: %f\n", stats.stddev());
    StdOut.printf("95per confidence interval: [%f, %f]", stats.confidenceLo(), stats.confidenceHi());

  }

}
