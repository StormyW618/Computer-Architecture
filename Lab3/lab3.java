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

import java.io.File;
import java.util.NoSuchElementException;
import java.io.FileNotFoundException;
import java.util.Scanner;
import Lab2.mipsAssembler;
import Lab3.mipsEmulator;

public class lab3 {

   public static void main(String[] args) {
      //define mips objects
      mipsAssembler test1Asm = new mipsAssembler(args[0]);
      //mipsAssembler test1Asm = new mipsAssembler("Lab3/test2.asm"); 
      mipsEmulator test1Em = new mipsEmulator(test1Asm);
      if (args.length == 1) {
      //if (args.length == 0) {
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
                  test1Em.command(user);
                  // break from while loop
                  break;
               } else {
                  // pass to command function
                  if(user.length()>0)
                  {
                     test1Em.command(user);
                  }
               }
            }
         } catch (IllegalStateException e) {
            System.out.println("Scanner was closed");
         }
      } else {
         try {
            // no user input, read from script file
            Scanner file_input = new Scanner(new File (args[1]));
            //Scanner file_input = new Scanner(new File ("Lab3/script2"));
            while (true) {
               String file = file_input.nextLine();// obtaining file input
               System.out.println("\nmips> " + file);// printing prompt
               // checking if quit command entered
               if (file.contains("q")) {
                  // close scanner
                  file_input.close();
                  // pass to command function takes string input
                  test1Em.command(file);
                  // break from while loop
                  break;
               } else {
                  //System.out.println("program continues");
                  // pass to command function
                  test1Em.command(file);
               }
            }
         } catch (IllegalStateException | FileNotFoundException e) {
            System.out.println("Scanner was closed");
         }
      }
   }
}