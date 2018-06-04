package skynet;

import com.jfoenix.controls.JFXComboBox;
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

public class EditAccountController implements Initializable {

    @FXML JFXTextField firstName;
    @FXML JFXTextField lastName;
    @FXML JFXTextField email;
    @FXML JFXTextField username;
    @FXML JFXPasswordField password;
    @FXML JFXComboBox rank;
    @FXML Label editAccountTextTitle;

    DatabaseConnection db = new DatabaseConnection();
    Crypt crypt = new Crypt();

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

    public void editAccountSaveButton(Event event){
        if(firstName.getText().isEmpty()){
            new Popup(Alert.AlertType.WARNING, "Error", "First name can't be empty");
            return;
        }

        if(lastName.getText().isEmpty()){
            new Popup(Alert.AlertType.WARNING, "Error", "Last name can't be empty");
            return;
        }

        if(username.getText().isEmpty()){
            new Popup(Alert.AlertType.WARNING, "Error", "Last name can't be empty");
            return;
        }
        if(Utility.validEmail(email.getText())) {
            int id = Integer.parseInt(editAccountTextTitle.getText().split("#")[1]);

            if(password.getText().isEmpty()){
                db.editAccount(id,username.getText(), firstName.getText(), lastName.getText(), email.getText(), rank.getValue().toString());

                ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
            }

            if(!password.getText().isEmpty()){
                if(password.getText().length() <=5){
                    new Popup(Alert.AlertType.INFORMATION, "Info", "Password must be at least 6 characters long.");
                    return;
                }else {
                    String encryptedPassword = crypt.encrypt(password.getText());
                    db.editAccount(id,username.getText(), encryptedPassword, firstName.getText(), lastName.getText(), email.getText(), rank.getValue().toString());

                    ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
                }

            }
        }

        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }

    public void editAccountDeleteButton(Event event){
        int id = Integer.parseInt(editAccountTextTitle.getText().split("#")[1]);

        db.deleteById("users", id);

        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }
}
