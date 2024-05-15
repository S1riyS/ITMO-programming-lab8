package s1riys.lab8.client.commands;

import s1riys.lab8.client.utils.ValidationHelper;
import s1riys.lab8.client.console.IConsole;
import s1riys.lab8.client.managers.SessionManager;
import s1riys.lab8.client.network.UDPClient;
import s1riys.lab8.common.constants.Commands;
import s1riys.lab8.common.exceptions.APIException;
import s1riys.lab8.common.exceptions.WrongAmountOfElementsException;
import s1riys.lab8.common.network.requests.RemoveGreaterKeyRequest;
import s1riys.lab8.common.network.responses.RemoveGreaterKeyResponse;

import java.io.IOException;

import static s1riys.lab8.client.commands.utils.SignatureHelper.defineSignature;

public class RemoveGreaterKey extends ServersideCommand {
    public RemoveGreaterKey(IConsole console, UDPClient client) {
        super(
                defineSignature(Commands.REMOVE_GREATER_KEY, "<id>"),
                "Удаляет из коллекции все элементы, ключ которых превышает заданный",
                console,
                client
        );
    }

    @Override
    public Boolean executeImpl(String[] data) {
        try {
            ValidationHelper.validateArgsLength(data, 1);
            Long id = Long.parseLong(data[0]);

            RemoveGreaterKeyRequest request = new RemoveGreaterKeyRequest(id, SessionManager.getCurrentUser());
            RemoveGreaterKeyResponse response = (RemoveGreaterKeyResponse) client.sendAndReceiveCommand(request);
            ValidationHelper.validateResponse(response);

            if (response.count == 0) console.println("Продукты для удаления не найдены");
            else console.println("Продукты удалены (%s шт.)".formatted(response.count));

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
