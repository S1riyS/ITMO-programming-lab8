package s1riys.lab8.server.commands;

import s1riys.lab8.common.constants.Commands;
import s1riys.lab8.common.exceptions.ForbiddenException;
import s1riys.lab8.common.models.Product;
import s1riys.lab8.common.network.requests.RemoveGreaterKeyRequest;
import s1riys.lab8.common.network.requests.Request;
import s1riys.lab8.common.network.responses.RemoveGreaterKeyResponse;
import s1riys.lab8.common.network.responses.Response;
import s1riys.lab8.server.managers.CollectionManager;

import java.util.List;
import java.util.Objects;

public class RemoveGreaterKey extends RepositoryCommand {
    public RemoveGreaterKey(CollectionManager collectionManager) {
        super(Commands.REMOVE_GREATER_KEY, collectionManager);
    }

    @Override
    public Response execute(Request data) {
        try {
            RemoveGreaterKeyRequest request = (RemoveGreaterKeyRequest) data;

            List<Long> IdsToRemove = collectionManager.getCollection().values().stream()
                    .filter(product -> product.getId() > request.id)
                    .filter(product -> Objects.equals(product.getCreatorId(), request.getUser().getId()))
                    .map(Product::getId)
                    .toList();
            IdsToRemove.forEach(id -> {
                try {
                    collectionManager.remove(id, request.getUser());
                } catch (ForbiddenException ignored) {
                }
            });

            return new RemoveGreaterKeyResponse(IdsToRemove.size(), null);

        } catch (Exception e) {
            return new RemoveGreaterKeyResponse(-1, "Во время удаления произошла ошибка");
        }
    }
}
