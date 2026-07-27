package Dashboard;

import Login.LoginController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import jdbc.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class DashboardController {

    @FXML
    void doLogout(ActionEvent event) {

        try {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Logout");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");

            if (alert.showAndWait().get().getButtonData().isDefaultButton()) {

                Parent root = FXMLLoader.load(getClass().getResource("/Login/LoginView.fxml"));

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Login");
                stage.show();

                ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void openDashboard(ActionEvent event) {
        System.out.println("Dashboard");
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard/DashboardView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Property Management");
            stage.centerOnScreen();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void openProperties(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Properties/PropertiesView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.setTitle("Property Management");
            stage.setMaximized(true);      // Optional
            stage.show();

            // Close Login Window
            Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            loginStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void openCustomers(ActionEvent event) {
        System.out.println("Customers");
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CustomerMaster/CustomerMasterView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();

            stage.setScene(new Scene(root));
            stage.setTitle("Property Management");
            stage.centerOnScreen();
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void openDealers(ActionEvent event) {
        System.out.println("Dealers");
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Deals/DealsView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.setTitle("Deals Management");
            stage.setMaximized(true);      // Optional
            stage.show();

            // Close Login Window
            Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            loginStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @FXML
    void doBrowseCustomers(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BrowseCustomers/BrowseCustomersView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.setTitle("Browse Customers");
            stage.setMaximized(true);      // Optional
            stage.show();

            // Close Login Window
            Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            loginStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void doFindProperties(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PropertyFinder/PropertyFinderView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.setTitle("Property Management");
            stage.setMaximized(true);      // Optional
            stage.show();

            // Close Login Window
            Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            loginStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void doFindDeals(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DealsFinder/DealsFinderView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.setTitle("Deals Management");
            stage.setMaximized(true);      // Optional
            stage.show();

            // Close Login Window
            Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            loginStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void openAnalytics(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Analytics/AnalyticsView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.setTitle("Analytics");
            stage.setMaximized(true);      // Optional
            stage.show();

            // Close Login Window
            Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            loginStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void openSettings(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Settings/SettingsView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.setTitle("Settings");
            stage.setMaximized(true);      // Optional
            stage.show();

            // Close Login Window
            Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            loginStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


        @FXML
        private PieChart customerChart;

        @FXML
        private Label lblAvailable;

        @FXML
        private Label lblChandigarh;

        @FXML
        private Label lblCustomers;

        @FXML
        private Label lblLudhiana;

        @FXML
        private Label lblMohali;

        @FXML
        private Label lblPatiala;

        @FXML
        private Label lblProperties;

        @FXML
        private Label lblSold;

        @FXML
        private PieChart propertyChart;

    @FXML
    public void initialize() {
        System.out.println("Dashboard Loaded Successfully...");
        loadDashboard();
        loadTopCities();
    }

    private void loadDashboard() {

        try {

            Connection con = DBConnection.getConnection();

            // Total Customers
            ResultSet rs = con.createStatement().executeQuery(
                    "SELECT COUNT(*) FROM customers");
            if (rs.next()) {
                lblCustomers.setText(String.valueOf(rs.getInt(1)));
            }

            // Total Properties
            rs = con.createStatement().executeQuery(
                    "SELECT COUNT(*) FROM properties");
            if (rs.next()) {
                lblProperties.setText(String.valueOf(rs.getInt(1)));
            }

            // Available Properties
            rs = con.createStatement().executeQuery(
                    "SELECT COUNT(*) FROM deals WHERE deal_status='Ongoing'");
            int available = 0;
            if (rs.next()) {
                available = rs.getInt(1);
                lblAvailable.setText(String.valueOf(available));
            }

            // Sold Properties
            rs = con.createStatement().executeQuery(
                    "SELECT COUNT(*) FROM deals WHERE deal_status='Completed'");
            int sold = 0;
            if (rs.next()) {
                sold = rs.getInt(1);
                lblSold.setText(String.valueOf(sold));
            }

            loadPropertyChart(available, sold);
            loadCustomerChart(con);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void loadPropertyChart(int available, int sold) {

        ObservableList<PieChart.Data> list =
                FXCollections.observableArrayList(
                        new PieChart.Data("Available", available),
                        new PieChart.Data("Sold", sold)
                );

        propertyChart.setData(list);
        propertyChart.setTitle("Property Status");
    }
    private void loadCustomerChart(Connection con) {

        try {

            ResultSet rs = con.createStatement().executeQuery(
                    "SELECT ctype, COUNT(*) total FROM customers GROUP BY ctype");

            ObservableList<PieChart.Data> list =
                    FXCollections.observableArrayList();

            while (rs.next()) {

                list.add(new PieChart.Data(
                        rs.getString("ctype"),
                        rs.getInt("total")
                ));
            }

            customerChart.setData(list);
            customerChart.setTitle("Customer Categories");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadTopCities() {

        try {

            Connection con = DBConnection.getConnection();

            String query = """
                SELECT city, COUNT(*) AS total
                FROM properties
                GROUP BY city
                ORDER BY total DESC
                LIMIT 4
                """;

            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            Label[] cityNames = {city1, city2, city3, city4};
            Label[] cityCounts = {lblcity1, lblcity2, lblcity3, lblcity4};

            int i = 0;

            while (rs.next() && i < 4) {

                cityNames[i].setText(rs.getString("city"));
                cityCounts[i].setText(String.valueOf(rs.getInt("total")));

                i++;
            }

            // Clear remaining labels if fewer than 4 cities exist
            while (i < 4) {
                cityNames[i].setText("-");
                cityCounts[i].setText("0");
                i++;
            }

            rs.close();
            pst.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private Label city1;

    @FXML
    private Label city2;

    @FXML
    private Label city3;

    @FXML
    private Label city4;

    @FXML
    private Label lblcity1;

    @FXML
    private Label lblcity2;

    @FXML
    private Label lblcity3;

    @FXML
    private Label lblcity4;

}