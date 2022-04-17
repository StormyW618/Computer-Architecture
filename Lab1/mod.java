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
