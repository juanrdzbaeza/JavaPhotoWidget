package com.juanrdzbaeza.javaphotowidget.api;

import javafx.scene.image.Image;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ImageDatabase implements IImageDatabase {

    private final String url = "jdbc:sqlite:JPhW.sqlite";

    public ImageDatabase() {
        initializeDatabase();
    }

    private void initializeDatabase() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS images ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL, "
                + "data BLOB NOT NULL"
                + ");";

        try (Connection conn = DriverManager.getConnection(this.url);
             Statement stmt = conn.createStatement()) {

            stmt.execute(createTableSQL);
            System.out.println("Database initialized successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveImageToDatabase(String imagePath, String imageName) {


        String sql = "INSERT INTO images(name, data) VALUES(?, ?)";

        try (Connection conn = DriverManager.getConnection(this.url);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             FileInputStream fis = new FileInputStream(imagePath)) {

            pstmt.setString(1, imageName);
            pstmt.setBinaryStream(2, fis, (int) new File(imagePath).length());
            pstmt.executeUpdate();

            System.out.println("Image saved to database.");
        } catch (IOException | SQLException e) {
            Logger.getLogger(ImageDatabase.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    @Override
    public List<Image> loadImagesFromDatabase() {

        List<Image> images = new ArrayList<>();

        String sql = "SELECT data FROM images";

        try (Connection conn = DriverManager.getConnection(this.url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                byte[] imageBytes = rs.getBytes("data");
                ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
                Image image = new Image(bais);
                images.add(image);
            }
        } catch (SQLException e) {
            Logger.getLogger(ImageDatabase.class.getName()).log(Level.SEVERE, null, e);
        }

        return images;
    }

}