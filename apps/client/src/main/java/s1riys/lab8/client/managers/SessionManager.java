package s1riys.lab8.client.managers;

import s1riys.lab8.common.models.User;

public class SessionManager {
    private static User currentUser = null;
    public static String currentLanguage = "Русский";

    public static User getCurrentUser() {
        return SessionManager.currentUser;
    }

    public static void setCurrentUser(User user) {
        SessionManager.currentUser = user;
    }

    public static String getCurrentLanguage() {
        return currentLanguage;
    }

    public static void setCurrentLanguage(String currentLanguage) {
        SessionManager.currentLanguage = currentLanguage;
    }
}
