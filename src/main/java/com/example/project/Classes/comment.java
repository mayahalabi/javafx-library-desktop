package com.example.project.Classes;

/**
 * Represents a comment for a book, including the comment description, rating,
 * and associated book ID along with the date the comment was written.
 */
public class comment {
    private int comment_id;
    private String comment_date;
    private String comment_description; // Corrected spelling
    private int rating;
    private int book_id;

    /**
     * Constructor to initialize all fields of the comment.
     *
     * @param comment_id The unique identifier for the comment.
     * @param comment_date The date when the comment was made.
     * @param comment_description The text content of the comment.
     * @param rating The rating provided for the book (1-5).
     * @param book_id The ID of the book that the comment relates to.
     */
    public comment(int comment_id, String comment_date, String comment_description, int rating, int book_id) {
        this.comment_id = comment_id;
        this.comment_date = comment_date;
        this.comment_description = comment_description; // Consistent spelling
        this.rating = rating;
        this.book_id = book_id;
    }

    /**
     * Constructor to initialize the comment with default rating of 0.
     *
     * @param comment_id The unique identifier for the comment.
     * @param comment_date The date when the comment was made.
     * @param comment_description The text content of the comment.
     * @param book_id The ID of the book that the comment relates to.
     */
    public comment(int comment_id, String comment_date, String comment_description, int book_id) {
        this(comment_id, comment_date, comment_description, 0, book_id);
    }

    // Getters and setters for each field

    /**
     * Returns the unique identifier for the comment.
     *
     * @return The comment ID.
     */
    public int getComment_id() { return comment_id; }

    /**
     * Sets the unique identifier for the comment.
     *
     * @param comment_id The comment ID to set.
     */
    public void setComment_id(int comment_id) { this.comment_id = comment_id; }

    /**
     * Returns the date when the comment was written.
     *
     * @return The comment date.
     */
    public String getComment_date() { return comment_date; }

    /**
     * Sets the date when the comment was written.
     *
     * @param comment_date The date to set for the comment.
     */
    public void setComment_date(String comment_date) { this.comment_date = comment_date; }

    /**
     * Returns the description (text content) of the comment.
     *
     * @return The comment description.
     */
    public String getComment_description() { return comment_description; }

    /**
     * Sets the description (text content) of the comment.
     *
     * @param comment_description The description to set for the comment.
     */
    public void setComment_description(String comment_description) { this.comment_description = comment_description; } // Consistent spelling

    /**
     * Returns the rating given to the book in the comment.
     *
     * @return The rating for the book.
     */
    public int getRating() { return rating; }

    /**
     * Sets the rating for the book in the comment.
     *
     * @param rating The rating to set for the book (usually between 1 to 5).
     */
    public void setRating(int rating) { this.rating = rating; }

    /**
     * Returns the book ID associated with the comment.
     *
     * @return The book ID that the comment relates to.
     */
    public int getBook_id() { return book_id; }

    /**
     * Sets the book ID associated with the comment.
     *
     * @param book_id The ID of the book to associate with this comment.
     */
    public void setBook_id(int book_id) { this.book_id = book_id; }

    /**
     * Returns a string representation of the comment, including the description, rating, and the date it was written.
     *
     * @return A formatted string with comment description, rating, and date.
     */
    @Override
    public String toString() {
        return comment_description + "\nrating: " + rating + "\nwritten on: " + comment_date; // Consistent spelling
    }
}