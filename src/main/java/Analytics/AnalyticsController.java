package Analytics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;
import jdbc.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AnalyticsController {

    @FXML
    private PieChart customerChart;

    @FXML
    private PieChart propertyChart;

    @FXML
    private PieChart structureChart;

    @FXML
    private PieChart dealChart;

    @FXML
    void goDashboard(ActionEvent event){
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard/DashboardView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.setTitle("Dashboard");
            stage.setMaximized(true);
            stage.show();

            Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            loginStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {

        loadCustomerChart();
        loadPropertyChart();
        loadStructureChart();
        loadDealChart();
    }

    private void loadCustomerChart() {

        ObservableList<PieChart.Data> data =
                FXCollections.observableArrayList();

        String sql = "SELECT ctype, COUNT(*) total FROM customers GROUP BY ctype";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {

                data.add(new PieChart.Data(
                        rs.getString("ctype"),
                        rs.getInt("total")));
            }

            customerChart.setData(data);
            customerChart.setTitle("Customer Types");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Property Types
    private void loadPropertyChart() {

        ObservableList<PieChart.Data> data =
                FXCollections.observableArrayList();

        String sql = "SELECT property_type, COUNT(*) total FROM properties GROUP BY property_type";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {

                data.add(new PieChart.Data(
                        rs.getString("property_type"),
                        rs.getInt("total")));
            }

            propertyChart.setData(data);
            propertyChart.setTitle("Property Types");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Construction Types
    private void loadStructureChart() {

        ObservableList<PieChart.Data> data =
                FXCollections.observableArrayList();

        String sql = "SELECT construction_type, COUNT(*) total FROM properties GROUP BY construction_type";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {

                data.add(new PieChart.Data(
                        rs.getString("construction_type"),
                        rs.getInt("total")));
            }

            structureChart.setData(data);
            structureChart.setTitle("Property Structure");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Deal Status
    private void loadDealChart() {

        ObservableList<PieChart.Data> data =
                FXCollections.observableArrayList();

        String sql = "SELECT deal_status, COUNT(*) total FROM deals GROUP BY deal_status";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {

                data.add(new PieChart.Data(
                        rs.getString("deal_status"),
                        rs.getInt("total")));
            }

            dealChart.setData(data);
            dealChart.setTitle("Deal Status");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}