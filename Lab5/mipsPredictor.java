/**
 * Programmers: Nathan Jaggers, Storm Randolph
 * 
 * CPE 315 - Computer Architecture
 * Dr.Seng
 * Spring 2022
 * 
 */

package Lab5;

import Lab2.Instruction;
import Lab2.mipsAssembler;
import Lab3.mipsEmulator;
import java.util.ArrayList;
import java.util.Arrays;

//finish step


public class mipsPredictor extends mipsEmulator {

    /****************************************************
    * Class is to organize data and simplify process of
    * predicting branches as correlating branch predictor
    * for assembly programs.
    ****************************************************/

   // ---MEMBERS---
   public int predictionsCount; //amount of predictions made in program
   public int predictionsCorrect; //amount of predictions made correctly in program
   public ArrayList<Integer> ghr; // Global History Register  
   public ArrayList<Integer> predictionTable; // table to hold 2-bit prediction value  

   // ---METHODS---
   // constructors
   public mipsPredictor() {
    // run previous constructor
    super();

    // initialize members
    predictionsCount = 0; 
    predictionsCorrect = 0;
    ghr = new ArrayList<>();  
    predictionTable = new ArrayList<>();

 }

 public mipsPredictor(ArrayList<Instruction> assembledProgram) {
    // run previous constructor
    super(assembledProgram);

    // initialize members
    predictionsCount = 0; 
    predictionsCorrect = 0;
    ghr = new ArrayList<>();  
    predictionTable = new ArrayList<>();

 }

 public mipsPredictor(mipsAssembler assembled) {
    // run previous constructor
    super(assembled);

    // initialize members
    predictionsCount = 0; 
    predictionsCorrect = 0;
    ghr = new ArrayList<>();  
    predictionTable = new ArrayList<>();


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
        
         case 'b':
            // print branch predictions
            predictions();
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
      System.out.println("b = output the branch predictor accuracy");
      System.out.println("s = step through a single clock cycle (i.e. simulate 1 cycle and stop)");
      System.out.println("s num = step through num clock cycles");
      System.out.println("r = run until the program ends and display timing summary");
      System.out.println("m num1 num2 = display data memory from location num1 to num2");
      System.out.println("c = clear all registers, memory, and the program counter to 0");
      System.out.println("q = exit the program");
   }

   @Override
   public void step(int numOfSteps) {
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
   public void clear() {
      // clear pc
      pc = 0;
      // clear memory
      Arrays.fill(dataMemory, 0);
      // clear registers
      Arrays.fill(registers, 0);

      // reset prediction values
      predictionsCount = 0; 
      predictionsCorrect = 0;
      ghr.clear();  
      predictionTable.clear();

      // print out notification
      System.out.println("\tSimulator reset");
   }

   public void predictions()
   {
        //prints out prediction results of loaded program

        //example output
        //accuracy 61.79% (8360 correct predictions, 13529 predictions)

        System.out.printf("accuracy %f%% (%d correct predictions, %d predictions)",(predictionsCorrect/predictionsCount)*100, predictionsCorrect, predictionsCount);
   }

}