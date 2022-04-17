package Lab1;

public class reverse {

    public static void main(String[] args) {

        int result = 0;
        int num1 = 4;
        int testbit = 1;
        // System.out.println(num1 & testbit);
        for (int i = 1; i < 32; i++) {

            if ((num1 & testbit) != 0) {
                result = result | 1;
            }
            result = result << 1;
            testbit = testbit << 1;
        }
        System.out.println(result);
    }

}
