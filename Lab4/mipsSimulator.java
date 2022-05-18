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
import Lab2.Instruction;
import Lab2.mipsAssembler;
import java.util.ArrayList;
import Lab3.mipsEmulator;

//todo
//flesh out class

public class mipsSimulator extends mipsEmulator {
   /****************************************************
    * Class is to organize data and simplify process of
    * simulating piplines running assembly program.
    ****************************************************/

   // ---MEMBERS---
   public int clock; // int to hold clock count for processor
   public int instruction; // int to hold instruction count for processor
   public boolean execute; // flag to determine if to execute an instruction
   public ArrayList<String> pipeline; // queue to hold instructions going through pipeline

   // ---METHODS---
   // constructors
   public mipsSimulator() {
      // run previous constructor
      super();

      // initialize members
      // queue is an interface, initialize with linked list or something
      pipeline = new ArrayList<>();
      clock = 0;
      instruction = 0;
      execute = true;

      initPipeline();

   }

   public mipsSimulator(ArrayList<Instruction> assembledProgram) {
      // run previous constructor
      super(assembledProgram);

      // initialize members
      // queue is an interface, initialize with linked list or something
      pipeline = new ArrayList<>();
      clock = 0;
      instruction = 0;
      execute = true;

      initPipeline();
   }

   public mipsSimulator(mipsAssembler assembled) {
      // run previous constructor
      super(assembled);

      // initialize members
      // queue is an interface, initialize with linked list or something
      pipeline = new ArrayList<>();
      clock = 0;
      instruction = 0;
      execute = true;

      initPipeline();

   }

   // stall function? - to insert a stall in the queue?
   // stall Detect? to determine when to stall?
   //hazard detection functions
   //inputs: instruction output from IF/ID stage, ID/EX mem read
   //outputs: adds stall to pipeline or doesn't
   //make a call to this function for every instruction in pipeline
   //change to boolean to implement hazard flag
   public void hazard_detection(){
      //check for use after load hazard
      if(pipeline.get(1).contains("lw") && program.get(pc - 1).rt == program.get(pc).rt || program.get(pc - 1).rt == program.get(pc).rs ){
            //paas to use after load resolution function
            use_after_load();
      }
      //check for uncoditional jump hazard
      if(pipeline.get(0).contains("j")||pipeline.get(0).contains("jal")||pipeline.get(0).contains("jr")){
         //pass to unconditional jump resolution function
         unconditional_jump();
      }
      // check for branch hazards
      //program continues to run normally until exe/mem state//
      if(pipeline.get(2).contains("beq") ||pipeline.get(2).contains("bne")){
         //pass to branch hazard resolution function
         branch_hazard();
      }
   }

   public void use_after_load(){
       //partially shift pipeline and insert stall
       pipeline.set(3, pipeline.get(2));
       pipeline.set(2, pipeline.get(1));
       pipeline.set(1, "stall");
   }

   public void unconditional_jump(){
      shiftPipeline("squash");
   }

   public void branch_hazard(){
      //check if beq instruction
      if(pipeline.get(2).contains("beq")){
         //check if branch was taken 
         //if taken, 3 wrong path instructions taken
         //if not, do nothing, everything works fine
         if(program.get(pc-3).rs == program.get(pc-3).rt){
            pipeline.set(3, pipeline.get(2));
            pipeline.set(2, "squash");
            pipeline.set(1, "squash");
            pipeline.set(0, "squash");
         }
      }
       //check if bne instruction
       if(pipeline.get(2).contains("bne")){
         //check if branch was taken 
         //if taken, 3 wrong path instructions taken
         //if not, do nothing, everything works fine
         if(program.get(pc-3).rs != program.get(pc-3).rt){
            pipeline.set(3, pipeline.get(2));
            pipeline.set(2, "squash");
            pipeline.set(1, "squash");
            pipeline.set(0, "squash");
         }
      }
   }

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

         case 'p':
         // show pipeline register 
         showPipeline();
         break;

         case 's':
            // step through the program
            // single or multiple based on input
            String[] splitInput = userInput.split(" ");
            if (splitInput.length == 1) {
               // run step command for 1 iteration
               step(1);
            } else if (splitInput.length == 2) {
               splitInput[1] = splitInput[1].trim();
               try {
                  // get number of steps after s command
                  step(Integer.parseInt(splitInput[1]));
               } catch (NumberFormatException e) {
                  System.out.println("Second argument not a number");
               }

            } else {
               System.out.println("Too many arguments, try again.");
            }

            break;

         case 'r':
            // run the program in its entirety
            run();
            break;

         case 'm':
            // display data memory from location num1 to num2
            // split string into list to properly parse out args num1 and num2
            String[] arr = userInput.split(" ");
            if (arr.length == 1) {
               memory(0, 0);
            } else if (arr.length == 2) {
               int num1 = Integer.parseInt(arr[1]);
               memory(num1, num1);
            } else if (arr.length == 3) {
               int num1 = Integer.parseInt(arr[1]);
               int num2 = Integer.parseInt(arr[2]);
               memory(num1, num2);
            } else {
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
   public void help() {
      System.out.println("h = show help");
      System.out.println("d = dump register state");
      System.out.println("p = show pipeline registers");
      System.out.println("s = step through a single clock cycle (i.e. simulate 1 cycle and stop)");
      System.out.println("s num = step through num clock cycles");
      System.out.println("r = run until the program ends and display timing summary");
      System.out.println("m num1 num2 = display data memory from location num1 to num2");
      System.out.println("c = clear all registers, memory, and the program counter to 0");
      System.out.println("q = exit the program");
   }

   public void showPipeline() {

      //System.out.println("pc    IF/ID    ID/EXE    EXE/MEM    MEM/WB");
      System.out.printf("%3s    %-7s  %-7s  %-7s  %-7s\n",
                        "pc",
                        "if/id",
                        "id/exe",
                        "exe/mem",
                        "mem/wb");

      System.out.printf("%3s    %-7s  %-7s  %-7s  %-7s\n",
            pc,
            pipeline.get(0),
            pipeline.get(1),
            pipeline.get(2),
            pipeline.get(3));
   }

   @Override
   public void step(int numOfSteps) {
      // uses pipeline array list to simulate
      // instructions steping through the pipeline.
      // each step through the pipeline is a clock cycle.
      // delays are added as needed per instruction.
      // instructions are retrieved and executed 
      // from program array list and executeInst method
      // in mipsEmulator.

      // increment number of cycles
      // must account for delays

      // if not delay
      // clock += 1;
      // else
      // conditional: clock += 3;
      // use after load: clock += 1;
      // jump: clock +=1;

      for (int i = 0; i < numOfSteps; i++) {
         //only step through pipeline if pc is not at end of program
         //or instructions are still in pipeline
         if (pc < program.size() | !piplineEmpty()) 
         {
            //if instructions left feed into pipeline
            //if not feed in emptys
            //check hazard flag   
            if(pc < program.size())
            {
               //add in case to check inside pipeline
               //check rt of load instruct vs rs and rt for register instructs
               shiftPipeline(program.get(pc).instruct);
            }
            else
            {
               shiftPipeline("empty");
            }

            //check incoming instruction feeding into pipeline
            //decide if to execute or simulate?
            //if(pipeline(0) == branch)
               //{ execute = 0};
               //...
               //if(execute)
               //{run as normal}
               //else
               //{simulate pipeline}
            //maybe change execute to delay? and in this loop decide weather to
            //execution of instructions like regular or to simulate a delay.
            //functions that return to boolean if we do delay portion of code or not.
            //functions that help decide when to turn boolean back?

            //if execute run like normal

            //if simulate, (like for branch),
            //simulate what pipeline would do until it
            //catches up to execute. then return to normal executing

            if (execute)
            {
               executeInst();

               // increment number of instructions for each instruction added to pipeline?
               // increment for each instruction coming out of the pipeline? is there a difference
               instruction += 1;
            }
            
            // increment clock cycles for every step through pipeline
            clock += 1;
         } 
         else 
         {
            break;
         }
      }

      // print current pipeline
      showPipeline();
   }

   @Override
   public void run() {
      while (pc < program.size()) {
         executeInst();
      }
   }

   @Override
   public void clear() {
      // clear pc
      pc = 0;
      // clear memory
      Arrays.fill(dataMemory, 0);
      // clear registers
      Arrays.fill(registers, 0);
      // clear pipeline
      pipeline.clear();
      pipeline.add("empty");
      pipeline.add("empty");
      pipeline.add("empty");
      pipeline.add("empty");
      // print out notification
      System.out.println("\tSimulator reset");
   }

   public void initPipeline() {
      // remove anything in the pipeline
         pipeline.clear();

      // fill in pipeline with strings
      for (int i = 0; i < 4; i++)
         pipeline.add("empty");

   }

   public boolean piplineEmpty()
   {
      // function is to check if pipleine is empty
      // primarily used to check if end of program condition reached

      boolean empty = false;

      // if all segments of the pipeline are empty, set flag as true
      if (pipeline.get(0).contains("empty") &
          pipeline.get(1).contains("empty") &
          pipeline.get(2).contains("empty") &
          pipeline.get(3).contains("empty"))
          empty = true;

      return empty;
   }

   public void shiftPipeline(String input)
   {
      pipeline.set(3, pipeline.get(2));
      pipeline.set(2, pipeline.get(1));
      pipeline.set(1, pipeline.get(0));
      pipeline.set(0, input);
   }
}