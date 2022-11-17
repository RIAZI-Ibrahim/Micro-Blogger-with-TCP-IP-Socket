package frontend.gui;

public class ControllerDataHolder {
    private static volatile ControllerDataHolder instance;
    private static Object lock = new Object();
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private ControllerDataHolder() {
    }

    public static ControllerDataHolder getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (lock) {
            if (instance == null) {
                instance = new ControllerDataHolder();
            }

            return instance;
        }
    }

}
