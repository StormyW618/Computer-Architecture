# Name: Nathan Jaggers, Storm Randolph
# Section: 315 - 01
# Lab 1 - reverse.asm
# Description: Takes in a 32-bit integer and reverses it
# ------------------------------------------------------------
# public class reverse {
#
#     public static void main(String[] args) {
#
#         long result = 0;
#         int num1 = 5;
#         int testbit = 1;
#         for (int i = 1; i < 32; i++) {
#
#             if ((num1 & testbit) != 0) {
#                 result = result | 1;
#             }
#             result = result << 1;
#             testbit = testbit << 1;
#         }
#         System.out.println(result);
#     }
#
# }
#
# --------------------------------------------------------------

# declare global so programmer can see actual addresses.
.globl welcome
.globl prompt1
.globl prompt2
.globl result

#  Data Area (this area contains strings to be displayed during the program)
.data

welcome:
	.asciiz " This program reverses a 32-bit number \n\n"

prompt1:
	.asciiz " Enter an integer: "

result: 
	.asciiz " \n Result = "

#Text Area (i.e. instructions)
.text

main:

	# Display the welcome message (load 4 into $v0 to display)
	ori     $v0, $0, 4			

	# This generates the starting address for the welcome message.
	# (assumes the register first contains 0).
	lui     $a0, 0x1001
	syscall

	# Display prompt
	ori     $v0, $0, 4			
	
	# This is the starting address of the prompt (notice the
	# different address from the welcome message)
	lui     $a0, 0x1001
	ori     $a0, $a0,0x2A
	syscall

	# Read 1st integer from the user (5 is loaded into $v0, then a syscall)
	ori     $v0, $0, 5
	syscall

	# Clear $s0 for the sum
	ori     $s0, $0, 0	

	# Store 1st integer to save value 
	addu    $s0, $v0, $s0
				
	#initialize result
	addi $s1, $0, 0
	#initialize test bit
	addi $t0, $0, 1
	#initialize for loop counter
	addi $t1, $0, 1
	#initialize shift amount bit
	addi $t2, $0, 1
	
  loop:
	#branch if counter equals 32
	beq $t1, 32, end
	
	#test input and testbit
	and $t4, $s0, $t0
	
	#if match, or result and 1
	beq $t4, $0, shift 
	ori $s1, $s1, 1 
	
  shift:	
	#left shift result and testbit 1 bit 
	sll $s1, $s1, $t2
	sll $t0, $t0, $t2
	#inc for loop counter and jump to loop
	addi $t1, $t1, 1
	j loop
  end:
    
  # Display the result text
	ori     $v0, $0, 4			
	lui     $a0, 0x1001
	ori     $a0, $a0,0x40
	syscall
	
	# Display the remainder
	# load 1 into $v0 to display an integer
	ori     $v0, $0, 1			
	add 	$a0, $s1, $0
	syscall
	
	# Exit (load 10 into $v0)
	ori     $v0, $0, 10
	syscall
