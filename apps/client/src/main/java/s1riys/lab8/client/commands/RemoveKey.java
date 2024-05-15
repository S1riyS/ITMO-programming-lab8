package s1riys.lab8.client.commands;

import static s1riys.lab8.client.commands.utils.SignatureHelper.defineSignature;

import s1riys.lab8.client.utils.ValidationHelper;
import s1riys.lab8.client.console.IConsole;
import s1riys.lab8.client.managers.SessionManager;
import s1riys.lab8.client.network.UDPClient;
import s1riys.lab8.common.constants.Commands;
import s1riys.lab8.common.exceptions.APIException;
import s1riys.lab8.common.exceptions.WrongAmountOfElementsException;
import s1riys.lab8.common.network.requests.RemoveKeyRequest;
import s1riys.lab8.common.network.responses.RemoveKeyResponse;

import java.io.IOException;

public class RemoveKey extends ServersideCommand {
    public RemoveKey(IConsole console, UDPClient client) {
        super(
                defineSignature(Commands.REMOVE_KEY, "<id>"),
                "Удаляет элемент из коллекции по его ключу",
                console,
                client
        );
    }

    @Override
    public Boolean executeImpl(String[] data) {
        try {
            ValidationHelper.validateArgsLength(data, 1);
            Long id = Long.parseLong(data[0]);

            RemoveKeyRequest request = new RemoveKeyRequest(id, SessionManager.getCurrentUser());
            RemoveKeyResponse response = (RemoveKeyResponse) client.sendAndReceiveCommand(request);
            ValidationHelper.validateResponse(response);

            console.println("Продукт успешно удален.");
            return true;

        } catch (WrongAmountOfElementsException e) {
            console.printError("Неверное количество аргументов");
        } catch (NumberFormatException e) {
            console.printError("id должен быть представлен числом");
        } catch (IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        } catch (APIException e) {
            console.printError(e.getMessage());
        }
        return false;
    }
}
