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
 *              the Java equivalent to the exponent.asm
 */

package Lab1;

import java.util.Scanner;

public class exponent {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Enter a base:");
        int base = input.nextInt();

        System.out.println("Enter an exponent:");
        int exp = input.nextInt();

        input.close();
        int result, subtotal = 0, inc;

        if (exp == 0)
            result = 1;
        else {
            inc = 1;
            for (int j = 0; j < exp; j++) {
                for (int i = base; i > 0; i--) {
                    subtotal += inc;
                }
                inc = subtotal;
                subtotal = 0;
            }
            result = inc;
        }

        System.out.printf("Result = %d", result);

    }
}
