package skynet;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseConnection {

    private String url = "jdbc:mysql://db4free.net:3307/carwashmanager?useUnicode=true&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private String username = "darklight";
    private String password = "darklight1337";
    private Connection connection;
    private Statement stmt;

    public String sqlToday = "DAY(date)=DAY(NOW())";
    public String sqlThisMonth = "MONTH(date)=MONTH(NOW());";


    public void connect() {
        System.out.println("Connecting database...");

        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connected!");
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }

    }

    public boolean checkLogin(String username, String password) {
        Crypt crypt = new Crypt();

        connect();

        try {
            stmt = connection.createStatement();

            String sqlPassowrd = "SELECT password FROM carwashmanager.users WHERE username='" + username + "';";
            ResultSet rset = stmt.executeQuery(sqlPassowrd);
            String encryptedLoginPassword = null;

            while (rset.next()) {
                encryptedLoginPassword = rset.getString("password");
            }

            //System.out.println(encryptedLoginPassword);
            //System.out.println(crypt.decrypt(encryptedLoginPassword));

            if (encryptedLoginPassword != null) {
                if (crypt.decrypt(encryptedLoginPassword).equals(password)) {
                    System.out.println("login successfully");
                    return true;
                } else {
                    System.out.println("Incorect login password");
                    return false;
                }
            } else {
                System.out.println("User does not exist");
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void addUser(String username, String password, String firstName, String lastName, String email, String rank) {
        connect();

        try {
            stmt = connection.createStatement();
            String sql = "INSERT INTO users " +
                    "VALUES (null, '" + username + "', '" + password + "', '" +firstName + "', '" +lastName +"', '" + email + "', '" + rank + "')";

            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            //close connections to database
            try {
                if (stmt != null)
                    connection.close();
            } catch (SQLException se) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

    }

    public String getRankByUsername(String username){

        connect();

        String rank = "unranked";

        try{
            stmt = connection.createStatement();

            String sql =  "SELECT `rank` FROM users WHERE username='" + username + "';";

            System.out.println(sql);

            //System.out.println(sql);

            stmt = connection.createStatement();
            ResultSet rset = stmt.executeQuery(sql);

            while (rset.next()) {
                rank = rset.getString("rank");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        System.out.println("db rank:" + rank);

        return rank;
    }

    public String getPasswordById(int id){

        connect();

        String encryptedPassword = "a";

        try{
            stmt = connection.createStatement();

            String sql =  "SELECT password FROM users WHERE id=" + id + ";";

            stmt = connection.createStatement();
            ResultSet rset = stmt.executeQuery(sql);

            while (rset.next()) {
                encryptedPassword = rset.getString("password");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }


        return encryptedPassword;
    }

    public int getIdByUsername(String username){
        connect();

        int id = 0;

        try{
            stmt = connection.createStatement();

            String sql =  "SELECT id FROM users WHERE username='" + username + "';";

            stmt = connection.createStatement();
            ResultSet rset = stmt.executeQuery(sql);

            while (rset.next()) {
                id = rset.getInt("id");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }


        return id;

    }

    public String getUsernameById(int id){
        connect();

        String username = "username";

        try{
            stmt = connection.createStatement();

            String sql =  "SELECT username FROM users WHERE id=" + id + ";";

            stmt = connection.createStatement();
            ResultSet rset = stmt.executeQuery(sql);

            while (rset.next()) {
                username = rset.getString("username");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }


        return username;

    }

    public void updatePrice(int id, String serviceType, Double priceValue){
        connect();

        try {
            stmt = connection.createStatement();
            String sql =    "UPDATE prices "
                    + "SET id=" + id + ", serviceType='" + serviceType + "', " + "priceValue=" + priceValue + " "
                    + "WHERE id=" + id +";";

            System.out.println(sql);
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void updatePassword(int id, String password){
        connect();

        try {
            stmt = connection.createStatement();
            String sql =    "UPDATE users "
                    + "SET password='" + password + "' "
                    + "WHERE id=" + id +";";

            System.out.println(sql);
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getPriceByServiceType(String serviceType){
        connect();

        double price= 0;

        try{
            stmt = connection.createStatement();

            String sql = "SELECT priceValue FROM prices WHERE serviceType='" + serviceType +"';";

            //System.out.println(sql);

            stmt = connection.createStatement();
            ResultSet rset = stmt.executeQuery(sql);

            while (rset.next()) {
                price = rset.getDouble("priceValue");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return price;

    }


    public List<Price> getPrices(){

        connect();

        List<Price> prices = new ArrayList<>();

        try {
            stmt = connection.createStatement();

            String pricesSQL = "SELECT * FROM carwashmanager.prices";
            ResultSet rset = stmt.executeQuery(pricesSQL);

            while (rset.next()) {
                int id = rset.getInt("id");
                String serviceType = rset.getString("serviceType");
                Double priceValue = rset.getDouble("priceValue");

                prices.add(new Price(id, serviceType, priceValue));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return prices;
    }

    public void addCarWash(double interiorCleaning, double exteriorCleaning, double engineCleaning, double polishingWaxing, double upholsteryCleaning) {
        connect();

        Date dt = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String currentDate = sdf.format(dt);

        Double totalPrice = interiorCleaning + exteriorCleaning + engineCleaning + polishingWaxing + upholsteryCleaning;

        try {
            stmt = connection.createStatement();
            String sql = "INSERT INTO track " +
                    "VALUES (null, " + interiorCleaning + ", " + exteriorCleaning + ", " + engineCleaning + ", " + polishingWaxing + ", " + upholsteryCleaning + ", " + totalPrice + ", " + "\'" + currentDate +  "\'" + ")";

            //System.out.println(sql);
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            //close connections to database
            try {
                if (stmt != null)
                    connection.close();
            } catch (SQLException se) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

    }

    public Double moneyMade(String date){
        connect();

        double moneyMade= 0;

        try{
            stmt = connection.createStatement();

            String sql = "SELECT SUM(totalPrice) from track where " + date + ";";

            //System.out.println(sql);

            stmt = connection.createStatement();
            ResultSet rset = stmt.executeQuery(sql);

            while (rset.next()) {
                moneyMade = rset.getDouble("SUM(totalPrice)");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return moneyMade;
    }


    public String carsWashed(String date){
        connect();

        int carsWashedToday = 0;

        try{
            stmt = connection.createStatement();

            String sql = "SELECT count(date) from track where " + date + ";";

            //System.out.println(sql);

            stmt = connection.createStatement();
            ResultSet rset = stmt.executeQuery(sql);

            while (rset.next()) {
                carsWashedToday = rset.getInt("count(date)");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return String.valueOf(carsWashedToday);
    }

    public List<Employee> getEmployees(){

        connect();

        List<Employee> employee = new ArrayList<>();

        try {
            stmt = connection.createStatement();

            String employeesSql = "SELECT * FROM carwashmanager.employees";
            ResultSet rset = stmt.executeQuery(employeesSql);

            while (rset.next()) {
                int id = rset.getInt("id");
                String firstName = rset.getString("firstName");
                String lastName = rset.getString("lastName");
                String email = rset.getString("email");
                String rank = rset.getString("rank");

                employee.add(new Employee(id, username, firstName, lastName, email, rank));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employee;
    }

    public List<User> getUsers(){

        connect();

        List<User> users = new ArrayList<>();

        try {
            stmt = connection.createStatement();

            String usersSql = "SELECT * FROM carwashmanager.users";
            ResultSet rset = stmt.executeQuery(usersSql);

            while (rset.next()) {
                int id = rset.getInt("id");
                String firstName = rset.getString("firstName");
                String lastName = rset.getString("lastName");
                String username = rset.getString("username");
                String email = rset.getString("email");
                String rank = rset.getString("rank");


                users.add(new User(id, username, firstName, lastName, email, rank));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public List<Inventory> getInventory(){

        connect();

        List<Inventory> invetory = new ArrayList<>();

        try {
            stmt = connection.createStatement();

            String inventorySQL = "SELECT * FROM carwashmanager.inventory";
            ResultSet rset = stmt.executeQuery(inventorySQL);

            while (rset.next()) {
                int id = rset.getInt("id");
                String name = rset.getString("name");
                int quantity = rset.getInt("quantity");
                String unit = rset.getString("unit");
                String supplier = rset.getString("supplier");
                Double pricePerUnit = rset.getDouble("pricePerUnit");

                invetory.add(new Inventory(id, name, quantity, unit, supplier, pricePerUnit));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return invetory;
    }

    public void addEmployee(String firstName, String lastName, String email, String rank) {
        connect();

        try {
            stmt = connection.createStatement();
            String sql = "INSERT INTO employees " +
                    "VALUES (null, '"+ firstName + "', '" + lastName + "', '" + email + "', '" + rank + "')";

            System.out.println(sql);
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            //close connections to database
            try {
                if (stmt != null)
                    connection.close();
            } catch (SQLException se) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

    }

    public void addNewItem(String name, int quantity, String unit, String supplier, Double pricePerUnit) {
        connect();

        try {
            stmt = connection.createStatement();
            String sql = "INSERT INTO inventory " +
                    "VALUES (null, '" + name + "', '" + quantity + "', '" + unit + "', '" + supplier + "', '" + pricePerUnit + "')";

            System.out.println(sql);
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            //close connections to database
            try {
                if (stmt != null)
                    connection.close();
            } catch (SQLException se) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

    }

    public void editItem(int id, String name, int quantity, String unit, String supplier, Double pricePerUnit){
        connect();

        try {

            stmt = connection.createStatement();
            String sql =    "UPDATE inventory "
                            + "SET id=" + id + ", name='" + name + "', " + "quantity=" + quantity + ", " + "unit='" + unit + "', " + "supplier='" + supplier + "', " + "pricePerUnit=" + pricePerUnit + " "
                            + "WHERE id=" + id +";";

            //System.out.println(sql);
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void editEmployee(int id,String firstName, String lastName, String email, String rank){
        connect();

        try {

            //fucking `rank` is a reserved keyword in mysql how tf should i know, at least tell me in the error you piece of shit
            stmt = connection.createStatement();
            String sql =    "UPDATE employees "
                            + "SET id=" + id + "firstName='" + firstName + "', " + "lastName='" + lastName + "', " + "email='" + email + "', " + "`rank`='" + rank + "' "
                            + "WHERE id=" + id +";";

            System.out.println(sql);
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void editAccount(int id,String username, String password, String firstName, String lastName, String email, String rank){
        connect();

        try {

            //fucking `rank` is a reserved keyword in mysql how tf should i know, at least tell me in the error you piece of shit
            stmt = connection.createStatement();
            String sql =    "UPDATE users "
                    + "SET id=" + id + ", username='" + username + "', " + "'password='" + password + "', firstName='" + firstName + "', " + "lastName='" + lastName + "', " + "email='" + email + "', " + "`rank`='" + rank + "' "
                    + "WHERE id=" + id +";";

            System.out.println(sql);
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void editAccount(int id,String username, String firstName, String lastName, String email, String rank){
        connect();

        try {

            //fucking `rank` is a reserved keyword in mysql how tf should i know, at least tell me in the error you piece of shit
            stmt = connection.createStatement();
            String sql =    "UPDATE users "
                    + "SET id=" + id + ", username='" + username + "', firstName='" + firstName + "', " + "lastName='" + lastName + "', " + "email='" + email + "', " + "`rank`='" + rank + "' "
                    + "WHERE id=" + id +";";

            System.out.println(sql);
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void updateUserSettings(int id, String firstName, String lastName, String email){
        connect();

        try {
            stmt = connection.createStatement();
            String sql =    "UPDATE employees "
                    + "SET firstName='" + firstName + "', " + "lastName='" + lastName + "', " + "email='" + email + "' "
                    + "WHERE id=" + id +";";

            System.out.println(sql);
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteById(String tableName, int id){
        connect();

        try {

            stmt = connection.createStatement();
            String sql = "DELETE FROM " + tableName + " WHERE id=" + id;

            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
