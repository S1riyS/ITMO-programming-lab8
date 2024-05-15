package s1riys.lab8.server.commands;

import s1riys.lab8.server.managers.CollectionManager;

public abstract class RepositoryCommand extends Command {
    protected final CollectionManager collectionManager;

    public RepositoryCommand(String keyword, CollectionManager collectionManager) {
        super(keyword);
        this.collectionManager = collectionManager;
    }
}
