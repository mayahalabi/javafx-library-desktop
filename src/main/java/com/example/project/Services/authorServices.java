package com.example.project.Services;

import com.example.project.Classes.author;
import com.example.project.Connection.DBHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.project.Services.bookServices.checkBooksByAuthor;
import static com.example.project.Settings.settingAdminForm.center;
import static com.example.project.loginOptions.showAlert;

/**
 * This class provides services for managing authors in the application.
 * It includes methods for populating the author table, adding, updating,
 * deleting authors, and checking author existence in the database.
 */

public class authorServices {

    public static TableView<author> tableViewAuthor; // TableView to display authors
    public static TextField firstnameField, lastnameField; // TextFields for author input

    /**
     * Populates the author table by retrieving data from the database.
     *
     * @return an ObservableList of authors retrieved from the database.
     */
    public static ObservableList<author> populateAuthorTable() {
        DBHandler db = new DBHandler(); // Initialize database handler
        Connection conn = db.getConnection(); // Establish database connection
        ObservableList<author> authorList = FXCollections.observableArrayList(); // List to hold authors

        String query = "SELECT * FROM author"; // SQL query to select all authors
        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            // Loop through the result set and populate the author list
            while (resultSet.next()) {
                int authorid = resultSet.getInt("author_id");
                String firstname = resultSet.getString("first_name");
                String lastname = resultSet.getString("last_name");
                authorList.add(new author(authorid, firstname, lastname)); // Add author to the list
            }
        } catch (SQLException exe) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error loading author data: " + exe.getMessage());
            exe.printStackTrace(); // Print stack trace for debugging
        } finally {
            db.closeConnection(); // Ensure the database connection is closed
        }
        return authorList; // Return the populated list of authors
    }

    /**
     * Adds a new author to the database.
     *
     * @param firstname the first name of the author.
     * @param lastname  the last name of the author.
     */
    public static void addAuthor(String firstname, String lastname) {
        // Check if the input fields are empty
        if (firstname.isEmpty() || lastname.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "All fields are required.");
            return; // Exit if fields are empty
        }

        try {
            DBHandler db = new DBHandler(); // Initialize database handler
            Connection conn = db.getConnection(); // Establish database connection

            // Check if the author already exists in the database
            if (authorExists(firstname, lastname)) {
                showAlert(Alert.AlertType.WARNING, "Warning", "This author already exists.");
                return; // Exit if author already exists
            }

            // Insert new author into the database
            String query = "INSERT INTO author (first_name, last_name) VALUES (?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, firstname); // Set first name
                statement.setString(2, lastname);   // Set last name

                int rowsInserted = statement.executeUpdate(); // Execute the insert
                if (rowsInserted > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "A new author has " +
                            "been added successfully.");
                    // Clear input fields after successful addition
                    firstnameField.clear();
                    lastnameField.clear();
                    // Refresh the author table
                    tableViewAuthor.setItems(populateAuthorTable());
                }
            } catch (SQLException exe) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error adding new author: " + exe.getMessage());
            } finally {
                db.closeConnection(); // Ensure the database connection is closed
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding author: "
                    + e.getMessage());
        }
    }

    /**
     * Checks if an author already exists in the database.
     *
     * @param firstname the first name of the author.
     * @param lastname  the last name of the author.
     * @return true if the author exists, false otherwise.
     */
    private static boolean authorExists(String firstname, String lastname) {
        DBHandler db = new DBHandler(); // Initialize database handler
        try {
            Connection conn = db.getConnection(); // Establish database connection

            String query = "SELECT COUNT(*) FROM author WHERE first_name = ? AND last_name = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, firstname); // Set first name
                statement.setString(2, lastname);   // Set last name
                ResultSet resultSet = statement.executeQuery(); // Execute query
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0; // Returns true if author exists
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error checking author existence: " +
                        e.getMessage());
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while checking author existence: "
                    + e.getMessage());
        } finally {
            db.closeConnection();
        }
        return false; // Return false if an error occurs
    }

    /**
     * Deletes an author from the database by their ID.
     *
     * @param authorId the ID of the author to be deleted.
     */
    public static void deleteAuthor(int authorId) {
        // Check if the author has books assigned to them
        if (checkBooksByAuthor(authorId)) {
            showAlert(Alert.AlertType.WARNING, "Warning", "This author has books assigned. Delete or " +
                    "reassign them before proceeding.");
            return; // Exit if author has books
        }

        DBHandler db = new DBHandler(); // Initialize database handler
        Connection conn = db.getConnection(); // Establish database connection

        String query = "DELETE FROM author WHERE author_id = ?"; // SQL query to delete author
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, authorId); // Set author ID
            int rowsDeleted = statement.executeUpdate(); // Execute delete
            if (rowsDeleted > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Author deleted successfully.");
                // Clear input fields after deletion
                firstnameField.clear();
                lastnameField.clear();
                // Refresh the author table
                tableViewAuthor.setItems(populateAuthorTable());
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "No author found with the specified id.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error deleting author: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
        } finally {
            db.closeConnection(); // Ensure the database connection is closed
        }
    }

    /**
     * Creates the TableView for displaying authors.
     * It sets up the columns and input fields for adding/updating authors.
     */
    public static void createAuthorsTable() {
        // Create the TableView and its columns
        tableViewAuthor = new TableView<>();
        tableViewAuthor.setPrefSize(1040, 800); // Set preferred size

        // Set up columns for author details
        TableColumn<author, String> authoridColumn = new TableColumn<>("Author Id");
        authoridColumn.setCellValueFactory(new PropertyValueFactory<>("author_id")); // Map author_id property

        TableColumn<author, String> firstnameColumn = new TableColumn<>("First Name");
        firstnameColumn.setCellValueFactory(new PropertyValueFactory<>("first_name")); // Map first_name property

        TableColumn<author, String> lastnameColumn = new TableColumn<>("Last Name");
        lastnameColumn.setCellValueFactory(new PropertyValueFactory<>("last_name")); // Map last_name property

        // Set equal width for each column
        double columnWidth = 300; // Desired width for each column
        authoridColumn.setPrefWidth(columnWidth);
        firstnameColumn.setPrefWidth(columnWidth);
        lastnameColumn.setPrefWidth(columnWidth);

        // Add columns to the TableView
        tableViewAuthor.getColumns().addAll(authoridColumn, firstnameColumn, lastnameColumn);

        // Create the input fields and labels for author information
        Label firstnameLabel = new Label("First Name:");
        firstnameField = new TextField();
        firstnameField.setPromptText("Enter firstname"); // Set placeholder text

        Label lastnameLabel = new Label("Last Name:");
        lastnameField = new TextField();
        lastnameField.setPromptText("Enter lastname"); // Set placeholder text

        // Button to add a new author
        Button addButton = new Button("Add Author");
        // Add author on button click
        addButton.setOnAction(e -> addAuthor(firstnameField.getText(), lastnameField.getText()));

        // Button to delete an author
        Button deleteButton = new Button("Delete author");
        // Delete author on button click
        deleteButton.setOnAction(e -> deleteAuthor(getAuthorId(firstnameField.getText(), lastnameField.getText())));

        // Button to update an author
        Button updateButton = new Button("Update Author");
        updateButton.setOnAction(e -> {
            // Retrieve the currently selected item in the event handler
            author selectedItem = tableViewAuthor.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                // Update selected author
                updateAuthor(selectedItem.getAuthor_id(), firstnameField.getText(), lastnameField.getText());
            } else {
                // Show error if no author is selected
                showAlert(Alert.AlertType.ERROR, "Error", "Author not selected.");
            }
        });

        // Create a VBox to hold the input fields and buttons
        VBox inputBox = new VBox(10); // Vertical box with spacing of 10
        inputBox.setPadding(new Insets(10)); // Padding around the input box
        inputBox.getChildren().addAll(firstnameLabel, firstnameField, lastnameLabel, lastnameField, addButton,
                deleteButton, updateButton);

        // Create a BorderPane and set the VBox to the left and the TableView to the center
        BorderPane layoutt = new BorderPane();
        layoutt.setLeft(inputBox); // Set input box to the left
        layoutt.setCenter(tableViewAuthor); // Set TableView to the center

        // Populate the TableView with data
        tableViewAuthor.setItems(populateAuthorTable());

        // Add listener to the TableView for row selection
        tableViewAuthor.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Fill input fields with the selected author's information
                firstnameField.setText(newValue.getFirst_name());
                lastnameField.setText(newValue.getLast_name());
            }
        });

        center.add(layoutt, 0, 0); // Add layout to the center of the main UI
    }

    /**
     * Retrieves the author ID based on their first and last name.
     *
     * @param firstname the first name of the author.
     * @param lastname  the last name of the author.
     * @return the author ID if found, otherwise -1.
     */
    public static int getAuthorId(String firstname, String lastname) {
        DBHandler db = new DBHandler(); // Initialize database handler
        try {
            Connection conn = db.getConnection(); // Establish database connection

            if (conn == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Database connection not established.");
                return -1; // Change to -1 to indicate failure
            }

            String query = "SELECT author_id FROM author WHERE first_name = ? AND last_name = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, firstname); // Set first name
                statement.setString(2, lastname);   // Set last name
                ResultSet resultSet = statement.executeQuery(); // Execute query
                if (resultSet.next()) {
                    return resultSet.getInt("author_id"); // Return the found author ID
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error fetching author: " + e.getMessage());
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error fetching author: " + e.getMessage());
        } finally {
            db.closeConnection();
        }
        return -1; // Return -1 to indicate that the author does not exist
    }

    /**
     * Updates an existing author's information.
     *
     * @param authorId    the ID of the author to be updated.
     * @param newFirstName the new first name of the author.
     * @param newLastName  the new last name of the author.
     */
    public static void updateAuthor(int authorId, String newFirstName, String newLastName) {
        DBHandler db = new DBHandler(); // Initialize database handler
        Connection conn = db.getConnection(); // Establish database connection

        // Query to check existing author
        String checkQuery = "SELECT first_name, last_name FROM author WHERE author_id = ?";
        // Query to update author details
        String updateQuery = "UPDATE author SET first_name = ?, last_name = ? WHERE author_id = ?";

        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, authorId); // Set author ID for checking
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                String currentFirstName = rs.getString("first_name"); // Get current first name
                String currentLastName = rs.getString("last_name");   // Get current last name

                // Check if the new names are the same as the existing ones
                if (newFirstName.equals(currentFirstName) && newLastName.equals(currentLastName)) {
                    showAlert(Alert.AlertType.WARNING, "Warning", "Change something to be updated.");
                    return; // Exit if no changes were made
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "No author found with the provided ID.");
                return; // Exit if author does not exist
            }

            // Proceed with the update
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                updateStmt.setString(1, newFirstName); // Set new first name
                updateStmt.setString(2, newLastName);   // Set new last name
                updateStmt.setInt(3, authorId); // Set author ID for updating

                int rowsUpdated = updateStmt.executeUpdate(); // Execute the update
                if (rowsUpdated > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Author updated successfully.");
                    // Optionally update the author table view
                    tableViewAuthor.setItems(populateAuthorTable()); // Refresh the table view
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "No author found with the provided ID.");
                }
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error updating author: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
        } finally {
            db.closeConnection(); // Ensure the database connection is closed
        }
    }

}
