package s1riys.lab8.client.commands;

import static s1riys.lab8.client.commands.utils.SignatureHelper.defineSignature;

import s1riys.lab8.client.utils.ValidationHelper;
import s1riys.lab8.client.console.IConsole;
import s1riys.lab8.client.forms.ProductForm;
import s1riys.lab8.client.managers.SessionManager;
import s1riys.lab8.client.network.UDPClient;
import s1riys.lab8.common.exceptions.APIException;
import s1riys.lab8.common.exceptions.InvalidFormException;
import s1riys.lab8.common.models.Product;
import s1riys.lab8.common.constants.Commands;
import s1riys.lab8.common.network.requests.AddRequest;
import s1riys.lab8.common.network.responses.AddResponse;

import java.io.IOException;

public class Insert extends ServersideCommand {
    public Insert(IConsole console, UDPClient client) {
        super(
                defineSignature(Commands.INSERT, "{product}"),
                "Добавить новый элемент",
                console,
                client
        );
    }

    @Override
    public Boolean executeImpl(String[] data) {
        try {
            Product product = new ProductForm(console).build();

            AddResponse response = (AddResponse) client.sendAndReceiveCommand(new AddRequest(product, SessionManager.getCurrentUser()));
            ValidationHelper.validateResponse(response);

            console.println("Новый продукт с id=%s успешно добавлен!".formatted(response.newId));
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
