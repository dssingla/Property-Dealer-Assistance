module com.example.propertydealerassistance {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    requires com.github.librepdf.openpdf;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires jbcrypt;
    requires layout;


    opens com.example.propertydealerassistance to javafx.fxml;
    exports com.example.propertydealerassistance;

    opens CustomerMaster to javafx.fxml;
    exports CustomerMaster;

    opens Properties to javafx.fxml;
    exports Properties to javafx.fxml;

    opens Deals to javafx.fxml;
    exports Deals to javafx.fxml;

    opens Login to javafx.fxml;
    exports Login to javafx.fxml;

    opens Dashboard to javafx.fxml;
    exports Dashboard to javafx.fxml;

    opens BrowseCustomers to javafx.fxml, javafx.base;
    exports BrowseCustomers to javafx.fxml;

    opens PropertyFinder to javafx.fxml, javafx.base;
    exports PropertyFinder to javafx.fxml;

    opens DealsFinder to javafx.fxml, javafx.base;
    exports DealsFinder to javafx.fxml;

    opens Settings to javafx.fxml;
    exports Settings to javafx.fxml;

    opens Analytics to javafx.fxml;
    exports Analytics to javafx.fxml;
}