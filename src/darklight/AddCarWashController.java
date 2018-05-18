package darklight;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;


public class AddCarWashController {

    @FXML RadioButton interiorCleaning;
    @FXML RadioButton exteriorCleaning;
    @FXML RadioButton engineCleaning;
    @FXML RadioButton polishingWaxing;
    @FXML RadioButton upholsteryCleaning;

    @FXML
    public void addNewCarWash(Event event){
        DatabaseConnection db = new DatabaseConnection();

        db.addCarWash(boolToInt(interiorCleaning.isSelected()), boolToInt(exteriorCleaning.isSelected()), boolToInt(engineCleaning.isSelected()), boolToInt(polishingWaxing.isSelected()), boolToInt(upholsteryCleaning.isSelected()));

        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();

    }


    public int boolToInt(boolean b) {
        return b ? 1 : 0;
    }
}
