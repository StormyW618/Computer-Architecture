//Nathan Jaggers
//Storm Randolf

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
    public static ArrayList<Instruction> program = new ArrayList<>();
    public static ArrayList<Integer> bin = new ArrayList<>();

    public static class Instruction {

        String type;
        String instruct;
        int linenum;
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

    }

    public static void main(String[] args) {
        // System.out.println("Hello World"); // prints Hello World

        // init hash tables
        init_type(type);
        init_opMap(opcode);
        init_func(func);
        init_reg(reg);

        // read in file, get rid of white spaces and comments
        // parse data and fill list of instructions with data
        ArrayList<String> lines;
        //lines = firstpass(args[0]);
        lines = firstpass("./Lab2/test4.asm");
        secondpass(lines);
        thirdpass(lines);
        // System.out.println("test successful");

        // short num = 10;
        // int num2 = -1;
        // System.out.println(Integer.toBinaryString(num));
        // System.out.println(Integer.toBinaryString(num2));
        // System.out.println(Integer.bitCount(num));

        // // start conversion to binary
        for (int i = 0; i < program.size(); i++) {
            // bin.add(inst2bin(program.get(i)));
            inst2bin2(program.get(i));
            System.out.println();
            if(program.get(i).instruct.contains("invalid")){
                break;
            }
        }

        // //print conversion
        // for (int i = 0; i < bin.size(); i++) {
        // System.out.println((bin.get(i)).toBinaryString(i));
        // }

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
                // removes whitespace from votes
                // line = filter(line);

                line = line.replace("\n", "");
                line = line.replace("\r", "");
                line = line.replace("\t", "");
                line = line.replace("#", "");
                if (!line.contains("j")) {
                    line = line.replace(" ", "");
                }

                // adding filtered line to list
                if (!(line == "")) {
                    lines.add(line);
                }

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
    
    // will read file line by line
    // passes line to line filtering function
    public static void thirdpass(ArrayList lines) {
        String test[];
        String test2[];
        int linenumber = 0;
        ArrayList<String> list2 = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = (String) lines.get(i);
            if (line.contains(":") && !line.contains("j") && !line.contains("$")) {
                line = line.trim();
                line = line.replace(":", " ");
                test = line.split(" ");
                list2.add(test[0]);
            }
            // if label and j/jal instruction are on the same line
            // replaces colon with whitespaces and splits into list
            else if (line.contains(":") && !line.contains("$")) {
                line = line.trim();
                line = line.replace(":", " ");
                test = line.split(" ");
                list2.add(test[1]);
                list2.add(test[2]);
            }
            // if label and any other instruction are on the same line
            else if (line.contains(":") && line.contains("$")) {
                line = filter(line);
                line = line.replace(":", " ");
                line = line.replace("$", " $");
                test = line.split(" ");
                list2.add(test[1]);
                list2.add(test[2]);
                list2.add(test[3]);
                list2.add(test[4]);
            }
            // if line doesn't contain label but is j/jal instruction
            else if (!line.contains("$") && line != "") {
                line = line.trim();
                test = line.split(" ");
                list2.add(test[0]);
                list2.add(test[1]);
            }
            // lw or sw instruction
            else if (line.contains("(")) {
                // split instruction off main string
                test = line.split("\\$", 2);
                // add instruction to list
                list2.add(test[0]);
                // replace $ that was split off
                test[1] = "$" + test[1];
                // split current string on comma
                test2 = test[1].split(",");
                // add first register to list
                list2.add(test2[0]);
                // separate immediate and add to list
                test2 = test2[1].split("\\(");
                list2.add(test2[0]);
                // remove closing parenthese and add second reg to list
                test2[1] = test2[1].replace(")", "");
                list2.add(test2[1]);
            }
            // if line contains any other instruction other than j/jal
            else {
                // remove whitespace from line
                // line = filter(line);
                // strip whitespace
                // add$t0,$t1,$1
                // line = line.split("$",2)
                // [add, t0,$t1,$t1]
                //
                // line = line.replaceAll("\\s", "");
                // line = line.replace("$", " $");
                // line = line.replace(",", "");
                test = line.split("\\$", 2);
                list2.add(test[0]);
                test[1] = "$" + test[1];
                test2 = test[1].split(",");
                for (int j = 0; j < test2.length; j++) {
                    list2.add(test2[j]);
                }

            }

            // call function to take parsed data and fill out instruction fields
            // then add to list of instructions
            Instruction lineData = fillInstructData(list2, linenumber);
            if (lineData.instruct != "") {
                program.add(lineData);
                linenumber++;
            }
            list2.clear();

        }
        // j test1
        // check if present in hashtable, if it is go to that line + 1(label handling)
        // check other hashmap for instruction, registers, and binary conversion
    }

    // finds line or lines that contain labels
    // will have these two numbers into hashmap upon ...
    // should this just parse label? or test label and parse if a label?
    public static void label(String line, int linenum) {
        if (line.contains(":")) {
            String[] temp = line.split(":");
            // System.out.println(line);
            // System.out.println(linenum);
            lineLabel.put(temp[0], linenum);
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

    public static Instruction fillInstructData(ArrayList<String> parsedLine, int instNum) {
        // define new instruction to fill out
        Instruction data = new Instruction();

        data.type = type.get(parsedLine.get(0));
        data.linenum = instNum;
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
                data.immediate = lineLabel.get(parsedLine.get(3)) - (data.linenum + 1);
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

        }
        else if (!lineLabel.containsKey(parsedLine.get(0))){

            //invalid instruction
            data.type = "invalid";
            data.instruct = String.format("invalid instruction: %s",parsedLine.get(0));
        
        }

        return data;
    }

    public static int inst2bin(Instruction inst) {
        int binary = 0;

        if (inst.type == "R") {
            // type R instruction
            binary |= ((inst.opcode & 63) << 26); // 31-26
            binary |= ((inst.rs & 31) << 21); // 25-21
            binary |= ((inst.rt & 31) << 16); // 20-16
            binary |= ((inst.rd & 31) << 11); // 15-11
            binary |= ((inst.shamt & 31) << 6); // 10-6
            binary |= ((inst.func & 63) << 0); // 5-0
        } else if (inst.type == "I") {
            // type I instruction
            binary |= ((inst.opcode & 63) << 26); // 31-26
            binary |= ((inst.rs & 31) << 21); // 25-21
            binary |= ((inst.rt & 31) << 16); // 20-16
            binary |= ((inst.immediate & 0xFFFF) << 0); // 15-0
        } else if (inst.type == "J") {
            // type J instruction
            binary |= ((inst.opcode & 63) << 26); // 31-26
            binary |= ((inst.address & 31) << 0); // 25-0

        }
        else if (inst.type.contains("invalid")){
            System.out.print(inst.instruct);
        }

        return binary;
    }

    public static void inst2bin2(Instruction inst) {

        if (inst.type == "R") {
            // type R instruction
            // binary |= ((inst.opcode & 63) << 26); // 31-26
            // binary |= ((inst.rs & 31) << 21); // 25-21
            // binary |= ((inst.rt & 31) << 16); // 20-16
            // binary |= ((inst.rd & 31) << 11); // 15-11
            // binary |= ((inst.shamt & 31) << 6); // 10-6
            // binary |= ((inst.func & 63) << 0); // 5-0
            System.out.print((String.format("%6s", Integer.toBinaryString(inst.opcode)).replace(' ', '0')));
            System.out.print(" ");
            System.out.print((String.format("%5s", Integer.toBinaryString(inst.rs)).replace(' ', '0')));
            System.out.print(" ");
            System.out.print((String.format("%5s", Integer.toBinaryString(inst.rt)).replace(' ', '0')));
            System.out.print(" ");
            System.out.print((String.format("%5s", Integer.toBinaryString(inst.rd)).replace(' ', '0')));
            System.out.print(" ");
            System.out.print((String.format("%5s", Integer.toBinaryString(inst.shamt)).replace(' ', '0')));
            System.out.print(" ");
            System.out.print((String.format("%6s", Integer.toBinaryString(inst.func)).replace(' ', '0')));

        } else if (inst.type == "I") {
            // type I instruction
            // binary |= ((inst.opcode & 63) << 26); // 31-26
            // binary |= ((inst.rs & 31) << 21); // 25-21
            // binary |= ((inst.rt & 31) << 16); // 20-16
            // binary |= ((inst.immediate & 0xFFFF) << 0); // 15-0

            System.out.print((String.format("%6s", Integer.toBinaryString(inst.opcode)).replace(' ', '0')));
            System.out.print(" ");
            System.out.print((String.format("%5s", Integer.toBinaryString(inst.rs)).replace(' ', '0')));
            System.out.print(" ");
            System.out.print((String.format("%5s", Integer.toBinaryString(inst.rt)).replace(' ', '0')));
            System.out.print(" ");
            System.out
                    .print((String.format("%16s", Integer.toBinaryString(0xFFFF & inst.immediate)).replace(' ', '0')));

        } else if (inst.type == "J") {
            // type J instruction
            // binary |= ((inst.opcode & 63) << 26); // 31-26
            // binary |= ((inst.address & 31) << 0); // 25-0

            System.out.print((String.format("%6s", Integer.toBinaryString(inst.opcode)).replace(' ', '0')));
            System.out.print(" ");
            System.out.print((String.format("%26s", Integer.toBinaryString(inst.address)).replace(' ', '0')));
        }
        else if (inst.type.contains("invalid")){
            System.out.print(inst.instruct);
        }

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
