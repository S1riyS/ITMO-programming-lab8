package s1riys.lab8.client.forms;

import s1riys.lab8.client.console.IConsole;
import s1riys.lab8.client.managers.ScannerManager;
import s1riys.lab8.common.exceptions.ConstraintsViolationException;
import s1riys.lab8.common.exceptions.InvalidFormException;
import s1riys.lab8.common.models.Coordinates;

/**
 * Represents a form for creating Coordinates objects.
 */
public class CoordinatesForm implements Form<Coordinates> {
    private final IConsole console;

    /**
     * Constructs a CoordinatesForm object with the specified console.
     *
     * @param console the console to use for output
     */
    public CoordinatesForm(IConsole console) {
        this.console = console;
    }

    /**
     * Builds a Coordinates object based on user input.
     *
     * @return the built Coordinates object
     * @throws InvalidFormException if the form is invalid
     */
    @Override
    public Coordinates build() throws InvalidFormException {
        var coordinates = new Coordinates(askX(), askY());
        if (!coordinates.validate()) throw new InvalidFormException();
        return coordinates;
    }

    /**
     * Asks the user for the X coordinate.
     *
     * @return the X coordinate entered by the user
     */
    private int askX() {
        int x;
        var fileMode = ScannerManager.getFileMode();
        while (true) {
            try {
                console.println("Введите координату по X:");
                console.ps2();

                var strX = ScannerManager.getInput();
                if (fileMode) console.println(strX);

                x = Integer.parseInt(strX);
                if (x <= -205) throw new ConstraintsViolationException();
                break;
            } catch (ConstraintsViolationException exception) {
                console.printError("Координата по X должна быть больше -205");
            } catch (NumberFormatException exception) {
                console.printError("Координата по X должна быть представлена числом");
            }
        }
        return x;
    }

    /**
     * Asks the user for the Y coordinate.
     *
     * @return the Y coordinate entered by the user
     */
    private Long askY() {
        Long y;
        var fileMode = ScannerManager.getFileMode();
        while (true) {
            try {
                console.println("Введите координату по Y:");
                console.ps2();

                var strY = ScannerManager.getInput();
                if (fileMode) console.println(strY);

                y = Long.parseLong(strY);
                if (y <= -558) throw new ConstraintsViolationException();
                break;
            } catch (ConstraintsViolationException exception) {
                console.printError("Координата по Y должна быть больше -558");
            } catch (NumberFormatException exception) {
                console.printError("Координата по Y должна быть представлена числом");
            }
        }
        return y;
    }
}
