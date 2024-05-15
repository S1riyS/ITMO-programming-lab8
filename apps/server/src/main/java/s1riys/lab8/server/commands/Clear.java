package s1riys.lab8.server.commands;

import s1riys.lab8.common.constants.Commands;
import s1riys.lab8.common.network.requests.Request;
import s1riys.lab8.common.network.responses.ClearResponse;
import s1riys.lab8.common.network.responses.Response;
import s1riys.lab8.server.managers.CollectionManager;

public class Clear extends RepositoryCommand {
    public Clear(CollectionManager collectionManager) {
        super(Commands.CLEAR, collectionManager);
    }

    @Override
    public Response execute(Request data) {
        try {
            collectionManager.clear(data.getUser());
            return new ClearResponse(null);
        } catch (Exception e) {
            return new ClearResponse("Не удалось очистить коллекцию");
        }
    }
}
