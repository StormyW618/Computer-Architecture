package Lab2;

public class Instruction {

    /************************************************************
     *members hold relevant data for a mips assebmly instruction
     *not all fields will be used for each instruction, that will
     *be determined based on the type of instruction
     ************************************************************/

    String type; //instruction type (R,I,J)
    String instruct; //name of instruction (e.g add)
    int instOrder; //number holding order of instruction (e.g this is the 4th instruction)
    int opcode; //opcode for instruction
    int rs; //register for instruction
    int rt; //register for instruction
    int rd; //register for instruction
    int shamt; //shift ammount 
    int func; //function to further define operation
    int immediate; //16-bit immediate value
    int address; //26-bit address

    // constructor
    public Instruction() {
        //initialize all to default
        type = "";
        instruct = "";
        instOrder = -1;
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
