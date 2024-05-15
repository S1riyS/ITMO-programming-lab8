package s1riys.lab8.client.forms;

import s1riys.lab8.client.console.IConsole;
import s1riys.lab8.client.managers.ScannerManager;
import s1riys.lab8.common.models.UnitOfMeasure;

/**
 * Represents a form for creating UnitOfMeasure objects.
 */
public class UnitOfMeasureForm implements Form<UnitOfMeasure> {
    private final IConsole console;

    /**
     * Constructs a UnitOfMeasureForm object with the specified console.
     *
     * @param console the console to use for input and output
     */
    public UnitOfMeasureForm(IConsole console) {
        this.console = console;
    }

    /**
     * Builds a UnitOfMeasure object based on user input.
     *
     * @return the built UnitOfMeasure object
     */
    @Override
    public UnitOfMeasure build() {
        UnitOfMeasure unitOfMeasure;
        var enumNames = UnitOfMeasure.getNames();
        var fileMode = ScannerManager.getFileMode();
        while (true) {
            try {
                console.println("Введите единицу измерения или null (Список доступных наименований: %s):".formatted(enumNames));
                console.ps2();

                var strUnitOfMeasure = ScannerManager.getInput();
                if (fileMode) console.println(strUnitOfMeasure);

                if (strUnitOfMeasure.equals("null")) return null;
                unitOfMeasure = UnitOfMeasure.valueOf(strUnitOfMeasure);
                break;
            } catch (IllegalArgumentException exception) {
                console.printError("Единица измерения не найдена");
            }
        }
        return unitOfMeasure;
    }
}
