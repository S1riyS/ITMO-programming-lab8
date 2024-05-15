package s1riys.lab8.server.commands;

import s1riys.lab8.common.constants.Commands;
import s1riys.lab8.common.models.Product;
import s1riys.lab8.common.network.requests.Request;
import s1riys.lab8.common.network.responses.Response;
import s1riys.lab8.common.network.responses.ShowResponse;
import s1riys.lab8.server.managers.CollectionManager;

import java.util.ArrayList;
import java.util.List;

public class Show extends RepositoryCommand {
    public Show(CollectionManager collectionManager) {
        super(Commands.SHOW, collectionManager);
    }

    @Override
    public Response execute(Request data) {
        try {
            List<Product> collection = new ArrayList<>(collectionManager.getCollection().values());
            return new ShowResponse(collection, null);
        } catch (Exception e) {
            System.out.println(e);
            return new ShowResponse(null, e.toString());
        }
    }
}
