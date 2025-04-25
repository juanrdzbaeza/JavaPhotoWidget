package com.juanrdzbaeza.javaphotowidget;

import com.juanrdzbaeza.javaphotowidget.api.IImageDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JavaPhotoWidgetLogic {

    private final IImageDatabase imageDatabase;

    @Autowired
    public JavaPhotoWidgetLogic(IImageDatabase imageDatabase) {
        this.imageDatabase = imageDatabase;
    }


}
