package school.mjc.stage0.evennumbers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.nio.file.Files.readAllLines;
import static org.junit.jupiter.api.Assertions.*;

public class EvenNumbersTest {
    private ByteArrayOutputStream outputStream;

    @BeforeAll
    public static void findImports() throws IOException {
        var notAllowedStrings = readAllLines(
                Path.of("src/main/java/school/mjc/stage0/evennumbers/EvenNumbers.java")
        ).stream()
                .filter(line -> line.contains("import")).toList();

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
            Class.forName("school.mjc.stage0.evennumbers.EvenNumbers");
            assertTrue(true);
        } catch (ClassNotFoundException e) {
            fail("No EvenNumbers class found!");
        }
    }

    @Test
    public void hasMainMethod() throws ClassNotFoundException {
        Class<?> clazz = Class.forName("school.mjc.stage0.evennumbers.EvenNumbers");

        try {
            clazz.getDeclaredMethod("main", String[].class);
            assertTrue(true);
        } catch (NoSuchMethodException e) {
            fail("No main method!");
        }
    }

    @Test
    public void hasCorrectOutput() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> clazz = Class.forName("school.mjc.stage0.evennumbers.EvenNumbers");
        Method mainMethod = clazz.getDeclaredMethod("main", String[].class);

        String[] args = new String[]{"987"};
        mainMethod.invoke(clazz, (Object) args);

        StringBuilder sb = new StringBuilder();
        IntStream.range(0, 988)
                .filter(i -> i % 2 == 0)
                .forEach(i -> sb.append(i).append("\r\n"));

        assertEquals(sb.toString(), outputStream.toString(StandardCharsets.UTF_8), "Incorrect output");
    }

    @Test
    public void hasForCycle() throws IOException {
        String result = Files.walk(Paths.get("src/main/java/school/mjc/stage0/evennumbers"))
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".java"))
                .flatMap(this::getFileLines)
                .map(Object::toString)
                .collect(Collectors.joining("\n"));

        Pattern pattern = Pattern.compile("for[ ]*\\(int [a-z][ ]*=[ ]*0;[ ]+[a-z][ ]*<[ ]*[a-zA-Z]+;[ ]*[a-z]\\+\\+\\)[ ]*\\{");
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
