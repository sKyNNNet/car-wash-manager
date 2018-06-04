package skynet;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddNewAccountController implements Initializable {

    @FXML JFXTextField firstName;
    @FXML JFXTextField lastName;
    @FXML JFXTextField email;
    @FXML JFXTextField username;
    @FXML JFXPasswordField password;
    @FXML JFXComboBox rank;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //allow only alphanumeric input
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\sa-zA-Z0-9*")) {
                username.setText(newValue.replaceAll("[^\\sa-zA-Z0-9]", ""));
            }
        });

        rank.getItems().addAll("CEO", "Assistant", "Car Washer", "unranked");
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

    public void addNewAccount(Event event){
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

        if(password.getText().isEmpty()){
            new Popup(Alert.AlertType.WARNING, "Error", "Password can't be empty");
            return;
        }

        if(password.getText().length() <=5){
            new Popup(Alert.AlertType.INFORMATION, "Info", "Password must be at least 6 characters long.");
            return;
        }

        if(Utility.validEmail(email.getText())){
            DatabaseConnection db = new DatabaseConnection();
            Crypt crypt = new Crypt();

            String encryptedPassword = crypt.encrypt(password.getText());

            if(rank.getValue() == null)
                rank.setValue("unranked");

            db.addUser(username.getText(), encryptedPassword, firstName.getText(), lastName.getText(), email.getText(), rank.getValue().toString());

            ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        } else {
            new Popup(Alert.AlertType.WARNING, "Error", "Invalid email format!");
        }
    }

}
