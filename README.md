# JavaPhotoWidget

JavaPhotoWidget is a JavaFX-based application for managing and displaying images in a carousel. It allows users to load images from directories or files, display them in a slideshow, and save them to a SQLite database.

## Features

- Load images from a directory or individual files.
- Display images in a carousel with automatic transitions.
- Save images to a SQLite database.
- Retrieve and display images stored in the database.
- Shuffle images for random display order.

## Technologies Used

- **JavaFX**: For the graphical user interface.
- **SQLite**: For storing image data.
- **Spring Framework**: For dependency injection and service management.
- **Maven**: For project management and dependency resolution.

## Database Structure

The SQLite database `JPhW.sqlite` has the following structure:

```sql
CREATE TABLE images (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    data BLOB NOT NULL
);
```

## How to Run

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/juanrdzbaeza/JavaPhotoWidget.git
   cd JavaPhotoWidget
   ```

2. **Build the Project**:
   Ensure you have Maven installed. Run:
   ```bash
   mvn clean install
   ```

3. **Run the Application**:
   Execute the main class:
   ```bash
   mvn javafx:run
   ```

## Usage

1. **Load Images**:
    - Use the 🖻 button to load individual image files.
    - Use the 🗁 button to load all images from a directory.

2. **Image Carousel**:
    - The carousel starts automatically if images are loaded.
    - Click on an image to save it to the database.

3. **Database Integration**:
    - Images are saved to the SQLite database when clicked.
    - Images are loaded from the database if no images are found in the `resources/images` directory.

## Project Structure

- `src/main/java/com/juanrdzbaeza/javaphotowidget`: Main application logic.
- `src/main/java/com/juanrdzbaeza/javaphotowidget/api`: Database interaction logic.
- `src/main/resources`: Contains FXML files and default images.

## Dependencies

The project uses the following dependencies:

- **JavaFX**: For UI components.
- **SQLite JDBC**: For database connectivity.
- **Spring Framework**: For dependency injection.
- **Maven Plugins**: For building and running the application.

## License

This project is licensed under the [MIT License](LICENSE). See the `LICENSE` file for details.