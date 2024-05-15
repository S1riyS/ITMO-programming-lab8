package s1riys.lab8.client.commands;

import static s1riys.lab8.client.commands.utils.SignatureHelper.defineSignature;

import s1riys.lab8.client.utils.ValidationHelper;
import s1riys.lab8.client.console.IConsole;
import s1riys.lab8.client.managers.SessionManager;
import s1riys.lab8.client.network.UDPClient;
import s1riys.lab8.common.constants.Commands;
import s1riys.lab8.common.exceptions.APIException;
import s1riys.lab8.common.network.requests.ClearRequest;
import s1riys.lab8.common.network.responses.ClearResponse;

import java.io.IOException;

public class Clear extends ServersideCommand {
    public Clear(IConsole console, UDPClient client) {
        super(defineSignature(Commands.CLEAR), "Очищает коллекцию", console, client);
    }

    @Override
    public Boolean executeImpl(String[] data) {
        try {
            ClearResponse response = (ClearResponse) client.sendAndReceiveCommand(new ClearRequest(SessionManager.getCurrentUser()));
            ValidationHelper.validateResponse(response);

            console.println("Коллекция очищена");
            return true;
        } catch (IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        } catch (APIException e) {
            console.printError(e.getMessage());
        }
        return false;
    }
}
