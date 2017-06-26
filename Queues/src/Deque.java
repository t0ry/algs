import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
  private Node<Item> head;
  private Node<Item> tail;
  private int size;

  /**
   * construct an empty deque
   */
  public Deque() {

  }

  private void validateForNull(Item item) {
    if (item == null)
      throw new IllegalArgumentException("Item can't be null.");
  }

  private void validateSize() {
    if (this.size == 0) {
      throw new NoSuchElementException("Queue is empty.");
    }
  }

  private void emptyQueue() {
    if (this.size > 1)
      throw new IllegalStateException("Cannot empty queue with more that 1 element");
    this.head = null;
    this.tail = null;
  }

  /**
   * is the deque empty?
   * 
   * @return
   */
  public boolean isEmpty() {
    return this.size == 0;
  }

  /**
   * return the number of items on the deque
   * 
   * @return
   */
  public int size() {
    return this.size;
  }

  /**
   * add the item to the front
   * 
   * @param item
   */
  public void addFirst(Item item) {
    this.validateForNull(item);

    if (this.size == 0) {
      this.head = new Node<>(item);
      this.tail = head;

    } else {
      Node<Item> oldHead = this.head;
      this.head = new Node<>(item);
      this.head.next = oldHead;
      this.head.next.prev = this.head;

    }
    this.size++;
  }

  /**
   * add the item to the end
   * 
   * @param item
   */
  public void addLast(Item item) {
    this.validateForNull(item);

    if (this.size == 0) {
      this.head = new Node<>(item);
      this.tail = head;

    } else {
      Node<Item> oldTail = this.tail;
      this.tail = new Node<>(item);
      this.tail.prev = oldTail;
      this.tail.prev.next = this.tail;

    }
    this.size++;
  }

  /**
   * remove and return the item from the front
   * 
   * @return
   */
  public Item removeFirst() {
    this.validateSize();

    final Item first = this.head.item;
    if (this.size == 1) { // empty queue now
      this.emptyQueue();
    } else {
      this.head = this.head.next; // points to second element
      if (this.head.next != null) {
        this.head.next.prev = this.head;
      }
      this.head.prev = null;
    }
    this.size--;

    return first;
  }

  /**
   * remove and return the item from the end
   * 
   * @return
   */
  public Item removeLast() {
    this.validateSize();

    final Item last = this.tail.item;
    if (this.size == 1) { // empty queue now
      this.emptyQueue();
    } else {
      this.tail = this.tail.prev;
      if (this.tail.prev != null) {
        this.tail.prev.next = this.tail;
      }
      this.tail.next = null;

    }
    this.size--;

    return last;
  }

  /**
   * return an iterator over items in order from front to end
   */
  public Iterator<Item> iterator() {
    return new QueueIterator();
  }

  private class Node<T> {
    private Node<T> prev;
    private Node<T> next;
    private T item;

    public Node(T item) {
      this.item = item;
    }

  }

  private class QueueIterator implements Iterator<Item> {
    private Node<Item> currentNode;

    public QueueIterator() {
      this.currentNode = head;
    }

    @Override
    public boolean hasNext() {
      return this.currentNode != null;
    }

    @Override
    public Item next() {
      if (!this.hasNext()) {
        throw new NoSuchElementException("No more elements to iterate through.");
      }
      Item item = this.currentNode.item;
      this.currentNode = this.currentNode.next;

      return item;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException(
          "This iterator does not provide remove functionality. Use Queue methods instead.");
    }
  }

}
