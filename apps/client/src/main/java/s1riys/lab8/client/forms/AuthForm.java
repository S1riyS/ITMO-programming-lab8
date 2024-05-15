package s1riys.lab8.client.forms;

import s1riys.lab8.client.console.IConsole;
import s1riys.lab8.client.managers.ScannerManager;
import s1riys.lab8.common.exceptions.ConstraintsViolationException;
import s1riys.lab8.common.exceptions.InvalidFormException;
import s1riys.lab8.common.models.User;

public abstract class AuthForm implements Form<User> {
    protected final IConsole console;
    protected final int MAX_NAME_LENGTH = 30;
    protected final int MIN_PASSWORD_LENGTH = 5;

    public AuthForm(IConsole console) {
        this.console = console;
    }

    @Override
    public User build() throws InvalidFormException {
        User user = new User(
                -1,
                askName(),
                askPassword()
        );
        if (!user.validate()) throw new InvalidFormException();
        return user;
    }

    private String askName() {
        String name;
        var fileMode = ScannerManager.getFileMode();

        while (true) {
            try {
                console.println("Введите имя пользователя:");
                console.ps2();

                name = ScannerManager.getInput();
                if (fileMode) console.println(name);

                String sanitizedName = name.trim();
                if (sanitizedName.isEmpty() || sanitizedName.length() > MAX_NAME_LENGTH)
                    throw new ConstraintsViolationException();
                break;

            } catch (ConstraintsViolationException exception) {
                console.printError("Имя пользователя должно содержать от 1 до %s символов".formatted(MAX_NAME_LENGTH));
            }
        }
        return name;
    }

    protected abstract String askPassword();
}
