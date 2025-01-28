package com.example.project.Services;

import com.example.project.Connection.DBHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.project.Settings.settingAdminForm.center;
import static com.example.project.loginOptions.showAlert;

/**
 * A service class to handle email change requests for a user.
 * Provides functionality to update a user's email address in the database
 * by verifying their old email and ensuring it matches the one stored in the database.
 */
public class emailServices {

    public static TextField oldEmailField;
    public static TextField newEmailField;
    public static TextField usernameField;

    public static Label oldemailLabel;
    public static Label newemailLabel;
    public static Label usernameLabel;

    /**
     * Attempts to change the email address for a user.
     * Verifies the old email and updates it to a new email in the database.
     *
     * @param username The username of the user who wants to change their email.
     * @param oldEmail The current email that the user is trying to replace.
     * @param newEmail The new email that the user wants to set.
     * @return A boolean indicating if the email change was successful.
     */
    public static boolean changeEmail(String username, String oldEmail, String newEmail) {
        // Create DBHandler instance to manage the database connection
        DBHandler db = new DBHandler();

        try {
            // Establish the database connection
            Connection conn = db.getConnection();

            // Query to retrieve the existing email for the given username
            String query = "SELECT email FROM user WHERE username = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                // Set the username parameter in the query
                statement.setString(1, username);

                // Execute the query and check if the user exists in the database
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        // Retrieve the existing email from the result set
                        String existingEmail = resultSet.getString("email");

                        // Check if the old email provided matches the existing email in the database
                        if (oldEmail.equals(existingEmail)) {
                            // Update the email in the database if the old email matches
                            String updateQuery = "UPDATE user SET email = ? WHERE username = ?";
                            try (PreparedStatement updateStatement = conn.prepareStatement(updateQuery)) {
                                // Set the new email and username for the update query
                                updateStatement.setString(1, newEmail);
                                updateStatement.setString(2, username);

                                // Execute the update query and check if the email was successfully updated
                                int rowsUpdated = updateStatement.executeUpdate();
                                if (rowsUpdated > 0) {
                                    // Display success message and return true if email updated
                                    showAlert(Alert.AlertType.INFORMATION, "Success", "Email changed successfully.");
                                    return true;
                                } else {
                                    // Display error message if the update fails
                                    showAlert(Alert.AlertType.ERROR, "Error", "Email change failed.");
                                }
                            }
                        } else {
                            // Display error message if the old email doesn't match
                            showAlert(Alert.AlertType.ERROR, "Error", "Old email does not match.");
                        }
                    } else {
                        // Display error message if the username doesn't exist in the database
                        showAlert(Alert.AlertType.ERROR, "Error", "Username not found.");
                    }
                }
            }
        } catch (SQLException e) {
            // Display error message if there is an SQL exception
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while changing the email.");
            e.printStackTrace();
        } finally {
            // Close the database connection
            db.closeConnection();
        }
        // Return false if the email change was not successful
        return false;
    }

    /**
     * This method creates the graphical user interface (GUI) for changing a user's email.
     * It includes input fields for the username, old email, and new email, along with a button
     * to trigger the email change process.
     *
     * The method uses JavaFX components such as `TextField`, `Label`, `Button`, and `VBox` to
     * structure the UI. It sets up event handling for the button to call the `changeEmail`
     * method when clicked.
     */
    public static void changeEmailInterface(){
        // Initialize the labels and text fields for user input
        usernameLabel = new Label("Username: ");
        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");

        oldemailLabel = new Label("Old Email: ");
        oldEmailField = new TextField();
        oldEmailField.setPromptText("Enter old email");

        newemailLabel = new Label("New Email: ");
        newEmailField = new TextField();
        newEmailField.setPromptText("Enter new email");

        // Create a button to trigger the email change action
        Button change = new Button("Change Email");

        // Create a VBox to organize the input components vertically
        VBox vbox = new VBox(20); // Spacing between HBox elements

        // Create individual VBoxes for each input field-label pair for organization
        VBox userBox = new VBox(10); // Internal spacing between label and field
        userBox.getChildren().addAll(usernameLabel, usernameField);

        VBox oldPassBox = new VBox(10);
        oldPassBox.getChildren().addAll(oldemailLabel, oldEmailField);

        VBox newPassBox = new VBox(10);
        newPassBox.getChildren().addAll(newemailLabel, newEmailField);

        // Add all individual VBox containers to the main VBox
        vbox.getChildren().addAll(userBox, oldPassBox, newPassBox, change);

        // Add padding to the VBox to ensure space around the edges
        vbox.setPadding(new Insets(40, 10, 10, 40)); // Top, Right, Bottom, Left padding

        // Add the VBox layout containing all components to the main layout (center)
        center.getChildren().add(vbox);

        // Set up the action handler for the "Change Email" button
        change.setOnAction(e -> {
            // Call the changeEmail method with the input from the text fields
            changeEmail(usernameField.getText(), oldEmailField.getText(), newEmailField.getText());
        });
    }
}
