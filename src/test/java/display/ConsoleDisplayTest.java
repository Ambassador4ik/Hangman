package display;

import handlers.OutputHandler;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ConsoleDisplayTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private PrintStream originalPrintStream;

    @BeforeEach
    public void setUpStreams() throws UnsupportedEncodingException {
        // Save the original OutputHandler printStream
        originalPrintStream = OutputHandler.printStream();
        // Set OutputHandler's printStream to our ByteArrayOutputStream
        OutputHandler.setPrintStream(outContent, true);
    }

    @AfterEach
    public void restoreStreams() {
        // Restore OutputHandler's printStream to the original
        OutputHandler.setPrintStream(originalPrintStream, true);
    }

    @Test
    public void testConstructor() {
        List<Line> initialLines = new ArrayList<>();
        ImmutableLine line1 = new ImmutableLine("Line 1");
        MutableLine<String> line2 = new MutableLine<>(Collections.singletonList("Line 2"));
        initialLines.add(line1);
        initialLines.add(line2);

        ConsoleDisplay consoleDisplay = new ConsoleDisplay(initialLines);

        assertEquals(0, consoleDisplay.shift(), "Shift should be 0 after initialization.");
    }

    @Test
    public void testRender() {
        List<Line> initialLines = new ArrayList<>();
        ImmutableLine line1 = new ImmutableLine("Line 1");
        MutableLine<String> line2 = new MutableLine<>(Collections.singletonList("Line 2"));
        initialLines.add(line1);
        initialLines.add(line2);

        ConsoleDisplay consoleDisplay = new ConsoleDisplay(initialLines);
        consoleDisplay.render();

        String lineSeparator = System.lineSeparator();
        String expectedOutput = "Line 1" + lineSeparator + "Line 2" + lineSeparator;
        assertEquals(expectedOutput, outContent.toString(), "Render should print initial lines.");
    }

    @Test
    public void testOnLineChanged() {
        List<Line> initialLines = new ArrayList<>();
        ImmutableLine line1 = new ImmutableLine("Line 1");
        MutableLine<String> line2 = new MutableLine<>(Collections.singletonList("Line 2"));
        initialLines.add(line1);
        initialLines.add(line2);

        ConsoleDisplay consoleDisplay = new ConsoleDisplay(initialLines);
        consoleDisplay.render();
        outContent.reset();

        // Update the content of line2
        line2.setContent(Collections.singletonList("Updated Line 2"));

        // Since line2 is at index 1, total lines = 2, shift = 0
        int totalLines = 2;
        int index = 1;
        int shift = consoleDisplay.shift();
        int linesBelow = totalLines - index + shift;

        String expectedOutput = String.format("\033[%dA", linesBelow) // Move up
                + "\r\033[K" // Clear line
                + "Updated Line 2" + System.lineSeparator()
                + String.format("\033[%dB", linesBelow - 1); // Move back down

        assertEquals(expectedOutput, outContent.toString(), "onLineChanged should update the line in the console.");
    }

    @Test
    public void testOnLineChangedBeforeRender() {
        List<Line> initialLines = new ArrayList<>();
        MutableLine<String> line1 = new MutableLine<>(Collections.singletonList("Line 1"));
        initialLines.add(line1);

        ConsoleDisplay consoleDisplay = new ConsoleDisplay(initialLines);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            line1.setContent(Collections.singletonList("Updated Line 1"));
        });

        String expectedMessage = "Display has not been rendered yet.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage), "Should throw IllegalStateException if display is not rendered.");
    }

    @Test
    public void testUpdateLineInvalidIndex() {
        List<Line> initialLines = new ArrayList<>();
        ImmutableLine line1 = new ImmutableLine("Line 1");
        initialLines.add(line1);

        ConsoleDisplay consoleDisplay = new ConsoleDisplay(initialLines);
        consoleDisplay.render();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            consoleDisplay.updateLine(5, "Invalid Index");
        });

        String expectedMessage = "Invalid line index.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage), "Should throw IllegalArgumentException for invalid index.");
    }

    @Test
    public void testShiftUpNegative() {
        ConsoleDisplay consoleDisplay = new ConsoleDisplay(Collections.emptyList());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            consoleDisplay.shiftUp(-1);
        });

        String expectedMessage = "Shift lines must be non-negative.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage), "Should throw IllegalArgumentException for negative shiftUp.");
    }

    @Test
    public void testShiftDownNegative() {
        ConsoleDisplay consoleDisplay = new ConsoleDisplay(Collections.emptyList());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            consoleDisplay.shiftDown(-1);
        });

        String expectedMessage = "Shift lines must be non-negative.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage), "Should throw IllegalArgumentException for negative shiftDown.");
    }

    @Test
    public void testShiftUpAndDown() {
        ConsoleDisplay consoleDisplay = new ConsoleDisplay(Collections.emptyList());

        consoleDisplay.shiftUp(3);
        assertEquals(3, consoleDisplay.shift(), "Shift should be increased by shiftUp.");

        consoleDisplay.shiftDown(1);
        assertEquals(2, consoleDisplay.shift(), "Shift should be decreased by shiftDown.");

        consoleDisplay.shiftDown(5);
        assertEquals(0, consoleDisplay.shift(), "Shift should not be negative after shiftDown.");
    }

    @Test
    public void testAddLines() {
        List<Line> initialLines = new ArrayList<>();
        ImmutableLine line1 = new ImmutableLine("Line 1");
        initialLines.add(line1);

        ConsoleDisplay consoleDisplay = new ConsoleDisplay(initialLines);
        consoleDisplay.render();
        outContent.reset();

        List<Line> newLines = new ArrayList<>();
        ImmutableLine line2 = new ImmutableLine("Line 2");
        MutableLine<String> line3 = new MutableLine<>(Collections.singletonList("Line 3"));
        newLines.add(line2);
        newLines.add(line3);

        consoleDisplay.addLines(newLines);

        String expectedOutput = "Line 2" + System.lineSeparator() + "Line 3" + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString(), "addLines should print new lines when rendered.");

        outContent.reset();
        line3.setContent(Collections.singletonList("Updated Line 3"));

        // Now total lines = 3, index of line3 = 2, shift = 0
        int totalLines = 3;
        int index = 2;
        int shift = consoleDisplay.shift();
        int linesBelow = totalLines - index + shift;

        String expectedUpdateOutput = String.format("\033[%dA", linesBelow)
                + "\r\033[K"
                + "Updated Line 3" + System.lineSeparator()
                + String.format("\033[%dB", linesBelow - 1);

        assertEquals(expectedUpdateOutput, outContent.toString(), "Changing line3 should update the display.");
    }

    @Test
    public void testAddLinesNull() {
        ConsoleDisplay consoleDisplay = new ConsoleDisplay(Collections.emptyList());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            consoleDisplay.addLines(null);
        });

        String expectedMessage = "newLines cannot be null.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage), "Should throw IllegalArgumentException when newLines is null.");
    }

    @Test
    public void testUpdateLineBeforeRender() {
        List<Line> initialLines = new ArrayList<>();
        ImmutableLine line1 = new ImmutableLine("Line 1");
        initialLines.add(line1);

        ConsoleDisplay consoleDisplay = new ConsoleDisplay(initialLines);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            consoleDisplay.updateLine(0, "Updated Line 1");
        });

        String expectedMessage = "Display has not been rendered yet.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage), "Should throw IllegalStateException if display is not rendered.");
    }

    @Test
    public void testUpdateLineWithShift() {
        List<Line> initialLines = new ArrayList<>();
        MutableLine<String> line1 = new MutableLine<>(Collections.singletonList("Line 1"));
        initialLines.add(line1);

        ConsoleDisplay consoleDisplay = new ConsoleDisplay(initialLines);
        consoleDisplay.render();

        consoleDisplay.shiftUp(2);
        outContent.reset();

        line1.setContent(Collections.singletonList("Updated Line 1"));

        // total lines = 1, index = 0, shift = 2
        int totalLines = 1;
        int index = 0;
        int shift = consoleDisplay.shift();
        int linesBelow = totalLines - index + shift;

        String expectedOutput = String.format("\033[%dA", linesBelow)
                + "\r\033[K"
                + "Updated Line 1" + System.lineSeparator()
                + String.format("\033[%dB", linesBelow - 1);

        assertEquals(expectedOutput, outContent.toString(), "Updating line with shiftUp should account for the shift.");
    }
}
