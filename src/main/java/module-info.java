module com.juanrdzbaeza.javaphotowidget {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires java.sql;
    requires spring.beans;
    requires spring.context;

    opens com.juanrdzbaeza.javaphotowidget to javafx.fxml;
    exports com.juanrdzbaeza.javaphotowidget;
    exports com.juanrdzbaeza.javaphotowidget.api;
    opens com.juanrdzbaeza.javaphotowidget.api to javafx.fxml;
}