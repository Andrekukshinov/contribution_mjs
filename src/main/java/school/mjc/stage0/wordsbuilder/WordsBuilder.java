package school.mjc.stage0.wordsbuilder;

public class WordsBuilder {

    private static String[] WORDS = {
            "Lorem",
            "ipsum",
            "dolor",
            "sit",
            "amet,",
            "consectetur",
            "adipiscing",
            "elit,",
            "sed",
            "do",
            "eiusmod",
            "tempor",
            "incididunt",
            "ut",
            "labore",
            "et",
            "dolore",
            "magna",
            "aliqua"
    };

    public static void main(String[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < WORDS.length; i++) {
            stringBuilder.append(WORDS[i]);

            if (i < WORDS.length - 1) {
                stringBuilder.append(" ");
            }
        }

        System.out.println(stringBuilder);
    }
}
