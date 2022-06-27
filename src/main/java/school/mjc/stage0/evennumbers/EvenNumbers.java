package school.mjc.stage0.evennumbers;

public class EvenNumbers {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("No args provided");
            return;
        }

        int maxNumber = Integer.parseInt(args[0]);

        for (int i = 0; i < maxNumber; i++) {
            if (i % 2 == 0) {
                System.out.println(i);
            }
        }
    }
}
