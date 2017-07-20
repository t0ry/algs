import java.awt.Point;
import java.util.TreeSet;

import edu.princeton.cs.algs4.BST;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class KdTree extends PointSET {
  private final BST<Double, Point2D> bst;

  
  /**
   * construct an empty set of points
   */
  public KdTree() {
    this.bst = new BST<>();
  }

  private void valideteArg(Object arg) {
    if(arg == null) {
      throw new IllegalArgumentException ("Argument cannot be null");
    }
  }
  /**
   * is the set empty&
   * 
   * @return
   */
  public boolean isEmpty() {
    return this.bst.isEmpty();
  }

  /**
   * number of points in the set
   * 
   * @return
   */
  public int size() {
    return this.size();
  }

  /**
   * add the point to the set (if it is not already in the set)
   * 
   * @param p
   */
  public void insert(Point2D p) {
    this.valideteArg(p);
    
    this.bst.put(p.x(), p);
    
  }

  /**
   * does the set contain point p?
   * 
   * @param p
   * @return
   */
  public boolean contains(Point2D p) {
    this.valideteArg(p);
    
    return this.contains(p);
  }

  /**
   * draw all points to standard draw
   */
  public void draw() {

  }

  /**
   * all points that are inside the rectangle
   * 
   * @param rect
   * @return
   */
  public Iterable<Point2D> range(RectHV rect) {
    this.valideteArg(rect);
  }

  /**
   * a nearest neighbor in the set to point p; null if the set is empty
   * 
   * @param p
   * @return
   */
  public Point2D nearest(Point2D p) {
    this.valideteArg(p);
   
  }
  
  

  public static void main(String[] args) {

  }
}
