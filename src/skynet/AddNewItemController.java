package skynet;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class AddNewItemController implements Initializable {

    @FXML TextField name;
    @FXML TextField quantity;
    @FXML TextField unit;
    @FXML TextField supplier;
    @FXML TextField pricePerUnit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //allow only int / double type as input for price and quantity
        Pattern pattern = Pattern.compile("\\d*|\\d+\\.\\d*");

        pricePerUnit.setTextFormatter(new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        }));

        quantity.setTextFormatter(new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        }));
    }

    public void saveNewItem(Event event){

        if(name.getText().isEmpty()){
            new Popup(Alert.AlertType.INFORMATION, "Info", "Product name can't be empty.");
            return;
        }

        if(quantity.getText().isEmpty()){
            new Popup(Alert.AlertType.INFORMATION, "Info", "Quantity volume is set to 0.");
        }

        if(supplier.getText().isEmpty()){
            new Popup(Alert.AlertType.INFORMATION, "Info", "Supplier name is empty.");
        }

        if(unit.getText().isEmpty()){
            new Popup(Alert.AlertType.ERROR, "Error", "Unit type can't be empty.");
            return;
        }

        DatabaseConnection db = new DatabaseConnection();

        db.addNewItem(name.getText(), Integer.parseInt(quantity.getText()), unit.getText(), supplier.getText(), Double.parseDouble(pricePerUnit.getText()));

        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();

    }
}
