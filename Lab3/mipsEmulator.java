/**
 * Programmers: Nathan Jaggers, Storm Randolph
 * 
 * CPE 315 - Computer Architecture
 * Dr.Seng
 * Spring 2022
 * 
 */

package Lab3;

import Lab2.Instruction;
import Lab2.mipsAssembler;
import java.util.ArrayList;

public class mipsEmulator {

   /****************************************************
    * Class is to organize data and simplify process of
    * emulating assembly program.
    ****************************************************/

   // ---MEMBERS---
   int pc; // Program counter
   int[] registers; // register file
   int[] dataMemory; // memory to store and read data
   ArrayList<Instruction> program;

   // ---METHODS---
   // constructors
   mipsEmulator() {
      // initalize all members to default, zero, null
      pc = 0;
      registers = new int[32];
      dataMemory = new int[8192];
      program = new ArrayList<>();
   }

   mipsEmulator(ArrayList<Instruction> assembledProgram) {
      // initalize all members to default, zero, null
      pc = 0;
      registers = new int[32];
      dataMemory = new int[8192];
      // initialize program
      program = assembledProgram;
   }

   mipsEmulator(mipsAssembler assembled) {
      // initalize all members to default, zero, null
      pc = 0;
      registers = new int[32];
      dataMemory = new int[8192];
      // initialize program
      program = assembled.program;
   }

   // user commands
   public void command(String userInput) {
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
            help();
            break;

         case 'd':
            // dump register state
            dump();
            break;

         case 's':
            // step through the program
            // single or multiple based on input
            if (userInput.length() > 1)
            {
               //get number of steps after s command
               step(Integer.parseInt(userInput.substring(1)));
            }
            else
            {
               //run step command for 1 iteration
               step(1);
            }
            break;

         case 'r':
            // run the program in its entirety
            run();
            break;

         case 'm':
            // display data memory from location num1 to num2
            memory();
            break;

         case 'c':
            // clear all registers, memory, and the program counter to 0
            clear();
            break;

         case 'q':
            // exit the program
            quit();
            break;

         default:
            // command not found. Notify user
            System.out.print("Command not found. Please try again\n");

            break;

      }

   }

   public void help()
   {

   }

   public void dump()
   {

   }

   public void step(int numOfSteps)
   {
      // uses program array list and executes
      // a specified number of instructions. Number
      // of instructions process id determined by 
      // value passed into function 

      // switch()
      // {

      // }
   }

   public void run()
   {

   }

   public void memory()
   {

   }

   public void clear()
   {

   }

   public void quit()
   {
      System.out.println("Quitting Program...");
   }
}
