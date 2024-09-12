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
 *              the Java equivalent to the divide.asm
 */

package Lab1;

import java.util.Scanner;

public class divide {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Enter a high 32 bit number:");
        int high = input.nextInt();

        System.out.println("Enter a low 32 bit number:");
        int low = input.nextInt();

        System.out.println("Enter an divisor:");
        int divisor = input.nextInt();

        input.close();

        int testbit = 1;

        for (int i = divisor; i != 1; i>>=1) {

            // mask LSB in high of 64 bit number
            testbit = high & 1;

            // shift to divide high
            high >>= 1;

            // shift to divide low
            low >>>= 1;

            // insert bit from high
            testbit <<= 31;

            low = low | testbit;

        }

        System.out.printf("High 32 bit number: %d \n", high);

        System.out.printf("Low 32 bit number: %d \n", low);

    }

}
