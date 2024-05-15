package s1riys.lab8.client.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import s1riys.lab8.client.forms.RegisterForm;
import s1riys.lab8.client.network.UDPClient;
import s1riys.lab8.client.utils.ValidationHelper;
import s1riys.lab8.client.managers.DialogManager;
import s1riys.lab8.client.managers.SessionManager;
import s1riys.lab8.common.exceptions.APIException;
import s1riys.lab8.common.models.User;
import s1riys.lab8.common.network.requests.LoginRequest;
import s1riys.lab8.common.network.requests.RegisterRequest;
import s1riys.lab8.common.network.responses.LoginResponse;
import s1riys.lab8.common.network.responses.RegisterResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AuthController extends LocalizedController {
    private UDPClient client;
    private Runnable callback;

    @FXML
    private Button logInButton;

    @FXML
    private ComboBox<String> languageComboBox;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signUpButton;

    @FXML
    private Label titleLabel;

    @FXML
    void initialize() {
        languageComboBox.setItems(FXCollections.observableArrayList(localeMap.keySet()));

        languageComboBox.setValue(SessionManager.getCurrentLanguage());
        languageComboBox.setStyle("-fx-font: 13px \"Sergoe UI\";");
        languageComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            localizator.setBundle(ResourceBundle.getBundle("locales/gui", localeMap.get(newValue)));
            SessionManager.setCurrentLanguage(newValue);
            changeLanguage();
        });

        loginField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue.matches(".{0,30}")) {
                loginField.setText(oldValue);
            }
        });

        passwordField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue.matches("\\S*")) {
                passwordField.setText(oldValue);
            }
        });
    }

    @FXML
    void login() {
        try {
            if (!validateCredentials()) return;

            User potentialUser = new User(-1, loginField.getText(), passwordField.getText());

            LoginRequest request = new LoginRequest(potentialUser);
            LoginResponse response = (LoginResponse) client.sendAndReceiveCommand(request);
            ValidationHelper.validateResponse(response);

            SessionManager.setCurrentUser(response.user);
            SessionManager.setCurrentLanguage(languageComboBox.getValue());
            callback.run();

        } catch (APIException e) {
            DialogManager.alert("LogInError", localizator);
        } catch (IOException e) {
            DialogManager.alert("UnavailableError", localizator);
        }
    }

    @FXML
    void signUp() {
        try {
            if (!validateCredentials()) return;

            User newUser = new User(-1, loginField.getText(), passwordField.getText());

            RegisterRequest request = new RegisterRequest(newUser);
            RegisterResponse response = (RegisterResponse) client.sendAndReceiveCommand(request);
            ValidationHelper.validateResponse(response);

            SessionManager.setCurrentUser(response.user);
            SessionManager.setCurrentLanguage(languageComboBox.getValue());

            callback.run();

        } catch (APIException e) {
            DialogManager.alert("SignUpError", localizator);
        } catch (IOException e) {
            DialogManager.alert("UnavailableError", localizator);
        }
    }

    private boolean validateCredentials() {
        List<String> errors = new ArrayList<>();
        errors.addAll(validateLogin(loginField.getText()));
        errors.addAll(validatePassword(passwordField.getText()));

        if (errors.isEmpty()) return true;

        StringBuilder message = new StringBuilder();
        for (String error : errors) message.append(error).append("\n");
        DialogManager.createAlert(
                localizator.getKeyString("Error"),
                message.toString(),
                Alert.AlertType.ERROR,
                false
        );
        return false;

    }

    @Override
    public void changeLanguage() {
        titleLabel.setText(localizator.getKeyString("AuthTitle"));
        loginField.setPromptText(localizator.getKeyString("LoginField"));
        passwordField.setPromptText(localizator.getKeyString("PasswordField"));
        logInButton.setText(localizator.getKeyString("LogInButton"));
        signUpButton.setText(localizator.getKeyString("SignUpButton"));
    }

    private List<String> validateLogin(String login) {
        List<String> errors = new ArrayList<String>();

        // TODO: localize
        if (login.trim().isEmpty()) errors.add("Логин не может быть пустым");
        return errors;
    }

    private List<String> validatePassword(String password) {
        List<String> errors = new ArrayList<String>();

        // TODO: localize
        if (password.trim().isEmpty()) errors.add("Пароль не может быть пустым");
        return errors;
    }

    public void setClient(UDPClient client) {
        this.client = client;
    }

    public void setCallback(Runnable callback) {
        this.callback = callback;
    }
}
