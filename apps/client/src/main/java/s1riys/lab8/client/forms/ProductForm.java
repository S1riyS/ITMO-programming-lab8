package s1riys.lab8.client.forms;

import s1riys.lab8.client.console.IConsole;
import s1riys.lab8.client.managers.ScannerManager;
import s1riys.lab8.common.exceptions.ConstraintsViolationException;
import s1riys.lab8.common.exceptions.InvalidFormException;
import s1riys.lab8.common.exceptions.MustNotBeEmptyException;
import s1riys.lab8.common.models.Coordinates;
import s1riys.lab8.common.models.Organization;
import s1riys.lab8.common.models.Product;
import s1riys.lab8.common.models.UnitOfMeasure;

import java.util.Date;

/**
 * Represents a form for creating Product objects.
 */
public class ProductForm implements Form<Product> {
    private final IConsole console;
    /**
     * Constructs a ProductForm object with the specified console.
     *
     * @param console the console to use for output
     */
    public ProductForm(IConsole console) {
        this.console = console;
    }

    /**
     * Builds a Product object based on user input.
     *
     * @return the built Product object
     * @throws InvalidFormException if the form is invalid
     */
    @Override
    public Product build() throws InvalidFormException {
        console.println("Параметры продукта");
        Product product = new Product(
                Long.parseLong("1"),
                askName(),
                askCoordinates(),
                new Date(),
                askPrice(),
                askPartNumber(),
                askUnitOfMeasure(),
                askManufacturer()
        );
        if (!product.validate()) throw new InvalidFormException();
        return product;
    }

    /**
     * Asks the user for the name of the product.
     *
     * @return the name of the product entered by the user
     */
    private String askName() {
        String name;
        var fileMode = ScannerManager.getFileMode();
        while (true) {
            try {
                console.println("Введите название продукта:");
                console.ps2();

                name = ScannerManager.getInput();
                if (fileMode) console.println(name);
                if (name.trim().isEmpty()) throw new MustNotBeEmptyException();
                break;
            } catch (MustNotBeEmptyException exception) {
                console.printError("Название продукта не может быть пустым");
            }
        }
        return name;
    }

    /**
     * Asks the user for the coordinates of the product.
     *
     * @return the coordinates entered by the user
     * @throws InvalidFormException if the coordinates form is invalid
     */
    private Coordinates askCoordinates() throws InvalidFormException {
        return new CoordinatesForm(console).build();
    }

    /**
     * Asks the user for the price of the product.
     *
     * @return the price entered by the user
     */
    private Long askPrice() {
        Long price;
        var fileMode = ScannerManager.getFileMode();
        while (true) {
            try {
                console.println("Введите цену:");
                console.ps2();

                var strPrice = ScannerManager.getInput();
                if (fileMode) console.println(strPrice);

                price = Long.parseLong(strPrice);
                if (price <= 0) throw new ConstraintsViolationException();
                break;
            } catch (ConstraintsViolationException exception) {
                console.printError("Цена должна быть больше 0");
            } catch (NumberFormatException exception) {
                console.printError("Цена должна быть представлена числом");
            }
        }
        return price;
    }

    /**
     * Asks the user for the part number of the product.
     *
     * @return the part number entered by the user
     */
    private String askPartNumber() {
        String partNumber;
        var fileMode = ScannerManager.getFileMode();
        while (true) {
            try {
                console.println("Введите уникальный номер, под которым товар будет храниться в каталоге, или null:");
                console.ps2();

                partNumber = ScannerManager.getInput();
                if (fileMode) console.println(partNumber);
                if (partNumber.equals("null")) return null;
                if (partNumber.length() < 21) throw new ConstraintsViolationException();
                break;
            } catch (ConstraintsViolationException exception) {
                console.printError("Длина номера должна быть не меньше 21");
            }
        }
        return partNumber;
    }

    /**
     * Asks the user for the unit of measure of the product.
     *
     * @return the unit of measure entered by the user
     * @throws InvalidFormException if the unit of measure form is invalid
     */
    private UnitOfMeasure askUnitOfMeasure() throws InvalidFormException {
        return new UnitOfMeasureForm(console).build();
    }

    /**
     * Asks the user for the manufacturer of the product.
     *
     * @return the manufacturer entered by the user
     * @throws InvalidFormException if the organization form is invalid
     */
    private Organization askManufacturer() throws InvalidFormException {
        return new OrganizationForm(console).build();
    }
}
