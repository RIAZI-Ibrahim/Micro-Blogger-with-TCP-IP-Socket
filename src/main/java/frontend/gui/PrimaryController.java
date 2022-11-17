package frontend.gui;

import java.io.IOException;

import frontend.client.Authenticator;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class PrimaryController {

    @FXML
    private Text serverMsg;
    @FXML
    private TextField nameField;
    @FXML
    private TextField passField;



    @FXML
    private void switchToSecondary() {
        try {
            App.setRoot("secondary");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void login() {
        String token = Authenticator.getToken(nameField.getText(),passField.getText());
        if(token.startsWith("ERROR!"))serverMsg.setText(token);
        else{
            ControllerDataHolder.getInstance().setUserName(nameField.getText());
            switchToSecondary();
        }
    }
}
