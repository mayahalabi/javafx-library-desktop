package com.example.project.Services;

import com.example.project.Classes.book;
import com.example.project.Classes.borrowing_transaction;
import com.example.project.Classes.checkTableClass;
import com.example.project.Connection.DBHandler;
import com.example.project.loginOptions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.example.project.Services.bookServices.checkbookid;
import static com.example.project.Services.notificationServices.checkReminder;
import static com.example.project.Services.userServices.checkUsername;
import static com.example.project.Settings.settingAdminForm.center;
import static com.example.project.Settings.settingUserForm.center1;
import static com.example.project.loginOptions.showAlert;

/**
 * This service class handles the borrowing transaction-related functionalities,
 * such as populating the transaction table, adding a new borrowing transaction,
 * and managing transaction data. It interacts with the database to fetch and store transaction data,
 * specifically related to borrowing books by users.
 */
public class borrowingTransactionServices {


    public static TableView<borrowing_transaction> tableView;

    public static  TextField issueDateField;
    public static TextField dueDateField;
    public static TextField returnDateField;
    public static TextField usernameField;
    public static TextField bookidField;

    /**
     * Populates the borrowing transactions table with data from the database.
     * This method queries the `borrowing_transaction` table for transaction records,
     * processes the data, and returns an ObservableList containing the transactions.
     *
     * @return ObservableList<borrowing_transaction> A list of borrowing transactions.
     */
    public static ObservableList<borrowing_transaction> populateTransactionTable() {
        DBHandler db = new DBHandler();
        Connection conn = db.getConnection();

        // Initialize an observable list for transactions
        ObservableList<borrowing_transaction> transactionList = FXCollections.observableArrayList();

        // SQL query to fetch transaction details
        String query = "SELECT transaction_id, issue_date, due_date, return_date, username, book_id FROM borrowing_transaction";
        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            // Process each row of the result set
            while (resultSet.next()) {
                // Retrieve data for each transaction
                int thistransactionid = resultSet.getInt("transaction_id");
                Timestamp thisduedate = resultSet.getTimestamp("due_date");
                Timestamp thisreturndate = resultSet.getTimestamp("return_date");
                String thisusername = resultSet.getString("username");
                int thisbookid = resultSet.getInt("book_id");

                // If the return date is null, the transaction is still open (book not returned)
                if (thisreturndate == null || thisreturndate.equals("")) {
                    transactionList.add(new borrowing_transaction(thistransactionid, thisduedate, thisusername, thisbookid));
                } else {
                    // If the return date is present, the transaction has been closed (book returned)
                    transactionList.add(new borrowing_transaction(thistransactionid, thisduedate, thisreturndate, thisusername, thisbookid));
                }

            }
        } catch (SQLException exe) {
            // Show error message if something goes wrong
            showAlert(Alert.AlertType.ERROR, "Error", "Error loading transaction data: " + exe.getMessage());
            exe.printStackTrace();
        } finally {
            // Close the database connection after the query is complete
            db.closeConnection();
        }
        // Return the populated list of transactions
        return transactionList;
    }

    /**
     * Adds a new borrowing transaction to the database.
     *
     * @param duedate The due date for returning the book.
     * @param returndate The date the book was actually returned (can be null if not yet returned).
     * @param username The username of the person borrowing the book.
     * @param bookid The ID of the book being borrowed.
     */
    public static void addTransaction(Timestamp duedate, Timestamp returndate, String username, int bookid) {
        // Validate input fields to ensure no empty or invalid values
        if (duedate == null || username.isEmpty() || bookid <= 0) {
            // Show error alert if any of the fields are invalid
            showAlert(Alert.AlertType.ERROR, "Error", "Check if you entered those fields:\nTransaction Id\nDue Date\nUsername\nbook Id.");
            return;
        }

        try {
            // Establish connection to the database
            DBHandler db = new DBHandler();
            Connection conn = db.getConnection();

            // Validate if the username exists and the book ID is valid
            if (checkUsername(username) && checkbookid(bookid)) {
                // SQL query to insert the new borrowing transaction
                String query = "INSERT INTO borrowing_transaction (issue_date, due_date, return_date, username, book_id) VALUES (NOW(), ?, ?, ?, ?)";

                try (PreparedStatement statement = conn.prepareStatement(query)) {
                    // Set the values for the prepared statement
                    statement.setTimestamp(1, duedate);
                    statement.setTimestamp(2, returndate);
                    statement.setString(3, username);
                    statement.setInt(4, bookid);

                    // Execute the query to insert the transaction into the database
                    int rowsInserted = statement.executeUpdate();

                    if (rowsInserted > 0) {
                        // Success: show an informational alert
                        showAlert(Alert.AlertType.INFORMATION, "Success", "A new borrowing transaction has been added successfully.");

                        // Clear input fields after successful transaction addition
                        dueDateField.clear();
                        returnDateField.clear();
                        usernameField.clear();
                        bookidField.clear();

                        // Refresh the table view with updated transaction data
                        tableView.setItems(populateTransactionTable());
                    }
                } catch (SQLException exe) {
                    // Handle SQL exceptions during insertion
                    showAlert(Alert.AlertType.ERROR, "Error", "Error adding new transaction: " + exe.getMessage());
                    exe.printStackTrace();
                } finally {
                    // Close the database connection after the operation
                    db.closeConnection();
                }
            } else {
                // Show error if username or book ID is invalid
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid username or book ID.");
            }
        } catch (Exception e) {
            // Handle any other exceptions
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding transaction.");
            e.printStackTrace();
        }
    }

    /**
     * Deletes a borrowing transaction from the database based on the selected transaction.
     *
     * @param transactionid The ID of the transaction to be deleted.
     */
    public static void deleteTransaction(int transactionid) {
        // Retrieve the currently selected transaction from the TableView
        borrowing_transaction selectedTransaction = tableView.getSelectionModel().getSelectedItem();

        // Check if a transaction is selected, if not, show an error message
        if (selectedTransaction == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a transaction to delete.");
            return;
        }

        // Get the transaction ID from the selected transaction
        int thisTransactionid = selectedTransaction.getTransaction_id();

        // Initialize database connection
        DBHandler db = new DBHandler();
        Connection conn = db.getConnection();

        // SQL query to delete the transaction based on the transaction ID
        String query = "DELETE FROM borrowing_transaction WHERE transaction_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            // Set the transaction ID in the prepared statement
            statement.setInt(1, thisTransactionid);

            // Execute the DELETE query and check how many rows were deleted
            int rowsDeleted = statement.executeUpdate();

            // If the transaction was successfully deleted, show success message
            if (rowsDeleted > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Transaction deleted successfully.");

                // Clear the input fields after successful deletion
                dueDateField.clear();
                returnDateField.clear();
                usernameField.clear();
                bookidField.clear();

                // Refresh the table to reflect the deletion
                tableView.setItems(populateTransactionTable());
            } else {
                // If no rows were deleted, show error message
                showAlert(Alert.AlertType.ERROR, "Error", "No borrowing transaction found with the specified id.");
            }
        } catch (SQLException exe) {
            // Handle any SQL exceptions that occur during the deletion process
            showAlert(Alert.AlertType.ERROR, "Error", "Error deleting transaction: " + exe.getMessage());
            exe.printStackTrace();
        } finally {
            // Close the database connection after the operation is complete
            db.closeConnection();
        }
    }

    /**
     * Fetches all the book IDs from the database's `book` table.
     *
     * @return ObservableList<Integer> A list of book IDs.
     */
    public static ObservableList<Integer> fetchBookIDs() {
        // Initialize the list to store book IDs
        ObservableList<Integer> bookIDs = FXCollections.observableArrayList();

        // Establish a connection to the database
        DBHandler db = new DBHandler();
        Connection conn = db.getConnection();

        // SQL query to retrieve all book IDs from the book table
        String query = "SELECT book_id FROM book";

        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            // Iterate through the result set and add book IDs to the list
            while (resultSet.next()) {
                bookIDs.add(resultSet.getInt("book_id"));
            }
        } catch (SQLException exe) {
            // Handle any SQL exceptions by showing an error message
            showAlert(Alert.AlertType.ERROR, "Error", "Error loading book IDs: " + exe.getMessage());
            exe.printStackTrace();
        } finally {
            // Close the database connection
            db.closeConnection();
        }
        // Return the list of book IDs
        return bookIDs;
    }

    /**
     * Fetches all the usernames from the database's `user` table.
     *
     * @return ObservableList<String> A list of usernames.
     */
    public static ObservableList<String> fetchUsernames() {
        // Initialize the list to store usernames
        ObservableList<String> usernames = FXCollections.observableArrayList();

        // Establish a connection to the database
        DBHandler db = new DBHandler();
        Connection conn = db.getConnection();

        // SQL query to retrieve all usernames from the user table
        String query = "SELECT username FROM user";
        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            // Iterate through the result set and add usernames to the list
            while (resultSet.next()) {
                usernames.add(resultSet.getString("username"));
            }
        } catch (SQLException exe) {
            // Handle any SQL exceptions by showing an error message
            showAlert(Alert.AlertType.ERROR, "Error", "Error loading usernames: " + exe.getMessage());
            exe.printStackTrace();
        } finally {
            // Close the database connection
            db.closeConnection();
        }
        // Return the list of usernames
        return usernames;
    }

    /**
     * Creates and initializes the TableView for displaying borrowing transactions.
     * Sets up the input fields, buttons, and event handlers for adding, deleting,
     * updating, and extending transactions.
     */
    public static void createTransactionTable() {
        // Create the TableView and its columns
        tableView = new TableView<>();
        tableView.setPrefSize(1040, 800);

        // Create columns for transaction details
        TableColumn<borrowing_transaction, String> idColumn = new TableColumn<>("Transaction Id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("transaction_id"));

        TableColumn<borrowing_transaction, String> issueColumn = new TableColumn<>("Issue Date");
        issueColumn.setCellValueFactory(new PropertyValueFactory<>("issue_date"));

        TableColumn<borrowing_transaction, String> duedateColumn = new TableColumn<>("Due Date");
        duedateColumn.setCellValueFactory(new PropertyValueFactory<>("due_date"));

        TableColumn<borrowing_transaction, String> returndateColumn = new TableColumn<>("Return Date");
        returndateColumn.setCellValueFactory(new PropertyValueFactory<>("return_date"));

        TableColumn<borrowing_transaction, String> userColumn = new TableColumn<>("Username");
        userColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<borrowing_transaction, String> bookColumn = new TableColumn<>("Book Id");
        bookColumn.setCellValueFactory(new PropertyValueFactory<>("book_id"));

        // Set equal width for each column
        double columnWidth = 200; // Desired width for each column
        idColumn.setPrefWidth(columnWidth);
        issueColumn.setPrefWidth(columnWidth);
        duedateColumn.setPrefWidth(columnWidth);
        returndateColumn.setPrefWidth(columnWidth);
        userColumn.setPrefWidth(columnWidth);
        bookColumn.setPrefWidth(columnWidth);

        // Add columns to the table
        tableView.getColumns().addAll(idColumn, issueColumn, duedateColumn, returndateColumn, userColumn, bookColumn);

        // Create the input fields and labels for transaction details
        Label issueLabel = new Label("Issue Date:");
        issueDateField = new TextField();
        issueDateField.setPromptText("yyyy-MM-dd HH:mm:ss");

        Label dueLabel = new Label("Due Date:");
        dueDateField = new TextField();
        dueDateField.setPromptText("yyyy-MM-dd HH:mm:ss");

        Label returnLabel = new Label("Return Date:");
        returnDateField = new TextField();
        returnDateField.setPromptText("yyyy-MM-dd HH:mm:ss");

        Label userLabel = new Label("Username:");
        usernameField = new TextField();
        usernameField.setPromptText("Enter username");

        Label bookLabel = new Label("Book Id:");
        bookidField = new TextField();
        bookidField.setPromptText("Enter book id");

        // Button to add a new transaction
        Button addButton = new Button("Add Transaction");
        addButton.setOnAction(e -> addTransaction(Timestamp.valueOf(dueDateField.getText()),
                Timestamp.valueOf(returnDateField.getText()),
                usernameField.getText(),
                Integer.parseInt(bookidField.getText())));

        // Button to delete a selected transaction
        Button deleteButton = new Button("Delete Transaction");
        deleteButton.setOnAction(e -> {
            // Retrieve the currently selected item in the event handler
            borrowing_transaction selectedItem = tableView.getSelectionModel().getSelectedItem();

            if (selectedItem != null) {
                deleteTransaction(selectedItem.getTransaction_id());
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Transaction not selected.");
            }
        });

        // Button to update the selected transaction
        Button updateButton = new Button("Update Transaction");
        updateButton.setOnAction(e -> {
            // Retrieve the currently selected item in the event handler
            borrowing_transaction selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                updateTransaction(selectedItem.getTransaction_id(), Timestamp.valueOf(dueDateField.getText()), Timestamp.valueOf(returnDateField.getText()), usernameField.getText(), Integer.parseInt(bookidField.getText()));
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Transaction not selected.");
            }
        });

        // Button for extending the due date of the transaction
        Button extend = new Button("Extention");
        extend.setOnAction(e -> {
                borrowExtend(usernameField.getText(), Integer.parseInt(bookidField.getText()));
                //populateTransactionTable();
        });


        // Create a VBox to hold the input fields and buttons
        VBox inputBox = new VBox(10);
        inputBox.setPadding(new Insets(10));
        inputBox.getChildren().addAll(dueLabel, dueDateField, returnLabel, returnDateField, userLabel, usernameField, bookLabel, bookidField, addButton, deleteButton, updateButton, extend);

        // Create a BorderPane and set the VBox to the left and the TableView to the center
        BorderPane layoutt = new BorderPane();
        layoutt.setLeft(inputBox);
        layoutt.setCenter(tableView);

        // Populate the TableView with data
        tableView.setItems(populateTransactionTable());

        // Add listener to the TableView for row selection
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                dueDateField.setText(newValue.getIssue_date());
                returnDateField.setText(newValue.getDue_date());
                usernameField.setText(newValue.getUsername());
                bookidField.setText(String.valueOf(newValue.getBook_id()));
            }
        });

        // Add the layout to the center of the main pane
        center.add(layoutt, 0, 0);
    }

    /**
     * Creates and returns a ScrollPane that contains a GridPane displaying the borrowed books.
     *
     * @return A ScrollPane containing a GridPane with borrowed books.
     */
    public static ScrollPane displayBorrowedBookScrollPane() {
        // Retrieve the GridPane for displaying borrowed books
        GridPane bookGrid = getBorrowedBookGrid();

        // Create a ScrollPane and set the GridPane as its content
        ScrollPane scrollPane = new ScrollPane(bookGrid);

        // Configure the ScrollPane to fit the content width and add padding
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));

        // Return the configured ScrollPane
        return scrollPane;
    }

    /**
     * Creates and returns a GridPane that displays the borrowed books with their covers.
     *
     * @return A GridPane containing ImageViews of borrowed books.
     */
    public static GridPane getBorrowedBookGrid() {
        // Initialize a GridPane for displaying books
        GridPane content = new GridPane();

        // Set horizontal and vertical gaps between grid elements
        content.setHgap(10);
        content.setVgap(10);
        content.setPadding(new Insets(5));

        // Fetch the list of borrowed books from the database
        List<book> books = getBorrowedBooks(); // Fetch books from the database
        int columns = 15;  // Adjust the number of columns per row

        // Loop through each book in the list and create ImageView for its cover
        for (int i = 0; i < books.size(); i++) {
            // Get the current book object
            book currentBook = books.get(i);

            // Safely create an ImageView for the current book's cover
            ImageView bookCover = bookServices.createImageView(currentBook);

            if (bookCover != null) {
                // Store the current book object in the ImageView's userData
                bookCover.setUserData(currentBook);

                // Calculate the row and column in the grid where the book cover will be placed
                int row = i / columns;
                int col = i % columns;

                // Add the ImageView to the GridPane
                content.add(bookCover, col, row);
            }
        }
        // Return the populated GridPane
        return content;
    }

    /**
     * Retrieves the list of books that have been borrowed by the currently logged-in user.
     *
     * This method queries the database for borrowing transactions of the logged-in user,
     * joining the `borrowing_transaction`, `book`, `author`, and `images` tables to
     * gather the complete details of each borrowed book.
     *
     * @return A list of `book` objects representing the borrowed books.
     */
    public static List<book> getBorrowedBooks() {
        // Initialize an empty list to store the borrowed books
        List<book> books = new ArrayList<>();

        // Establish a connection to the database
        DBHandler db = new DBHandler();
        Connection con = db.getConnection();

        // SQL query to fetch books borrowed by the currently logged-in user
        String querry = "SELECT * FROM borrowing_transaction natural join book natural join author natural join images " +
                "WHERE username = ?";

        try (PreparedStatement statement = con.prepareStatement(querry)) {
            // Set the username of the currently logged-in user in the query
            statement.setString(1, loginOptions.getUsername());

            // Execute the query and retrieve the result set
            ResultSet rs = statement.executeQuery();

            // Iterate through the result set and map each row to a book object
            while (rs.next()) {
                // Add the mapped book to the list
                books.add(bookServices.mapResultSetToBook(rs));
            }

        } catch (SQLException e) {
            // Print the stack trace in case of an error during the database query
            e.printStackTrace();
        } finally {
            // Ensure the database connection is closed after the operation
            db.closeConnection();
        }
        // Return the list of borrowed books
        return books;
    }

    /**
     * This method handles the process of adding a borrowing transaction for a book.
     * It checks if the book is already borrowed by the user or if the book is out of stock.
     * If neither of these conditions is met, it inserts a new borrowing transaction into the database.
     *
     * @param username The username of the person borrowing the book.
     * @param thisBook The book being borrowed.
     */
    public static void addBorrow(String username, book thisBook) {

        // Get the current time as an Instant object
        Instant now = Instant.now();

        // Add one month to the current time to set the due date
        LocalDateTime oneMonthLater = LocalDateTime.now().plus(1, ChronoUnit.MONTHS);

        // Convert the due date from LocalDateTime to Instant
        Instant oneMonthLaterInstant = oneMonthLater.atZone(ZoneId.systemDefault()).toInstant();

        // Convert Instant to SQL Timestamp for database compatibility
        Timestamp oneMonthLaterTimestamp = Timestamp.from(oneMonthLaterInstant);

        // Initialize DBHandler to establish a database connection
        DBHandler db = new DBHandler();
        Connection conn = db.getConnection();

        // Initialize DBHandler to establish a database connection
        if(checkBorrowed(thisBook.getBookid(),username)){
            // Show error alert if the book is already borrowed
            loginOptions.showAlert(Alert.AlertType.ERROR, "Invalid", "Book already Borrowed");
        } else if(thisBook.getQuantity() <= 0){
            // Show error alert if the book is out of stock
            loginOptions.showAlert(Alert.AlertType.ERROR, "Invalid", "Book is out of stock");
        } else {
            // SQL query to insert a new borrowing transaction
            String query = "INSERT INTO borrowing_transaction (issue_date, due_date, username, book_id) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                // Set the issue date (current time)
                statement.setTimestamp(1, Timestamp.from(Instant.now())); // Automatically set issue date to current time

                // Set the due date (one month from now)
                statement.setTimestamp(2, oneMonthLaterTimestamp);

                // Set the username (borrower)
                statement.setString(3, username);

                // Set the book ID
                statement.setInt(4, thisBook.getBookid()    );

                // Execute the insert statement and check if the insertion was successful
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {

                    // If the transaction is successfully added, reduce the quantity of the book
                    changeQuantity(thisBook.getBookid());

                    // If the book's quantity becomes zero, set its status to "Out of stock"
                    if(thisBook.getQuantity() <= 0)
                        thisBook.setStatus("Out of stock");

                    // Show success alert
                    loginOptions.showAlert(Alert.AlertType.INFORMATION, "Success", "A new borrowing transaction has been added successfully.");
                }
            } catch (SQLException exe) {
                // Handle any SQL errors and display an error alert
                loginOptions.showAlert(Alert.AlertType.ERROR, "Error", "Error adding new transaction: " + exe.getMessage());
                exe.printStackTrace();
            } finally {
                // Close the database connection after the operation is complete
                db.closeConnection();
            }
        }
    }

    /**
     * Checks if a specific book has already been borrowed by a user.
     *
     * @param bookid The ID of the book being checked.
     * @param username The username of the person who may have borrowed the book.
     * @return boolean Returns true if the user has already borrowed the book, otherwise false.
     */
    public static boolean checkBorrowed(int bookid, String username) {
        // Initialize DBHandler to establish a connection to the database
        DBHandler db = new DBHandler();

        // Use try-with-resources to ensure proper closure of the database connection and prepared statement
        try (Connection conn = db.getConnection();
             PreparedStatement statement = conn.prepareStatement(
                     "SELECT * FROM borrowing_transaction WHERE username = ? AND book_id = ?")) {

            // Set the parameters for the query
            statement.setString(1, username);
            statement.setInt(2, bookid);

            // Execute the query and store the result in ResultSet
            try (ResultSet rs = statement.executeQuery()) {
                // If a result exists, the book is borrowed
                return rs.next();
            }

        } catch (SQLException e) {
            // Print the stack trace in case of an SQL error
            e.printStackTrace();
            return false; // Return false if an exception occurs (as a fallback)
        } finally {
            // Ensure the database connection is closed after the operation is complete
            db.closeConnection();
        }
    }

    /**
     * Decreases the quantity of a book by 1 in the database.
     * This method is typically used when a book is borrowed, reducing its available stock.
     *
     * @param book_id The ID of the book whose quantity is to be updated.
     */
    public static void changeQuantity(int book_id) {
        // Initialize DBHandler to get a connection to the database
        DBHandler db = new DBHandler();
        Connection conn = db.getConnection();

        // SQL query to decrease the book's quantity by 1
        String query = "UPDATE book SET quantity = quantity - 1 where book_id  = ?";

        // Use try-with-resources to ensure proper closure of the PreparedStatement
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            // Set the book_id parameter for the query
            statement.setInt(1, book_id);

            // Execute the update statement to decrease the quantity
            int rowsInserted = statement.executeUpdate();

        } catch (SQLException e) {
            // Print the stack trace if an error occurs during the database operation
            e.printStackTrace();
        } finally {
            // Ensure the database connection is closed after the operation
            db.closeConnection();
        }
    }

    /**
     * Checks if a borrowing transaction with a specific transaction ID exists in the database.
     * This method verifies whether the transaction ID provided corresponds to an existing transaction.
     *
     * @param transactionid The transaction ID to be checked.
     * @return boolean Returns true if the transaction exists, otherwise false.
     */
    public static boolean checktransactionId(int transactionid) {
        // Initialize DBHandler to get a connection to the database
        DBHandler db = new DBHandler();

        // Attempt to establish a connection to the database and perform the query
        try {
            Connection conn = db.getConnection();

            // SQL query to check if a transaction with the given transaction_id exists
            String query = "SELECT * FROM borrowing_transaction WHERE transaction_id = ?"; // Corrected 'transaction_id'

            // Use try-with-resources to ensure proper closure of the PreparedStatement
            try (PreparedStatement statement = conn.prepareStatement(query)) {

                // Set the transaction ID parameter for the query
                statement.setInt(1, transactionid);

                // Execute the query and get the result
                ResultSet resultSet = statement.executeQuery();

                // If a result exists, the transaction is found
                if (resultSet.next()) { // Check if there is at least one result
                    return true; // Transaction found
                } else {
                    // If no result is found, show an error alert
                    showAlert(Alert.AlertType.ERROR, "Error", "Transaction does not exist.");
                }
            }
        } catch (SQLException e) {
            // Print the stack trace in case of an SQL error
            e.printStackTrace();
        } finally {
            // Ensure the database connection is closed after the operation
           db.closeConnection();
        }
        return false; // Transaction not found
    }

    /**
     * Fetches a list of borrowed transactions for a specific user from the database.
     * This method retrieves all borrowing transactions associated with a given username.
     *
     * @param username The username of the user whose borrowing transactions are to be fetched.
     * @return ObservableList<checkTableClass> A list of checkTableClass objects containing borrowing transaction data.
     */
    public static ObservableList<checkTableClass> fetchBorrowedTransactionsBySpecificUser(String username) {

        // Initialize an ObservableList to store the borrowed transactions
        ObservableList<checkTableClass> borrowedList = FXCollections.observableArrayList();

        // Initialize DBHandler to establish a connection to the database
        DBHandler db = new DBHandler();
        Connection conn = db.getConnection();

        // SQL query to fetch borrowing transactions along with book details for a specific user
        String query = "SELECT bt.transaction_id, bt.issue_date, bt.due_date, bt.return_date, bt.username, bt.book_id, b.title " +
                "FROM borrowing_transaction bt JOIN book b ON bt.book_id = b.book_id WHERE bt.username = ?";

        // Use try-with-resources to automatically close the PreparedStatement
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            // Set the username parameter for the query
            statement.setString(1, username);

            // Execute the query and retrieve the results in a ResultSet
            ResultSet resultSet = statement.executeQuery();

            // Iterate through the ResultSet to retrieve each transaction's details
            while (resultSet.next()) {
                // Retrieve data from the ResultSet for each column
                Timestamp issue = resultSet.getTimestamp("issue_date");
                Timestamp due = resultSet.getTimestamp("due_date");
                Timestamp returnDate = resultSet.getTimestamp("return_date");
                int bookId = resultSet.getInt("book_id");
                String title = resultSet.getString("title");

                // Add the transaction data to the borrowedList as a new checkTableClass object
                borrowedList.add(new checkTableClass(username, bookId, title, issue, due, returnDate));
            }
        } catch (SQLException e) {
            // Handle any SQLException
            e.printStackTrace();
        } finally {
            // Ensure that the database connection is closed after the operation
            db.closeConnection();
        }
        // Return the list of borrowed transactions
        return borrowedList;
    }

    /**
     * Creates and displays a table showing all borrowed transactions for a specific user.
     * This method sets up a TableView to present transaction data and provides an option
     * to set a reminder for the selected borrowing transaction.
     *
     * @param username The username for which the borrowed transactions will be displayed.
     */
    public static void createBorrowedTransactionTableForUser(String username) {
        // Create a new TableView to display borrowed transactions for the user
        TableView<checkTableClass> tableViewForUser = new TableView<>();
        tableViewForUser.setPrefSize(800, 600);

        // Define the columns for the table
        TableColumn<checkTableClass, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<checkTableClass, Integer> bookIdColumn = new TableColumn<>("Book ID");
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));

        TableColumn<checkTableClass, Timestamp> issueDateColumn = new TableColumn<>("Issue Date");
        issueDateColumn.setCellValueFactory(new PropertyValueFactory<>("issueDate"));

        TableColumn<checkTableClass, Timestamp> dueDateColumn = new TableColumn<>("Due Date");
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        TableColumn<checkTableClass, Timestamp> returnDateColumn = new TableColumn<>("Return Date");
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        // Set the preferred width for each column
        usernameColumn.setPrefWidth(150);
        bookIdColumn.setPrefWidth(100);
        issueDateColumn.setPrefWidth(150);
        dueDateColumn.setPrefWidth(150);
        returnDateColumn.setPrefWidth(150);

        // Add the defined columns to the TableView
        tableViewForUser.getColumns().addAll(usernameColumn, bookIdColumn, issueDateColumn, dueDateColumn, returnDateColumn);

        // Fetch borrowed transactions for the specified user and populate the TableView
        tableViewForUser.setItems(fetchBorrowedTransactionsBySpecificUser(username));

        // Create a Button for setting a reminder
        Button reminderButton = new Button("Check Reminder");
        reminderButton.setOnAction(e -> {
            // Get the selected transaction from the TableView
            checkTableClass selectedTransaction = tableViewForUser.getSelectionModel().getSelectedItem();

            // If a transaction is selected, trigger the reminder logic
            if (selectedTransaction != null) {
                checkReminder(selectedTransaction.getBookId(), selectedTransaction.getUsername());
            } else {
                // If no transaction is selected, show an error message
                showAlert(Alert.AlertType.ERROR, "Error", "Please select a borrowing transaction to set a reminder.");
            }
        });

        // Create a VBox layout to hold the reminder button
        VBox inputBox = new VBox(10);
        inputBox.setPadding(new Insets(10));
        inputBox.getChildren().addAll(reminderButton);

        // Create a BorderPane layout
        BorderPane layoutt = new BorderPane();
        layoutt.setLeft(inputBox);
        layoutt.setCenter(tableViewForUser);

        // Add the layout to the grid
        center1.add(layoutt, 0, 0);
    }

    /**
     * Retrieves the transaction ID for a specific borrowing transaction based on the username and book ID.
     * This method fetches the transaction ID from the database using the provided username and book ID.
     *
     * @param username The username of the user who borrowed the book.
     * @param bookid The ID of the borrowed book.
     * @return The transaction ID if the transaction is found, or -1 if an error occurs or no transaction is found.
     */
    public static int getTransactionId(String username, int bookid) {
        DBHandler db = null; // DBHandler instance for database connection
        Connection conn = null; // Connection object to interact with the database

        try {
            // Initialize DBHandler and establish a database connection
            db = new DBHandler();
            conn = db.getConnection();

            // SQL query to fetch the transaction ID for a specific user and book
            String query = "SELECT transaction_id FROM borrowing_transaction WHERE username = ? AND book_id = ?";

            // Use try-with-resources to automatically close the PreparedStatement
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, username);
                statement.setInt(2, bookid);

                // Execute the query and retrieve the result
                ResultSet resultSet = statement.executeQuery();

                // If the query returns a result, retrieve the transaction ID
                if (resultSet.next()) {
                    return resultSet.getInt("transaction_id");
                }
            } catch (SQLException e) {
                // Handle SQL exceptions
                showAlert(Alert.AlertType.ERROR, "Error", "Error fetching transaction: " + e.getMessage());
                e.printStackTrace(); // Consider logging instead of printing
            }
        } catch (Exception e) {
            // Handle any other exceptions
            showAlert(Alert.AlertType.ERROR, "Error", "Error fetching a transaction: " + e.getMessage());
            e.printStackTrace(); // Consider logging instead of printing
        } finally {
            // Ensure the database connection is closed properly
            if (db != null) {
                db.closeConnection(); // Ensure that the connection is closed
            }
        }
        return -1; // Return -1 if no transaction is found or in case of an error
    }

    /**
     * Updates a borrowing transaction with new due date, return date, username, and book ID based on the transaction ID.
     * If no changes are detected, an alert is shown, and the update process is skipped.
     *
     * @param transactionId The transaction ID of the borrowing transaction to update.
     * @param newDueDate The new due date for the transaction.
     * @param newReturnDate The new return date for the transaction.
     * @param username The new username for the transaction.
     * @param bookId The new book ID for the transaction.
     */
    public static void updateTransaction(int transactionId, Timestamp newDueDate, Timestamp newReturnDate, String username, int bookId) {
        DBHandler db = new DBHandler(); // DBHandler instance for managing database connection
        Connection conn = db.getConnection(); // Establish the connection to the database

        // SQL queries: one for checking the current values of the transaction, the other for updating the transaction
        String checkQuery = "SELECT due_date, return_date, username, book_id FROM borrowing_transaction WHERE transaction_id = ?";
        String updateQuery = "UPDATE borrowing_transaction SET due_date = ?, return_date = ?, username = ?, book_id = ? WHERE transaction_id = ?";

        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {

            // Set the transaction ID to find the relevant transaction
            checkStmt.setInt(1, transactionId);

            // Execute the query to fetch the current transaction details
            ResultSet rs = checkStmt.executeQuery();

            // Check if the new values are the same as the current values
            if (rs.next()) {
                // Retrieve the current values from the database
                Timestamp currentDueDate = rs.getTimestamp("due_date");
                Timestamp currentReturnDate = rs.getTimestamp("return_date");
                String currentUsername = rs.getString("username");
                int currentBookId = rs.getInt("book_id");

                // Check if all new values are the same as the current values
                if (newDueDate.equals(currentDueDate) &&
                        newReturnDate.equals(currentReturnDate) &&
                        username.equals(currentUsername) &&
                        bookId == currentBookId) {

                    // If no changes are detected, show a warning and return
                    showAlert(Alert.AlertType.WARNING, "Warning", "Change something to be updated.");
                    return;
                }
            } else {
                // If no transaction is found with the provided transaction ID, show an error
                showAlert(Alert.AlertType.ERROR, "Error", "No transaction found with the provided transaction ID.");
                return;
            }

            // Proceed to update the transaction if there are any differences in values
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                // Set the new values for the update
                updateStmt.setTimestamp(1, newDueDate);
                updateStmt.setTimestamp(2, newReturnDate);
                updateStmt.setString(3, username);
                updateStmt.setInt(4, bookId);
                updateStmt.setInt(5, transactionId);

                // Execute the update query
                int rowsUpdated = updateStmt.executeUpdate();
                if (rowsUpdated > 0) {
                    // If the update was successful, show an info alert and repopulate the table
                    showAlert(Alert.AlertType.INFORMATION, "Info", "Transaction updated successfully.");
                    // Update the transaction table view if needed
                    tableView.setItems(populateTransactionTable()); // Assumes you have a method to repopulate your table
                } else {
                    // If no rows were updated, show an error
                    showAlert(Alert.AlertType.ERROR, "Error", "No transaction found with the provided transaction ID.");
                }
            }
        } catch (SQLException e) {
            // Handle any SQL exceptions
            showAlert(Alert.AlertType.ERROR, "Error", "Error updating transaction: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Always close the connection in the finally block to avoid resource leaks
            db.closeConnection();
        }
    }

    /**
     * Deletes all borrowing transactions associated with a given book ID.
     *
     * @param conn The database connection used to execute the query.
     * @param bookid The ID of the book whose associated transactions should be deleted.
     */
    public static void deleteTransactionWBookID(Connection conn, int bookid) {

        // SQL query to delete borrowing transactions where the book_id matches the provided bookid
        String query = "DELETE FROM borrowing_transaction WHERE book_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            // Set the book ID parameter in the prepared statement
            statement.setInt(1, bookid);
            statement.executeUpdate();  // Execute delete
            System.out.println("Deleted transactions for book ID: " + bookid);
        } catch (SQLException exe) {
            // Handle SQL exceptions
            showAlert(Alert.AlertType.ERROR, "Error", "Error deleting transactions: " + exe.getMessage());
            exe.printStackTrace();
        }
    }

    /**
     * Extends the due date of a borrowing transaction by one month for a specific user and book.
     *
     * @param username The username of the user who borrowed the book.
     * @param bookId The ID of the book whose due date needs to be extended.
     */
    public static void borrowExtend(String username, int bookId) {
        DBHandler db = new DBHandler();

        // Establish connection with the database
        try (
             Connection conn = db.getConnection()) {

            // Query to fetch the current due date of the specified book for the user
            String query = "SELECT due_date FROM borrowing_transaction WHERE username = ? AND book_id = ?";

            // Query to update the due date in the database
            String updateQuery = "UPDATE borrowing_transaction SET due_date = ? WHERE username = ? AND book_id = ?";

            // Prepare and execute the query to get the current due date
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                // set the fields
                statement.setString(1, username);
                statement.setInt(2, bookId);
                ResultSet rs = statement.executeQuery();

                // If a result is found, extend the due date by 1 month
                if (rs.next()) {
                    // Get the current due date
                    Timestamp currentDueDate = rs.getTimestamp("due_date");

                    // Add 1 month to the current due date
                    LocalDateTime newDueDate = currentDueDate.toLocalDateTime().plusMonths(1);

                    // Update the due date
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                        updateStmt.setTimestamp(1, Timestamp.valueOf(newDueDate));
                        updateStmt.setString(2, username);
                        updateStmt.setInt(3, bookId);
                        int rowsUpdated = updateStmt.executeUpdate();

                        // If the update was successful, show a success message
                        if (rowsUpdated > 0) {
                            showAlert(Alert.AlertType.INFORMATION, "Info", "Transaction updated successfully.");
                            tableView.setItems(populateTransactionTable());  // Refresh the table view
                        } else {
                            // If no rows were updated, show an error message
                            showAlert(Alert.AlertType.ERROR, "Error", "Cannot extend; check with administrator.");
                        }
                    }
                } else {
                    // If no transaction is found for the user and book, show a warning
                    showAlert(Alert.AlertType.WARNING, "Warning", "No transaction found for the provided book and user.");
                }
            }
        } catch (SQLException e) {
            // Handle SQL exceptions
            showAlert(Alert.AlertType.ERROR, "Error", "Error in extending: " + e.getMessage());
        }
    }
}
