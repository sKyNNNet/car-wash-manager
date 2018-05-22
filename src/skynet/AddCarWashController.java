package skynet;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

import java.util.List;


public class AddCarWashController {

    @FXML RadioButton interiorCleaning;
    @FXML RadioButton exteriorCleaning;
    @FXML RadioButton engineCleaning;
    @FXML RadioButton polishingWaxing;
    @FXML RadioButton upholsteryCleaning;

    @FXML
    public void addNewCarWash(Event event){
        DatabaseConnection db = new DatabaseConnection();

        List<Price> prices = db.getPrices();

        double interiorCleaningPrice = 0;
        double exteriorCleaningPrice = 0;
        double engineCleaningPrice = 0;
        double polishingWaxingPrice = 0;
        double upholsteryCleaningPrice = 0;

        for(Price p : prices) {
            if(p.getServiceType().equals("interiorCleaning") && interiorCleaning.isSelected())
                interiorCleaningPrice = p.getPriceValue();
            if(p.getServiceType().equals("exteriorCleaning") && exteriorCleaning.isSelected())
                exteriorCleaningPrice = p.getPriceValue();
            if(p.getServiceType().equals("engineCleaning") && engineCleaning.isSelected())
                engineCleaningPrice = p.getPriceValue();
            if(p.getServiceType().equals("polishingAndWaxing") && polishingWaxing.isSelected())
                polishingWaxingPrice = p.getPriceValue();
            if(p.getServiceType().equals("upholsteryCleaning") && upholsteryCleaning.isSelected())
                upholsteryCleaningPrice = p.getPriceValue();
        }

        db.addCarWash(interiorCleaningPrice, exteriorCleaningPrice, engineCleaningPrice, polishingWaxingPrice, upholsteryCleaningPrice);

        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();

    }
}
