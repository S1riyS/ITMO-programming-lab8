package s1riys.lab8.server.handlers;

import s1riys.lab8.common.managers.CommandManager;
import s1riys.lab8.common.network.requests.Request;
import s1riys.lab8.common.network.responses.NoSuchCommandResponse;
import s1riys.lab8.common.network.responses.Response;
import s1riys.lab8.server.commands.Command;

public class CommandHandler {
    private CommandManager <Command> commandManager;

    public CommandHandler(CommandManager<Command> commandManager) {
        this.commandManager = commandManager;
    }

    public Response handle(Request request) {
        Command command = commandManager.getOne(request.getName());
        if (command == null) return new NoSuchCommandResponse(request.getName());
        return command.execute(request);
    }

}
