/**
 * Programmers: Nathan Jaggers, Storm Randolph
 * 
 * CPE 315 - Computer Architecture
 * Dr.Seng
 * Spring 2022
 * 
 */

package Lab4;

import java.util.Arrays;
import java.util.LinkedList;
import Lab2.Instruction;
import Lab2.mipsAssembler;
import java.util.ArrayList;
import java.util.Queue;
import Lab3.mipsEmulator;

      //todo
      //flesh out class

public class mipsSimulator extends mipsEmulator {
   /****************************************************
    * Class is to organize data and simplify process of
    * simulating piplines running assembly program.
    ****************************************************/

   // ---MEMBERS---
   public int clock; //int to hold clock count for processor
   public String [] stages; //holds names of stages 
   public Queue<String> pipeline; //queue to hold instructions going through pipeline

    
   // ---METHODS---
   // constructors
   public mipsSimulator()
   {
      //run previous constructor
      super();

      //initialize members
      //queue is an interface, initialize with linked list or something
      pipeline = new LinkedList<>();
      clock = 0;
      initStages();
      initQueue();

   }

   public mipsSimulator(ArrayList<Instruction> assembledProgram)
   {
      //run previous constructor
      super(assembledProgram);

      //initialize members
      //queue is an interface, initialize with linked list or something
      pipeline = new LinkedList<>();
      clock = 0;

      initStages();
      initQueue();

   }

   public mipsSimulator(mipsAssembler assembled)
   {
      //run previous constructor
      super(assembled);

      //initialize members
      //queue is an interface, initialize with linked list or something
      pipeline = new LinkedList<>();
      clock = 0;

      initStages();
      initQueue();

   }

   //stall function? - to insert a stall in the queue?
   //stall Detect? to determine when to stall?

   // user commands
   @Override
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

   @Override
   public void help()
   {
      System.out.println("h = show help");
      System.out.println("d = dump register state");
      System.out.println("s = step through a single clock cycle (i.e. simulate 1 cycle and stop)");
      System.out.println("s num = step through num clock cycles");
      System.out.println("r = run until the program ends and display timing summary");
      System.out.println("m num1 num2 = display data memory from location num1 to num2");
      System.out.println("c = clear all registers, memory, and the program counter to 0");
      System.out.println("q = exit the program");
   }

   public void showPipeline()
   {
      Object[] temp = pipeline.toArray();

      System.out.printf("pc\t\t%s/%s\t%s/%s\t%s/%s\t%s/%s\n",
                        stages[0],stages[1],
                        stages[1],stages[2],
                        stages[2],stages[3],
                        stages[3],stages[4]);
      
      System.out.printf("%d\t\t%s\t%s\t%s\t%s\n",
                        pc,
                        temp[0],
                        temp[1],
                        temp[3],
                        temp[4]);
   }

   @Override
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

   @Override
   public void run()
   {
      while(pc < program.size())
      {
         executeInst();
      }
   }

   @Override
   public void clear()
   {
      //clear pc
      pc = 0;
      //clear memory
      Arrays.fill(dataMemory, 0);
      //clear registers
      Arrays.fill(registers, 0);
      //clear pipeline
      pipeline.clear();
      pipeline.add("empty");
      pipeline.add("empty");
      pipeline.add("empty");
      pipeline.add("empty");
      //print out notification
      System.out.println("\tSimulator reset");
   }

   public void initStages()
   {
      //fill in names of stages
      stages[0] = "IF";
      stages[1] = "ID";
      stages[2] = "EXE";
      stages[3] = "MEM";
      stages[4] = "WB";

   }

   public void initQueue()
   {
      //remove anything in the pipeline
      while(pipeline.size()>0)
         pipeline.remove();

      //fill in pipeline with strings
      for (int i = 0; i < stages.length-1; i++)
      pipeline.add("empty");

   }

}