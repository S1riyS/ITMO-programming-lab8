package s1riys.lab8.client.commands;

import s1riys.lab8.client.console.IConsole;
import s1riys.lab8.client.managers.SessionManager;
import s1riys.lab8.client.network.UDPClient;
import s1riys.lab8.common.exceptions.UnauthorizedException;

public abstract class ServersideCommand extends Command {
    protected final UDPClient client;

    public ServersideCommand(String name, String description, IConsole console, UDPClient client) {
        super(name, description, console);
        this.client = client;
    }

    protected void beforeExecuteHook() throws UnauthorizedException {
        if (SessionManager.getCurrentUser() == null) throw new UnauthorizedException();
    }

    protected abstract Boolean executeImpl(String[] data);

    @Override
    public Boolean execute(String[] data) {
        try {
            beforeExecuteHook();
            return executeImpl(data);

        } catch (UnauthorizedException e) {
            console.printError("Для выполнения данной команды войдите в аккаунт (login) или зарегистрируйте новый (register)");
        }
        return false;
    }
}
