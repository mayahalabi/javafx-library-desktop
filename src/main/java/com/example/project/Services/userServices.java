package com.example.project.Services;

import com.example.project.Connection.DBHandler;
import com.example.project.Classes.user;
import com.example.project.Settings.settingUserForm;
import com.example.project.loginOptions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

import static com.example.project.Settings.settingAdminForm.center;
import static com.example.project.loginOptions.showAlert;

public class userServices {

    // TableView to display users
    private static TableView<user> usertableView;

    // TextFields for user input
    public static TextField usernameField;
    public static TextField fNameField;
    public static TextField lNameField;
    public static TextField phoneField;
    public static TextField emailField;
    public static TextField passwordField;
    public static TextField addressField;
    public static TextField registerField;
    public static ComboBox<String> roleBox;

    /**
     * Method to create the user table and input fields for creating/updating users.
     */
    public static void createUserTable(){

        // Create the TableView and its columns
        usertableView = new TableView<>();

        // Create columns for each user field
        TableColumn<user, String> userNameColumn = new TableColumn<>("Username");
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<user, String> fNameColumn = new TableColumn<>("First Name");
        fNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<user, String> lNameColumn = new TableColumn<>("Last Name");
        lNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<user, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<user, String> phoneColumn = new TableColumn<>("Phone Number");
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        TableColumn<user, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<user, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        TableColumn<user, String> registrDate = new TableColumn<>("Register Date");
        registrDate.setCellValueFactory(new PropertyValueFactory<>("registration_date"));

        TableColumn<user, String> passColumn = new TableColumn<>("Password");
        passColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        // Set equal width for each column
        double columnWidth = 116; // Desired width for each column
        userNameColumn.setPrefWidth(columnWidth);
        fNameColumn.setPrefWidth(columnWidth);
        lNameColumn.setPrefWidth(columnWidth);
        emailColumn.setPrefWidth(columnWidth);
        phoneColumn.setPrefWidth(columnWidth);
        addressColumn.setPrefWidth(columnWidth);
        roleColumn.setPrefWidth(columnWidth);
        registrDate.setPrefWidth(columnWidth);
        passColumn.setPrefWidth(columnWidth);

        // Add the columns to the TableView
        usertableView.getColumns().addAll(userNameColumn, fNameColumn, lNameColumn, emailColumn, phoneColumn, addressColumn, roleColumn, registrDate, passColumn);

        // Create input fields and labels for adding/updating users
        Label usernameLabel = new Label("Username");
        usernameField = new TextField();
        usernameField.setPromptText("Enter username");

        Label fNameLabel = new Label("First Name:");
        fNameField = new TextField();
        fNameField.setPromptText("Enter first name");

        Label lNameLabel = new Label("Last Name:");
        lNameField = new TextField();
        lNameField.setPromptText("Enter last name");

        Label emailLabel = new Label("Email:");
        emailField = new TextField();
        emailField.setPromptText("Enter email");

        Label phoneLabel = new Label("Phone Number:");
        phoneField = new TextField();
        phoneField.setPromptText("Enter phone number");

        Label addressLabel = new Label("Address:");
        addressField = new TextField();
        addressField.setPromptText("Enter address");

        Label roleLabel = new Label("Role:");
        roleBox = new ComboBox<>();
        roleBox.getItems().addAll("user", "admin");
        roleBox.setValue(null); // Set default value

        Label passLabel = new Label("Password:");
        passwordField = new TextField();
        passwordField.setPromptText("Enter password");

        // Button to add a new user
        Button addButton = new Button("Add User");
        addButton.setOnAction(e -> addUser(usernameField.getText(), fNameField.getText(), lNameField.getText(), emailField.getText(), phoneField.getText(), addressField.getText(), roleBox, passwordField.getText()));

        // Button to delete a user
        Button deleteButton = new Button("Delete User");
        deleteButton.setOnAction(e -> deleteUser());

        // Button to update a user
        Button updateButton = new Button("Update User");
        updateButton.setOnAction(e -> {
            // Retrieve the currently selected item in the event handler
            user selectedItem = usertableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                updateUser2(selectedItem.getUsername(), usernameField.getText(), fNameField.getText(),
                        lNameField.getText(), emailField.getText(), phoneField.getText(), addressField.getText(),
                        passwordField.getText(), roleBox);
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "User not selected.");
            }
        });

        // Layout for buttons (Add, Delete)
        HBox hbox = new HBox(10, addButton, deleteButton);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPadding(new Insets(5));

        // Create a VBox to hold the input fields and buttons
        VBox inputBox = new VBox(10);
        inputBox.setPadding(new Insets(10));
        inputBox.getChildren().addAll(usernameLabel, usernameField, fNameLabel, fNameField, lNameLabel, lNameField, emailLabel, emailField, phoneLabel, phoneField, addressLabel, addressField, roleLabel, roleBox, passLabel, passwordField, hbox, updateButton);

        // Create a BorderPane and set the VBox to the left and the TableView to the center
        BorderPane layoutt = new BorderPane();
        layoutt.setLeft(inputBox);
        layoutt.setCenter(usertableView);


        // Populate the TableView with data
        usertableView.setItems(populateUserTable());

        // Add listener to the TableView for row selection
        usertableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                usernameField.setText(newValue.getUsername());
                fNameField.setText(newValue.getFirstName());
                lNameField.setText(newValue.getLastName());
                emailField.setText(newValue.getEmail());
                passwordField.setText(newValue.getPassword());
                phoneField.setText(newValue.getPhoneNumber());
                addressField.setText(newValue.getAddress());
                roleBox.setValue(newValue.getRole());
            }
        });

        // Add layout to center of the main layout
        center.add(layoutt, 0, 0);
    }

    /**
     * Method to add a user to the database.
     */
    private static void addUser(String thisusername, String thisfirstName, String thislastName, String thisemail, String thisPhoneNumber,
                                String thisaddress,
                                ComboBox<String> roleBox, String thispassword) {
        // Check if required fields are empty
        if (thisusername.isEmpty() || thisfirstName.isEmpty() || thislastName.isEmpty() || thisemail.isEmpty() || roleBox == null || thispassword.isEmpty()) {
            loginOptions.showAlert(Alert.AlertType.ERROR, "Error", "Check if you entered those fields:\nUsername\nFirst Name\nLast Name\nEmail\nRole\nPassword.");
            return;
        }

        try {
            DBHandler db = new DBHandler();
            Connection conn = db.getConnection();

            // SQL query to insert a new user into the database
            String query = "INSERT INTO user (username, firstName, lastName, email, phoneNumber, address, role, registration_date, password) VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), ?)";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                // Set values for the placeholders
                statement.setString(1, thisusername);
                statement.setString(2, thisfirstName);
                statement.setString(3, thislastName);
                statement.setString(4, thisemail);
                statement.setString(5, thisPhoneNumber);
                statement.setString(6, thisaddress);
                statement.setString(7, roleBox.getValue());
                statement.setString(8, thispassword);

                // Execute the query
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    loginOptions.showAlert(Alert.AlertType.INFORMATION, "Success", "A new user has been added successfully.");
                    // Clear input fields after adding the user
                    usernameField.clear();
                    fNameField.clear();
                    lNameField.clear();
                    emailField.clear();
                    phoneField.clear();
                    addressField.clear();
                    addressField.clear();
                    passwordField.clear();
                    roleBox.setValue(null);

                    // Refresh the TableView
                    usertableView.setItems(populateUserTable());
                }
            } catch (SQLException exe) {
                loginOptions. showAlert(Alert.AlertType.ERROR, "Error", "Error adding new user: " + exe.getMessage());
            } finally {
                db.closeConnection();
            }
        } catch (Exception e) {
            loginOptions.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding user.");
        }
    }

    /**
     * Method to delete a selected user.
     */
    public static void deleteUser() {
        // Retrieve the selected user from the TableView
        user selectedUser = usertableView.getSelectionModel().getSelectedItem();

        // Check if a user is selected
        if (selectedUser == null) {
            // Show an alert if no user is selected
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a user to delete.");

            return;
        }

        // Get the username of the selected user
        String thisUsername = selectedUser.getUsername();

        // Initialize the DBHandler to establish a connection to the database
        DBHandler db = new DBHandler();
        Connection conn = db.getConnection();

        // SQL query to delete a user based on username
        String query = "DELETE FROM user WHERE username = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {

            // Set the username parameter for the query
            statement.setString(1, thisUsername);

            // Execute the update and check how many rows are deleted
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                // Show success alert if a user is deleted
                showAlert(Alert.AlertType.INFORMATION, "Success", "User deleted successfully.");
                // Clear the input fields and reset the TableView
                usernameField.clear();
                fNameField.clear();
                lNameField.clear();
                emailField.clear();
                phoneField.clear();
                addressField.clear();
                passwordField.clear();
                roleBox.setValue(null);
                usertableView.setItems(populateUserTable());
            } else {
                // Show error if no user was found with the specified username
                showAlert(Alert.AlertType.ERROR, "Error", "No user found with the specified username.");
            }
        } catch (SQLException exe) {
            // Handle SQL exceptions
            showAlert(Alert.AlertType.ERROR, "Error", "Error deleting user: " + exe.getMessage());
            exe.printStackTrace();
        } finally {
            db.closeConnection(); // Close the database connection
        }
    }

    /**
     * This method retrieves all user data from the database and populates an ObservableList of user objects.
     * The ObservableList can be used to display user data in a JavaFX TableView or similar UI component.
     *
     * @return ObservableList<user> A list of user objects containing user data retrieved from the database.
     */
    public static ObservableList<user> populateUserTable() {
        // Initialize the DBHandler and create an observable list to hold user data
        DBHandler db = new DBHandler();
        Connection conn = db.getConnection();
        ObservableList<user> userList = FXCollections.observableArrayList();

        // SQL query to retrieve all user details from the database
        String query = "SELECT username, firstName, lastName, email, phoneNumber, address, role, registration_date, password FROM user";
        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            // Loop through the result set and add user data to the observable list
            while (resultSet.next()) {
                // Extract data for each user and add it to the list
                String thisusername = resultSet.getString("username");
                String thisfname = resultSet.getString("firstName");
                String thislastname = resultSet.getString("lastName");
                String thisemail = resultSet.getString("email");
                String thisphone = resultSet.getString("phoneNumber");
                String thisaddress = resultSet.getString("address");
                String thisrole = resultSet.getString("role");
                String thispassword = resultSet.getString("password");
                userList.add(new user(thisusername, thisfname, thislastname, thisemail, thispassword, thisrole, thisaddress, thisphone));
            }
        } catch (SQLException exe) {
            // Handle SQL exceptions
            showAlert(Alert.AlertType.ERROR, "Error", "Error loading user data: " + exe.getMessage());
            exe.printStackTrace();
        } finally {
            db.closeConnection(); // Close the database connection
        }
        return userList; // Return the populated user list
    }

    /**
     * Adds a new address for a user in the database. This method attempts to insert a new address
     * for a given username into the `user` table.
     *
     * @param username The username of the user whose address is being added.
     * @param address  The new address to be added for the user.
     * @return boolean Returns true if the address was successfully added, false otherwise.
     */
    public static boolean addAddress(String username, String address) {
        // Initialize the DBHandler and establish a database connection
        DBHandler db = new DBHandler();
        try {
            Connection conn = db.getConnection();

            // SQL query to update the address for a given username
            String query = "INSERT INTO user (address) VALUES (?)";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, address); // Set the address parameter

                // Execute the update and check how many rows are updated
                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    // Show success alert if the address is added
                    loginOptions.showAlert(Alert.AlertType.INFORMATION, "Success", "Address added successfully.");
                    return true;
                } else {
                    // Show error if address was not added
                    loginOptions.showAlert(Alert.AlertType.ERROR, "Error", "Failed to add address.");
                    return false;
                }
            }
        } catch (SQLException e) {
            // Handle SQL exceptions
            loginOptions.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding address.");
            e.printStackTrace();
        } finally {
            db.closeConnection(); // Close the database connection
        }
        return false; // Return false if address could not be added
    }

    /**
     * Adds a phone number for a user in the database. This method attempts to insert a new phone number
     * for a given username into the `user` table.
     *
     * @param username    The username of the user whose phone number is being added.
     * @param phoneNumber The phone number to be added for the user.
     * @return boolean    Returns true if the phone number was successfully added, false otherwise.
     */
    public static boolean addPhoneNumber(String username, String phoneNumber) {
        // Initialize the DBHandler and establish a database connection
        DBHandler db = new DBHandler();
        try {
            Connection conn = db.getConnection();

            // SQL query to update the phone number for a given username
            String query = "INSERT INTO user (phoneNumber) VALUES (?)";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, phoneNumber); // Set the phone number parameter

                // Execute the update and check how many rows are updated
                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    // Show success alert if the phone number is added
                    loginOptions.showAlert(Alert.AlertType.INFORMATION, "Success", "Phone number added successfully.");
                    return true;
                } else {
                    // Show error if phone number was not added
                    loginOptions.showAlert(Alert.AlertType.ERROR, "Error", "Failed to add Phone number.");
                    return false;
                }
            }
        } catch (SQLException e) {
            // Handle SQL exceptions
            loginOptions.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding phone number.");
            e.printStackTrace();
        } finally {
            db.closeConnection(); // Close the database connection
        }
        return false; // Return false if phone number could not be added
    }

    /**
     * Signs out the current user by performing necessary session cleanup and redirecting to the login page.
     * This method performs actions like displaying a success message and redirecting the user to the login screen.
     *
     * @param primaryStage The primary stage (window) of the JavaFX application, used to show the login page after sign-out.
     * @return boolean     Returns true if the sign-out process was successful, false otherwise.
     */
    public static boolean signOut(Stage primaryStage) {
        try {
            // Perform any necessary cleanup of user session data here
            showAlert(Alert.AlertType.INFORMATION, "Success", "User signed out successfully.");

            // Redirect to the login page
            loginOptions.signIn(primaryStage);

            // Return true to indicate successful sign-out
            return true;
        } catch (Exception e) {
            // Handle any exceptions that may occur
            e.printStackTrace();
            // Return false to indicate sign-out failure
            return false;
        }
    }

    /**
     * Creates a TableView for displaying user information and a form for editing user details.
     * The TableView contains columns for each user attribute, and the form allows the user to update their information.
     * The TableView is populated with the information of a specific user based on their username.
     *
     * This method also sets up the UI layout, adds input fields and buttons, and listens for row selection in the table.
     * The user information can be updated via the form, and the corresponding data will be displayed in the TableView.
     */
    public static void createSpecificUserTable(){
        // Create the TableView and its columns
        usertableView = new TableView<>();

        TableColumn<user, String> userNameColumn = new TableColumn<>("Username");
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<user, String> fNameColumn = new TableColumn<>("First Name");
        fNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<user, String> lNameColumn = new TableColumn<>("Last Name");
        lNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<user, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<user, String> phoneColumn = new TableColumn<>("Phone Number");
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        TableColumn<user, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<user, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        TableColumn<user, String> registrDate = new TableColumn<>("Register Date");
        registrDate.setCellValueFactory(new PropertyValueFactory<>("registration_date"));

        TableColumn<user, String> passColumn = new TableColumn<>("Password");
        passColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        // Set equal width for each column
        double columnWidth = 116; // Desired width for each column
        userNameColumn.setPrefWidth(columnWidth);
        fNameColumn.setPrefWidth(columnWidth);
        lNameColumn.setPrefWidth(columnWidth);
        emailColumn.setPrefWidth(columnWidth);
        phoneColumn.setPrefWidth(columnWidth);
        addressColumn.setPrefWidth(columnWidth);
        roleColumn.setPrefWidth(columnWidth);
        registrDate.setPrefWidth(columnWidth);
        passColumn.setPrefWidth(columnWidth);

        // Add the columns to the TableView
        usertableView.getColumns().addAll(userNameColumn, fNameColumn, lNameColumn, emailColumn, phoneColumn, addressColumn, roleColumn, registrDate, passColumn);

        // Create the input fields and labels
        Label usernameLabel = new Label("Username:");
        usernameField = new TextField();
        usernameField.setPromptText("Enter username");

        Label fNameLabel = new Label("First Name:");
        fNameField = new TextField();
        fNameField.setPromptText("Enter first name");

        Label lNameLabel = new Label("Last Name:");
        lNameField = new TextField();
        lNameField.setPromptText("Enter last name");

        Label emailLabel = new Label("Email:");
        emailField = new TextField();
        emailField.setPromptText("Enter email");

        Label phoneLabel = new Label("Phone Number:");
        phoneField = new TextField();
        phoneField.setPromptText("Enter phone number");

        Label addressLabel = new Label("Address:");
        addressField = new TextField();
        addressField.setPromptText("Enter address");

        Label registerLabel = new Label("Register Date:");
        registerField = new TextField();
        registerField.setPromptText("Enter registration date");

        Label passLabel = new Label("Password:");
        passwordField = new TextField();
        passwordField.setPromptText("Enter password");

        // Button to update user information
        Button updateBotton = new Button("Update User");
        updateBotton.setOnAction(e -> updateUser(usernameField.getText(),fNameField.getText(),lNameField.getText(),
                emailField.getText(),phoneField.getText(), addressField.getText(), registerField.getText(),
                passwordField.getText()));

        // Create a VBox to hold the input fields and buttons
        VBox inputBox = new VBox(10);
        inputBox.setPadding(new Insets(10));
        inputBox.getChildren().addAll(usernameLabel, usernameField, fNameLabel, fNameField, lNameLabel, lNameField, emailLabel, emailField, phoneLabel, phoneField, addressLabel, addressField, registerLabel, registerField, passLabel, passwordField, updateBotton);

        // Create a BorderPane and set the VBox to the left and the TableView to the center
        BorderPane layoutt = new BorderPane();
        layoutt.setLeft(inputBox);
        layoutt.setCenter(usertableView);

        // Populate the TableView with data
        ObservableList<user> userList = FXCollections.observableArrayList();
        userList.add(getUserInformation(loginOptions.getUsername()));
        usertableView.setItems(userList);

        // Add listener to the TableView for row selection
        usertableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                usernameField.setText(newValue.getUsername());
                fNameField.setText(newValue.getFirstName());
                lNameField.setText(newValue.getLastName());
                emailField.setText(newValue.getEmail());
                passwordField.setText(newValue.getPassword());
                phoneField.setText(newValue.getPhoneNumber());
                addressField.setText(newValue.getAddress());
                registerField.setText(newValue.getRegistration_date());
            }
        });

        settingUserForm.center1.add(layoutt,0,0);
    }

    /**
     * Retrieves detailed information of a user from the database based on their username.
     * This method queries the database for a specific user's data and returns a `user` object containing the user's details.
     *
     * @param username The username of the user whose information is to be retrieved.
     * @return A `user` object containing the user's information (e.g., first name, last name, email, etc.), or a new `user` object if the user is not found.
     */
    public static user getUserInformation(String username) {
        // Create a new user object to store the user's information
        user info = new user();

        // Initialize the DBHandler to establish a connection to the database
        DBHandler db = new DBHandler();
        Connection conn = db.getConnection();

        // SQL query to select all columns for the given username
        String query = "SELECT * FROM user WHERE username = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)){
            // Set the username parameter in the query
            statement.setString(1, username);

            // Execute the query and retrieve the result
            ResultSet resultSet = statement.executeQuery();

            // Check if a record for the given username exists in the result set
            if (resultSet.next()) {
                // Extract data from the result set for the corresponding user
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");
                String address = resultSet.getString("address");
                String phoneNumber = resultSet.getString("phoneNumber");
                String registrationDate = resultSet.getString("registration_Date");

                // Set the extracted data to the user object
                info.setUsername(username);
                info.setFirstname(firstname);
                info.setLastName(lastname);
                info.setEmail(email);
                info.setPassword(password);
                info.setRole(role);
                info.setAddress(address);
                info.setPhoneNumber(phoneNumber);
            }
        } catch (SQLException e){
            // Handle any SQL exceptions that may occur during the query execution
            e.printStackTrace();
        } finally {
            // Close the database connection to release resources
            db.closeConnection();
        }
        // Return the populated user object with the user's information
        return info;
    }

    /**
     * Updates the information of an existing user in the database based on the provided details.
     * This method executes an SQL update query to modify the user's data in the database and refreshes
     * the displayed information in the UI upon successful update.
     *
     * @param username        The unique username of the user to be updated.
     * @param firstName       The updated first name of the user.
     * @param lastName        The updated last name of the user.
     * @param email           The updated email address of the user.
     * @param phone           The updated phone number of the user.
     * @param address         The updated address of the user.
     * @param registrationDate The updated registration date of the user.
     * @param password        The updated password for the user.
     */
    public static void updateUser(String username, String firstName, String lastName, String email,
                                  String phone, String address, String registrationDate,
                                  String password) {
        // Initialize the DBHandler to establish a database connection
        DBHandler db = new DBHandler();
        Connection conn = db.getConnection();

        // SQL query to update the user's details based on the provided username
        String query = "UPDATE user SET firstName = ?, lastName = ?, email = ?, phoneNumber = ?, " +
                "address = ?, registration_date = ?, password = ? WHERE username = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            // Set the parameters in the prepared statement
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, email);
            statement.setString(4, phone);
            statement.setString(5, address);
            statement.setString(6, registrationDate);
            statement.setString(7, password);
            statement.setString(8, username); // The username is used to identify the specific user to be updated

            // Execute the update query
            int rowsUpdated = statement.executeUpdate();

            // If rows are updated, show success message and update the table view
            if (rowsUpdated > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "User updated successfully.");

                // Create a list to hold the updated user information and refresh the table view
                ObservableList<user> userInfoList = FXCollections.observableArrayList();
                userInfoList.add(getUserInformation(username));
                usertableView.setItems(userInfoList);
            }
        } catch (SQLException e) {
            // Handle SQL exceptions that may occur during the update process
            showAlert(Alert.AlertType.ERROR, "Error", "Error while updating user." + e.getMessage());
            e.printStackTrace();
        } finally {
            // Ensure that the database connection is closed after the operation
            db.closeConnection();
        }
    }

    /**
     * Checks whether a user with the given username exists in the database.
     * This method queries the `user` table to check if a username already exists.
     * It returns a boolean value indicating whether the username is found.
     *
     * @param thisusername The username to check for existence in the database.
     * @return `true` if the username exists in the database, `false` otherwise.
     */
    public  static boolean checkUsername(String thisusername) {
        // Initialize the DBHandler to establish a database connection
        DBHandler db = new DBHandler();
        try {
            // Get a connection to the database
            Connection conn = db.getConnection();

            // SQL query to select a user with the provided username
            String query = "SELECT * FROM user WHERE username = ?";

            // Prepare the SQL statement with the query
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                // Set the username parameter in the query
                statement.setString(1, thisusername);

                // Execute the query to get the result set
                ResultSet resultSet = statement.executeQuery();

                // Check if the result set contains any rows (i.e., if the username exists)
                if (resultSet.next()) { // Check if there is at least one result
                    return true; // Username found
                } else {
                    System.out.println("User does not exist.");  // no user found
                }
            }
        } catch (SQLException e) {
            // Handle SQL exceptions that may occur during the update process
            e.printStackTrace();
        } finally {
            // Ensure that the database connection is closed after the operation
            db.closeConnection();
        }
        return false;
    }

    /**
     * This method updates the user information in the database.
     *
     * @param username       The current username of the user to be updated.
     * @param newUsername   The new username to assign to the user (if different).
     * @param firstName     The user's first name.
     * @param lastName      The user's last name.
     * @param email         The user's email address.
     * @param phone         The user's phone number.
     * @param address       The user's physical address.
     * @param password      The user's password.
     * @param roleBox       The ComboBox containing the user's role (e.g., Admin, User).
     */
    public static void updateUser2(String username, String newUsername, String firstName, String lastName, String email,
                                  String phone, String address, String password, ComboBox<String> roleBox) {
        // Create an instance of DBHandler to establish a connection to the database
        DBHandler db = new DBHandler();
        Connection conn = db.getConnection();

        // SQL query to check if the user exists with the provided username
        String checkQuery = "SELECT username, firstName, lastName, email, phoneNumber, address, role, password FROM user WHERE username = ?";
        // SQL query to update the user details
        String updateQuery = "UPDATE user SET username = ?, firstName = ?, lastName = ?, email = ?, phoneNumber = ?, address = ?, role = ?, password = ? WHERE username = ?";

        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            // Set the username parameter to search for the user in the database
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            // If the user is found in the database
            if (rs.next()) {
                // Retrieve the current user details from the database
                String currentUsername = rs.getString("username");
                String currentFirstName = rs.getString("firstName");
                String currentLastName = rs.getString("lastName");
                String currentEmail = rs.getString("email");
                String currentPhone = rs.getString("phoneNumber");
                String currentAddress = rs.getString("address");
                String currentPassword = rs.getString("password");
                String currentRole = rs.getString("role");

                // Check if the user details are the same as the current values
                if (newUsername.equals(currentUsername) &&
                        firstName.equals(currentFirstName) &&
                        lastName.equals(currentLastName) &&
                        email.equals(currentEmail) &&
                        (phone == null ? currentPhone == null : phone.equals(currentPhone)) &&
                        (address == null ? currentAddress == null : address.equals(currentAddress)) &&
                        password.equals(currentPassword) &&
                        roleBox.getValue().equals(currentRole)) {
                    // If no changes are made, show a warning and exit the method
                    showAlert(Alert.AlertType.WARNING, "Warning", "Change something to be updated.");
                    return;
                }
            } else {
                // If no user is found with the provided username, show an error and exit the method
                showAlert(Alert.AlertType.ERROR, "Error", "No user found with the provided username.");
                return;
            }

            // SQL query to check if the new username already exists (but exclude the current user)
            String checkNewUsernameQuery = "SELECT username FROM user WHERE username = ? AND username != ?";
            try (PreparedStatement checkNewStmt = conn.prepareStatement(checkNewUsernameQuery)) {
                checkNewStmt.setString(1, newUsername);
                checkNewStmt.setString(2, username); // Ensure we exclude the current username from the check
                ResultSet newRs = checkNewStmt.executeQuery();

                // If the new username already exists, show an error and exit the method
                if (newRs.next()) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Username already exists.");
                    return;
                }
            }

            // Proceed with the update of the user data in the database
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                updateStmt.setString(1, newUsername); // Set the new username
                updateStmt.setString(2, firstName);
                updateStmt.setString(3, lastName);
                updateStmt.setString(4, email);
                updateStmt.setString(5, phone);
                updateStmt.setString(6, address);
                updateStmt.setString(7, roleBox.getValue());
                updateStmt.setString(8, password);
                updateStmt.setString(9, username); // Use the old username for the WHERE clause

                // Execute the update query
                int rowsUpdated = updateStmt.executeUpdate();
                if (rowsUpdated > 0) {
                    // If the update is successful, show an information message and update the user table view
                    showAlert(Alert.AlertType.INFORMATION, "Info", "User updated successfully.");
                    // Update the user table view if needed
                    usertableView.setItems(populateUserTable()); // Assumes you have a method to repopulate your table
                } else {
                    // If no rows were updated, show an error message
                    showAlert(Alert.AlertType.ERROR, "Error", "No user found with the provided username.");
                }
            }
        } catch (SQLException e) {
            // Handle any SQL exceptions that occur during the process
            showAlert(Alert.AlertType.ERROR, "Error", "Error updating user: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close the database connection in the finally block to ensure it's always closed
            db.closeConnection();
        }
    }
}