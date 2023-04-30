module cmsy182fp.cmsy182finalproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens cmsy182fp.cmsy182finalproject to javafx.fxml;
    exports cmsy182fp.cmsy182finalproject;
}