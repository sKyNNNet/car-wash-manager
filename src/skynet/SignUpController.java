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
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {

    @FXML
    JFXTextField username;
    @FXML
    JFXTextField email;
    @FXML
    PasswordField password;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //allow only alphanumeric input for username
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^[a-zA-Z0-9]*$")) {
                username.setText(newValue.replaceAll("[^\\sa-zA-Z0-9]*$", ""));
            }
        });

    }

    public void signUpButton(Event event) {

        if(username.getText().isEmpty()){
            new Popup(Alert.AlertType.WARNING, "Error", "Username can't be empty");

            return;
        }

        if(password.getText().isEmpty()){
            new Popup(Alert.AlertType.WARNING, "Error", "Password can't be empty");
            return;
        }


        DatabaseConnection db = new DatabaseConnection();
        Crypt crypt = new Crypt();

        if (!Utility.validEmail(email.getText())) {
           new Popup(Alert.AlertType.WARNING, "Error", "Incorrect email format");

        } else {
            try {
                db.addUser(username.getText(), crypt.encrypt(password.getText()), "unknown", "unknown", email.getText(), "unranked");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);

                alert.setTitle("Account info");
                alert.setHeaderText(null);
                alert.setContentText("Account successfully created!");
                alert.showAndWait();

                ((Node) event.getSource()).getScene().getWindow().hide();
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("UI/login.fxml"));

                Parent root = fxmlLoader.load();

                Stage mainStage = new Stage();
                Scene mainScene = new Scene(root);
                mainStage.setResizable(false);
                mainStage.setScene(mainScene);
                mainStage.show();
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }

    public void alreadyMemberButton(Event event) {
        try {
            ((Node) event.getSource()).getScene().getWindow().hide();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("UI/login.fxml"));

            Parent root = fxmlLoader.load();

            Stage mainStage = new Stage();
            Scene mainScene = new Scene(root);
            mainStage.setResizable(false);
            mainStage.setScene(mainScene);
            mainStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
