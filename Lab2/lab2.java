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
    public HashMap<String, String> type = new HashMap<>();

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
                // remove comments and whitespace from line
                line = filter(line);
                // strip whitespace
                line = line.replaceAll("\\s", "");
                // test to make sure line is properly filtered at this point
                if (line != "") {
                    System.out.println(line);
                }
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
    public static String filter(String line) {
        String filtered = "";
        for (int i = 0; i < line.length(); i++) {
            String x = Character.toString(line.charAt(i));
            if (!x.contains("#")) {
                if (!x.contains("\\s+") && !x.contains(" ") && !x.contains("\n")) {
                    filtered += Character.toString(line.charAt(i));
                }
            } else {
                return filtered;
            }
        }
        return filtered;
    }

    // finds line or lines that contain labels
    // will have these two numbers into hashmap upon ...
    public static void label(String line, int linenum) {
        if (line.contains(":")) {
            System.out.println(line);
            System.out.println(linenum);
        }
    }

    // initialize opcode hashmap
    public static void init_opMap(HashMap<String, Integer> opMap) {
        // opcodes are 6 bits (31:26)
        opMap.put("add",    0x0);
        opMap.put("addi",   0x8);
        opMap.put("addiu",  0x9);
        opMap.put("addu",   0x0);
        opMap.put("and",    0x0);
        opMap.put("andi",   0xc);
        opMap.put("beq",    0x4);
        opMap.put("bne",    0x5);
        opMap.put("j",      0x2);
        opMap.put("jal",    0x3);
        opMap.put("jr",     0x0);
        opMap.put("lbu",    0x24);
        opMap.put("lhu",    0x25);
        opMap.put("ll",     0x30);
        opMap.put("lui",    0xf);
        opMap.put("lw",     0x23);
        opMap.put("nor",    0x0);
        opMap.put("or",     0x0);
        opMap.put("ori",    0xd);
        opMap.put("slt",    0x0);
        opMap.put("slti",   0xa);
        opMap.put("sltiu",  0xb);
        opMap.put("sltu",   0x0);
        opMap.put("sll",    0x0);
        opMap.put("srl",    0x0);
        opMap.put("sb",     0x28);
        opMap.put("sc",     0x38);
        opMap.put("sh",     0x29);
        opMap.put("sw",     0x2b);
        opMap.put("sub",    0x0);
        opMap.put("subu",   0x0);

    }

    // initialize opcode hashmap
    public static void init_type(HashMap<String, String> typeMap) {
        // instructions are R, I, or J type
        typeMap.put("add",    "R");
        typeMap.put("addi",   "I");
        typeMap.put("addiu",  "I");
        typeMap.put("addu",   "R");
        typeMap.put("and",    "R");
        typeMap.put("andi",   "I");
        typeMap.put("beq",    "I");
        typeMap.put("bne",    "I");
        typeMap.put("j",      "J");
        typeMap.put("jal",    "J");
        typeMap.put("jr",     "R");
        typeMap.put("lbu",    "I");
        typeMap.put("lhu",    "I");
        typeMap.put("ll",     "I");
        typeMap.put("lui",    "I");
        typeMap.put("lw",     "I");
        typeMap.put("nor",    "R");
        typeMap.put("or",     "R");
        typeMap.put("ori",    "I");
        typeMap.put("slt",    "R");
        typeMap.put("slti",   "I");
        typeMap.put("sltiu",  "I");
        typeMap.put("sltu",   "R");
        typeMap.put("sll",    "R");
        typeMap.put("srl",    "R");
        typeMap.put("sb",     "I");
        typeMap.put("sc",     "I");
        typeMap.put("sh",     "I");
        typeMap.put("sw",     "I");
        typeMap.put("sub",    "R");
        typeMap.put("subu",   "R");
        
    }
}
