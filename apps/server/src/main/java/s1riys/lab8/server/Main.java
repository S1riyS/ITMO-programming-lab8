package s1riys.lab8.server;

import io.github.cdimascio.dotenv.Dotenv;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionFactoryImpl;
import s1riys.lab8.common.managers.CommandManager;
import s1riys.lab8.common.constants.Commands;
import s1riys.lab8.server.commands.*;
import s1riys.lab8.server.handlers.CommandHandler;
import s1riys.lab8.server.managers.AuthManager;
import s1riys.lab8.server.managers.CollectionManager;
import s1riys.lab8.server.network.UDPServer;
import s1riys.lab8.server.utils.HibernateSessionFactoryUtil;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import static s1riys.lab8.common.constants.Network.PORT;

public class Main {
    public static Logger logger = LogManager.getLogger("ServerLogger");
    public static Dotenv dotenv;

    public static void main(String[] args) {
        loadEnv();

        SessionFactory sessionFactory = HibernateSessionFactoryUtil.getSessionFactory();
        Runtime.getRuntime().addShutdownHook(new Thread(sessionFactory::close));

        CollectionManager collectionManager = new CollectionManager();
        collectionManager.startRefreshing();
        AuthManager authManager = new AuthManager();

        CommandManager<Command> serverCommandManager = new CommandManager<>() {{
            register(Commands.REGISTER, new Register(authManager));
            register(Commands.LOGIN, new Login(authManager));
            register(Commands.INFO, new Info(collectionManager));
            register(Commands.SHOW, new Show(collectionManager));
            register(Commands.INSERT, new Insert(collectionManager));
            register(Commands.UPDATE, new Update(collectionManager));
            register(Commands.REMOVE_KEY, new RemoveKey(collectionManager));
            register(Commands.CLEAR, new Clear(collectionManager));
            register(Commands.REMOVE_GREATER, new RemoveGreater(collectionManager));
            register(Commands.REMOVE_LOWER, new RemoveLower(collectionManager));
            register(Commands.REMOVE_GREATER_KEY, new RemoveGreaterKey(collectionManager));
            register(Commands.MAX_BY_CREATION_DATE, new MaxByCreationDate(collectionManager));
        }};
        CommandHandler commandHandler = new CommandHandler(serverCommandManager);

        try {
            UDPServer server = new UDPServer(InetAddress.getLocalHost(), PORT, commandHandler);
            server.run();
        } catch (SocketException e) {
            System.out.println("Ошибка сокета");
        } catch (UnknownHostException e) {
            System.out.println("Неизвестный хост");
        }
    }

    private static void loadEnv() {
        dotenv = Dotenv.configure()
                .filename(".env.dev")
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();
    }
}