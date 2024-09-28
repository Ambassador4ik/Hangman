package handlers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for UserInputHandler.
 */
@Getter
@Accessors(fluent = true)
public class UserInputHandlerTest {

    /**
     * Utility method to remove ANSI escape codes from a string.
     *
     * @param text the text from which to remove ANSI codes
     * @return the cleaned text
     */
    private String removeAnsiCodes(String text) {
        return text.replaceAll("\u001B\\[[;\\d]*[ -/]*[@-~]", "");
    }

    /**
     * Tests that the handler exits when the exit sequence is entered.
     */
    @Test
    public void testUserInputHandlerExitsOnExitSeq() {
        // Arrange
        String exitSeq = "exit";
        String prompt = "Enter input:";
        Predicate<String> logic = input -> true;

        String simulatedUserInput = exitSeq + "\n";
        ByteArrayInputStream testIn = new ByteArrayInputStream(
                simulatedUserInput.getBytes(StandardCharsets.UTF_8));

        ByteArrayOutputStream testOut = new ByteArrayOutputStream();

        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        try {
            System.setIn(testIn);

            // Redirect OutputHandler's output
            PrintStream testPrintStream = new PrintStream(testOut);
            OutputHandler.printStream(testPrintStream);

            UserInputHandler handler = new UserInputHandler(logic, prompt, exitSeq);
            handler.run();

            // Assert
            String output = testOut.toString();
            String outputClean = removeAnsiCodes(output);
            assertTrue(outputClean.contains("The program was terminated by the user."));
        } finally {
            System.setIn(originalIn);
            OutputHandler.printStream(originalOut);
        }
    }

    /**
     * Tests that the handler processes input using the logic predicate and exits
     * when logic returns false.
     */
    @Test
    public void testUserInputHandlerLogicPredicateFalse() {
        // Arrange
        String exitSeq = "exit";
        String prompt = "Enter input:";
        Predicate<String> logic = input -> false;

        String simulatedUserInput = "some input\n";
        ByteArrayInputStream testIn = new ByteArrayInputStream(
                simulatedUserInput.getBytes(StandardCharsets.UTF_8));

        ByteArrayOutputStream testOut = new ByteArrayOutputStream();

        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        try {
            System.setIn(testIn);
            PrintStream testPrintStream = new PrintStream(testOut);
            OutputHandler.printStream(testPrintStream);

            UserInputHandler handler = new UserInputHandler(logic, prompt, exitSeq);
            handler.run();

            // Assert
            String output = testOut.toString();
            String outputClean = removeAnsiCodes(output);
            assertTrue(outputClean.contains(prompt));
        } finally {
            System.setIn(originalIn);
            OutputHandler.printStream(originalOut);
        }
    }

    /**
     * Tests that the logic predicate receives the correct inputs.
     */
    @Test
    public void testUserInputHandlerLogicPredicateReceivesInput() {
        // Arrange
        String exitSeq = "exit";
        String prompt = "Enter input:";
        List<String> inputsReceived = new ArrayList<>();

        Predicate<String> logic = input -> {
            inputsReceived.add(input);
            return !input.equals("stop");
        };

        String simulatedUserInput = "first input\nsecond input\nstop\n";
        ByteArrayInputStream testIn = new ByteArrayInputStream(
                simulatedUserInput.getBytes(StandardCharsets.UTF_8));

        ByteArrayOutputStream testOut = new ByteArrayOutputStream();

        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        try {
            System.setIn(testIn);
            PrintStream testPrintStream = new PrintStream(testOut);
            OutputHandler.printStream(testPrintStream);

            UserInputHandler handler = new UserInputHandler(logic, prompt, exitSeq);
            handler.run();

            // Assert
            assertEquals(3, inputsReceived.size());
            assertEquals("first input", inputsReceived.get(0));
            assertEquals("second input", inputsReceived.get(1));
            assertEquals("stop", inputsReceived.get(2));
        } finally {
            System.setIn(originalIn);
            OutputHandler.printStream(originalOut);
        }
    }

    /**
     * Tests that the handler's fields are correctly set and accessible via getters.
     */
    @Test
    public void testUserInputHandlerFields() {
        // Arrange
        String exitSeq = "exit";
        String prompt = "Enter input:";
        Predicate<String> logic = input -> true;

        // Act
        UserInputHandler handler = new UserInputHandler(logic, prompt, exitSeq);

        // Assert
        assertEquals(prompt, handler.prompt());
        assertEquals(exitSeq, handler.exitSeq());
        assertSame(logic, handler.logic());
    }
}
