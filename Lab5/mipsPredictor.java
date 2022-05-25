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

//---to do---
//maybe fill ghr and prediction table with zeros in constructors?
//make function to help with updating prediction table?
//function to get prediction or just do it in step? maybe get/update prediction is same function?

public class mipsPredictor extends mipsEmulator {

    /****************************************************
     * Class is to organize data and simplify process of
     * predicting branches as correlating branch predictor
     * for assembly programs.
     ****************************************************/

    // ---MEMBERS---
    public int predictionsCount; // amount of predictions made in program
    public int predictionsCorrect; // amount of predictions made correctly in program
    public int ghrSize; // how long we want the GHR shift register to be
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
        ghrSize = 2;
        ghr = new ArrayList<>();
        predictionTable = new ArrayList<>();

    }

    public mipsPredictor(ArrayList<Instruction> assembledProgram) {
        // run previous constructor
        super(assembledProgram);

        // initialize members
        predictionsCount = 0;
        predictionsCorrect = 0;
        ghrSize = 2;
        ghr = new ArrayList<>();
        predictionTable = new ArrayList<>();

    }

    public mipsPredictor(mipsAssembler assembled) {
        // run previous constructor
        super(assembled);

        // initialize members
        predictionsCount = 0;
        predictionsCorrect = 0;
        ghrSize = 2;
        ghr = new ArrayList<>();
        predictionTable = new ArrayList<>();

    }

    public mipsPredictor(mipsAssembler assembled, int inputSize) {
        // run previous constructor
        super(assembled);

        // initialize members
        predictionsCount = 0;
        predictionsCorrect = 0;
        ghrSize = inputSize;
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

        // keep track of steps executed
        int stepsExecuted;
        boolean result;
        for (stepsExecuted = 0; stepsExecuted < numOfSteps; stepsExecuted++) {
            if (pc < program.size()) {

                // if instruction is a branch
                if (((program.get(pc).instruct == "beq") || (program.get(pc).instruct == "beq"))) {
                    // get index for update prediction
                    int index = ghr2index();
                    // make prediction and increment counter
                    result = updateprediction(index);

                    // update ghr
                    if (result == true) {
                        ghrShift(1);
                    } else {
                        ghrShift(0);
                    }

                }

                // run instruction
                executeInst();
            } else {
                break;
            }

        }

        System.out.printf("\t%d instruction(s) executed\n", stepsExecuted);
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

    public void predictions() {
        // prints out prediction results of loaded program

        // example output
        // accuracy 61.79% (8360 correct predictions, 13529 predictions)

        System.out.printf("accuracy %f%% (%d correct predictions, %d predictions)",
                (predictionsCorrect / predictionsCount) * 100, predictionsCorrect, predictionsCount);
    }

    // prediction functions
    public int ghr2index() {
        if (ghr.isEmpty()) {
            return 0;
        } else {
            // store array as string
            String temp = ghr.toString();

            // replace parts of string and convert to binary value
            temp = temp.replace('[', ' ');
            temp = temp.replace(']', ' ');
            temp = temp.replace(',', ' ');
            temp = temp.replaceAll(" ", "");

            // parse out integer from binary string
            int num = Integer.parseInt(temp, 2);

            // return index
            return num;
        }
    }

    public void ghrShift(int input) {
        // shift everyting down, make space for new input
        for (int i = ghrSize - 1; i > 0; i--) {
            ghr.set(i, ghr.get(i - 1));
        }

        // insert new value
        ghr.set(0, input);
    }

    // updates prediction function
    // takes in prediction from table using address given by ghr
    // have int or string taken to determine how prediction should be updated(ex.
    // flowchart from lecture)
    public boolean updateprediction(int index) {

        // cases dependent on if taken or not
        // prediction from prediction table

        int prediction = predictionTable.get(index);

        // check if correct
        boolean correct = false;
        if (program.get(pc).instruct == "beq") {
            if (registers[program.get(pc).rs] == registers[program.get(pc).rt]) {
                if (prediction > 1) {
                    correct = true;
                }
            }

        } else if (program.get(pc).instruct == "bne") {
            if (registers[program.get(pc).rs] != registers[program.get(pc).rt]) {
                if (prediction > 1) {
                    correct = true;
                }
            }
        }

        // strongly nt
        if (prediction == 0) {
            // increment prediction table if prediction false
            // if correct, do nothing
            if (correct == false) {
                predictionTable.set(index, 1);
            }

        }

        // weakly not taken
        if (prediction == 1) {
            // if prediction is not taken
            // if prediction true, decrement
            // if prediction false, increment
            if (correct == true) {
                predictionTable.set(index, 0);
            } else {
                predictionTable.set(index, 2);
            }

        }

        // weakly t
        if (prediction == 2) {
            // if prediciton taken
            // if prediction true, increment
            // else, decrement prediction
            if (correct == true) {
                predictionTable.set(index, 3);
            } else {
                predictionTable.set(index, 1);
            }
        }

        // strongly taken
        if (prediction == 3) {
            // if prediction taken
            // if prediction true, do nothing
            // else, decrement prediction
            if (correct == false) {
                predictionTable.set(index, 2);
            }
        }
        return correct;
    }

}