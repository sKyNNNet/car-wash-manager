package skynet;

import com.jfoenix.controls.JFXTextField;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoggedInUserSettings implements Initializable {

    @FXML JFXTextField firstName;
    @FXML JFXTextField lastName;
    @FXML JFXTextField email;
    @FXML Label welcomeLabel;
    @FXML Label userIdLabel;

    DatabaseConnection db = new DatabaseConnection();
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

    public void changePasswordButtonClick(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("UI/userPasswordChange.fxml"));

            Parent root = fxmlLoader.load();

            UserPasswordChange controller = fxmlLoader.getController();

            controller.userIdLabel.setText(userIdLabel.getText());

            Stage mainStage = new Stage();
            Scene mainScene = new Scene(root);
            mainStage.getIcons().add(new Image(getClass().getResourceAsStream("../logo/appicon.png")));
            mainStage.setResizable(false);
            mainStage.setScene(mainScene);
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            int id = Integer.parseInt(userIdLabel.getText());
            db.updateUserSettings(id, firstName.getText(), lastName.getText(), email.getText());

            ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        }

    }

}
