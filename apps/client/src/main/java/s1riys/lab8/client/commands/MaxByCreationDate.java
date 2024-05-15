package s1riys.lab8.client.commands;

import s1riys.lab8.client.utils.ValidationHelper;
import s1riys.lab8.client.console.IConsole;
import s1riys.lab8.client.managers.SessionManager;
import s1riys.lab8.client.network.UDPClient;
import s1riys.lab8.common.constants.Commands;
import s1riys.lab8.common.exceptions.APIException;
import s1riys.lab8.common.network.requests.MaxByCreationDateRequest;
import s1riys.lab8.common.network.responses.MaxByCreationDateResponse;

import java.io.IOException;

import static s1riys.lab8.client.commands.utils.SignatureHelper.defineSignature;

public class MaxByCreationDate extends ServersideCommand {
    public MaxByCreationDate(IConsole console, UDPClient client) {
        super(
                defineSignature(Commands.MAX_BY_CREATION_DATE),
                "Выводит объект из коллекции, значение поля creationDate которого является максимальным",
                console,
                client
        );
    }

    @Override
    public Boolean executeImpl(String[] data) {
        try {
            MaxByCreationDateRequest request = new MaxByCreationDateRequest(SessionManager.getCurrentUser());
            MaxByCreationDateResponse response = (MaxByCreationDateResponse) client.sendAndReceiveCommand(request);
            ValidationHelper.validateResponse(response);
            console.println(response.product);

            return true;
        } catch (IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        } catch (APIException e) {
            console.printError(e.getMessage());
        }
        return false;
    }
}
