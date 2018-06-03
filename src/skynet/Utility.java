package skynet;

import javafx.scene.control.TextField;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.*;
import java.util.Scanner;

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

    public static String readFile(File file){

        String s = "";

        try {
            Scanner sc = new Scanner(file);
            while(sc.hasNextLine()){
                s += sc.nextLine();
            }
        }catch (IOException e){

        }

        return s;
    }

    public static void writeFile(String fileName, String text){
        try {
            PrintWriter writer = new PrintWriter(fileName, "UTF-8");
            writer.append(text);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
