package school.mjc.stage0.sumdigits;

public class SumDigits {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("No args provided");
            return;
        }

        char[] numbers = args[0].toCharArray();
        int i = 0;
        int sum = 0;
        while (i < numbers.length) {
            sum += Character.getNumericValue(numbers[i]);
            i++;
        }

        System.out.println(sum);
    }
}
