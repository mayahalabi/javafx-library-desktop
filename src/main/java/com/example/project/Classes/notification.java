package com.example.project.Classes;

import java.sql.Timestamp;

/**
 * Represents a notification for a user regarding a book transaction,
 * such as a reminder to return a book or a fine notification.
 */
public class notification {
    private int notificationId;
    private Timestamp reminderDate;
    private String username;
    private int book_id;
    private int fine_id;
    private String message;

    /**
     * Constructor to initialize a notification with the given details.
     *
     * @param notificationId The unique ID of the notification.
     * @param reminderDate The timestamp of when the reminder is set.
     * @param username The username of the person receiving the notification.
     * @param book_id The ID of the book related to the notification.
     * @param fine_id The ID of the fine related to the notification (if applicable).
     */
    public notification(int notificationId, Timestamp reminderDate, String username, int book_id, int fine_id) {
        this.notificationId = notificationId;
        this.reminderDate = reminderDate;
        this.username = username;
        this.book_id = book_id;
        this.fine_id = fine_id;
        this.message = "Dont forget to return the book!";
    }

    // Getters and Setters

    /**
     * Gets the unique identifier for the notification.
     *
     * @return The notification ID.
     */
    public int getNotificationId() {
        return notificationId;
    }

    /**
     * Sets the unique identifier for the notification.
     *
     * @param notificationId The notification ID to set.
     */
    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    /**
     * Gets the reminder date of the notification as a string.
     *
     * @return The reminder date as a string.
     */
    public String getReminderDate() {
        return reminderDate.toString();
    }

    /**
     * Sets the reminder date of the notification.
     *
     * @param reminderDate The reminder date to set.
     */
    public void setReminderDate(Timestamp reminderDate) {
        this.reminderDate = reminderDate;
    }

    /**
     * Gets the username of the person receiving the notification.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the person receiving the notification.
     *
     * @param username The username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the ID of the book related to the notification.
     *
     * @return The book ID.
     */
    public int getBook_id() {
        return book_id;
    }

    /**
     * Sets the ID of the book related to the notification.
     *
     * @param book_id The book ID to set.
     */
    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    /**
     * Gets the ID of the fine associated with the notification.
     *
     * @return The fine ID.
     */
    public int getFine_id() {
        return fine_id;
    }

    /**
     * Sets the ID of the fine associated with the notification.
     *
     * @param fine_id The fine ID to set.
     */
    public void setFine_id(int fine_id) {
        this.fine_id = fine_id;
    }

    /**
     * Gets the message of the notification.
     *
     * @return The message of the notification.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message of the notification.
     *
     * @param message The message to set for the notification.
     */
    public void setMessage(String message) {
        this.message = message;
    }
}