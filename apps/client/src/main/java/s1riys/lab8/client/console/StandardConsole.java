package s1riys.lab8.client.console;

/**
 * The StandardConsole class implements the IConsole interface and provides basic console input/output operations.
 */
public class StandardConsole implements IConsole {
    private static final String PS1 = "$ ";
    private static final String PS2 = "> ";

    /**
     * Prints the specified object.
     *
     * @param obj The object to be printed.
     */
    @Override
    public void print(Object obj) {
        System.out.print(obj);
    }

    /**
     * Prints the specified object followed by a new line.
     *
     * @param obj The object to be printed.
     */
    @Override
    public void println(Object obj) {
        System.out.println(obj);
    }

    /**
     * Prints the specified object as an error message.
     *
     * @param obj The object to be printed as an error message.
     */
    @Override
    public void printError(Object obj) {
        System.out.println(ConsoleColors.RED + "ERROR" + ConsoleColors.RESET + " - " + obj);
    }

    /**
     * Prints the primary prompt.
     */
    @Override
    public void ps1() {
        print(PS1);
    }

    /**
     * Prints the secondary prompt.
     */
    @Override
    public void ps2() {
        print(PS2);
    }
}
