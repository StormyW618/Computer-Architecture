package Lab2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;

public class lab2 {

    // HashMap look up tables
    public static HashMap<String, String> type = new HashMap<>();
    public static HashMap<String, Integer> opcode = new HashMap<>();
    public static HashMap<String, Integer> func = new HashMap<>();
    public static HashMap<String, Integer> reg = new HashMap<>();
    public static HashMap<String, Integer> lineLabel = new HashMap<>();

    // array to store instructions from file
    // public static ArrayList<Instruction> program;

    public class Instruction {

        String type;
        String instruct;
        int opcode;
        int rs;
        int rt;
        int rd;
        int shamt;
        int func;
        int immediate;
        int address;

        // constructor
        public Instruction() {
            type = "";
            instruct = "";
            opcode = 0;
            rs = 0;
            rt = 0;
            rd = 0;
            shamt = 0;
            func = 0;
            immediate = 0;
            address = 0;
        }

        // functions
        // one to help convert to binary
        public int inst2bin() {
            int binary = 0;

            if (type == "R") {
                // type R instruction
                binary |= ((opcode & 63) << 26); // 31-26
                binary |= ((rs & 31) << 21); // 25-21
                binary |= ((rt & 31) << 16); // 20-16
                binary |= ((rd & 31) << 11); // 15-11
                binary |= ((shamt & 31) << 6); // 10-6
                binary |= ((func & 63) << 0); // 5-0
            } else if (type == "I") {
                // type I instruction
                binary |= ((opcode & 63) << 26); // 31-26
                binary |= ((rs & 31) << 21); // 25-21
                binary |= ((rt & 31) << 16); // 20-16
                binary |= ((immediate & 0xFFFF) << 0); // 15-0
            } else if (type == "J") {
                // type J instruction
                binary |= ((opcode & 63) << 26); // 31-26
                binary |= ((address & 31) << 0); // 25-0

            }

            return binary;
        }
    }

    public static void main(String[] args) {
        System.out.println("Hello World"); // prints Hello World
        // firstpass("./Lab2/test2.asm");
        ArrayList<String> lines;
        ArrayList<Instruction> program;
        lines = firstpass("./Lab2/test1.asm");
        secondpass(lines);
        thirdpass(lines);
        System.out.println("test successful");
        // System.out.println(args[0]);
        // secondpass(args[0]);
        // init_opMap(opcode);
        // System.out.println(opcode);

    }

      // takes filename as input
    // will read file line by line
    // passes line to comment filtering function
    // creates list of lines without comments
    // consider: linenums will be one off, won't need to traverse file again
    public static ArrayList firstpass(String filename) {
        try {
            ArrayList<String> lines = new ArrayList<>();
            File myfile = new File(filename);
            Scanner fileread = new Scanner(myfile);
            while (fileread.hasNextLine()) {
                // place where parse passes through
                String line = fileread.nextLine();
                // removes comments from code
                line = filtercomments(line);
                // adding filtered line to list
                lines.add(line);
            }
            fileread.close();
            // return array of lines
            return lines;
        } catch (FileNotFoundException e) {
            System.out.println("File was not found");
        }
        return null;
    }

    // takes arraylist of lines in file as input
    // will read each index and check if it is a label
    // would we need to account for jumps?
    public static void secondpass(ArrayList lines) {
        for (int i = 0; i < lines.size(); i++) {
            String line = (String) lines.get(i);
            if (line.contains(":")) {
                label(line, i + 1);
            }
        }
    }

    // takes filename as input
    // will read file line by line
    // passes line to line filtering function
    public static void thirdpass(ArrayList lines) {
        String test[];
        for (int i = 0; i < lines.size(); i++) {
            String line = (String) lines.get(i);
            // if label and j/jal instruction are on the same line
            // replaces colon with whitespaces and splits into list
            if (line.contains(":") && !line.contains("$")) {
                line = line.trim();
                line = line.replace(":", " ");
                test = line.split(" ");
            }
            // if label and any other instruction are on the same line
            else if (line.contains(":") && line.contains("$")) {
                line = filter(line);
                line = line.replace(":", " ");
                line = line.replace("$", " $");
                test = line.split(" ");
            }
            // if line doesn't contain label but is j/jal instruction
            else if (!line.contains("$") && line != "") {
                line = line.trim();
                test = line.split(" ");
            }
            // if line contains any other instruction other than j/jal
            else {
                // remove whitespace from line
                line = filter(line);
                // strip whitespace
                line = line.replaceAll("\\s", "");
                line = line.replace("$", " $");
                test = line.split(" ");
            }
        }
        // check if present in hashtable, if it is go to that line + 1(label handling)
        // check other hashmap for instruction, registers, and binary conversion
    }

    // finds line or lines that contain labels
    // will have these two numbers into hashmap upon ...
    public static void label(String line, int linenum) {
        if (line.contains(":")) {
            System.out.println(line);
            System.out.println(linenum);
        }
    }

    // filters out comments from line
    public static String filtercomments(String line) {
        String filtered = "";
        for (int i = 0; i < line.length(); i++) {
            String x = Character.toString(line.charAt(i));
            if (!x.contains("#")) {
                filtered += Character.toString(line.charAt(i));
            } else {
                return filtered;
            }
        }
        return filtered;
    }

    // filters out whitespace from line
    public static String filter(String line) {
        String filtered = "";
        for (int i = 0; i < line.length(); i++) {
            String x = Character.toString(line.charAt(i));
            if (!x.contains("\\s+") && !x.contains(" ") && !x.contains(",")) {
                filtered += Character.toString(line.charAt(i));
            }
        }
        return filtered;
    }

    // initialize instruction type hashmap
    public static void init_type(HashMap<String, String> typeMap) {
        // instructions are R, I, or J type
        typeMap.put("add", "R");
        typeMap.put("addi", "I");
        typeMap.put("addiu", "I");
        typeMap.put("addu", "R");
        typeMap.put("and", "R");
        typeMap.put("andi", "I");
        typeMap.put("beq", "I");
        typeMap.put("bne", "I");
        typeMap.put("j", "J");
        typeMap.put("jal", "J");
        typeMap.put("jr", "R");
        typeMap.put("lbu", "I");
        typeMap.put("lhu", "I");
        typeMap.put("ll", "I");
        typeMap.put("lui", "I");
        typeMap.put("lw", "I");
        typeMap.put("nor", "R");
        typeMap.put("or", "R");
        typeMap.put("ori", "I");
        typeMap.put("slt", "R");
        typeMap.put("slti", "I");
        typeMap.put("sltiu", "I");
        typeMap.put("sltu", "R");
        typeMap.put("sll", "R");
        typeMap.put("srl", "R");
        typeMap.put("sb", "I");
        typeMap.put("sc", "I");
        typeMap.put("sh", "I");
        typeMap.put("sw", "I");
        typeMap.put("sub", "R");
        typeMap.put("subu", "R");

    }

    // initialize opcode hashmap
    public static void init_opMap(HashMap<String, Integer> opMap) {
        // opcodes are 6 bits (31:26)
        opMap.put("add", 0x0);
        opMap.put("addi", 0x8);
        opMap.put("addiu", 0x9);
        opMap.put("addu", 0x0);
        opMap.put("and", 0x0);
        opMap.put("andi", 0xc);
        opMap.put("beq", 0x4);
        opMap.put("bne", 0x5);
        opMap.put("j", 0x2);
        opMap.put("jal", 0x3);
        opMap.put("jr", 0x0);
        opMap.put("lbu", 0x24);
        opMap.put("lhu", 0x25);
        opMap.put("ll", 0x30);
        opMap.put("lui", 0xf);
        opMap.put("lw", 0x23);
        opMap.put("nor", 0x0);
        opMap.put("or", 0x0);
        opMap.put("ori", 0xd);
        opMap.put("slt", 0x0);
        opMap.put("slti", 0xa);
        opMap.put("sltiu", 0xb);
        opMap.put("sltu", 0x0);
        opMap.put("sll", 0x0);
        opMap.put("srl", 0x0);
        opMap.put("sb", 0x28);
        opMap.put("sc", 0x38);
        opMap.put("sh", 0x29);
        opMap.put("sw", 0x2b);
        opMap.put("sub", 0x0);
        opMap.put("subu", 0x0);

    }

    // initialize function address hashmap
    public static void init_func(HashMap<String, Integer> funcMap) {
        // registers are 6 bits (5:0)
        funcMap.put("add", 0x20);
        funcMap.put("addu", 0x21);
        funcMap.put("and", 0x24);
        funcMap.put("jr", 0x08);
        funcMap.put("nor", 0x27);
        funcMap.put("or", 0x25);
        funcMap.put("slt", 0x2a);
        funcMap.put("sltu", 0x2b);
        funcMap.put("sll", 0x00);
        funcMap.put("srl", 0x02);
        funcMap.put("sub", 0x22);
        funcMap.put("subu", 0x23);
    }

    // initialize register address hashmap
    public static void init_reg(HashMap<String, Integer> regMap) {
        // registers are from 0-31
        regMap.put("$zero", 0);
        regMap.put("$0", 0);
        regMap.put("$at", 1);
        regMap.put("$v0", 2);
        regMap.put("$v1", 3);
        regMap.put("$a0", 4);
        regMap.put("$a1", 5);
        regMap.put("$a2", 6);
        regMap.put("$a3", 7);
        regMap.put("$t0", 8);
        regMap.put("$t1", 9);
        regMap.put("$t2", 10);
        regMap.put("$t3", 11);
        regMap.put("$t4", 12);
        regMap.put("$t5", 13);
        regMap.put("$t6", 14);
        regMap.put("$t7", 15);
        regMap.put("$s0", 16);
        regMap.put("$s1", 17);
        regMap.put("$s2", 18);
        regMap.put("$s3", 19);
        regMap.put("$s4", 20);
        regMap.put("$s5", 21);
        regMap.put("$s6", 22);
        regMap.put("$s7", 23);
        regMap.put("$t8", 24);
        regMap.put("$t9", 25);
        regMap.put("$k0", 26);
        regMap.put("$k1", 27);
        regMap.put("$gp", 28);
        regMap.put("$sp", 29);
        regMap.put("$fp", 30);
        regMap.put("$ra", 31);
    }

}
