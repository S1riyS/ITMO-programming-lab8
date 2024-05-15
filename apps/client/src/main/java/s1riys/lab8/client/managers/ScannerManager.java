package s1riys.lab8.client.managers;

import java.util.Scanner;

/**
 * The ScannerManager class provides methods for managing the Scanner object and input modes.
 */
public class ScannerManager {
    private static Scanner scanner;
    private static boolean isFileMode;

    /**
     * Retrieves the input from the user.
     *
     * @return The input provided by the user as a String.
     */
    public static String getInput() {
        return getScanner().nextLine().trim();
    }

    /**
     * Retrieves the Scanner object.
     *
     * @return The Scanner object.
     */
    public static Scanner getScanner() {
        return scanner;
    }

    /**
     * Sets the Scanner object.
     *
     * @param scanner The Scanner object to be set.
     */
    public static void setScanner(Scanner scanner) {
        ScannerManager.scanner = scanner;
    }

    /**
     * Retrieves the input mode.
     *
     * @return true if the input mode is file mode, false if it is interactive mode.
     */
    public static boolean getFileMode() {
        return isFileMode;
    }

    /**
     * Sets the input mode to file mode.
     */
    public static void setScriptMode() {
        ScannerManager.isFileMode = true;
    }

    /**
     * Sets the input mode to interactive mode.
     */
    public static void setInteractiveMode() {
        ScannerManager.isFileMode = false;
    }
}
