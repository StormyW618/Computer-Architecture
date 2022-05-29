/**
 * Programmers: Nathan Jaggers, Storm Randolph
 * 
 * CPE 315 - Computer Architecture
 * Dr.Seng
 * Spring 2022
 * 
 * Lab 6 - Cache Simulator
 * 
 * Description:
 */

package Lab6;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class lab6 {

    public static void main(String[] args) {

        // declare caches
        cache test1 = new cache();

        try {
            // read entrys in memory file
            // Scanner file_input = new Scanner(new File("./Lab6/mem_stream.1"));
            Scanner file_input = new Scanner(new File(args[0]));

            while (file_input.hasNextLine()) {
                // read line in file, parse it and then update caches
                String file = file_input.nextLine();// obtaining file input

            }

            // print out results for caches
            test1.showSummary();

        } catch (IllegalStateException | FileNotFoundException e) {
            // if trouble opening file
            System.out.println("Scanner was closed");
        }
    }
}
