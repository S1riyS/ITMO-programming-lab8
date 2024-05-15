package s1riys.lab8.client.managers;

import s1riys.lab8.client.commands.*;
import s1riys.lab8.client.console.IConsole;
import s1riys.lab8.client.network.UDPClient;
import s1riys.lab8.common.exceptions.ScriptsRecursionException;
import s1riys.lab8.common.managers.CommandManager;
import s1riys.lab8.common.constants.Commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class ScriptManager {
    public enum ExitCode {
        OK,
        ERROR,
        EXIT,
    }

    private final UDPClient client;
    IConsole console;
    private final CommandManager<Command> clientCommandManager;
    private final List<String> scriptStack = new ArrayList<>();

    public ScriptManager(UDPClient client, IConsole console) {
        this.client = client;
        this.console = console;

        this.clientCommandManager = new CommandManager<>() {{
            register(Commands.HELP, new Help(console, this));
            register(Commands.INFO, new Info(console, client));
            register(Commands.SHOW, new Show(console, client));
            register(Commands.INSERT, new Insert(console, client));
            register(Commands.UPDATE, new Update(console, client));
            register(Commands.REMOVE_KEY, new RemoveKey(console, client));
            register(Commands.CLEAR, new Clear(console, client));
            register(Commands.EXECUTE_SCRIPT, new ExecuteScript(console));
            register(Commands.EXIT, new Exit(console));
            register(Commands.REMOVE_GREATER, new RemoveGreater(console, client));
            register(Commands.REMOVE_LOWER, new RemoveLower(console, client));
            register(Commands.REMOVE_GREATER_KEY, new RemoveGreaterKey(console, client));
            register(Commands.MAX_BY_CREATION_DATE, new MaxByCreationDate(console, client));
        }};
    }

    private String[] parseCommand(String input) {
        input += " ";
        String commandKeyword = input.split(" ", 2)[0];
        String commandArgs = input.split(" ", 2)[1].trim();
        return new String[]{commandKeyword, commandArgs};
    }

    /**
     * Handles exit
     */
    private void handleExit() {
        console.println("Работа завершена");
        System.exit(0);
    }

    /**
     * Starts script mode
     *
     * @param filename Name of file with script to execute
     */
    public ExitCode run(String filename) {
        scriptStack.add(filename);

        try (Scanner scriptScanner = new Scanner(new File(filename), StandardCharsets.UTF_8)) {
            ExitCode exitCode;
            if (!scriptScanner.hasNext()) throw new NoSuchElementException();

            Scanner previousScanner = ScannerManager.getScanner();
            ScannerManager.setScanner(scriptScanner);
            ScannerManager.setScriptMode();

            do {
                String input = ScannerManager.getInput();
                String commandKeyword = parseCommand(input)[0];
                String[] commandArgs = parseCommand(input)[1].split(" ");

                console.ps1();
                console.println(input);

                if (commandKeyword.equals("execute_script")) {
                    for (String scriptName : scriptStack) {
                        if (scriptName.equals(commandArgs[0])) throw new ScriptsRecursionException(scriptName);
                    }
                }

                exitCode = launch(commandKeyword, commandArgs);
            } while (exitCode == ExitCode.OK && scriptScanner.hasNextLine());

            ScannerManager.setScanner(previousScanner);
            return exitCode;

        } catch (FileNotFoundException exception) {
            console.printError("Файл со скриптом не найден!");
        } catch (NoSuchElementException exception) {
            console.printError("Непредвиденный конец файла");
        } catch (ScriptsRecursionException exception) {
            StringJoiner stringJoiner = new StringJoiner(" -> ");
            for (String scriptName : scriptStack) {
                stringJoiner.add(scriptName);
            }
            stringJoiner.add(exception.recursionCause);
            console.printError("Вызванный скрипт порождает рекурсию! (" + stringJoiner + ")");
        } catch (IOException e) {
            console.printError("Ошибка чтения");
        } finally {
            scriptStack.remove(scriptStack.size() - 1);
        }
        return ExitCode.ERROR;
    }

    /**
     * Launches command
     *
     * @param keyword Keyword of command
     * @param args    Arguments of command
     */
    private ExitCode launch(String keyword, String[] args) {
        if (keyword.isEmpty()) return ExitCode.OK;

        Command command = clientCommandManager.getOne(keyword);
        if (command == null) {
            console.printError("Команда '" + keyword + "' не найдена (используйте 'help' для справки)");
            return ExitCode.ERROR;
        }

        switch (keyword) {
            case Commands.EXIT -> {
                command.execute(args);
                return ExitCode.EXIT;
            }
            case Commands.EXECUTE_SCRIPT -> {
                if (!command.execute(args)) return ExitCode.ERROR;
                return run(args[0]);
            }
            default -> {
                if (!command.execute(args)) return ExitCode.ERROR;
            }
        }
        return ExitCode.OK;
    }
}
