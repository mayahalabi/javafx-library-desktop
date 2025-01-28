package com.example.project.Services;

import com.example.project.Classes.comment;
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
import java.util.ArrayList;
import java.util.List;

import static com.example.project.Services.bookServices.checkbookid;
import static com.example.project.Services.bookServices.desField;
import static com.example.project.Settings.settingAdminForm.center;
import static com.example.project.loginOptions.showAlert;

/**
 * Service class to handle comment-related functionalities such as retrieving comments
 * for a particular book based on its ID.
 * This class interacts with the database to fetch comments and store them in a list.
 */
public class commentServices {
    // TableView to display the comments in the UI (assuming a TableView is used to display comments)
    public static TableView<comment> tableView;

    // TextFields to allow input for rating and book ID (used when adding comments)
    public static TextField ratingField, bookidField;

    /**
     * Retrieves the list of comments associated with a specific book ID.
     * The method queries the database for all comments that are related to the provided book ID.
     *
     * @param bookId The ID of the book whose comments need to be fetched.
     * @return A List of comments associated with the given book ID.
     */
    public static List<comment> getCommentsOfBookId(int bookId){
        // Initialize DBHandler to establish a connection to the database
        DBHandler db = new DBHandler();
        Connection con = db.getConnection();

        // List to hold the comments fetched from the database
        List<comment> comments = new ArrayList<comment>();

        // SQL query to retrieve all comments related to the given bookId
        String sql = "select * from comment where book_id=?";

        try(PreparedStatement statement = con.prepareStatement(sql)){
            // Set the bookId parameter in the query
            statement.setInt(1,bookId);

            // Execute the query and retrieve the result set
            ResultSet rs = statement.executeQuery();

            // Iterate through the result set and map each row to a comment object
            while(rs.next()){
                // Extract the data from the result set
                int id = rs.getInt("comment_id");
                String date = rs.getString("comment_date");
                String description = rs.getString("comment_description");
                int rating = rs.getInt("rating");

                // Create a new comment object and add it to the comments list
                comment thiscomment = new comment( id, date, description, rating, bookId);
                comments.add(thiscomment);
            }
        } catch (SQLException e){
            // Handle any SQL exceptions by printing the stack trace
            e.printStackTrace();
        } finally {
            // Close the database connection to avoid resource leaks
            db.closeConnection();
        }
        // Return the list of comments fetched from the database
        return comments;
    }

    /**
     * Service method to add a new comment to the database for a specific book.
     * This method inserts the comment description, rating, and book ID into the `comment` table.
     *
     * @param bookId The ID of the book to which the comment is associated.
     * @param description The textual description of the comment.
     * @param rating The rating provided for the book in the comment (e.g., 1 to 5).
     */
    public static void addComment(int bookId, String description, int rating) {
        // Initialize DBHandler to manage the database connection
        DBHandler db = new DBHandler();
        Connection con = db.getConnection();

        // SQL query to insert a new comment with the current date and time
        String sql = "INSERT INTO comment (comment_date, comment_description, rating, book_id) VALUES (NOW(), ?, ?, ?)";

        try (PreparedStatement statement = con.prepareStatement(sql)) {
            // Set the parameters for the prepared statement
            statement.setString(1, description);  // comment_description
            statement.setInt(2, rating);          // rating
            statement.setInt(3, bookId);          // book_id

            // Execute the insert operation to add the comment into the database
            statement.executeUpdate();
        } catch (SQLException e) {
            // Handle SQL exceptions (e.g., connection issues or query execution problems)
            e.printStackTrace();
        } finally {
            // Close the database connection to release resources
            db.closeConnection();
        }
    }

    /**
     * Adds a new comment to the database for a specific book.
     * This method inserts the provided comment description, rating, and book ID into the `comment` table.
     *
     * It also performs basic validation to ensure the comment description is not empty and the book ID is valid.
     * If the book ID does not exist in the database, an error is shown.
     * Upon successful insertion, it clears the input fields and updates the displayed comments in the UI.
     *
     * @param commentDescription The description of the comment being added.
     * @param rating The rating associated with the comment (e.g., 1 to 5).
     * @param bookId The ID of the book to which the comment is associated.
     */
    private static void addComment2(String commentDescription, int rating, int bookId) {
        // Basic validation: Ensure the comment description is not empty and the book ID is valid
        if (commentDescription.isEmpty() || bookId <= 0) {
            showAlert(Alert.AlertType.ERROR, "Error", "Check if you entered those fields:\nComment Id\nComment Description\nBook Id.");
            return;
        }

        // Initialize DBHandler to manage the database connection
        DBHandler db = new DBHandler();

        try {
            // Establish a connection to the database
            Connection conn = db.getConnection();

            // Check if the provided book ID is valid (exists in the database)
            if (checkbookid(bookId)) {
                // SQL query to insert a new comment with the current date and time
                String query = "INSERT INTO comment (comment_date, comment_description, rating, book_id) VALUES (NOW(), ?, ?, ?)";

                try (PreparedStatement statement = conn.prepareStatement(query)) {
                    // Set the parameters for the prepared statement
                    statement.setString(1, commentDescription);
                    statement.setInt(2, rating);
                    statement.setInt(3, bookId);

                    // Execute the insert operation and check if the insert was successful
                    int rowsInserted = statement.executeUpdate();
                    if (rowsInserted > 0) {
                        // Show success message
                        showAlert(Alert.AlertType.INFORMATION, "Success", "A new comment has been added successfully.");

                        // Clear the input fields after successful insertion
                        desField.clear();
                        ratingField.clear();
                        bookidField.clear();

                        // Update the TableView to reflect the newly added comment
                        tableView.setItems(populateCommentsTable());
                    }
                } catch (SQLException exe) {
                    // Handle any SQL exceptions by showing an error message
                    showAlert(Alert.AlertType.ERROR, "Error", "Error adding new comment: " + exe.getMessage());
                    exe.printStackTrace();
                } finally {
                    // Close the database connection
                    db.closeConnection();
                }
            } else {
                // Show an error if the book ID is not found
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid book ID.");
            }
        } catch (Exception e) {
            // Catch any unexpected exceptions and show an error message
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding comment.");
            e.printStackTrace();
        } finally {
            // Ensure the database connection is closed in the finally block
            db.closeConnection();
        }
    }

    /**
     * Retrieves all comments from the database and returns them as an ObservableList of `comment` objects.
     * This method fetches all comment data, including the comment ID, date, description, rating, and book ID,
     * and adds them to a list that can be used to populate a TableView.
     *
     * @return An ObservableList containing all the comments from the database.
     */
    public static ObservableList<comment> populateCommentsTable() {
        // Initialize DBHandler to manage the database connection
        DBHandler db = new DBHandler();
        Connection conn = db.getConnection();

        // Create an observable list to store the retrieved comments
        ObservableList<comment> commentList = FXCollections.observableArrayList();

        // SQL query to retrieve all comments from the comment table
        String query = "SELECT * FROM comment";

        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            // Iterate through each result in the ResultSet
            while (resultSet.next()) {
                // Retrieve data from each column and map it to a new comment object
                int commentid = resultSet.getInt("comment_id");
                String commentdate = resultSet.getString("comment_date");
                String discription = resultSet.getString("comment_description");
                int rating = resultSet.getInt("rating");
                int bookId = resultSet.getInt("book_id");

                // Add the comment object to the observable list
                commentList.add(new comment(commentid, commentdate, discription, rating, bookId));
            }
        } catch (SQLException exe) {
            // Handle any SQL exceptions (e.g., database connection issues or query execution problems)
            showAlert(Alert.AlertType.ERROR, "Error", "Error loading comment data: " + exe.getMessage());
            exe.printStackTrace();
        } finally {
            // Ensure the database connection is closed
            db.closeConnection();
        }
        // Return the populated list of comments
        return commentList;
    }

    /**
     * Deletes the currently selected comment from the TableView and the database.
     * This method retrieves the selected comment from the TableView, gets the comment ID,
     * and deletes the corresponding record from the `comment` table in the database.
     *
     * If no comment is selected, an error message is displayed.
     * After successful deletion, the comment is removed from the TableView and the input fields are cleared.
     */
    public static void deleteComment() {
        // Retrieve the currently selected comment from the TableView
        comment selectedComment = tableView.getSelectionModel().getSelectedItem();

        // Check if a comment is selected
        if (selectedComment == null) {
            // Show error if no comment is selected
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a comment to delete.");
            return;
        }

        // Get the comment ID of the selected comment
        int thisCommentid = selectedComment.getComment_id();

        // Initialize DBHandler to manage the database connection
        DBHandler db = new DBHandler();
        Connection conn = db.getConnection();

        // SQL query to delete the selected comment from the comment table
        String query = "DELETE FROM comment WHERE comment_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            // Set the comment ID in the SQL query
            statement.setInt(1, thisCommentid);

            // Execute the delete operation
            int rowsDeleted = statement.executeUpdate();

            // Check if a row was deleted
            if (rowsDeleted > 0) {
                // Show success alert if the comment was deleted
                showAlert(Alert.AlertType.INFORMATION, "Success", "Comment deleted successfully.");

                // Clear the input fields after deletion
                desField.clear();
                ratingField.clear();
                bookidField.clear();

                // Refresh the TableView to reflect the deleted comment
                tableView.setItems(populateCommentsTable());
            } else {
                // Show error alert if no comment was found with the specified ID
                showAlert(Alert.AlertType.ERROR, "Error", "No comment found with the specified id.");
            }
        } catch (SQLException exe) {
            // Handle any SQL exceptions (e.g., database connection issues or query execution problems)
            showAlert(Alert.AlertType.ERROR, "Error", "Error deleting comment: " + exe.getMessage());
            exe.printStackTrace();
        } finally {
            // Ensure the database connection is closed
            db.closeConnection();
        }
    }

    /**
     * Creates and initializes the TableView for displaying comments, along with input fields and buttons for managing comments.
     * This method sets up the layout, columns, and necessary actions for adding, deleting, and updating comments.
     * It also populates the TableView with existing comments from the database and allows for row selection.
     */
    public static void createCommentsTable() {
        // Create the TableView and its columns
        tableView = new TableView<>();
        tableView.setPrefSize(1040, 800);

        // Define columns for displaying comment data
        TableColumn<comment, Integer> commentidColumn = new TableColumn<>("Comment Id");
        commentidColumn.setCellValueFactory(new PropertyValueFactory<>("comment_id"));

        TableColumn<comment, String> dateColumn = new TableColumn<>("Comment Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("comment_date"));

        TableColumn<comment, String> desColumn = new TableColumn<>("Comment Description");
        desColumn.setCellValueFactory(new PropertyValueFactory<>("comment_description"));

        TableColumn<comment, Integer> ratingColumn = new TableColumn<>("Rating");
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));

        TableColumn<comment, Integer> bookidColumn = new TableColumn<>("Book Id");
        bookidColumn.setCellValueFactory(new PropertyValueFactory<>("book_id"));

        // Set equal width for each column
        double columnWidth = 200; // Adjust width as needed
        commentidColumn.setPrefWidth(columnWidth);
        dateColumn.setPrefWidth(columnWidth);
        desColumn.setPrefWidth(columnWidth);
        ratingColumn.setPrefWidth(columnWidth);
        bookidColumn.setPrefWidth(columnWidth);

        // Add columns to the TableView
        tableView.getColumns().addAll(commentidColumn, dateColumn, desColumn, ratingColumn, bookidColumn);

        // Create the input fields and labels for comment management
        Label desLabel = new Label("Description:");
        desField = new TextField();
        desField.setPromptText("Enter description");

        Label ratingLabel = new Label("Rating:");
        ratingField = new TextField();
        ratingField.setPromptText("1-5");

        Label bookidLabel = new Label("Book ID:");
        bookidField = new TextField();
        bookidField.setPromptText("Enter book ID");

        // Button to add a new comment
        Button addButton = new Button("Add Comment");
        addButton.setOnAction(e -> addComment2(desField.getText(), Integer.parseInt(ratingField.getText()), Integer.parseInt(bookidField.getText())));

        // Button to delete a comment
        Button deleteButton = new Button("Delete Comment");
        deleteButton.setOnAction(e -> deleteComment());

        // Button to update an existing comment
        Button updateButton = new Button("Update Comment");
        updateButton.setOnAction(e -> {
            // Retrieve the currently selected item in the event handler
            comment selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                // If a comment is selected, proceed with updating it
                updateComment(selectedItem.getComment_id(), Integer.parseInt(ratingField.getText()), desField.getText(), Integer.parseInt(bookidField.getText()));
            } else {
                // If no comment is selected, show an error
                showAlert(Alert.AlertType.ERROR, "Error", "Comment not selected.");
            }
        });

        // Create a VBox to hold the input fields and buttons
        VBox inputBox = new VBox(10);
        inputBox.setPadding(new Insets(10));
        inputBox.getChildren().addAll(desLabel, desField, ratingLabel, ratingField, bookidLabel, bookidField, addButton, deleteButton, updateButton);

        // Create a BorderPane to arrange the UI components
        BorderPane layoutt = new BorderPane();
        layoutt.setLeft(inputBox); // Place the input box on the left side
        layoutt.setCenter(tableView); // Place the TableView in the center

        // Populate the TableView with data from the database
        tableView.setItems(populateCommentsTable());

        // Add listener to TableView for row selection
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // When a row is selected, populate the input fields with the selected comment's details
                desField.setText(newValue.getComment_description());
                ratingField.setText(String.valueOf(newValue.getRating()));
                bookidField.setText(String.valueOf(newValue.getBook_id()));
            }
        });

        // Add the layout to the main container (assumed to be 'center')
        center.add(layoutt, 0, 0);
    }

    /**
     * Updates an existing comment in the database by modifying its rating, description, and associated book ID.
     * This method checks if the new values are different from the existing ones before proceeding with the update.
     * It provides feedback to the user if no changes were made or if any error occurs during the process.
     *
     * @param commentId The ID of the comment to be updated.
     * @param newRate The new rating value to set for the comment.
     * @param newDescription The new description of the comment.
     * @param bookid The new book ID associated with the comment.
     */
    public static void updateComment(int commentId, int newRate, String newDescription, int bookid) {
        DBHandler db = new DBHandler();
        Connection conn = db.getConnection();

        // SQL query to check if the comment exists and retrieve its current details
        String checkQuery = "SELECT rating, comment_description, book_id FROM comment WHERE comment_id = ?";
        // SQL query to update the comment in the database
        String updateQuery = "UPDATE comment SET rating = ?, comment_description = ?, book_id = ?, comment_date = NOW() WHERE comment_id = ?";

        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, commentId);  // Set the comment ID to check if the comment exists
            ResultSet rs = checkStmt.executeQuery();

            // Check if a comment with the specified ID exists
            if (rs.next()) {
                // Retrieve current values for the comment
                int currentRate = rs.getInt("rating");
                String currentDescription = rs.getString("comment_description");
                int currentBookid = rs.getInt("book_id");

                // Check if the new values are the same as the existing ones
                if (newRate == currentRate && newDescription.equals(currentDescription) && bookid == currentBookid) {
                    // If the values are the same, display a warning and return
                    showAlert(Alert.AlertType.WARNING, "Warning", "Change something to be updated.");
                    return;
                }
            } else {
                // If the comment ID does not exist, show an error message
                showAlert(Alert.AlertType.ERROR, "Error", "No comment found with the provided ID.");
                return;
            }

            // Proceed with the update operation
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                updateStmt.setInt(1, newRate); // Set the new rating
                updateStmt.setString(2, newDescription); // Set the new description
                updateStmt.setInt(3, bookid); // Set the new book ID
                updateStmt.setInt(5, commentId); // Set the comment ID for the update

                // Execute the update query
                int rowsUpdated = updateStmt.executeUpdate();
                if (rowsUpdated > 0) {
                    // If the comment is successfully updated, show a success message
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Comment updated successfully.");
                    // Update the comment table view with the latest data
                    tableView.setItems(populateCommentsTable());
                } else {
                    // If no rows were updated, show an error message
                    showAlert(Alert.AlertType.ERROR, "Error", "No comment found with the provided ID.");
                }
            }
        } catch (SQLException e) {
            // If an error occurs during the SQL operation, show an error message
            showAlert(Alert.AlertType.ERROR, "Error", "Error updating comment: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Ensure the database connection is closed
            db.closeConnection();
        }
    }

    /**
     * Deletes all comments associated with a given book ID from the `comment` table in the database.
     * This method takes a database connection and a book ID as parameters, and deletes all comments
     * linked to that book ID. It ensures that no comments are left for the specified book.
     *
     * @param conn The established database connection to be used for executing the delete query.
     * @param bookid The ID of the book for which the comments need to be deleted.
     */
    public static void deleteCommentWBOOKID(Connection conn, int bookid) {
        // Check if the database connection is established
        if (conn == null) {
            // If no connection exists, show an error alert
            showAlert(Alert.AlertType.ERROR, "Error", "Database connection not established. Cannot delete comments.");
            return;
        }

        // SQL query to delete comments based on book_id
        String query = "DELETE FROM comment WHERE book_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            // Set the book ID in the query
            statement.setInt(1, bookid);

            // Execute the delete operation
            statement.executeUpdate();   // Executes the delete query

        } catch (SQLException exe) {
            // If an error occurs during the delete operation, show an error alert
            showAlert(Alert.AlertType.ERROR, "Error", "Error deleting comments: " + exe.getMessage());
            exe.printStackTrace();
        }
    }
}
