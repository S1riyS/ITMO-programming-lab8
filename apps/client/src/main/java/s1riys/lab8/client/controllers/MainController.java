package s1riys.lab8.client.controllers;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import s1riys.lab8.client.console.StandardConsole;
import s1riys.lab8.client.managers.DialogManager;
import s1riys.lab8.client.managers.ScriptManager;
import s1riys.lab8.client.managers.SessionManager;
import s1riys.lab8.client.network.UDPClient;
import s1riys.lab8.client.utils.ProductRenderer;
import s1riys.lab8.client.utils.ValidationHelper;
import s1riys.lab8.common.exceptions.APIException;
import s1riys.lab8.common.models.Product;
import s1riys.lab8.common.network.requests.*;
import s1riys.lab8.common.network.responses.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public class MainController extends LocalizedController {
    private UDPClient client;
    private Stage stage;

    private final ObservableList<Product> masterData = FXCollections.observableArrayList();
    private final FilteredList<Product> filteredData = new FilteredList<>(masterData, p -> true);
    private final SortedList<Product> sortedData = new SortedList<>(filteredData);

    private final HashMap<Long, Color> colorsMap = new HashMap<>();
    private final HashMap<Long, Label> infoMap = new HashMap<>();
    private final Random random = new Random();
    private final boolean isRefreshing = true;
    private final Integer refreshingRate = 7_500;
    private Product selectedProduct = null;
    private EditController editController;

    // Tabs
    @FXML
    private Tab tableTab;

    @FXML
    private Tab visualizationTab;

    // Panes
    @FXML
    private AnchorPane visualPane;

    // Labels
    @FXML
    private Label userLabel;

    @FXML
    private Label mainTitleLabel;

    // Main buttons
    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button removeButton;

    // Other buttons
    @FXML
    private MenuButton othersButton;

    @FXML
    private MenuItem infoButton;

    @FXML
    private MenuItem clearButton;

    @FXML
    private MenuItem executeScriptButton;

    @FXML
    private MenuItem removeGreaterButton;

    @FXML
    private MenuItem removeLowerButton;

    @FXML
    private MenuItem removeGreaterKeyButton;

    // Language
    @FXML
    private ComboBox<String> languageComboBox;

    // Table
    @FXML
    private TableView<Product> tableView;

    @FXML
    private TableColumn<Product, Long> ownerIdColumn;

    @FXML
    private TableColumn<Product, Long> idColumn;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, Integer> xColumn;

    @FXML
    private TableColumn<Product, Long> yColumn;

    @FXML
    private TableColumn<Product, String> dateColumn;

    @FXML
    private TableColumn<Product, Long> priceColumn;

    @FXML
    private TableColumn<Product, String> partNumberColumn;

    @FXML
    private TableColumn<Product, String> unitOfMeasureColumn;

    @FXML
    private TableColumn<Product, Long> manufacturerIdColumn;

    @FXML
    private TableColumn<Product, String> manufacturerNameColumn;

    @FXML
    private TableColumn<Product, String> manufacturerFullNameColumn;

    @FXML
    private TableColumn<Product, String> manufacturerAnnualTurnoverColumn;

    @FXML
    private TableColumn<Product, Integer> manufacturerEmployeesCountColumn;

    // Filter
    @FXML
    private TextField filterField;

    @FXML
    private ComboBox<TableColumn<Product, ?>> filterColumnComboBox;

    @FXML
    public void initialize() {
        Platform.runLater(this::changeLanguage);

        visualizationTab.setOnSelectionChanged(event -> visualise(true));

        languageComboBox.setItems(FXCollections.observableArrayList(localeMap.keySet()));
        languageComboBox.setStyle("-fx-font: 13px \"Sergoe UI\";");
        languageComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            localizator.setBundle(ResourceBundle.getBundle("locales/gui", localeMap.get(newValue)));
            changeLanguage();
        });

        userLabel.setText("%s (ID: %S)".formatted(
                SessionManager.getCurrentUser().getName(),
                SessionManager.getCurrentUser().getId()
        ));

        ownerIdColumn.setCellValueFactory(product -> new SimpleLongProperty(product.getValue().getCreatorId()).asObject());
        idColumn.setCellValueFactory(product -> new SimpleLongProperty(product.getValue().getId()).asObject());
        nameColumn.setCellValueFactory(product -> new SimpleStringProperty(product.getValue().getName()));
        xColumn.setCellValueFactory(product -> new SimpleIntegerProperty(product.getValue().getCoordinates().getX()).asObject());
        yColumn.setCellValueFactory(product -> new SimpleLongProperty(product.getValue().getCoordinates().getY()).asObject());
        dateColumn.setCellValueFactory(product -> new SimpleStringProperty(localizator.getDate(product.getValue().getCreationDate())));
        priceColumn.setCellValueFactory(product -> new SimpleLongProperty(product.getValue().getPrice()).asObject());
        partNumberColumn.setCellValueFactory(product -> new SimpleStringProperty(product.getValue().getPartNumber()));

        unitOfMeasureColumn.setCellValueFactory(
                product -> new SimpleStringProperty(
                        product.getValue().getUnitOfMeasure() != null ? product.getValue().getUnitOfMeasure().toString() : null
                )
        );

        manufacturerIdColumn.setCellValueFactory(product -> {
            if (product.getValue().getManufacturer() != null) {
                return new SimpleLongProperty(product.getValue().getManufacturer().getId()).asObject();
            }
            return null;
        });

        manufacturerNameColumn.setCellValueFactory(product -> {
            if (product.getValue().getManufacturer() != null) {
                return new SimpleStringProperty(product.getValue().getManufacturer().getName());
            }
            return null;
        });

        manufacturerFullNameColumn.setCellValueFactory(product -> {
            if (product.getValue().getManufacturer() != null) {
                return new SimpleStringProperty(product.getValue().getManufacturer().getFullName());
            }
            return null;
        });

        manufacturerAnnualTurnoverColumn.setCellValueFactory(product -> {
            if (product.getValue().getManufacturer() != null) {
                return new SimpleStringProperty(localizator.getNumber(product.getValue().getManufacturer().getAnnualTurnover()));
            }
            return null;
        });

        manufacturerEmployeesCountColumn.setCellValueFactory(product -> {
            if (product.getValue().getManufacturer() != null) {
                return new SimpleIntegerProperty(product.getValue().getManufacturer().getEmployeesCount()).asObject();
            }
            return null;
        });

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean controlButtonsState;
            if (newSelection != null) {
                selectedProduct = newSelection;
                controlButtonsState = !Objects.equals(newSelection.getCreatorId(), SessionManager.getCurrentUser().getId());
                editButton.setDisable(controlButtonsState);
                removeButton.setDisable(controlButtonsState);
            }
        });

        // Filters
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (selectedProduct != null) tableView.getSelectionModel().select(selectedProduct);

            filteredData.setPredicate(product -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                TableColumn<Product, ?> selectedColumn = filterColumnComboBox.getSelectionModel().getSelectedItem();
                var observableValueToFilter = selectedColumn.getCellObservableValue(product);

                if (observableValueToFilter == null) return false;
                return observableValueToFilter.getValue().toString().toLowerCase().contains(lowerCaseFilter);
            });
        });

        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        filterColumnComboBox.setItems(tableView.getColumns());
        filterColumnComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(TableColumn<Product, ?> column) {
                if (column != null) {
                    return column.getText();
                }
                return "WTF";
            }

            @Override
            public TableColumn<Product, ?> fromString(String string) {
                return null;
            }
        });
        filterColumnComboBox.setValue(nameColumn);

    }

    @FXML
    public void add() {
        editController.clear();
        editController.show();
        var product = editController.getProduct();
        if (product != null) {
            product = product.copy(product.getId(), SessionManager.getCurrentUser().getId());

            try {
                var response = (AddResponse) client.sendAndReceiveCommand(new AddRequest(product, SessionManager.getCurrentUser()));
                ValidationHelper.validateResponse(response);

                loadCollection();
                DialogManager.createAlert(
                        localizator.getKeyString("Add"),
                        localizator.getKeyString("AddSuccess"),
                        Alert.AlertType.INFORMATION,
                        false
                );
            } catch (APIException e) {
                DialogManager.alert("AddErr", localizator);
            } catch (IOException e) {
                DialogManager.alert("UnavailableError", localizator);
            }
        }
    }

    @FXML
    void edit() {
        Product selectedProduct = tableView.getSelectionModel().getSelectedItem();
        editProduct(selectedProduct);
    }

    @FXML
    void remove() {
        Product selectedProduct = tableView.getSelectionModel().getSelectedItem();
        if (!Objects.equals(selectedProduct.getCreatorId(), SessionManager.getCurrentUser().getId())) return;

        try {
            RemoveKeyRequest request = new RemoveKeyRequest(selectedProduct.getId(), SessionManager.getCurrentUser());
            RemoveKeyResponse response = (RemoveKeyResponse) client.sendAndReceiveCommand(request);
            ValidationHelper.validateResponse(response);

            loadCollection();
            DialogManager.createAlert(
                    localizator.getKeyString("Remove"),
                    localizator.getKeyString("RemoveSuccess"),
                    Alert.AlertType.INFORMATION,
                    false
            );
        } catch (APIException e) {
            DialogManager.alert("RemoveErr", localizator);
        } catch (IOException e) {
            DialogManager.alert("UnavailableError", localizator);
        }
    }

    @FXML
    public void info() {
        try {
            var response = (InfoResponse) client.sendAndReceiveCommand(new InfoRequest(SessionManager.getCurrentUser()));
            ValidationHelper.validateResponse(response);

            DialogManager.createAlert(
                    localizator.getKeyString("Info"),
                    response.infoMessage,
                    Alert.AlertType.INFORMATION,
                    true
            );

        } catch (APIException | IOException e) {
            DialogManager.alert("UnavailableError", localizator);
        }
    }

    @FXML
    void clear() {
        try {
            ClearRequest request = new ClearRequest(SessionManager.getCurrentUser());
            ClearResponse response = (ClearResponse) client.sendAndReceiveCommand(request);
            ValidationHelper.validateResponse(response);

            loadCollection();
            DialogManager.createAlert(
                    localizator.getKeyString("Clear"),
                    localizator.getKeyString("ClearSuccess"),
                    Alert.AlertType.INFORMATION,
                    false
            );

        } catch (APIException e) {
            DialogManager.alert("ClearErr", localizator);
        } catch (IOException e) {
            DialogManager.alert("UnavailableError", localizator);
        }
    }

    @FXML
    public void executeScript() {
        var chooser = new FileChooser();
        chooser.setInitialDirectory(new File("."));
        var file = chooser.showOpenDialog(stage);

        if (file != null) {
            var result = (new ScriptManager(client, new StandardConsole())).run(file.getAbsolutePath());
            if (result == ScriptManager.ExitCode.ERROR) {
                DialogManager.alert("ExecuteScriptErr", localizator);
            } else {
                DialogManager.info("ExecuteScriptSuccess", localizator);
            }
        }
    }

    @FXML
    void removeGreater() {
        editController.clear();
        editController.show();

        Product product = editController.getProduct();
        if (product != null) {
            product = product.copy(product.getId(), SessionManager.getCurrentUser().getId());

            try {
                RemoveGreaterRequest request = new RemoveGreaterRequest(product, SessionManager.getCurrentUser());
                RemoveGreaterResponse response = (RemoveGreaterResponse) client.sendAndReceiveCommand(request);
                ValidationHelper.validateResponse(response);

                loadCollection();
                DialogManager.createAlert(
                        localizator.getKeyString("RemoveGreater"),
                        localizator.getKeyString("RemoveGreaterSuccess"),
                        Alert.AlertType.INFORMATION,
                        false
                );

            } catch (APIException e) {
                DialogManager.alert("RemoveGreaterErr", localizator);
            } catch (IOException e) {
                DialogManager.alert("UnavailableError", localizator);
            }
        }
    }

    @FXML
    void removeLower() {
        editController.clear();
        editController.show();

        Product product = editController.getProduct();
        if (product != null) {
            product = product.copy(product.getId(), SessionManager.getCurrentUser().getId());

            try {
                RemoveLowerRequest request = new RemoveLowerRequest(product, SessionManager.getCurrentUser());
                RemoveLowerResponse response = (RemoveLowerResponse) client.sendAndReceiveCommand(request);
                ValidationHelper.validateResponse(response);

                loadCollection();
                DialogManager.createAlert(
                        localizator.getKeyString("RemoveLower"),
                        localizator.getKeyString("RemoveLowerSuccess"),
                        Alert.AlertType.INFORMATION,
                        false
                );

            } catch (APIException e) {
                DialogManager.alert("RemoveLowerErr", localizator);
            } catch (IOException e) {
                DialogManager.alert("UnavailableError", localizator);
            }
        }
    }

    @FXML
    void removeGreaterKey() {
        Optional<String> input = DialogManager.createDialog(localizator.getKeyString("RemoveGreaterKey"), "ID: ");

        if (input.isPresent() && !input.get().isEmpty()) {
            try {
                int id = Integer.parseInt(input.orElse(""));
                RemoveGreaterKeyRequest request = new RemoveGreaterKeyRequest((long) id, SessionManager.getCurrentUser());
                RemoveGreaterKeyResponse response = (RemoveGreaterKeyResponse) client.sendAndReceiveCommand(request);
                ValidationHelper.validateResponse(response);

                loadCollection();
                DialogManager.createAlert(
                        localizator.getKeyString("RemoveGreaterKey"),
                        localizator.getKeyString("RemoveGreaterKeySuccess"),
                        Alert.AlertType.INFORMATION,
                        false
                );

            } catch (APIException e) {
                DialogManager.alert("RemoveGreaterKeyErr", localizator);
            } catch (IOException e) {
                DialogManager.alert("UnavailableError", localizator);
            }
        }
    }

    private void loadCollection(boolean redrawAnyway) {
        try {
            ShowRequest request = new ShowRequest(SessionManager.getCurrentUser());
            ShowResponse response = (ShowResponse) client.sendAndReceiveCommand(request);
            ValidationHelper.validateResponse(response);

            List<Product> loadedData = response.products;
            List<Product> currentData = masterData.stream().toList();

            List<Product> intersection = new ArrayList<>(loadedData);
            intersection.retainAll(currentData);

            List<Product> difference = new ArrayList<>(
                    Stream.of(loadedData, currentData)
                            .flatMap(Collection::stream)
                            .toList());
            difference.removeAll(intersection);

            setCollection(response.products);

            boolean dataHasChanged = !difference.isEmpty();
            if (dataHasChanged || redrawAnyway) {
                visualise(false);
            }

        } catch (APIException | IOException e) {
            DialogManager.alert("UnavailableError", localizator);
        }
    }

    private void loadCollection() {
        loadCollection(false);
    }

    public void setCollection(List<Product> collection) {
        this.masterData.setAll(collection);

        // Preserve sorting and set collection
        ObservableList<TableColumn<Product, ?>> selectedSorting = FXCollections.observableArrayList(tableView.getSortOrder());
        tableView.setItems(sortedData);
        tableView.getSortOrder().setAll(selectedSorting);

        // Restore selected row
        if (selectedProduct != null) tableView.getSelectionModel().select(selectedProduct);
    }

    private void editProduct(Product product) {
        if (!Objects.equals(product.getCreatorId(), SessionManager.getCurrentUser().getId())) return;

        editController.fill(product);
        editController.show();

        Product updatedProduct = editController.getProduct();
        if (updatedProduct != null) {
            updatedProduct = updatedProduct.copy(product.getId(), SessionManager.getCurrentUser().getId());

            if (product.getManufacturer() != null && updatedProduct.getManufacturer() != null) {
                updatedProduct.getManufacturer().setId(product.getManufacturer().getId());
            }

            try {
                UpdateRequest request = new UpdateRequest(product.getId(), updatedProduct, SessionManager.getCurrentUser());
                UpdateResponse response = (UpdateResponse) client.sendAndReceiveCommand(request);
                ValidationHelper.validateResponse(response);

                loadCollection();
                DialogManager.createAlert(
                        localizator.getKeyString("Update"),
                        localizator.getKeyString("UpdateSuccess"),
                        Alert.AlertType.INFORMATION,
                        false
                );
            } catch (APIException e) {
                DialogManager.createAlert(
                        localizator.getKeyString("Error"),
                        localizator.getKeyString("UpdateErr") + "\n%s".formatted(e.getMessage()),
                        Alert.AlertType.ERROR,
                        false
                );
            } catch (IOException e) {
                DialogManager.alert("UnavailableError", localizator);
            }
        }
    }

    public void visualise(boolean isActivatedByUser) {
        visualPane.getChildren().clear();
        infoMap.clear();

        for (Product product : tableView.getItems()) {
            // Defining color
            if (!colorsMap.containsKey(product.getCreatorId())) {
                var r = random.nextDouble();
                var g = random.nextDouble();
                var b = random.nextDouble();
                if (Math.abs(r - g) + Math.abs(r - b) + Math.abs(b - g) < 0.6) {
                    r += (1 - r) / 1.4;
                    g += (1 - g) / 1.4;
                    b += (1 - b) / 1.4;
                }
                colorsMap.put(product.getCreatorId(), Color.color(r, g, b));
            }

            // Defining radius
            double radius = Math.log(product.getPrice()) * 7;
            radius = Math.max(15, Math.min(radius, 100));

            // Defining position
            int x = Math.abs(product.getCoordinates().getX());
            x = Math.max(50, Math.min(x, 800));

            long y = Math.abs(product.getCoordinates().getY());
            y = Math.max(50, Math.min(y, 350));

            // Circle
            Circle circle = new Circle(radius, colorsMap.get(product.getCreatorId()));
            circle.setCenterX(x);
            circle.setCenterY(y);
            visualPane.getChildren().add(circle);

            // Id
            Text id = new Text('#' + String.valueOf(product.getId()));
            id.setFont(Font.font("Segoe UI", radius / 2));
            id.setX(circle.getCenterX() - id.getLayoutBounds().getWidth() / 2);
            id.setY(circle.getCenterY() + id.getLayoutBounds().getHeight() / 4);
            visualPane.getChildren().add(id);

            // Info
            Label info = new Label(new ProductRenderer(localizator).describe(product));
            info.setStyle("-fx-background-color: white; -fx-border-color: #c0c0c0; -fx-border-width: 2");
            info.setFont(Font.font("Segoe UI", 15));
            info.setLayoutX(circle.getCenterX() - id.getLayoutBounds().getWidth() / 2);
            info.setLayoutY(circle.getCenterY() + id.getLayoutBounds().getHeight() / 4);
            info.setTranslateZ(1);
            info.setVisible(false);

            infoMap.put(product.getId(), info);

            // Events
            circle.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2) {
                    editProduct(product);
                }
            });

            Arrays.asList(circle, id, info).forEach(node -> node.setOnMouseEntered(mouseEvent -> {
                info.setVisible(true);
                circle.setFill(colorsMap.get(product.getCreatorId()).brighter());
            }));

            Arrays.asList(circle, id, info).forEach(node -> node.setOnMouseExited(mouseEvent -> {
                info.setVisible(false);
                circle.setFill(colorsMap.get(product.getCreatorId()));
            }));

            // Animation
            if (isActivatedByUser) {
                Duration animationDuration = Duration.millis(600);

                ScaleTransition scaleTransition = new ScaleTransition(animationDuration, circle);
                scaleTransition.setFromX(0.2);
                scaleTransition.setFromY(0.2);
                scaleTransition.setToX(1);
                scaleTransition.setToY(1);
                scaleTransition.setCycleCount(1);

                FadeTransition idFadeTransition = new FadeTransition(animationDuration, id);
                idFadeTransition.setFromValue(0.1);
                idFadeTransition.setToValue(1.0);
                idFadeTransition.setCycleCount(1);

                scaleTransition.play();
                idFadeTransition.play();
            }
        }

        for (Label infoLabel : infoMap.values()) {
            visualPane.getChildren().add(infoLabel);
        }
    }

    public void startRefreshing() {
        Thread refresher = new Thread(() -> {
            while (isRefreshing) {
                Platform.runLater(this::loadCollection);
                try {
                    Thread.sleep(refreshingRate);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                    System.out.println("Thread was interrupted, Failed to complete operation");
                }
            }
        });
        refresher.start();
    }

    @Override
    public void changeLanguage() {
        // Title
        mainTitleLabel.setText(localizator.getKeyString("MainTitle"));

        // Tabs
        tableTab.setText(localizator.getKeyString("TableTab"));
        visualizationTab.setText(localizator.getKeyString("VisualizationTab"));

        // Buttons
        othersButton.setText(localizator.getKeyString("Others"));
        infoButton.setText(localizator.getKeyString("Info"));
        clearButton.setText(localizator.getKeyString("Clear"));
        executeScriptButton.setText(localizator.getKeyString("ExecuteScript"));
        removeGreaterButton.setText(localizator.getKeyString("RemoveGreater"));
        removeLowerButton.setText(localizator.getKeyString("RemoveLower"));
        removeGreaterKeyButton.setText(localizator.getKeyString("RemoveGreaterKey"));

        // Product columns
        ownerIdColumn.setText(localizator.getKeyString("OwnerId"));
        nameColumn.setText(localizator.getKeyString("Name"));
        dateColumn.setText(localizator.getKeyString("CreationDate"));
        priceColumn.setText(localizator.getKeyString("Price"));
        partNumberColumn.setText(localizator.getKeyString("PartNumber"));
        unitOfMeasureColumn.setText(localizator.getKeyString("UnitOfMeasure"));

        // Manufacturer columns
        manufacturerIdColumn.setText(localizator.getKeyString("ManufacturerId"));
        manufacturerNameColumn.setText(localizator.getKeyString("ManufacturerName"));
        manufacturerFullNameColumn.setText(localizator.getKeyString("ManufacturerFullName"));
        manufacturerAnnualTurnoverColumn.setText(localizator.getKeyString("ManufacturerAnnualTurnover"));
        manufacturerEmployeesCountColumn.setText(localizator.getKeyString("ManufacturerEmployeesCount"));

        var selectedColumn = filterColumnComboBox.getSelectionModel().getSelectedItem();
        filterColumnComboBox.setItems(null);
        filterColumnComboBox.setItems(tableView.getColumns());
        filterColumnComboBox.setValue(selectedColumn);

        editController.changeLanguage();

        loadCollection(true);

    }

    public void setClient(UDPClient client) {
        this.client = client;
    }

    public void setEditController(EditController editController) {
        this.editController = editController;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}