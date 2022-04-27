/**
 * Programmers: Nathan Jaggers, Storm Randolph
 * 
 * CPE 315 - Computer Architecture
 * Dr.Seng
 * Spring 2022
 * 
 * Lab 1 - Programing with MIPS
 * 
 * Description: Series of small programs that 
 *              allow for the practice of programming
 *              in the MIPS assembly language. Here is 
 *              the Java equivalent to the mod.asm
 */

package Lab1;

import java.util.Scanner;

public class mod {
    
    public static void main(String []args)
    {
        Scanner input = new Scanner(System.in);

        System.out.println("Enter an numerator:");
        int num = input.nextInt();

        System.out.println("Enter an denominator (Power of 2):");
        int denom = input.nextInt();

        input.close();
        int remainder = num & (denom - 1);

        System.out.printf("The remainder is %d",remainder);

    }
}
