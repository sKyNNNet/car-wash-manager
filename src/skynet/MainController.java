package skynet;

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
    TableView<Employee> employeesTable;
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
    TableView<Inventory> inventoryTable;
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

        employeesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        inventoryTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

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

    public void inventoryEditButton(){

        Inventory item  = inventoryTable.getSelectionModel().getSelectedItem();

        if(item != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("UI/editItem.fxml"));

                Parent root = fxmlLoader.load();

                EditItemController controller = fxmlLoader.getController();

                controller.name.setText(item.getName());
                controller.quantity.setText(String.valueOf(item.getQuantity()));
                controller.unit.setText(item.getUnit());
                controller.supplier.setText(item.getSupplier());
                controller.pricePerUnit.setText(String.valueOf(item.getPricePerUnit()));
                controller.editItemTextTitle.setText("Edit item #" + item.getId());

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
    }

    public void employeeEditButton(){

        Employee employee  = employeesTable.getSelectionModel().getSelectedItem();

        if(employee != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("UI/editEmployee.fxml"));

                Parent root = fxmlLoader.load();

                EditEmployeeController controller = fxmlLoader.getController();

                controller.firstName.setText(employee.getFirstName());
                controller.lastName.setText(employee.getLastName());
                controller.username.setText(employee.getUsername());
                controller.email.setText(employee.getEmail());
                controller.rank.setText(employee.getRank());
                controller.editEmployeeTextTitle.setText("Edit employee #" + employee.getId());

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
