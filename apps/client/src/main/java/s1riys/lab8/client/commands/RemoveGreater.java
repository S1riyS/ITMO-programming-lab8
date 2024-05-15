package s1riys.lab8.client.commands;

import static s1riys.lab8.client.commands.utils.SignatureHelper.defineSignature;

import s1riys.lab8.client.utils.ValidationHelper;
import s1riys.lab8.client.console.IConsole;
import s1riys.lab8.client.forms.ProductForm;
import s1riys.lab8.client.managers.SessionManager;
import s1riys.lab8.client.network.UDPClient;
import s1riys.lab8.common.constants.Commands;
import s1riys.lab8.common.exceptions.APIException;
import s1riys.lab8.common.exceptions.InvalidFormException;
import s1riys.lab8.common.models.Product;
import s1riys.lab8.common.network.requests.RemoveGreaterRequest;
import s1riys.lab8.common.network.responses.RemoveGreaterResponse;

import java.io.IOException;

public class RemoveGreater extends ServersideCommand {
    public RemoveGreater(IConsole console, UDPClient client) {
        super(
                defineSignature(Commands.REMOVE_GREATER, "{product}"),
                "Удаляет из коллекции все элементы, превышающие заданный",
                console,
                client
        );
    }

    @Override
    public Boolean executeImpl(String[] data) {
        try {
            Product product = new ProductForm(console).build();

            RemoveGreaterRequest request = new RemoveGreaterRequest(product, SessionManager.getCurrentUser());
            RemoveGreaterResponse response = (RemoveGreaterResponse) client.sendAndReceiveCommand(request);
            ValidationHelper.validateResponse(response);

            if (response.count == 0) {
                console.println("Продукты для удаления не найдены");
            } else {
                console.println("Продукты удалены (%s шт.)".formatted(response.count));
            }
            return true;

        } catch (InvalidFormException e) {
            console.printError("Поля продукта не валидны");
        } catch (IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        } catch (APIException e) {
            console.printError(e.getMessage());
        }
        return false;
    }
}
