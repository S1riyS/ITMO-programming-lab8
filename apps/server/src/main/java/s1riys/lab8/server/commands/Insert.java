package s1riys.lab8.server.commands;

import s1riys.lab8.common.network.requests.AddRequest;
import s1riys.lab8.common.network.requests.Request;
import s1riys.lab8.common.network.responses.AddResponse;
import s1riys.lab8.common.network.responses.Response;
import s1riys.lab8.server.managers.CollectionManager;
import s1riys.lab8.common.constants.Commands;

public class Insert extends RepositoryCommand {

    public Insert(CollectionManager collectionManager) {
        super(Commands.INSERT, collectionManager);
    }

    public Response execute(Request data) {
        AddRequest req = (AddRequest) data;
        try {
            if (!req.product.validate()) {
                return new AddResponse((long) -1, "Поля продукта не валидны, продукт не добавлен");
            }
            Long newId = collectionManager.add(req.product, req.getUser());
            return new AddResponse(newId, null);
        } catch (Exception e) {
            return new AddResponse((long) -1, e.toString());
        }
    }
}
