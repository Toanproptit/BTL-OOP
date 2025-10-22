package model;

public class SessionManager {
    private static Account currentUser;

    public static void setCurrentUser(Account acc) { currentUser = acc; }
    public static Account getCurrentUser() { return currentUser; }

    public static boolean isAdmin() {
        return currentUser != null && currentUser.getRole() == Account.Role.ADMIN;
    }
    public static void clear() { currentUser = null; }
}
