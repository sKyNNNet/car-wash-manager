package skynet;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;


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
    Label moneyMadeToday;
    @FXML
    Label moneyMadeThisMonth;

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

    @FXML
    VBox teamMembersVBox;

    @FXML
    VBox leftPanelVBox;

    @FXML
    AnchorPane mainePane;

    @FXML
    AnchorPane scrollBoxPane;


    private DatabaseConnection db = new DatabaseConnection();


    private final String inactiveSelection = "-fx-background-color: #333645;";
    private final String activeSelection = "-fx-background-color: #2d2f3d; -fx-effect:  dropshadow( gaussian , rgba(0,0,0,0.4) , 10,0,1,1.5 );";

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

        //set the scroll pane for team members (if needed)
        ScrollPane scrollPane = new ScrollPane(teamMembersVBox);
        scrollPane.setFitToWidth(true);
        scrollBoxPane.getChildren().add(scrollPane);

    }

    public void addTeamMemberToLayout(String name, String rank) {
        VBox member = new VBox();
        HBox hbox = new HBox();
        VBox info = new VBox();
        Label nameLabel = new Label();
        Label rankLabel = new Label();

        ImageView icon = new ImageView();

        Image ceo = new Image("icons/ceo.png");
        Image assistant = new Image("icons/assistant.png");
        Image carWasher = new Image("icons/worker.png");

        if (rank.equals("CEO")) {
            icon.setImage(ceo);

        } else if (rank.equals("Assistent")) {
            icon.setImage(assistant);

        } else if (rank.equals("Car Washer")) {
            icon.setImage(carWasher);

        }

        icon.setPreserveRatio(true);
        icon.setFitWidth(30);
        icon.setFitHeight(30);


        nameLabel.setText(name);
        rankLabel.setText(rank);

        member.getStyleClass().add("memberVBox");
        info.getStyleClass().add("memberInfoVBox");
        nameLabel.getStyleClass().add("memberNameText");
        rankLabel.getStyleClass().add("memberRankText");

        info.getChildren().add(nameLabel);
        info.getChildren().add(rankLabel);
        hbox.getChildren().add(icon);
        hbox.getChildren().add(info);
        member.getChildren().add(hbox);
        teamMembersVBox.getChildren().add(member);
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

    public void inventoryEditButton() {

        Inventory item = inventoryTable.getSelectionModel().getSelectedItem();

        if (item != null) {
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

    public void employeeEditButton() {

        Employee employee = employeesTable.getSelectionModel().getSelectedItem();

        if (employee != null) {
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
            DatabaseConnection db = new DatabaseConnection();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("UI/settings.fxml"));

            Parent root = fxmlLoader.load();

            SettingsController controller = fxmlLoader.getController();

            controller.interiorCleaning.setText(String.valueOf(db.getPriceByServiceType("interiorCleaning")));
            controller.exteriorCleaning.setText(String.valueOf(db.getPriceByServiceType("exteriorCleaning")));
            controller.engineCleaning.setText(String.valueOf(db.getPriceByServiceType("engineCleaning")));
            controller.polishingAndWaxing.setText(String.valueOf(db.getPriceByServiceType("polishingAndWaxing")));
            controller.upholsteryCleaning.setText(String.valueOf(db.getPriceByServiceType("upholsteryCleaning")));

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
        carsWashedToday.setText(db.carsWashed(db.sqlToday));
        carsWashedThisMonth.setText(db.carsWashed(db.sqlThisMonth));

        moneyMadeToday.setText(db.moneyMade(db.sqlToday).toString());
        moneyMadeThisMonth.setText(db.moneyMade(db.sqlThisMonth).toString());
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

        teamMembersVBox.getChildren().clear();

        for (Employee emp : e) {
            String fullName = emp.getFirstName() + " " + emp.getLastName();
            addTeamMemberToLayout(fullName, emp.getRank());

        }

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
