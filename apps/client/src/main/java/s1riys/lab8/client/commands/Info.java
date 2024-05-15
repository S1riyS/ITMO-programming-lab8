package s1riys.lab8.client.commands;

import static s1riys.lab8.client.commands.utils.SignatureHelper.defineSignature;
import s1riys.lab8.client.console.IConsole;
import s1riys.lab8.client.managers.SessionManager;
import s1riys.lab8.client.network.UDPClient;
import s1riys.lab8.common.constants.Commands;
import s1riys.lab8.common.network.requests.InfoRequest;
import s1riys.lab8.common.network.responses.InfoResponse;

import java.io.IOException;

public class Info extends ServersideCommand {
    public Info(IConsole console, UDPClient client) {
        super(
                defineSignature(Commands.INFO),
                "Выводит в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)",
                console,
                client
        );
    }

    @Override
    public Boolean executeImpl(String[] data) {
        try {
            InfoResponse response = (InfoResponse) client.sendAndReceiveCommand(new InfoRequest(SessionManager.getCurrentUser()));
            console.println(response.infoMessage);
            return true;
        } catch (IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        }
        return false;
    }
}
