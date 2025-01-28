package com.example.project.Services;

import com.example.project.Connection.DBHandler;
import com.example.project.Classes.genre;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.example.project.loginOptions.showAlert;

/**
 * Service class for managing book genres in the database.
 * This class provides methods to retrieve genre information associated with specific books.
 */
public class bookgenresServices {

    /**
     * Retrieves a list of genres associated with a specific book.
     *
     * @param bookId the ID of the book whose genres are to be retrieved.
     * @return a List of genre objects associated with the specified book.
     */
    public static List<genre> getGenreIds(int bookId) {
        DBHandler db = new DBHandler(); // Initialize database handler
        Connection conn = db.getConnection(); // Establish database connection
        List<genre> genres = new ArrayList<>(); // List to hold genre objects

        String sql = "SELECT genre_id FROM bookgenres WHERE book_id = ?"; // SQL query to retrieve genre IDs

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            // Set the bookId parameter for the SQL query
            statement.setInt(1, bookId);

            // Execute the query and obtain the result set
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int genreId = rs.getInt("genre_id"); // Retrieve genre ID from the result set
                // Fetch the genre object using the genre ID and add it to the genres list
                genres.add(genreServices.getAllGenres(genreId));
            }
        } catch (SQLException e) {
            // Print stack trace for debugging in case of an SQL error
            e.printStackTrace();
        } finally {
            db.closeConnection(); // Ensure the database connection is closed to prevent leaks
        }
        return genres; // Return the list of genres associated with the book
    }


    /**
     * Deletes all genre associations for a specified book from the `bookgenres` table.
     *
     * @param conn   the database connection to be used for the deletion operation.
     *               Must be an open, valid connection.
     * @param bookid the ID of the book whose associated genres are to be deleted.
     *
     * This method will delete all entries in the `bookgenres` table where the `book_id`
     * matches the specified `bookid`, effectively removing all genre associations for
     * that book. If the `conn` parameter is null, an error alert is shown, and the method exits.
     * Upon successful execution, a message is logged to the console.
     */
    public static void deleteBookGenreWBOOKID(Connection conn, int bookid) {

        // Checks the connection
        if (conn == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Database connection not established. Cannot delete book genres.");
            return;
        }

        // query that selects bookgenres based on bookid
        String query = "DELETE FROM bookgenres WHERE book_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, bookid); //set bookid in query
            statement.executeUpdate();  // Execute delete
            System.out.println("Deleted book genres for book ID: " + bookid);
        } catch (SQLException exe) {
            // Error message if deletion was not successfull
            showAlert(Alert.AlertType.ERROR, "Error", "Error deleting book genres: " + exe.getMessage());
            exe.printStackTrace();
        }
    }


}
