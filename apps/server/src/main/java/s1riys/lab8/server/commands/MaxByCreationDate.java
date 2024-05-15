package s1riys.lab8.server.commands;

import s1riys.lab8.common.constants.Commands;
import s1riys.lab8.common.models.Product;
import s1riys.lab8.common.network.requests.MaxByCreationDateRequest;
import s1riys.lab8.common.network.requests.Request;
import s1riys.lab8.common.network.responses.MaxByCreationDateResponse;
import s1riys.lab8.common.network.responses.Response;
import s1riys.lab8.server.managers.CollectionManager;

import java.util.Comparator;
import java.util.Optional;

public class MaxByCreationDate extends RepositoryCommand {
    public MaxByCreationDate(CollectionManager collectionManager) {
        super(Commands.MAX_BY_CREATION_DATE, collectionManager);
    }

    @Override
    public Response execute(Request data) {

        MaxByCreationDateRequest request = (MaxByCreationDateRequest) data;
        Optional<Product> product = collectionManager.getCollection().values().stream()
                .max(Comparator.comparing(Product::getCreationDate));

        if (product.isPresent()) return new MaxByCreationDateResponse(product.get(), null);
        else return new MaxByCreationDateResponse(null, "Не удалось обнаружить продукт с максимальной датой создания");
    }
}
