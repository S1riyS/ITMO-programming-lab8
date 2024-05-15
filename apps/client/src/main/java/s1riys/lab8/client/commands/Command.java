package s1riys.lab8.client.commands;

import s1riys.lab8.common.interfaces.Executable;
import s1riys.lab8.client.console.IConsole;

public abstract class Command implements Executable<String[], Boolean> {
    private final String signature;
    private final String description;
    protected final IConsole console;

    public Command(String signature, String description, IConsole console) {
        this.signature = signature;
        this.description = description;
        this.console = console;
    }

    public String getSignature() {
        return signature;
    }

    public String getDescription() {
        return description;
    }

    public abstract Boolean execute(String[] data);
}