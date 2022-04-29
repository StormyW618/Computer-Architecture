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
import java.util.Arrays;

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
            
            //split string into list to properly parse out args num1 and num2
            String[] arr = userInput.split(" ");
            int num1 = Integer.parseInt(arr[1]);
            int num2 = Integer.parseInt(arr[2]);
            memory(num1, num2);
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
      System.out.println("h = show help");
      System.out.println("d = dump register state");
      System.out.println("s = single step through the program (i.e. execute 1 instruction and stop)");
      System.out.println("s num = step through num instructions of the program");
      System.out.println("r = run until the program ends");
      System.out.println("m num1 num2 = display data memory from location num1 to num2");
      System.out.println("c = clear all registers, memory, and the program counter to 0");
      System.out.println("q = exit the program");
   }

   public void dump()
   {

      System.out.println("pc = " + pc);
      // for(int i = 0; i < 32; i++){
         
      // }
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
      for (int i = 0; i < numOfSteps; i++)
      {
         switch(program.get(pc).instruct)
         {
            case "add":
            //add rs and rt, store in rd
            
            break;
            
            case "addi":
            //add rs and SignExtendedimmediate, store in rt
            
            break;
            
            case "addiu":
            //add unsigned rs and SignExtendedimmediate, store in rt
            
            break;
            
            case "addu":
            //add unsigned rs and rt, store in rd
            
            break;
            
            case "and":
            //and rs and rt, store in rd
            
            break;
            
            case "andi":
            //and rs and ZeroExtendedImm, store in rt
            
            break;
            
            case "beq":
            //if rs == rt, pc = pc + 4 + BranchAddr
            
            break;
            
            case "bne":
            //if rs == rt, pc = pc + 4 + BranchAddr
            
            break;
            
            case "j":
            //pc = JumpAddr
            
            break;
            
            case "jal":
            //store return address in $ra and jump to JumpAddress
            //R[31] = pc + 4, pc = JumpAddr
            
            break;
            
            case "jr":
            
            break;
            
            case "lbu":
            
            break;
            
            case "lhu":
            
            break;
            
            case "ll":
            
            break;
            
            case "lui":
            
            break;
            
            case "lw":
            
            break;
            
            case "nor":
            
            break;
            
            case "or":
            
            break;
            
            case "ori":
            
            break;
            
            case "slt":
            
            break;
            
            case "slti":
            
            break;
            
            case "sltiu":
            
            break;
            
            case "sltu":
            
            break;
            
            case "sll":
            
            break;
            
            case "srl":
            
            break;
            
            case "sb":
            
            break;
            
            case "sc":
            
            break;
            
            case "sh":
            
            break;
            
            case "sw":
            
            break;
            
            case "sub":
            
            break;
            
            case "subu":
            
            break;
            
            default:
               //print out invalid instruction message
               System.out.println(program.get(pc).instruct);
            break;
               
         }

         //increment pc
         pc++;
      }

   }

   public void run()
   {

   }


   public void memory(int num1, int num2)
   {
      //printing out values from datamemory[num1] to datamemory[num2]
      for(int i = num1; i <= num2; i++){
         System.out.printf("%d = %d", i, dataMemory[i]);
      }
   }

   public void clear()
   {

      //clear pc
      pc = 0;
      //clear memory
      Arrays.fill(dataMemory, 0);
      //clear registers
      Arrays.fill(registers, 0);
   }

   public void quit()
   {
      System.out.println("Quitting Program...");
   }
}
