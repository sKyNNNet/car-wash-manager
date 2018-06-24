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
    @FXML TextField email;
    @FXML JFXComboBox rank;
    private String loginRank;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginRank = MainController.loginRank;

        if(loginRank.equals("CEO")){
            rank.getItems().addAll("CEO", "Assistant", "Car Washer", "unranked");
        } else {
            rank.getItems().addAll("Assistant", "Car Washer", "unranked");
        }

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

        if(Utility.validEmail(email.getText())){
            DatabaseConnection db = new DatabaseConnection();

            if(rank.getValue() == null)
                rank.setValue("unranked");

            db.addEmployee(firstName.getText(), lastName.getText(), email.getText(), rank.getValue().toString());

            ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        } else {
            new Popup(Alert.AlertType.WARNING, "Error", "Invalid email format!");
        }
    }

}
