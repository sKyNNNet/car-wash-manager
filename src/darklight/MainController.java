package darklight;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class MainController implements Initializable {

    @FXML
    HBox dashboardTab;
    @FXML
    HBox employeesTab;
    @FXML
    HBox inventoryTab;

    @FXML
    VBox dashboardVBox;
    @FXML
    VBox employeesVBox;
    @FXML
    VBox inventoryVBox;

    @FXML
    Label carsWashedToday;
    @FXML
    Label carsWashedThisMonth;

    @FXML
    TableView employeesTable;
    @FXML
    TableColumn firstName;
    @FXML
    TableColumn lastName;
    @FXML
    TableColumn username;
    @FXML
    TableColumn email;
    @FXML
    TableColumn rank;

    @FXML
    TableView inventoryTable;
    @FXML
    TableColumn name;
    @FXML
    TableColumn quantity;
    @FXML
    TableColumn unit;
    @FXML
    TableColumn supplier;
    @FXML
    TableColumn pricePerUnit;

    @FXML
    Button dashboardAdd;
    @FXML
    Button dashboardSettings;

    private DatabaseConnection db = new DatabaseConnection();

    private final String inactiveSelection = "-fx-background-color: #333645;";
    private final String activeSelection = "-fx-background-color: #2d2f3d;";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //display dashboard when we login
        showDashboardTab();

        //update stats
        updateDashboard();
        updateEmployees();
        updateInventory();

    }

    public void addCarWash() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("UI/addCarWash.fxml"));
            Stage mainStage = new Stage();
            Scene mainScene = new Scene(root);
            mainStage.setResizable(false);
            mainStage.setScene(mainScene);
            mainStage.show();

            //when we get back to the main window we refresh the stats
            mainStage.focusedProperty().addListener((ov, t, t1) -> updateDashboard());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void addNewEmployee() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("UI/addNewEmployee.fxml"));
            Stage mainStage = new Stage();
            Scene mainScene = new Scene(root);
            mainStage.setResizable(false);
            mainStage.setScene(mainScene);
            mainStage.show();

            //when we get back to the main window we refresh the stats
            mainStage.focusedProperty().addListener((ov, t, t1) -> updateEmployees());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addNewItem() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("UI/addNewItem.fxml"));
            Stage mainStage = new Stage();
            Scene mainScene = new Scene(root);
            mainStage.setResizable(false);
            mainStage.setScene(mainScene);
            mainStage.show();

            //when we get back to the main window we refresh the stats
            mainStage.focusedProperty().addListener((ov, t, t1) -> updateInventory());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void dashboardSettingsButton() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("UI/settings.fxml"));
            Stage mainStage = new Stage();
            Scene mainScene = new Scene(root);
            mainStage.setResizable(false);
            mainStage.setScene(mainScene);
            mainStage.show();

            //when we get back to the main window we refresh the stats
            mainStage.focusedProperty().addListener((ov, t, t1) -> updateDashboard());

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //tab switcher
    public void showDashboardTab() {
        dashboardVBox.setVisible(true);
        employeesVBox.setVisible(false);
        inventoryVBox.setVisible(false);

        dashboardTab.setStyle(activeSelection);
        employeesTab.setStyle(inactiveSelection);
        inventoryTab.setStyle(inactiveSelection);
    }

    public void showEmployeesTab() {
        dashboardVBox.setVisible(false);
        employeesVBox.setVisible(true);
        inventoryVBox.setVisible(false);

        dashboardTab.setStyle(inactiveSelection);
        employeesTab.setStyle(activeSelection);
        inventoryTab.setStyle(inactiveSelection);
    }

    public void showInventoryTab() {
        dashboardVBox.setVisible(false);
        employeesVBox.setVisible(false);
        inventoryVBox.setVisible(true);

        dashboardTab.setStyle(inactiveSelection);
        employeesTab.setStyle(inactiveSelection);
        inventoryTab.setStyle(activeSelection);
    }

    public void updateDashboard() {
        carsWashedToday.setText(db.carsWashedToday());
        carsWashedThisMonth.setText(db.carsWashedThisMonth());
    }

    public void updateEmployees() {

        List<Employee> e = db.getEmployees();

        ObservableList<Employee> data = FXCollections.observableArrayList(e);

        firstName.setCellValueFactory(new PropertyValueFactory<Employee, String>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<Employee, String>("lastName"));
        username.setCellValueFactory(new PropertyValueFactory<Employee, String>("username"));
        email.setCellValueFactory(new PropertyValueFactory<Employee, String>("email"));
        rank.setCellValueFactory(new PropertyValueFactory<Employee, String>("rank"));

        employeesTable.setItems(data);
    }

    public void updateInventory() {

        List<Inventory> i = db.getInventory();

        ObservableList<Inventory> data = FXCollections.observableArrayList(i);

        name.setCellValueFactory(new PropertyValueFactory<Inventory, String>("name"));
        quantity.setCellValueFactory(new PropertyValueFactory<Inventory, Integer>("quantity"));
        unit.setCellValueFactory(new PropertyValueFactory<Inventory, String>("unit"));
        supplier.setCellValueFactory(new PropertyValueFactory<Inventory, String>("supplier"));
        pricePerUnit.setCellValueFactory(new PropertyValueFactory<Inventory, Double>("pricePerUnit"));

        inventoryTable.setItems(data);
    }
}
