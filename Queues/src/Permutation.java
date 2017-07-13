import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
  public static void main(String[] args) {
    final int k = Integer.parseInt(args[0]);

    final RandomizedQueue<String> queue = new RandomizedQueue<>();
    for (int i = 0; !StdIn.isEmpty(); i++) {
      String item = StdIn.readString();

      // nothing to process for k =0
      if (k == 0) {
        continue;
      }
      // enqueue while size of queue < k
      if (queue.size() < k) {
        queue.enqueue(item);
        continue;
      }

      // for more than k items emulate Khnuth shuffling

      // if random id in queue of size k -> dequeue and enqueue incoming
      // ignore otherwise
      if (StdRandom.uniform(i + 1) < queue.size()) {
        queue.dequeue();
        queue.enqueue(item);
      }

    }

    for (int i = 0; i < k; i++) {
      String outItem = queue.dequeue();
      StdOut.printf("%s\n", outItem);
    }

  }
}
