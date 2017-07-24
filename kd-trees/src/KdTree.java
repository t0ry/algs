import java.awt.Point;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import edu.princeton.cs.algs4.BST;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

  private Node root; // root of BST

  private class Node {
    private Point2D key; // sorted t;by key
    private Comparator<Point2D> order;
    private RectHV rect;

    private Node left, right; // left and right subtrees
    private int size; // number of nodes in subtree

    public Node(Point2D key, int size, Comparator<Point2D> order, RectHV rect) {
      this.key = key;
      this.order = order;
      this.size = size;
      this.rect = rect;
    }

    public Point2D getOrtoBase(Point2D p) {
      if (this.order == Point2D.X_ORDER) {
        return new Point2D(this.key.x(), p.y());
      }
      return new Point2D(p.x(), this.key.y());
    }
    
    public RectHV getLeftRect() {
      if (this.order == Point2D.X_ORDER) {
        return new RectHV(rect.xmin(), rect.ymin(), key.x(), rect.ymax());
      }
      return new RectHV(rect.xmin(), rect.ymin(),rect.xmax(), key.y());
    }
    
    public RectHV getRightRect() {
      if (this.order == Point2D.X_ORDER) {
        return new RectHV(key.x(), rect.ymin(), rect.xmax(), rect.ymax());
      }
      return new RectHV(rect.xmin(), key.y(),  rect.xmax(), rect.ymax());
    }
  }

  /**
   * construct an empty set of points
   */
  public KdTree() {

  }

  private void valideteArg(Object arg) {
    if (arg == null) {
      throw new IllegalArgumentException("Argument cannot be null");
    }
  }

  // return number of key-value pairs in BST rooted at x
  private int size(Node node) {
    if (node == null)
      return 0;
    else
      return node.size;
  }

  private Point2D get(Node parent, Point2D key) {
    if (key == null)
      throw new IllegalArgumentException("called get() with a null key");
    if (parent == null)
      return null;
    if (key.compareTo(parent.key) == 0) {
      return parent.key;
    }

    int cmp = parent.order.compare(parent.key, key);
    if (cmp < 0)
      return get(parent.left, key);
    else if (cmp > 0)
      return get(parent.right, key);
    else
      return parent.key;
  }

  private Node put(Node parent, Point2D key, Comparator<Point2D> order, RectHV rect) {
    if (parent == null)
      return new Node(key, 1, order, rect);

    if (parent.key.compareTo(key) == 0) {
      parent.key = key;
      return parent;
    }

    int cmp = parent.order.compare(key, parent.key);
    Comparator<Point2D> nextOrder = (parent.order == Point2D.X_ORDER) ? Point2D.Y_ORDER : Point2D.X_ORDER;
    if (cmp < 0)
      parent.left = put(parent.left, key, nextOrder, parent.getLeftRect());
    else
      parent.right = put(parent.right, key, nextOrder, parent.getRightRect());

    parent.size = 1 + size(parent.left) + size(parent.right);
    return parent;
  }

  private Node nearest(Node x, Point2D p, Node nearest) {
    // check if x is nearer than nearest
    if (nearest == null || p.distanceToOrder().compare(nearest.key, x.key) > 0) {
      nearest = x;
    }

    Point2D ortoBase = x.getOrtoBase(p);
    if (x.order.compare(x.key, p) < 0) {
      nearest = nearest(x.left, p, nearest);

      if (x.rect.distanceSquaredTo(p)< p.distanceSquaredTo(nearest.key)) {
        nearest = nearest(x.right, p, nearest);
      }

    } else {
      nearest = nearest(x.right, p, nearest);

      if (x.rect.distanceSquaredTo(p)< p.distanceSquaredTo(nearest.key)) {
        nearest = nearest(x.left, p, nearest);
      }
    }

    return nearest;
  }

  private Iterable<Point2D> range(Node parent, RectHV rect, List<Point2D> pointsInRect) {
    // noting to check in branch
    if (parent == null) {
      return pointsInRect;
    }

    // point in rect
    if (rect.contains(parent.key)) {
      pointsInRect.add(parent.key);
    }

    if (parent.order == Point2D.X_ORDER) {
      if (parent.key.x() > rect.xmin()) {
        range(parent.left, rect, pointsInRect);
      }
      if (parent.key.x() < rect.xmax()) {
        range(parent.right, rect, pointsInRect);
      }
    } else {
      if (parent.key.y() > rect.ymin()) {
        range(parent.left, rect, pointsInRect);
      }
      if (parent.key.y() < rect.ymax()) {
        range(parent.right, rect, pointsInRect);
      }
    }

    return pointsInRect;
  }
  
  private void draw (Node x, Node parent) {
    if(x == null) {
      return;
    } 
    
    StdDraw.setPenColor(StdDraw.BLACK);
    StdDraw.setPenRadius(0.05);
    x.key.draw();
    
  }

  /**
   * is the set empty&vae
   * 
   * @return
   */
  public boolean isEmpty() {
    return size() == 0;
  }

  /**
   * number of points in the set
   * 
   * @return
   */
  public int size() {
    return this.size(root);
  }

  /**
   * add the point to the set (if it is not already in the set)
   * 
   * @param p
   */
  public void insert(Point2D p) {
    this.valideteArg(p);

    put(root, p, Point2D.X_ORDER, new RectHV(0.0, 0.0, 1.0, 1.0));
  }

  /**
   * does the set contain point p?
   * 
   * @param p
   * @return
   */
  public boolean contains(Point2D p) {
    return get(root, p) != null;
  }

  /**
   * draw all points to standard draw
   */
  public void draw() {
    draw (root, null);
  }

  /**
   * all points that are inside the rectangle
   * 
   * @param rect
   * @return
   */
  public Iterable<Point2D> range(RectHV rect) {
    this.valideteArg(rect);

    return this.range(root, rect, new LinkedList<Point2D>());
  }

  /**
   * a nearest neighbor in the set to point p; null if the set is empty
   * 
   * @param p
   * @return
   */
  public Point2D nearest(Point2D p) {
    this.valideteArg(p);

    return this.nearest(root, p, null).key;

  }

  public static void main(String[] args) {

  }
}
