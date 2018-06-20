package skynet;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DatabaseConnection {

    private static String url = "jdbc:mysql://db4free.net:3306/carwashmanager?useUnicode=true&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static String username = "darklight";
    private static String password = "darklight1337";
    private static Connection connection = null;
    private static Statement stmt = null;
    private static DataSource datasource = null;
    public String sqlToday = "DAY(date)=DAY(NOW())";
    public String sqlThisMonth = "MONTH(date)=MONTH(NOW())";

    public static DataSource getDataSource() {
        if (datasource == null) {

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(url);
            config.setUsername(username);
            config.setPassword(password);
            config.setMaximumPoolSize(10);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            datasource = new HikariDataSource(config);
        }

        return datasource;
    }


    public boolean checkLogin(String username, String password) {
        Crypt crypt = new Crypt();

        try {
            DataSource dataSource = getDataSource();
            connection = dataSource.getConnection();
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
        }finally {
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

        return false;
    }

    public void addUser(String username, String password, String firstName, String lastName, String email, String rank) {
        try {
            DataSource dataSource = getDataSource();
            connection = dataSource.getConnection();
            stmt = connection.createStatement();
            String sql = "INSERT INTO users " +
                    "VALUES (null, '" + username + "', '" + password + "', '" + firstName + "', '" + lastName + "', '" + email + "', '" + rank + "')";

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

    public String getRankByUsername(String username) {
        String rank = "unranked";

        try {
            DataSource dataSource = getDataSource();
            connection = dataSource.getConnection();
            stmt = connection.createStatement();

            String sql = "SELECT `rank` FROM users WHERE username='" + username + "';";

            System.out.println(sql);

            //System.out.println(sql);

            stmt = connection.createStatement();
            ResultSet rset = stmt.executeQuery(sql);

            while (rset.next()) {
                rank = rset.getString("rank");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
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

        System.out.println("db rank:" + rank);

        return rank;
    }

    public String getPasswordById(int id) {
        String encryptedPassword = "a";

        try {
            DataSource dataSource = getDataSource();
            connection = dataSource.getConnection();
            stmt = connection.createStatement();

            String sql = "SELECT password FROM users WHERE id=" + id + ";";

            stmt = connection.createStatement();
            ResultSet rset = stmt.executeQuery(sql);

            while (rset.next()) {
                encryptedPassword = rset.getString("password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
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


        return encryptedPassword;
    }

    public int getIdByUsername(String username) {
        int id = 0;

        try {
            DataSource dataSource = getDataSource();
            connection = dataSource.getConnection();
            stmt = connection.createStatement();

            String sql = "SELECT id FROM users WHERE username='" + username + "';";

            stmt = connection.createStatement();
            ResultSet rset = stmt.executeQuery(sql);

            while (rset.next()) {
                id = rset.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
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


        return id;

    }

    public String getUsernameById(int id) {
        String username = "username";

        try {
            DataSource dataSource = getDataSource();
            connection = dataSource.getConnection();
            stmt = connection.createStatement();

            String sql = "SELECT username FROM users WHERE id=" + id + ";";

            stmt = connection.createStatement();
            ResultSet rset = stmt.executeQuery(sql);

            while (rset.next()) {
                username = rset.getString("username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
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


        return username;

    }

    public void updatePrice(int id, String serviceType, Double priceValue) {
        try {
            DataSource dataSource = getDataSource();
            connection = dataSource.getConnection();
            stmt = connection.createStatement();
            String sql = "UPDATE prices "
                    + "SET id=" + id + ", serviceType='" + serviceType + "', " + "priceValue=" + priceValue + " "
                    + "WHERE id=" + id + ";";

            System.out.println(sql);
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
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

    public void updatePassword(int id, String password) {
        try {
            DataSource dataSource = getDataSource();
            connection = dataSource.getConnection();
            stmt = connection.createStatement();
            String sql = "UPDATE users "
                    + "SET password='" + password + "' "
                    + "WHERE id=" + id + ";";

            System.out.println(sql);
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
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

    public double getPriceByServiceType(String serviceType) {
        double price = 0;

        try {
            DataSource dataSource = getDataSource();
            connection = dataSource.getConnection();
            stmt = connection.createStatement();

            String sql = "SELECT priceValue FROM prices WHERE serviceType='" + serviceType + "';";

            //System.out.println(sql);

            stmt = connection.createStatement();
            ResultSet rset = stmt.executeQuery(sql);

            while (rset.next()) {
                price = rset.getDouble("priceValue");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
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

        return price;

    }


    public List<Price> getPrices() {
        List<Price> prices = new ArrayList<>();

        try {
            DataSource dataSource = getDataSource();
            connection = dataSource.getConnection();
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
        }finally {
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

        return prices;
    }

    public void addCarWash(double interiorCleaning, double exteriorCleaning, double engineCleaning, double polishingWaxing, double upholsteryCleaning) {
        Double totalPrice = interiorCleaning + exteriorCleaning + engineCleaning + polishingWaxing + upholsteryCleaning;

        try {
            DataSource dataSource = getDataSource();
            connection = dataSource.getConnection();
            stmt = connection.createStatement();
            String sql = "INSERT INTO track " +
                    "VALUES (null, " + interiorCleaning + ", " + exteriorCleaning + ", " + engineCleaning + ", " + polishingWaxing + ", " + upholsteryCleaning + ", " + totalPrice + ", "  + "NOW()" + ")";

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

    public Double moneyMade(String date) {
        double moneyMade = 0;

        try {
            DataSource dataSource = getDataSource();
            connection = dataSource.getConnection();
            stmt = connection.createStatement();

            String sql = "SELECT SUM(totalPrice) from track where " + date + ";";

            //System.out.println(sql);

            stmt = connection.createStatement();
            ResultSet rset = stmt.executeQuery(sql);

            while (rset.next()) {
                moneyMade = rset.getDouble("SUM(totalPrice)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
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
        return moneyMade;
    }


    public String carsWashed(String date) {
        int carsWashedToday = 0;

        try {
            DataSource dataSource = getDataSource();
            connection = dataSource.getConnection();
            stmt = connection.createStatement();

            String sql = "SELECT count(date) from track where " + date + ";";

            //System.out.println(sql);

            stmt = connection.createStatement();
            ResultSet rset = stmt.executeQuery(sql);

            while (rset.next()) {
                carsWashedToday = rset.getInt("count(date)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
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

        return String.valueOf(carsWashedToday);
    }

    public List<Employee> getEmployees() {
        List<Employee> employee = new ArrayList<>();

        try {
            DataSource dataSource = getDataSource();
            connection = dataSource.getConnection();
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
        }finally {
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

        return employee;
    }

    public List<User> getUsers() {
        List<User> users = new ArrayList<>();

        try {
            DataSource dataSource = getDataSource();
            connection = dataSource.getConnection();
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
        }finally {
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

        return users;
    }

    public List<Inventory> getInventory() {
        List<Inventory> invetory = new ArrayList<>();

        try {
            DataSource dataSource = getDataSource();
            connection = dataSource.getConnection();
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
        }finally {
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

        return invetory;
    }

    public void addEmployee(String firstName, String lastName, String email, String rank) {
        try {
            DataSource dataSource = getDataSource();
            connection = dataSource.getConnection();
            stmt = connection.createStatement();
            String sql = "INSERT INTO employees " +
                    "VALUES (null, '" + firstName + "', '" + lastName + "', '" + email + "', '" + rank + "')";

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
        try {
            DataSource dataSource = getDataSource();
            connection = dataSource.getConnection();
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

    public void editItem(int id, String name, int quantity, String unit, String supplier, Double pricePerUnit) {
        try {
            DataSource dataSource = getDataSource();
            connection = dataSource.getConnection();
            stmt = connection.createStatement();
            String sql = "UPDATE inventory "
                    + "SET id=" + id + ", name='" + name + "', " + "quantity=" + quantity + ", " + "unit='" + unit + "', " + "supplier='" + supplier + "', " + "pricePerUnit=" + pricePerUnit + " "
                    + "WHERE id=" + id + ";";

            //System.out.println(sql);
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
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

    public void editEmployee(int id, String firstName, String lastName, String email, String rank) {
        try {
            DataSource dataSource = getDataSource();
            connection = dataSource.getConnection();
            //fucking `rank` is a reserved keyword in mysql how tf should i know, at least tell me in the error you piece of shit
            stmt = connection.createStatement();
            String sql = "UPDATE employees "
                    + "SET id=" + id + ", firstName='" + firstName + "', " + "lastName='" + lastName + "', " + "email='" + email + "', " + "`rank`='" + rank + "' "
                    + "WHERE id=" + id + ";";

            System.out.println(sql);
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
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

    public void editAccount(int id, String username, String password, String firstName, String lastName, String email, String rank) {
        try {
            DataSource dataSource = getDataSource();
            connection = dataSource.getConnection();
            //fucking `rank` is a reserved keyword in mysql how tf should i know, at least tell me in the error you piece of shit
            stmt = connection.createStatement();
            String sql = "UPDATE users "
                    + "SET id=" + id + ", username='" + username + "', " + "'password='" + password + "', firstName='" + firstName + "', " + "lastName='" + lastName + "', " + "email='" + email + "', " + "`rank`='" + rank + "' "
                    + "WHERE id=" + id + ";";

            System.out.println(sql);
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
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

    public void editAccount(int id, String username, String firstName, String lastName, String email, String rank) {
        try {
            DataSource dataSource = getDataSource();
            connection = dataSource.getConnection();
            //fucking `rank` is a reserved keyword in mysql how tf should i know, at least tell me in the error you piece of shit
            stmt = connection.createStatement();
            String sql = "UPDATE users "
                    + "SET id=" + id + ", username='" + username + "', firstName='" + firstName + "', " + "lastName='" + lastName + "', " + "email='" + email + "', " + "`rank`='" + rank + "' "
                    + "WHERE id=" + id + ";";

            System.out.println(sql);
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
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

    public void updateUserSettings(int id, String firstName, String lastName, String email) {
        try {
            DataSource dataSource = getDataSource();
            connection = dataSource.getConnection();
            stmt = connection.createStatement();
            String sql = "UPDATE employees "
                    + "SET firstName='" + firstName + "', " + "lastName='" + lastName + "', " + "email='" + email + "' "
                    + "WHERE id=" + id + ";";

            System.out.println(sql);
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
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

    public void deleteById(String tableName, int id) {
        try {
            DataSource dataSource = getDataSource();
            connection = dataSource.getConnection();
            stmt = connection.createStatement();
            String sql = "DELETE FROM " + tableName + " WHERE id=" + id;

            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
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
}
