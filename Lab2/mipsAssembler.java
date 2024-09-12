package Lab2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;

public class mipsAssembler {

    /****************************************************
     * Class is to organize data and simplify process of
     * converting mips assembly program into binary.
     ****************************************************/

    // ---MEMBERS---

    // file io
    public static String filename; // assembly file name
    public static File asmFile; // assembly file

    // look up tables
    public static HashMap<String, String> type;
    public static HashMap<String, Integer> opcode;
    public static HashMap<String, Integer> func;
    public static HashMap<String, Integer> reg;
    public static HashMap<String, Integer> lineLabel;

    // data
    public static ArrayList<String> lines; // holds file instruction lines as strings
    public static ArrayList<Instruction> program; // holds file instructions as parsed data
    public static ArrayList<String> bin; // holds file conversion to binary string

    // ---METHODS---
    // constructors
    public mipsAssembler() {
        // initialize filename with nothing
        filename = "";
        asmFile = null;

        // allocate members
        type = new HashMap<>();
        opcode = new HashMap<>();
        func = new HashMap<>();
        reg = new HashMap<>();
        lineLabel = new HashMap<>();

        lines = new ArrayList<>();
        program = new ArrayList<>();
        bin = new ArrayList<>();

        // initialize tables
        init_tables();

    }

    public mipsAssembler(String userfile) {
        // initialize filename with nothing
        filename = userfile;
        asmFile = null;

        // allocate members
        type = new HashMap<>();
        opcode = new HashMap<>();
        func = new HashMap<>();
        reg = new HashMap<>();
        lineLabel = new HashMap<>();

        lines = new ArrayList<>();
        program = new ArrayList<>();
        bin = new ArrayList<>();

        // initialize tables
        init_tables();

        // Test if proper open
        // if proper, can start processing data
        // if not throws exception
        int resume = firstpass();
        if (resume == 0) {
            secondpass();
            thirdpass();
        }

    }
    // another constructor with file and functions to set it all up?

    // init
    public static void init_tables() {
        // calls all init functions to fill out look up tables
        init_type(type);
        init_opMap(opcode);
        init_func(func);
        init_reg(reg);
    }

    public static void init_type(HashMap<String, String> typeMap) {
        // initialize instruction type hashmap
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

    public static void init_opMap(HashMap<String, Integer> opMap) {
        // initialize opcode hashmap
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

    public static void init_func(HashMap<String, Integer> funcMap) {
        // initialize function address hashmap
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

    public static void init_reg(HashMap<String, Integer> regMap) {
        // initialize register address hashmap
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

    // file io
    public static int firstpass() {
        // takes filename as input
        // will read file line by line
        // passes line to comment filtering function
        // creates list of lines without comments
        try {
            // if no current file name, ask user for input
            if (filename.isEmpty()) {
                System.out.println("Enter the filepath of the asm file you would like to access.");
                Scanner input = new Scanner(System.in);
                filename = input.nextLine();
                input.close();
            }

            // open file and create scanner to read lines
            asmFile = new File(filename);
            Scanner fileread = new Scanner(asmFile);

            // continue until file has been read completely
            while (fileread.hasNextLine()) {

                // place where parse passes through
                String line = fileread.nextLine();
                // removes comments from code
                line = filtercomments(line);

                // removes newline, carage return tabs, etc.
                line = line.replace("\n", "");
                line = line.replace("\r", "");
                line = line.replace("\t", "");
                line = line.replace("#", "");

                // removes spaces if not a jump instruction
                if (!line.contains("j")) {
                    line = line.replace(" ", "");
                }

                // if line isnt empty, add filtered line to list
                if (!(line == "")) {
                    lines.add(line);
                }

            }
            fileread.close();

            // return zero if process exited normally
            return 0;

        } catch (FileNotFoundException e) {
            System.out.println("File was not found");
        }
        // return -1 for irregular exits
        return -1;
    }

    public static void secondpass() {
        // takes arraylist of lines in file as input
        // will read each index and check if it is a label

        int linenum = 0;
        for (int i = 0; i < lines.size(); i++) {
            String line = (String) lines.get(i);
            if (line.contains(":")) {
                label(line, linenum);
            }
            if (line.contains("$") || line.contains(",") || line.contains("j")) {
                linenum++;
            }
        }
    }

    public static void thirdpass() {
        // will read file line by line
        // passes line to line filtering function
        String splitLine[];
        int linenumber = 0;
        ArrayList<String> instlist = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {

            String line = (String) lines.get(i);

            // Test if label on the line
            // if so is it alone?
            if (line.contains(":") && !line.contains("j") && !line.contains("$")) {
                line = line.trim();
                line = line.replace(":", " ");
                splitLine = line.split(" ");
                splitLine = line.split(":");
                instlist.add(splitLine[0]);
            }
            // if label and an instruction are on the same line
            // split label from instruction and then parse
            else if (line.contains(":") && (line.contains("$") || (line.contains("j")))) {
                line = line.trim();
                line = line.replace(":", " ");
                splitLine = line.split(" ");
                // splitLine = line.split(":");
                instlist = parseInstruction(splitLine[1], instlist);
            }
            // line just has an instruction
            else if (!line.contains(":")) {
                instlist = parseInstruction(line, instlist);
            }

            // call function to take parsed data and fill out instruction fields
            // then add to list of instructions
            Instruction lineData = fillInstructData(instlist, linenumber);
            if (lineData.instruct != "") {
                program.add(lineData);
                linenumber++;
            }
            instlist.clear();

        }
    }

    public static void label(String line, int linenum) {
        // finds line or lines that contain labels
        // adds these to a hashmap
        if (line.contains(":")) {
            String[] temp = line.split(":");
            lineLabel.put(temp[0], linenum);
        }
    }

    public static String filtercomments(String lineWithComments) {
        // takes line and filters out comments
        String filtered = "";
        for (int i = 0; i < lineWithComments.length(); i++) {
            String x = Character.toString(lineWithComments.charAt(i));
            if (!x.contains("#")) {
                filtered += Character.toString(lineWithComments.charAt(i));
            } else {
                return filtered;
            }
        }
        return filtered;
    }

    public static String filter(String line) {
        // takes line and filters out whitespace
        String filtered = "";
        for (int i = 0; i < line.length(); i++) {
            String x = Character.toString(line.charAt(i));
            if (!x.contains("\\s+") && !x.contains(" ") && !x.contains(",")) {
                filtered += Character.toString(line.charAt(i));
            }
        }
        return filtered;
    }

    // data io
    public static void printBinary() {
        // update binary list
        prog2bin();

        // loop through binary list and print asm binary
        for (int i = 0; i < bin.size(); i++) {
            System.out.println(bin.get(i));
            if (program.get(i).instruct.contains("invalid"))
                break;
        }
    }

    // would be fun to include function that then recreates the asm file as text

    // data manip
    public static ArrayList<String> parseInstruction(String subLine, ArrayList<String> currentParse) {
        String[] parsedInst;
        ArrayList<String> newParse = currentParse;
        // Test for type of instruction

        if (subLine.contains("j") && !subLine.contains("$")) {
            // if the instruction is a j/jal
            // replaces colon with whitespaces and splits into list
            subLine = subLine.trim();
            parsedInst = subLine.split(" ");
            newParse.add(parsedInst[0]);
            newParse.add(parsedInst[1]);

        } else if (subLine.contains("(")) {
            // split instruction off main string
            parsedInst = subLine.split("\\$", 2);
            // add instruction to list
            newParse.add(parsedInst[0]);
            // replace $ that was split off
            parsedInst[1] = "$" + parsedInst[1];
            // split current string on comma
            parsedInst = parsedInst[1].split(",");
            // add first register to list
            newParse.add(parsedInst[0]);
            // separate immediate and add to list
            parsedInst = parsedInst[1].split("\\(");
            newParse.add(parsedInst[0]);
            // remove closing parenthese and add second reg to list
            parsedInst[1] = parsedInst[1].replace(")", "");
            newParse.add(parsedInst[1]);
        } else {
            parsedInst = subLine.split("\\$", 2);
            newParse.add(parsedInst[0].trim());
            if (parsedInst.length > 1) {
                parsedInst[1] = "$" + parsedInst[1];
                parsedInst = parsedInst[1].split(",");
            }
            for (int j = 0; j < parsedInst.length; j++) {
                newParse.add(parsedInst[j]);
            }
        }

        // return result
        return newParse;
    }

    public static Instruction fillInstructData(ArrayList<String> parsedLine, int instNum) {
        // define new instruction to fill out
        Instruction data = new Instruction();

        data.type = type.get(parsedLine.get(0));
        data.instOrder = instNum;
        // test what kind of instruction we are dealing with
        if (data.type == "R") {
            // example add $t0, $t1, $t2
            // name rd, rs, rt
            // fill out instruction name, opcode and function
            data.instruct = parsedLine.get(0);
            data.opcode = opcode.get(data.instruct);
            data.func = func.get(data.instruct);

            // based on instruction name, decide what values from
            // string array get pased into instruction data
            if ((data.instruct.contains("sll")) | (data.instruct.contains("srl"))) {
                // example sll $t0, $t1, 4
                // name rd, rt, shmat
                data.rd = reg.get(parsedLine.get(1));
                data.rt = reg.get(parsedLine.get(2));
                data.shamt = Integer.parseInt(parsedLine.get(3));
            } else if (data.instruct.contains("jr")) {
                // example jr $t0
                // name rs
                data.rs = data.rs = reg.get(parsedLine.get(1));
            } else {
                // example add $t0, $t1, $t2
                // name rd, rs, rt
                data.rd = reg.get(parsedLine.get(1));
                data.rs = reg.get(parsedLine.get(2));
                data.rt = reg.get(parsedLine.get(3));
            }

        } else if (data.type == "I") {
            // example addi $t0, $t1, 8
            // name rt, rs, immediate
            // fill out instruction name and opcode
            data.instruct = parsedLine.get(0);
            data.opcode = opcode.get(data.instruct);

            if ((data.instruct.contains("beq")) | (data.instruct.contains("bne"))) {
                // example beq $t0, $t1, label
                // name rs, rt, ?
                data.rs = reg.get(parsedLine.get(1));
                data.rt = reg.get(parsedLine.get(2));
                data.immediate = lineLabel.get(parsedLine.get(3)) - (data.instOrder + 1);
                // may need to fill out immediate with label?

            } else if (data.instruct.contains("lw") | data.instruct.contains("sw")) {
                // example lw $t0, 12($s2)
                // name rt, immediate(rs)
                data.rt = reg.get(parsedLine.get(1));
                data.rs = reg.get(parsedLine.get(3));
                data.immediate = Integer.parseInt(parsedLine.get(2));
            } else {
                // example addi $t0, $t1, 8
                // name rt, rs, immediate
                data.rt = reg.get(parsedLine.get(1));
                data.rs = reg.get(parsedLine.get(2));
                data.immediate = Integer.parseInt(parsedLine.get(3));
            }
        } else if (data.type == "J") {
            // example j label
            // name addr
            // fill out instruction name and opcode
            data.instruct = parsedLine.get(0);
            data.opcode = opcode.get(data.instruct);
            // arithmetic
            data.address = lineLabel.get(parsedLine.get(1));

        } else if (!lineLabel.containsKey(parsedLine.get(0))) {

            // invalid instruction
            data.type = "invalid";
            data.instruct = String.format("invalid instruction: %s", parsedLine.get(0));

        }

        return data;
    }

    public static String inst2bin(Instruction inst) {
        String binary = new String();

        if (inst.type == "R") {

            binary = (String.format("%6s", Integer.toBinaryString(inst.opcode)).replace(' ', '0')) + ' ' +
                    (String.format("%5s", Integer.toBinaryString(inst.rs)).replace(' ', '0')) + ' ' +
                    (String.format("%5s", Integer.toBinaryString(inst.rt)).replace(' ', '0')) + ' ' +
                    (String.format("%5s", Integer.toBinaryString(inst.rd)).replace(' ', '0')) + ' ' +
                    (String.format("%5s", Integer.toBinaryString(inst.shamt)).replace(' ', '0')) + ' ' +
                    (String.format("%6s", Integer.toBinaryString(inst.func)).replace(' ', '0'));

        } else if (inst.type == "I") {

            binary = (String.format("%6s", Integer.toBinaryString(inst.opcode)).replace(' ', '0')) + ' ' +
                    (String.format("%5s", Integer.toBinaryString(inst.rs)).replace(' ', '0')) + ' ' +
                    (String.format("%5s", Integer.toBinaryString(inst.rt)).replace(' ', '0')) + ' ' +
                    (String.format("%16s", Integer.toBinaryString(0xFFFF & inst.immediate)).replace(' ', '0'));

        } else if (inst.type == "J") {
            binary = (String.format("%6s", Integer.toBinaryString(inst.opcode)).replace(' ', '0')) + ' ' +
                    (String.format("%26s", Integer.toBinaryString(inst.address)).replace(' ', '0'));
        } else if (inst.type.contains("invalid")) {
            binary = inst.instruct;
        }

        return binary;
    }

    public static void prog2bin() {
        // program will iterate through size of program and
        // generate line by line binary
        for (int i = 0; i < program.size(); i++) {
            bin.add(inst2bin(program.get(i)));
        }
    }

}
