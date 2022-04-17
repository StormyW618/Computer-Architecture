# Name: Nathan Jaggers, Storm Randolph
# Section: 315 - 01
# Lab 1 - divide.asm
# Description: Takes in a 64-bit integer as two 32-bit 
# 	       integers and a divisor and prints out the
#	       result of the division in two 32-bit registers
# ------------------------------------------------------------
# package Lab1;
#
# import java.util.Scanner;
#
# public class divide {
#
#     public static void main(String[] args) {
#         Scanner input = new Scanner(System.in);
#
#         System.out.println("Enter a high 32 bit number:");
#         int high = input.nextInt();
#
#         System.out.println("Enter a low 32 bit number:");
#         int low = input.nextInt();
#
#         System.out.println("Enter an divisor:");
#         int divisor = input.nextInt();
#
#         input.close();
#
#         int testbit = 1;
#
#         #System.out.println("hi");
#
#         for (int i = 0; i < 100; i++) {
#
#             System.out.println("in loop");
#             System.out.println(i);
#
#             if (divisor == 1) {
#                 break;
#             }
#             # mask LSB in high of 64 bit number
#             testbit = high & 1;
#             #System.out.println(testbit);
#
#             # shift to divide high
#             high >>= 1;
#
#             # shift to divide low
#             low >>>= 1;
#             # insert bit from high
#             testbit <<= 31;
#             #System.out.print(testbit);
#             low = low | testbit;
#             #System.out.print(low);
#
#             divisor >>>= 1;
#
#         }
#
#         System.out.printf("High 32 bit number: %d \n", high);
#
#         System.out.printf("Low 32 bit number: %d \n", low);
#
#     }
#
# }
#
# --------------------------------------------------------------

# declare global so programmer can see actual addresses.
.globl welcome
.globl prompt1
.globl prompt2
.globl prompt3
.globl result1
.globl result2

#  Data Area (this area contains strings to be displayed during the program)
.data

welcome:
	.asciiz " This program takes a 64-bit number as input in the form of two 32-bit numbers and a divisor and prints the result\n\n"

prompt1:
	.asciiz " Enter the high of the 64 bit num: "

prompt2:
	.asciiz " Enter the low of the 64 bit num: "
	
prompt3:
	.asciiz " Enter the divisor: "
	
result1: 
	.asciiz " \n Result High= "
	
result2: 
	.asciiz " \n Result Low= "

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
	ori     $a0, $a0,0x75
	syscall

	# Read 1st integer from the user (5 is loaded into $v0, then a syscall)
	#high of the 64-bit number
	ori     $v0, $0, 5
	syscall

	# Store 1st integer to save value 
	addu    $s0, $v0, $0
	
	#prompt for second value
	ori     $v0, $0, 4
	lui     $a0, 0x1001
	ori     $a0, $a0,0x99
	syscall
	
	# Read 2nd integer from the user
	#low of the 64-bit number
	ori     $v0, $0, 5
	syscall

	# Store 2nd integer to save value 
	addu    $s1, $v0, $0
	
	#prompt for third value
	ori     $v0, $0, 4
	lui     $a0, 0x1001
	ori     $a0, $a0,0xbc
	syscall

	# Read 3rd integer from the user
	#divisor
	ori     $v0, $0, 5
	syscall

	# Store 3rd integer to save value 
	addu    $s2, $v0, $0
	
  	#s0-high
  	#s1-low
  	#s2-divisor
  	#adding 1 to $t0 to compare against divisor
	addi $t0, 1
	#registers for mask and 32 bit shift
	addi $t2, 1
	addi $t5,31
divide: 
	#if divisor is equal to 1 end program, else continue
	beq $t0, $s2, end
	#anding high and 1
	and $t4, $s0, $t2
	#shifting high right 1
	srl $s0, $s0, $t2
	#shifting low right by 1
	srl $s1, $s1, $t2
	#shift testbit 32 bits
	sll $t4,$t4, $t5
	#or testbit and low
	or  $s1, $s1, $t4
	#shift divisor by 1
	srl $s2, $s2, $t2
	#keep going through loop until condition met
	j divide

  end:
    
  	# Display the result text
	ori     $v0, $0, 4			
	lui     $a0, 0x1001
	ori     $a0, $a0,0xd2
	syscall
	
	# Display the high 32
	# load 1 into $v0 to display an integer
	ori     $v0, $0, 1			
	add 	$a0, $s0, $0
	syscall
	
	# Display the result text
	ori     $v0, $0, 4			
	lui     $a0, 0x1001
	ori     $a0, $a0,0xe3
	syscall
	
	# Display the low 32
	# load 1 into $v0 to display an integer
	ori     $v0, $0, 1			
	add 	$a0, $s1, $0
	syscall
	
	# Exit (load 10 into $v0)
	ori     $v0, $0, 10
	syscall
