package skynet;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Popup {

    public Popup(Alert.AlertType type, String title, String context){
        Alert alert = new Alert(type);

        //set the logo
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(
                new Image(this.getClass().getResource("../logo/appicon.png").toString()));

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(context);
        alert.showAndWait();
    }
}
