package com.juanrdzbaeza.javaphotowidget.api;

import org.springframework.stereotype.Service;

import java.sql.*;

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


}
