package com.example.project;

import com.example.project.Classes.user;
import com.example.project.Connection.DBHandler;
import com.example.project.HomePages.innerHomePage;
import com.example.project.Services.userServices;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import java.sql.*;

public class loginOptions {

    public static TextField username = new TextField();
    private static PasswordField password = new PasswordField();
    private static TextField email = new TextField();
    private static TextField FName = new TextField();
    private static TextField LName = new TextField();
    private static PasswordField confirmPassword = new PasswordField();


    /**
     * Displays the Sign In screen for the user.
     *
     * @param primaryStage The primary stage for the application where the sign-in UI is displayed.
     */
    public static void signIn(Stage primaryStage) {
        // Create and style the Sign In label
        Label signIn = new Label("Sign In");
        signIn.setStyle("-fx-text-fill: #C6AD8F; -fx-font-weight: bold; -fx-font-size: 35px");

        // Set placeholders and dimensions for the username and password fields
        password.setPromptText("Password"); // Placeholder for password field
        password.setPrefWidth(250);          // Set the preferred width
        password.setMinWidth(350);           // Set the minimum width (optional)
        password.setMaxWidth(350);           // Set the maximum width

        username.setPromptText("Username"); // Placeholder for username field
        username.setPrefWidth(250);          // Set the preferred width
        username.setMinWidth(350);           // Set the minimum width (optional)
        username.setMaxWidth(350);           // Set the maximum width

        // Style the username and password fields
        username.setStyle("-fx-text-fill: #A9A9A9; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 20px; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #D3D3D3; " +
                "-fx-background-color: #F6F4F2;");
        password.setStyle("-fx-text-fill: #A9A9A9; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 20px; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #D3D3D3; " +
                "-fx-background-color: #F6F4F2;");

        // Create a GridPane layout for the sign-in form
        GridPane inner = new GridPane();
        inner.setMaxWidth(450); // Set maximum width for the inner grid
        inner.setMaxHeight(450); // Set maximum height for the inner grid
        inner.setVgap(20); // Increased vertical spacing between rows
        inner.setPadding(new Insets(20)); // Padding inside the grid
        inner.setAlignment(Pos.CENTER); // Ensure grid content is centered
        inner.setStyle("-fx-background-color: #425664;"); // Set background color

        // Center the "Sign In" label at the top of the grid
        inner.add(signIn, 0, 0);
        GridPane.setHalignment(signIn, HPos.CENTER); // Align the label horizontally center

        // Add username and password fields to the grid, centered below "Sign In"
        inner.add(username, 0, 1);
        GridPane.setHalignment(username, HPos.CENTER); // Center username field

        inner.add(password, 0, 2);
        GridPane.setHalignment(password, HPos.CENTER); // Center password field

        // Create a "Forgot Password" hyperlink
        Hyperlink forgotPassword = new Hyperlink("Forgot Password?");
        forgotPassword.setStyle("-fx-text-fill: #C6AD8F; -fx-font-size: 12px; -fx-font-weight: bold; -fx-underline: true");

        inner.add(forgotPassword, 0, 3); // Add hyperlink to grid
        GridPane.setHalignment(forgotPassword, HPos.RIGHT); // Align right

        // Create buttons for signing in and creating an account
        Button signInButton = new Button("Sign In");
        signInButton.setStyle("-fx-text-fill:#425664; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 20px; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #C6AD8F; " +
                "-fx-background-color: #C6AD8F;"); // Style for sign in button
        signInButton.setPrefWidth(150); // Set preferred width of 150 pixels
        signInButton.setMinWidth(150);  // Ensure button does not shrink smaller than 150 pixels
        signInButton.setMaxWidth(150);  // Set maximum width

        Button createButton = new Button("Create One"); // Button to create a new account
        createButton.setStyle("-fx-text-fill: #425664; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 20px; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #C6AD8F; " +
                "-fx-background-color: #C6AD8F"); // Style for create account button
        createButton.setPrefWidth(150); // Set preferred width of 150 pixels
        createButton.setMinWidth(150);  // Ensure button does not shrink smaller than 150 pixels
        createButton.setMaxWidth(150);  // Set maximum width

        // Create a horizontal box for the buttons
        HBox buttonBox = new HBox(20, signInButton, createButton); // Horizontal box for buttons
        buttonBox.setAlignment(Pos.CENTER); // Center the buttons horizontally
        inner.add(buttonBox, 0, 4); // Add button box to the grid
        GridPane.setHalignment(buttonBox, HPos.CENTER); // Center button box

        // Spacer row to add vertical spacing for the bottom
        Label spacer = new Label(); // Create a spacer label
        spacer.setPrefHeight(50); // Adjust height as necessary for spacing
        inner.add(spacer, 0, 5); // Add spacer to the grid

        // Create an "ADMINISTRATOR" hyperlink at the bottom-left
        Hyperlink admin = new Hyperlink("ADMINISTRATOR");
        admin.setStyle("-fx-text-fill: #C6AD8F; -fx-font-size: 12px; -fx-font-weight: bold; -fx-underline: true");
        inner.add(admin, 0, 6); // Add admin hyperlink to the grid
        GridPane.setHalignment(admin, HPos.RIGHT); // Align the "ADMINISTRATOR" to the left

        // Create an outer VBox to ensure proper vertical alignment
        VBox outer = new VBox();
        outer.setAlignment(Pos.CENTER); // Center everything vertically
        outer.getChildren().add(inner); // Add inner grid to outer VBox
        outer.setStyle("-fx-background-color: #F6F4F2"); // Set background color for outer container

        // Button Actions
        createButton.setOnAction(e -> {
            signUp(primaryStage); // Handle sign-up button action
        });
        forgotPassword.setOnAction(e -> forgetPassword(primaryStage)); // Handle forgot password action
        admin.setOnAction(e -> admin(primaryStage)); // Handle admin link action
        signInButton.setOnAction(e -> {
            user info = userServices.getUserInformation(username.getText()); // Get user info based on username
            String thisusername = username.getText(); // Get the entered username
            String thispassword = password.getText(); // Get the entered password

            // Validate that both fields are filled
            if (thisusername.isEmpty() || thispassword.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Both username and password are required.");
            } else {
                // Check if the user is an admin
                if(info.getRole().equals("admin")) {
                    showAlert(Alert.AlertType.ERROR, "Sign In Failed", "Not a user");
                } else {
                    // Validate the sign-in credentials
                    boolean isValidLogin = validateSignIn(thisusername, thispassword);
                    if (isValidLogin) {
                        innerHomePage.displayHomePage(primaryStage); // Display home page on successful sign in
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid Username or Password."); // Show error on invalid login
                    }
                }
            }
        });

        // Scene setup for the Sign In UI
        Scene scene = new Scene(outer, 600, 600); // Create a new scene with the outer layout
        primaryStage.setScene(scene); // Set the scene on the primary stage
        primaryStage.setTitle("Sign In"); // Set the title of the window
        primaryStage.setMaximized(true); // Enables maximized window mode with standard controls
    }


    public static void signUp(Stage primaryStage) {
        // Create a label for the Sign Up header
        Label signUp = new Label("Sign Up");
        signUp.setStyle("-fx-text-fill: #C6AD8F; -fx-font-weight: bold; -fx-font-size: 35px");

        // Set up username field with prompt text and width specifications
        username.setPromptText("username");
        username.setPrefWidth(250);          // Set the preferred width
        username.setMinWidth(350);           // Set the minimum width (optional)
        username.setMaxWidth(350);

        // Set up password field with prompt text and width specifications
        password.setPromptText("Password");
        password.setPrefWidth(250);          // Set the preferred width
        password.setMinWidth(350);           // Set the minimum width (optional)
        password.setMaxWidth(350);

        // Set up confirm password field with prompt text and width specifications
        confirmPassword.setPromptText("Confirm Password");
        confirmPassword.setPrefWidth(250);
        confirmPassword.setMinWidth(350);
        confirmPassword.setMaxWidth(350);

        // Set up first name field with prompt text and width specifications
        FName.setPromptText("First Name");
        FName.setPrefWidth(250);          // Set the preferred width
        FName.setMinWidth(350);           // Set the minimum width (optional)
        FName.setMaxWidth(350);

        // Set up last name field with prompt text and width specifications
        LName.setPromptText("Last Name");
        LName.setPrefWidth(250);          // Set the preferred width
        LName.setMinWidth(350);           // Set the minimum width (optional)
        LName.setMaxWidth(350);

        // Set up email field with prompt text and width specifications
        email.setPromptText("Email");
        email.setPrefWidth(250);          // Set the preferred width
        email.setMinWidth(350);           // Set the minimum width (optional)
        email.setMaxWidth(350);

        // Style the input fields for visual consistency
        String inputStyle = "-fx-text-fill: #A9A9A9; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 20px; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #D3D3D3; " +
                "-fx-background-color: #F6F4F2;";

        // Apply the style to each input field
        username.setStyle(inputStyle);
        password.setStyle(inputStyle);
        confirmPassword.setStyle(inputStyle);
        FName.setStyle(inputStyle);
        LName.setStyle(inputStyle);
        email.setStyle(inputStyle);

        // Create a GridPane layout to organize the sign-up form
        GridPane inner = new GridPane();
        inner.setMaxWidth(450);
        inner.setMaxHeight(450);
        inner.setVgap(20); // Set vertical spacing between rows
        inner.setPadding(new Insets(20)); // Padding inside the grid
        inner.setAlignment(Pos.CENTER); // Center grid content
        inner.setStyle("-fx-background-color: #425664;"); // Background color

        // Add the Sign Up label at the top of the GridPane
        inner.add(signUp, 0, 0);
        GridPane.setHalignment(signUp, HPos.CENTER); // Center label horizontally

        // Add input fields to the GridPane in a vertical arrangement
        inner.add(username, 0, 1);
        GridPane.setHalignment(username, HPos.CENTER);

        inner.add(FName, 0, 2);
        GridPane.setHalignment(FName, HPos.CENTER);

        inner.add(LName, 0, 3);
        GridPane.setHalignment(LName, HPos.CENTER);

        inner.add(email, 0, 4);
        GridPane.setHalignment(email, HPos.CENTER);

        inner.add(password, 0, 5);
        GridPane.setHalignment(password, HPos.CENTER); // Center password field

        inner.add(confirmPassword, 0, 6);
        GridPane.setHalignment(confirmPassword, HPos.CENTER);

        // Create buttons for sign-up and sign-in actions
        Button signUpButton = new Button("Sign Up");
        signUpButton.setStyle("-fx-text-fill:#425664; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 20px; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #C6AD8F; " +
                "-fx-background-color: #C6AD8F;");
        signUpButton.setPrefWidth(150); // Set preferred width of 150 pixels
        signUpButton.setMinWidth(150);  // Ensure button does not shrink smaller than 150 pixels
        signUpButton.setMaxWidth(150);

        Button signInButton = new Button("Sign In");
        signInButton.setStyle("-fx-text-fill: #425664; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 20px; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #C6AD8F; " +
                "-fx-background-color: #C6AD8F");
        signInButton.setPrefWidth(150); // Set preferred width of 150 pixels
        signInButton.setMinWidth(150);  // Ensure button does not shrink smaller than 150 pixels
        signInButton.setMaxWidth(150);

        // Add buttons to a horizontal box for layout
        HBox buttonBox = new HBox(20, signUpButton, signInButton); // Horizontal box for buttons
        buttonBox.setAlignment(Pos.CENTER); // Center the buttons horizontally
        inner.add(buttonBox, 0, 7);
        GridPane.setHalignment(buttonBox, HPos.CENTER); // Center button box

        // Create an outer VBox to ensure proper vertical alignment
        VBox outer = new VBox();
        outer.setAlignment(Pos.CENTER); // Center everything vertically
        outer.getChildren().add(inner);
        outer.setStyle("-fx-background-color: #F6F4F2"); // Background color

        // Action handling for the Sign In button
        signInButton.setOnAction(e -> signIn(primaryStage));

        // Action handling for the Sign Up button
        signUpButton.setOnAction(e -> {
            // Retrieve input values
            String thisusername = username.getText().trim();
            String thisfirstname = FName.getText().trim();
            String thislastname = LName.getText().trim();
            String thisemail = email.getText().trim();
            String thispassword = password.getText().trim();
            String thisconfirmpassword = confirmPassword.getText().trim();

            // Validate input fields
            if (thisusername.isBlank() || thisfirstname.isBlank() || thislastname.isBlank() ||
                    thisemail.isBlank() || thispassword.isBlank() || thisconfirmpassword.isBlank()) {
                showAlert(Alert.AlertType.ERROR, "Sign Up Failed", "All the fields must be filled out.");
            } else if (!thispassword.equals(thisconfirmpassword)) {
                showAlert(Alert.AlertType.ERROR, "Sign Up Failed", "Passwords do not match.");
            } else {
                // Attempt to add a new client/user
                if (addClient(thisusername, thisfirstname, thislastname, thisemail, thispassword, "user")) {
                    innerHomePage.displayHomePage(primaryStage); // Navigate to the home page on success
                } else {
                    showAlert(Alert.AlertType.ERROR, "Sign Up Failed", "Please try again.");
                }
            }
        });

        // Scene setup
        Scene scene = new Scene(outer, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Sign Up");
    }


    /**
     * Displays the Administrator login interface.
     *
     * @param primaryStage The main stage of the application.
     */
    public static void admin(Stage primaryStage) {
        // Create a label for the "Administrator" title
        Label admin = new Label("Administrator");
        admin.setStyle("-fx-text-fill: #C6AD8F; -fx-font-weight: bold; -fx-font-size: 35px");

        // Set placeholders and dimensions for the username and password fields
        username.setPromptText("Username");
        password.setPromptText("Password");
        password.setPrefWidth(250);          // Set the preferred width for the password field
        password.setMinWidth(350);           // Set the minimum width for the password field (optional)
        password.setMaxWidth(350);           // Set the maximum width for the password field
        username.setPrefWidth(250);          // Set the preferred width for the username field
        username.setMinWidth(350);           // Set the minimum width for the username field (optional)
        username.setMaxWidth(350);           // Set the maximum width for the username field

        // Style the username and password fields
        username.setStyle("-fx-text-fill: #A9A9A9; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 20px; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #D3D3D3; " +
                "-fx-background-color: #F6F4F2;");
        password.setStyle("-fx-text-fill: #A9A9A9; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 20px; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #D3D3D3; " +
                "-fx-background-color: #F6F4F2;");

        // Create a GridPane layout for the login form
        GridPane inner = new GridPane();
        inner.setMaxWidth(450);             // Set the maximum width of the GridPane
        inner.setMaxHeight(450);            // Set the maximum height of the GridPane
        inner.setVgap(20);                   // Set vertical spacing between rows
        inner.setPadding(new Insets(20));    // Set padding inside the grid
        inner.setAlignment(Pos.CENTER);      // Center the grid content
        inner.setStyle("-fx-background-color: #425664;"); // Set background color for the grid

        // Center the "Administrator" label at the top of the grid
        inner.add(admin, 0, 0);
        GridPane.setHalignment(admin, HPos.CENTER); // Align the title horizontally center

        // Add username and password fields to the grid, centered below the title
        inner.add(username, 0, 1);
        GridPane.setHalignment(username, HPos.CENTER); // Center username field

        inner.add(password, 0, 2);
        GridPane.setHalignment(password, HPos.CENTER); // Center password field

        // Create a "Forgot Password?" hyperlink
        Hyperlink forgotPassword = new Hyperlink("Forgot Password?");
        forgotPassword.setStyle("-fx-text-fill: #C6AD8F; -fx-font-size: 12px; -fx-font-weight: bold; -fx-underline: true");

        // Add the hyperlink to the grid
        inner.add(forgotPassword, 0, 3);
        GridPane.setHalignment(forgotPassword, HPos.RIGHT); // Align right

        // Create the Sign In button
        Button signInButton = new Button("Sign In");
        signInButton.setStyle("-fx-text-fill:#425664; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 20px; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #C6AD8F; " +
                "-fx-background-color: #C6AD8F;");

        signInButton.setPrefWidth(150); // Set preferred width of the button
        signInButton.setMinWidth(150);  // Ensure button does not shrink smaller than preferred width
        signInButton.setMaxWidth(150);  // Set maximum width for the button

        // Add the Sign In button to the grid, centered below the password field
        inner.add(signInButton, 0, 4);
        GridPane.setHalignment(signInButton, HPos.CENTER); // Center button box

        // Create an outer VBox to ensure proper vertical alignment
        VBox outer = new VBox();
        outer.setAlignment(Pos.CENTER); // Center everything vertically
        outer.getChildren().add(inner);
        outer.setStyle("-fx-background-color: #F6F4F2"); // Set background color for the outer container

        // Set actions for the hyperlinks and buttons
        forgotPassword.setOnAction(e -> forgetPassword(primaryStage)); // Action for forgot password
        signInButton.setOnAction(e -> {
            // Retrieve user information based on the entered username
            user info = userServices.getUserInformation(username.getText());
            String thisusername = username.getText();
            String thispassword = password.getText();

            // Check if username or password fields are empty
            if (thisusername.isEmpty() || thispassword.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Both username and password are required.");
            } else {
                // Check if the retrieved user role is not "admin"
                if (info.getRole().equals("user")) {
                    showAlert(Alert.AlertType.ERROR, "Sign In failed", "Not an Administrator");
                } else {
                    // Validate the entered username and password
                    boolean isValidLogin = validateSignIn(thisusername, thispassword);
                    if (isValidLogin) {
                        // If valid, display the home page for the administrator
                        innerHomePage.displayHomePage(primaryStage);
                    } else {
                        // Show an error alert for invalid login
                        showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid Username or Password.");
                    }
                }
            }
        });

        // Set up the scene with the outer VBox
        Scene scene = new Scene(outer);
        primaryStage.setScene(scene); // Set the scene to the primary stage
        primaryStage.setTitle("Administrator"); // Set the title of the window
        primaryStage.setMaximized(true); // Enables maximized window mode with standard controls
    }


    /**
     * Displays the "Forget Password" interface for password recovery.
     *
     * @param primaryStage The main stage of the application.
     */
    public static void forgetPassword(Stage primaryStage) {
        // Create a label for the "Forget Password" title
        Label forget = new Label("Forget Password");
        forget.setStyle("-fx-text-fill: #C6AD8F; -fx-font-weight: bold; -fx-font-size: 35px");

        // Set placeholders and dimensions for the username and password fields
        username.setPromptText("Username");
        username.setPrefWidth(250);          // Set the preferred width for the username field
        username.setMinWidth(350);           // Set the minimum width for the username field (optional)
        username.setMaxWidth(350);           // Set the maximum width for the username field

        password.setPromptText("New Password");
        password.setPrefWidth(250);          // Set the preferred width for the new password field
        password.setMinWidth(350);           // Set the minimum width for the new password field (optional)
        password.setMaxWidth(350);           // Set the maximum width for the new password field

        confirmPassword.setPromptText("Confirm Password");
        confirmPassword.setPrefWidth(250);   // Set the preferred width for the confirm password field
        confirmPassword.setMinWidth(350);    // Set the minimum width for the confirm password field (optional)
        confirmPassword.setMaxWidth(350);    // Set the maximum width for the confirm password field

        // Style the username, password, and confirm password fields
        username.setStyle("-fx-text-fill: #A9A9A9; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 20px; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #D3D3D3; " +
                "-fx-background-color: #F6F4F2;");
        password.setStyle("-fx-text-fill: #A9A9A9; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 20px; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #D3D3D3; " +
                "-fx-background-color: #F6F4F2;");
        confirmPassword.setStyle("-fx-text-fill: #A9A9A9; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 20px; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #D3D3D3; " +
                "-fx-background-color: #F6F4F2;");

        // Create a GridPane layout for the password recovery form
        GridPane inner = new GridPane();
        inner.setMaxWidth(450);             // Set the maximum width of the GridPane
        inner.setMaxHeight(450);            // Set the maximum height of the GridPane
        inner.setVgap(20);                   // Set vertical spacing between rows
        inner.setPadding(new Insets(20));    // Set padding inside the grid
        inner.setAlignment(Pos.CENTER);      // Center the grid content
        inner.setStyle("-fx-background-color: #425664;"); // Set background color for the grid

        // Center the "Forget Password" label at the top of the grid
        inner.add(forget, 0, 0);
        GridPane.setHalignment(forget, HPos.CENTER); // Align the title horizontally center

        // Add username and password fields to the grid, centered below the title
        inner.add(username, 0, 1);
        GridPane.setHalignment(username, HPos.CENTER); // Center username field

        inner.add(password, 0, 2);
        GridPane.setHalignment(password, HPos.CENTER); // Center password field

        inner.add(confirmPassword, 0, 3);
        GridPane.setHalignment(confirmPassword, HPos.CENTER); // Center confirm password field

        // Create the "Apply" button for password recovery
        Button signInButton = new Button("Apply");
        signInButton.setStyle("-fx-text-fill:#425664; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 20px; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #C6AD8F; " +
                "-fx-background-color: #C6AD8F;");
        signInButton.setPrefWidth(150); // Set preferred width of the button
        signInButton.setMinWidth(150);  // Ensure button does not shrink smaller than preferred width
        signInButton.setMaxWidth(150);  // Set maximum width for the button

        // Add the "Apply" button to the grid, centered below the fields
        inner.add(signInButton, 0, 4);
        GridPane.setHalignment(signInButton, HPos.CENTER); // Center button box

        // Create an outer VBox to ensure proper vertical alignment
        VBox outer = new VBox();
        outer.setAlignment(Pos.CENTER); // Center everything vertically
        outer.getChildren().add(inner);
        outer.setStyle("-fx-background-color: #F6F4F2"); // Set background color for the outer container

        // Set action for the "Apply" button
        signInButton.setOnAction(e -> {
            String thisUsername = username.getText().trim(); // Get trimmed username
            String currentP = password.getText().trim();     // Get trimmed new password
            String newP = confirmPassword.getText().trim();   // Get trimmed confirm password

            // Validate input fields for empty or blank values
            if (currentP.isBlank() || newP.isBlank() || thisUsername.isBlank()) {
                showAlert(Alert.AlertType.ERROR, "Apply failed", "Missing information");
            } else if (userServices.checkUsername(thisUsername)) { // Check if the username exists
                // Validate if new password and confirm password match
                if (currentP.equals(newP)) {
                    changePassword(username.getText().trim(), confirmPassword.getText().trim()); // Change the password
                } else {
                    showAlert(Alert.AlertType.ERROR, "Apply failed", "Passwords do not match"); // Alert for mismatched passwords
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Apply failed", "Invalid username"); // Alert for invalid username
            }
        });

        // Scene setup
        Scene scene = new Scene(outer); // Create a new scene with specified dimensions
        primaryStage.setScene(scene);               // Set the scene to the primary stage
        primaryStage.setTitle("Forget Password");
        primaryStage.setMaximized(true); // Enables maximized window mode with standard controls// Set the title of the window
    }


    /**
     * Validates the user's sign-in credentials by checking the database.
     *
     * @param thisusername The username entered by the user.
     * @param thispassword The password entered by the user.
     * @return true if the username and password match an entry in the database; false otherwise.
     */
    private static boolean validateSignIn(String thisusername, String thispassword) {
        String user_name; // Variable to store username (not used in the current context)

        try {
            // Create an instance of DBHandler to manage database connections
            DBHandler db = new DBHandler();
            Connection conn = db.getConnection(); // Get database connection

            // Check if the connection is established
            if (conn == null) {
                System.out.println("Database connection not established. Cannot load database.");
                return false; // Return false if the connection fails
            }

            // SQL query to select a user with the specified username and password
            String query = "SELECT * FROM user WHERE username = ? AND password = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, thisusername);  // Set the username parameter
                statement.setString(2, thispassword);  // Set the password parameter

                // Execute the query and retrieve results
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return true; // User found, login is valid
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print stack trace in case of SQL exceptions
        }

        // Return false if the user is not found or if an error occurs
        return false;
    }


    /**
     * Adds a new client to the database.
     *
     * @param thisusername The username of the new client.
     * @param thisfirstName The first name of the new client.
     * @param thislastName The last name of the new client.
     * @param thisemail The email of the new client.
     * @param thispassword The password for the new client.
     * @param role The role assigned to the new client (e.g., user, admin).
     * @return true if the client was successfully added to the database; false otherwise.
     */
    private static boolean addClient(String thisusername, String thisfirstName, String thislastName, String thisemail, String thispassword, String role) {
        try {
            // Create an instance of DBHandler to manage database connections
            DBHandler db = new DBHandler();
            Connection conn = db.getConnection(); // Get database connection

            // Check if the connection is established
            if (conn == null) {
                System.out.println("Database connection not established. Cannot load database.");
                return false; // Return false if the connection fails
            }

            // SQL query to insert a new user into the database
            String query = "INSERT INTO user (username, firstName, lastName, email, password, role, registration_date) VALUES (?, ?, ?, ?, ?, ?, NOW())";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, thisusername);  // Set the username parameter
                statement.setString(2, thisfirstName);  // Set the first name parameter
                statement.setString(3, thislastName);   // Set the last name parameter
                statement.setString(4, thisemail);       // Set the email parameter
                statement.setString(5, thispassword);    // Set the password parameter
                statement.setString(6, role);            // Set the role parameter

                // Execute the update and check how many rows were inserted
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("New user added successfully.");

                    // Clear the input fields after successful insertion
                    FName.clear();
                    LName.clear();
                    email.clear();
                    password.clear();
                    confirmPassword.clear();
                    return true; // Return true indicating success
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print stack trace in case of SQL exceptions
        }
        return false; // Return false if insertion fails or an error occurs
    }


    /**
     * Displays a custom alert dialog with specified type, title, and content.
     *
     * @param alertType The type of alert to be displayed (e.g., ERROR, INFORMATION).
     * @param title The title of the alert dialog.
     * @param content The content text to be displayed in the alert dialog.
     */
    public static void showAlert(Alert.AlertType alertType, String title, String content) {
        // Create a new alert of the specified type
        Alert alert = new Alert(alertType);
        alert.setTitle(title); // Set the title of the alert
        alert.setContentText(content); // Set the content text of the alert
        alert.getDialogPane().setHeaderText(null); // Remove default header text

        // Set styles for the dialog pane to customize its appearance
        alert.getDialogPane().setStyle("-fx-background-color: #425664; " +
                "-fx-border-color: #C6AD8F; " +
                "-fx-border-radius: 15; " +
                "-fx-background-radius: 15; " +
                "-fx-border-width: 2;" +
                "-fx-font-weight: bold");

        // Set text color for the content label in the dialog
        Label contentLabel = (Label) alert.getDialogPane().lookup(".content.label");
        if (contentLabel != null) {
            contentLabel.setTextFill(Color.web("#C6AD8F")); // Set content text color to the specified color
        }

        // Customize the appearance of alert buttons
        alert.getButtonTypes().forEach(buttonType -> {
            Button button = (Button) alert.getDialogPane().lookupButton(buttonType);
            button.setStyle("-fx-background-color: #C6AD8F; -fx-text-fill: white; -fx-font-weight: bold;"); // Style for buttons
        });

        // Show the alert and wait for the user to respond
        alert.showAndWait();
    }


    /**
     * Changes the password for the specified username in the database.
     *
     * @param thisusername The username for which the password is to be changed.
     * @param newpassword The new password to be set for the user.
     */
    private static void changePassword(String thisusername, String newpassword) {
        // Create a new stage for displaying the home page after password change
        Stage primaryStage = new Stage();

        try {
            // Initialize database connection
            DBHandler db = new DBHandler();
            Connection conn = db.getConnection();

            // Check if the database connection was established successfully
            if (conn == null) {
                System.out.println("Database connection not established. Cannot load database.");
                return; // Exit method if connection fails
            }

            // SQL query to update the user's password
            String query = "UPDATE user SET password = ? WHERE username = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, newpassword); // Set the new password in the query
                statement.setString(2, thisusername); // Set the username in the query

                // Execute the update and check how many rows were affected
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Password changed successfully.");

                    // Clear the input fields after successful password change
                    username.clear();
                    password.clear();
                    confirmPassword.clear();

                    // Display the home page after successful password change
                    innerHomePage.displayHomePage(primaryStage);
                }
            }
        } catch (SQLException e) {
            // Print the stack trace for any SQL exceptions encountered
            e.printStackTrace();
        }
    }


    /**
     * Retrieves the text from the username input field.
     *
     * @return A string representing the current username entered in the input field.
     */
    public static String getUsername() {
        // Return the text entered in the username input field
        return username.getText();
    }
}