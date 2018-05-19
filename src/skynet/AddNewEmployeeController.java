package skynet;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddNewEmployeeController {

    @FXML TextField firstName;
    @FXML TextField lastName;
    @FXML TextField username;
    @FXML PasswordField password;
    @FXML TextField email;
    @FXML TextField rank;

    public void saveNewEmployee(Event event){
        DatabaseConnection db = new DatabaseConnection();
        Crypt crypt = new Crypt();

        String password = crypt.encrypt(this.password.getText());

        db.addEmployee(username.getText(), password, firstName.getText(), lastName.getText(), email.getText(), rank.getText());

        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();

    }
}
