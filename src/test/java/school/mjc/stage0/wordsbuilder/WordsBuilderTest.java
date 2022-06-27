package school.mjc.stage0.wordsbuilder;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.Files.readAllLines;
import static org.junit.jupiter.api.Assertions.*;

public class WordsBuilderTest {

    public static final String LINE_DELIMETER = "\r\n";
    private ByteArrayOutputStream outputStream;

    @BeforeAll
    public static void findImports() throws IOException {
        var notAllowedStrings = readAllLines(
                        Path.of("src/main/java/school/mjc/stage0/wordsbuilder/WordsBuilder.java")
                ).stream()
                .filter(line -> line.contains("import"))
                .collect(Collectors.toList());

        assertTrue(notAllowedStrings.isEmpty(), "No imports are allowed");
    }

    @BeforeEach
    public void beforeEach() {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    public void hasWordsBuilderClass() {
        try {
            Class.forName("school.mjc.stage0.wordsbuilder.WordsBuilder");
            assertTrue(true);
        } catch (ClassNotFoundException e) {
            fail("No WordsBuilder class found!");
        }
    }

    @Test
    public void hasMainMethod() throws ClassNotFoundException {
        Class<?> clazz = Class.forName("school.mjc.stage0.wordsbuilder.WordsBuilder");

        try {
            clazz.getDeclaredMethod("main", String[].class);
            assertTrue(true);
        } catch (NoSuchMethodException e) {
            fail("No main method!");
        }
    }

    @Test
    public void hasWordsVariable() throws ClassNotFoundException {
        Class<?> clazz = Class.forName("school.mjc.stage0.wordsbuilder.WordsBuilder");
        try {
            Field field = clazz.getDeclaredField("WORDS");
            System.out.println(field.getType());
            assertTrue(field.getType().isAssignableFrom(String[].class), "WORDS variable is wrong type!");
        } catch (NoSuchFieldException e) {
            fail("No WORDS variable found!");
        }
    }

    @Test
    public void hasCorrectOutput() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Class<?> clazz = Class.forName("school.mjc.stage0.wordsbuilder.WordsBuilder");
        Method mainMethod = clazz.getDeclaredMethod("main", String[].class);

        String[] args = null;
        mainMethod.invoke(clazz, (Object) args);

        Field field = clazz.getDeclaredField("WORDS");
        field.setAccessible(true);
        String[] words = (String[]) field.get(clazz);
        String expected = String.join(" ", words) + LINE_DELIMETER;
        assertEquals(expected, outputStream.toString(StandardCharsets.UTF_8), "Incorrect output");
    }

    @Test
    public void hasForCycle() throws IOException {
        String result = Files.walk(Paths.get("src/main/java/school/mjc/stage0/wordsbuilder"))
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".java"))
                .flatMap(this::getFileLines)
                .map(Object::toString)
                .collect(Collectors.joining("\n"));

        Pattern pattern = Pattern.compile("for[ ]*\\(int [a-z][ ]*=[ ]*0;[ ]+[a-z][ ]*<[ ]*WORDS\\.length;[ ]*[a-z]\\+\\+\\)[ ]*\\{");
        Matcher matcher = pattern.matcher(result);

        assertTrue(matcher.find(), "No for cycle found");
    }

    private Stream<?> getFileLines(Path sourcePath) {
        try {
            return readAllLines(sourcePath, StandardCharsets.UTF_8).stream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Stream.empty();
    }
}
