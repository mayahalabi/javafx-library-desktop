package com.example.project.Services;

import com.example.project.Connection.DBHandler;
import javafx.scene.control.Alert;

import java.sql.*;

import static com.example.project.Services.bookServices.imagepathField;
import static com.example.project.loginOptions.showAlert;

/**
 * Service class for managing image data in the database.
 * This class provides methods to retrieve and add images to the database.
 */
public class imageServices {

    /**
     * Retrieves the ID of an image based on its path.
     *
     * @param image_path the path of the image to be fetched.
     * @return the ID of the image if found, otherwise -1.
     */
    public static int getImageId(String image_path) {
        // Initialize the database handler and connection
        DBHandler db = new DBHandler();
        Connection conn = db.getConnection();

        String query = "SELECT image_id FROM images WHERE image_path = ?"; // SQL query to retrieve image ID
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, image_path); // Set the image path parameter
            ResultSet resultSet = statement.executeQuery(); // Execute the query
            if (resultSet.next()) {
                return resultSet.getInt("image_id"); // Return the image ID if found
            }
        } catch (SQLException e) {
            // Handle SQL exceptions and show an error alert
            showAlert(Alert.AlertType.ERROR, "Error", "Error fetching image: " + e.getMessage());
        } finally {
            db.closeConnection(); // Close the database connection
        }
        return -1; // Return -1 if the image does not exist
    }

    /**
     * Adds a new image to the database.
     *
     * @param imagePath the path of the image to be added.
     */
    public static void addImage(String imagePath) {
        // Initialize the database handler and connection
        DBHandler db = new DBHandler();
        Connection conn = db.getConnection();

        String query2 = "INSERT INTO images (image_path) VALUES (?)"; // SQL query for inserting a new image

        try (PreparedStatement statement2 = conn.prepareStatement(query2, Statement.RETURN_GENERATED_KEYS)) {
            statement2.setString(1, imagePath); // Set the image path parameter

            // Execute the update and check how many rows were inserted
            int rowsInserted2 = statement2.executeUpdate();
            if (rowsInserted2 > 0) {
                ResultSet generatedKeys = statement2.getGeneratedKeys(); // Get generated keys for the new row
                if (generatedKeys.next()) {
                    int imageID = generatedKeys.getInt(1); // Retrieve the generated image ID
                    // Optionally, you could do something with the imageID here, like logging or processing
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to retrieve image ID.");
                    return; // Exit if no ID was retrieved
                }
                imagepathField.clear(); // Clear the image path input field
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Error adding new image.");
            }
        } catch (SQLException e) {
            // Handle SQL exceptions and show an error alert
            showAlert(Alert.AlertType.ERROR, "Error", "Error adding new image: " + e.getMessage());
        } finally {
            db.closeConnection(); // Ensure the database connection is closed
        }
    }
}
