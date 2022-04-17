package Lab2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;

public class lab2 {

    // HashMap look up tables
    // type?
    // opcodes
    // registers
    // functions
    // labels/address
    // whatever else you need

    // find efficeint way to initialize map
    // maybe make function to help initialize it?
    public HashMap<String, Integer> mapOpcode = new HashMap<>();

    public class Instruction {

        String type;
        String opcode;
        String rs;
        String rt;
        String rd;
        String shamt;
        String func;
        String immediate;
        String address;

        // constructor

        // functions
        // one to help convert to binary
        //
    }

    public static void main(String[] args) {
        System.out.println("Hello World"); // prints Hello World
        secondpass("./Lab2/test2.asm");
    }

    // takes filename as input
    // will read file line by line
    // passes line to line filtering function
    public static void firstpass(String filename) {
        try {
            int linenum = 1;
            File myfile = new File(filename);
            Scanner fileread = new Scanner(myfile);
            while (fileread.hasNextLine()) {
                // place where parse passes through
                String line = fileread.nextLine();
                label(line, linenum);
                linenum++;
            }
            fileread.close();
        } catch (FileNotFoundException e) {
            System.out.println("File was not found");
        }
    }

    // takes filename as input
    // will read file line by line
    // passes line to line filtering function
    public static void secondpass(String filename) {
        try {
            int linenum = 1;
            File myfile = new File(filename);
            Scanner fileread = new Scanner(myfile);
            while (fileread.hasNextLine()) {
                // place where parse passes through
                String line = fileread.nextLine();
                if (line.contains("#")) {
                    // remove comments from line
                    line = removecomment(line);
                }
                // strip whitespace
                line.trim();
                // test to make sure line is properly filtered at this point
                System.out.println(line);
                // check if present in hashtable, if it is go to that line + 1(label handling)
                // check other hashmap for instruction, registers, and binary conversion
                linenum++;
            }
            fileread.close();
        } catch (FileNotFoundException e) {
            System.out.println("File was not found");
        }
    }

    // finds line or lines that contain labels
    // will have these two numbers into hashmap upon ...
    public static void label(String line, int linenum) {
        if (line.contains(":")) {
            System.out.println(line);
            System.out.println(linenum);
        }
    }

    public static String removecomment(String line) {
        String filtered = "";
        for (int i = 0; i < line.length(); i++) {
            if (Character.toString(line.charAt(i)) != "#") {
                filtered += Character.toString(line.charAt(i));
            } else {
                return filtered;
            }
        }
        return filtered;
    }

    // initialize opcode hashmap
    public static void init_opmap(HashMap<String, Integer> opmap) {
        // opcodes are 6 bits (31:26)
        opmap.put("add", 0);
        opmap.put("addi", 8);
        opmap.put("addiu", 0);
        opmap.put("addu", 0);
        opmap.put("and", 0);
        opmap.put("andi", 0);
        opmap.put("beq", 0);
        opmap.put("bne", 0);
        opmap.put("j", 0);
        opmap.put("jal", 0);
        opmap.put("jr", 0);
        opmap.put("lbu", 0);
        opmap.put("lhu", 0);
        opmap.put("ll", 0);
        opmap.put("lui", 0);
        opmap.put("lw", 0);
        opmap.put("nor", 0);
        opmap.put("or", 0);
        opmap.put("ori", 0);
        opmap.put("slt", 0);
        opmap.put("slti", 0);
        opmap.put("sltiu", 0);
        opmap.put("sltu", 0);
        opmap.put("sll", 0);
        opmap.put("srl", 0);
        opmap.put("sb", 0);
        opmap.put("sc", 0);
        opmap.put("sh", 0);
        opmap.put("sw", 0);
        opmap.put("sub", 0);
        opmap.put("subu", 0);

    }
}
