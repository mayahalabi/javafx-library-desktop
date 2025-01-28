package com.example.project.HomePages;

import com.example.project.Services.bookServices;
import com.example.project.Services.searchServices;
import com.example.project.Services.userServices;
import com.example.project.Settings.settingAdminForm;
import com.example.project.Settings.settingUserForm;
import com.example.project.Classes.book;
import com.example.project.bookDetails;
import com.example.project.loginOptions;
import com.example.project.Classes.user;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * The innerHomePage class handles the display and functionality of the user's home page in the BookWave application.
 */
public class innerHomePage {

    /**
     * Displays the home page for the user, including a title, search bar, and a scrollable list of books.
     *
     * @param primaryStage the main application window
     */
    public static void displayHomePage(Stage primaryStage) {
        // Create a GridPane for the main layout
        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-color: #425664");

        // Title section
        HBox titleBox = new HBox();
        Label title = new Label("  BookWave");
        title.setStyle(" -fx-text-fill: #425664; -fx-font-family: 'Inter'; -fx-font-size: 32px; -fx-font-weight: bold;");
        titleBox.getChildren().add(title); // Add title to the title box

        // Create a search bar for book searches
        TextField searchBar = new TextField();
        searchBar.setPromptText("Search");
        searchBar.setPrefWidth(1100);
        searchBar.setStyle("-fx-text-fill: #696969; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 18px; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #D3D3D3; " +
                "-fx-background-color: #D3D3D3;");

        // Create an account button for user account settings
        Button accountButton = new Button("My Account");
        accountButton.setStyle("-fx-font-family: 'Inter';" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-color: #425664;" +
                "-fx-text-fill: #C6AD8F;" +
                "-fx-background-radius: 10;" +
                "-fx-border-radius: 10;");
        accountButton.setPrefWidth(450);

        // HBox to contain the search bar and account button
        HBox searchBox = new HBox(10);
        searchBox.setStyle("-fx-padding: 10;");
        searchBox.getChildren().addAll(searchBar, accountButton);

        // Main layout using VBox to hold the title and search box
        VBox root = new VBox(10);
        root.getChildren().addAll(titleBox, searchBox);

        // Initialize the ScrollPane to display books
        ScrollPane bookScroll = bookServices.displayBookScrollPane();
        VBox mainLayout = new VBox(20);
        VBox.setVgrow(bookScroll, Priority.ALWAYS); // Allow the bookScroll to grow
        mainLayout.getChildren().addAll(root, bookScroll);
        mainLayout.setStyle("-fx-padding: 20px;");

        // Action to handle search queries
        searchBar.setOnAction(e -> {
            String searchText = searchBar.getText().trim(); // Get the search text
            bookScroll.setContent(null);  // Clear previous search results

            // If the search bar is not empty, perform a search
            if (!searchText.isBlank()) {
                GridPane searchGrid = searchServices.getSearchGrid(searchText);
                bookScroll.setContent(searchGrid); // Display search results
                setBookCoverActions(bookScroll, primaryStage);  // Attach actions to search results
            } else {
                GridPane defaultGrid = bookServices.getBookGrid();
                bookScroll.setContent(defaultGrid); // Reset to the default book display
                setBookCoverActions(bookScroll, primaryStage);  // Attach actions to default grid
            }
        });

        // Attach actions to the initial book covers
        setBookCoverActions(bookScroll, primaryStage);

        // Action for account button
        accountButton.setOnAction(e -> {
            user info = userServices.getUserInformation(loginOptions.getUsername()); // Get user info
            System.out.println(info.getRole());
            // Display the appropriate settings form based on user role
            if (info != null && info.getRole().equals("user")) {
                settingUserForm.display(primaryStage); // Open user settings
            } else {
                settingAdminForm.display(primaryStage); // Open admin settings
            }
        });

        // Create and display the scene with the main layout
        Scene scene = new Scene(mainLayout);
        primaryStage.setTitle("Welcome Back to BookWave");
        primaryStage.setScene(scene); // Set the scene on the primary stage
        primaryStage.setMaximized(true); // Enables maximized window mode with standard controls
        primaryStage.show(); // Show the primary stage
    }

    /**
     * Attaches click actions to each book cover in the provided ScrollPane.
     * When a book cover is clicked, the details of the book will be displayed.
     *
     * @param bookScroll the ScrollPane containing the book covers
     * @param primaryStage the main application window
     */
    public static void setBookCoverActions(ScrollPane bookScroll, Stage primaryStage) {
        Node content = bookScroll.getContent();

        // Check if the content of the ScrollPane is a GridPane
        if (content instanceof GridPane) {
            GridPane bookGrid = (GridPane) content;

            // Iterate through all children of the GridPane to set actions
            for (int i = 0; i < bookGrid.getChildren().size(); i++) {
                Node node = bookGrid.getChildren().get(i);

                // Check if the node is an ImageView (book cover)
                if (node instanceof ImageView) {
                    ImageView bookCover = (ImageView) node;
                    book currentBook = (book) bookCover.getUserData(); // Get associated book object

                    // Attach click event to display book details
                    bookCover.setOnMouseClicked(e -> {
                        try {
                            System.out.println("Clicked on: " + currentBook.getTitle());
                            bookDetails.displayBookDetails(primaryStage, currentBook); // Display book details
                        } catch (Exception ex) {
                            ex.printStackTrace(); // Print stack trace for debugging
                        }
                    });
                }
            }
        } else {
            System.err.println("The content of the ScrollPane is not a GridPane."); // Error handling
        }
    }
}