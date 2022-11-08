module com.example.task03 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.example.task03 to javafx.fxml;
    exports com.example.task03;
}