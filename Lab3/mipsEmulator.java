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
            // display data memory from location num1 to num2
            
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

      //create flag for incrementing
      boolean increment = true;  

      for (int i = 0; i < numOfSteps; i++)
      {
         switch(program.get(pc).instruct)
         {
            case "add":
            //add rs and rt, store in rd
            //R[rd] = R[rs] + R[rt]
            registers[program.get(pc).rd] = registers[program.get(pc).rs] + registers[program.get(pc).rt]; 
            
            break;
            
            case "addi":
            //add rs and SignExtendedimmediate, store in rt
            //R[rt] = R[rs] + SignExtImm
            registers[program.get(pc).rt] = registers[program.get(pc).rs] + program.get(pc).immediate; 
            
            break;
            
            case "addiu":
            //add unsigned rs and SignExtendedimmediate, store in rt
            //R[rt] = R[rs] + SignExtImm 
            
            break;
            
            case "addu":
            //add unsigned rs and rt, store in rd
            //R[rd] = R[rs] + R[rt]
            
            break;
            
            case "and":
            //bitwise and rs and rt, store in rd
            //R[rd] = R[rs] & R[rt]
            registers[program.get(pc).rd] = registers[program.get(pc).rs] & registers[program.get(pc).rt];
            
            break;
            
            case "andi":
            //and rs and ZeroExtendedImm, store in rt
            //R[rt] = R[rs] & ZeroExtImm
            
            break;
            
            case "beq":
            //if rs == rt, pc = pc + 4 + BranchAddr
            //if(R[rs]==R[rt]) PC=PC+4+BranchAddr
            if (registers[program.get(pc).rs]==registers[program.get(pc).rt])
               pc = pc + 4 + registers[program.get(pc).immediate];
            
            //set increment to false so we stay on branch instruction
            increment = false;

            break;
            
            case "bne":
            //if rs == rt, pc = pc + 4 + BranchAddr
            //if(R[rs]!=R[rt]) PC=PC+4+BranchAddr
            if (registers[program.get(pc).rs] != registers[program.get(pc).rt])
               pc = pc + 4 + registers[program.get(pc).immediate];

            //set increment to false so we stay on post branch instruction
            increment = false;

            break;
            
            case "j":
            //change pc to jump label address
            //PC=JumpAddr
            pc = program.get(pc).address;

            //set increment to false so we stay on post jump instruction
            increment = false;
            
            break;
            
            case "jal":
            //store return address in $ra and jump to JumpAddress
            //R[31] = pc + 4, pc = JumpAddr
            registers[31] = pc + 4;
            pc = program.get(pc).address;

            //set increment to false so we stay on post jump instruction
            increment = false;

            break;
            
            case "jr":
            //Jump to address stored in register
            //pc = R[rs]
            pc = registers[program.get(pc).rs];

            //set increment to false so we stay on post jump instruction
            increment = false;
            
            break;
            
            case "lbu":
            //load unsigned byte into rt from address in rs and offset
            //R[rt]={24’b0,M[R[rs]+SignExtImm](7:0)}
            
            break;
            
            case "lhu":
            //load unsigned halfword into rt from address in rs and offset
            //R[rt]={16’b0,M[R[rs]+SignExtImm](15:0)}
            
            break;
            
            case "ll":
            //From wiki - 
            //Load-link returns the current value of a memory location, 
            //while a subsequent store-conditional to the same memory 
            //location will store a new value only if no updates have occurred 
            //to that location since the load-link. 
            //R[rt] = M[R[rs]+SignExtImm] 
            
            break;
            
            case "lui":
            //Load register rt with the upper 16 bits of a 32bit value
            //R[rt] = {imm, 16’b0}
            
            break;
            
            case "lw":
            //load value from memory at address in register rs + offset to register rt
            //R[rt] = M[R[rs]+SignExtImm]
            registers[program.get(pc).rt] = dataMemory[program.get(pc).rs+program.get(pc).immediate];
            
            break;
            
            case "nor":
            //store into register rd the negated bitwise or of rs and rt 
            //R[rd] = ~ (R[rs] | R[rt])
            registers[program.get(pc).rd] = ~ (registers[program.get(pc).rs] | registers[program.get(pc).rt]);
            
            break;
            
            case "or":
            //store into register rd the bitwise or of rs and rt 
            //R[rd] = R[rs] | R[rt]
            registers[program.get(pc).rd] = (registers[program.get(pc).rs] | registers[program.get(pc).rt]);
            
            break;
            
            case "ori":
            //store into register rt the bitwise or of rs and and immediate 
            //R[rt] = R[rs] | ZeroExtImm 
            
            break;
            
            case "slt":
            //return true or false to rd for if register rs is less than register rt
            //R[rd] = (R[rs] < R[rt]) ? 1 : 0
            registers[program.get(pc).rd] = (registers[program.get(pc).rs] < registers[program.get(pc).rt]) ? 1 : 0;
            
            break;
            
            case "slti":
            //return true or false to rt for if register rs is less than immediate
            //R[rt] = (R[rs] < SignExtImm)? 1 : 0
            
            break;
            
            case "sltiu":
            //return true or false to rt for if register rs is less than immediate
            //R[rt] = (R[rs] < SignExtImm)? 1 : 0 
            
            break;
            
            case "sltu":
            //return true or false to rd for if register rs is less than register rt, both as unsigned
            // R[rd] = (R[rs] < R[rt]) ? 1 : 0 
            
            break;
            
            case "sll":
            //store into rd the value in rt shifted to the left shamt times
            //R[rd] = R[rt] << shamt
            registers[program.get(pc).rd] = (registers[program.get(pc).rt] << program.get(pc).shamt);
            break;
            
            case "srl":
            //store into rd the value in rt shifted to the left shamt times
            //the bits shifted in will be zero rather than matching sign
            //R[rd] = R[rt] >>> shamt
            registers[program.get(pc).rd] = (registers[program.get(pc).rt] >>> program.get(pc).shamt);

            break;
            
            case "sb":
            //store into memory at address in rs + offset the first byte in register rt
            //M[R[rs]+SignExtImm](7:0) = R[rt](7:0)
            
            break;
            
            case "sc":
            //From wiki - 
            //Load-link returns the current value of a memory location, 
            //while a subsequent store-conditional to the same memory 
            //location will store a new value only if no updates have occurred 
            //to that location since the load-link. 
            //M[R[rs]+SignExtImm] = R[rt]; R[rt] = (atomic) ? 1 : 0
            
            break;
            
            case "sh":
            //store into memory at address in rs + offset the first halfword in register rt
            //M[R[rs]+SignExtImm](15:0) = R[rt](15:0)
            
            break;
            
            case "sw":
            //store into memory at address in rs + offset the value in register rt
            //M[R[rs]+SignExtImm] = R[rt]
            dataMemory[program.get(pc).rs+program.get(pc).immediate] = registers[program.get(pc).rt];
            
            break;
            
            case "sub":
            //store into register rd the result of subtraction between register rs and rt
            //R[rd] = R[rs] - R[rt] 
            registers[program.get(pc).rd] = (registers[program.get(pc).rs] - registers[program.get(pc).rt]);

            break;
            
            case "subu":
            //store into register rd the result of subtraction between register rs and rt, both unsigned
            //R[rd] = R[rs] - R[rt]
            
            break;
            
            case default:
               //print out invalid instruction message
               System.out.println(program.get(pc).instruct);
            break;
               
         }

         //increment pc 
         if (increment)
            pc++;
         
         //reset increment to true in case it was set to false
         increment = true;
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
