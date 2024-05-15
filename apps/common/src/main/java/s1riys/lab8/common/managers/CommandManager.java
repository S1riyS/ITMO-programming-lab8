package s1riys.lab8.common.managers;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CommandManager<T> {
    private final Map<String, T> commands = new LinkedHashMap<>();

    public void register(String commandName, T command) {
        commands.put(commandName, command);
    }

    public T getOne(String commandName) {
        return commands.get(commandName);
    }

    public Map<String, T> getAll() {
        return commands;
    }
}
