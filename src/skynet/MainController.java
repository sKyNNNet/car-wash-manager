package skynet;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class MainController implements Initializable {

    private final String inactiveSelection = "-fx-background-color: #333645;";
    private final String activeSelection = "-fx-background-color: #2d2f3d; -fx-effect:  dropshadow( gaussian , rgba(0,0,0,0.4) , 10,0,1,1.5 );";
    @FXML
    HBox dashboardTab;
    @FXML
    VBox noPower;
    @FXML
    HBox employeesTab;
    @FXML
    HBox inventoryTab;
    @FXML
    HBox accountsListTab;
    @FXML
    VBox dashboardVBox;
    @FXML
    VBox employeesVBox;
    @FXML
    VBox inventoryVBox;
    @FXML
    VBox accountsListVBox;
    @FXML
    Label employeeStats;
    @FXML
    Label inventoryStats;
    @FXML
    Label inventoryPriceStats;
    @FXML
    Label moneyMade;
    @FXML
    Label carsWashed;
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
    TableView<User> accountsTableView;
    @FXML
    TableColumn accountFirstName;
    @FXML
    TableColumn accountLastName;
    @FXML
    TableColumn accountUsername;
    @FXML
    TableColumn accountEmail;
    @FXML
    TableColumn accountRank;

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
    @FXML
    MenuButton userMenu;

    private DatabaseConnection db = new DatabaseConnection();
    public static String loginRank = "unranked";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        restrictUsage();
        //display dashboard when we login
        showDashboardTab();

        //update stats
        updateDashboard();
        updateEmployees();
        updateInventory();
        updateAccounts();

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
        ImageView editUserInfoImgView = new ImageView();

        Image ceo = new Image("skynet/icons/ceo.png");
        Image assistant = new Image("skynet/icons/assistant.png");
        Image carWasher = new Image("skynet/icons/worker.png");
        Image editUserInfoImg = new Image("skynet/icons/editEmployeeInfo3.png");


        Button editUserInfoButton = new Button();


        if (rank.equals("CEO")) {
            icon.setImage(ceo);

        } else if (rank.equals("Assistant")) {
            icon.setImage(assistant);

        } else if (rank.equals("Car Washer")) {
            icon.setImage(carWasher);

        }

        icon.setPreserveRatio(true);
        icon.setFitWidth(30);
        icon.setFitHeight(30);

        editUserInfoImgView.setImage(editUserInfoImg);
        editUserInfoImgView.setPreserveRatio(true);
        editUserInfoImgView.setFitWidth(18);
        editUserInfoImgView.setFitHeight(18);


        nameLabel.setText(name);
        rankLabel.setText(rank);

        member.getStyleClass().add("memberVBox");
        info.getStyleClass().add("memberInfoVBox");
        nameLabel.getStyleClass().add("memberNameText");
        rankLabel.getStyleClass().add("memberRankText");

        info.getChildren().add(nameLabel);
        info.getChildren().add(rankLabel);
        info.setPrefWidth(210);
        hbox.getChildren().add(icon);
        hbox.getChildren().add(info);

        //if logged in as CEO we can edit all user infos
        if (loginRank.equals("CEO")) {
            editUserInfoButton.setGraphic(editUserInfoImgView);
            editUserInfoButton.setText("");
            editUserInfoButton.setStyle("-fx-background-color: transparent; fx-margin: 5 0 0 0;");
            editUserInfoButton.setCursor(Cursor.HAND);
            editUserInfoButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(getClass().getResource("UI/userSettings.fxml"));

                        Parent root = fxmlLoader.load();

                        UserSettings controller = fxmlLoader.getController();

                        controller.firstName.setText(name.split(" ")[0]);

                        List<Employee> employees = db.getEmployees();

                        for (Employee e : employees) {
                            if (e.getFirstName().equals(name.split(" ")[0])) {
                                controller.details.setText(name + " - #" + e.getId());
                                controller.firstName.setText(e.getFirstName());
                                controller.lastName.setText(e.getLastName());
                                controller.email.setText(e.getEmail());

                            }
                        }
                        Stage mainStage = new Stage();
                        Scene mainScene = new Scene(root);
                        mainStage.getIcons().add(new Image(getClass().getResourceAsStream("logo/appicon.png")));
                        mainStage.setResizable(false);
                        mainStage.setScene(mainScene);
                        mainStage.show();

                        mainStage.focusedProperty().addListener((ov, t, t1) -> updateEmployees());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });


            hbox.getChildren().add(editUserInfoButton);
        }

        //if logged in as Assistant
        if (loginRank.equals("Assistant") && !rank.equals("CEO")) {
            editUserInfoButton.setGraphic(editUserInfoImgView);
            editUserInfoButton.setText("");
            editUserInfoButton.setStyle("-fx-background-color: transparent; fx-margin: 5 0 0 0;");
            editUserInfoButton.setCursor(Cursor.HAND);
            editUserInfoButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(getClass().getResource("UI/userSettings.fxml"));

                        Parent root = fxmlLoader.load();

                        UserSettings controller = fxmlLoader.getController();

                        controller.firstName.setText(name.split(" ")[0]);

                        List<Employee> employees = db.getEmployees();

                        for (Employee e : employees) {
                            if (e.getFirstName().equals(name.split(" ")[0])) {
                                controller.details.setText(e.getUsername() + " - #" + e.getId());
                                controller.firstName.setText(e.getFirstName());
                                controller.lastName.setText(e.getLastName());
                                controller.email.setText(e.getEmail());

                            }
                        }
                        Stage mainStage = new Stage();
                        Scene mainScene = new Scene(root);
                        mainStage.getIcons().add(new Image(getClass().getResourceAsStream("logo/appicon.png")));
                        mainStage.setResizable(false);
                        mainStage.setScene(mainScene);
                        mainStage.show();

                        //when we get back to the main window we refresh the stats
                        mainStage.focusedProperty().addListener((ov, t, t1) -> updateEmployees());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });


            hbox.getChildren().add(editUserInfoButton);
        }


        member.getChildren().add(hbox);

        teamMembersVBox.getChildren().add(member);
    }

    public void addCarWash() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("UI/addCarWash.fxml"));
            Stage mainStage = new Stage();
            Scene mainScene = new Scene(root);
            mainStage.getIcons().add(new Image(getClass().getResourceAsStream("logo/appicon.png")));
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
            mainStage.getIcons().add(new Image(getClass().getResourceAsStream("logo/appicon.png")));
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
            mainStage.getIcons().add(new Image(getClass().getResourceAsStream("logo/appicon.png")));
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
                mainStage.getIcons().add(new Image(getClass().getResourceAsStream("logo/appicon.png")));
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
                controller.email.setText(employee.getEmail());

                if (employee.getRank().equals("CEO"))
                    controller.rank.getSelectionModel().select(0);
                else if (employee.getRank().equals("Assistant"))
                    controller.rank.getSelectionModel().select(1);
                else if (employee.getRank().equals("Car Washer"))
                    controller.rank.getSelectionModel().select(2);
                else
                    controller.rank.getSelectionModel().select(3);

                controller.editEmployeeTextTitle.setText("Edit Employee #" + employee.getId());

                Stage mainStage = new Stage();
                Scene mainScene = new Scene(root);
                mainStage.getIcons().add(new Image(getClass().getResourceAsStream("logo/appicon.png")));
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
        if (loginRank.equals("CEO")) {
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
                mainStage.getIcons().add(new Image(getClass().getResourceAsStream("logo/appicon.png")));
                mainStage.setResizable(false);
                mainStage.setScene(mainScene);
                mainStage.show();

                //when we get back to the main window we refresh the stats
                mainStage.focusedProperty().addListener((ov, t, t1) -> updateInventory());

            } catch (IOException e) {
                e.printStackTrace();
            }


        } else {
            new Popup(Alert.AlertType.WARNING, "Error", "Only the CEO can modify these settings.");
            return;
        }
    }

    //tab switcher
    public void showDashboardTab() {
        if (loginRank.equals("CEO") || loginRank.equals("Assistant")) {
            noPower.setVisible(false);
            updateDashboard();
            dashboardVBox.setVisible(true);
            employeesVBox.setVisible(false);
            inventoryVBox.setVisible(false);
            accountsListVBox.setVisible(false);

            dashboardTab.setStyle(activeSelection);
            employeesTab.setStyle(inactiveSelection);
            inventoryTab.setStyle(inactiveSelection);
            accountsListTab.setStyle(inactiveSelection);
        } else {
            showRestrictedUsage();
        }
    }

    public void showEmployeesTab() {
        if (loginRank.equals("CEO") || loginRank.equals("Assistant")) {
            noPower.setVisible(false);
            dashboardVBox.setVisible(false);
            employeesVBox.setVisible(true);
            inventoryVBox.setVisible(false);
            accountsListVBox.setVisible(false);

            dashboardTab.setStyle(inactiveSelection);
            employeesTab.setStyle(activeSelection);
            inventoryTab.setStyle(inactiveSelection);
            accountsListTab.setStyle(inactiveSelection);
        } else {
            showRestrictedUsage();
        }
    }

    public void showInventoryTab() {
        if (loginRank.equals("CEO") || loginRank.equals("Assistant")) {
            noPower.setVisible(false);
            dashboardVBox.setVisible(false);
            employeesVBox.setVisible(false);
            inventoryVBox.setVisible(true);
            accountsListVBox.setVisible(false);

            dashboardTab.setStyle(inactiveSelection);
            employeesTab.setStyle(inactiveSelection);
            inventoryTab.setStyle(activeSelection);
            accountsListTab.setStyle(inactiveSelection);
        } else {
            showRestrictedUsage();
        }
    }

    public void showAccountsListTab() {

        if (loginRank.equals("CEO")) {
            noPower.setVisible(false);
            dashboardVBox.setVisible(false);
            employeesVBox.setVisible(false);
            inventoryVBox.setVisible(false);
            accountsListVBox.setVisible(true);

            dashboardTab.setStyle(inactiveSelection);
            employeesTab.setStyle(inactiveSelection);
            inventoryTab.setStyle(inactiveSelection);
            accountsListTab.setStyle(activeSelection);
        } else {
            showRestrictedUsage();
        }

    }

    public void updateDashboard() {

        List<Employee> employees = db.getEmployees();
        List<Inventory> inventory = db.getInventory();

        int inventoryItems  = 0;
        int inventoryPrice  = 0;

        for(Inventory i : inventory){
            inventoryItems += i.getQuantity();
            inventoryPrice += i.getPricePerUnit() * i.getQuantity();
        }

        employeeStats.setText(employees.size() + " employees");
        inventoryStats.setText(inventoryItems + " items in inventory");
        inventoryPriceStats.setText("inventory price - " + inventoryPrice);


        String moneyMadeToday = db.moneyMade(db.sqlToday).toString();
        String moneyMadeThisMonth = db.moneyMade(db.sqlThisMonth).toString();
        moneyMade.setText(moneyMadeToday + " today / " + moneyMadeThisMonth + " mth");

        String carsWashedToday = db.carsWashed(db.sqlToday);
        String carsWashedThisMonth = db.carsWashed(db.sqlThisMonth);
        carsWashed.setText(carsWashedToday + " today / " + carsWashedThisMonth + " mth");

    }

    public void updateEmployees() {

        List<Employee> e = db.getEmployees();

        ObservableList<Employee> data = FXCollections.observableArrayList(e);

        firstName.setCellValueFactory(new PropertyValueFactory<Employee, String>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<Employee, String>("lastName"));
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

    public void updateAccounts() {

        List<User> u = db.getUsers();

        ObservableList<User> data = FXCollections.observableArrayList(u);

        accountFirstName.setCellValueFactory(new PropertyValueFactory<Employee, String>("firstName"));
        accountLastName.setCellValueFactory(new PropertyValueFactory<Employee, String>("lastName"));
        accountUsername.setCellValueFactory(new PropertyValueFactory<Employee, String>("username"));
        accountEmail.setCellValueFactory(new PropertyValueFactory<Employee, String>("email"));
        accountRank.setCellValueFactory(new PropertyValueFactory<Employee, String>("rank"));

        accountsTableView.setItems(data);

    }

    public void logoutUser() {
        try {

            mainePane.getScene().getWindow().hide();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("UI/login.fxml"));

            Parent root = fxmlLoader.load();

            Stage mainStage = new Stage();
            Scene mainScene = new Scene(root);
            mainStage.getIcons().add(new Image(getClass().getResourceAsStream("logo/appicon.png")));
            mainStage.setResizable(false);
            mainStage.setScene(mainScene);
            mainStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void openUserSettings() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("UI/loggedInUserSettings.fxml"));

            Parent root = fxmlLoader.load();

            LoggedInUserSettings controller = fxmlLoader.getController();

            List<User> users = db.getUsers();

            for (User u : users) {
                if (u.getUsername().equals(userMenu.getText())) {
                    controller.welcomeLabel.setText("Welcome " + userMenu.getText());
                    controller.firstName.setText(u.getFirstName());
                    controller.lastName.setText(u.getLastName());
                    controller.email.setText(u.getEmail());
                    controller.userIdLabel.setText(String.valueOf(u.getId()));
                }
            }

            Stage mainStage = new Stage();
            Scene mainScene = new Scene(root);
            mainStage.getIcons().add(new Image(getClass().getResourceAsStream("logo/appicon.png")));
            mainStage.setResizable(false);
            mainStage.setScene(mainScene);
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void accountEditButton() {
        User user = accountsTableView.getSelectionModel().getSelectedItem();

        if (user != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("UI/editAccount.fxml"));

                Parent root = fxmlLoader.load();

                EditAccountController controller = fxmlLoader.getController();

                controller.firstName.setText(user.getFirstName());
                controller.lastName.setText(user.getLastName());
                controller.username.setText(user.getUsername());
                controller.email.setText(user.getEmail());

                if (user.getRank().equals("CEO"))
                    controller.rank.getSelectionModel().select(0);
                else if (user.getRank().equals("Assistant"))
                    controller.rank.getSelectionModel().select(1);
                else if (user.getRank().equals("Car Washer"))
                    controller.rank.getSelectionModel().select(2);
                else
                    controller.rank.getSelectionModel().select(3);

                controller.editAccountTextTitle.setText("Edit Account #" + user.getId());

                Stage mainStage = new Stage();
                Scene mainScene = new Scene(root);
                mainStage.getIcons().add(new Image(getClass().getResourceAsStream("logo/appicon.png")));
                mainStage.setResizable(false);
                mainStage.setScene(mainScene);
                mainStage.show();

                //when we get back to the main window we refresh the stats
                mainStage.focusedProperty().addListener((ov, t, t1) -> updateAccounts());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addNewAccount() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("UI/addNewAccount.fxml"));
            Stage mainStage = new Stage();
            Scene mainScene = new Scene(root);
            mainStage.getIcons().add(new Image(getClass().getResourceAsStream("logo/appicon.png")));
            mainStage.setResizable(false);
            mainStage.setScene(mainScene);
            mainStage.show();

            //when we get back to the main window we refresh the stats
            mainStage.focusedProperty().addListener((ov, t, t1) -> updateAccounts());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setUserMenuUsername(String username) {
        userMenu.setText(username);
        loginRank = db.getRankByUsername(userMenu.getText());

        updateEmployees();

        restrictUsage();
    }

    public void restrictUsage() {
        if (loginRank.equals("Car Washer") || loginRank.equals("unranked")) {
            dashboardVBox.setVisible(false);
            noPower.setVisible(true);
        }

        if (loginRank.equals("CEO") || loginRank.equals("Assistant")) {
            dashboardVBox.setVisible(true);
            noPower.setVisible(false);
        }
    }

    public void showRestrictedUsage() {
        dashboardVBox.setVisible(false);
        employeesVBox.setVisible(false);
        inventoryVBox.setVisible(false);
        accountsListVBox.setVisible(false);
        noPower.setVisible(true);
    }
}
