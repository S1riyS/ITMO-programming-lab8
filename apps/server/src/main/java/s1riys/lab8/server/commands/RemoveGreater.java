package s1riys.lab8.server.commands;

import s1riys.lab8.common.constants.Commands;
import s1riys.lab8.common.exceptions.ForbiddenException;
import s1riys.lab8.common.models.Product;
import s1riys.lab8.common.network.requests.RemoveGreaterRequest;
import s1riys.lab8.common.network.requests.Request;
import s1riys.lab8.common.network.responses.RemoveGreaterResponse;
import s1riys.lab8.common.network.responses.Response;
import s1riys.lab8.server.managers.CollectionManager;

import java.util.List;
import java.util.Objects;

public class RemoveGreater extends RepositoryCommand {
    public RemoveGreater(CollectionManager collectionManager) {
        super(Commands.REMOVE_GREATER, collectionManager);
    }

    @Override
    public Response execute(Request data) {
        try {
            RemoveGreaterRequest request = (RemoveGreaterRequest) data;

            List<Long> idsToRemove = collectionManager.getSortedCollection().stream()
                    .filter(Objects::nonNull)
                    .filter(product -> product.compareTo(request.product) > 0)
                    .filter(product -> Objects.equals(product.getCreatorId(), request.getUser().getId()))
                    .map(Product::getId)
                    .toList();
            idsToRemove.forEach(id -> {
                try {
                    collectionManager.remove(id, request.getUser());
                } catch (ForbiddenException ignored) {
                }
            });

            return new RemoveGreaterResponse(idsToRemove.size(), null);
        } catch (Exception e) {
            return new RemoveGreaterResponse(-1, "Во время удаления произошла ошибка");
        }
    }
}
