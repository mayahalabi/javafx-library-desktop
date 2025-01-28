package com.example.project.Services;

import com.example.project.Connection.DBHandler;
import com.example.project.Classes.book;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The searchServices class provides methods to search for books in the database.
 * It performs a search based on the title of the book and retrieves associated
 * book, image, and author details from the database.
 */
public class searchServices {

    /**
     * Searches for books based on the provided search string.
     * This method searches for books whose title contains the search string.
     * It returns a list of books along with their associated images and authors.
     *
     * @param search The search string used to filter book titles.
     * @return A list of books that match the search criteria.
     */
    public static List<book> searchBook(String search) {
        // Creates a new instance of DBHandler to manage database connections
        DBHandler dbHandler = new DBHandler();

        // Establishes a connection to the database
        Connection conn = dbHandler.getConnection();

        // List to hold the results of the search query
        List<book> books = new ArrayList<>();

        // SQL query to search for books whose titles match the provided search string.
        // The query performs a natural join between 'book', 'images', and 'author' tables.
        String sql = "select * from book natural join images natural join author where title like ?";

        try(PreparedStatement statement = conn.prepareStatement(sql)){
            // Set the search parameter with wildcards for partial matching (e.g., '%search%')
            statement.setString(1,"%" + search + "%");

            // Execute the query and retrieve the result set
            ResultSet resultSet = statement.executeQuery();

            // Process the result set and map each row to a 'book' object
            while(resultSet.next()){
                books.add(bookServices.mapResultSetToBook(resultSet)); // Map the current result to a 'book' object and add it to the list
            }
        } catch (SQLException e) {
            // Handle any SQL exceptions that may occur during query execution
            e.printStackTrace();
        } finally {
            // Ensure that the database connection is closed after the operation is complete
            dbHandler.closeConnection();
        }
        return books; // Return the list of books that match the search criteria
    }

    /**
     * Searches for books based on the author's first or last name.
     * This method retrieves books where the author's first or last name contains the search string.
     * It returns a list of books with their associated author and image details.
     *
     * @param search The search string used to filter author names (both first and last).
     * @return A list of books that match the search criteria.
     */
    public static List<book> searchAuthor(String search) {
        // Creates a new instance of DBHandler to manage database connections
        DBHandler dbHandler = new DBHandler();

        // Establishes a connection to the database
        Connection conn = dbHandler.getConnection();

        // List to hold the results of the search query
        List<book> books = new ArrayList<>();

        // SQL query to search for books where the author's first or last name matches the search string.
        // The query performs a natural join between 'book', 'author', and 'images' tables.
        String sql = "select * from book natural join author natural join images where first_name like ? or last_name like ?";

        try(PreparedStatement statement = conn.prepareStatement(sql)){
            // Set the search parameter with wildcards for partial matching (e.g., '%search%')
            statement.setString(1,"%" + search + "%");
            statement.setString(2,"%" + search + "%");

            // Execute the query and retrieve the result set
            ResultSet resultSet = statement.executeQuery();

            // Process the result set and map each row to a 'book' object
            while(resultSet.next()){
                books.add(bookServices.mapResultSetToBook(resultSet)); // Map the current result to a 'book' object and add it to the list
            }
        } catch (SQLException e) {
            // Handle any SQL exceptions that may occur during query execution
            e.printStackTrace();
        } finally {
            // Ensure that the database connection is closed after the operation is complete
            dbHandler.closeConnection();
        }
        // Return the list of books that match the search criteria
        return books;
    }

    /**
     * Searches for books based on the genre type.
     * This method retrieves books that match a given genre type.
     * It returns a list of books along with their associated genre, author, and image details.
     *
     * @param search The search string used to filter the genre type.
     * @return A list of books that match the genre search criteria.
     */
    public static List<book> searchGenre(String search) {
        // Creates a new instance of DBHandler to manage database connections
        DBHandler dbHandler = new DBHandler();

        // Establishes a connection to the database
        Connection conn = dbHandler.getConnection();

        // List to hold the results of the search query
        List<book> books = new ArrayList<>();

        // SQL query to search for books that match the specified genre type.
        // The query performs a natural join between 'book', 'bookgenres', 'genre', 'images', and 'author' tables.
        String sql = "select * from book natural join bookgenres natural join " +
                "genre natural join images natural join  author where type like ?";

        try(PreparedStatement statement = conn.prepareStatement(sql)){
            // Set the search parameter with wildcards for partial matching (e.g., '%search%')
            statement.setString(1,"%" + search + "%"); // Genre search

            // Execute the query and retrieve the result set
            ResultSet resultSet = statement.executeQuery();

            // Process the result set and map each row to a 'book' object
            while(resultSet.next()){
                books.add(bookServices.mapResultSetToBook(resultSet)); // Map the current result to a 'book' object and add it to the list
            }
        } catch (SQLException e) {
            // Handle any SQL exceptions that may occur during query execution
            e.printStackTrace();
        } finally {
            // Ensure that the database connection is closed after the operation is complete
            dbHandler.closeConnection();
        }
        // return books related to the searched genre
        return books;
    }

    /**
     * Searches for books based on the provided search term. The search process is performed in three steps:
     * 1. It first attempts to search by book title.
     * 2. If no results are found, it then searches by author.
     * 3. If no results are found in the second step, it searches by genre.
     *
     * @param search The search term to look for (could be a book title, author name, or genre).
     * @return A list of books that match the search term. If no matches are found, an empty list is returned.
     */
    public static List<book> search(String search) {
        // Search for books by title
        List<book> books = searchBook(search);

        // If books are found by title, return them
        if (!books.isEmpty()) {
            return books;
        }

        // If no books were found, try searching by author
        books = searchAuthor(search);

        // If books are found by author, return them
        if (!books.isEmpty()) {
            return books;
        }

        // If no books were found by author, try searching by genre
        books = searchGenre(search);

        // If books are found by genre, return them
        if (!books.isEmpty()) {
            return books;
        }

        // If no results are found after all search attempts, return an empty list
        return books;// Empty list will be returned here if no results were found
    }

    /**
     * Generates a GridPane layout displaying books based on a search term.
     * Each book is represented by its cover image, and the books are arranged
     * in a grid format with a set number of columns per row.
     *
     * @param search The search term to look for (could be a book title, author name, or genre).
     * @return A GridPane containing the book cover images arranged in a grid.
     */
    public static GridPane getSearchGrid(String search) {
        // Create a new GridPane for the content layout
        GridPane content = new GridPane();
        content.setHgap(10); // Set horizontal gap between grid cells
        content.setVgap(10); // Set vertical gap between grid cells
        content.setPadding(new Insets(5)); // Set padding around the entire grid

        // Fetch the list of books based on the search term
        List<book> books = search(search); // This method retrieves the list of books
        int columns = 15;  // Set the number of columns to be used for the grid layout

        // Iterate over the list of books to create an ImageView for each
        for (int i = 0; i < books.size(); i++) {
            book currentBook = books.get(i); // Get the current book object

            // Create an ImageView for the book cover
            ImageView bookCover = bookServices.createImageView(currentBook);
            if (bookCover != null) {
                // Store the current book object in the ImageView's userData property
                bookCover.setUserData(currentBook);

                // Calculate the row and column position for the current ImageView in the grid
                int row = i / columns; // Row index is determined by dividing the index by columns
                int col = i % columns; // Column index is determined by the remainder of the division

                // Add the ImageView to the GridPane at the calculated position
                content.add(bookCover, col, row);
            }
        }
        // Return the GridPane with all the book covers arranged in the grid
        return content;
    }

    /**
     * Creates and returns a ScrollPane containing the grid of book covers based on the search term.
     * The grid is generated using the `getSearchGrid` method and placed inside the ScrollPane to
     * allow for scrolling if there are too many books to fit on the screen.
     *
     * @param search The search term to look for (could be a book title, author name, or genre).
     * @return A ScrollPane that contains the GridPane of book covers, with scrolling enabled.
     */
    public static ScrollPane displaySearchScrollPane(String search) {
        // Get the GridPane containing the books (generated by the search term)
        GridPane bookGrid = getSearchGrid(search);

        // Create a ScrollPane and add the GridPane to it
        ScrollPane scrollPane = new ScrollPane(bookGrid);

        // Set the ScrollPane to adjust the width of the content to fit within the pane
        scrollPane.setFitToWidth(true);

        // Add padding around the ScrollPane's content
        scrollPane.setPadding(new Insets(10));

        // Return the configured ScrollPane containing the book grid
        return scrollPane;
    }
}
