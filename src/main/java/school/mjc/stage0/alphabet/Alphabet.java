package school.mjc.stage0.alphabet;

public class Alphabet {
    public static void main(String[] args) {
        char c = 'A';

        while (c <= 'Z') {
            System.out.print(c);
            c++;
        }

        c = 'a';

        while (c <= 'z') {
            System.out.print(c);
            c++;
        }
    }
}
