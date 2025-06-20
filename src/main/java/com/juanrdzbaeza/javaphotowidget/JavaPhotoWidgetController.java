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
            ArrayList<File> filesDisordered = barajar(List.of(imageFiles));
            for (File file : filesDisordered) {
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

        // Resize the window and assign an event after the scene is loaded
        javafx.application.Platform.runLater(() -> {
            // Obtener el Stage actual
            javafx.stage.Stage stage = (javafx.stage.Stage) imageView.getScene().getWindow();

            // Agregar un listener para imprimir el tamaño de la ventana al redimensionar
            stage.widthProperty().addListener((observable, oldValue, newValue) -> {
                System.out.println("Ancho de la ventana: " + newValue);
                imageView.setFitWidth((Double) newValue);
            });

            stage.heightProperty().addListener((observable, oldValue, newValue) -> {
                System.out.println("Alto de la ventana: " + newValue);
                imageView.setFitHeight((Double) newValue);
            });
        });
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

    @FXML
    protected void onLoadFromDBButtonClick() {
        // Cargar imágenes desde la base de datos
        List<Image> dbImages = javaPhotoWidgetLogic.loadImagesFromDatabase();

        if (dbImages.isEmpty()) {
            System.out.println("No hay imágenes en la base de datos.");
            return;
        }

        // Crear un diálogo para seleccionar imágenes
        javafx.scene.control.Dialog<List<Image>> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Seleccionar Imágenes");
        dialog.setHeaderText("Selecciona las imágenes para el carrusel");

        javafx.scene.control.ListView<Image> listView = new javafx.scene.control.ListView<>();
        listView.getItems().addAll(dbImages);
        listView.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);

        listView.setCellFactory(param -> new javafx.scene.control.ListCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(Image item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    imageView.setImage(item);
                    imageView.setPreserveRatio(true);
                    setGraphic(imageView);
                }
            }
        });

        dialog.getDialogPane().setContent(listView);
        dialog.getDialogPane().getButtonTypes().addAll(javafx.scene.control.ButtonType.OK, javafx.scene.control.ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == javafx.scene.control.ButtonType.OK) {
                return new ArrayList<>(listView.getSelectionModel().getSelectedItems());
            }
            return null;
        });

        // Mostrar el diálogo y obtener las imágenes seleccionadas
        dialog.showAndWait().ifPresent(selectedImages -> {
            if (!selectedImages.isEmpty()) {
                images.clear();
                images.addAll(selectedImages);
                startImageCarousel();
            }
        });
    }

    @FXML
    protected void onPreviousImageButtonClick() {

    }

    @FXML
    protected void onPauseButtonClick() {

    }


    @FXML
    protected void onNextImageButtonClick() {

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
            //imageView.setFitHeight(1040);
//            try {
//                javaPhotoWidgetLogic.saveImageToDatabase(
//                        Paths.get(new java.net.URI(imageView.getImage().getUrl())).toString(),
//                        imageView.getImage().getUrl().substring(imageView.getImage().getUrl().lastIndexOf("/") + 1)
//                );
//            } catch (URISyntaxException e) {
                // throw new RuntimeException(e);
//            }
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
                        //imageView.setFitHeight(1080);
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