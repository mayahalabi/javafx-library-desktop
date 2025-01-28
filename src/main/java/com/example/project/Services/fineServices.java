package com.example.project.Services;

import com.example.project.Classes.fines;
import com.example.project.Connection.DBHandler;
import com.example.project.loginOptions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.sql.*;

import static com.example.project.Services.borrowingTransactionServices.checktransactionId;
import static com.example.project.Settings.settingAdminForm.center;
import static com.example.project.Settings.settingUserForm.center1;
import static com.example.project.loginOptions.showAlert;

/**
 * fineServices class handles the fine-related services, including
 * populating the fines table in the UI and interacting with the database.
 */
public class fineServices {

    // TableView and TextField for handling the fines UI
    public static TableView<fines> tableView;
    public static TextField amountField, statusField, transactionidField;

    /**
     * Populates the TableView with fine data from the database.
     *
     * This method retrieves all fines from the `fine` table and adds them to an ObservableList.
     * The ObservableList is then returned and can be bound to a TableView.
     *
     * @return ObservableList of fines to populate the table with fine data.
     */
    public static ObservableList<fines> populateFinesTable() {
        // Initialize the DBHandler to manage database connection
        DBHandler db = new DBHandler();
        Connection conn = db.getConnection();

        // List to store fine records retrieved from the database
        ObservableList<fines> fineList = FXCollections.observableArrayList();

        // SQL query to select all fines from the fine table
        String query = "SELECT * FROM fine";
        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            // Loop through the result set and populate the fineList with fine data
            while (resultSet.next()) {
                int fineid = resultSet.getInt("fine_id");
                double amount = resultSet.getDouble("fine_amount");
                String status = resultSet.getString("fine_status");
                int transactionid = resultSet.getInt("transaction_id");

                // Create a new fine object and add it to the list
                fineList.add(new fines(fineid, amount, status, transactionid));
            }
        } catch (SQLException exe) {
            // Show an error alert in case of a SQLException
            showAlert(Alert.AlertType.ERROR, "Error", "Error loading fine data: " + exe.getMessage());
            exe.printStackTrace();
        } finally {
            // Close the database connection
            db.closeConnection();
        }
        // Return the populated list of fines
        return fineList;
    }

    /**
     * Adds a new fine record to the database.
     *
     * @param amount The fine amount.
     * @param status The fine status (e.g., "paid", "unpaid").
     * @param transactionid The ID of the associated transaction.
     */
    private static void addFine(double amount, String status, int transactionid) {
        // Validate the inputs
        if (amount == 0 || status.isEmpty() || transactionid <= 0) {
            loginOptions.showAlert(Alert.AlertType.ERROR, "Error", "All the Fields are required.");
            return;
        }
        try {
            // Initialize the DBHandler to manage the database connection
            DBHandler db = new DBHandler();
            Connection conn = db.getConnection();

            // Check if the provided transaction ID exists in the database
            if (checktransactionId(transactionid)) {
                // Prepare the SQL query to insert the new fine record
                String query = "INSERT INTO fine (fine_amount, fine_status, transaction_id) VALUES (?, ?, ?)";
                try (PreparedStatement statement = conn.prepareStatement(query)) {
                    // Set the parameters for the prepared statement
                    statement.setDouble(1, amount);
                    statement.setString(2, status);
                    statement.setInt(3, transactionid);

                    // Execute the update and check if the insertion was successful
                    int rowsInserted = statement.executeUpdate();
                    if (rowsInserted > 0) {
                        // Show success message and clear the input fields
                        loginOptions.showAlert(Alert.AlertType.INFORMATION, "Success", "A new fine has been added successfully.");
                        amountField.clear();
                        statusField.clear();
                        transactionidField.clear();

                        // Re-populate the fines table with updated data
                        tableView.setItems(populateFinesTable());
                    }
                } catch (SQLException exe) {
                    // Handle SQL exceptions
                    loginOptions.showAlert(Alert.AlertType.ERROR, "Error", "Error adding new fine: " + exe.getMessage());
                    exe.printStackTrace();
                } finally {
                    // Close the database connection
                    db.closeConnection();
                }
            } else {
                // If the transaction ID is invalid, show an error message
                loginOptions.showAlert(Alert.AlertType.ERROR, "Error", "Invalid transaction id.");
            }
        } catch (Exception e) {
            // Handle unexpected errors
            loginOptions.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding fine.");
            e.printStackTrace();
        }
    }

    /**
     * Deletes the selected fine from the database.
     *
     * This method checks if a fine is selected in the `TableView`. If a fine is selected,
     * it retrieves the fine ID and deletes the record from the `fine` table in the database.
     * After successful deletion, it clears the input fields and refreshes the table.
     */
    public static void deleteFine() {
        // Retrieve the selected fine from the TableView
        fines selectedFine = tableView.getSelectionModel().getSelectedItem();

        // Check if a fine is selected
        if (selectedFine == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a fine to delete.");
            return; // Exit method if no fine is selected
        }

        int thisFineid = selectedFine.getFine_id(); // Get the fine_id of the selected fine

        // Set up the database connection
        DBHandler db = new DBHandler();
        Connection conn = db.getConnection();

        // SQL query to delete the fine
        String query = "DELETE FROM fine WHERE fine_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, thisFineid); // Set the fine ID in the query

            // Execute the query and check if any rows were deleted
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                // If a fine was deleted, show success message and clear fields
                showAlert(Alert.AlertType.INFORMATION, "Success", "Fine deleted successfully.");
                amountField.clear();
                statusField.clear();
                transactionidField.clear();

                // Re-populate the fines table with the updated data
                tableView.setItems(populateFinesTable());
            } else {
                // If no rows were deleted (i.e., the fine ID was not found), show error message
                showAlert(Alert.AlertType.ERROR, "Error", "No fine found with the specified id.");
            }
        } catch (SQLException exe) {
            // If there is an error executing the SQL, show an error message
            showAlert(Alert.AlertType.ERROR, "Error", "Error deleting fine: " + exe.getMessage());
            exe.printStackTrace();
        } finally {
            // Close the database connection
            db.closeConnection();
        }
    }

    /**
     * Creates and sets up a TableView displaying fines information and includes input fields
     * and buttons for adding, updating, and deleting fines.
     *
     * This method configures the following components:
     * - A TableView displaying fines with columns for Fine ID, Fine Amount, Fine Status, and Transaction ID.
     * - Input fields for the user to enter fine details (Amount, Status, Transaction ID).
     * - Buttons to add, update, and delete fines from the TableView.
     *
     * The TableView is populated with data, and listeners are set up to handle user interaction with the rows.
     * Additionally, input fields are populated with selected row data for editing.
     */
    public static void createFinesTable(){
        // Create the TableView for displaying fines
        tableView = new TableView<>();
        tableView.setPrefSize(1040, 800);

        // Create and configure the TableColumns
        TableColumn<fines, String> fineidColumn = new TableColumn<>("Fine Id");
        fineidColumn.setCellValueFactory(new PropertyValueFactory<>("fine_id"));

        TableColumn<fines, String> amountColumn = new TableColumn<>("Fine Amount");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("fine_amount"));

        TableColumn<fines, String> statusColumn = new TableColumn<>("Fine Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("fine_status"));

        TableColumn<fines, String> transactionidColumn = new TableColumn<>("Transaction Id");
        transactionidColumn.setCellValueFactory(new PropertyValueFactory<>("transaction_id"));

        // Set equal width for each column
        double columnWidth = 200; // Desired width for each column
        fineidColumn.setPrefWidth(columnWidth);
        amountColumn.setPrefWidth(columnWidth);
        statusColumn.setPrefWidth(columnWidth);
        transactionidColumn.setPrefWidth(columnWidth);

        // Add columns to the TableView
        tableView.getColumns().addAll(fineidColumn, amountColumn, statusColumn, transactionidColumn);

        // Create labels and text fields for entering fine details
        Label amountLabel = new Label("Amount:");
        amountField = new TextField();
        amountField.setPromptText("Enter amount");

        Label statusLabel = new Label("Status:");
        statusField = new TextField();
        statusField.setPromptText("Enter status");

        Label transactionidLabel = new Label("Transaction ID:");
        transactionidField = new TextField();
        transactionidField.setPromptText("Enter transaction id");

        // Button to add a new fine to the TableView
        Button addButton = new Button("Add Fine");
        addButton.setOnAction(e -> addFine((Double.valueOf(amountField.getText())),
                statusField.getText(), Integer.parseInt(transactionidField.getText())));

        // Button to delete the currently selected fine from the TableView
        Button deleteButton = new Button("Delete Fine");
        deleteButton.setOnAction(e -> deleteFine());

        // Button to update the selected fine with new data from the input fields
        Button updateButton = new Button("Update Fine");
        updateButton.setOnAction(e -> {
            // Retrieve the currently selected item in the event handler
            fines selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                // Update the fine using the selected fine's ID and the new values entered by the user
                updateFine(selectedItem.getFine_id(), Double.valueOf(amountField.getText()), statusField.getText(), Integer.parseInt(transactionidField.getText()));
            } else {
                // If no fine is selected, show an error alert
                showAlert(Alert.AlertType.ERROR, "Error", "Fine not selected.");
            }
        });

        // Create a VBox to organize the input fields and buttons vertically
        VBox inputBox = new VBox(10);
        inputBox.setPadding(new Insets(10));
        inputBox.getChildren().addAll(amountLabel, amountField, statusLabel, statusField, transactionidLabel, transactionidField, addButton, deleteButton, updateButton);

        // Create a BorderPane for layout: input box on the left, TableView in the center
        BorderPane layoutt = new BorderPane();
        layoutt.setLeft(inputBox);
        layoutt.setCenter(tableView);

        // Populate the TableView with data using the populateFinesTable() method
        tableView.setItems(populateFinesTable());

        // Add a listener for row selection to populate input fields with the selected fine's data
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // When a row is selected, update the input fields with the selected fine's data
                amountField.setText(String.valueOf(newValue.getFine_amount()));
                statusField.setText(String.valueOf(newValue.getFine_status()));
                transactionidField.setText(String.valueOf(newValue.getTransaction_id()));
            }
        });

        // Add the layout (input fields and TableView) to the center of the parent container
        center.add(layoutt, 0, 0);
    }

    /**
     * Checks if a fine exists in the database based on the given fine_id.
     *
     * This method executes a SQL query to find a fine by its ID and returns
     * `true` if the fine exists, otherwise it shows an error alert and returns `false`.
     *
     * @param fineid The ID of the fine to be checked.
     * @return true if the fine exists in the database, otherwise false.
     */
    public  static boolean checkfineid(int fineid) {
        // Initialize a DBHandler to interact with the database.
        DBHandler db = new DBHandler();
        try {
            // Establish a connection to the database
            Connection conn = db.getConnection();

            // SQL query to select a fine record based on the fine_id
            String query = "SELECT * FROM fine WHERE fine_id = ?";

            // Use a try-catch-finally block for proper exception handling and connection management.
            try (PreparedStatement statement = conn.prepareStatement(query)) {

                // Set the fine_id parameter in the SQL query
                statement.setInt(1, fineid);

                // Execute the query and get the result set
                ResultSet resultSet = statement.executeQuery();

                // Check if a result is returned (i.e., the fine exists in the database)
                if (resultSet.next()) { // Check if there is at least one result
                    return true; // Fine found
                } else {
                    // fine not found
                    showAlert(Alert.AlertType.ERROR, "Error", "Fine does not exist.");
                }
            }
        } catch (SQLException e) {
            // If an SQLException occurs, print the stack trace for debugging purposes
            e.printStackTrace();
        } finally {
            // Ensure the database connection is closed after the operation is complete
            db.closeConnection();
        }
        return false; // Fine not found
    }

    /**
     * Fetches the fines for a specific user based on the provided username.
     *
     * @param username The username of the user for whom to fetch fines.
     * @return An ObservableList containing all the fines associated with the user.
     */
    public static ObservableList<fines> fetchFinesBySpecificUser(String username) {
        ObservableList<fines> borrowedList = FXCollections.observableArrayList();
        DBHandler db = new DBHandler();
        Connection conn = db.getConnection();
        String query = "SELECT f.fine_id, f.fine_amount, f.fine_status, f.transaction_id FROM fine f NATURAL JOIN user u WHERE u.username = ?";

        // Use try-with-resources to automatically close resources
        try (PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setString(1, username); // Set the username parameter
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int fineId = resultSet.getInt("fine_id");
                double fineamount = resultSet.getDouble("fine_amount");
                String fineStatus = resultSet.getString("fine_status");
                int transactionId = resultSet.getInt("transaction_id");

                // Add the retrieved fine to the list
                borrowedList.add(new fines(fineId, fineamount, fineStatus, transactionId));
            }
        } catch (SQLException e) {
            // Print stack trace (could be replaced with logging or user feedback)
            e.printStackTrace();
        } finally {
            db.closeConnection(); // Ensure connection is closed
        }
        // returns the borrowed lips
        return borrowedList;
    }

    /**
     * Creates a table displaying fines for a specific user.
     * This method sets up the TableView with columns for fine ID, fine amount, fine status,
     * and transaction ID. It fetches the fine data for the given username and displays it.
     *
     * @param username The username for which to fetch and display fines.
     */
    public static void createFineTableForUser(String username) {
        // Create a new TableView to display fine information
        TableView<fines> tableView = new TableView<>();
        tableView.setPrefSize(800, 600);

        // Define the TableColumns for the fine details
        TableColumn<fines, String> idColumn = new TableColumn<>("Fine Id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("fine_id"));

        TableColumn<fines, Integer> amountColumn = new TableColumn<>("Fine Amount");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("fine_amount"));

        TableColumn<fines, String> statusColumn = new TableColumn<>("Fine Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("fine_status"));

        TableColumn<fines, String> transactionColumn = new TableColumn<>("Transaction Id");
        transactionColumn.setCellValueFactory(new PropertyValueFactory<>("transaction_id"));

        // Set widths for the columns
        idColumn.setPrefWidth(150);
        amountColumn.setPrefWidth(150);
        statusColumn.setPrefWidth(150);
        transactionColumn.setPrefWidth(150);

        // Add the columns to the TableView
        tableView.getColumns().addAll(idColumn, amountColumn, statusColumn, transactionColumn);

        // Set the items in the TableView by fetching fines for the specific user
        // This assumes that fetchFinesBySpecificUser(username) returns an ObservableList of fines for the user
        tableView.setItems(fetchFinesBySpecificUser(loginOptions.getUsername()));

        // Create a BorderPane to manage the layout
        BorderPane layoutt = new BorderPane();
        layoutt.setLeft(tableView); // Place the TableView on the left side of the BorderPane

        // Assuming there's a GridPane or Pane called center1 to add the layout
        center1.add(layoutt, 0, 0); // Add the BorderPane layout to the parent container
    }

    /**
     * Updates the details of a fine record in the database.
     * This method checks whether the specified fine exists and ensures that the new values
     * are different from the existing ones before updating the fine details.
     *
     * @param fineId The ID of the fine to be updated.
     * @param newAmount The new fine amount to set.
     * @param newStatus The new status of the fine (e.g., 'Paid', 'Unpaid').
     * @param transactionid The new transaction ID associated with the fine.
     */
    public static void updateFine(int fineId, double newAmount, String newStatus, int transactionid) {
        DBHandler db = new DBHandler();
        Connection conn = db.getConnection();

        // Query to check if the fine exists with the provided fineId
        String checkQuery = "SELECT fine_amount, fine_status, transaction_id FROM fine WHERE fine_id = ?";

        // SQL query to update the fine details
        String updateQuery = "UPDATE fine SET fine_amount = ?, fine_status = ?, transaction_id = ? WHERE fine_id = ?";

        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            // Set the fineId parameter in the SQL query
            checkStmt.setInt(1, fineId);

            // Execute the check query and retrieve the existing fine details
            ResultSet rs = checkStmt.executeQuery();

            // Check if the new values are the same as the existing ones
            if (rs.next()) {
                // Get the current values of the fine details from the result set
                double currentAmount = rs.getDouble("fine_amount");
                String currentStatus = rs.getString("fine_status");
                int transactionId = rs.getInt("transaction_id");

                // Check if the new values are the same as the existing ones
                if (newAmount == currentAmount && newStatus.equals(currentStatus) && transactionId == transactionid) {
                    // If no change, show a warning to the user
                    showAlert(Alert.AlertType.WARNING, "Warning", "Change something to be updated.");
                    return;
                }
            } else {
                // If no fine with the given ID exists, show an error message
                showAlert(Alert.AlertType.ERROR, "Error", "No fine found with the provided ID.");
                return;
            }

            // Proceed with the update if the fine exists and values are different
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                // Set the parameters for the update query
                updateStmt.setDouble(1, newAmount);
                updateStmt.setString(2, newStatus);
                updateStmt.setInt(3, transactionid);
                updateStmt.setInt(4, fineId);

                // Check if any rows were updated
                int rowsUpdated = updateStmt.executeUpdate();
                if (rowsUpdated > 0) {
                    // If successful, show a success message and refresh the table
                    showAlert(Alert.AlertType.INFORMATION, "Info", "Fine updated successfully.");
                    // Update the fine table view if needed
                    tableView.setItems(populateFinesTable()); // Assuming method to repopulate the table with updated fines data
                } else {
                    // If no rows were updated, display an error
                    showAlert(Alert.AlertType.ERROR, "Error", "No fine found with the provided ID.");
                }
            }
        } catch (SQLException e) {
            // If there is an SQL error, show an error message
            showAlert(Alert.AlertType.ERROR, "Error", "Error updating fine: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Ensure the database connection is closed after the operation
            db.closeConnection();
        }
    }
}
