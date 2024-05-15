package s1riys.lab8.client.commands.utils;

public class SignatureHelper {
    public static String defineSignature(String commandKeyword, String arguments) {
        return "%s %s".formatted(commandKeyword, arguments);
    }

    public static String defineSignature(String commandKeyword) {
        return commandKeyword;
    }
}
