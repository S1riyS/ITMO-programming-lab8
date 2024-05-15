package s1riys.lab8.client.forms;

import s1riys.lab8.client.console.IConsole;
import s1riys.lab8.client.managers.ScannerManager;
import s1riys.lab8.common.exceptions.ConstraintsViolationException;

public class LoginForm extends AuthForm {
    public LoginForm(IConsole console) {
        super(console);
    }

    @Override
    protected String askPassword() {
        String password;
        var fileMode = ScannerManager.getFileMode();

        while (true) {
            try {
                console.println("Введите пароль:");
                console.ps2();

                password = ScannerManager.getInput();
                if (fileMode) console.println(password);

                if (password.length() < MIN_PASSWORD_LENGTH) throw new ConstraintsViolationException();
                break;

            } catch (ConstraintsViolationException exception) {
                console.printError("Минимальная длинна пароля - %s символов".formatted(MIN_PASSWORD_LENGTH));
            }
        }
        return password;
    }
}
