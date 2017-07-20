import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {
  protected final TreeSet<Point2D> set;

  /**
   * construct an empty set of points
   */
  public PointSET() {
    this.set = new TreeSet<>();
  }

  private void valideteArg(Object arg) {
    if (arg == null) {
      throw new IllegalArgumentException("Argument cannot be null");
    }
  }

  /**
   * is the set empty&
   * 
   * @return
   */
  public boolean isEmpty() {
    return this.set.isEmpty();
  }

  /**
   * number of points in the set
   * 
   * @return
   */
  public int size() {
    return this.set.size();
  }

  /**
   * add the point to the set (if it is not already in the set)
   * 
   * @param p
   */
  public void insert(Point2D p) {
    this.valideteArg(p);

    this.set.add(p);
  }

  /**
   * does the set contain point p?
   * 
   * @param p
   * @return
   */
  public boolean contains(Point2D p) {
    this.valideteArg(p);

    return this.set.contains(p);
  }

  /**
   * draw all points to standard draw
   */
  public void draw() {
    for (Point2D point : set) {
      point.draw();
    }
  }

  /**
   * all points that are inside the rectangle
   * 
   * @param rect
   * @return
   */
  public Iterable<Point2D> range(RectHV rect) {
    this.valideteArg(rect);
    List<Point2D> result = new LinkedList<>();

    for (Point2D point : set) {
      if (rect.contains(point)) {
        result.add(point);
      }
    }
    return result;
  }

  /**
   * a nearest neighbor in the set to point p; null if the set is empty
   * 
   * @param p
   * @return
   */
  public Point2D nearest(Point2D p) {
    this.valideteArg(p);

    Point2D nearestPoint = null;
    Iterator<Point2D> iterator = this.set.iterator();
    while (iterator.hasNext()) {
      final Point2D current = iterator.next();
      if (p.distanceToOrder().compare(nearestPoint, current) > 0) {
        nearestPoint = current;
      }
    }

    return nearestPoint;
  }

  public static void main(String[] args) {

  }
}
