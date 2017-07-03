import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

/**
 * Finds all line segments containing 4 points
 *
 */
public class BruteCollinearPoints {
  private final Point[] points;
  private List<LineSegment> lineSegments;

  public BruteCollinearPoints(Point[] points) {
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
	      for (int j = i + 1; j < this.points.length - 2; j++) {
	        for (int k = j + 1; k < this.points.length - 1; k++) {
	          if (points[i].slopeOrder().compare(this.points[j], this.points[k]) == 0) {
	            boolean fourth = false;
	            int el = k + 1;

	            Point min = null;
	            Point max = null;
	            while (el < this.points.length) {
	              if (points[i].slopeOrder().compare(this.points[j], this.points[el]) == 0) {

	                min = this.points[i];
	                max = this.points[i];
	                if (min.compareTo(this.points[j]) > 0) {
	                  min = this.points[j];
	                }
	                if (max.compareTo(this.points[j]) < 0) {
	                  max = this.points[j];
	                }
	                if (min.compareTo(this.points[k]) > 0) {
	                  min = this.points[k];
	                }
	                if (max.compareTo(this.points[k]) < 0) {
	                  max = this.points[k];
	                }

	                if (min.compareTo(this.points[el]) > 0) {
	                  min = this.points[el];
	                }
	                if (max.compareTo(this.points[el]) < 0) {
	                  max = this.points[el];
	                }
	                fourth = true;
	              }
	              el++;
	            }
	            if (fourth) {
	              LineSegment segment = new LineSegment(min, max);
	              this.lineSegments.add(segment);

	            }
	          }
	        }
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
    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
      StdOut.println(segment);
      segment.draw();
    }
    StdDraw.show();
  }

}
