module com.example.gamelife {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.gamelife to javafx.fxml;
    exports com.example.gamelife;
}