import java.util.*;
import java.util.ArrayList;

public class helloWorld {


    /* This is my first java program.
     * This will print 'Hello World' as the output
     */
 
    public static void main(String []args) {
       System.out.println("Hello World"); // prints Hello World

      ArrayList<Integer> list = new ArrayList<>();

      list.add(1);
      list.add(0);
      list.add(0);
      list.add(1);
      // list.add(0);
      // list.add(1);
      // list.add(0);
      // list.add(0);

      String temp = list.toString();

      System.out.println(temp);
      temp = temp.replace('[',' ');
      temp = temp.replace(']',' ');
      temp = temp.replace(',',' ');
      temp = temp.replaceAll(" ","");
      //temp = temp.trim();
      System.out.println(temp);
      int num = Integer.parseInt(temp,2);

      System.out.println(num);



    }

 }