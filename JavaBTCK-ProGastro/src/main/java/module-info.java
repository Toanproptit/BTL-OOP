module org.example.progastro {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.jetbrains.annotations;
    requires com.google.gson;
    requires org.json;
    requires org.apache.poi.ooxml;
    exports model;
    opens model to com.google.gson;
    opens org.example.progastro to javafx.fxml;
    exports org.example.progastro;
    exports controller;
    opens controller to javafx.fxml;
}