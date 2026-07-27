package Properties;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jdbc.DBConnection;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;

public class PropertiesController {


    private File image1File;
    private File image2File;
    @FXML
    private Button btnBrowse1;

    @FXML
    private Button btnBrowse2;

    @FXML
    private ComboBox<String> comboApprovedBy;

    @FXML
    private TitledPane comboConType;

    @FXML
    private TitledPane comboProType;

    @FXML
    private ComboBox<String> comboProperty;

    @FXML
    private ImageView img1;

    @FXML
    private ImageView img2;

    @FXML
    private RadioButton rbAgricultural;

    @FXML
    private RadioButton rbCommercial;

    @FXML
    private RadioButton rbConstructed;

    @FXML
    private RadioButton rbPlot;

    @FXML
    private RadioButton rbResidential;
    @FXML
    private TextField txtArea;

    @FXML
    private TextField txtCity;

    @FXML
    private TextField txtDirection;

    @FXML
    private TextField txtFront;

    @FXML
    private TextField txtInfo;

    @FXML
    private TextField txtLeft;

    @FXML
    private TextField txtLocation;

    @FXML
    private TextField txtMobile;

    @FXML
    private TextField txtPrice;

    @FXML
    private TextField txtRear;

    @FXML
    private TextField txtRight;

    @FXML
    private TextField txtSize;


    @FXML
    void doClear(ActionEvent event) {

        // TextFields
        txtMobile.clear();
        txtCity.clear();
        txtLocation.clear();
        txtArea.clear();
        txtSize.clear();
        txtFront.clear();
        txtRear.clear();
        txtLeft.clear();
        txtRight.clear();
        txtDirection.clear();
        txtPrice.clear();
        txtInfo.clear();

        // ComboBoxes
        comboProperty.getItems().clear();
        comboProperty.setValue(null);

        comboApprovedBy.setValue(null);

        // Radio Buttons
        rbCommercial.setSelected(false);
        rbResidential.setSelected(false);
        rbAgricultural.setSelected(false);

        rbPlot.setSelected(false);
        rbConstructed.setSelected(false);

        // Images
        img1.setImage(null);
        img2.setImage(null);

        // Clear stored image paths (if you are using them)

        System.out.println("Form Cleared Successfully");
    }


    HashMap<String, Integer> propertyMap = new HashMap<>();
    Connection con;
    PreparedStatement pst;
    ResultSet rs;

    @FXML
    void doFetch(ActionEvent event) {

        try {

            comboProperty.getItems().clear();

            String query = "SELECT property_id FROM properties WHERE mobile=?";

            pst = con.prepareStatement(query);
            pst.setString(1, txtMobile.getText());

            rs = pst.executeQuery();

            boolean found = false;

            while (rs.next()) {

                found = true;
                comboProperty.getItems().add(String.valueOf(rs.getInt("property_id")));

            }

            if (found) {
                comboProperty.getSelectionModel().selectFirst();
                System.out.println("Property IDs Loaded Successfully");
            } else {
                System.out.println("No Property Found");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void doGetPicture1(ActionEvent event) {

        FileChooser fc = new FileChooser();

        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif"));

        image1File = fc.showOpenDialog(null);

        if (image1File != null) {
            img1.setImage(new Image(image1File.toURI().toString()));
        }
    }

    @FXML
    void doGetPicture2(ActionEvent event) {

        FileChooser fc = new FileChooser();

        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif"));

        image2File = fc.showOpenDialog(null);

        if (image2File != null) {
            img2.setImage(new Image(image2File.toURI().toString()));
        }
    }


    @FXML
    void doList(ActionEvent event) {

        try {

            String propertyType = "";
            String constructionType = "";

            // Property Type
            if (rbCommercial.isSelected())
                propertyType = "Commercial";
            else if (rbResidential.isSelected())
                propertyType = "Residential";
            else if (rbAgricultural.isSelected())
                propertyType = "Agricultural";

            // Construction Type
            if (rbPlot.isSelected())
                constructionType = "Plot";
            else if (rbConstructed.isSelected())
                constructionType = "Constructed";

            String query = "INSERT INTO properties (mobile, property_type, city, location, area, size, front_side, rear_side, left_side, right_side, direction, construction_type, approved_by, price, other_info, image1, image2,property_status) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            pst = con.prepareStatement(query);

            pst.setString(1, txtMobile.getText());
            pst.setString(2, propertyType);
            pst.setString(3, txtCity.getText());
            pst.setString(4, txtLocation.getText());
            pst.setString(5, txtArea.getText());
            pst.setString(6, txtSize.getText());
            pst.setString(7, txtFront.getText());
            pst.setString(8, txtRear.getText());
            pst.setString(9, txtLeft.getText());
            pst.setString(10, txtRight.getText());
            pst.setString(11, txtDirection.getText());
            pst.setString(12, constructionType);
            pst.setString(13, comboApprovedBy.getValue());
            pst.setDouble(14, Double.parseDouble(txtPrice.getText()));
            pst.setString(15, txtInfo.getText());

            // Image 1
            if (image1File != null) {
                FileInputStream fis1 = new FileInputStream(image1File);
                pst.setBinaryStream(16, fis1, (int) image1File.length());
            } else {
                pst.setNull(16, java.sql.Types.BLOB);
            }

            // Image 2
            if (image2File != null) {
                FileInputStream fis2 = new FileInputStream(image2File);
                pst.setBinaryStream(17, fis2, (int) image2File.length());
            } else {
                pst.setNull(17, java.sql.Types.BLOB);
            }
            pst.setString(18,"Available");
            int status = pst.executeUpdate();

            if (status > 0)
                System.out.println("Property Listed Successfully");
            else
                System.out.println("Property Not Added");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    void doRemove(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Property");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this property?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            try {

                int propertyId = Integer.parseInt(comboProperty.getValue());

                String query = "DELETE FROM properties WHERE property_id=?";

                pst = con.prepareStatement(query);
                pst.setInt(1, propertyId);

                int status = pst.executeUpdate();

                if (status > 0) {

                    System.out.println("Property Deleted Successfully");

                    doClear(null);

                } else {

                    System.out.println("Record Not Found");

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    void doUpdate(ActionEvent event) {

        try {

            String propertyType = "";
            String constructionType = "";

            // Property Type
            if (rbCommercial.isSelected())
                propertyType = "Commercial";
            else if (rbResidential.isSelected())
                propertyType = "Residential";
            else if (rbAgricultural.isSelected())
                propertyType = "Agricultural";

            // Construction Type
            if (rbPlot.isSelected())
                constructionType = "Plot";
            else if (rbConstructed.isSelected())
                constructionType = "Constructed";

            int propertyId = Integer.parseInt(comboProperty.getValue());

            String query = "UPDATE properties SET "
                    + "mobile=?, "
                    + "property_type=?, "
                    + "city=?, "
                    + "location=?, "
                    + "area=?, "
                    + "size=?, "
                    + "front_side=?, "
                    + "rear_side=?, "
                    + "left_side=?, "
                    + "right_side=?, "
                    + "direction=?, "
                    + "construction_type=?, "
                    + "approved_by=?, "
                    + "price=?, "
                    + "other_info=?, "
                    + "image1=?, "
                    + "image2=? "
                    + "WHERE property_id=?";

            pst = con.prepareStatement(query);

            pst.setString(1, txtMobile.getText());
            pst.setString(2, propertyType);
            pst.setString(3, txtCity.getText());
            pst.setString(4, txtLocation.getText());
            pst.setDouble(5, Double.parseDouble(txtArea.getText()));
            pst.setString(6, txtSize.getText());
            pst.setString(7, txtFront.getText());
            pst.setString(8, txtRear.getText());
            pst.setString(9, txtLeft.getText());
            pst.setString(10, txtRight.getText());
            pst.setString(11, txtDirection.getText());
            pst.setString(12, constructionType);
            pst.setString(13, comboApprovedBy.getValue());
            pst.setDouble(14, Double.parseDouble(txtPrice.getText()));
            pst.setString(15, txtInfo.getText());

            // Image 1
            if (image1File != null) {
                FileInputStream fis1 = new FileInputStream(image1File);
                pst.setBinaryStream(16, fis1, (int) image1File.length());
            } else {
                pst.setNull(16, java.sql.Types.BLOB);
            }

            // Image 2
            if (image2File != null) {
                FileInputStream fis2 = new FileInputStream(image2File);
                pst.setBinaryStream(17, fis2, (int) image2File.length());
            } else {
                pst.setNull(17, java.sql.Types.BLOB);
            }

            pst.setInt(18, propertyId);

            int status = pst.executeUpdate();

            if (status > 0)
                System.out.println("Property Updated Successfully");
            else
                System.out.println("Update Failed");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadPropertyDetails() {

        try {

            if (comboProperty.getValue() == null)
                return;

            int propertyId = Integer.parseInt(comboProperty.getValue());

            String query = "SELECT * FROM properties WHERE property_id=?";

            pst = con.prepareStatement(query);
            pst.setInt(1, propertyId);

            rs = pst.executeQuery();

            if (rs.next()) {

                txtMobile.setText(rs.getString("mobile"));
                txtCity.setText(rs.getString("city"));
                txtLocation.setText(rs.getString("location"));
                txtArea.setText(rs.getString("area"));
                txtSize.setText(rs.getString("size"));

                txtFront.setText(rs.getString("front_side"));
                txtRear.setText(rs.getString("rear_side"));
                txtLeft.setText(rs.getString("left_side"));
                txtRight.setText(rs.getString("right_side"));

                txtDirection.setText(rs.getString("direction"));
                txtPrice.setText(rs.getString("price"));
                txtInfo.setText(rs.getString("other_info"));

                comboApprovedBy.setValue(rs.getString("approved_by"));

                // Property Type
                String propertyType = rs.getString("property_type");

                rbCommercial.setSelected(false);
                rbResidential.setSelected(false);
                rbAgricultural.setSelected(false);

                if ("Commercial".equalsIgnoreCase(propertyType))
                    rbCommercial.setSelected(true);
                else if ("Residential".equalsIgnoreCase(propertyType))
                    rbResidential.setSelected(true);
                else if ("Agricultural".equalsIgnoreCase(propertyType))
                    rbAgricultural.setSelected(true);

                // Construction Type
                String constructionType = rs.getString("construction_type");

                rbPlot.setSelected(false);
                rbConstructed.setSelected(false);

                if ("Plot".equalsIgnoreCase(constructionType))
                    rbPlot.setSelected(true);
                else if ("Constructed".equalsIgnoreCase(constructionType))
                    rbConstructed.setSelected(true);

                // ==========================
                // Load Image 1 (LONGBLOB)
                // ==========================

                InputStream is1 = rs.getBinaryStream("image1");

                if (is1 != null) {
                    Image image1 = new Image(is1);
                    img1.setImage(image1);
                    is1.close();
                } else {
                    img1.setImage(null);
                }

                // ==========================
                // Load Image 2 (LONGBLOB)
                // ==========================

                InputStream is2 = rs.getBinaryStream("image2");

                if (is2 != null) {
                    Image image2 = new Image(is2);
                    img2.setImage(image2);
                    is2.close();
                } else {
                    img2.setImage(null);
                }

            } else {

                System.out.println("Property Not Found");

            }

        } catch (Exception e) {
            e.printStackTrace();
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

        try {
            con = DBConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        comboProperty.setOnAction(e -> loadPropertyDetails());

        ToggleGroup propertyGroup = new ToggleGroup();

        rbCommercial.setToggleGroup(propertyGroup);
        rbResidential.setToggleGroup(propertyGroup);
        rbAgricultural.setToggleGroup(propertyGroup);

        ToggleGroup constructionGroup = new ToggleGroup();

        rbPlot.setToggleGroup(constructionGroup);
        rbConstructed.setToggleGroup(constructionGroup);
        comboApprovedBy.getItems().addAll(
                "GMADA",
                "MC",
                "PUDA",
                "HUDA"
        );
    }

}
