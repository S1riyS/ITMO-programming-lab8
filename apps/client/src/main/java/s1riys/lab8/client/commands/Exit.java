package s1riys.lab8.client.commands;

import static s1riys.lab8.client.commands.utils.SignatureHelper.defineSignature;
import s1riys.lab8.client.console.IConsole;
import s1riys.lab8.common.constants.Commands;

public class Exit extends Command {
    public Exit(IConsole console) {
        super(defineSignature(Commands.EXIT), "Завершает программу (без сохранения в файл)", console);
    }

    @Override
    public Boolean execute(String[] data) {
        return true;
    }
}
