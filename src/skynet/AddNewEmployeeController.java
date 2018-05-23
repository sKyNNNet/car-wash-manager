package skynet;

import com.jfoenix.controls.JFXComboBox;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddNewEmployeeController implements Initializable {

    @FXML TextField firstName;
    @FXML TextField lastName;
    @FXML TextField username;
    @FXML PasswordField password;
    @FXML TextField email;
    @FXML JFXComboBox rank;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rank.getItems().addAll("CEO", "Assistant", "Car Washer", "unranked");


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

    public void saveNewEmployee(Event event){

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

        if(Utility.validEmail(email.getText())){
            DatabaseConnection db = new DatabaseConnection();
            Crypt crypt = new Crypt();

            String encryptedPassword = crypt.encrypt(this.password.getText());

            db.addEmployee(username.getText(), encryptedPassword, firstName.getText(), lastName.getText(), email.getText(), rank.getValue().toString());

            if(password.getText().isEmpty()){
                new Popup(Alert.AlertType.WARNING, "Info", "User " + username.getText() + " has no password set.");
            }

            ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        } else {
            new Popup(Alert.AlertType.WARNING, "Error", "Invalid email format!");
        }
    }

}
