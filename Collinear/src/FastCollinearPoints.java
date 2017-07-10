import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
  private final Point[] points;
  private List<LineSegment> lineSegments;

  public FastCollinearPoints(Point[] points) {
    this.validateForNull(points);
    this.validateForNull(points[0]);
    for (int i = 0; i < points.length; i++) {
      for (int j = i + 1; j < points.length; j++) {
        this.validateForNull(points[j]);
        this.validateForDuplicates(points[i], points[j]);
      }
    }
    this.points = Arrays.copyOf(points, points.length);
    this.findSegments();
  }

  private void validateForDuplicates(Point point1, Point point2) {
    if (point1.compareTo(point2) == 0) {
      throw new IllegalArgumentException("Duplicate points detected");
    }
  }

  private void validateForNull(Object object) {
    if (object == null) {
      throw new IllegalArgumentException("Argument cannot be null");
    }
  }

  private void findSegments() {
    this.lineSegments = new LinkedList<>();
    List<Point> mins = new LinkedList<>();

    Arrays.sort(this.points);
    for (int i = 0; i < this.points.length - 3; i++) { // n-3
      Point[] sortesPoints = Arrays.copyOfRange(points, i + 1, points.length);

      Arrays.sort(sortesPoints, this.points[i].slopeOrder());

      Point[] usedPoints = Arrays.copyOfRange(points, 0, i);

      Arrays.sort(usedPoints, this.points[i].slopeOrder());

      for (int k = 0; k < sortesPoints.length - 2;) { // 1/2 (n- 2)
        boolean exists = false;

        int j = k + 1;

        while (j < sortesPoints.length && points[i].slopeOrder().compare(sortesPoints[k], sortesPoints[j]) == 0) {
          j++;
        }

        if (j - k < 3) {
          k = j;
          continue;
        }

        int lo = 0;
        int hi = usedPoints.length - 1;
        while (lo <= hi) {
          // Key is in a[lo..hi] or not present.
          int mid = lo + (hi - lo) / 2;
          int compared = points[i].slopeOrder().compare(sortesPoints[k], usedPoints[mid]);
          if (compared < 0)
            hi = mid - 1;
          else if (compared > 0)
            lo = mid + 1;
          else {
            exists = true;
            break;
          }

        }

        if (exists) {
          k = j;
          continue;
        }
        LineSegment segment = new LineSegment(this.points[i], sortesPoints[j - 1]);
        this.lineSegments.add(segment);
        mins.add(this.points[i]);
        k = j;
      }

    }
  }

  /**
   * the number of line segments
   * 
   * @return
   */
  public int numberOfSegments() {
    return this.lineSegments.size();
  }

  /**
   * the line segments
   * 
   * @return
   */
  public LineSegment[] segments() {
    return this.lineSegments.toArray(new LineSegment[this.lineSegments.size()]);
  }

  public static void main(String[] args) {

    // read the n points from a file
    In in = new In(args[0]);
    int n = in.readInt();
    Point[] points = new Point[n];
    for (int i = 0; i < n; i++) {
      int x = in.readInt();
      int y = in.readInt();
      points[i] = new Point(x, y);
    }

    // draw the points
    StdDraw.enableDoubleBuffering();
    StdDraw.setXscale(0, 32768);
    StdDraw.setYscale(0, 32768);
    for (Point p : points) {
      p.draw();
    }
    StdDraw.show();

    // print and draw the line segments
    FastCollinearPoints collinear = new FastCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
      StdOut.println(segment);
      segment.draw();
    }
    StdDraw.show();
  }

}
