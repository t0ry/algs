import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

public class Permutation {
 public static void main(String[] args)  {
   final int k = Integer.parseInt(args[0]);
   final String[] strings = StdIn.readAllStrings();
   
   for(int i = 0; i < strings.length; i++){
     StdOut.println(strings[i]);
   }
  
   
   final RandomizedQueue<String> queue = new RandomizedQueue<>();
   for(int i = 0; i < k; i++){
     queue.enqueue(strings[i]);
     strings[i] = null;
   }
   
   StdOut.println("---------RESULT--------");
   for(int i = 0; i < k; i++){
     String item = queue.dequeue();
     if(i < strings.length - k){
       queue.enqueue(strings[k + i]);
     }
     StdOut.printf("%s\n", item);
   }
    
  }
}
