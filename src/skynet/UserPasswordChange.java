package skynet;

import com.jfoenix.controls.JFXPasswordField;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class UserPasswordChange {

    @FXML JFXPasswordField oldPassword;
    @FXML JFXPasswordField newPassword;
    @FXML JFXPasswordField confirmNewPassword;
    @FXML Label userIdLabel;

    private Crypt crypt = new Crypt();
    private DatabaseConnection db = new DatabaseConnection();

    public void savePasswordChangeButton(Event event){
        int userId = Integer.valueOf(userIdLabel.getText());
        String encryptedPassword = db.getPasswordById(userId);

        if(!crypt.decrypt(encryptedPassword).equals(oldPassword.getText())){
            new Popup(Alert.AlertType.ERROR, "Error", "Wrong old password, please try again!");
            return;
        }

        if(!newPassword.getText().equals(confirmNewPassword.getText())){
            new Popup(Alert.AlertType.WARNING, "Error", "The passwords do not match, please try again!");
            return;
        } else if(newPassword.getText().length() <= 5){
            new Popup(Alert.AlertType.WARNING, "Error", "The new password must be at least 6 characters long!");
            return;
        }

        db.updatePassword(userId, crypt.encrypt(newPassword.getText()));
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();

    }

    public void passwordChangeCancelButton(Event event){
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }
}
