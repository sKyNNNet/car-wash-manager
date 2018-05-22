package skynet;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class SettingsController implements Initializable {
    @FXML TextField interiorCleaning;
    @FXML TextField exteriorCleaning;
    @FXML TextField engineCleaning;
    @FXML TextField polishingAndWaxing;
    @FXML TextField upholsteryCleaning;

    public Utility util = new Utility();

    public void saveSettingsButton(Event event){
        DatabaseConnection db = new DatabaseConnection();

        double interiorCleaningPrice = util.stringToDouble(interiorCleaning.getText());
        double exteriorCleaningPrice = util.stringToDouble(exteriorCleaning.getText());
        double engineCleaningPrice = util.stringToDouble(engineCleaning.getText());
        double polishingAndWaxingPrice = util.stringToDouble(polishingAndWaxing.getText());
        double upholsteryCleaningPrice = util.stringToDouble(upholsteryCleaning.getText());

        db.updatePrice(1, "interiorCleaning", interiorCleaningPrice);
        db.updatePrice(2, "exteriorCleaning", exteriorCleaningPrice);
        db.updatePrice(3, "engineCleaning", engineCleaningPrice);
        db.updatePrice(4, "polishingAndWaxing", polishingAndWaxingPrice);
        db.updatePrice(5, "upholsteryCleaning", upholsteryCleaningPrice);

        ((Stage)(((javafx.scene.control.Button)event.getSource()).getScene().getWindow())).close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        //allow only double type as input
        Pattern pattern = Pattern.compile("\\d*|\\d+\\.\\d*");

        interiorCleaning.setTextFormatter(new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        }));

        exteriorCleaning.setTextFormatter(new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        }));
        engineCleaning.setTextFormatter(new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        }));
        polishingAndWaxing.setTextFormatter(new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        }));
        upholsteryCleaning.setTextFormatter(new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        }));
    }
}
