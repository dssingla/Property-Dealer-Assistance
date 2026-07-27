package PropertyFinder;

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPTable;

import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jdbc.DBConnection;

public class PropertyFinderController {

    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnExcel;

    @FXML
    private Button btnFind;

    @FXML
    private Button btnPDF;

    @FXML
    private ComboBox<String> cmbArea;

    @FXML
    private ComboBox<String> cmbCity;

    @FXML
    private ComboBox<String> cmbPropertyType;

    @FXML
    private ComboBox<String> cmbStructure;

    @FXML
    private ComboBox<String> cmbStatus;

    @FXML
    private TableColumn<PropertyBean, String> colApprovedBy;

    @FXML
    private TableColumn<PropertyBean, String> colArea;

    @FXML
    private TableColumn<PropertyBean, String> colCity;

    @FXML
    private TableColumn<PropertyBean, String> colConstruction;

    @FXML
    private TableColumn<PropertyBean, String> colFacing;

    @FXML
    private TableColumn<PropertyBean, String> colFront;

    @FXML
    private TableColumn<PropertyBean, ?> colImage1;

    @FXML
    private TableColumn<PropertyBean, ?> colImage2;

    @FXML
    private TableColumn<PropertyBean, ?> colLeft;

    @FXML
    private TableColumn<PropertyBean, String> colOtherInfo;

    @FXML
    private TableColumn<PropertyBean, Double> colPrice;

    @FXML
    private TableColumn<PropertyBean, String> colPropertyType;

    @FXML
    private TableColumn<PropertyBean, String> colRear;

    @FXML
    private TableColumn<PropertyBean, String> colRight;

    @FXML
    private TableColumn<PropertyBean, String> colSize;

    @FXML
    private TableView<PropertyBean> propertyTable;

    @FXML
    private TextField txtMaxPrice;

    @FXML
    private TextField txtMinPrice;


    @FXML
    void doFindProperty(ActionEvent event) {

        ObservableList<PropertyBean> list = FXCollections.observableArrayList();
        try {

            Connection con = DBConnection.getConnection();

            String sql =
                    "SELECT * FROM properties " +
                            "WHERE city=? " +
                            "AND area=? " +
                            "AND construction_type=? " +
                            "AND property_type=? " +
                            "AND price BETWEEN ? AND ? " +
                            "AND property_status=?";

            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, cmbCity.getValue());
            pst.setString(2, cmbArea.getValue());
            pst.setString(3, cmbPropertyType.getValue());
            pst.setString(4, cmbStructure.getValue());
            pst.setDouble(5, Double.parseDouble(txtMinPrice.getText()));
            pst.setDouble(6, Double.parseDouble(txtMaxPrice.getText()));

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                list.add(new PropertyBean(

                        rs.getString("area"),
                        rs.getString("city"),
                        rs.getString("size"),
                        rs.getString("front_side"),
                        rs.getString("rear_side"),
                        rs.getString("left_side"),
                        rs.getString("right_side"),
                        rs.getString("direction"),
                        rs.getString("property_type"),
                        rs.getString("construction_type"),
                        rs.getString("approved_by"),
                        rs.getDouble("price"),
                        rs.getString("other_info"),
                        rs.getBytes("image1"),
                        rs.getBytes("image2"),
                        rs.getString("property_status")

                ));

            }

            propertyTable.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void doPDF(ActionEvent event) {

        try {

            Document document = new Document(PageSize.A4.rotate());

            FileChooser chooser = new FileChooser();
            chooser.setTitle("Save PDF");
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

            File file = chooser.showSaveDialog(btnPDF.getScene().getWindow());

            if (file == null)
                return;

            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            // Title
            Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD);
            Paragraph title = new Paragraph("Property Finder Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Generated On : " + LocalDate.now()));
            document.add(new Paragraph(" "));

            // Table
            PdfPTable pdfTable = new PdfPTable(11);
            pdfTable.setWidthPercentage(100);

            pdfTable.addCell("Area");
            pdfTable.addCell("City");
            pdfTable.addCell("Size");
            pdfTable.addCell("Front");
            pdfTable.addCell("Rear");
            pdfTable.addCell("Left");
            pdfTable.addCell("Right");
            pdfTable.addCell("Facing");
            pdfTable.addCell("Property Type");
            pdfTable.addCell("Construction");
            pdfTable.addCell("Approved By");

            // Add data from TableView
            for (PropertyBean p : propertyTable.getItems()) {

                pdfTable.addCell(p.getArea());
                pdfTable.addCell(p.getCity());
                pdfTable.addCell(p.getSize());
                pdfTable.addCell(p.getFrontSide());
                pdfTable.addCell(p.getRearSide());
                pdfTable.addCell(p.getLeftSide());
                pdfTable.addCell(p.getRightSide());
                pdfTable.addCell(p.getDirection());
                pdfTable.addCell(p.getPropertyType());
                pdfTable.addCell(p.getConstructionType());
                pdfTable.addCell(p.getApprovedBy());
            }

            document.add(pdfTable);

            document.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("PDF Generated Successfully.");
            alert.show();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void exportExcel(ActionEvent event) {

        try {

            // Save Dialog
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Excel File");

            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));

            fileChooser.setInitialFileName("Property_Report.xlsx");

            File file = fileChooser.showSaveDialog(propertyTable.getScene().getWindow());

            if (file == null)
                return;

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Properties");

            // Header
            Row header = sheet.createRow(0);

            String[] headers = {
                    "Area",
                    "City",
                    "Size",
                    "Front Side",
                    "Rear Side",
                    "Left Side",
                    "Right Side",
                    "Facing",
                    "Property Type",
                    "Construction",
                    "Approved By",
                    "Price"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Data
            int rowNum = 1;

            for (PropertyBean p : propertyTable.getItems()) {

                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(p.getArea());
                row.createCell(1).setCellValue(p.getCity());
                row.createCell(2).setCellValue(p.getSize());
                row.createCell(3).setCellValue(p.getFrontSide());
                row.createCell(4).setCellValue(p.getRearSide());
                row.createCell(5).setCellValue(p.getLeftSide());
                row.createCell(6).setCellValue(p.getRightSide());
                row.createCell(7).setCellValue(p.getDirection());
                row.createCell(8).setCellValue(p.getPropertyType());
                row.createCell(9).setCellValue(p.getConstructionType());
                row.createCell(10).setCellValue(p.getApprovedBy());
                row.createCell(11).setCellValue(p.getPrice());
            }

            // Auto Size Columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Save
            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);

            fos.close();
            workbook.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Excel exported successfully.");
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void goDashboard(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard/DashboardView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard");
            stage.setMaximized(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {

        colArea.setCellValueFactory(new PropertyValueFactory<>("area"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        colFront.setCellValueFactory(new PropertyValueFactory<>("frontSide"));
        colRear.setCellValueFactory(new PropertyValueFactory<>("rearSide"));
        colLeft.setCellValueFactory(new PropertyValueFactory<>("leftSide"));
        colRight.setCellValueFactory(new PropertyValueFactory<>("rightSide"));
        colFacing.setCellValueFactory(new PropertyValueFactory<>("direction"));
        colPropertyType.setCellValueFactory(new PropertyValueFactory<>("propertyType"));
        colConstruction.setCellValueFactory(new PropertyValueFactory<>("constructionType"));
        colApprovedBy.setCellValueFactory(new PropertyValueFactory<>("approvedBy"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colOtherInfo.setCellValueFactory(new PropertyValueFactory<>("otherInfo"));

        loadComboBoxes();

    }



    private void loadComboBoxes(){
        try {

            Connection con = DBConnection.getConnection();

            String query = "SELECT DISTINCT area, city, property_type, construction_type, property_status FROM properties";


            PreparedStatement pst = con.prepareStatement(query);

            ResultSet rs = pst.executeQuery();

            while(rs.next()){

                String area = rs.getString("area");
                String city = rs.getString("city");
                String type = rs.getString("construction_type");
                String structure = rs.getString("property_type");
                String status = rs.getString("property_status");


                if(area != null && !cmbArea.getItems().contains(area))
                    cmbArea.getItems().add(area);


                if(city != null && !cmbCity.getItems().contains(city))
                    cmbCity.getItems().add(city);


                if(type != null && !cmbPropertyType.getItems().contains(type))
                    cmbPropertyType.getItems().add(type);


                if(structure != null && !cmbStructure.getItems().contains(structure))
                    cmbStructure.getItems().add(structure);

                if (status != null && !cmbStatus.getItems().contains(status)) {
                    cmbStatus.getItems().add(status);
                }
            }


            rs.close();
            pst.close();
            con.close();

        }
        catch(Exception e){

            e.printStackTrace();

        }

    }

}
