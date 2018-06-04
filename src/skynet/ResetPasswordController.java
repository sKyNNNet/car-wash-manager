package skynet;

import com.jfoenix.controls.JFXTextField;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.net.URL;
import java.security.SecureRandom;
import java.util.List;
import java.util.ResourceBundle;

public class ResetPasswordController implements Initializable {

    @FXML JFXTextField email;
    @FXML JFXTextField username;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //allow only alphanumeric input
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\sa-zA-Z0-9*")) {
                username.setText(newValue.replaceAll("[^\\sa-zA-Z0-9]", ""));
            }
        });
    }

    public void resetPasswordButtonClick(Event event){
        if(email.getText().isEmpty() && username.getText().isEmpty()){
            new Popup(Alert.AlertType.ERROR, "Error", "You need to type your email or username");
            return;
        }

        DatabaseConnection db = new DatabaseConnection();
        Crypt crypt = new Crypt();

        List<User> users = db.getUsers();

        User currentUser = null;

        if(!username.getText().isEmpty()){
            for(User u: users){
                if(username.getText().toLowerCase().equals(u.getUsername().toLowerCase())){
                    currentUser = u;

                    break;
                }
            }
            if(currentUser != null){
                String newPassword = generateRandomPassword(10);
                if(Utility.validEmail(currentUser.getEmail())){
                    db.updatePassword(currentUser.getId(), crypt.encrypt(newPassword));
                    new Email(currentUser.getEmail(), "Car Wash Manager - Password reset", "Your password has been reset.\r\nAccount: " + currentUser.getUsername() + "\r\n" + "New password: " + newPassword);
                    new Popup(Alert.AlertType.INFORMATION, "Info", "Your password has been reset, check the email associated with this account!");
                    ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
                } else {
                    new Popup(Alert.AlertType.ERROR, "Error", "Your account has an invalid email address set up, we can't reset your password!");
                }
            }
        }

        if(!email.getText().isEmpty()){
            if(Utility.validEmail(email.getText())){
                for(User u: users){
                    if(email.getText().toLowerCase().equals(u.getEmail().toLowerCase())){
                        currentUser = u;

                        break;
                    }
                }
                if(currentUser != null){
                    String newPassword = generateRandomPassword(10);

                    if(Utility.validEmail(currentUser.getEmail())){
                        db.updatePassword(currentUser.getId(), crypt.encrypt(newPassword));
                        new Email(currentUser.getEmail(), "Car Wash Manager - Password reset", "Your password has been reset.\r\nAccount: " + currentUser.getUsername() + "\r\n" + "New password: " + newPassword);
                        new Popup(Alert.AlertType.INFORMATION, "Info", "Your password has been reset, check the email!");
                        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
                    } else {
                        new Popup(Alert.AlertType.ERROR, "Error", "Your account has an invalid email address set up, we can't reset your password!");
                    }
                }
                else {
                    new Popup(Alert.AlertType.WARNING, "Error", "Account " + username.getText() + "was not found!");
                    return;
                }

            }
        }
    }

    public String generateRandomPassword(int length){
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom r = new SecureRandom();

        StringBuilder password = new StringBuilder(length);

            for(int i = 0; i < length; i++)
                password.append( chars.charAt( r.nextInt(chars.length())));


            return password.toString();

    }
}
