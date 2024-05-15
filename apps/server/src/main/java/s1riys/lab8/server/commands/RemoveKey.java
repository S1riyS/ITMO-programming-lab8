package s1riys.lab8.server.commands;

import s1riys.lab8.common.constants.Commands;
import s1riys.lab8.common.network.requests.RemoveKeyRequest;
import s1riys.lab8.common.network.requests.Request;
import s1riys.lab8.common.network.responses.RemoveKeyResponse;
import s1riys.lab8.common.network.responses.Response;
import s1riys.lab8.common.network.responses.UpdateResponse;
import s1riys.lab8.server.managers.CollectionManager;

public class RemoveKey extends RepositoryCommand {
    public RemoveKey(CollectionManager collectionManager) {
        super(Commands.REMOVE_KEY, collectionManager);
    }

    @Override
    public Response execute(Request data) {
        RemoveKeyRequest request = (RemoveKeyRequest) data;
        try {
            int removedCount = collectionManager.remove(request.id, request.getUser());

            if (removedCount == 0) return new RemoveKeyResponse("Не существует продукта с таким id");
            return new RemoveKeyResponse(null);

        } catch (Exception e) {
            e.printStackTrace();
            return new RemoveKeyResponse("Не удалось удалить продукт");
        }
    }
}
