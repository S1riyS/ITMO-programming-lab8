package s1riys.lab8.client.forms;

import s1riys.lab8.client.console.IConsole;
import s1riys.lab8.client.managers.ScannerManager;
import s1riys.lab8.common.exceptions.ConstraintsViolationException;
import s1riys.lab8.common.exceptions.InvalidFormException;
import s1riys.lab8.common.exceptions.MustNotBeEmptyException;
import s1riys.lab8.common.models.Organization;

/**
 * Represents a form for creating Organization objects.
 */
public class OrganizationForm implements Form<Organization> {
    private final IConsole console;

    /**
     * Constructs an OrganizationForm object with the specified console.
     *
     * @param console the console to use for output
     */
    public OrganizationForm(IConsole console) {
        this.console = console;
    }

    /**
     * Builds an Organization object based on user input.
     *
     * @return the built Organization object
     * @throws InvalidFormException if the form is invalid
     */
    @Override
    public Organization build() throws InvalidFormException {
        console.println("Параметры организации (чтобы оставить поле для организации пустым, введите 'null'). Для продолжения введите любой символ");
        console.ps2();

        var fileMode = ScannerManager.getFileMode();
        var input = ScannerManager.getInput();
        if (fileMode) console.println(input);
        if (input.equals("null")) return null;

        Organization organization = new Organization(
                1L,
                askName(),
                askFullName(),
                askAnnualTurnover(),
                askEmployeesCount()
        );
        if (!organization.validate()) throw new InvalidFormException();
        return organization;
    }

    /**
     * Asks the user for the name of the organization.
     *
     * @return the name of the organization entered by the user
     */
    private String askName() {
        String name;
        var fileMode = ScannerManager.getFileMode();
        while (true) {
            try {
                console.println("Введите название организации:");
                console.ps2();

                name = ScannerManager.getInput();
                if (fileMode) console.println(name);
                if (name.trim().isEmpty()) throw new MustNotBeEmptyException();
                break;
            } catch (MustNotBeEmptyException exception) {
                console.printError("Название организации не может быть пустым");
            }
        }
        return name;
    }

    /**
     * Asks the user for the full name of the organization.
     *
     * @return the full name of the organization entered by the user
     */
    private String askFullName() {
        String fullName;
        var fileMode = ScannerManager.getFileMode();
        while (true) {
            try {
                console.println("Введите полное название организации:");
                console.ps2();

                fullName = ScannerManager.getInput();
                if (fileMode) console.println(fullName);
                if (fullName.isEmpty()) throw new MustNotBeEmptyException();
                if (fullName.length() > 1366) throw new ConstraintsViolationException();
                break;
            } catch (MustNotBeEmptyException exception) {
                console.printError("Полное название организации не может быть пустым");
            } catch (ConstraintsViolationException exception) {
                console.printError("Длина полного названия не может превышать 1366 символов");
            }
        }
        return fullName;
    }

    /**
     * Asks the user for the annual turnover of the organization.
     *
     * @return the annual turnover entered by the user
     */
    private Double askAnnualTurnover() {
        Double annualTurnover;
        var fileMode = ScannerManager.getFileMode();
        while (true) {
            try {
                console.println("Введите ежегодный оборот:");
                console.ps2();

                var strAnnualTurnover = ScannerManager.getInput();
                if (fileMode) console.println(strAnnualTurnover);

                annualTurnover = Double.parseDouble(strAnnualTurnover);
                if (annualTurnover <= 0) throw new ConstraintsViolationException();
                break;
            } catch (ConstraintsViolationException exception) {
                console.printError("Ежегодный оборот не может быть меньше 0");
            } catch (NumberFormatException exception) {
                console.printError("Ежегодный оборот должен быть представлен числом");
            }
        }
        return annualTurnover;
    }

    /**
     * Asks the user for the number of employees in the organization.
     *
     * @return the number of employees entered by the user
     */
    private int askEmployeesCount() {
        int employeesCount;
        var fileMode = ScannerManager.getFileMode();
        while (true) {
            try {
                console.println("Введите количество сотрудников:");
                console.ps2();

                var strEmployeesCount = ScannerManager.getInput();
                if (fileMode) console.println(strEmployeesCount);

                employeesCount = Integer.parseInt(strEmployeesCount);
                if (employeesCount <= 0) throw new ConstraintsViolationException();
                break;
            } catch (ConstraintsViolationException exception) {
                console.printError("Количество сотрудников не может быть меньше 0");
            } catch (NumberFormatException exception) {
                console.printError("Количество сотрудников должно быть представлено числом");
            }
        }
        return employeesCount;
    }
}
