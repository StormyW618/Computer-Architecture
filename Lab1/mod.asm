# Name: Nathan Jaggers, Storm Randolph
# Section: 315 - 01
# Lab 1 - Mod.asm
# Description: Takes in an integer and power of two and prints 
#	       the result of the modulus between the two
# ------------------------------------------------------------
# package Lab1;
#
# import java.util.Scanner;
#
# public class mod {
#    
#    public static void main(String []args)
#    {
#        Scanner input = new Scanner(System.in);
#
#        System.out.println("Enter an numerator:");
#       int num = input.nextInt();
#
#        System.out.println("Enter an denominator (Power of 2):");
#        int denom = input.nextInt();
#
#        int remainder = num & (denom - 1);
#
#        System.out.printf("The remainder is %d",remainder);
#
#    }
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
	.asciiz " This program mods two numbers \n\n"

prompt1:
	.asciiz " Enter an integer: "

prompt2:
	.asciiz " Enter a power of two integer: "

result: 
	.asciiz " \n Remainder = "

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
	ori     $a0, $a0,0x22
	syscall

	# Read 1st integer from the user (5 is loaded into $v0, then a syscall)
	ori     $v0, $0, 5
	syscall

	# Clear $s0 for the sum
	ori     $s0, $0, 0	

	# Store 1st integer to save value 
	# (could have put 1st integer into $s0 and skipped clearing it above)
	addu    $s0, $v0, $s0
	
	# Display prompt (4 is loaded into $v0 to display)
	ori     $v0, $0, 4			
	lui     $a0, 0x1001
	ori     $a0, $a0,0x36
	syscall

	# Read 2nd integer 
	ori	$v0, $0, 5			
	syscall
	# $v0 now has the value of the second integer
	
	# Store 2nd integer to temporary 
	addu    $t0, $v0, $t0
	#add integer 1 to temporary
		addi $t1,$t1, 1
	#subtract one from 2nd integer
		sub $t2, $t0,$t1
	#and numerator and divisor
		and $s0, $s0,$t2
    
  	# Display the remainder text
	ori     $v0, $0, 4			
	lui     $a0, 0x1001
	ori     $a0, $a0,0x58
	syscall
	
	# Display the remainder
	# load 1 into $v0 to display an integer
	ori     $v0, $0, 1			
	add 	$a0, $s0, $0
	syscall
	
	# Exit (load 10 into $v0)
	ori     $v0, $0, 10
	syscall
