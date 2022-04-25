/**
 * Programmers: Nathan Jaggers, Storm Randolph
 * 
 * CPE 315 - Computer Architecture
 * Dr.Seng
 * Spring 2022
 * 
 * Lab 2 - MIPS Assembler
 * 
 * Description: To build a MIPS simulator 
 *              front-end assembler/parser.
 *              Takes an assembly file and 
 *              converts it into binary.
 */

package Lab2;

import Lab2.mipsAssembler;

public class lab2 {

    public static void main(String[] args) {

        //define mips object
        //mipsAssembler test = new mipsAssembler(args[0]);
        mipsAssembler test = new mipsAssembler("Lab2/test1.asm");
        //processing and parsing handled by the class

        //print out binary
        test.printBinary();
    }

}
