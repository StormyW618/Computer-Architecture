import java.util.*;

public class helloWorld {


    /* This is my first java program.
     * This will print 'Hello World' as the output
     */
 
    public static void main(String []args) {
       System.out.println("Hello World"); // prints Hello World

       ArrayList<String> test = new ArrayList<>();

       ArrayList<String> test1 = new ArrayList<>();

       test = newlist(test1);

    }

    public static ArrayList<String> newlist(ArrayList<String> data)
    {
       data.add("one");
       data.add("2");
       data.add("3");
       data.add("4");
       data.add("5");

       return data;
    }
 }