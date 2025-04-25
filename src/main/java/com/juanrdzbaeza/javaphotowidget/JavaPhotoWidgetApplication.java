package com.juanrdzbaeza.javaphotowidget;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class JavaPhotoWidgetApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(JavaPhotoWidgetApplication.class.getResource("JavaPhotoWidget-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1080, 1080);
        stage.setTitle("JavaPhotoWidget!");
        stage.getIcons().add(new Image(Objects.requireNonNull(JavaPhotoWidgetApplication.class.getResourceAsStream("icon.png")))); // Agrega el ícono
        stage.setScene(scene);
        stage.setAlwaysOnTop(true); // Mantiene la ventana siempre encima
        stage.setResizable(false); // Impide redimensionar la ventana
        //stage.setMaximized(false); // Evita que la ventana se maximice
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}