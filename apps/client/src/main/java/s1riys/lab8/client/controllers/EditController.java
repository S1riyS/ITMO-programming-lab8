package s1riys.lab8.client.controllers;

import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import s1riys.lab8.client.managers.DialogManager;
import s1riys.lab8.client.managers.SessionManager;
import s1riys.lab8.common.models.Coordinates;
import s1riys.lab8.common.models.Organization;
import s1riys.lab8.common.models.Product;
import s1riys.lab8.common.models.UnitOfMeasure;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class EditController extends LocalizedController {
    private Stage stage;
    private Product product;

    // Labels
    @FXML
    private Label titleLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label xLabel;

    @FXML
    private Label yLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label partNumberLabel;

    @FXML
    private Label unitOfMeasureLabel;

    @FXML
    private Label hasMunufacturerLabel;

    @FXML
    private Label mNameLabel;

    @FXML
    private Label mFullNameLabel;

    @FXML
    private Label mAnnualTurnoverLabel;

    @FXML
    private Label mEmployeesCountLabel;

    // Fields
    @FXML
    private TextField nameField;

    @FXML
    private TextField xField;

    @FXML
    private TextField yField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField partNumberField;

    @FXML
    private ChoiceBox<String> unitOfMeasureBox;

    @FXML
    private CheckBox hasManufacturerCheckBox;

    @FXML
    private TextField mNameField;

    @FXML
    private TextField mFullNameField;

    @FXML
    private TextField mAnnualTurnoverField;

    @FXML
    private TextField mEmployeesCountField;

    // Buttons
    @FXML
    private Button okButton;

    @FXML
    private Button cancelButton;

    @FXML
    public void initialize() {
        cancelButton.setOnAction(event -> stage.close());

        var unitOfMeasures = FXCollections.observableArrayList(
                Arrays.stream(UnitOfMeasure.values())
                        .map(Enum::toString)
                        .collect(Collectors.toList())
        );
        unitOfMeasureBox.setItems(unitOfMeasures);
        unitOfMeasureBox.setStyle("-fx-font: 14px \"Sergoe UI\";");

        Arrays.asList(mNameField, mFullNameField, mAnnualTurnoverField, mEmployeesCountField).forEach(field -> {
            field.disableProperty().bind(hasManufacturerCheckBox.selectedProperty().not());
        });

        // Int validation
        Arrays.asList(xField, mEmployeesCountField).forEach(field -> {
            field.textProperty().addListener((observableValue, oldValue, newValue) -> {
                if (!newValue.matches("^-?\\d{0,9}$|^$")) {
                    field.setText(oldValue);
                }
            });
        });

        // Long validation
        Arrays.asList(priceField, yField).forEach(field -> {
            field.textProperty().addListener((observableValue, oldValue, newValue) -> {
                if (!newValue.matches("^-?\\d{0,19}$|^$")) {
                    field.setText(oldValue);
                }
            });
        });
    }

    @FXML
    void cancel(ActionEvent event) {

    }

    @FXML
    public void ok() {
        try {
            // Name
            String name = nameField.getText().trim();

            // X
            Integer x;
            if (xField.getText().isEmpty()) x = null;
            else x = Integer.parseInt(xField.getText());

            // Y
            Long y;
            if (yField.getText().isEmpty()) y = null;
            else y = Long.parseLong(yField.getText());

            // Price
            Long price;
            if (priceField.getText().isEmpty()) price = null;
            else price = Long.parseLong(priceField.getText());

            // Part number
            String partNumber;
            if (partNumberField.getText().isEmpty()) partNumber = null;
            else partNumber = partNumberField.getText().trim();

            // Unit of measure
            UnitOfMeasure unitOfMeasure = null;
            if (unitOfMeasureBox.getValue() != null)
                unitOfMeasure = UnitOfMeasure.valueOf(unitOfMeasureBox.getValue());

            // Manufacturer
            Organization manufacturer = null;
            if (hasManufacturerCheckBox.isSelected()) {
                // Name
                String mName = mNameField.getText().trim();

                // Full name
                String mFullName = mFullNameField.getText().trim();

                // Annual turnover
                Double mAnnualTurnover;
                if (mAnnualTurnoverField.getText().isEmpty()) mAnnualTurnover = null;
                else mAnnualTurnover = Double.parseDouble(mAnnualTurnoverField.getText());

                // Employees count
                Integer mEmployeesCount;
                if (mEmployeesCountField.getText().isEmpty()) mEmployeesCount = null;
                else mEmployeesCount = Integer.parseInt(mEmployeesCountField.getText());

                manufacturer = new Organization(
                        (long) -1,
                        mName,
                        mFullName,
                        mAnnualTurnover,
                        mEmployeesCount
                );
            }

            Product newProduct = new Product(
                    (long) -1,
                    name,
                    new Coordinates(x, y),
                    new Date(),
                    price,
                    partNumber,
                    unitOfMeasure,
                    manufacturer,
                    SessionManager.getCurrentUser().getId()
            );

            if (!newProduct.validate()) {
                System.out.println(newProduct);
                System.out.println("NORMAL");
                DialogManager.alert("InvalidProduct", localizator);
            } else {
                product = newProduct;
                stage.close();
            }

        } catch (NumberFormatException e) {
            System.out.println("CATCH");
            DialogManager.alert("InvalidProduct", localizator);
        }
    }

    public void clear() {
        nameField.clear();
        xField.clear();
        yField.clear();
        priceField.clear();
        partNumberField.clear();
        unitOfMeasureBox.valueProperty().setValue(null);
        hasManufacturerCheckBox.disableProperty();

        mNameField.clear();
        mFullNameField.clear();
        mAnnualTurnoverField.clear();
        mEmployeesCountField.clear();
    }

    public void fill(Product product) {
        nameField.setText(product.getName());
        xField.setText(Integer.toString(product.getCoordinates().getX()));
        yField.setText(Long.toString(product.getCoordinates().getY()));
        priceField.setText(Long.toString(product.getPrice()));
        partNumberField.setText(product.getPartNumber() == null ? "" : product.getPartNumber());
        unitOfMeasureBox.setValue(product.getUnitOfMeasure() == null ? null : product.getUnitOfMeasure().toString());

        hasManufacturerCheckBox.selectedProperty().setValue(!(product.getManufacturer() == null));

        if (product.getManufacturer() != null) {
            var manufacturer = product.getManufacturer();
            mNameField.setText(manufacturer.getName());
            mFullNameField.setText(manufacturer.getFullName());
            mAnnualTurnoverField.setText(Double.toString(manufacturer.getAnnualTurnover()));
            mEmployeesCountField.setText(Integer.toString(manufacturer.getEmployeesCount()));
        } else {
            mNameField.clear();
            mFullNameField.clear();
            mAnnualTurnoverField.clear();
            mEmployeesCountField.clear();
        }
    }

    public Product getProduct() {
        var tmpProduct = product;
        product = null;
        return tmpProduct;
    }

    @Override
    public void changeLanguage() {
        titleLabel.setText(localizator.getKeyString("EditTitle"));

        nameLabel.setText(localizator.getKeyString("Name"));
        priceLabel.setText(localizator.getKeyString("Price"));
        partNumberLabel.setText(localizator.getKeyString("PartNumber"));
        unitOfMeasureLabel.setText(localizator.getKeyString("UnitOfMeasure"));

        hasMunufacturerLabel.setText(localizator.getKeyString("HasManufacturer"));
        mNameLabel.setText(localizator.getKeyString("ManufacturerName"));
        mFullNameLabel.setText(localizator.getKeyString("ManufacturerFullName"));
        mAnnualTurnoverLabel.setText(localizator.getKeyString("ManufacturerAnnualTurnover"));
        mEmployeesCountLabel.setText(localizator.getKeyString("ManufacturerEmployeesCount"));

        cancelButton.setText(localizator.getKeyString("CancelButton"));
    }

    public void show() {
        if (!stage.isShowing()) stage.showAndWait();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}

