package com.juanrdzbaeza.javaphotowidget.api;

import javafx.scene.image.Image;

import java.util.List;

public interface IImageDatabase {

    void saveImageToDatabase(String imagePath, String imageName);

    List<Image> loadImagesFromDatabase();

}
