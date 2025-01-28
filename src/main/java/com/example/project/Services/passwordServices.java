package com.example.project.Services;

import com.example.project.Connection.DBHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.project.Settings.settingAdminForm.center;

/**
 * A utility class for handling password-related services such as changing the user password.
 * It includes functionality to check and update the user's password in the database.
 */
public class passwordServices {

    public static TextField oldPassField;
    public static TextField newPassField;
    public static TextField usernameField;

    public static Label oldpassLabel;
    public static Label newpassLabel;
    public static Label usernameLabel;

    /**
     * Changes the password for a user if the old password matches the existing one.
     *
     * This method first checks if the old password provided by the user matches the
     * existing password stored in the database. If they match, the password is updated
     * with the new password provided by the user.
     *
     * @param username The username of the user requesting the password change.
     * @param oldPassword The user's current password.
     * @param newPassword The new password the user wants to set.
     * @return true if the password change was successful, false otherwise.
     */
    public static boolean changePassword(String username, String oldPassword, String newPassword) {
        DBHandler db = new DBHandler(); // Initialize the DBHandler to manage database operations
        try {
            Connection conn = db.getConnection();  // Get the database connection

            // Query to check if the old password matches the stored password for the given username
            String query = "SELECT password FROM user WHERE username = ?";

            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, username);

                // Execute the query and check the result
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String existingPassword = resultSet.getString("password");

                        // Compare the old password with the existing password
                        if (oldPassword.equals(existingPassword)) {
                            // If passwords match, proceed to update the password
                            String updateQuery = "UPDATE user SET password = ? WHERE username = ?";
                            try (PreparedStatement updateStatement = conn.prepareStatement(updateQuery)) {
                                updateStatement.setString(1, newPassword);
                                updateStatement.setString(2, username);

                                // Execute the update statement
                                int rowsUpdated = updateStatement.executeUpdate();
                                if (rowsUpdated > 0) {
                                    // If rows were updated, password change was successful
                                    showAlert(Alert.AlertType.INFORMATION, "Success", "Password changed successfully.");
                                    return true;
                                } else {
                                    // If no rows were updated, inform the user about failure
                                    showAlert(Alert.AlertType.ERROR, "Error", "Password change failed.");
                                }
                            }
                        } else {
                            // If old password does not match, show error
                            showAlert(Alert.AlertType.ERROR, "Error", "Old password does not match.");
                        }
                    } else {
                        // If username is not found in the database, show error
                        showAlert(Alert.AlertType.ERROR, "Error", "Username not found.");
                    }
                }
            }
        } catch (SQLException e) {
            // Catch any SQL exceptions and show an error alert
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while changing the password.");
            e.printStackTrace();
        } finally {
            // Ensure the database connection is closed after the operation
            db.closeConnection();
        }
        // Return false if password change was not successful
        return false;
    }

    /**
     * Displays an alert dialog to the user with a specified type, title, and message.
     *
     * @param alertType The type of the alert (e.g., INFORMATION, ERROR, CONFIRMATION).
     * @param error The title of the alert dialog (e.g., "Success", "Error").
     * @param s The message displayed in the body of the alert dialog.
     */
    private static void showAlert(Alert.AlertType alertType, String error, String s) {
    }

    /**
     * Creates and displays the user interface for changing a user's password.
     * This interface includes fields for entering the username, old password, and new password,
     * as well as a button to trigger the password change operation.
     */
    public static void changePasswordInterface(){
        // Create and initialize the UI components (labels and text fields)
        usernameLabel = new Label("Username: ");
        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");

        oldpassLabel = new Label("Old Password: ");
        oldPassField = new TextField();
        oldPassField.setPromptText("Enter old password");

        newpassLabel = new Label("New Password: ");
        newPassField = new TextField();
        newPassField.setPromptText("Enter new password");

        // Create the 'Change Password' button
        Button change = new Button("Change Password");

        // VBox layout for the components (vertical box with spacing between elements)
        VBox vbox = new VBox(20);

        // Create VBox containers for each label and text field pair
        VBox userBox = new VBox(10);
        userBox.getChildren().addAll(usernameLabel, usernameField);

        VBox oldPassBox = new VBox(10);
        oldPassBox.getChildren().addAll(oldpassLabel, oldPassField);

        VBox newPassBox = new VBox(10);
        newPassBox.getChildren().addAll(newpassLabel, newPassField);

        // Add each VBox (label + text field pair) to the main VBox layout
        vbox.getChildren().addAll(userBox, oldPassBox, newPassBox, change);

        // Add padding to the main VBox to control the spacing around the edges
        vbox.setPadding(new Insets(40, 10, 10, 40)); // Top, Right, Bottom, Left padding

        // Add the VBox to the central layout (GridPane or other container)
        center.getChildren().add(vbox);

        // Define the action when the 'Change Password' button is clicked
        change.setOnAction(e -> {
            changePassword(usernameField.getText(), oldPassField.getText(), newPassField.getText());
        });

    }
}
