import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.naming.OperationNotSupportedException;

public class Deque<Item> implements Iterable<Item> {
	private Node<Item> head;
	private Node<Item> tail;
	int size;

	/**
	 * construct an empty deque
	 */
	public Deque() {

	}

	private void validateForNull(Item item) {
		if (item == null)
			throw new IllegalArgumentException("Item can't be null.");
	}
	
	private void validateSize(){
	  if (this.size == 0) {
      throw new NoSuchElementException("Queue is empty.");
    } 
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
			final Node<Item> oldHead = this.head;
			this.head = new Node<>(item);
			this.head.next = oldHead;
			oldHead.prev = this.head;
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
			final Node<Item> oldTail = this.tail;
			this.tail = new Node<>(item);
			this.tail.prev = oldTail;
			oldTail.next = this.tail;
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
		
		final Node<Item> first = this.head;
		this.head = first.next;
		this.head.next.prev = this.head;
		this.head.prev = null;
		this.size--;

		return first.item;
	}

	/**
	 * remove and return the item from the end
	 * 
	 * @return
	 */
	public Item removeLast() {
		this.validateSize();
		
		final Node<Item> last = this.tail;
		this.tail = last.prev;
		this.tail.prev.next = this.tail;
		this.tail.next = null;
		this.size--;

		return last.item;
	}

	/**
	 * return an iterator over items in order from front to end
	 */
	public Iterator<Item> iterator() {
		return new QueueIterator();
	}

	class Node<T> {
		private T item;
		Node<T> prev;
		Node<T> next;

		public Node(T item) {
			this.item = item;
		}

	}

	class QueueIterator implements Iterator<Item> {
		private Node<Item> preCurrentNode = new Node<>(null); // create dummy
																// node

		public QueueIterator() {
			this.preCurrentNode.next = head; // point next of dummy node to head
		}

		@Override
		public boolean hasNext() {
			return this.preCurrentNode.next != null;
		}

		@Override
		public Item next() {
			if (!this.hasNext()) {
				throw new UnsupportedOperationException("No more elements to iterate through.");
			}

			this.preCurrentNode = this.preCurrentNode.next;
			Item currentItem = this.preCurrentNode.item;

			return currentItem;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException(
					"This iterator does not provide remove functionality. Use Queue methods instead.");
		}
	}

	/**
	 * unit testing (optional)
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
