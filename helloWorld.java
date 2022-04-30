import java.util.*;

public class helloWorld {


    /* This is my first java program.
     * This will print 'Hello World' as the output
     */
 
    public static void main(String []args) {
       System.out.println("Hello World"); // prints Hello World
       HashMap<Integer,String> test = new HashMap<>();

       test.put(0,"hello");

       System.out.println(test.get(0));
    }

 }