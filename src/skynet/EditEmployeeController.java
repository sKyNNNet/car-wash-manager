package skynet;

import com.jfoenix.controls.JFXComboBox;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EditEmployeeController implements Initializable {

    @FXML TextField firstName;
    @FXML TextField lastName;
    @FXML TextField email;
    @FXML JFXComboBox rank;
    @FXML Label editEmployeeTextTitle;

    private DatabaseConnection db = new DatabaseConnection();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

    public void editEmployeeSaveButton(Event event){
        if(firstName.getText().isEmpty()){
            new Popup(Alert.AlertType.WARNING, "Error", "First name can't be empty");
            return;
        }

        if(lastName.getText().isEmpty()){
            new Popup(Alert.AlertType.WARNING, "Error", "Last name can't be empty");
            return;
        }
        if(Utility.validEmail(email.getText())) {
            int id = Integer.parseInt(editEmployeeTextTitle.getText().split("#")[1]);

            db.editEmployee(id,firstName.getText(), lastName.getText(), email.getText(), rank.getValue().toString());

            ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        }

        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();

    }

    public void editEmployeeDeleteButton(Event event){
        int id = Integer.parseInt(editEmployeeTextTitle.getText().split("#")[1]);

        db.deleteById("employee", id);

        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }

}
