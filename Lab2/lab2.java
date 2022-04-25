package Lab2;

import Lab2.mipsAssembler;

public class lab2 {

    public static void main(String[] args) {

        //define mips object
        mipsAssembler test = new mipsAssembler(args[0]);
        //processing and parsing handled by the class

        //print out binary
        test.printBinary();
    }

}
