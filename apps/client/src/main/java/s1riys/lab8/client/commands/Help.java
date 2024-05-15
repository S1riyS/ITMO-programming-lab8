package s1riys.lab8.client.commands;

import static s1riys.lab8.client.commands.utils.SignatureHelper.defineSignature;
import s1riys.lab8.client.console.IConsole;
import s1riys.lab8.common.constants.Commands;
import s1riys.lab8.common.managers.CommandManager;

import java.util.StringJoiner;

public class Help extends Command {
    private CommandManager<Command> commandManager;
    public Help(IConsole console, CommandManager<Command> commandManager) {
        super(defineSignature(Commands.HELP), "Выводит справку по доступным командам", console);
        this.commandManager = commandManager;
    }

    @Override
    public Boolean execute(String[] data) {
        StringJoiner joiner = new StringJoiner("\n");

        for (Command command : commandManager.getAll().values()) {
            joiner.add("* %s - %s".formatted(command.getSignature(), command.getDescription()));
        }
        console.println(joiner.toString());
        return true;
    }
}
