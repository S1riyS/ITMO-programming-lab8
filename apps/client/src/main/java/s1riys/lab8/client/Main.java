package s1riys.lab8.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import s1riys.lab8.client.console.StandardConsole;
import s1riys.lab8.client.controllers.AuthController;
import s1riys.lab8.client.controllers.EditController;
import s1riys.lab8.client.controllers.MainController;
import s1riys.lab8.client.network.UDPClient;
import s1riys.lab8.client.utils.Localizator;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import static s1riys.lab8.common.constants.Network.PORT;

public class Main extends Application {
    public static Logger logger = LogManager.getLogger("ServerLogger");

    public static UDPClient client;
    private Stage mainStage;
    private static Localizator defaultLocalizator;

    public static void main(String[] args) {
        var console = new StandardConsole();

        try {
            client = new UDPClient(InetAddress.getLocalHost(), PORT);
            defaultLocalizator = new Localizator(ResourceBundle.getBundle(
                    "locales/gui",
                    new Locale("ru", "RU"))
            );
            launch(args);

        } catch (UnknownHostException e) {
            System.out.println("Неизвестный хост");
            logger.error("Неизвестный хост", e);
        } catch (IOException e) {
            System.out.println("Невозможно подключиться к серверу!");
            logger.error("Невозможно подключиться к серверу!", e);
        }
    }

    @Override
    public void start(Stage stage) {
        mainStage = stage;
        applyAppIcon(mainStage);
        startAuth();
    }

    private void startAuth() {
        FXMLLoader authLoader = new FXMLLoader(getClass().getResource("/auth.fxml"));
        Parent authRoot = loadFxml(authLoader);

        AuthController authController = authLoader.getController();
        authController.setLocalizator(defaultLocalizator);
        authController.setClient(client);
        authController.setCallback(this::startMain);

        mainStage.setScene(new Scene(authRoot));
        mainStage.setTitle("Authorization");
        mainStage.setResizable(false);
        mainStage.show();
    }

    private void startMain() {
        // Edit scene
        var editLoader = new FXMLLoader(getClass().getResource("/edit.fxml"));
        var editRoot = loadFxml(editLoader);

        var editScene = new Scene(editRoot);
        var editStage = new Stage();

        applyAppIcon(editStage);
        editStage.setTitle("Product Editor");
        editStage.setScene(editScene);
        editStage.setResizable(false);
        editStage.setTitle("Products");
        EditController editController = editLoader.getController();

        editController.setStage(editStage);
        editController.setLocalizator(defaultLocalizator);

        // Main scene
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/main.fxml"));
        Parent mainRoot = loadFxml(mainLoader);

        MainController mainController = mainLoader.getController();
        mainController.setLocalizator(defaultLocalizator);
        mainController.setClient(client);
        mainController.setEditController(editController);
        mainController.setStage(mainStage);
        mainController.startRefreshing();

        mainStage.setScene(new Scene(mainRoot));
        mainStage.setTitle("Product management");
        mainStage.setResizable(false);
        mainStage.show();
    }

    private Parent loadFxml(FXMLLoader loader) {
        Parent parent = null;
        try {
            parent = loader.load();
        } catch (IOException e) {
            logger.error("Can't load " + loader.toString(), e);
            System.exit(1);
        }
        return parent;
    }

    private void applyAppIcon(Stage stage) {
//        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/app.png")));
//        stage.getIcons().add(icon);
    }
}
