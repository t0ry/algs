import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
  public static void main(String[] args) {
    final int k = Integer.parseInt(args[0]);

    final RandomizedQueue<String> queue = new RandomizedQueue<>();
    while (!StdIn.isEmpty()) {
      String item = StdIn.readString();
      if (k == 0) {
        continue;
      }

      if (queue.size() == k) {
        int skip = StdRandom.uniform(2);
        if (skip == 0) {
          queue.dequeue();
          queue.enqueue(item);
        }
      } else {
        queue.enqueue(item);
      }
    }

    for (int i = 0; i < k; i++) {
      String outItem = queue.dequeue();
      StdOut.printf("%s\n", outItem);

    }

  }
}
