package com.example.project.Settings;

import com.example.project.loginOptions;
import com.example.project.HomePages.innerHomePage;
import com.example.project.Services.borrowingTransactionServices;
import com.example.project.Services.userServices;
import com.example.project.bookDetails;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.geometry.Insets;

/**
 * This class creates and manages the user settings interface.
 * It allows the user to manage various aspects of the system, such as borrowing books, commenting, etc.
 */
public class settingUserForm {

    // GridPane objects for organizing the layout
    public static GridPane center1;
    public static GridPane top;

    /**
     * Displays the user settings form.
     * This method constructs the layout for the user settings page, including
     * the left sidebar with user-related options and the top bar with navigation controls.
     *
     * @param primaryStage the main application window (Stage)
     */
    public static void display(Stage primaryStage){

        // Load the logo image for the user settings page
        Image image = new Image("file:C:\\Users\\mayam\\OneDrive\\Desktop\\settingLogo.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(80);  // Set the width of the logo image
        imageView.setFitHeight(80); // Set the height of the logo image

        // Create a label for the account heading
        Label label = new Label("My Account\nUser");
        label.setStyle("-fx-text-fill: #808080; -fx-font-weight: bold; -fx-font-size: 20px;");

        // Create a horizontal line to separate the header from the content
        Line line = new Line(0, 0, 300, 0); // Horizontal line spanning 300 pixels
        line.setStroke(Color.web("#425664")); // Dark grayish-blue color
        line.setStrokeWidth(3); // Set line width

        // Create buttons for user settings and related actions
        Button borrowed = new Button("Borrowed books");
        borrowed.setPrefWidth(250);
        borrowed.setPrefHeight(30);
        borrowed.setStyle("-fx-text-fill:#425664; " +
                "-fx-font-size: 15px; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #C6AD8F; " +
                "-fx-background-color: #C6AD8F;" +
                "-fx-font-weight: bold");

        // Layout for the left section containing user settings options
        Button userManagment = new Button("User settings");
        userManagment.setPrefWidth(250);
        userManagment.setPrefHeight(30);
        userManagment.setStyle("-fx-text-fill:#425664; " +
                "-fx-font-size: 15px; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #C6AD8F; " +
                "-fx-background-color: #C6AD8F;" +
                "-fx-font-weight: bold");

        Button reminder = new Button("Reminder");
        reminder.setPrefWidth(250);
        reminder.setPrefHeight(30);
        reminder.setMaxHeight(30);
        reminder.setStyle("-fx-text-fill:#425664; " +
                "-fx-font-size: 15px; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #C6AD8F; " +
                "-fx-background-color: #C6AD8F;" +
                "-fx-font-weight: bold");

        Button fines = new Button("Check Fines");
        fines.setPrefWidth(250);
        fines.setPrefHeight(30);
        fines.setMaxHeight(30);
        fines.setStyle("-fx-text-fill:#425664; " +
                "-fx-font-size: 15px; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #C6AD8F; " +
                "-fx-background-color: #C6AD8F;" +
                "-fx-font-weight: bold");

        // Sign-out and extension buttons
        Button signOut = new Button("Sign Out");
        signOut.setPrefWidth(100);
        signOut.setPrefHeight(35);
        signOut.setStyle("-fx-text-fill:#425664; " +
                "-fx-font-size: 15px; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #C6AD8F; " +
                "-fx-background-color: #F6F4F2;" +
                "-fx-font-weight: bold");

        Button extend = new Button("Extention");
        extend.setPrefWidth(100);
        extend.setPrefHeight(35);
        extend.setStyle("-fx-text-fill:#425664; " +
                "-fx-font-size: 15px; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #C6AD8F; " +
                "-fx-background-color: #F6F4F2;" +
                "-fx-font-weight: bold");

        // Setup the left panel of the UI
        GridPane left = new GridPane();
        left.setVgap(10);  // Vertical gap between elements
        left.setHgap(10); // Horizontal gap between elements
        left.setPrefWidth(300);  // Set preferred width of the left panel
        left.setStyle("-fx-background-color: #F6F4F2"); // Light background color
        VBox.setVgrow(left, Priority.ALWAYS);

        // Add elements to the left panel
        left.add(imageView, 0, 0);
        left.add(line, 0, 1, 2, 1); // Horizontal line spans two columns
        left.add(label, 1, 0);
        left.add(borrowed, 0, 4, 2, 1); // Buttons occupy two columns
        left.add(userManagment, 0, 5, 2, 1);
        left.add(reminder, 0, 6, 2, 1);
        left.add(fines, 0, 7, 2, 1);

        // Spacer to push the signOut button to the bottom of the left panel
        Region spacer = new Region();
        GridPane.setVgrow(spacer, Priority.ALWAYS); // Allow vertical growth
        left.add(spacer, 0, 8, 2, 1);
        left.add(signOut, 0, 9, 2, 1); // Add sign-out button

        // Set horizontal alignment for buttons
        GridPane.setHalignment(borrowed, HPos.CENTER);
        GridPane.setHalignment(userManagment, HPos.CENTER);
        GridPane.setHalignment(reminder, HPos.CENTER);
        GridPane.setHalignment(fines, HPos.CENTER);
        GridPane.setHalignment(signOut, HPos.CENTER);

        // Set margins for various UI elements
        GridPane.setMargin(imageView, new Insets(15, 5, 10, 15)); // Padding for the logo image
        GridPane.setMargin(label, new Insets(15, 15, 15, 0)); // Padding for label
        GridPane.setMargin(line, new Insets(0, 0, 15, 0)); // Padding for line
        GridPane.setMargin(borrowed, new Insets(10, 0, 10, 0)); // Padding for buttons
        GridPane.setMargin(userManagment, new Insets(10, 0, 10, 0));
        GridPane.setMargin(reminder, new Insets(10, 0, 10, 0));
        GridPane.setMargin(fines, new Insets(10, 0, 10, 0));
        GridPane.setMargin(signOut, new Insets(10, 0, 50, 0)); // Larger margin for the sign-out button at the bottom

        // Create and style the back hyperlink (HomePage link)
        Hyperlink back = new Hyperlink("HomePage");
        back.setStyle("-fx-text-fill: white; -fx-font-weight: bold; "
                + "-fx-focus-color: transparent; -fx-border-color: transparent; -fx-font-size: 30");

        // Setup top navigation bar with back link
        top = new GridPane();
        top.setPrefHeight(120); // Height of the top bar
        top.setStyle("-fx-background-color: #425664"); // Background color for the top bar
        top.add(back, 0, 0); // Add the back link
        top.setAlignment(Pos.CENTER_RIGHT); // Align the back link to the right

        // Center section to display dynamic content
        center1 = new GridPane();
        center1.setStyle("-fx-background-color: #C6AD8F"); // Background color for the center
        GridPane.setHgrow(center1, Priority.ALWAYS); // Allow horizontal growth
        GridPane.setVgrow(center1, Priority.ALWAYS); // Allow vertical growth

        // Action for the back hyperlink to navigate to the home page
        back.setOnAction(e-> {
            bookDetails.check = true; // Set flag to true indicating navigation to the homepage
            innerHomePage.displayHomePage(primaryStage); // Navigate to the home page
        });

        // Action for the 'Borrowed books' button
        borrowed.setOnAction(e-> {
            bookDetails.check = false; // Set flag to false indicating user is viewing borrowed books
            ScrollPane borrow = borrowingTransactionServices.displayBorrowedBookScrollPane();
            center1.getChildren().clear(); // Clear the center section
            center1.getChildren().add(borrow); // Add the borrowed books view
            innerHomePage.setBookCoverActions(borrow,primaryStage); // Set book cover actions

        });

        // Action for the 'Reminder' button
        reminder.setOnAction(e -> {
            center1.getChildren().clear(); // Clear the center section
            borrowingTransactionServices.createBorrowedTransactionTableForUser(loginOptions.getUsername());
        });

        // Action for the 'Check Fines' button
        fines.setOnAction(e -> {
            center1.getChildren().clear(); // Clear the center section
            com.example.project.Services.fineServices.createFineTableForUser(loginOptions.getUsername());
        });

        // Action for the 'User Management' button
        userManagment.setOnAction(e -> {
            center1.getChildren().clear(); // Clear the center section
            userServices.createSpecificUserTable(); // Display the user management table
        });

        // Action for the 'Sign Out' button
        signOut.setOnAction(e -> userServices.signOut(primaryStage)); // Sign out the user

        // Create the layout for the entire screen
        GridPane layout = new GridPane();
        layout.add(left, 0, 0, 1, 2); // Left column (spans two rows)
        layout.add(top, 1, 0); // Top bar
        layout.add(center1, 1, 1); // Center section

        // Set horizontal alignment for the top panel
        GridPane.setHalignment(top, HPos.RIGHT);

        // Create and display the scene
        Scene scene = new Scene(layout);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Settings"); // Set the window title
        primaryStage.setMaximized(true); // Maximize the window
        primaryStage.show(); // Show the window
    }
}