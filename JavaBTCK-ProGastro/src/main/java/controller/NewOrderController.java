package controller;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.net.URL;
import java.util.ResourceBundle;

public class NewOrderController{
    @FXML
    private ImageView searchIcon;
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Image image = new Image(getClass().getResource("/UML/icons8-search-48.png").toExternalForm());
            searchIcon.setImage(image);
        } catch (Exception e) {
            System.err.println("Không tìm thấy ảnh kính lúp: " + e.getMessage());
        }
    }
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/progastro/view/search.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Search Example");
        stage.setScene(scene);
        stage.show();
    }
}
