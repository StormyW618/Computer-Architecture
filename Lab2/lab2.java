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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import Lab2.mipsAssembler;

public class lab2 {

    public static void main(String[] args) {
        System.out.println("Hello World"); // prints Hello World
        //secondpass("./Lab2/test2.asm");
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

        //define mips object
        //mipsAssembler test = new mipsAssembler(args[0]);
        mipsAssembler test = new mipsAssembler("Lab2/test1.asm");
        //processing and parsing handled by the class

        // //print out binary
        // test.printBinary();
    }
