package display;

import handlers.OutputHandler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Getter;

@SuppressWarnings("MultipleStringLiterals")
public class ConsoleDisplay implements LineListener {
    private final List<Line> lines;
    private boolean isRendered;

    @Getter private int shift; // Number of lines shifted

    public ConsoleDisplay(Collection<Line> initialLines) {
        this.lines = new ArrayList<>(initialLines);
        this.shift = 0;
        // Register as a listener to each MutableLine
        for (Line line : lines) {
            if (line instanceof MutableLine) {
                ((MutableLine<?>) line).addListener(this);
            }
        }
        isRendered = false;
    }

    /**
     * Renders the display at the current cursor position.
     */
    public void render() {
        for (Line line : lines) {
            OutputHandler.println(line.getContent());
        }
        isRendered = true;
    }

    @Override
    public void onLineChanged(Line line) {
        if (!isRendered) {
            throw new IllegalStateException("Display has not been rendered yet.");
        }
        int index = lines.indexOf(line);
        if (index == -1) {
            throw new IllegalArgumentException("Line not found in display.");
        }
        updateLine(index, line.getContent());
    }

    /**
     * Updates a specific line in the display, accounting for the current shift.
     *
     * @param index      The index of the line to update.
     * @param newContent The new content for the line.
     */
    public void updateLine(int index, String newContent) {
        if (!isRendered) {
            throw new IllegalStateException("Display has not been rendered yet.");
        }
        if (index < 0 || index >= lines.size()) {
            throw new IllegalArgumentException("Invalid line index.");
        }

        // Calculate the number of lines to move up:
        // lines.size() - index + shift
        int linesBelow = lines.size() - index + shift;

        // Move cursor up to the target line
        OutputHandler.printf("\033[%dA", linesBelow); // Move up

        // Clear the line
        OutputHandler.print("\r\033[K");

        // Print the new content
        OutputHandler.println(newContent);

        // Move the cursor back down to the original position
        OutputHandler.printf("\033[%dB", linesBelow - 1);
    }

    /**
     * Shifts the display up by a specified number of lines.
     * Useful when external output is printed below the display.
     *
     * @param linesToShift The number of lines to shift.
     */
    public void shiftUp(int linesToShift) {
        if (linesToShift < 0) {
            throw new IllegalArgumentException("Shift lines must be non-negative.");
        }
        shift += linesToShift;
    }

    /**
     * Shifts the display down by a specified number of lines.
     * Useful when external output is printed above the display.
     *
     * @param linesToShift The number of lines to shift.
     */
    public void shiftDown(int linesToShift) {
        if (linesToShift < 0) {
            throw new IllegalArgumentException("Shift lines must be non-negative.");
        }
        shift = Math.max(shift - linesToShift, 0);
    }

    /**
     * Adds a collection of lines to the display and hooks up event listeners.
     *
     * @param newLines The list of lines to be added to the display.
     * @throws IllegalStateException If the display has not been rendered yet.
     */
    public void addLines(List<? extends Line> newLines) {
        if (newLines == null) {
            throw new IllegalArgumentException("newLines cannot be null.");
        }

        // Add new lines to the existing list
        for (Line line : newLines) {
            lines.add(line);
            // Register as a listener if the line is mutable
            if (line instanceof MutableLine) {
                ((MutableLine<?>) line).addListener(this);
            }
        }

        if (isRendered) {
            // Append the new lines to the console
            for (Line line : newLines) {
                OutputHandler.println(line.getContent());
            }
        }
    }
}
