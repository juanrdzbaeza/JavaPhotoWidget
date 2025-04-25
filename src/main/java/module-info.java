module com.juanrdzbaeza.javaphotowidget {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    

    opens com.juanrdzbaeza.javaphotowidget to javafx.fxml;
    exports com.juanrdzbaeza.javaphotowidget;


}