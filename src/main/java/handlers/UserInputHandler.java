package handlers;

import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Predicate;
import lombok.Getter;

/**
 * A class that handles user input in a loop, processes it using a provided logic predicate,
 * and exits gracefully based on specified conditions.
 */
@Getter
public class UserInputHandler {
    private final Predicate<String> logic;
    private final String exitSeq;
    private final String prompt;

    /**
     * Constructs a UserInputHandler.
     *
     * @param logic   A predicate that processes user input. Should return true to continue,
     *                or false to terminate the loop.
     * @param prompt  The hint text displayed to the user when requesting input.
     * @param exitSeq The specific string that, when entered, triggers the exit sequence.
     */
    public UserInputHandler(Predicate<String> logic, String prompt, String exitSeq) {
        this.logic = logic;
        this.exitSeq = exitSeq;
        this.prompt = prompt;
    }

    /**
     * Starts the input handling loop.
     */
    public void run() {
        try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) { // Encoding specified
            while (true) {
                // Display the prompt
                OutputHandler.print(prompt);

                // Check if there's another line of input
                if (!scanner.hasNextLine()) {
                    // Input stream closed, exit gracefully
                    OutputHandler.println("\nNo more input. Exiting.");
                    break;
                }

                // Read user input
                String input = scanner.nextLine();

                // Clear the previous prompt and input using ANSI escape codes
                // \033[F moves the cursor up one line
                // \033[2K clears the entire line
                OutputHandler.print("\033[F\033[2K");

                if (input.equals(exitSeq)) {
                    // If input matches the exit string, print exit message and terminate
                    OutputHandler.println("The program was terminated by the user.");
                    break;
                } else {
                    // Process the input using the logic predicate
                    boolean shouldContinue = logic.test(input);
                    if (!shouldContinue) {
                        // If logic returns false, terminate the loop
                        break;
                    }
                }
            }
        } catch (NoSuchElementException | IllegalStateException e) {
            // Handle the exception gracefully
            OutputHandler.println("\nInput was closed unexpectedly. Exiting.");
        }
    }
}
