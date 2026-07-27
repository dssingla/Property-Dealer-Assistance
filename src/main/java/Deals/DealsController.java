package Deals;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.stage.Stage;
import jdbc.DBConnection;

import javax.swing.*;

public class DealsController {

    @FXML
    private Button btnModify;

    @FXML
    private Button btnSave;

    @FXML
    private ComboBox<String> cmbProperty;

    @FXML
    private DatePicker dpDealDate;

    @FXML
    private DatePicker dpRegistryDate;

    @FXML
    private Button goDashboard;

    @FXML
    private RadioButton rbCancelled;

    @FXML
    private RadioButton rbCompleted;

    @FXML
    private RadioButton rbOngoing;

    @FXML
    private TextField txtAdvance;

    @FXML
    private TextField txtBalance;

    @FXML
    private TextField txtBuyerName;

    @FXML
    private TextField txtBuyerPhone;

    @FXML
    private TextField txtCommission;

    @FXML
    private TextField txtDownPayment;

    @FXML
    private TextField txtFinalAmount;

    @FXML
    private TextField txtPropertyID;

    @FXML
    private TextField txtSellerName;

    @FXML
    private TextField txtSellerPhone;

    Connection con;
    PreparedStatement pst;
    ResultSet rs;

    @FXML
    void doModify(ActionEvent event) {

        try {

            String status = "";

            if (rbCompleted.isSelected())
                status = "Completed";
            else if (rbOngoing.isSelected())
                status = "Ongoing";
            else if (rbCancelled.isSelected())
                status = "Cancelled";

            String query = "UPDATE deals SET "
                    + "seller_mobile=?, "
                    + "seller_name=?, "
                    + "buyer_mobile=?, "
                    + "buyer_name=?, "
                    + "deal_date=?, "
                    + "registry_date=?, "
                    + "final_amount=?, "
                    + "advance=?, "
                    + "down_payment=?, "
                    + "balance=?, "
                    + "commission=?, "
                    + "deal_status=? "
                    + "WHERE property_id=?";

            pst = con.prepareStatement(query);

            pst.setString(1, txtSellerPhone.getText());
            pst.setString(2, txtSellerName.getText());
            pst.setString(3, txtBuyerPhone.getText());
            pst.setString(4, txtBuyerName.getText());
            pst.setDate(5, java.sql.Date.valueOf(dpDealDate.getValue()));
            pst.setDate(6, java.sql.Date.valueOf(dpRegistryDate.getValue()));
            pst.setDouble(7, Double.parseDouble(txtFinalAmount.getText()));
            pst.setDouble(8, Double.parseDouble(txtAdvance.getText()));
            pst.setDouble(9, Double.parseDouble(txtDownPayment.getText()));
            pst.setDouble(10, Double.parseDouble(txtBalance.getText()));
            pst.setDouble(11, Double.parseDouble(txtCommission.getText()));
            pst.setString(12, status);
            pst.setInt(13, Integer.parseInt(txtPropertyID.getText()));

            int count = pst.executeUpdate();

            if (count > 0)
                JOptionPane.showMessageDialog(null, "Deal Updated Successfully");
            else
                JOptionPane.showMessageDialog(null, "No Deal Found for this Property");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    @FXML
    void doSave(ActionEvent event) {

        try {

            String status = "";

            if (rbCompleted.isSelected())
                status = "Completed";
            else if (rbOngoing.isSelected())
                status = "Ongoing";
            else if (rbCancelled.isSelected())
                status = "Cancelled";

            // Start Transaction
            con.setAutoCommit(false);

            String query = "INSERT INTO deals(property_id,seller_mobile,seller_name,"
                    + "buyer_mobile,buyer_name,deal_date,registry_date,"
                    + "final_amount,advance,down_payment,balance,"
                    + "commission,deal_status)"
                    + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

            pst = con.prepareStatement(query);

            pst.setInt(1, Integer.parseInt(txtPropertyID.getText()));
            pst.setString(2, txtSellerPhone.getText());
            pst.setString(3, txtSellerName.getText());
            pst.setString(4, txtBuyerPhone.getText());
            pst.setString(5, txtBuyerName.getText());
            pst.setDate(6, java.sql.Date.valueOf(dpDealDate.getValue()));
            pst.setDate(7, java.sql.Date.valueOf(dpRegistryDate.getValue()));
            pst.setDouble(8, Double.parseDouble(txtFinalAmount.getText()));
            pst.setDouble(9, Double.parseDouble(txtAdvance.getText()));
            pst.setDouble(10, Double.parseDouble(txtDownPayment.getText()));
            pst.setDouble(11, Double.parseDouble(txtBalance.getText()));
            pst.setDouble(12, Double.parseDouble(txtCommission.getText()));
            pst.setString(13, status);

            int count = pst.executeUpdate();

            if (count > 0) {

                if (status.equals("Completed")) {

                    String deleteQuery = "UPDATE properties\n" +
                            "SET property_status='Sold'\n" +
                            "WHERE property_id=?;";
                    PreparedStatement deletePst = con.prepareStatement(deleteQuery);
                    deletePst.setInt(1, Integer.parseInt(txtPropertyID.getText()));

                    int deleted = deletePst.executeUpdate();

                    if (deleted == 0) {
                        throw new Exception("Property not found. Delete operation failed.");
                    }

                    deletePst.close();
                }

                con.commit();

                JOptionPane.showMessageDialog(null, "Deal Saved Successfully");
                txtPropertyID.clear();
                txtSellerPhone.clear();
                txtSellerName.clear();
                txtBuyerPhone.clear();
                txtBuyerName.clear();
                txtFinalAmount.clear();
                txtAdvance.clear();
                txtDownPayment.clear();
                txtBalance.clear();
                txtCommission.clear();
                dpDealDate.setValue(null);
                dpRegistryDate.setValue(null);
                rbCompleted.setSelected(false);
                rbOngoing.setSelected(false);
                rbCancelled.setSelected(false);

            } else {

                con.rollback();
                JOptionPane.showMessageDialog(null, "Failed to Save Deal");
            }

            con.setAutoCommit(true);

        } catch (Exception e) {

            try {
                con.rollback();
                con.setAutoCommit(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    @FXML
    void goDashboard(ActionEvent event) {

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

    private void loadPropertyDetails() {

        try {

            if (cmbProperty.getValue() == null)
                return;

            String query =
                    "SELECT p.property_id, p.mobile, c.cname " +
                            "FROM properties p " +
                            "LEFT JOIN customers c ON p.mobile = c.mobile " +
                            "WHERE p.property_id=?";

            pst = con.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(cmbProperty.getValue().toString()));

            rs = pst.executeQuery();

            if (rs.next()) {

                txtPropertyID.setText(rs.getString("property_id"));
                txtSellerPhone.setText(rs.getString("mobile"));
                txtSellerName.setText(rs.getString("cname"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadPropertyIDs() {

        try {

            con = DBConnection.getConnection();

            String query = "SELECT property_id FROM properties ORDER BY property_id";

            pst = con.prepareStatement(query);

            rs = pst.executeQuery();

            cmbProperty.getItems().clear();

            while (rs.next()) {
                cmbProperty.getItems().add(rs.getString("property_id"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    void initialize() {
        loadPropertyIDs();
        cmbProperty.setOnAction(e -> loadPropertyDetails());
    }

}
