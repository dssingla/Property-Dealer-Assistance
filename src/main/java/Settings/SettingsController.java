package Settings;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jdbc.Config;
import jdbc.DBConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SettingsController {

    @FXML
    private Button btnBackup;

    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnRestore;

    @FXML
    private Button btnSave;

    @FXML
    private CheckBox chkAutoBackup;

    @FXML
    private CheckBox chkDarkMode;

    @FXML
    private CheckBox chkNotification;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private PasswordField txtCurrentPassword;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtName;

    @FXML
    private PasswordField txtNewPassword;

    @FXML
    private TextField txtPhone;

    String MYSQL_BIN = Config.get("mysql.bin");
    String DB_NAME = "propertydealer";
    String DB_USER = Config.get("db.user");
    String DB_PASSWORD = Config.get("db.password");
    @FXML
    void doBackup(ActionEvent event) {

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Database Backup");
        chooser.setInitialFileName("propertydealer_backup.sql");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("SQL Files", "*.sql"));

        File file = chooser.showSaveDialog(null);

        if (file == null)
            return;

        try {

            ProcessBuilder pb = new ProcessBuilder(
                    MYSQL_BIN + "mysqldump.exe",
                    "-u" + DB_USER,
                    "-p" + DB_PASSWORD,
                    DB_NAME);

            pb.redirectOutput(file);

            Process process = pb.start();

            int status = process.waitFor();

            if (status == 0) {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Database Backup Successful.");
                alert.showAndWait();

            } else {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Backup Failed.");
                alert.showAndWait();
            }

        } catch (Exception e) {

            e.printStackTrace();

            new Alert(Alert.AlertType.ERROR,
                    e.getMessage()).show();
        }
    }

    @FXML
    void doRestore(ActionEvent event) {

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Backup File");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("SQL Files", "*.sql"));

        File file = chooser.showOpenDialog(null);

        if (file == null)
            return;

        try {

            ProcessBuilder pb = new ProcessBuilder(
                    MYSQL_BIN + "mysql.exe",
                    "-u" + DB_USER,
                    "-p" + DB_PASSWORD,
                    DB_NAME);

            pb.redirectInput(file);

            Process process = pb.start();

            int status = process.waitFor();

            if (status == 0) {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Database Restored Successfully.");
                alert.showAndWait();

            } else {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Restore Failed.");
                alert.showAndWait();
            }

        } catch (Exception e) {

            e.printStackTrace();

            new Alert(Alert.AlertType.ERROR,
                    e.getMessage()).show();
        }
    }


    @FXML
    void doSave(ActionEvent event) {

        String username = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();

        String currentPassword = txtCurrentPassword.getText();
        String newPassword = txtNewPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();

        // Validation
        if (username.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            new Alert(Alert.AlertType.ERROR,
                    "Please fill all required fields.").show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            new Alert(Alert.AlertType.ERROR,
                    "New Password and Confirm Password do not match.")
                    .show();
            return;
        }

        try (Connection con = DBConnection.getConnection()) {

            // Get stored password hash
            String checkSql = "SELECT password FROM admin WHERE username=?";

            PreparedStatement check = con.prepareStatement(checkSql);
            check.setString(1, username);

            ResultSet rs = check.executeQuery();

            if (!rs.next()) {

                new Alert(Alert.AlertType.ERROR,
                        "Admin account not found.")
                        .show();

                return;
            }

            String storedHash = rs.getString("password");

            // Verify current password
            if (!BCrypt.checkpw(currentPassword, storedHash)) {

                new Alert(Alert.AlertType.ERROR,
                        "Current Password is incorrect.")
                        .show();

                return;
            }

            String newHash = BCrypt.hashpw(newPassword, BCrypt.gensalt());

            String updateSql =
                    "UPDATE admin SET email=?, phone=?, password=?, notification=?, dark_mode=?, auto_backup=? WHERE username=?";

            PreparedStatement pst = con.prepareStatement(updateSql);

            pst.setString(1, email);
            pst.setString(2, phone);
            pst.setString(3, newHash);
            pst.setBoolean(4, chkNotification.isSelected());
            pst.setBoolean(5, chkDarkMode.isSelected());
            pst.setBoolean(6, chkAutoBackup.isSelected());
            pst.setString(7, username);

            int status = pst.executeUpdate();

            if (status > 0) {

                new Alert(Alert.AlertType.INFORMATION,
                        "Settings Updated Successfully.")
                        .show();

                txtCurrentPassword.clear();
                txtNewPassword.clear();
                txtConfirmPassword.clear();

            } else {

                new Alert(Alert.AlertType.ERROR,
                        "Unable to update settings.")
                        .show();
            }

            rs.close();
            check.close();
            pst.close();

        } catch (Exception e) {
            e.printStackTrace();

            new Alert(Alert.AlertType.ERROR,
                    "Error : " + e.getMessage())
                    .show();
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

    @FXML
    public void initialize() {
        loadSettings();
    }

    private void loadSettings() {

        String sql = "SELECT * FROM admin LIMIT 1";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {

                txtName.setText(rs.getString("username"));
                txtEmail.setText(rs.getString("email"));
                txtPhone.setText(rs.getString("phone"));

                chkNotification.setSelected(rs.getBoolean("notification"));
                chkDarkMode.setSelected(rs.getBoolean("dark_mode"));
                chkAutoBackup.setSelected(rs.getBoolean("auto_backup"));

                // Never load password from database
                txtCurrentPassword.clear();
                txtNewPassword.clear();
                txtConfirmPassword.clear();

            } else {

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText(null);
                alert.setContentText("No admin record found.");
                alert.showAndWait();
            }

        } catch (Exception e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Database Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();

            e.printStackTrace();
        }
    }
}
