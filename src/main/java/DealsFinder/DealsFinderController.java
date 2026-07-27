package DealsFinder;

import com.lowagie.text.Font;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import jdbc.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

public class DealsFinderController {

    @FXML
    private Button btnFind;

    @FXML
    private ComboBox<String> cmbBuyerMobile;

    @FXML
    private ComboBox<String> cmbSellerMobile;

    @FXML
    private ComboBox<String> cmbStatus;

    @FXML
    private TableView<DealsFinderBean> tblDeals;

    @FXML
    private TableColumn<DealsFinderBean, Double> colAdvance;

    @FXML
    private TableColumn<DealsFinderBean, Double> colBalance;

    @FXML
    private TableColumn<DealsFinderBean, String> colBuyerMobile;

    @FXML
    private TableColumn<DealsFinderBean, String> colBuyerName;

    @FXML
    private TableColumn<DealsFinderBean, Double> colCommission;

    @FXML
    private TableColumn<DealsFinderBean, String> colDealDate;

    @FXML
    private TableColumn<DealsFinderBean, String> colDealStatus;

    @FXML
    private TableColumn<DealsFinderBean, Double> colFinalAmount;

    @FXML
    private TableColumn<DealsFinderBean, Integer> colPropertyId;

    @FXML
    private TableColumn<DealsFinderBean, String> colRegistryDate;

    @FXML
    private TableColumn<DealsFinderBean, String> colSellerMobile;

    @FXML
    private TableColumn<DealsFinderBean, String> colSellerName;


    @FXML
    void doFindDeals(ActionEvent event) {

        ObservableList<DealsFinderBean> list = FXCollections.observableArrayList();

        try {

            Connection con = DBConnection.getConnection();

            StringBuilder sql = new StringBuilder("SELECT * FROM deals WHERE 1=1");

            ArrayList<Object> values = new ArrayList<>();

            if (cmbSellerMobile.getValue() != null) {
                sql.append(" AND seller_mobile=?");
                values.add(cmbSellerMobile.getValue().toString());
            }

            if (cmbBuyerMobile.getValue() != null) {
                sql.append(" AND buyer_mobile=?");
                values.add(cmbBuyerMobile.getValue().toString());
            }

            if (cmbStatus.getValue() != null) {
                sql.append(" AND deal_status=?");
                values.add(cmbStatus.getValue().toString());
            }

            PreparedStatement pst = con.prepareStatement(sql.toString());

            for (int i = 0; i < values.size(); i++) {
                pst.setObject(i + 1, values.get(i));
            }

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                list.add(new DealsFinderBean(

                        rs.getInt("property_id"),
                        rs.getString("seller_mobile"),
                        rs.getString("seller_name"),
                        rs.getString("buyer_mobile"),
                        rs.getString("buyer_name"),
                        rs.getString("deal_date"),
                        rs.getString("registry_date"),
                        rs.getDouble("final_amount"),
                        rs.getDouble("advance"),
                        rs.getDouble("balance"),
                        rs.getDouble("commission"),
                        rs.getString("deal_status")

                ));
            }

            tblDeals.setItems(list);

            rs.close();
            pst.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void doCreateExcel(ActionEvent event) {

        try {

            FileChooser chooser = new FileChooser();
            chooser.setTitle("Save Excel File");
            chooser.setInitialFileName("Deals_Report.xlsx");

            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));

            File file = chooser.showSaveDialog(tblDeals.getScene().getWindow());

            if (file == null)
                return;

            Workbook wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet("Deals");

            Row header = sheet.createRow(0);

            String[] headings = {
                    "Property ID",
                    "Seller Mobile",
                    "Seller Name",
                    "Buyer Mobile",
                    "Buyer Name",
                    "Deal Date",
                    "Registry Date",
                    "Final Amount",
                    "Advance",
                    "Balance",
                    "Commission",
                    "Status"
            };

            for (int i = 0; i < headings.length; i++) {
                header.createCell(i).setCellValue(headings[i]);
            }

            int rowNo = 1;

            for (DealsFinderBean d : tblDeals.getItems()) {

                Row row = sheet.createRow(rowNo++);

                row.createCell(0).setCellValue(d.getPropertyId());
                row.createCell(1).setCellValue(d.getSellerMobile());
                row.createCell(2).setCellValue(d.getSellerName());
                row.createCell(3).setCellValue(d.getBuyerMobile());
                row.createCell(4).setCellValue(d.getBuyerName());
                row.createCell(5).setCellValue(d.getDealDate());
                row.createCell(6).setCellValue(d.getRegistryDate());
                row.createCell(7).setCellValue(d.getFinalAmount());
                row.createCell(8).setCellValue(d.getAdvance());
                row.createCell(9).setCellValue(d.getBalance());
                row.createCell(10).setCellValue(d.getCommission());
                row.createCell(11).setCellValue(d.getDealStatus());
            }

            for (int i = 0; i < headings.length; i++)
                sheet.autoSizeColumn(i);

            FileOutputStream fos = new FileOutputStream(file);
            wb.write(fos);

            fos.close();
            wb.close();

            new Alert(Alert.AlertType.INFORMATION,
                    "Excel exported successfully.").show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void doCreatePDF(ActionEvent event) {

        try {

            FileChooser chooser = new FileChooser();
            chooser.setTitle("Save PDF");
            chooser.setInitialFileName("Deals_Report.pdf");

            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

            File file = chooser.showSaveDialog(tblDeals.getScene().getWindow());

            if (file == null)
                return;

            Document document = new Document(PageSize.A4.rotate());

            PdfWriter.getInstance(document,
                    new FileOutputStream(file));

            document.open();

            Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD);

            Paragraph title =
                    new Paragraph("Deals Report", titleFont);

            title.setAlignment(Element.ALIGN_CENTER);

            document.add(title);
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Generated On : " + LocalDate.now()));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(12);

            table.setWidthPercentage(100);

            table.addCell("Property ID");
            table.addCell("Seller Mobile");
            table.addCell("Seller Name");
            table.addCell("Buyer Mobile");
            table.addCell("Buyer Name");
            table.addCell("Deal Date");
            table.addCell("Registry Date");
            table.addCell("Final Amount");
            table.addCell("Advance");
            table.addCell("Balance");
            table.addCell("Commission");
            table.addCell("Status");

            for (DealsFinderBean d : tblDeals.getItems()) {

                table.addCell(String.valueOf(d.getPropertyId()));
                table.addCell(d.getSellerMobile());
                table.addCell(d.getSellerName());
                table.addCell(d.getBuyerMobile());
                table.addCell(d.getBuyerName());
                table.addCell(d.getDealDate());
                table.addCell(d.getRegistryDate());
                table.addCell(String.valueOf(d.getFinalAmount()));
                table.addCell(String.valueOf(d.getAdvance()));
                table.addCell(String.valueOf(d.getBalance()));
                table.addCell(String.valueOf(d.getCommission()));
                table.addCell(d.getDealStatus());
            }

            document.add(table);

            document.close();

            new Alert(Alert.AlertType.INFORMATION,
                    "PDF created successfully.").show();

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
    public void initialize() {

        fillCombos();

        colPropertyId.setCellValueFactory(new PropertyValueFactory<>("propertyId"));
        colSellerMobile.setCellValueFactory(new PropertyValueFactory<>("sellerMobile"));
        colSellerName.setCellValueFactory(new PropertyValueFactory<>("sellerName"));
        colBuyerMobile.setCellValueFactory(new PropertyValueFactory<>("buyerMobile"));
        colBuyerName.setCellValueFactory(new PropertyValueFactory<>("buyerName"));
        colDealDate.setCellValueFactory(new PropertyValueFactory<>("dealDate"));
        colRegistryDate.setCellValueFactory(new PropertyValueFactory<>("registryDate"));
        colFinalAmount.setCellValueFactory(new PropertyValueFactory<>("finalAmount"));
        colAdvance.setCellValueFactory(new PropertyValueFactory<>("advance"));
        colBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));
        colCommission.setCellValueFactory(new PropertyValueFactory<>("commission"));
        colDealStatus.setCellValueFactory(new PropertyValueFactory<>("dealStatus"));
    }

    public void fillCombos() {

        try (
                Connection con = DBConnection.getConnection()) {

            // Seller Mobile
            PreparedStatement pst = con.prepareStatement(
                    "SELECT DISTINCT seller_mobile FROM deals");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                cmbSellerMobile.getItems().add(rs.getString(1));
            }

            rs.close();
            pst.close();

            // Buyer Mobile
            pst = con.prepareStatement(
                    "SELECT DISTINCT buyer_mobile FROM deals");
            rs = pst.executeQuery();

            while (rs.next()) {
                cmbBuyerMobile.getItems().add(rs.getString(1));
            }

            rs.close();
            pst.close();

            // Deal Status
            pst = con.prepareStatement(
                    "SELECT DISTINCT deal_status FROM deals");
            rs = pst.executeQuery();

            while (rs.next()) {
                cmbStatus.getItems().add(rs.getString(1));
            }

            rs.close();
            pst.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
