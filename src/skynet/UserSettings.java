package skynet;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class UserSettings implements Initializable {

    @FXML
    JFXTextField firstName;
    @FXML JFXTextField lastName;
    @FXML JFXTextField username;
    @FXML JFXTextField email;
    @FXML JFXPasswordField newPassword;
    @FXML JFXPasswordField confirmNewPassword;
    @FXML Label details;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //allow only alphanumeric input for username
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^[a-zA-Z0-9]*$")) {
                username.setText(newValue.replaceAll("[^\\sa-zA-Z0-9]*$", ""));
            }
        });

        //allow only letter for first name / last name
        firstName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\sa-zA-Z*")) {
                firstName.setText(newValue.replaceAll("[^\\sa-zA-Z]", ""));
            }
        });

        lastName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\sa-zA-Z*")) {
                lastName.setText(newValue.replaceAll("[^\\sa-zA-Z]", ""));
            }
        });
    }

    public void saveSettingsButtonClick(Event event){
        if(firstName.getText().isEmpty()){
            new Popup(Alert.AlertType.WARNING, "Error", "First name can't be empty");
            return;
        }

        if(lastName.getText().isEmpty()){
            new Popup(Alert.AlertType.WARNING, "Error", "Last name can't be empty");
            return;
        }

        if(username.getText().isEmpty()){
            new Popup(Alert.AlertType.WARNING, "Error", "Username can't be empty");
            return;
        }

        if(Utility.validEmail(email.getText())) {
            int id = Integer.parseInt(details.getText().split("#")[1]);

            DatabaseConnection db = new DatabaseConnection();

            if(!newPassword.getText().isEmpty() && newPassword.getText().equals(confirmNewPassword.getText())){
                Crypt crypt = new Crypt();
                db.updateUserSettings(id, firstName.getText(), lastName.getText(), username.getText(), email.getText(), crypt.encrypt(newPassword.getText()));
            } else if (newPassword.getText().isEmpty()){
                db.updateUserSettings(id, firstName.getText(), lastName.getText(), username.getText(), email.getText());

            }

            ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        } else {
            if(username.getText().isEmpty()){
                new Popup(Alert.AlertType.WARNING, "Error", "Invalid email format!");
            }
        }

        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }
}
