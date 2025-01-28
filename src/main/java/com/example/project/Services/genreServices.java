package com.example.project.Services;

import com.example.project.Classes.genre;
import com.example.project.Connection.DBHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.project.Settings.settingAdminForm.center;
import static com.example.project.loginOptions.showAlert;

/**
 * The genreServices class provides various services related to the 'genre' data model.
 * This includes operations like populating the genre table, adding, updating, deleting genres,
 * and performing genre checks within a database.
 *
 * It connects to the database and performs CRUD (Create, Read, Update, Delete) operations
 * on the 'genre' table. This class uses the 'DBHandler' class to establish connections to
 * the database and execute SQL queries.
 */
public class genreServices {

    public static TableView<genre> tableView;
    public static TextField genretypeField;

    /**
     * Populates the genre table with data from the database.
     *
     * This method connects to the database, executes a SQL query to retrieve all genres,
     * and adds each genre to an ObservableList, which is then returned to be displayed in the TableView.
     * It handles any SQL exceptions and ensures the database connection is closed after use.
     *
     * @return ObservableList<genre> The list of genres to be displayed in the TableView.
     */
    public static ObservableList<genre> populateGenreTable() {
        DBHandler db = new DBHandler();
        Connection conn = db.getConnection();
        ObservableList<genre> genreList = FXCollections.observableArrayList();

        // SQL query to retrieve all genres (genre_id and type)
        String query = "SELECT genre_id, type FROM genre";

        // Use try-with-resources to automatically close PreparedStatement and ResultSet
        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            // Iterate through the result set and add each genre to the ObservableList
            while (resultSet.next()) {
                // Fetch the genre_id as an integer and type as a string
                int thisgenreid = resultSet.getInt("genre_id");
                String thistype = resultSet.getString("type");

                // Add the new genre object to the list
                genreList.add(new genre(thisgenreid, thistype));
            }
        } catch (SQLException exe) {
            // Show error message if there is an issue executing the query
            showAlert(Alert.AlertType.ERROR, "Error", "Error loading genre data: " + exe.getMessage());
            exe.printStackTrace();
        } finally {
            // Ensure the database connection is closed
            db.closeConnection();
        }
        // Return the populated ObservableList
        return genreList;
    }

    /**
     * Adds a new genre to the genre table in the database.
     *
     * This method checks if the genre type is not empty, verifies that the genre does not already exist,
     * and then inserts the new genre into the database. If the operation is successful, the genre TableView
     * is updated and the input field is cleared. If there are any errors, appropriate alerts are shown.
     *
     * @param thistype The genre type to be added.
     */
    public static void addGenre(String thistype) {
        // Check if the genre type field is empty
        if (thistype.isEmpty()) {
            // Show an error message if the genre type is not provided
            showAlert(Alert.AlertType.ERROR, "Error", "The genre field is required.");
            return;
        }

        // Create a database handler to interact with the database
        DBHandler db = new DBHandler();
        try (Connection conn = db.getConnection()) {

            // Check if the genre already exists in the database
            if (genreExists(thistype)) {
                showAlert(Alert.AlertType.WARNING, "Warning", "This genre already exists.");
                return;
            }

            // SQL query to insert a new genre into the database
            String query = "INSERT INTO genre (type) VALUES (?)";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                // Set the genre type parameter in the SQL query
                statement.setString(1, thistype);

                // Execute the query to insert the genre
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    // Show a success message if the genre was added successfully
                    showAlert(Alert.AlertType.INFORMATION, "Success", "A new genre has been added successfully.");
                    // Clear the input field for the genre type
                    genretypeField.clear();
                    // Refresh the genre TableView with the updated list of genres
                    tableView.setItems(populateGenreTable());
                }
            } catch (SQLException exe) {
                // Show an error message if there is an issue executing the insert query
                showAlert(Alert.AlertType.ERROR, "Error", "Error adding new genre: " + exe.getMessage());
            }
        } catch (Exception e) {
            // Show an error message if there is a connection issue or other exception
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding genre: " + e.getMessage());
        } finally {
            // Ensure the database connection is closed after the operation
            db.closeConnection();
        }
    }

    /**
     * Deletes the selected genre from the genre table in the database.
     *
     * This method retrieves the selected genre from the TableView, checks if a genre is selected,
     * and then executes a SQL DELETE statement to remove the genre with the specified genre ID.
     * After deletion, the genre TableView is refreshed to reflect the changes.
     */
    public static void deleteGenre() {
        // Retrieve the selected genre from the TableView
        genre selectedGenre = tableView.getSelectionModel().getSelectedItem();

        // Check if a genre has been selected
        if (selectedGenre == null) {
            // If no genre is selected, show an error alert and return
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a genre to delete.");
            return;
        }

        // Retrieve the genre ID from the selected genre
        int thisGenreid = selectedGenre.getGenre_id();

        // Establish a connection to the database
        DBHandler db = new DBHandler();
        Connection conn = db.getConnection();

        // SQL query to delete the genre based on its genre_id
        String query = "DELETE FROM genre WHERE genre_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            // Set the genre_id parameter for the query
            statement.setInt(1, thisGenreid);

            // Execute the DELETE statement and get the number of affected rows
            int rowsDeleted = statement.executeUpdate();

            // Check if the deletion was successful
            if (rowsDeleted > 0) {
                // Show a success message if the genre was deleted
                showAlert(Alert.AlertType.INFORMATION, "Success", "Genre deleted successfully.");
                // Clear the genre type input field
                genretypeField.clear();
                // Refresh the TableView with the updated list of genres
                tableView.setItems(populateGenreTable());
            } else {
                // Show an error message if no genre with the specified ID was found
                showAlert(Alert.AlertType.ERROR, "Error", "No genre found with the specified id.");
            }
        } catch (SQLException exe) {
            // Show an error message if there was a problem executing the SQL query
            showAlert(Alert.AlertType.ERROR, "Error", "Error deleting genre: " + exe.getMessage());
            exe.printStackTrace();
        } finally {
            // Ensure the database connection is closed after the operation
            db.closeConnection();
        }
    }

    /**
     * Creates and sets up the genre table and the corresponding user interface elements (input fields, buttons).
     *
     * This method creates a `TableView` for displaying genre data, adds columns for genre ID and genre type,
     * and sets up input fields and buttons for adding, deleting, and updating genres.
     * It also handles interactions such as adding genre data, deleting selected genres,
     * and updating the genre type.
     */
    public static void createGenreTable(){
        // Create the TableView and its columns for displaying genres
        tableView = new TableView<>();
        tableView.setPrefSize(1040, 800);

        // Genre ID column - displays the genre's ID
        TableColumn<genre, String> genreidColumn = new TableColumn<>("Genre Id");
        genreidColumn.setCellValueFactory(new PropertyValueFactory<>("genre_id"));

        // Genre type column - displays the genre's type
        TableColumn<genre, String> genretypeColumn = new TableColumn<>("Genre type");
        genretypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        // Set equal width for each column for consistency
        double columnWidth = 520; // Desired width for each column
        genreidColumn.setPrefWidth(columnWidth);
        genretypeColumn.setPrefWidth(columnWidth);

        // Add the columns to the table
        tableView.getColumns().addAll(genreidColumn, genretypeColumn);

        // Create the input fields and labels for adding/updating genre data
        Label genretypeLabel = new Label("Genre type:");
        genretypeField = new TextField();
        genretypeField.setPromptText("Enter genre type");

        // Button to add a new genre
        Button addButton = new Button("Add Genre");
        addButton.setOnAction(e -> addGenre(genretypeField.getText()));

        // Button to delete the selected genre
        Button deleteButton = new Button("Delete Genre");
        deleteButton.setOnAction(e -> deleteGenre());

        // Button to update the selected genre's type
        Button updateButton = new Button("Update Genre");
        updateButton.setOnAction(e -> {
            // Retrieve the currently selected item in the event handler
            genre selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                updateGenre(selectedItem.getGenre_id(), genretypeField.getText());
            } else {
                // Show an error if no genre is selected
                showAlert(Alert.AlertType.ERROR, "Error", "Genre not selected.");
            }
        });

        // Create a VBox to hold the input fields and buttons for genre operations
        VBox inputBox = new VBox(10);
        inputBox.setPadding(new Insets(10));
        inputBox.getChildren().addAll(genretypeLabel, genretypeField, addButton, deleteButton, updateButton);

        // Create a BorderPane layout and add the VBox and TableView
        BorderPane layoutt = new BorderPane();
        layoutt.setLeft(inputBox);
        layoutt.setCenter(tableView);

        // Populate the TableView with data (genres) from the database
        tableView.setItems(populateGenreTable());

        // Add a listener to the TableView for row selection, updating input fields with the selected genre's data
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                genretypeField.setText(newValue.getType());
            }
        });

        // Add the layout to the center region of the parent container (assumed to be a GridPane or similar)
        center.add(layoutt, 0, 0);
    }

    /**
     * Retrieves a genre from the database based on the specified genre ID.
     *
     * This method queries the `genre` table to retrieve the details of a genre with a given genre ID.
     * It returns a `genre` object if a genre with the specified ID exists, or `null` if no such genre is found.
     *
     * @param genreId The ID of the genre to be fetched.
     * @return A `genre` object representing the genre with the specified ID, or `null` if no genre is found.
     */
    public static genre getAllGenres(int genreId) {
        DBHandler db = new DBHandler();
        Connection conn = db.getConnection();
        String sql = "SELECT * FROM genre WHERE genre_id = ?"; // SQL query to fetch genre by ID

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            // Set the genreId parameter before executing the query
            statement.setInt(1, genreId);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {  // Check if result exists (only one genre is expected)
                String type = rs.getString("type"); // Retrieve genre type from the result set
                return new genre(genreId, type); // Return the genre object with the retrieved data
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print the stack trace if an exception occurs
        } finally {
            db.closeConnection(); // Ensure the database connection is closed
        }
        return null; // Return null if no genre with the specified ID is found
    }

    /**
     * Checks if a genre with the specified genre ID exists in the database.
     *
     * This method queries the `genre` table to check if a genre with the given `genreid` exists.
     * It returns `true` if the genre exists, and `false` if it does not.
     *
     * @param genreid The ID of the genre to be checked.
     * @return A boolean indicating whether the genre exists in the database.
     */
    public  static boolean checkgenreId(String genreid) {
        DBHandler db = new DBHandler();
        try {
            Connection conn = db.getConnection();

            // SQL query to check if the genre with the specified ID exists in the database
            String query = "SELECT * FROM genre WHERE genre_id = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, genreid); // Set the genre ID parameter

                // Execute the query and check if any genre exists with the provided genre ID
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) { // If there's a result, genre exists
                    return true; // Genre found
                } else {
                    // If no genre found, show an error alert
                    showAlert(Alert.AlertType.ERROR, "Error", "Genre does not exist.");
                }
            }
        } catch (SQLException e) {
            // sql error
            e.printStackTrace();
        } finally {
            db.closeConnection();  // Ensure the connection is closed
        }
        return false; // Genre not found
    }

    /**
     * Checks if a genre with the specified type exists in the database.
     *
     * This method queries the database to check if a genre of the given type already exists
     * in the `genre` table. It returns `true` if the genre exists, and `false` if it doesn't.
     *
     * @param thistype The type of the genre to be checked (e.g., "Fiction", "Science Fiction").
     * @return A boolean indicating whether the genre exists in the database.
     */
    private static boolean genreExists(String thistype) {
        DBHandler db = new DBHandler();
        try {
            Connection conn = db.getConnection();

            // SQL query to check if the genre with the specified type exists in the database
            String query = "SELECT COUNT(*) FROM genre WHERE type = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, thistype);
                ResultSet resultSet = statement.executeQuery();

                // If the genre type exists, return true; otherwise, return false
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0; // Returns true if genre exists
                }
            } catch (SQLException e) {
                // Show an error alert if there's a SQL exception
                showAlert(Alert.AlertType.ERROR, "Error", "Error checking genre existence: " + e.getMessage());
            }
        } catch (Exception e) {
            // Show an error alert for other exceptions
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while checking genre existence: " + e.getMessage());
        } finally {
            db.closeConnection(); // Ensure the database connection is closed
        }
        return false; // Return false if an error occurs or genre does not exist
    }

    /**
     * Updates the genre type for a given genre ID.
     *
     * This method checks if a genre with the given genre ID exists in the database.
     * If the genre exists and the new genre type is different from the current one,
     * it updates the genre type. If the new genre type is the same as the current one,
     * it displays a warning message.
     * If the genre with the provided genre ID does not exist, it shows an error message.
     *
     * @param genreId The ID of the genre to be updated.
     * @param newGenreType The new genre type to set for the given genre ID.
     */
    public static void updateGenre(int genreId, String newGenreType) {
        DBHandler db = new DBHandler();
        Connection conn = db.getConnection();

        // Query to check the current genre type for the provided genre ID
        String checkQuery = "SELECT type FROM genre WHERE genre_id = ?";
        // Query to update the genre type for the provided genre ID
        String updateQuery = "UPDATE genre SET type = ? WHERE genre_id = ?";

        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            // Set the genre ID parameter for the check query
            checkStmt.setInt(1, genreId);
            ResultSet rs = checkStmt.executeQuery();

            // Check if the genre exists
            if (rs.next()) {
                String currentGenreType = rs.getString("type");

                // Check if the new genre type is the same as the existing one
                if (newGenreType.equals(currentGenreType)) {
                    showAlert(Alert.AlertType.WARNING, "Warning", "Change something to be updated.");
                    return; // Exit method if the genre type is the same
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "No genre found with the provided ID.");
                return; // Exit method if no genre is found
            }

            // Proceed with the update if the genre exists and the new genre type is different
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                updateStmt.setString(1, newGenreType); // Set the new genre type
                updateStmt.setInt(2, genreId); // Set the genre ID for the update

                // Execute the update query
                int rowsUpdated = updateStmt.executeUpdate();
                if (rowsUpdated > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Info", "Genre updated successfully.");
                    // Update the genre table view after successful update
                    tableView.setItems(populateGenreTable());
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "No genre found with the provided ID.");
                }
            }
        } catch (SQLException e) {
            // Handle SQL exceptions and show an error alert
            showAlert(Alert.AlertType.ERROR, "Error", "Error updating genre: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Ensure the database connection is closed
            db.closeConnection();
        }
    }

    /**
     * Retrieves the genre ID for a given genre type.
     *
     * This method queries the database to fetch the genre ID based on the provided genre type.
     * If the genre type exists in the database, it returns the corresponding genre ID.
     * If the genre type is not found, it returns -1 to indicate that the genre does not exist.
     *
     * @param type The genre type to search for (e.g., "Fiction", "Non-Fiction").
     * @return The genre ID if found, or -1 if the genre does not exist in the database.
     */
    public static int getGenreId(String type) {
        DBHandler db = new DBHandler();
        try {
            Connection conn = db.getConnection();

            // SQL query to fetch genre_id based on the genre type
            String query = "SELECT genre_id FROM genre WHERE type = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                // Set the genre type parameter in the query
                statement.setString(1, type);

                // Execute the query and retrieve the result
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    // Return the genre_id if the genre type is found
                    return resultSet.getInt("genre_id");
                }
            } catch (SQLException e) {
                // Handle any SQL exceptions that may occur
                showAlert(Alert.AlertType.ERROR, "Error", "Error fetching genre: " + e.getMessage());
            }
        } catch (Exception e) {
            // Handle any exceptions that may occur during the connection or execution
            showAlert(Alert.AlertType.ERROR, "Error", "Error fetching genre: " + e.getMessage());
        } finally {
            // Ensure the database connection is closed after the operation
            db.closeConnection();
        }
        return -1; // Return -1 to indicate that the genre does not exist
    }
}
