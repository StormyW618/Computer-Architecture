# Name: Nathan Jaggers, Storm Randolph
# Section: 315 - 01
# Lab 1 - exponent.asm
# Description: Takes in a base and power and prints 
#	       the result of the exponential of the two
# ------------------------------------------------------------
# package Lab1;
#
# import java.util.Scanner;
#
# public class exponent {
#     public static void main(String[] args) {
#         Scanner input = new Scanner(System.in);
#
#         System.out.println("Enter a base:");
#         int base = input.nextInt();
#
#         System.out.println("Enter an exponent:");
#         int exp = input.nextInt();
#
#         int result, subtotal = 0, inc;
#
#         if (exp == 0)
#             result = 1;
#         else {
#             inc = 1;
#             for (int j = 0; j < exp; j++) {
#                 for (int i = base; i > 0; i--) {
#                     subtotal += inc;
#                 }
#                 inc = subtotal;
#                 subtotal = 0;
#             }
#             result = inc;
#         }
#
#         System.out.printf("Result = %d", result);
#
#     }
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
	.asciiz " This program exponentiates two numbers \n\n"

prompt1:
	.asciiz " Enter an base: "

prompt2:
	.asciiz " Enter a power: "

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
	ori     $a0, $a0,0x2b
	syscall

	# Read 1st integer from the user (5 is loaded into $v0, then a syscall)
	ori     $v0, $0, 5
	syscall

	# Store 1st integer to save value 
	addi    $s0, $v0, 0
	
	# Display prompt (4 is loaded into $v0 to display)
	ori     $v0, $0, 4			
	lui     $a0, 0x1001
	ori     $a0, $a0,0x3c
	syscall

	# Read 2nd integer 
	ori	$v0, $0, 5			
	syscall
	# $v0 now has the value of the second integer
	
	# Store power integer to temporary 
	addu    $t0, $v0, $t0
	
	#test if x^0, if so branch
	beq $t0, $0, one
	
	#initialize inc with 1
	#and subtotal with zero
	addi $t1, $0, 1
	addi $t4, $0, 0
	
	#initilize power for loop counter with 0
	addi $t2, $0, 0
	
	#stay in loop until counter is equal to the power
  power:
	beq $t0, $t2, save
	
	#initialize mult for loop counter to base value
	add $t3, $0, $s0
	
	#stay in loop until counter is equal to the zero
  multip:
	beq $t3, $0, multDone
	#add inc to subtotal and decrement counter
	add $t4, $t4, $t1
	addi $t3, $t3, -1
	j multip
	
  multDone:
  	#update inc with subtotal
  	addi $t1, $t4, 0
  	#reinitialize subtotal to zero
  	addi $t4, $0, 0
	#increment power counter and jump to loop
	addi $t2, $t2, 1
  	j power
  
  save:
  	#put final inc value into result
  	addi $s0, $t1, 0
  	j end
  one:
  	#if exp = 0, result = 1
        addi $s0, $0, 1
      
  end:
   
  # Display the result text
	ori     $v0, $0, 4			
	lui     $a0, 0x1001
	ori     $a0, $a0,0x4f
	syscall
	
	# Display the result
	# load 1 into $v0 to display an integer
	ori     $v0, $0, 1			
	add 	$a0, $s0, $0
	syscall
	
	# Exit (load 10 into $v0)
	ori     $v0, $0, 10
	syscall
