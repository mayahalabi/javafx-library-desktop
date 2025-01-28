package com.example.project.Classes;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * Represents a borrowing transaction in the library system.
 * This class stores details about a book borrowing transaction,
 * including the issue date, due date, return date, and the user involved.
 */
public class borrowing_transaction {
    private int transaction_id;
    private Timestamp issue_date;
    private Timestamp due_date;
    private Timestamp return_date;
    private String username;
    private int book_id;

    /**
     * Constructor to initialize a BorrowingTransaction object with all fields.
     *
     * @param transaction_id The unique identifier for the transaction.
     * @param due_date The due date for the return of the book.
     * @param return_date The return date of the book, can be null if not returned yet.
     * @param username The username of the borrower.
     * @param book_id The ID of the book being borrowed.
     */
    public borrowing_transaction(int transaction_id, Timestamp due_date, Timestamp return_date, String username, int book_id) {
        this.transaction_id = transaction_id;
        this.issue_date = Timestamp.from(Instant.now());  // Automatically set the current issue date
        this.due_date = due_date;
        this.return_date = return_date;
        this.username = username;
        this.book_id = book_id;
    }

    /**
     * Constructor to initialize a BorrowingTransaction object with only the return date as null.
     *
     * @param transaction_id The unique identifier for the transaction.
     * @param due_date The due date for the return of the book.
     * @param username The username of the borrower.
     * @param book_id The ID of the book being borrowed.
     */
    public borrowing_transaction(int transaction_id, Timestamp due_date, String username, int book_id) {
        this(transaction_id, due_date, null,username, book_id);
        this.issue_date = Timestamp.from(Instant.now());
    }

    // Getters and setters

    /**
     * Returns the unique identifier of the transaction.
     *
     * @return The transaction's unique identifier.
     */
    public int getTransaction_id() {
        return transaction_id;
    }

    /**
     * Sets the unique identifier for the transaction.
     *
     * @param transaction_id The unique identifier to set for the transaction.
     */
    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    /**
     * Returns the issue date as a string.
     *
     * @return The issue date of the transaction as a string.
     */
    public String getIssue_date() {
        return issue_date.toString();
    }

    /**
     * Sets the issue date of the transaction.
     *
     * @param issue_date The issue date to set for the transaction.
     */
    public void setIssue_date(Timestamp issue_date) {
        this.issue_date = issue_date;
    }

    /**
     * Returns the due date as a string.
     *
     * @return The due date of the transaction as a string.
     */
    public String getDue_date() {
        return due_date.toString();
    }

    /**
     * Sets the due date of the transaction.
     *
     * @param due_date The due date to set for the transaction.
     */
    public void setDue_date(Timestamp due_date) {
        this.due_date = due_date;
    }

    /**
     * Returns the return date of the transaction.
     *
     * @return The return date of the book, or null if not returned.
     */
    public Timestamp getReturn_date() {
        return return_date;
    }

    /**
     * Sets the return date for the transaction.
     *
     * @param return_date The return date to set for the transaction.
     */
    public void setReturn_date(Timestamp return_date) {
        this.return_date = return_date;
    }

    /**
     * Returns the username of the borrower.
     *
     * @return The username of the borrower.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the borrower.
     *
     * @param username The username to set for the borrower.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the ID of the book being borrowed.
     *
     * @return The book's unique identifier.
     */
    public int getBook_id() {
        return book_id;
    }

    /**
     * Sets the ID of the book being borrowed.
     *
     * @param book_id The unique identifier of the book to set.
     */
    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }
}

