package CustomerMaster;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jdbc.DBConnection;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class CustomerMasterController {
    Connection con;
    File customerPicRef;
    File aadharPicRef;
    @FXML
    private Button btnBrowseAadhar;

    @FXML
    private Button btnBrowsePic;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnUpdate;

    @FXML
    private ComboBox<String> cmbType;

    @FXML
    private ImageView imgAadhar;

    @FXML
    private ImageView imgCustomer;

    @FXML
    private TextArea txtAddress;

    @FXML
    private TextField txtCity;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtMob;

    @FXML
    private TextField txtName;

    @FXML
    void doBrowseAadhar(ActionEvent event) {

        {
            try
            {
                FileChooser chooser = new FileChooser();

                chooser.setTitle("Select Aadhaar Image");

                chooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter(
                                "All Images",
                                "*.jpg",
                                "*.jpeg",
                                "*.png"));

                aadharPicRef = chooser.showOpenDialog(null);

                if(aadharPicRef != null)
                {
                    imgAadhar.setImage(
                            new Image(new FileInputStream(aadharPicRef)));
                }
            }
            catch(Exception exp)
            {
                exp.printStackTrace();
            }
        }
    }

    @FXML
    void doBrowsePic(ActionEvent event) {

        {
            try
            {
                FileChooser chooser = new FileChooser();

                chooser.setTitle("Select Customer Picture");

                chooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter(
                                "All Images",
                                "*.jpg",
                                "*.jpeg",
                                "*.png"));

                customerPicRef = chooser.showOpenDialog(null);

                if(customerPicRef != null)
                {
                    imgCustomer.setImage(
                            new Image(new FileInputStream(customerPicRef)));
                }
            }
            catch(Exception exp)
            {
                exp.printStackTrace();
            }
        }
    }

    @FXML
    void doClear(ActionEvent event) {

        {
            txtMob.clear();
            txtName.clear();
            txtAddress.clear();
            txtCity.clear();
            txtEmail.clear();

            cmbType.setValue(null);

            imgCustomer.setImage(null);
            imgAadhar.setImage(null);

            txtMob.requestFocus();
            customerPicRef = null;

            aadharPicRef = null;
        }
    }

    @FXML
    void doDelete(ActionEvent event) {
        {
            try
            {
                PreparedStatement pst;

                pst = con.prepareStatement(
                        "delete from customers where mobile=?");

                pst.setString(1, txtMob.getText());

                int count = pst.executeUpdate();

                if(count==1)
                {
                    System.out.println("Record Deleted Successfully");
                    doClear(null);
                }
                else
                {
                    System.out.println("No Record Found");
                }
            }
            catch(Exception exp)
            {
                exp.printStackTrace();
            }
        }
    }

    @FXML
    void doSave(ActionEvent event) {

        {
            try
            {     if(customerPicRef == null || aadharPicRef == null)
            {
                System.out.println("Please Select Both Images");
                return;
            }
                PreparedStatement pst;

                pst = con.prepareStatement(
                        "insert into customers values(?,?,?,?,?,?,?,?,?)");

                pst.setString(1, txtMob.getText());

                pst.setString(2, txtName.getText());

                pst.setString(3, txtAddress.getText());

                pst.setString(4, txtCity.getText());

                pst.setString(5, txtEmail.getText());

                pst.setString(6, cmbType.getValue());

                File customerFile =
                        new File(customerPicRef.getAbsolutePath());

                FileInputStream customerStream =
                        new FileInputStream(customerFile);

                pst.setBinaryStream(
                        7,
                        (InputStream)customerStream,
                        (int)customerFile.length());


                File aadharFile =
                        new File(aadharPicRef.getAbsolutePath());

                FileInputStream aadharStream =
                        new FileInputStream(aadharFile);

                pst.setBinaryStream(
                        8,
                        (InputStream)aadharStream,
                        (int)aadharFile.length());

                java.sql.Date date =
                        java.sql.Date.valueOf(LocalDate.now());

                pst.setDate(9, date);

                pst.executeUpdate();

                System.out.println("Record Saved Successfully");
            }
            catch(Exception exp)
            {
                exp.printStackTrace();
            }
        }
    }

    @FXML
    void doSearch(ActionEvent event) {

        {
            try
            {
                PreparedStatement pst;

                pst = con.prepareStatement(
                        "select * from customers where mobile=?");

                pst.setString(1, txtMob.getText());

                ResultSet table = pst.executeQuery();

                if(table.next())
                {
                    txtName.setText(table.getString("cname"));
                    txtAddress.setText(table.getString("address"));
                    txtCity.setText(table.getString("city"));
                    txtEmail.setText(table.getString("email"));
                    cmbType.setValue(table.getString("ctype"));
                    InputStream customerStream =
                            table.getBinaryStream("pic");

                    imgCustomer.setImage(
                            new Image(customerStream));


                    InputStream aadharStream =
                            table.getBinaryStream("acard");

                    imgAadhar.setImage(
                            new Image(aadharStream));

                    System.out.println("Record Found");
                }
                else
                {
                    System.out.println("Record Not Found");
                }
            }
            catch(Exception exp)
            {
                exp.printStackTrace();
            }
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
    void doUpdate(ActionEvent event) {

        {
            try
            {
                PreparedStatement pst;

                pst = con.prepareStatement(
                        "update customers set cname=?,address=?,city=?,email=?,ctype=?,pic=?,acard=? where mobile=?");;

                pst.setString(1, txtName.getText());
                pst.setString(2, txtAddress.getText());
                pst.setString(3, txtCity.getText());
                pst.setString(4, txtEmail.getText());
                pst.setString(5, cmbType.getValue());

                File customerFile = new File(customerPicRef.getAbsolutePath());
                FileInputStream customerStream = new FileInputStream(customerFile);

                pst.setBinaryStream(6, customerStream, (int) customerFile.length());

                File aadharFile = new File(aadharPicRef.getAbsolutePath());
                FileInputStream aadharStream = new FileInputStream(aadharFile);

                pst.setBinaryStream(7, aadharStream, (int) aadharFile.length());

                pst.setString(8, txtMob.getText());

                int count = pst.executeUpdate();

                if(count==1)
                    System.out.println("Record Updated Successfully");
                else
                    System.out.println("Record Not Found");
            }
            catch(Exception exp)
            {
                exp.printStackTrace();
            }
        }
    }
    public void doConnect()
    {

        try {
            con = DBConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(con == null)
            System.out.println("Connection Error");
        else
            System.out.println("All is Well");
    }

    @FXML
    void initialize()
    {
        doConnect();

        cmbType.getItems().addAll(
                "Buyer",
                "Seller"
        );
    }
}
