package skynet;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML TextField loginUsername;
    @FXML TextField loginPassowrd;
    @FXML Button loginButton;


    @FXML
    public void login(ActionEvent event) throws IOException {
        DatabaseConnection db = new DatabaseConnection();
        boolean goodLogin = db.checkLogin(loginUsername.getText(), loginPassowrd.getText());

        if(goodLogin){
            ((Node)event.getSource()).getScene().getWindow().hide();
            Parent root = FXMLLoader.load(getClass().getResource("UI/main.fxml"));
            Stage mainStage = new Stage();
            Scene mainScene = new Scene(root);
            mainStage.setResizable(false);
            mainStage.setScene(mainScene);
            mainStage.show();
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Incorrect username or password!");
            alert.showAndWait();
        }
    }

    public void signUpButton(Event event){
        try {
            ((Node)event.getSource()).getScene().getWindow().hide();
            Parent root = FXMLLoader.load(getClass().getResource("UI/signup.fxml"));
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
