package com.example.project;

import com.example.project.Classes.book;
import com.example.project.HomePages.innerHomePage;
import com.example.project.Services.borrowingTransactionServices;
import com.example.project.Services.commentServices;
import com.example.project.Settings.settingUserForm;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class bookDetails{

    private static boolean isExpanded = false;
    public static VBox top;
    public static Hyperlink back = new Hyperlink("Back");
    public static boolean check = true;

    public static void displayBookDetails(Stage primaryStage, book currentBook) throws Exception {

        BorderPane layout = new BorderPane();
        // Top section
        Label title = new Label("Book Details");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 30px; -fx-text-fill: white");

        if(check){
            back = new Hyperlink("HomePage");
            back.setStyle("-fx-text-fill: white; -fx-font-weight: bold; "
                    + "-fx-focus-color: transparent; -fx-border-color: transparent; -fx-font-size: 30");

            back.setOnAction(e-> innerHomePage.displayHomePage(primaryStage));
        } else {
            back = new Hyperlink("settings");
            back.setStyle("-fx-text-fill: white; -fx-font-weight: bold; "
                    + "-fx-focus-color: transparent; -fx-border-color: transparent; -fx-font-size: 30");

            back.setOnAction(e-> settingUserForm.display(primaryStage));

        }

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        top = new VBox();
        top.setPrefHeight(100);
        top.setStyle("-fx-background-color: #425664");
        top.getChildren().addAll(new HBox(title, spacer, back));
        top.setAlignment(Pos.CENTER_LEFT);
        top.setPadding(new Insets(25, 25, 25, 50));

        // Set initial layout with original center
        layout.setTop(top);
        layout.setCenter(originalCenter(currentBook, layout));

        // Create the scene and set it to the stage
        Scene scene = new Scene(layout); // Adjust size if needed
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true); // Enables maximized window mode with standard controls
        primaryStage.show();
    }

    public static GridPane originalCenter(book currentBook, BorderPane layout) {
        GridPane center = new GridPane();
        center.setHgap(10);
        center.setVgap(10);
        center.setStyle("-fx-background-color: #F6F4F2");
        center.setPadding(new Insets(20, 25, 20, 25));

        HBox stars = new HBox();
        stars.setPadding(new Insets(10, 0, 10, 0));

        // Stars and borrow button
        int rate = currentBook.getRate();
        for (int i = 0; i < 5; i++) {
            Polygon star = createStar();
            if (i < rate) {
                star.setFill(Color.web("#C6AD8F")); // Filled star color
                rate--;
            } else {
                star.setFill(Color.web("#F6F4F2")); // Empty star color
            }
            stars.getChildren().add(star);
        }

        center.add(stars, 0, 1);

        Button borrow = new Button("Borrow");
        borrow.setStyle("-fx-text-fill: #C6AD8F; -fx-font-weight: bold; -fx-font-size: 20px; "
                + "-fx-background-radius: 15; -fx-border-radius: 15; -fx-border-color: #C6AD8F; "
                + "-fx-background-color: #425664; -fx-border-width: 4px;");
        borrow.setPrefSize(200, 50);
        center.add(borrow, 0, 1);
        GridPane.setMargin(borrow, new Insets(30, 10, 5, 50));

        // Image section
        Image image = new Image("file:" + currentBook.getImage().getImage_path());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(350);
        imageView.setFitHeight(450);
        imageView.setPreserveRatio(true);
        imageView.setStyle("-fx-background-color: #D3D3D3");
        center.add(imageView, 0, 0);

        Text info = new Text(currentBook.toString());
        info.setWrappingWidth(580);  // Slightly smaller to fit inside the ScrollPane
        info.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: #425664;");

// Create a VBox to contain the info text and apply the brown background
        VBox infoContainer = new VBox(info);
        infoContainer.setPadding(new Insets(10));
        infoContainer.setStyle("-fx-background-color: #C6AD8F; -fx-border-radius: 10;");  // Brown background

// Create a ScrollPane to hold the infoContainer
        ScrollPane infoScrollPane = new ScrollPane(infoContainer);
        infoScrollPane.setFitToWidth(true);  // Make sure the content fits the width
        infoScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);  // Hide horizontal scrollbar
        infoScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);  // Vertical scrollbar as needed
        infoScrollPane.setPrefSize(1300, 200);  // Adjust the size as needed
        infoScrollPane.setStyle("-fx-background-color: transparent; -fx-padding: 10;");

// Add the ScrollPane to the layout
        center.add(infoScrollPane, 1, 0);

        // Bottom section with arrow button
        GridPane bottom = createBottomSection(currentBook, layout);
        center.add(bottom, 1, 1,2,1);

        borrow.setOnAction(e-> borrowingTransactionServices.addBorrow(loginOptions.getUsername(),
                currentBook));

        return center;
    }


    public static GridPane expand(book currentBook, BorderPane layout) {
        GridPane expandCenter = new GridPane();
        expandCenter.setHgap(10);
        expandCenter.setVgap(10);
        expandCenter.setStyle("-fx-background-color: #F6F4F2");
        expandCenter.setPadding(new Insets(20, 25, 20, 25));

        // Expanded components (same as original, but with larger sizes)
        Image image = new Image("file:" + currentBook.getImage().getImage_path());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(350);
        imageView.setFitHeight(450);
        imageView.setPreserveRatio(true);
        imageView.setStyle("-fx-background-color: #D3D3D3");
        expandCenter.add(imageView, 0, 0);

        HBox stars = new HBox();
        stars.setPadding(new Insets(10, 0, 10, 0));

        // Loop to add five stars to the HBox

        int rate = currentBook.getRate();
        for (int i = 0; i < 5; i++) {
            Polygon star = createStar();
            if (i < rate) {
                star.setFill(Color.web("#C6AD8F")); // Filled star color
                rate--;
            } else {
                star.setFill(Color.web("#F6F4F2")); // Empty star color
            }
            stars.getChildren().add(star);
        }

        expandCenter.add(stars, 0, 1);

        Button borrow = new Button("Borrow");
        borrow.setStyle("-fx-text-fill: #C6AD8F; -fx-font-weight: bold; -fx-font-size: 20px; "
                + "-fx-background-radius: 15; -fx-border-radius: 15; -fx-border-color: #C6AD8F; "
                + "-fx-background-color: #425664; -fx-border-width: 4px;");
        borrow.setPrefSize(200, 50);
        expandCenter.add(borrow, 0, 2);
        GridPane.setMargin(borrow, new Insets(10, 0, 10, 50));

        GridPane bottom = createBottomSection(currentBook, layout);
        bottom.setPrefHeight(350); // Adjust height for expanded view
        expandCenter.add(bottom, 1, 0, 1, 4);

        borrow.setOnAction(e-> borrowingTransactionServices.addBorrow(loginOptions.getUsername(),
                currentBook));

        return expandCenter;
    }

    private static GridPane createBottomSection(book currentBook, BorderPane layout) {
        GridPane bottom = new GridPane();
        bottom.setMaxWidth(1200);
        bottom.setPrefHeight(200);
        bottom.setStyle("-fx-border-color: #425664; -fx-border-radius: 10; -fx-background-radius: 10;");
        bottom.setVgap(10);

        // VBox to hold comments
        VBox commentsBox = new VBox(10); // 10px spacing between comments
        commentsBox.setPadding(new Insets(10));
        commentsBox.setStyle("-fx-background-color: #C6AD8F; -fx-border-radius: 10;");

        // ScrollPane to display comments
        ScrollPane com = new ScrollPane(commentsBox);
        com.setFitToWidth(true);
        com.setPrefSize(1130, isExpanded ? 500 : 120);
        com.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);  // Hide horizontal scrollbar
        com.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);  // Show vertical scrollbar when needed
        bottom.add(com, 0, 1);

        // Add existing comments using a normal for loop
        for (int i = 0; i < currentBook.getCom().size(); i++) {
            Text commentText = new Text(currentBook.getCom().get(i).toString());
            commentText.setWrappingWidth(1100);  // Prevent overflow
            commentText.setStyle("-fx-font-size: 20px; -fx-text-fill: #425664;");
            commentText.setFill(Color.web("#425664"));
            commentsBox.getChildren().add(commentText);  // Add to VBox
        }

        // TextFields and Button for new comments
        TextField add = new TextField();
        add.setPromptText("Write comment here");
        add.setStyle("-fx-text-fill: #425664");
        styleTextField(add, 700, 50);

        TextField rate = new TextField();
        rate.setPromptText("1-5");

        styleTextField(rate, 70, 50);

        Button submit = new Button("Submit");
        submit.setStyle("-fx-text-fill: #C6AD8F; -fx-font-size: 20px; -fx-background-radius: 20; "
                + "-fx-border-radius: 20; -fx-background-color: #425664;");
        submit.setPrefSize(100, 50);

        // Add new comments on submit
        submit.setOnAction(event -> {
            int book_id = currentBook.getBookid();
            String comment = add.getText();
            String temp = rate.getText();

            if(!comment.isBlank() && !temp.isBlank() ) {
                int rateting = Integer.parseInt(rate.getText());
                if(rateting > 0 && rateting <= 5){
                    commentServices.addComment(book_id, comment, rateting);

                    // Include the current date
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = now.format(formatter);

                    // Create a new Text object for the new comment and add to commentsBox
                    String commentDisplay = comment + "\nRating: " + temp + "/5\nWritten on: " + formattedDate;
                    Text newCommentText = new Text(commentDisplay);
                    newCommentText.setWrappingWidth(1100);  // Prevent overflow
                    newCommentText.setStyle("-fx-font-size: 20px; -fx-text-fill: #425664;");
                    newCommentText.setFill(Color.web("#425664"));
                    commentsBox.getChildren().add(newCommentText);
                } else {
                    loginOptions.showAlert(Alert.AlertType.ERROR, "Submission failed", "Rating must be between " +
                            "1 - 5 ");

                }

            } else {
                loginOptions.showAlert(Alert.AlertType.ERROR, "Submission failed", "Both feilds must be " +
                        "entered ");

            }
            add.clear();
            rate.clear();
        });

        HBox row = new HBox(10, add, rate, submit);
        row.setPadding(new Insets(10));
        bottom.add(row, 0, 2);

        // Arrow button for expand/collapse
        Button arrowUp = new Button(isExpanded ? "▼" : "▲");
        arrowUp.setStyle("-fx-background-color: transparent; -fx-font-size: 16px; -fx-text-fill: #425664;");
        arrowUp.setPrefSize(50, 50);
        arrowUp.setOnAction(event -> toggleCenter(arrowUp, currentBook, layout));
        bottom.add(arrowUp, 0, 0);
        GridPane.setHalignment(arrowUp, HPos.CENTER);
        GridPane.setMargin(arrowUp, new Insets(0, 0, 5, 0));

        return bottom;
    }



    private static void toggleCenter(Button arrowUp,book currentBook, BorderPane layout) {
        isExpanded = !isExpanded; // Toggle the state
        layout.setCenter(isExpanded ? expand( currentBook, layout) : originalCenter(currentBook, layout)); // Switch layouts
        arrowUp.setText(isExpanded ? "▲" : "▼"); // Update arrow text
    }

    private static void styleTextField(TextField textField, int width, int height) {
        textField.setStyle("-fx-text-fill: #425664; -fx-font-size: 20px; "
                + "-fx-background-radius: 15; -fx-border-radius: 15; "
                + "-fx-border-color: #425664; -fx-background-color: #C6AD8F;");
        textField.setPrefSize(width, height);
    }


    public static Polygon createStar() {
        Polygon star = new Polygon(
                30.0, 0.0,
                36.0, 21.0,
                58.0, 21.0,
                40.0, 35.0,
                48.0, 58.0,
                30.0, 45.0,
                12.0, 58.0,
                20.0, 35.0,
                2.0, 21.0,
                24.0, 21.0
        );
        star.setStroke(Color.web("#C6AD8F"));
        star.setStrokeWidth(2);
        return star;
    }
}