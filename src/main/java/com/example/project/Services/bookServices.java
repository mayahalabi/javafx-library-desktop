package com.example.project.Services;

import com.example.project.Classes.*;
import com.example.project.Connection.DBHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.project.Services.authorServices.*;
import static com.example.project.Services.bookgenresServices.deleteBookGenreWBOOKID;
import static com.example.project.Services.borrowingTransactionServices.deleteTransactionWBookID;
import static com.example.project.Services.commentServices.deleteCommentWBOOKID;
import static com.example.project.Services.imageServices.getImageId;
import static com.example.project.Services.notificationServices.deleteNotificationWBOOKID;
import static com.example.project.Settings.settingAdminForm.center;
import static com.example.project.loginOptions.showAlert;

public class bookServices {

    public static TableView<BookAuthorImage> tableViewBook;
    public static TextField imagepathField, firstnameField, lastnameField,
            titleField, isbnField, publisherField, publishedyearField, statusField, desField, quantityField, rateField,
            bookidField, typeField;

    /**
     * Checks if a book with the given book ID exists in the 'book' table.
     *
     * @param bookid The book ID to check for existence in the database.
     * @return true if the book exists, false otherwise.
     */
    public static boolean checkbookid(int bookid) {
        DBHandler db = new DBHandler();

        // Try-with-resources ensures that the connection is properly closed
        try {
            Connection conn = db.getConnection();

            // SQL query to check if a book with the given book_id exists
            String query = "SELECT * FROM book WHERE book_id = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, bookid);
                ResultSet resultSet = statement.executeQuery();

                // Check if any result is returned, meaning the book exists
                if (resultSet.next()) {
                    return true;
                } else {
                    // Show an error alert if no matching book is found
                    showAlert(Alert.AlertType.ERROR, "Error", "Book ID does not exist: " + bookid);
                }
            }
        } catch (SQLException e) {
            // Log and handle the exception if something goes wrong with the database connection or query
            e.printStackTrace();
        } finally {
            // Ensure the connection is closed
            db.closeConnection();
        }
        // Return false if book doesn't exist or error occurs
        return false;
    }

    /**
     * This method generates a GridPane layout containing book covers.
     * Each book cover is represented as an ImageView and added to the GridPane.
     * The books are fetched from the database and displayed in rows and columns.
     *
     * @return GridPane containing book cover images
     */
    public static GridPane getBookGrid() {

        // Create a GridPane to organize the book covers
        GridPane content = new GridPane();

        // Set the horizontal gap between items (book covers) in the grid
        content.setHgap(10);

        // Set the vertical gap between items (book covers) in the grid
        content.setVgap(10);

        // Set padding around the GridPane content (adds space around the entire grid)
        content.setPadding(new Insets(5));

        // Fetch the list of books from the database (this method is assumed to return a list of book objects)
        List<book> books = AllgetBooks(); // This method should return the list of books from the database

        // Define how many columns we want in the grid
        int columns = 15;  // Adjust the number of columns per row

        // Iterate through each book in the list
        for (int i = 0; i < books.size(); i++) {
            // Get the current book from the list
            book currentBook = books.get(i);

            // Safely create an ImageView with a book cover
            ImageView bookCover = createImageView(currentBook);

            // Check if the book cover image was successfully created
            if (bookCover != null) {
                // Store book object in ImageView's userData
                bookCover.setUserData(currentBook);

                // Calculate row and column position
                int row = i / columns;
                int col = i % columns;

                // Add the ImageView to the GridPane
                content.add(bookCover, col, row);
            }
        }
        // Return the populated GridPane with all the book covers arranged in a grid
        return content;
    }

    /**
     * Creates an ImageView for a given book's cover image.
     *
     * This method takes a book object and tries to load its image from the specified file path.
     * If the image is loaded successfully, an ImageView is created, and its properties are set.
     * If there is an error while loading the image, an alert is shown, and the method returns null.
     *
     * @param currentBook The book object whose cover image is to be displayed.
     * @return An ImageView displaying the book's cover image, or null if an error occurs.
     */
    public static ImageView createImageView(book currentBook) {
        try {
            // Retrieve the image file path from the book object
            String imagePath = currentBook.getImage().getImage_path();

            // Create an Image object from the file path
            Image image = new Image("file:" + imagePath);

            // Create an ImageView to display the image
            ImageView imageView = new ImageView(image);

            // Set the dimensions for the ImageView
            imageView.setFitWidth(100);
            imageView.setFitHeight(150);

            // Preserve the aspect ratio of the image to avoid distortion
            imageView.setPreserveRatio(true);

            // Return the ImageView with the book cover
            return imageView;
        } catch (Exception e) {
            // Show an alert to the user indicating the failure to load the image
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load image for book: " + currentBook.getTitle());

            // Return null if there was an issue loading the image
            return null;
        }
    }

    /**
     * Creates a ScrollPane to display the grid of book covers.
     *
     * This method generates a ScrollPane containing a GridPane of book covers.
     * The book covers are fetched using the `getBookGrid()` method, which populates the grid.
     * The ScrollPane allows the user to scroll through the books if the content exceeds the visible area.
     * The ScrollPane is configured to fit the width and has padding around the edges.
     *
     * @return ScrollPane containing the grid of book covers
     */
    public static ScrollPane displayBookScrollPane() {

        // Fetch the GridPane that contains all the book covers
        GridPane bookGrid = getBookGrid();

        // Create a ScrollPane to wrap the GridPane, enabling scrolling if needed
        ScrollPane scrollPane = new ScrollPane(bookGrid);

        // Set the ScrollPane to fit the width of its content
        scrollPane.setFitToWidth(true);

        // Set padding around the ScrollPane to create space between the grid and the edge of the pane
        scrollPane.setPadding(new Insets(10));

        // Return the configured ScrollPane with the book grid inside
        return scrollPane;
    }

    /**
     * Fetches all books from the database, including their associated author and image details.
     *
     * This method executes a SQL query that retrieves data from the `book`, `author`, and `images` tables
     * by using natural joins. It processes the result set and maps each row to a `book` object, which is added
     * to a list of books. The method ensures proper closure of the database connection.
     *
     * @return A list of `book` objects, each containing book, author, and image information.
     */
    public static List<book> AllgetBooks() {
        // Initialize an empty list to store the book objects
        List<book> books = new ArrayList<>();

        // Create a new DBHandler instance to handle the database connection
        DBHandler db = new DBHandler();

        // Establish a connection to the database
        Connection con = db.getConnection();

        // SQL query to fetch book, author, and image data using natural joins
        String sql = "SELECT * FROM (author NATURAL JOIN book) NATURAL JOIN images";

        try (PreparedStatement statement = con.prepareStatement(sql)) {
            // Execute the SQL query
            ResultSet rs = statement.executeQuery();

            // Loop through each result row and map it to a book object
            while (rs.next()) {
                // Map the current row of the result set to a book object and add it to the list
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            // Print the stack trace if there is an exception during database interaction
            e.printStackTrace();
        } finally {
            // Close the database connection to prevent any potential leaks
            db.closeConnection();
        }
        // Return the list of books fetched from the database
        return books;
    }

    /**
     * Maps a ResultSet row to a `book` object.
     *
     * This method processes a `ResultSet` object from a database query, extracting relevant columns to
     * create a `book` object along with associated `author` and `image` objects. It also sets additional
     * properties for the book, such as its genres and comments, by fetching them from other services.
     *
     * @param rs The ResultSet containing the row to map to a `book` object.
     * @return A `book` object populated with data from the ResultSet.
     * @throws SQLException If an error occurs while extracting data from the ResultSet.
     */
    public static book mapResultSetToBook(ResultSet rs) throws SQLException {
        // Extract individual columns from the ResultSet
        int bookId = rs.getInt("book_id");
        int imageId = rs.getInt("image_id");
        int authorId = rs.getInt("author_id");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        String title = rs.getString("title");
        String isbn = rs.getString("isbn");
        String publisher = rs.getString("publisher");
        String publishedYear = rs.getString("published_year");
        String status = rs.getString("status");
        String description = rs.getString("discription");
        int quantity = rs.getInt("quantity");
        String imagePath = rs.getString("image_path");
        int rate = rs.getInt("rate");

        // Create the `images` object using data from the ResultSet
        images image = new images(imageId, imagePath);

        // Create the `author` object using data from the ResultSet
        author author = new author(authorId, firstName, lastName);

        // Create the `book` object, passing in the extracted fields
        book thisBook = new book(bookId, author, title, isbn, publisher,
                publishedYear, image, description, status, quantity, rate);

        // Fetch and set the genres for this book using the book's ID
        thisBook.setGenres(bookgenresServices.getGenreIds(bookId));

        // Fetch and set the comments for this book using the book's ID
        thisBook.setCom(commentServices.getCommentsOfBookId(bookId));

        // Return the populated `book` object
        return thisBook;
    }

    /**
     * Checks whether an author has any books in the database.
     *
     * This method queries the database to count the number of books associated with a particular
     * author (based on the given `authorId`). It returns `true` if the author has one or more books
     * in the system, and `false` otherwise.
     *
     * @param authorId The ID of the author to check.
     * @return `true` if the author has one or more books in the database, `false` otherwise.
     */
    public static boolean checkBooksByAuthor(int authorId) {
        DBHandler db = new DBHandler();
        Connection conn = db.getConnection();

        // SQL query to count the number of books by the specified author
        String query = "SELECT COUNT(*) AS count FROM book WHERE author_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            // Set the authorId parameter in the query
            statement.setInt(1, authorId);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            // Check if the result set contains data and process the result
            if (resultSet.next()) {
                int count = resultSet.getInt("count"); // Get the count of books
                return count > 0; // Returns true if books exist
            }
        } catch (SQLException e) {
            // Handle SQL exception and print the stack trace
            e.printStackTrace();
        } finally {
            // Ensure the database connection is closed
            db.closeConnection();
        }
        // Return false if no books were found
        return false;
    }

    /**
     * Populates the book table with a list of books and related information from the database.
     *
     * This method retrieves data from the `book`, `author`, `images`, `bookgenres`, and `genre` tables
     * in the database and combines it into a list of `BookAuthorImage` objects, which are then returned
     * as an `ObservableList` to be used in a table view.
     *
     * @return An ObservableList containing `BookAuthorImage` objects, each representing a book with related data.
     */
    public static ObservableList<BookAuthorImage> populateBookTable() {
        DBHandler db = new DBHandler();
        Connection conn = db.getConnection();
        ObservableList<BookAuthorImage> bookList = FXCollections.observableArrayList();

        // SQL query to join multiple tables and retrieve book, author, image, genre, and other related information
        String query = "SELECT b.book_id, b.title, b.isbn, b.publisher, b.published_year, " +
                "b.status, b.discription, a.author_id, a.first_name, a.last_name, i.image_id, " +
                "i.image_path, b.quantity, b.rate, bg.genre_id, g.type FROM book b JOIN author a ON " +
                "b.author_id = a.author_id JOIN images i ON b.image_id = i.image_id  join bookgenres  " +
                "bg on bg.book_id = b.book_id  join genre g on bg.genre_id = g.genre_id";

        // Try-with-resources to automatically close resources after use
        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            // Loop through the result set and populate the ObservableList with BookAuthorImage objects
            while (resultSet.next()) {
                int bookid = resultSet.getInt("book_id");
                String title = resultSet.getString("title");
                String isbn = resultSet.getString("isbn");
                String publisher = resultSet.getString("publisher");
                String publishedYear = resultSet.getString("published_year");
                String status = resultSet.getString("status");
                String description = resultSet.getString("discription");
                int quantity = resultSet.getInt("quantity");
                int imageid = resultSet.getInt("image_id");
                int rate = resultSet.getInt("rate");
                int authorid = resultSet.getInt("author_id");
                String imagePath = resultSet.getString("image_path");
                String firstname = resultSet.getString("first_name");
                String lastname = resultSet.getString("last_name");
                String type = resultSet.getString("type");
                int genreid = resultSet.getInt("genre_id");

                // Create author and image objects from the result set data
                author author = new author(authorid, firstname, lastname);
                images image = new images(imageid, imagePath);

                // Add the new BookAuthorImage object to the ObservableList
                bookList.add(new BookAuthorImage(bookid, quantity, status, author, image, title, isbn, publisher, publishedYear, description, rate, imageid, imagePath, authorid, firstname, lastname, genreid, type));
            }
        } catch (SQLException exe) {
            // Handle any SQL exceptions
            showAlert(Alert.AlertType.ERROR, "Error", "Error loading book data: " + exe.getMessage());
            exe.printStackTrace();
        } finally {
            // Close the database connection
            db.closeConnection();
        }
        // Return the populated list of books
        return bookList;
    }

    /**
     * Adds a new book to the system, including inserting related records for the author, image, and genre.
     * This method performs the following steps:
     * 1. Validates the required fields for the book.
     * 2. Inserts the author into the `author` table if the author does not already exist.
     * 3. Inserts the image into the `images` table if the image does not already exist.
     * 4. Inserts the genre(s) into the `genres` table if they do not already exist.
     * 5. Inserts the new book record into the `book` table.
     * 6. Clears the input fields and updates the TableView upon successful insertion.
     *
     * @param title          The title of the book.
     * @param isbn           The ISBN of the book.
     * @param publisher      The publisher of the book.
     * @param publishedyear  The year the book was published.
     * @param status         The status of the book (e.g., available, checked out).
     * @param description    A description of the book.
     * @param quantity       The quantity of books available.
     * @param rate           The rating of the book.
     * @param imagePath      The file path of the book's cover image.
     * @param firstname      The first name of the author.
     * @param lastname       The last name of the author.
     * @param type           The genre(s) of the book (comma-separated).
     */
    private static void addBook(String title, String isbn, String publisher, String publishedyear,
                                String status, String description, int quantity, int rate, String imagePath,
                                String firstname, String lastname, String type) {

        // Validate required fields
        if (title.isEmpty() || isbn.isEmpty() || status.isEmpty() || quantity < 0 || rate < 0 || imagePath.isEmpty() || firstname.isEmpty() || lastname.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "These fields are required:\nTitle\nISBN\nStatus\nQuantity\nRate\nImage Path\nFirst Name\nLast Name.");
            return;
        }

        // Create a DBHandler to manage the database connection
        DBHandler db = new DBHandler();
        try (Connection conn = db.getConnection()) {

            // Check if the author exists, and if not, insert it
            if(getAuthorId(firstname, lastname) == -1) {
                addAuthor(firstname, lastname);
            } else {
                System.out.println("done author");
            }

            // Check if the image exists, and if not, insert it
            if(getImageId(imagePath) == -1) {
                imageServices.addImage(imagePath);
            } else {
                imagepathField.clear();
                System.out.println("done image");
            }

            // Handle genres - check if each genre exists and insert if necessary
            String[] types = type.split(",");
            for(int i = 0; i < types.length; i++) {
                if(genreServices.getGenreId(types[i]) == -1){
                    genreServices.addGenre(types[i]);
                } else {
                    firstnameField.clear();
                    lastnameField.clear();
                    System.out.println("done genre");
                }
            }

            // Insert the book into the `book` table
            String query3 = "INSERT INTO book (title, isbn, publisher, published_year, status, discription, author_id, image_id, quantity, rate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement3 = conn.prepareStatement(query3)) {
                // Set parameters for the SQL query
                statement3.setString(1, title);
                statement3.setString(2, isbn);
                statement3.setString(3, publisher);
                statement3.setString(4, publishedyear);
                statement3.setString(5, status);
                statement3.setString(6, description);
                statement3.setInt(7, getAuthorId(firstname, lastname));
                statement3.setInt(8, getImageId(imagePath));
                statement3.setInt(9, quantity);
                statement3.setInt(10, rate);

                // Execute the SQL query to insert the new book
                int rowsInserted3 = statement3.executeUpdate();
                if (rowsInserted3 > 0) {
                    // If the book was successfully inserted, clear the fields and update the TableView
                    titleField.clear();
                    isbnField.clear();
                    publisherField.clear();
                    publishedyearField.clear();
                    statusField.clear();
                    desField.clear();
                    quantityField.clear();
                    rateField.clear();
                    tableViewBook.setItems(populateBookTable());
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Book added successfully.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Error adding new book.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error adding new book: " + e.getMessage());
            }
        } catch (Exception e) {
            // Catch any errors during the database connection or operation
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding book: " + e.getMessage());
        } finally {
            // Ensure the database connection is closed after the operation
            db.closeConnection();
        }
    }

    /**
     * Deletes a selected book and its related records from the database.
     * This method performs the following steps:
     * 1. Checks if a book is selected in the TableView.
     * 2. Deletes related records from foreign key tables (e.g., transactions, comments, notifications, book genres).
     * 3. Deletes the book record from the `book` table.
     * 4. Deletes the associated image record from the `images` table.
     * 5. Updates the TableView after a successful deletion.
     */
    public static void deleteBook() {
        // Get the selected book from the TableView
        BookAuthorImage selectedBook = tableViewBook.getSelectionModel().getSelectedItem();

        // If no book is selected, show an error message and return
        if (selectedBook == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a book to delete.");
            return;
        }

        // Get the book ID for the selected book
        int thisBookid = selectedBook.getBookid();

        // Create a new DBHandler instance to handle the database connection
        DBHandler db = new DBHandler();
        Connection conn = db.getConnection();

        // Delete foreign keys before
        deleteTransactionWBookID(conn, thisBookid);

        deleteCommentWBOOKID(conn, thisBookid);

        deleteNotificationWBOOKID(conn, thisBookid);

        deleteBookGenreWBOOKID(conn, thisBookid);

        // deleting book after getting rid of it in other tables
        String query1 = "DELETE FROM book WHERE book_id = ?";
        try (PreparedStatement statement1 = conn.prepareStatement(query1)) {
            statement1.setInt(1, thisBookid);

            // Execute the deletion query and check if any rows were deleted
            int rowsDeleted = statement1.executeUpdate();
            if (rowsDeleted > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Book deleted successfully.");

                // Clear all input fields
                imagepathField.clear();
                firstnameField.clear();
                lastnameField.clear();
                titleField.clear();
                isbnField.clear();
                publisherField.clear();
                publishedyearField.clear();
                statusField.clear();
                desField.clear();
                quantityField.clear();
                rateField.clear();

                // Update the TableView with the remaining books
                tableViewBook.setItems(populateBookTable());
            } else {
                // Success deleting a book
                showAlert(Alert.AlertType.ERROR, "Error", "No Book found with the specified id.");
            }
        } catch (SQLException exe) {
            // If an error occurs while deleting the book, show an error message
            showAlert(Alert.AlertType.ERROR, "Error", "Error deleting Book: " + exe.getMessage());
            exe.printStackTrace();
        }

        // Delete the associated image record from the `images` table
        int thisImageid = selectedBook.getImage().getImage_id();
        String query2 = "DELETE FROM images WHERE image_id = ?";
        try (PreparedStatement statement2 = conn.prepareStatement(query2)) {
            statement2.setInt(1, thisImageid);

            // Execute the deletion query for the image
            int rowsDeleted = statement2.executeUpdate();
            if (rowsDeleted > 0) {
                // After successful image deletion, clear the input fields and update the TableView
                imagepathField.clear();
                firstnameField.clear();
                lastnameField.clear();
                titleField.clear();
                isbnField.clear();
                publisherField.clear();
                publishedyearField.clear();
                statusField.clear();
                desField.clear();
                quantityField.clear();
                rateField.clear();
                tableViewBook.setItems(populateBookTable());
            } else {
                return; // No need to delete image if not found
            }
        } catch (SQLException exe) {
            // If an error occurs while deleting the image, show an error message
            showAlert(Alert.AlertType.ERROR, "Error", "Error deleting Book: " + exe.getMessage());
            exe.printStackTrace();
        } finally {
            // Close the database connection after completing the deletion process
            db.closeConnection();
        }
    }

    /**
     * Creates the Book TableView and input fields for managing books.
     * This method sets up the TableView with columns for displaying book data
     * and provides a layout with input fields for adding, deleting, and clearing books.
     * It also sets up the layout, buttons, and event listeners.
     */
    public static void createBookTable() {
        // Create the TableView and set its preferred size
        tableViewBook = new TableView<>();
        tableViewBook.setPrefSize(1040, 800);

        // Define the columns for displaying book details
        TableColumn<BookAuthorImage, Integer> bookIdColumn = new TableColumn<>("Book ID");
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookid"));

        TableColumn<BookAuthorImage, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<BookAuthorImage, String> isbnColumn = new TableColumn<>("ISBN");
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));

        TableColumn<BookAuthorImage, String> publisherColumn = new TableColumn<>("Publisher");
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));

        TableColumn<BookAuthorImage, String> publishedYearColumn = new TableColumn<>("Published Year");
        publishedYearColumn.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));

        TableColumn<BookAuthorImage, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<BookAuthorImage, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Define columns for author details
        TableColumn<BookAuthorImage, String> authorFirstNameColumn = new TableColumn<>("Author First Name");
        authorFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("first_name"));

        TableColumn<BookAuthorImage, String> authorLastNameColumn = new TableColumn<>("Author Last Name");
        authorLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("last_name"));

        // Define columns for image path
        TableColumn<BookAuthorImage, String> imagePathColumn = new TableColumn<>("Image Path");
        imagePathColumn.setCellValueFactory(new PropertyValueFactory<>("image_path"));

        // Define remaining columns for book quantity, rate, genre ID, and genre type
        TableColumn<BookAuthorImage, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<BookAuthorImage, Integer> rateColumn = new TableColumn<>("Rate");
        rateColumn.setCellValueFactory(new PropertyValueFactory<>("rate"));

        TableColumn<BookAuthorImage, Integer> genreidColumn = new TableColumn<>("Genre ID");
        genreidColumn.setCellValueFactory(new PropertyValueFactory<>("genre_id"));

        TableColumn<BookAuthorImage, String> genreTypeColumn = new TableColumn<>("Genre Type");
        genreTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        // Set equal width for each column
        double columnWidth = 150; // Adjust as necessary
        bookIdColumn.setPrefWidth(columnWidth);
        titleColumn.setPrefWidth(columnWidth);
        isbnColumn.setPrefWidth(columnWidth);
        publisherColumn.setPrefWidth(columnWidth);
        publishedYearColumn.setPrefWidth(columnWidth);
        statusColumn.setPrefWidth(columnWidth);
        descriptionColumn.setPrefWidth(columnWidth);
        authorFirstNameColumn.setPrefWidth(columnWidth);
        authorLastNameColumn.setPrefWidth(columnWidth);
        imagePathColumn.setPrefWidth(columnWidth);
        quantityColumn.setPrefWidth(columnWidth);
        rateColumn.setPrefWidth(columnWidth);
        genreidColumn.setPrefWidth(columnWidth);
        genreTypeColumn.setPrefWidth(columnWidth);

        // Add columns to the table
        tableViewBook.getColumns().addAll(
                bookIdColumn, titleColumn, isbnColumn, publisherColumn,
                publishedYearColumn, statusColumn, descriptionColumn,
                authorFirstNameColumn, authorLastNameColumn,
                imagePathColumn, quantityColumn, rateColumn, genreidColumn, genreTypeColumn
        );

        // Create input fields and labels for each property
        Label bookidLabel = new Label("Book Id:");
        bookidField = new TextField();
        bookidField.setEditable(Boolean.FALSE);

        Label titleLabel = new Label("Title:");
        titleField = new TextField();
        titleField.setPromptText("Enter title");

        Label isbnLabel = new Label("ISBN:");
        isbnField = new TextField();
        isbnField.setPromptText("Enter isbn");

        Label publisherLabel = new Label("Publisher:");
        publisherField = new TextField();
        publisherField.setPromptText("Enter publisher");

        Label publishedYearLabel = new Label("Published Year:");
        publishedyearField = new TextField();
        publishedyearField.setPromptText("Enter published year");

        Label statusLabel = new Label("Status:");
        statusField = new TextField();
        statusField.setPromptText("Enter status");

        Label descriptionLabel = new Label("Description:");
        desField = new TextField();
        desField.setPromptText("Enter description");

        Label authorFirstNameLabel = new Label("Author First Name:");
        firstnameField = new TextField();
        firstnameField.setPromptText("Enter first name");

        Label authorLastNameLabel = new Label("Author Last Name:");
        lastnameField = new TextField();
        lastnameField.setPromptText("Enter last name");

        Label imagePathLabel = new Label("Image Path:");
        imagepathField = new TextField();
        imagepathField.setPromptText("Enter image path");

        Label quantityLabel = new Label("Quantity:");
        quantityField = new TextField();
        quantityField.setPromptText("Enter quantity");

        Label rateLabel = new Label("Rate:");
        rateField = new TextField();
        rateField.setPromptText("Enter rate");

        Label typeLabel = new Label("Type:");
        typeField = new TextField();
        typeField.setPromptText("Enter type");

        // Button to add a new book
        Button addButton = new Button("Add Book");
        addButton.setOnAction(e -> addBook(
                titleField.getText(),
                isbnField.getText(),
                publisherField.getText(),
                publishedyearField.getText(),
                statusField.getText(),
                desField.getText(),
                Integer.parseInt(quantityField.getText()),
                Integer.parseInt(rateField.getText()),
                imagepathField.getText(),
                firstnameField.getText(),
                lastnameField.getText(),
                typeField.getText()
        ));

        // Button to delete a book
        Button deleteButton = new Button("Delete Book");
        deleteButton.setOnAction(e -> deleteBook());

        // Button to clear all fields
        Button clearButton = new Button("Clear Fields");
        clearButton.setOnAction(e -> {
            bookidField.clear();
            titleField.clear();
            isbnField.clear();
            publisherField.clear();
            publishedyearField.clear();
            statusField.clear();
            desField.clear();
            firstnameField.clear();
            lastnameField.clear();
            imagepathField.clear();
            quantityField.clear();
            rateField.clear();
            typeField.clear();
        });

        // Create HBoxes for each label and field pair with alignment and padding
        HBox bookidBox = new HBox(10, bookidLabel, bookidField);
        bookidBox.setAlignment(Pos.CENTER_LEFT);
        bookidBox.setPadding(new Insets(5));

        HBox titleBox = new HBox(10, titleLabel, titleField);
        titleBox.setAlignment(Pos.CENTER_LEFT);
        titleBox.setPadding(new Insets(5));

        HBox isbnBox = new HBox(10, isbnLabel, isbnField);
        isbnBox.setAlignment(Pos.CENTER_LEFT);
        isbnBox.setPadding(new Insets(5));

        HBox publisherBox = new HBox(10, publisherLabel, publisherField);
        publisherBox.setAlignment(Pos.CENTER_LEFT);
        publisherBox.setPadding(new Insets(5));

        HBox publishedYearBox = new HBox(10, publishedYearLabel, publishedyearField);
        publishedYearBox.setAlignment(Pos.CENTER_LEFT);
        publishedYearBox.setPadding(new Insets(5));

        HBox statusBox = new HBox(10, statusLabel, statusField);
        statusBox.setAlignment(Pos.CENTER_LEFT);
        statusBox.setPadding(new Insets(5));

        HBox descriptionBox = new HBox(10, descriptionLabel, desField);
        descriptionBox.setAlignment(Pos.CENTER_LEFT);
        descriptionBox.setPadding(new Insets(5));

        HBox authorFirstNameBox = new HBox(10, authorFirstNameLabel, firstnameField);
        authorFirstNameBox.setAlignment(Pos.CENTER_LEFT);
        authorFirstNameBox.setPadding(new Insets(5));

        HBox authorLastNameBox = new HBox(10, authorLastNameLabel, lastnameField);
        authorLastNameBox.setAlignment(Pos.CENTER_LEFT);
        authorLastNameBox.setPadding(new Insets(5));

        HBox imagePathBox = new HBox(10, imagePathLabel, imagepathField);
        imagePathBox.setAlignment(Pos.CENTER_LEFT);
        imagePathBox.setPadding(new Insets(5));

        HBox quantityBox = new HBox(10, quantityLabel, quantityField);
        quantityBox.setAlignment(Pos.CENTER_LEFT);
        quantityBox.setPadding(new Insets(5));

        HBox rateBox = new HBox(10, rateLabel, rateField);
        rateBox.setAlignment(Pos.CENTER_LEFT);
        rateBox.setPadding(new Insets(5));

        HBox typeBox = new HBox(10, typeLabel, typeField);
        typeBox.setAlignment(Pos.CENTER_LEFT);
        typeBox.setPadding(new Insets(5));

        HBox buttonBox = new HBox(10, addButton, deleteButton);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        buttonBox.setPadding(new Insets(5));

        // Create VBox and set spacing, padding, and alignment
        VBox vbox = new VBox(10,
                bookidBox, titleBox, isbnBox, publisherBox, publishedYearBox, statusBox,
                descriptionBox, authorFirstNameBox, authorLastNameBox,
                imagePathBox, quantityBox, rateBox, typeBox, buttonBox, clearButton
        );
        vbox.setPadding(new Insets(20)); // Adds 20px padding around the VBox

        // Create a BorderPane and set the VBox to the left and the TableView to the center
        BorderPane layoutt = new BorderPane();
        layoutt.setLeft(vbox);
        layoutt.setCenter(tableViewBook);

        // Populate the TableView with data
        tableViewBook.setItems(populateBookTable());

        // Add listener to the TableView for row selection
        tableViewBook.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                bookidField.setText(String.valueOf(newValue.getBookid()));
                titleField.setText(newValue.getTitle());
                isbnField.setText(newValue.getIsbn());
                publisherField.setText(newValue.getPublisher());
                publishedyearField.setText(newValue.getPublishedDate());
                statusField.setText(newValue.getStatus());
                desField.setText(newValue.getDescription());
                firstnameField.setText(newValue.getAuthor().getFirst_name());
                lastnameField.setText(newValue.getAuthor().getLast_name());
                imagepathField.setText(newValue.getImage().getImage_path());
                quantityField.setText(String.valueOf(newValue.getQuantity()));
                rateField.setText(String.valueOf(newValue.getRate()));
                typeField.setText(newValue.getType());
            }
        });

        // Add the layout to the center of the main container
        center.add(layoutt, 0, 0);
    }
}
