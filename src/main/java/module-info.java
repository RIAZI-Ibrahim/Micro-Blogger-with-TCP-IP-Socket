module groupid {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires Java.WebSocket;

    opens frontend.gui to javafx.fxml;
    exports frontend.gui;
    exports frontend.client;
    opens frontend.client to javafx.fxml;
}
