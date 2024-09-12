/**
 * Programmers: Nathan Jaggers, Storm Randolph
 * 
 * CPE 315 - Computer Architecture
 * Dr.Seng
 * Spring 2022
 * 
 * Lab 4 - Mips Processor Simulator
 * 
 * Description: In this program, we simulate a mips processor
 *              that uses pipelines. Using previous labs, we have 
 *              constructed a way to execute instructions. Now following up
 *              with that, this lab will focus on simulating a processor 
 *              with a pipleined architechture executing those instructions
 *              and show what it looks like for an instrucion to step through
 *              the pipeline.
 */

package Lab4;
 
import Lab2.mipsAssembler;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class lab4 {

   public static void main(String[] args) {
      // define mips objects
      mipsAssembler testAsm = new mipsAssembler(args[0]);
      mipsSimulator testSim = new mipsSimulator(testAsm);
      if (args.length == 1) {
         try {
            // scanner to access user input from console
            Scanner user_input = new Scanner(System.in);
            while (true) {
               System.out.print("mips>");// printing prompt
               String user = user_input.nextLine();// obtaining user input
               // checking if quit command entered
               if (user.contains("q")) {
                  // close scanner
                  user_input.close();
                  // pass to command function takes string input
                  testSim.command(user);
                  // break from while loop
                  break;
               } else {
                  // pass to command function
                  if (user.length() > 0) {
                     testSim.command(user);
                  }
               }
            }
         } catch (IllegalStateException e) {
            System.out.println("Scanner was closed");
         }
      } else {
         try {
            // no user input, read from script file
            Scanner file_input = new Scanner(new File(args[1]));
            // Scanner file_input = new Scanner(new File ("Lab3/script2"));
            while (true) {
               String file = file_input.nextLine();// obtaining file input
               System.out.println("mips> " + file + "\n");// printing prompt
               // checking if quit command entered
               if (file.contains("q")) {
                  // close scanner
                  file_input.close();
                  // pass to command function takes string input
                  testSim.command(file);
                  // break from while loop
                  break;
               } else {
                  // System.out.println("program continues");
                  // pass to command function
                  testSim.command(file);
               }
            }
         } catch (IllegalStateException | FileNotFoundException e) {
            System.out.println("Scanner was closed");
         }
      }
   }

}
