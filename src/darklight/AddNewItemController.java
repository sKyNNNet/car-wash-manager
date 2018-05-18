package darklight;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddNewItemController {

    @FXML TextField name;
    @FXML TextField quantity;
    @FXML TextField unit;
    @FXML TextField supplier;
    @FXML TextField pricePerUnit;

    public void saveNewItem(Event event){
        DatabaseConnection db = new DatabaseConnection();

        db.addNewItem(name.getText(), Integer.parseInt(quantity.getText()), unit.getText(), supplier.getText(), Double.parseDouble(pricePerUnit.getText()));

        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();

    }
}
