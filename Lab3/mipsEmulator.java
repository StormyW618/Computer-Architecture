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
import java.util.HashMap;

public class mipsEmulator {

   /****************************************************
    * Class is to organize data and simplify process of
    * emulating assembly program.
    ****************************************************/

   // ---MEMBERS---
   int pc; // Program counter
   int[] registers; // register file
   int[] dataMemory; // memory to store and read data
   ArrayList<Instruction> program; //list of instructions
   HashMap<Integer, String> regReverse;

   // ---METHODS---
   // constructors
   public mipsEmulator() {
      // initalize all members to default, zero, null
      pc = 0;
      registers = new int[32];
      dataMemory = new int[8192];
      regReverse = new HashMap<>();
      program = new ArrayList<>();

      //init table
      init_regRev(regReverse);
   }

   public mipsEmulator(ArrayList<Instruction> assembledProgram) {
      // initalize all members to default, zero, null
      pc = 0;
      registers = new int[32];
      dataMemory = new int[8192];
      regReverse = new HashMap<>();
      // initialize program and table
      program = assembledProgram;
      init_regRev(regReverse);
      
   }

   public mipsEmulator(mipsAssembler assembled) {
      // initalize all members to default, zero, null
      pc = 0;
      registers = new int[32];
      dataMemory = new int[8192];
      regReverse = new HashMap<>();
      // initialize program and table
      program = assembled.program;
      init_regRev(regReverse);
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
            String[] splitInput = userInput.split(" ");
            if(splitInput.length == 1)
            {
               //run step command for 1 iteration
               step(1);
            }
            else if(splitInput.length == 2)
            {
               splitInput[1] = splitInput[1].trim();
               try{
                  //get number of steps after s command
                  step(Integer.parseInt(splitInput[1]));
               }
               catch(NumberFormatException e){
                  System.out.println("Second argument not a number");
               }

            }
            else
            {
               System.out.println("Too many arguments, try again.");
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
            if(arr.length == 1)
            {
               memory(0, 0);
            }
            else if(arr.length == 2)
            {
               int num1 = Integer.parseInt(arr[1]);
               memory(num1, num1);
            }
            else if(arr.length == 3)
            {
               int num1 = Integer.parseInt(arr[1]);
               int num2 = Integer.parseInt(arr[2]);
               memory(num1, num2);
            }
            else
            {
               System.out.println("Too many arguments, try again.");
            }

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
      //Dump all including $at
      // System.out.println("\npc = " + pc);
      // for(int i = 0; i < 32; i++)
      // {
      //    if (i%4==0 && i != 0){
      //       System.out.println();   
      //    }
      //    System.out.printf("%s = %d\t", regReverse.get(i), registers[i]);
      // }
      // System.out.println();

      //print all registers excluding $at
      //purpose is to follow format of expected output from instructor
      System.out.println("\npc = " + pc);
      int printCount = 0;
      for(int i = 0; i < 32; i++)
      {
         if (printCount%4==0 && printCount != 0){
            System.out.println();   
         }
         if ((i!=1)&(i!=26)&(i!=27)&(i!=28)&(i!=30))
         {
            System.out.printf("%s = %d\t", regReverse.get(i), registers[i]);
            printCount++;
         }
      }
      System.out.println();


   }

   public void step(int numOfSteps)
   {
      // uses program array list and executes
      // a specified number of instructions. Number
      // of instructions process id determined by 
      // value passed into function 

      //keep track of steps executed
      int stepsExecuted;  
      for (stepsExecuted = 0; stepsExecuted < numOfSteps; stepsExecuted++)
      {
         if(pc < program.size())
         {
            executeInst();
         }
         else
         {
            break;
         }

      }

      System.out.printf("\t%d instruction(s) executed\n",stepsExecuted);

   }

   public void run()
   {
      while(pc < program.size())
      {
         executeInst();
      }
   }


   public void memory(int num1, int num2)
   {
      //printing out values from datamemory[num1] to datamemory[num2]
      for(int i = num1; i <= num2; i++){
         //desired output cause I like it and think it looks cool
         //System.out.printf("M[%d] = %d\n", i, dataMemory[i]);
         
         //output for assignment cause diff and I want points and for
         //me and storm to succeed and thrive
         System.out.printf("[%d] = %d\n", i, dataMemory[i]);
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
      //print out notification
      System.out.println("\tSimulator reset");
   }

   public void quit()
   {
      //System.out.println("Quitting Program...");
   }

   public void init_regRev(HashMap<Integer, String> regRevMap) {
      // initialize register address hashmap
      // registers are from 0-31
      regRevMap.put(0,"$0");
      regRevMap.put(1,"$at");
      regRevMap.put(2,"$v0");
      regRevMap.put(3,"$v1");
      regRevMap.put(4,"$a0");
      regRevMap.put(5,"$a1");
      regRevMap.put(6,"$a2");
      regRevMap.put(7,"$a3");
      regRevMap.put(8,"$t0");
      regRevMap.put(9,"$t1");
      regRevMap.put(10,"$t2");
      regRevMap.put(11,"$t3");
      regRevMap.put(12,"$t4");
      regRevMap.put(13,"$t5");
      regRevMap.put(14,"$t6");
      regRevMap.put(15,"$t7");
      regRevMap.put(16,"$s0");
      regRevMap.put(17,"$s1");
      regRevMap.put(18,"$s2");
      regRevMap.put(19,"$s3");
      regRevMap.put(20,"$s4");
      regRevMap.put(21,"$s5");
      regRevMap.put(22,"$s6");
      regRevMap.put(23,"$s7");
      regRevMap.put(24,"$t8");
      regRevMap.put(25,"$t9");
      regRevMap.put(26,"$k0");
      regRevMap.put(27,"$k1");
      regRevMap.put(28,"$gp");
      regRevMap.put(29,"$sp");
      regRevMap.put(30,"$fp");
      regRevMap.put(31,"$ra");
  }

  public void executeInst()
  {
   //create flag for incrementing
   boolean increment = true;

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
      {
         pc = pc + 1 + program.get(pc).immediate;
      
         //set increment to false so we stay on branch instruction
         increment = false;
      }

      break;
      
      case "bne":
      //if rs == rt, pc = pc + 4 + BranchAddr
      //if(R[rs]!=R[rt]) PC=PC+4+BranchAddr
      if (registers[program.get(pc).rs] != registers[program.get(pc).rt])
      {
         pc = pc + 1 + program.get(pc).immediate;

         //set increment to false so we stay on post branch instruction
         increment = false;
      }

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
      registers[31] = pc + 1;
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
      registers[program.get(pc).rt] = dataMemory[registers[program.get(pc).rs]+program.get(pc).immediate];
      
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

      //V1
      //dataMemory[registers[program.get(pc).rs]+program.get(pc).immediate] = registers[program.get(pc).rt];
      
      //V2
      if((!program.isEmpty()) && (pc>-1) && (pc<=program.size()))
      {
         int rIndex1 = program.get(pc).rs;
         int rIndex2 = program.get(pc).rt;

         if((registers.length > 0) && ((rIndex1>-1)&&(rIndex2>-1)) && ((rIndex1<registers.length)&&(rIndex2<registers.length)) )
         {
            int memIndex = registers[rIndex1]+program.get(pc).immediate;

            if((dataMemory.length > 0) && (memIndex>-1) && (memIndex<dataMemory.length))
            {
               dataMemory[memIndex] = registers[rIndex2];
            }
         }
      }
      
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
      
      default:
         //print out invalid instruction message
         System.out.println(program.get(pc).instruct);
      break;
         
   }

   //increment pc 
   if (increment)
   pc++;

  }

}
