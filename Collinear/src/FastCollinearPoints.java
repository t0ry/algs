import java.util.Arrays;
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

    for (int i = 0; i < this.points.length - 3; i++) {
      Point[] sortesPoints = Arrays.copyOfRange(points, i + 1, points.length);
      Arrays.sort(sortesPoints, this.points[i].slopeOrder());
      for (int k = 0; k < sortesPoints.length - 2;) {
        int j = k + 1;
        while (j < sortesPoints.length && points[i].slopeOrder().compare(sortesPoints[k], sortesPoints[j]) == 0) {
          j++;
        }

        if (j - k < 3) {
          k = j;
          continue;
        }

        Point min = this.points[i];
        Point max = this.points[i];
        for (int m = k; m < j; m++) {
          if (min.compareTo(sortesPoints[m]) > 0) {
            min = sortesPoints[m];
          }

          if (max.compareTo(sortesPoints[m]) < 0) {
            max = sortesPoints[m];
          }
        }

        LineSegment segment = new LineSegment(min, max);
        this.lineSegments.add(segment);
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
