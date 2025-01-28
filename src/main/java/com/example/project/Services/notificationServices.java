package com.example.project.Services;

import com.example.project.Classes.borrowing_transaction;
import com.example.project.Classes.notification;
import com.example.project.Connection.DBHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.example.project.Services.bookServices.checkbookid;
import static com.example.project.Services.borrowingTransactionServices.getTransactionId;
import static com.example.project.Services.userServices.checkUsername;
import static com.example.project.Settings.settingAdminForm.center;
import static com.example.project.loginOptions.showAlert;

/**
 * A service class for handling notifications related to reminders for users,
 * including populating the notification table with data from the database.
 */
public class notificationServices {
    // UI elements for the notification interface
    public static TableView<notification> tableView;
    public static TextField reminderdateField, usernameField, bookidField, fineidField;

    /**
     * Populates the notification table with data fetched from the database.
     *
     * This method retrieves notification data, including notification ID, reminder date, username,
     * book ID, and fine ID from the `notification` table in the database. It then converts the
     * results into a list of `notification` objects and returns it.
     *
     * @return An observable list of notifications to be displayed in the TableView.
     */
    public static ObservableList<notification> populateNotificationTable() {
        // Initialize DBHandler to manage database operations
        DBHandler db = new DBHandler();

        // Get database connection
        Connection conn = db.getConnection();

        // List to store notifications
        ObservableList<notification> notificationList = FXCollections.observableArrayList();

        // SQL query to fetch notification data
        String query = "SELECT notification_id, reminder_date, username, book_id, fine_id FROM notification";

        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            // Process each result row and create a notification object
            while (resultSet.next()) {
                int id = resultSet.getInt("notification_id");
                Timestamp reminderDate = resultSet.getTimestamp("reminder_date");
                String username = resultSet.getString("username");
                int bookId = resultSet.getInt("book_id");
                int fineId = resultSet.getInt("fine_id");

                // Add the created notification object to the list
                notificationList.add(new notification(id, reminderDate, username, bookId, fineId));
            }
        } catch (SQLException exe) {
            // If there is an error while fetching data, show an alert with the error message
            showAlert(Alert.AlertType.ERROR, "Error", "Error loading notification data: " + exe.getMessage());
            exe.printStackTrace();
        } finally {
            // Ensure the database connection is closed after the operation
            db.closeConnection();
        }
        // Return the populated list of notifications
        return notificationList;
    }

    /**
     * Adds a new notification to the database with the provided reminder date, username, book ID, and fine ID.
     *
     * The method first validates the input fields and checks whether the username and book ID are valid.
     * If the inputs are valid, it inserts the notification into the `notification` table in the database.
     * After insertion, it updates the table view to reflect the new notification and clears the input fields.
     *
     * @param reminderdate The reminder date for the notification.
     * @param username The username associated with the notification.
     * @param bookid The book ID related to the notification.
     * @param fineid The fine ID related to the notification (can be null).
     */
    public static void addNotification(Timestamp reminderdate, String username, int bookid, Integer fineid) {
        // Validate inputs to ensure that no required fields are missing
        if (reminderdate == null || username.isEmpty() || bookid <= 0) {
            // If validation fails, show an error alert and return early
            showAlert(Alert.AlertType.ERROR, "Error", "All fields are required.");
            return;
        }

        DBHandler db = null;

        try {
            // Initialize the DBHandler and establish the database connection
            db = new DBHandler();
            Connection conn = db.getConnection();

            // Check if the username and book ID are valid (exist in the system)
            if (checkUsername(username) && checkbookid(bookid)) {
                // Prepare the SQL query to insert the new notification into the database
                String query = "INSERT INTO notification (reminder_date, username, book_id, fine_id) VALUES (?, ?, ?, ?)";
                try (PreparedStatement statement = conn.prepareStatement(query)) {
                    // Set the parameters for the SQL query
                    statement.setTimestamp(1, reminderdate);
                    statement.setString(2, username);
                    statement.setInt(3, bookid);
                    statement.setInt(4, fineid);

                    // Check if the fine ID is valid and set it accordingly; otherwise, set it to NULL
                    if (fineid == null || fineid <= 0) {
                        statement.setNull(4, Types.INTEGER); // Set fine_id as NULL if not valid
                    } else {
                        statement.setInt(4, fineid);
                    }

                    // Set a default message for the notification
                    statement.setString(5, "Don't forget to return the book!"); // Set default message here

                    // Execute the insert statement and check if any rows were inserted
                    int rowsInserted = statement.executeUpdate();
                    if (rowsInserted > 0) {
                        // Show success alert and update the UI elements upon successful insertion
                        showAlert(Alert.AlertType.INFORMATION, "Success", "A new notification has been added successfully.");

                        // Clear the input fields
                        reminderdateField.clear();
                        usernameField.clear();
                        bookidField.clear();
                        fineidField.clear();

                        // Refresh the TableView with the latest data from the database
                        tableView.setItems(populateNotificationTable());
                    }
                } catch (SQLException exe) {
                    // Show an error alert if the SQL query fails
                    showAlert(Alert.AlertType.ERROR, "Error", "Error adding new notification: " + exe.getMessage());
                    exe.printStackTrace();
                }
            } else {
                // Show error if the username or book ID is invalid
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid username or book ID.");
            }
        } catch (Exception e) {
            // Catch any general exceptions and show an error alert
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding notification.");
            e.printStackTrace();
        } finally {
            // Ensure that the database connection is closed, even if an exception occurs
            if (db != null) {
                db.closeConnection(); // Ensure the connection is closed
            }
        }
    }

    /**
     * Deletes the selected notification from the database.
     *
     * This method checks if a notification is selected from the table. If a notification is selected,
     * it deletes the corresponding notification record from the `notification` table in the database
     * using the `notification_id`. After deletion, it updates the TableView and clears the input fields.
     *
     * @param notificationid The ID of the notification to be deleted. This is provided through selection
     *                       in the TableView.
     */
    public static void deleteNotification(int notificationid) {
        // Get the currently selected notification from the TableView
        notification selectedNotification = tableView.getSelectionModel().getSelectedItem();

        // If no notification is selected, show an error alert and return
        if (selectedNotification == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a notification to delete.");
            return;
        }

        // Get the notification ID of the selected notification
        int thisNotificationid = selectedNotification.getNotificationId();

        DBHandler db = new DBHandler();
        Connection conn = db.getConnection();

        // SQL query to delete the notification with the given notification ID
        String query = "DELETE FROM notification WHERE notification_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            // Set the notification ID as a parameter for the delete query
            statement.setInt(1, thisNotificationid);

            // Execute the delete query and check how many rows were deleted
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                // If a notification was deleted, show a success alert
                showAlert(Alert.AlertType.INFORMATION, "Success", "Notification deleted successfully.");

                // Clear the input fields after successful deletion
                reminderdateField.clear();
                usernameField.clear();
                bookidField.clear();
                fineidField.clear();

                // Refresh the TableView to show the updated list of notifications
                tableView.setItems(populateNotificationTable());
            } else {
                // If no notification with the given ID was found, show an error alert
                showAlert(Alert.AlertType.ERROR, "Error", "No notification found with the specified id.");
            }
        } catch (SQLException exe) {
            // Handle any SQL exceptions that occur during the delete operation
            showAlert(Alert.AlertType.ERROR, "Error", "Error deleting notification: " + exe.getMessage());
            exe.printStackTrace();
        } finally {
            // Ensure the database connection is closed after the operation
            db.closeConnection();
        }
    }

    /**
     * Creates and sets up the notification table and the related UI components.
     *
     * This method initializes a `TableView` to display notifications. It also creates labels, text fields,
     * and buttons to handle adding, deleting, and updating notifications. The layout uses a `BorderPane`
     * where the `TableView` is placed at the center, and the input fields and buttons are placed on the left.
     * Additionally, the table is populated with data from the database and includes row selection functionality.
     *
     * The following actions are supported:
     * - Adding a new notification to the database.
     * - Deleting a selected notification from the database.
     * - Updating the details of a selected notification in the database.
     */
    public static void createNotificationTable(){
        // Create the TableView and its columns to display notification data
        tableView = new TableView<>();
        tableView.setPrefSize(1040, 800);

        // Define and configure each column of the table
        TableColumn<notification, String> notificationidColumn = new TableColumn<>("Notification Id");
        notificationidColumn.setCellValueFactory(new PropertyValueFactory<>("notificationId"));

        TableColumn<notification, String> reminderColumn = new TableColumn<>("Reminder Date");
        reminderColumn.setCellValueFactory(new PropertyValueFactory<>("reminderDate"));

        TableColumn<notification, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<notification, String> bookidColumn = new TableColumn<>("Book Id");
        bookidColumn.setCellValueFactory(new PropertyValueFactory<>("book_id"));

        TableColumn<notification, String> fineidColumn = new TableColumn<>("Fine Id");
        fineidColumn.setCellValueFactory(new PropertyValueFactory<>("fine_id"));


        // Set equal width for each column
        double columnWidth = 200; // Desired width for each column
        notificationidColumn.setPrefWidth(columnWidth);
        reminderColumn.setPrefWidth(columnWidth);
        usernameColumn.setPrefWidth(columnWidth);
        bookidColumn.setPrefWidth(columnWidth);
        fineidColumn.setPrefWidth(columnWidth);

        // Add columns to the TableView
        tableView.getColumns().addAll(notificationidColumn, reminderColumn, usernameColumn, bookidColumn, fineidColumn);

        // Create input fields and labels for notification properties
        Label reminderLabel = new Label("ReminderDate:");
        reminderdateField = new TextField();
        reminderdateField.setPromptText("Enter reminder date");

        Label usernameLabel = new Label("Username:");
        usernameField = new TextField();
        usernameField.setPromptText("Enter username");

        Label bookidLabel = new Label("Book Id:");
        bookidField = new TextField();
        bookidField.setPromptText("Enter book id");

        Label fineidLabel = new Label("Fine Id:");
        fineidField = new TextField();
        fineidField.setPromptText("Enter fine id");

        // Create buttons for adding, deleting, and updating notifications

        // Button to add notification
        Button addButton = new Button("Add Notification");
        addButton.setOnAction(e -> addNotification(Timestamp.valueOf(reminderdateField.getText().trim()), usernameField.getText(),
                Integer.parseInt(bookidField.getText()), Integer.parseInt(fineidField.getText())));

        //Button to delete a notification
        Button deleteButton = new Button("Delete Notification");
        deleteButton.setOnAction(e -> deleteNotification(getNotificationId(usernameField.getText(), Integer.parseInt(bookidField.getText()))));

        //Button to update a notification
        Button updateButton = new Button("Update Notification");
        updateButton.setOnAction(e -> {
            // Retrieve the currently selected item in the event handler
            notification selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                // If a notification is selected, update it with the new details
                updateNotification(selectedItem.getNotificationId(), Timestamp.valueOf(reminderdateField.getText()), usernameField.getText(), Integer.parseInt(bookidField.getText()), Integer.parseInt(fineidField.getText()));
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Notification not selected.");
            }
        });

        // Create a VBox to hold the input fields and buttons
        VBox inputBox = new VBox(10);
        inputBox.setPadding(new Insets(10)); // Padding around the VBox
        inputBox.getChildren().addAll(reminderLabel, reminderdateField, usernameLabel, usernameField,
                bookidLabel, bookidField, fineidLabel, fineidField, addButton, deleteButton, updateButton);

        // Create a BorderPane layout
        BorderPane layoutt = new BorderPane();
        layoutt.setLeft(inputBox); // Place the VBox with input fields and buttons on the left
        layoutt.setCenter(tableView); // Place the TableView in the center

        // Populate the TableView with data from the database
        tableView.setItems(populateNotificationTable());

        // Add a listener to the TableView for row selection (to update input fields)
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // When a row is selected, populate the input fields with the selected notification's details
                reminderdateField.setText(newValue.getReminderDate());
                usernameField.setText(newValue.getUsername());
                bookidField.setText(String.valueOf(newValue.getBook_id()));
                fineidField.setText(String.valueOf(newValue.getFine_id()));
            }
        });

        // Add the layout to the center of the UI container
        center.add(layoutt, 0, 0);
    }

    /**
     * Checks if a notification ID exists in the database.
     *
     * This method queries the database to check if a notification with the given notification ID exists in the `notification` table.
     * It returns `true` if a notification with the specified ID is found, and `false` otherwise. If the notification ID is not found,
     * an error alert is displayed.
     *
     * @param notificationid The ID of the notification to check.
     * @return boolean Returns `true` if the notification ID exists, otherwise `false`.
     */
    public static boolean checknotificationid(int notificationid) {
        // Create a new DBHandler instance for database connection
        DBHandler db = new DBHandler();

        try {
            Connection conn = db.getConnection(); // Establish connection to the database

            // SQL query to check if the notification ID exists in the database
            String query = "SELECT * FROM notification WHERE notification_id = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, notificationid); // Set the provided notification ID to the query

                // Use executeQuery for SELECT statements
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) { // Check if there is at least one result
                    return true; // Notification found
                } else {
                    // notification not found
                    showAlert(Alert.AlertType.ERROR, "Error", "Notification ID does not exist.");
                }
            } catch (SQLException e) {
                // Print stack trace if there is an SQL exception
                e.printStackTrace();
            }
        } catch (Exception e) {
            // Catch any other exceptions and print stack trace
            e.printStackTrace();
        } finally {
            // Ensure that the database connection is closed
            db.closeConnection();
        }
        // Return false if no notification with the specified ID is found
        return false;
    }

    /**
     * Adds a fine for an overdue book based on the user's transaction.
     *
     * This method checks if a fine already exists for a specific book borrowed by a user.
     * If a fine does not exist, it inserts a new fine record into the database with a default fine amount and status.
     * It also handles cases where a fine already exists, showing an appropriate error message.
     *
     * @param username The username of the user who borrowed the book.
     * @param bookId The ID of the overdue book for which the fine is being added.
     */
    public static void addFineForOverdueBook(String username, int bookId) {
        // Create a new DBHandler instance for database connection
        DBHandler db = new DBHandler();

        // Establish connection to the database
        Connection conn = db.getConnection();

        try {

            // Check if a fine already exists for this transaction
            String checkQuery = "SELECT 1 FROM fine WHERE transaction_id = ?";
            try (PreparedStatement checkStatement = conn.prepareStatement(checkQuery)) {
                checkStatement.setInt(1, getTransactionId(username, bookId));
                ResultSet resultSet = checkStatement.executeQuery();

                // If a result is found, a fine already exists
                if (resultSet.next()) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Fine already added.");
                    return;
                }
            }

            // If no fine exists, insert a new fine
            String insertQuery = "INSERT INTO fine (fine_amount, fine_status, transaction_id) VALUES (?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(insertQuery)) {
                statement.setDouble(1, 50.0); // Example fine amount
                statement.setString(2, "Unpaid");
                statement.setInt(3, getTransactionId(username, bookId));
                int rowInserted = statement.executeUpdate();
                if (rowInserted > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Fine added successfully.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Fine addition failed.");
                }
            }
        } catch (SQLException e) {
            // Show error alert for SQL exceptions
            showAlert(Alert.AlertType.ERROR, "Error", "Error adding a fine.");
            e.printStackTrace();
        } finally {
            // close the database connection
            db.closeConnection();
        }
    }

    /**
     * Checks the reminder status for a borrowed book and performs actions based on its due date.
     *
     * This method performs the following checks for the given book and user:
     * 1. It retrieves the due date for the book from the borrowing transaction.
     * 2. If the book is overdue, it adds a fine for the user.
     * 3. If the book is due soon (within 3 days), it sends a reminder to the user.
     * 4. If the book is not due soon, it informs the user that the book is not due within 3 days.
     * 5. If no borrowing transaction is found for the given book and user, it shows an error alert.
     *
     * @param bookId The ID of the borrowed book.
     * @param username The username of the person who borrowed the book.
     */
    public static void checkReminder(int bookId, String username) {
        Timestamp dueDate = null;
        DBHandler db = new DBHandler();
        Connection conn = db.getConnection();
        String query = "SELECT due_date FROM borrowing_transaction WHERE book_id = ? AND username = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            // Set the bookId and username parameters for the query
            statement.setInt(1, bookId);
            statement.setString(2, username);

            // Execute the query to retrieve the due date
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Retrieve the due date from the result set
                dueDate = resultSet.getTimestamp("due_date");

                // Check if the book has been returned
                if (checkReturn(conn, bookId, username)) {
                    // Book has already been returned, no further action required
                }
                // Check if the book is overdue
                else if (isOverdue(dueDate)) {
                    addFineForOverdueBook(username, bookId);
                }
                // Check if the book is due within the next 3 days
                else if (isDueSoon(dueDate)) {
                    showAlert(Alert.AlertType.INFORMATION, "Reminder", "Hi " + username + ", please return the book with ID " + bookId + " within 3 days.");
                }
                // If the book is not due within 3 days
                else {
                    showAlert(Alert.AlertType.INFORMATION, "All Good", "It's not due within 3 days.");
                }
            }
            // If no borrowing transaction exists for this book and user
            else {
                showAlert(Alert.AlertType.ERROR, "Error", "No borrowing transaction found for this book and user.");
            }
        } catch (SQLException e) {
            // Handle any SQL exceptions that may occur during the process
            showAlert(Alert.AlertType.ERROR, "Error", "Error retrieving due date.");
            e.printStackTrace();
        } finally {
            // Ensure the database connection is closed after operations
            db.closeConnection();
        }
    }

    /**
     * Checks if a given due date is due within the next 3 days.
     *
     * This method compares the current date and time with the provided `dueDate` to determine
     * if the due date is within the next 3 days. If the `dueDate` is within the next 3 days
     * (inclusive of the current day), the method returns `true`. Otherwise, it returns `false`.
     *
     * @param dueDate The due date to be checked, provided as a `Timestamp`.
     * @return A boolean indicating whether the due date is due within the next 3 days
     *         (`true`) or not (`false`).
     */
    public static boolean isDueSoon(Timestamp dueDate) {
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();

        // Convert the due date from Timestamp to LocalDateTime for comparison
        LocalDateTime dueDateTime = dueDate.toLocalDateTime();

        // Calculate the number of days between the current date and the due date
        long daysBetween = ChronoUnit.DAYS.between(now, dueDateTime);

        // Return true if the due date is between today and 3 days from today
        return daysBetween >= 0 && daysBetween <= 3;
    }

    /**
     * Checks if a given due date is overdue based on the current date and time.
     *
     * This method compares the current date and time with the provided `dueDate`.
     * If the `dueDate` is before the current date and time, the method considers
     * it overdue and returns `true`. If the `dueDate` is on or after the current
     * date and time, the method returns `false`, indicating that the due date has
     * not passed yet.
     *
     * @param dueDate The due date to be checked, provided as a `Timestamp`.
     * @return A boolean indicating whether the due date is overdue (`true`) or not (`false`).
     */
    public static boolean isOverdue(Timestamp dueDate) {
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();

        // Convert the due date from Timestamp to LocalDateTime for comparison
        LocalDateTime dueDateTime = dueDate.toLocalDateTime();

        // Return true if the due date is before the current date and time (overdue)
        return dueDateTime.isBefore(now);
    }

    /**
     * Checks if a book has been returned by a user based on the transaction ID.
     *
     * This method queries the `borrowing_transaction` table to check if the `return_date` for a specific book
     * transaction (identified by `bookId` and `username`) is already set. If the `return_date` is not null,
     * it indicates that the book has been returned, and no fine is required. In such a case, an informational alert
     * is shown to notify the user. If the book hasn't been returned, the method returns false, indicating that
     * a fine may be applicable.
     *
     * @param conn The active database connection used to query the `borrowing_transaction` table.
     * @param bookId The ID of the book to check for return status.
     * @param username The username of the user who borrowed the book.
     * @return A boolean value indicating whether the book has been returned (true) or not (false).
     * @throws SQLException If there is an error executing the SQL query.
     */
    public static boolean checkReturn(Connection conn, int bookId, String username) throws SQLException {
        // Query to check the return date for a specific borrowing transaction
        String checkReturnDateQuery = "SELECT return_date FROM borrowing_transaction WHERE transaction_id = ?";

        try (PreparedStatement checkReturnStatement = conn.prepareStatement(checkReturnDateQuery)) {
            // Get the transaction ID based on the book ID and username
            int transactionId = getTransactionId(username, bookId); // get the id of the transaction
            checkReturnStatement.setInt(1, transactionId); // Set the transaction ID in the prepared statement

            // Execute the query and retrieve the result
            ResultSet resultSet = checkReturnStatement.executeQuery();

            // If there is a return date, it means the book has been returned
            if (resultSet.next()) {
                Timestamp returnDate = resultSet.getTimestamp("return_date");
                if (returnDate != null) {
                    // Book has been returned, show an information alert
                    showAlert(Alert.AlertType.INFORMATION, "Information", "Book has been returned. No fine is required.");
                    return true; // Book has been returned
                }
            }
        } catch (SQLException e) {
            // Rethrow the exception as a runtime exception with a custom error message
            throw new RuntimeException("Error checking return status", e);
        }
        // Book has not been returned (either no return date or no transaction found)
        return false;
    }

    /**
     * Creates and displays a TableView for borrowed books, allowing users to view
     * details of borrowing transactions and set reminders for due dates.
     */
    public static void createBorrowedBooksTable() {
        // Create a new TableView for displaying borrowing transactions
        TableView<borrowing_transaction> tableView = new TableView<>();
        tableView.setPrefSize(800, 600); // Set preferred size for the table

        // Define the columns for the TableView
        TableColumn<borrowing_transaction, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username")); // Bind to username property

        TableColumn<borrowing_transaction, Integer> bookIdColumn = new TableColumn<>("Book ID");
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("book_id")); // Bind to book_id property

        TableColumn<borrowing_transaction, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title")); // Bind to title property

        TableColumn<borrowing_transaction, Timestamp> dueDateColumn = new TableColumn<>("Due Date");
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("due_date")); // Bind to due_date property

        // Set preferred widths for the columns to improve layout
        usernameColumn.setPrefWidth(150);
        bookIdColumn.setPrefWidth(100);
        titleColumn.setPrefWidth(300);
        dueDateColumn.setPrefWidth(200);

        // Add columns to the TableView
        tableView.getColumns().addAll(usernameColumn, bookIdColumn, titleColumn, dueDateColumn);

        // Create a button to set reminders for selected borrowed books
        Button reminderButton = new Button("Set Reminder");
        reminderButton.setOnAction(e -> {
            borrowing_transaction selectedBook = tableView.getSelectionModel().getSelectedItem(); // Get the selected transaction
            if (selectedBook != null) {
                checkReminder(selectedBook.getBook_id(), selectedBook.getUsername()); // Call method to check reminder
            } else {
                // Show an error alert if no book is selected
                showAlert(Alert.AlertType.ERROR, "Error", "Please select a book to set a reminder.");
            }
        });

        // Create a layout to hold the TableView and button
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10)); // Set padding around the layout
        layout.getChildren().addAll(tableView, reminderButton); // Add TableView and button to the layout

        center.add(layout, 0, 0); // Add the layout to the main center region of the application
    }

    /**
     * Retrieves the notification ID associated with a specific username and book ID.
     *
     * @param username the username associated with the notification.
     * @param bookId the ID of the book related to the notification.
     * @return the notification ID if found, or -1 if not found or an error occurs.
     */
    public static int getNotificationId(String username, int bookId) {
        DBHandler db = new DBHandler(); // Create a new DBHandler instance
        try {
            Connection conn = db.getConnection(); // Establish a database connection

            // SQL query to select the notification ID based on username and book ID
            String query = "SELECT notification_id FROM notification WHERE username = ? AND book_id = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, username); // Set the username parameter
                statement.setInt(2, bookId); // Set the book ID parameter
                ResultSet resultSet = statement.executeQuery(); // Execute the query

                // Check if a matching notification is found
                if (resultSet.next()) {
                    return resultSet.getInt("notification_id"); // Return the ID if found
                } else {
                    return -1; // Return -1 if no matching notification found
                }
            } catch (SQLException e) {
                // Handle SQL exceptions and notify the user
                showAlert(Alert.AlertType.ERROR, "Error", "Error fetching notification: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (Exception e) {
            // Handle general exceptions and notify the user
            showAlert(Alert.AlertType.ERROR, "Error", "Error fetching notification.");
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return -1; // Default return value if an error occurs
    }

    /**
     * Updates an existing notification in the database with new values.
     *
     * @param notificationId the ID of the notification to be updated.
     * @param reminderDate the new reminder date for the notification.
     * @param username the new username associated with the notification.
     * @param bookId the new ID of the book related to the notification.
     * @param fineid the new ID of the fine, if applicable.
     */
    public static void updateNotification(int notificationId, Timestamp reminderDate, String username, int bookId, int fineid) {
        DBHandler db = new DBHandler(); // Create a new DBHandler instance
        Connection conn = db.getConnection(); // Establish a database connection

        // SQL queries to check current values and update the notification
        String checkQuery = "SELECT reminder_date, username, book_id, fine_id FROM notification WHERE notification_id = ?";
        String updateQuery = "UPDATE notification SET reminder_date = ?, username = ?, book_id = ?, fine_id = ? WHERE notification_id = ?";

        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, notificationId); // Set the notification ID for the check query
            ResultSet rs = checkStmt.executeQuery(); // Execute the check query

            // If a notification with the provided ID exists, proceed
            if (rs.next()) {
                // Retrieve current values from the database
                Timestamp currentReminderDate = rs.getTimestamp("reminder_date");
                String currentUsername = rs.getString("username");
                int currentBookId = rs.getInt("book_id");
                int currentFineid = rs.getInt("fine_id");

                // Check if all new values are the same as the current values
                if (reminderDate.equals(currentReminderDate) &&
                        username.equals(currentUsername) &&
                        bookId == currentBookId && fineid == currentFineid) {
                    showAlert(Alert.AlertType.WARNING, "Warning", "Change something to be updated.");
                    return; // Exit if no changes are detected
                }
            } else {
                // If no notification is found, alert the user
                showAlert(Alert.AlertType.ERROR, "Error", "No notification found with the provided ID.");
                return;
            }

            // Proceed with the update if changes are detected
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                // Set new values for the update query
                updateStmt.setTimestamp(1, reminderDate);
                updateStmt.setString(2, username);
                updateStmt.setInt(3, bookId);
                updateStmt.setInt(4, fineid);
                updateStmt.setInt(5, notificationId); // Set the notification ID for the update

                int rowsUpdated = updateStmt.executeUpdate(); // Execute the update query
                if (rowsUpdated > 0) {
                    // Notify the user of a successful update
                    showAlert(Alert.AlertType.INFORMATION, "Info", "Notification updated successfully.");
                    // Update the notification table view to reflect changes
                    tableView.setItems(populateNotificationTable()); // Ensure you have a method to repopulate the table
                } else {
                    // If no rows were updated, notify the user
                    showAlert(Alert.AlertType.ERROR, "Error", "No notification found with the provided ID.");
                }
            }
        } catch (SQLException e) {
            // Handle SQL exceptions and show an error alert
            showAlert(Alert.AlertType.ERROR, "Error", "Error updating notification: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.closeConnection(); // Ensure the database connection is closed
        }
    }

    /**
     * Deletes notifications from the notification table where the given book ID matches.
     *
     * This method removes all notifications associated with the specified book ID from the database.
     * It prepares a SQL DELETE query and executes it to delete the relevant records.
     * If the operation is successful, a message is logged indicating the deleted book ID.
     * If an error occurs during the operation, an error alert is shown and the exception details are printed.
     *
     * @param conn The active database connection that will be used to execute the delete query.
     * @param bookid The ID of the book for which the notifications are to be deleted.
     */
    public static void deleteNotificationWBOOKID(Connection conn, int bookid) {

        // SQL query to delete notifications where the book_id matches the provided bookid
        String query = "DELETE FROM notification WHERE book_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, bookid); // Set the bookid parameter for the query

            // Execute the delete operation
            statement.executeUpdate(); // Perform the deletion of records
            System.out.println("Deleted notifications for book ID: " + bookid);

        } catch (SQLException exe) {
            // If an error occurs during the SQL operation, show an alert and print the error details
            showAlert(Alert.AlertType.ERROR, "Error", "Error deleting notifications: " + exe.getMessage());
            exe.printStackTrace();
        }
    }
}