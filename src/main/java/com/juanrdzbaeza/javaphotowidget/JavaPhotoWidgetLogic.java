package com.juanrdzbaeza.javaphotowidget;

import com.juanrdzbaeza.javaphotowidget.api.IImageDatabase;
import javafx.scene.image.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JavaPhotoWidgetLogic {

    private final IImageDatabase imageDatabase;

    @Autowired
    public JavaPhotoWidgetLogic(IImageDatabase imageDatabase) {
        this.imageDatabase = imageDatabase;
    }

    public void saveImageToDatabase(String imageClickedPath, String imageClickedName) {
        imageDatabase.saveImageToDatabase(imageClickedPath, imageClickedName);
    }

    public List<Image> loadImagesFromDatabase() {
        return imageDatabase.loadImagesFromDatabase();
    }

}
