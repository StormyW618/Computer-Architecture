package Lab2;

import Lab2.mips;

public class lab2 {

    public static void main(String[] args) {

        //define mips object
        mips test1 = new mips();

        //clean file of comments and white spaces
        //then load file's parsed data into instruction list
        //mips.firstpass(args[0]);
        mips.firstpass("./Lab2/test2.asm");
        mips.secondpass();
        mips.thirdpass();
        System.out.println("test successful");

        //convert file to binary
        mips.prog2bin();

        //print out binary
        mips.printBinary();
    }

}
