package s1riys.lab8.server.commands;

import s1riys.lab8.common.constants.Commands;
import s1riys.lab8.common.network.requests.Request;
import s1riys.lab8.common.network.responses.InfoResponse;
import s1riys.lab8.common.network.responses.Response;
import s1riys.lab8.server.managers.CollectionManager;

import java.util.StringJoiner;

public class Info extends RepositoryCommand {
    public Info(CollectionManager collectionManager) {
        super(Commands.INFO, collectionManager);
    }

    @Override
    public Response execute(Request data) {
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add("Тип коллекции: " + collectionManager.getCollectionType());
        joiner.add("Количество элементов: " + collectionManager.getCollectionSize());
        joiner.add("Дата инициализации: " + collectionManager.getInitDate());
        joiner.add("Дата сохранения: " + collectionManager.getSaveTime());

        return new InfoResponse(joiner.toString(), null);
    }
}
