package org.example.progastro;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 650);
        scene.getStylesheets().add(getClass().getResource("/org/example/progastro/Login.css").toExternalForm());
        stage.setTitle("Login-progastro");
        stage.setScene(scene);
//        stage.setMaximized(true);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}

//java "-Dprism.order=sw" --module-path "C:\JAVA\JavaFX\Java Totorial\javafx-sdk-23.0.2\lib" --add-modules javafx.controls,javafx.fxml -jar ProGastro.jar
