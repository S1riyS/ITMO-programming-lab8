package s1riys.lab8.client.forms;

import s1riys.lab8.client.console.IConsole;
import s1riys.lab8.client.managers.ScannerManager;
import s1riys.lab8.common.exceptions.ConstraintsViolationException;
import s1riys.lab8.common.exceptions.MustNotBeEmptyException;

public class RegisterForm extends AuthForm {
    public RegisterForm(IConsole console) {
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
                if (password.isEmpty()) throw new MustNotBeEmptyException();

                console.println("Повторите введенный пароль:");
                console.ps2();

                String passwordAgain = ScannerManager.getInput();
                if (fileMode) console.println(passwordAgain);
                if (!password.equals(passwordAgain)) throw new ConstraintsViolationException();

                break;

            } catch (MustNotBeEmptyException exception) {
                console.printError("Минимальная длинна пароля - %s символов".formatted(MIN_PASSWORD_LENGTH));
            } catch (ConstraintsViolationException e) {
                console.printError("Введенные пароли не совпадают");
            }
        }
        return password;
    }
}
