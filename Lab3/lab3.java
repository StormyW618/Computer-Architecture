/**
 * Programmers: Nathan Jaggers, Storm Randolph
 * 
 * CPE 315 - Computer Architecture
 * Dr.Seng
 * Spring 2022
 * 
 * Lab 3 - Mips Emulator
 * 
 * Description: In this program, we take a mips assembly file
 *              and either run through the program with user input
 *              or through a mips file. When run by the user, there
 *              are commands for the user to check on the state of
 *              the program, run single or multiple lines of asm code
 *              and reset the state of the program. When run by a script,
 *              this process is automated.
 */

package Lab3;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class lab3 {

   public static void main(String[] args) {
      System.out.println("Hello World"); // prints Hello World
      Scanner user_input = new Scanner(System.in);
      // checking if q will be first command entered(?)
      if (args.length == 1) {
         try {
            while (true) {
               System.out.println("mips>");
               String user = user_input.nextLine();
               if (user.contains("q")) {
                  // close scanner
                  user_input.close();
                  // pass to command function
               } else {
                  System.out.println("program continues");
                  // pass to command function
               }
            }
         } catch (IllegalStateException e) {
            System.out.println("Scanner was closed");
         }
      } else {
         System.out.println("ASM & Script");
      }
   }

   public static void command(String userInput) {
      // This function will take a string, trim
      // it down and test it to see if it is a
      // valid command. If it is, it will go to
      // the appropriate section in the case
      // statement and execute the code for said
      // command

      userInput = userInput.trim();

      switch (userInput.charAt(0)) {

         case 'h':
            // show help

            break;

         case 'd':
            // dump register state

            break;

         case 's':
            // step through the program
            // single or multiple based on input

            break;

         case 'r':
            // run the program in its entirety

            break;

         case 'm':
            // display data memory from location num1 to num2

            break;

         case 'c':
            // clear all registers, memory, and the program counter to 0

            break;

         case 'q':
            // exit the program

            break;

         case default:
            // command not found. Notify user
            System.out.print("Command not found. Please try again\n");

            break;

      }

   }
}