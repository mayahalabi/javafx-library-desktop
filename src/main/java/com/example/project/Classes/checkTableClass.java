package com.example.project.Classes;

import java.sql.Timestamp;

/**
 * Represents the information to be displayed in a table for a specific borrowing transaction.
 * This class is designed to store and provide details about the transaction, such as
 * the book's title, issue date, due date, and return date along with the user's information.
 */
public class checkTableClass {
    private String username;
    private int bookId;
    private String title;
    private Timestamp issueDate;
    private Timestamp dueDate;
    private Timestamp returnDate;

    /**
     * Constructor to initialize a CheckTableClass object with all fields.
     *
     * @param username The username of the borrower.
     * @param bookId The ID of the book being borrowed.
     * @param title The title of the borrowed book.
     * @param issueDate The issue date when the book was borrowed.
     * @param dueDate The due date for returning the book.
     * @param returnDate The return date when the book was returned.
     */
    public checkTableClass(String username, int bookId, String title, Timestamp issueDate, Timestamp dueDate, Timestamp returnDate) {
        this.username = username;
        this.bookId = bookId;
        this.title = title;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
    }

    // Getters and setters

    /**
     * Returns the username of the borrower.
     *
     * @return The username of the borrower.
     */
    public String getUsername() { return username; }

    /**
     * Sets the username of the borrower.
     *
     * @param username The username to set for the borrower.
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * Returns the ID of the borrowed book.
     *
     * @return The unique identifier for the borrowed book.
     */
    public int getBookId() { return bookId; }

    /**
     * Sets the ID of the borrowed book.
     *
     * @param bookId The unique identifier for the borrowed book.
     */
    public void setBookId(int bookId) { this.bookId = bookId; }

    /**
     * Returns the title of the borrowed book.
     *
     * @return The title of the borrowed book.
     */
    public String getTitle() { return title; }

    /**
     * Sets the title of the borrowed book.
     *
     * @param title The title of the borrowed book.
     */
    public void setTitle(String title) { this.title = title; }

    /**
     * Returns the issue date of the book.
     *
     * @return The issue date of the borrowed book.
     */
    public Timestamp getIssueDate() { return issueDate; }

    /**
     * Sets the issue date of the book.
     *
     * @param issueDate The issue date to set for the book.
     */
    public void setIssueDate(Timestamp issueDate) { this.issueDate = issueDate; }

    /**
     * Returns the due date for the book's return.
     *
     * @return The due date for returning the borrowed book.
     */
    public Timestamp getDueDate() { return dueDate; }

    /**
     * Sets the due date for the book's return.
     *
     * @param dueDate The due date to set for the return of the book.
     */
    public void setDueDate(Timestamp dueDate) { this.dueDate = dueDate; }

    /**
     * Returns the return date of the book.
     *
     * @return The return date when the book was actually returned, or null if not returned yet.
     */
    public Timestamp getReturnDate() { return returnDate; }

    /**
     * Sets the return date of the book.
     *
     * @param returnDate The return date to set for the book.
     */
    public void setReturnDate(Timestamp returnDate) { this.returnDate = returnDate; }
}
