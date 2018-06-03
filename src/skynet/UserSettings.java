package skynet;
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
    @FXML JFXTextField email;
    @FXML Label details;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

        if(Utility.validEmail(email.getText())) {
            int id = Integer.parseInt(details.getText().split("#")[1]);

            DatabaseConnection db = new DatabaseConnection();
            db.updateUserSettings(id, firstName.getText(), lastName.getText(), email.getText());
        }

        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }

    public void cancelUserSettingsButton(Event event){
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }
}
