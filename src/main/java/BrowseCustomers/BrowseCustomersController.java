package BrowseCustomers;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Font;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;

import javafx.stage.Stage;
import jdbc.DBConnection;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.scene.control.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;

public class BrowseCustomersController {


    @FXML
    private TableColumn<Customer,String> colAddress;

    @FXML
    private TableColumn<Customer,String> colCategory;

    @FXML
    private TableColumn<Customer,String> colCity;

    @FXML
    private TableColumn<Customer,Date> colDate;

    @FXML
    private TableColumn<Customer,String> colEmail;

    @FXML
    private TableColumn<Customer,String> colMobile;

    @FXML
    private TableColumn<Customer,String> colName;

    @FXML
    private TableColumn<Customer,ImageView> colPicture;

    @FXML
    private ComboBox<String> comboCategory;

    @FXML
    private TableView<Customer> tableCustomers;


    ObservableList<Customer> customerList =
            FXCollections.observableArrayList();

    // This method runs automatically when FXML loads

    @FXML
    public void initialize(){
        fillCombo();
        colMobile.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getMobile()));

        colName.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getName()));

        colAddress.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getAddress()));

        colCity.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getCity()));

        colEmail.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getEmail()));

        colCategory.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getCategory()));

        colDate.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().getDate()));

        colPicture.setCellValueFactory(data ->{
            byte[] imageBytes = data.getValue().getPicture();
            ImageView imageView = new ImageView();
            if(imageBytes != null) {
                Image image = new Image(new ByteArrayInputStream(imageBytes),
                        60, 60, true, true);
                imageView.setImage(image);
            }
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);

            return new javafx.beans.property.SimpleObjectProperty<>(imageView);
        });


    }

    private void fillCombo(){
        try{

            Connection con = DBConnection.getConnection();
            String sql = "select distinct ctype from customers";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            comboCategory.getItems().add("All");
            while(rs.next()){
                comboCategory.getItems().add(rs.getString("ctype"));

            }

            comboCategory.setValue("All");
            con.close();
        }
        catch(Exception e){
            e.printStackTrace();

        }

    }

    @FXML
    void doShowData(ActionEvent event){


        String category = comboCategory.getValue();
        customerList.clear();
        try{

            Connection con = DBConnection.getConnection();
            String sql;
            PreparedStatement pst;

            if(category.equals("All")){
                sql = "select mobile,cname,address,city,email,ctype,doe,pic " +"from customers";
                pst = con.prepareStatement(sql);
            }
            else{
                sql = "select mobile,cname,address,city,email,ctype,doe,pic " + "from customers where ctype=?";
                pst = con.prepareStatement(sql);
                pst.setString(1,category);
            }

            ResultSet rs = pst.executeQuery();
            while(rs.next()){

                Customer c =
                        new Customer(
                                rs.getString("mobile"),
                                rs.getString("cname"),
                                rs.getString("address"),
                                rs.getString("city"),
                                rs.getString("email"),
                                rs.getString("ctype"),
                                rs.getDate("doe"),
                                rs.getBytes("pic"));

                customerList.add(c);
            }
            tableCustomers.setItems(customerList);
            con.close();
        }
        catch(Exception e){
            e.printStackTrace();

        }
    }

    @FXML
    void doExcel(ActionEvent event) {

        if(customerList.isEmpty()){

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText(
                    "No data available to export"
            );
            alert.show();
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Excel File");

        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Excel Files (*.xlsx)", "*.xlsx")
                );

        File file = fileChooser.showSaveDialog(tableCustomers.getScene().getWindow());

        if(file == null)
            return;

        try{
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Customers");

            // Header Row
            Row header = sheet.createRow(0);
            String[] columns = {
                    "Mobile",
                    "Name",
                    "Address",
                    "City",
                    "Email",
                    "Category",
                    "Date"

            };

            for(int i=0;i<columns.length;i++){

                Cell cell = header.createCell(i);
                cell.setCellValue(columns[i]);
            }
            // Data Rows
            int rowIndex = 1;
            for(Customer c : customerList){

                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(c.getMobile());
                row.createCell(1).setCellValue(c.getName());
                row.createCell(2).setCellValue(c.getAddress());
                row.createCell(3).setCellValue(c.getCity());
                row.createCell(4).setCellValue(c.getEmail());
                row.createCell(5).setCellValue(c.getCategory());
                row.createCell(6).setCellValue(c.getDate().toString()
                );
            }

            // Auto column size
            for(int i=0;i<columns.length;i++){
                sheet.autoSizeColumn(i);
            }
            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.close();
            workbook.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Excel exported successfully");
            alert.show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    @FXML
    void doPDF(ActionEvent event){

        if(customerList.isEmpty()){

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("No data available to export");
            alert.show();
            return;
        }

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Customer Report");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        File file = chooser.showSaveDialog(tableCustomers.getScene().getWindow());
        if(file == null) return;

        try{

            Document document = new Document(PageSize.A4.rotate(),
                    40, 40,
                    50, 40);

            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            // Title
            Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD, Color.BLUE);
            Paragraph title = new Paragraph("Customer Details Report", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);

            document.add(title);
            document.add(new Paragraph("\n"));

            Font dateFont = new Font(Font.HELVETICA, 10);
            Paragraph generated = new Paragraph("Generated Date : " + new java.util.Date(), dateFont);
            document.add(generated);
            document.add(new Paragraph("\n"));

            PdfPTable table = new PdfPTable(7);

            table.setWidthPercentage(100);

            // Column widths
            float widths[] = {
                    15, // Mobile
                    18, // Name
                    25, // Address
                    15, // City
                    25, // Email
                    12, // Category
                    15  // Date

            };
            table.setWidths(widths);
            String headers[] = {
                    "Mobile",
                    "Name",
                    "Address",
                    "City",
                    "Email",
                    "Category",
                    "Date"
            };
            for(String h: headers){

                PdfPCell cell = new PdfPCell(new Phrase(h));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(new Color(180, 200, 240));
                cell.setPadding(8);
                table.addCell(cell);
            }
            for(Customer c : customerList){

                addCell(table,c.getMobile());
                addCell(table,c.getName());
                addCell(table,c.getAddress());
                addCell(table,c.getCity());
                addCell(table,c.getEmail());
                addCell(table,c.getCategory());
                addCell(table, c.getDate().toString());
            }
            document.add(table);
            document.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("PDF Generated Successfully");
            alert.show();
        }
        catch(Exception e){

            e.printStackTrace();
        }
    }

    private void addCell(PdfPTable table, String value){

        PdfPCell cell = new PdfPCell(new Phrase(value));
        cell.setPadding(6);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
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

}