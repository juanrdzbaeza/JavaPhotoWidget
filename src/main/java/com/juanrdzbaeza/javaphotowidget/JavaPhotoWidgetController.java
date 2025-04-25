package com.juanrdzbaeza.javaphotowidget;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.nio.file.Paths;
import java.util.Objects;


public class JavaPhotoWidgetController {

    @FXML
    private HBox topControls;

    @FXML
    private ImageView imageView;

    private final List<Image> images = new ArrayList<>();
    private int currentImageIndex = 0;
    private Timeline timeline;

    private final JavaPhotoWidgetLogic javaPhotoWidgetLogic = new JavaPhotoWidgetLogic(
            new com.juanrdzbaeza.javaphotowidget.api.ImageDatabase()
    );

    @FXML
    private void initialize() {

        // Obtener todas las imágenes del directorio "resources/images"
        File[] imageFiles = null;
        try {
            File imagesDir = new File(Objects.requireNonNull(JavaPhotoWidgetApplication.class.getResource("images")).getPath());
            imageFiles = imagesDir.listFiles((dir, name) -> name.matches("(?i).*\\.(png|jpg|jpeg|gif)"));
        } catch (Exception e) {
//            throw new RuntimeException(e);
        }

        if (imageFiles != null) {
            for (File file : imageFiles) {
                images.add(new Image(file.toURI().toString()));
            }
        }


        // Sí hay imágenes cargadas, iniciar el carrusel
        if (!images.isEmpty()) {
            startImageCarousel();
        }
        else {
            // Cargar imágenes desde la base de datos al iniciar
            images.addAll(javaPhotoWidgetLogic.loadImagesFromDatabase());
            if (!images.isEmpty())
                startImageCarousel();
        }

    }

    @FXML
    protected void onLoadImagesButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.JPEG", "*.PNG", "*.JPG", "*.GIF")
        );
        List<File> files = fileChooser.showOpenMultipleDialog(null);
        if (files != null) {
            loadImages(files);
        }
    }

    @FXML
    protected void onLoadDirectoryButtonClick() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File directory = directoryChooser.showDialog(null);
        if (directory != null && directory.isDirectory()) {
//            File[] files = directory.listFiles((dir, name) -> name.matches(".*\\.(png|jpg|jpeg|gif)"));
            File[] files = directory.listFiles((dir, name) -> name.matches("(?i).*\\.(png|jpg|jpeg|gif)"));
            if (files != null) {
                loadImages(List.of(files));
            }
        }
    }

    private void loadImages(List<File> files) {
        images.clear();
        ArrayList<File> filesDisordered = barajar(files);
        for (File file : filesDisordered) {
            images.add(new Image(file.toURI().toString()));
        }
        if (!images.isEmpty()) {
            // Oculta los botones
            topControls.setVisible(false);
            topControls.setManaged(false);

            // Inicia el carrusel de imágenes
            startImageCarousel();
        }
    }

    private ArrayList<File> barajar(List<File> files) {
        ArrayList<File> filesDisordered = new ArrayList<>(files);
        Collections.shuffle(filesDisordered);
        return filesDisordered;
    }

    private void startImageCarousel() {
        if (timeline != null) {
            timeline.stop();
        }
        currentImageIndex = 0;
        imageView.setImage(images.get(currentImageIndex));
        timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
            currentImageIndex = (currentImageIndex + 1) % images.size();
            imageView.setImage(images.get(currentImageIndex));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // Evento para mostrar los botones al hacer clic en la imagen
        imageView.setOnMouseClicked(event -> {
            imageView.setFitHeight(1040);
            try {
                javaPhotoWidgetLogic.saveImageToDatabase(
                        Paths.get(new java.net.URI(imageView.getImage().getUrl())).toString(),
                        imageView.getImage().getUrl().substring(imageView.getImage().getUrl().lastIndexOf("/") + 1)
                );
            } catch (URISyntaxException e) {
                // throw new RuntimeException(e);
            }
            topControls.setVisible(true);
            topControls.setManaged(true);
            if (timeline != null) {
                timeline.stop();
            }
        });

        // Ejecutar lógica después de que la escena esté lista
        javafx.application.Platform.runLater(() -> {
            // Evento para ocultar los botones al salir del ImageView
            imageView.getScene().setOnMouseExited(
                    event -> {
                        imageView.setFitHeight(1080);
                        topControls.setVisible(false);
                        topControls.setManaged(false);
                        if (timeline != null) {
                            timeline.play();
                        }
                    }
            );
        });

    }

}