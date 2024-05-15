package s1riys.lab8.client.commands;

import s1riys.lab8.client.utils.ValidationHelper;
import s1riys.lab8.client.console.IConsole;
import s1riys.lab8.client.forms.ProductForm;
import s1riys.lab8.client.managers.SessionManager;
import s1riys.lab8.client.network.UDPClient;
import s1riys.lab8.common.constants.Commands;
import s1riys.lab8.common.exceptions.APIException;
import s1riys.lab8.common.exceptions.InvalidFormException;
import s1riys.lab8.common.models.Product;
import s1riys.lab8.common.network.requests.RemoveLowerRequest;
import s1riys.lab8.common.network.responses.RemoveLowerResponse;

import java.io.IOException;

import static s1riys.lab8.client.commands.utils.SignatureHelper.defineSignature;

public class RemoveLower extends ServersideCommand {
    public RemoveLower(IConsole console, UDPClient client) {
        super(
                defineSignature(Commands.REMOVE_LOWER, "{product}"),
                "Удаляет из коллекции все элементы, меньшие, чем заданный",
                console,
                client
        );
    }

    @Override
    public Boolean executeImpl(String[] data) {
        try {
            Product product = new ProductForm(console).build();

            RemoveLowerRequest request = new RemoveLowerRequest(product, SessionManager.getCurrentUser());
            RemoveLowerResponse response = (RemoveLowerResponse) client.sendAndReceiveCommand(request);
            ValidationHelper.validateResponse(response);

            if (response.count == 0) console.println("Продукты для удаления не найдены");
            else console.println("Продукты удалены (%s шт.)".formatted(response.count));

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
