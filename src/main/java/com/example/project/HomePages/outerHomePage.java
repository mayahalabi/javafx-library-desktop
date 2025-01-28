package com.example.project.HomePages;

import com.example.project.Services.bookServices;
import com.example.project.Services.searchServices;
import com.example.project.loginOptions;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * The outerHomePage class represents the main home page for the BookWave application.
 * It provides a search functionality, buttons for user actions, and displays information about the application.
 */
public class outerHomePage extends Application {
    // TextField for searching books
    private TextField searchBox = new TextField("Search");

    /**
     * The start method sets up the primary stage and the user interface components.
     *
     * @param primaryStage the main application window
     */
    public void start(Stage primaryStage) {

        // Title of the application
        Label title = new Label("BookWave");
        title.setPadding(new Insets(10, 10, 10, 10));
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 35px; -fx-text-fill: #425664;");

        // Styling for the search box
        searchBox.setStyle("-fx-text-fill: #696969; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 20px; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #D3D3D3; " +
                "-fx-background-color: #D3D3D3;");
        searchBox.setPrefWidth(1100); // Set preferred width for the search box

        // Sign In button with styling
        Button signIn = new Button("SignIn");
        signIn.setStyle("-fx-text-fill: #C6AD8F; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 20px; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #425664; " +
                "-fx-background-color: #425664");
        signIn.setPrefWidth(150); // Set preferred width of 150 pixels
        signIn.setMinWidth(150);  // Ensure button does not shrink smaller than 150 pixels
        signIn.setMaxWidth(150);

        // Sign Up button with styling
        Button signUp = new Button("SignUp");
        signUp.setStyle("-fx-text-fill: #C6AD8F; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 20px; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #425664; " +
                "-fx-background-color: #425664");
        signUp.setPrefWidth(150); // Set preferred width of 150 pixels
        signUp.setMinWidth(150);  // Ensure button does not shrink smaller than 150 pixels
        signUp.setMaxWidth(150);

        // HBox to hold the Sign In and Sign Up buttons
        HBox hbox = new HBox(signIn, signUp);
        hbox.setAlignment(Pos.CENTER); // Center align the buttons
        hbox.setSpacing(10); // Set spacing between buttons

        // Top GridPane to hold the title, search box, and action buttons
        GridPane top = new GridPane();
        top.setPadding(new Insets(10, 10, 10, 10));
        top.setStyle("-fx-background-color: #F6F4F2");
        top.setPrefHeight(100);
        top.setPadding(new Insets(25));
        top.add(title, 0, 0); // Add title to the first row
        top.add(searchBox, 0, 1); // Add search box to the second row
        top.add(hbox, 1, 1); // Add buttons to the second row, second column
        GridPane.setMargin(hbox, new Insets(0, 0, 0, 20)); // Set margin for the button box

        // Create a ScrollPane to display books
        ScrollPane middle = bookServices.displayBookScrollPane();

        // Action for search box to update displayed books
        searchBox.setOnAction(e -> {
            middle.setContent(null); // Clear current content of the ScrollPane
            if (!searchBox.getText().isBlank()) { // Check if search box is not empty
                middle.setContent(searchServices.displaySearchScrollPane(searchBox.getText())); // Update with search results
            } else {
                middle.setContent(bookServices.displayBookScrollPane()); // Reset to original book display
            }
        });

        // About section
        Label about = new Label("About Us");
        about.setStyle("-fx-font-weight: bold; -fx-text-fill: #425664; -fx-font-size: 30px;");

        // Description text about the application
        Text text = new Text("At BookWave, we carefully curate our library " +
                "to inspire and entertain readers.\nWe aim to create an " +
                "inclusive and welcoming environment for all.\n" +
                "Our collection spans bestsellers to timeless classics.\n" +
                "Join us in exploring the endless possibilities of the written word.");

        text.setStyle("-fx-font-family: 'Inter'; -fx-font-size: 15px;"); // Set font style for description
        text.setFill(javafx.scene.paint.Color.web("#425664")); // Set text color

        // Load and display an image
        Image image = new Image("file:C:\\Users\\mayam\\Downloads\\read.png\\");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(150); // Set image height
        imageView.setFitWidth(150); // Set image width

        // Layout for the About section
        GridPane layout = new GridPane();
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(70); // Set first column to take 70% width
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(30); // Set second column to take 30% width
        layout.getColumnConstraints().addAll(col1, col2); // Add column constraints
        layout.setStyle("-fx-background-color: #C6AD8F; -fx-background-radius: 15");
        layout.setPadding(new Insets(15, 15, 15, 15)); // Set padding for the layout
        layout.setHgap(10); // Set horizontal gap between elements
        layout.setVgap(10); // Set vertical gap between elements
        layout.add(about, 0, 0); // Add about label to the layout
        layout.add(text, 0, 1); // Add description text to the layout
        layout.add(imageView, 1, 0, 2, 2); // Add image view to the layout
        GridPane.setHalignment(imageView, HPos.RIGHT); // Align image to the right

        // Button actions for Sign In and Sign Up
        signIn.setOnAction(e -> loginOptions.signIn(primaryStage)); // Navigate to sign in
        signUp.setOnAction(e -> loginOptions.signUp(primaryStage)); // Navigate to sign up

        // Bottom StackPane to hold the About section layout
        StackPane bottom = new StackPane();
        bottom.setStyle("-fx-background-color: #F6F4F2");
        bottom.setAlignment(Pos.CENTER); // Center align the content
        bottom.setPadding(new Insets(10));
        bottom.setPrefHeight(170); // Set preferred height
        bottom.getChildren().addAll(layout); // Add layout to the bottom

        // Root layout for the scene
        BorderPane root = new BorderPane();
        root.setTop(top); // Set top layout
        root.setCenter(middle); // Set center ScrollPane
        root.setBottom(bottom); // Set bottom About section
        Scene scene = new Scene(root); // Set scene size
        primaryStage.setScene(scene); // Set the scene on the primary stage

        //primaryStage.setFullScreen(true); // Enables full screen mode
        primaryStage.setMaximized(true); // Enables maximized window mode with standard controls
        primaryStage.show(); // Display the stage
    }

    /**
     * The main method that launches the application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}
