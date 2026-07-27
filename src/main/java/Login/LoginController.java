package Login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import jdbc.DBConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblStatus;

    @FXML
    private CheckBox chkRemember;

    @FXML
    void doLogin(ActionEvent event) {

        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {

            lblStatus.setStyle("-fx-text-fill:red;");
            lblStatus.setText("Please enter username and password.");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {

            String sql = "SELECT password FROM admin WHERE username=?";

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, username);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                String storedHash = rs.getString("password");

                if (BCrypt.checkpw(password, storedHash)) {

                    FXMLLoader loader = new FXMLLoader(
                            getClass().getResource("/Dashboard/DashboardView.fxml"));

                    Parent root = loader.load();

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Property Dealer Assistance");
                    stage.setMaximized(true);
                    stage.show();

                    Stage loginStage = (Stage) ((Node) event.getSource())
                            .getScene().getWindow();
                    loginStage.close();

                } else {

                    lblStatus.setStyle("-fx-text-fill:red;");
                    lblStatus.setText("Invalid Username or Password");
                }

            } else {

                lblStatus.setStyle("-fx-text-fill:red;");
                lblStatus.setText("Invalid Username or Password");
            }

            rs.close();
            pst.close();

        } catch (Exception e) {

            e.printStackTrace();

            lblStatus.setStyle("-fx-text-fill:red;");
            lblStatus.setText("Database Error");
        }
    }

    @FXML
    void doClear(ActionEvent event) {

        txtUsername.clear();
        txtPassword.clear();
        chkRemember.setSelected(false);
        lblStatus.setText("");
    }

    @FXML
    void doResetPassword(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Forgot Password");
        alert.setHeaderText(null);
        alert.setContentText("Please contact the administrator to reset your password.");
        alert.showAndWait();
    }
}