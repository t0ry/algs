import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
  private int size;
  private Item[] queue;

  /**
   * construct an empty randomized queue
   */
  public RandomizedQueue() {
    this.queue = (Item[]) new Object[1];

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

  private void resize(final int length) {
    final Item[] newQueue = (Item[]) new Object[length];

    for (int i = 0; i < newQueue.length && i < this.queue.length; i++) {
      newQueue[i] = this.queue[i];
      this.queue[i] = null;
    }

    this.queue = newQueue;
  }

  /**
   * is the queue empty?
   * 
   * @return
   */
  public boolean isEmpty() {
    return this.size == 0;

  }

  /**
   * return the number of items on the queue
   * 
   * @return
   */
  public int size() {
    return this.size;
  }

  /**
   * add the item
   * 
   * @param item
   */
  public void enqueue(Item item) {
    this.validateForNull(item);

    if (this.queue.length == this.size) {
      this.resize(this.queue.length * 2);
    }
    this.queue[this.size++] = item;

  }

  /**
   * remove and return a random item
   * 
   * @return
   */
  public Item dequeue() {
    this.validateSize();

    final int dequeueIndex = StdRandom.uniform(this.size);

    final Item item = this.queue[dequeueIndex];
    if (dequeueIndex < this.size - 1) {
      this.queue[dequeueIndex] = this.queue[this.size - 1];
    }
    this.queue[--this.size] = null;
    
    if (this.queue.length / 4 > this.size) {
      this.resize(this.queue.length / 2);
    }
    return item;
  }

  /**
   * return (but do not remove) a random item
   * 
   * @return
   */
  public Item sample() {
    this.validateSize();

    final int sampleIndex = StdRandom.uniform(this.size);
    return this.queue[sampleIndex];
  }

  /**
   * return an independent iterator over items in random order
   */
  public Iterator<Item> iterator() {
    return new QueueIterator();
  }

  private class QueueIterator implements Iterator<Item> {

    private int preCurrentIndex = -1;
    private Item[] randomizedQueue;

    public QueueIterator() {
      this.randomizedQueue = (Item[]) new Object[size];
      for (int i = 0; i < size; i++) {
        this.randomizedQueue[i] = queue[i];
      }

      StdRandom.shuffle(this.randomizedQueue);
    }

    @Override
    public boolean hasNext() {
      return this.preCurrentIndex + 1 < this.randomizedQueue.length;
    }

    @Override
    public Item next() {
      if (!this.hasNext()) {
        throw new NoSuchElementException("No more elements to iterate through.");
      }

      Item currentItem = this.randomizedQueue[++this.preCurrentIndex];

      return currentItem;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException(
          "This iterator does not provide remove functionality. Use Queue methods instead.");
    }
  }

}
