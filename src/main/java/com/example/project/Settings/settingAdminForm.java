package com.example.project.Settings;


import com.example.project.HomePages.innerHomePage;
import com.example.project.Services.bookServices;
import com.example.project.Services.userServices;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

/**
 * This class creates and manages the admin settings interface.
 * It allows the admin to manage various aspects of the system, such as books, users, fines, etc.
 */
public class settingAdminForm{

    // GridPane containers for layout
    public static GridPane center;
    public static GridPane top;

    /**
     * Displays the settings page for the admin interface.
     *
     * @param primaryStage the main application window (Stage).
     */
    public static void display(Stage primaryStage) {

        // Load the logo image for the admin settings
        Image image = new Image("file:C:\\Users\\mayam\\OneDrive\\Desktop\\settingLogo.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(80);
        imageView.setFitHeight(80);

        // Create a label for the admin account heading
        Label label = new Label("My Account\nAdministrator");
        label.setStyle("-fx-text-fill: #808080; -fx-font-weight: bold; -fx-font-size: 20px;");

        // Horizontal line for separating sections
        Line line = new Line(0, 0, 300, 0);
        line.setStroke(Color.web("#425664")); // Line color
        line.setStrokeWidth(3); // Line width

        // Create buttons for various management options
        Button book = new Button("Book Management");
        book.setPrefWidth(200);
        book.setPrefHeight(30);
        book.setStyle("-fx-text-fill:#425664; " +
                "-fx-font-size: 15px; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #C6AD8F; " +
                "-fx-background-color: #C6AD8F;" +
                "-fx-font-weight: bold");

        Button user = new Button("User Management");
        user.setPrefWidth(200);
        user.setPrefHeight(30);
        user.setStyle("-fx-text-fill:#425664; " +
                "-fx-font-size: 15px; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #C6AD8F; " +
                "-fx-background-color: #C6AD8F;" +
                "-fx-font-weight: bold");

        Button fine = new Button("Fine Management");
        fine.setPrefWidth(200);
        fine.setPrefHeight(30);
        fine.setStyle("-fx-text-fill:#425664; " +
                "-fx-font-size: 15px; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #C6AD8F; " +
                "-fx-background-color: #C6AD8F;" +
                "-fx-font-weight: bold");

        Button notification = new Button("Notification Management");
        notification.setPrefWidth(250);
        notification.setPrefHeight(30);
        notification.setStyle("-fx-text-fill:#425664; " +
                "-fx-font-size: 15px; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #C6AD8F; " +
                "-fx-background-color: #C6AD8F;" +
                "-fx-font-weight: bold");

        Button comment = new Button("Comment Management");
        comment.setPrefWidth(250);
        comment.setPrefHeight(30);
        comment.setStyle("-fx-text-fill:#425664; " +
                "-fx-font-size: 15px; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #C6AD8F; " +
                "-fx-background-color: #C6AD8F;" +
                "-fx-font-weight: bold");

        Button genre = new Button("Genre Management");
        genre.setPrefWidth(250);
        genre.setPrefHeight(30);
        genre.setStyle("-fx-text-fill:#425664; " +
                "-fx-font-size: 15px; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #C6AD8F; " +
                "-fx-background-color: #C6AD8F;" +
                "-fx-font-weight: bold");

        Button transaction = new Button("Transaction Management");
        transaction.setPrefWidth(250);
        transaction.setPrefHeight(30);
        transaction.setStyle("-fx-text-fill:#425664; " +
                "-fx-font-size: 15px; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #C6AD8F; " +
                "-fx-background-color: #C6AD8F;" +
                "-fx-font-weight: bold");

        Button author = new Button("Author Management");
        author.setPrefWidth(250);
        author.setPrefHeight(30);
        author.setStyle("-fx-text-fill:#425664; " +
                "-fx-font-size: 15px; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #C6AD8F; " +
                "-fx-background-color: #C6AD8F;" +
                "-fx-font-weight: bold");

        Button changePassword = new Button("Change Password");
        changePassword.setPrefWidth(250);
        changePassword.setPrefHeight(30);
        changePassword.setStyle("-fx-text-fill:#425664; " +
                "-fx-font-size: 15px; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #C6AD8F; " +
                "-fx-background-color: #C6AD8F;" +
                "-fx-font-weight: bold");

        Button changeEmail = new Button("Change Email");
        changeEmail.setPrefWidth(250);
        changeEmail.setPrefHeight(30);
        changeEmail.setStyle("-fx-text-fill:#425664; " +
                "-fx-font-size: 15px; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #C6AD8F; " +
                "-fx-background-color: #C6AD8F;" +
                "-fx-font-weight: bold");

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

        // Add components to the left pane (vertical menu)
        GridPane left = new GridPane();
        left.setVgap(10); // Vertical gap between elements
        left.setHgap(10);
        left.setPrefWidth(300);
        left.setStyle("-fx-background-color: #F6F4F2");

        VBox.setVgrow(left, Priority.ALWAYS);

        // Add the logo, label, line, and buttons to the left pane
        left.add(imageView, 0, 0);
        left.add(label, 1, 0);
        left.add(line, 0, 1, 2, 1);
        left.add(book, 0, 2, 2, 1);
        left.add(user, 0, 3, 2, 1);
        left.add(fine, 0, 4, 2, 1);
        left.add(author, 0, 5, 2, 1); // Added author button here
        left.add(notification, 0, 6, 2, 1);
        left.add(comment, 0, 7, 2, 1);
        left.add(genre, 0, 8, 2, 1);
        left.add(transaction, 0, 9, 2, 1);
        left.add(changePassword, 0, 10, 2, 1);
        left.add(changeEmail, 0, 11, 2, 1);
        left.add(signOut, 0, 12, 2, 1); // Moved signOut down one position

        // Align elements to the center horizontally in the left pane
        GridPane.setHalignment(book, HPos.CENTER);
        GridPane.setHalignment(user, HPos.CENTER);
        GridPane.setHalignment(fine, HPos.CENTER);
        GridPane.setHalignment(author, HPos.CENTER); // Aligned author
        GridPane.setHalignment(notification, HPos.CENTER);
        GridPane.setHalignment(comment, HPos.CENTER);
        GridPane.setHalignment(genre, HPos.CENTER);
        GridPane.setHalignment(transaction, HPos.CENTER);
        GridPane.setHalignment(changePassword, HPos.CENTER);
        GridPane.setHalignment(changeEmail, HPos.CENTER);
        GridPane.setHalignment(signOut, HPos.CENTER);

        // Set margins for each component in the left pane
        GridPane.setMargin(imageView, new Insets(15, 5, 10, 15)); // Top, Right, Bottom, Left
        GridPane.setMargin(label, new Insets(15, 15, 15, 0));
        GridPane.setMargin(line, new Insets(0, 0, 10, 0));
        GridPane.setMargin(book, new Insets(5, 0, 5, 0)); // Reduced space below
        GridPane.setMargin(user, new Insets(5, 0, 5, 0)); // Reduced space below
        GridPane.setMargin(fine, new Insets(5, 0, 10, 0)); // Reduced space below
        GridPane.setMargin(author, new Insets(5, 0, 10, 0)); // Reduced space below for new button
        GridPane.setMargin(notification, new Insets(5, 0, 10, 0)); // Reduced space below
        GridPane.setMargin(comment, new Insets(5, 0, 10, 0)); // Reduced space below
        GridPane.setMargin(genre, new Insets(5, 0, 10, 0)); // Reduced space below
        GridPane.setMargin(transaction, new Insets(5, 0, 10, 0)); // Reduced space below
        GridPane.setMargin(changePassword, new Insets(5, 0, 10, 0)); // Reduced space below
        GridPane.setMargin(changeEmail, new Insets(5, 0, 10, 0)); // Reduced space below
        GridPane.setMargin(signOut, new Insets(5, 0, 10, 0)); // Reduced space below

        // Create top section with a hyperlink for navigation back to home page
        top = new GridPane();
        top.setPrefHeight(120);
        top.setStyle("-fx-background-color: #425664");
        top.setAlignment(Pos.CENTER);

        Hyperlink back = new Hyperlink("HomePage");
        back.setStyle("-fx-text-fill: white; -fx-font-weight: bold; "
                + "-fx-focus-color: transparent; -fx-border-color: transparent; -fx-font-size: 20");
        top.getChildren().add(back);
        top.setAlignment(Pos.CENTER_RIGHT);

        // Action for back hyperlink
        back.setOnAction(e-> innerHomePage.displayHomePage(primaryStage));

        // Center section for displaying dynamic content
        center = new GridPane();
        center.setStyle("-fx-background-color: #C6AD8F");
        GridPane.setHgrow(center, Priority.ALWAYS);
        GridPane.setVgrow(center, Priority.ALWAYS);

        // Handle User Management Button click
        user.setOnAction(e -> {
            center.getChildren().clear(); // Clear the existing content in the center
            com.example.project.Services.userServices.createUserTable(); // Create and display the user management table
        });

        // Handle Genre Management Button click
        genre.setOnAction(e -> {
            center.getChildren().clear(); // Clear the existing content in the center
            com.example.project.Services.genreServices.createGenreTable(); // Create and display the genre management table
        });

        // Handle Transaction Management Button click
        transaction.setOnAction(e -> {
            center.getChildren().clear(); // Clear the existing content in the center
            com.example.project.Services.borrowingTransactionServices.createTransactionTable(); // Create and display the transaction management table
        });

        // Handle Notification Management Button click
        notification.setOnAction(e -> {
            center.getChildren().clear(); // Clear the existing content in the center
            com.example.project.Services.notificationServices.createNotificationTable(); // Create and display the notification management table
        });

        // Handle Fine Management Button click
        fine.setOnAction(e -> {
            center.getChildren().clear();
            com.example.project.Services.fineServices.createFinesTable();
        });

        // Handle Comment Management Button click
        comment.setOnAction(e -> {
            center.getChildren().clear(); // Clear the existing content in the center
            com.example.project.Services.commentServices.createCommentsTable(); // Create and display the comment management table
        });

        // Handle Author Management Button click
        author.setOnAction(e -> {
            center.getChildren().clear(); // Clear the existing content in the center
            com.example.project.Services.authorServices.createAuthorsTable(); // Create and display the author management table
        });

        // Handle Book Management Button click
        book.setOnAction(e -> {
            center.getChildren().clear(); // Clear the existing content in the center
            bookServices.createBookTable(); // Create and display the book management table
        });

        // Handle Changing Password Button click
        changePassword.setOnAction(e -> {
            center.getChildren().clear(); // Clear the existing content in the center
            com.example.project.Services.passwordServices.changePasswordInterface(); // Create and display the change pass interface
        });

        // Change the email
        changeEmail.setOnAction(e -> {
            center.getChildren().clear(); // Clear the existing content in the center
            com.example.project.Services.emailServices.changeEmailInterface(); // change email interface
        });

        // sign out button that leads to the homepage
        signOut.setOnAction(e -> userServices.signOut(primaryStage));

        // Set up the layout for the scene, using GridPane for organizing components
        GridPane layout = new GridPane();

        // Align the top component to the right within its cell
        GridPane.setHalignment(top, HPos.RIGHT);

        // Add components to the GridPane
        layout.add(left, 0, 0, 1, 2); // Left column (spans two rows)
        layout.add(top, 1, 0); // Top-right bar (first row, second column)
        layout.add(center, 1, 1); // Center content area (second row, second column)

        // Create the scene with the layout and set it on the primary stage
        Scene scene = new Scene(layout);
        primaryStage.setScene(scene); // Set the scene for the primaryStage
        primaryStage.setTitle("Settings"); // Set the title of the application window
        primaryStage.setMaximized(true); // Set the window to be maximized by default
        primaryStage.show(); // Display the window to the user
    }
}