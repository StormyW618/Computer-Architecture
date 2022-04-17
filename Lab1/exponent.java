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
