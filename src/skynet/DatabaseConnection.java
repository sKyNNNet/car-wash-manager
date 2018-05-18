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

    public void addUser(String username, String password) {
        connect();

        try {
            stmt = connection.createStatement();
            String sql = "INSERT INTO users " +
                    "VALUES (null, " + username + ", " + password + ")";

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

    public void addCarWash(int interiorCleaning, int exteriorCleaning, int engineCleaning, int polishingWaxing, int upholsteryCleaning) {
        connect();

        Date dt = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String currentDate = sdf.format(dt);

        try {
            stmt = connection.createStatement();
            String sql = "INSERT INTO track " +
                    "VALUES (null, " + interiorCleaning + ", " + exteriorCleaning + ", " + engineCleaning + ", " + polishingWaxing + ", " + upholsteryCleaning + ", " + "\'" + currentDate +  "\'" + ")";

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

    public String carsWashedToday(){
        connect();

        int carsWashedToday = 0;

        try{
            stmt = connection.createStatement();

            String sql = "SELECT count(date) from track where DAY(date)=DAY(NOW());";

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

    public String carsWashedThisMonth(){
        connect();

        int carsWashedThisMonth = 0;

        try{
            stmt = connection.createStatement();

            String sql = "SELECT count(date) from track where MONTH(date)=MONTH(NOW());";

            //System.out.println(sql);

            stmt = connection.createStatement();
            ResultSet rset = stmt.executeQuery(sql);

            while (rset.next()) {
                carsWashedThisMonth = rset.getInt("count(date)");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return String.valueOf(carsWashedThisMonth);
    }

    public List<Employee> getEmployees(){

        connect();

        List<Employee> employee = new ArrayList<>();

        try {
            stmt = connection.createStatement();

            String employeesSql = "SELECT * FROM carwashmanager.users";
            ResultSet rset = stmt.executeQuery(employeesSql);

            while (rset.next()) {
                int id = rset.getInt("id");
                String firstName = rset.getString("firstName");
                String lastName = rset.getString("lastName");
                String username = rset.getString("username");
                String email = rset.getString("email");
                String rank = rset.getString("rank");

                employee.add(new Employee(id, username, firstName, lastName, email, rank));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employee;
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

    public void addEmployee(String username, String password, String firstName, String lastName, String email, String rank) {
        connect();

        try {
            stmt = connection.createStatement();
            String sql = "INSERT INTO users " +
                    "VALUES (null, '" + username + "', '" + password + "', '" + firstName + "', '" + lastName + "', '" + email + "', '" + rank + "')";

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

    public void editEmployee(int id, String username, String firstName, String lastName, String email, String rank){
        connect();

        try {

            //fucking `rank` is a reserved keyword in mysql how tf should i know, at least tell me in the error you piece of shit
            stmt = connection.createStatement();
            String sql =    "UPDATE users "
                            + "SET id=" + id + ", username='" + username + "', " + "firstName='" + firstName + "', " + "lastName='" + lastName + "', " + "email='" + email + "', " + "`rank`='" + rank + "' "
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
