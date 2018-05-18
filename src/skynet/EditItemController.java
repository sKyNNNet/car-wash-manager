package skynet;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditItemController {

    @FXML TextField name;
    @FXML TextField quantity;
    @FXML TextField unit;
    @FXML TextField supplier;
    @FXML TextField pricePerUnit;
    @FXML Label editItemTextTitle;

    private DatabaseConnection db = new DatabaseConnection();

    public void saveSelectedItemButton(Event event){
        int id = Integer.parseInt(editItemTextTitle.getText().split("#")[1]);

        db.editItem(id, name.getText(), Integer.parseInt(quantity.getText()), unit.getText(), supplier.getText(), Double.parseDouble(pricePerUnit.getText()));

        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();

    }

    public void deleteSelectedItemButton(Event event){
        int id = Integer.parseInt(editItemTextTitle.getText().split("#")[1]);
        
        db.deleteById("inventory", id);

        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }

}

