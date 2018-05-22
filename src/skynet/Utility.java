package skynet;

import javafx.scene.control.TextField;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class Utility {

    public static int stringToInt(String s){
        try {
            return Integer.parseInt(s);

        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            System.out.println("Error when trying to convert string:" + s + " to int");
        }

        return -1337;
    }

    public static double stringToDouble(String s){
        try {
            return Double.parseDouble(s);

        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            System.out.println("Error when trying to convert string:" + s + " to int");
        }

        return -1337;
    }

    public static boolean isEmpty(TextField t){
        return t.getText().isEmpty();
    }

    public static boolean validEmail(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }
}
