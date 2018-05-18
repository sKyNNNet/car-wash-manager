package skynet;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditEmployeeController {

    @FXML TextField firstName;
    @FXML TextField lastName;
    @FXML TextField username;
    @FXML TextField email;
    @FXML TextField rank;
    @FXML Label editEmployeeTextTitle;

    private DatabaseConnection db = new DatabaseConnection();

    public void editEmployeeSaveButton(Event event){
        int id = Integer.parseInt(editEmployeeTextTitle.getText().split("#")[1]);

        db.editEmployee(id, username.getText(), firstName.getText(), lastName.getText(), email.getText(), rank.getText());

        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();

    }

    public void editEmployeeDeleteButton(Event event){
        int id = Integer.parseInt(editEmployeeTextTitle.getText().split("#")[1]);

        db.deleteById("users", id);

        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }
}
