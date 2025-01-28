package com.example.project.Classes;

/**
 * Represents a fine associated with a borrowing transaction.
 * This includes the fine amount, status, and the transaction ID the fine is related to.
 */
public class fines {
    private int fine_id;
    private double fine_amount;
    private String fine_status;
    private int transaction_id;

    /**
     * Constructor to initialize a fine with all necessary details.
     *
     * @param fine_id The unique identifier for the fine.
     * @param fine_amount The amount of the fine.
     * @param fine_status The status of the fine (e.g., "Paid" or "Unpaid").
     * @param transaction_id The ID of the transaction the fine is associated with.
     */
    public fines(int fine_id, double fine_amount, String fine_status, int transaction_id) {
        this.fine_id = fine_id;
        this.fine_amount = fine_amount;
        this.fine_status = fine_status;
        this.transaction_id = transaction_id;
    }

    // Getters and setters for each field

    /**
     * Returns the unique identifier for the fine.
     *
     * @return The fine ID.
     */
    public int getFine_id() {
        return fine_id;
    }

    /**
     * Sets the unique identifier for the fine.
     *
     * @param fine_id The fine ID to set.
     */
    public void setFine_id(int fine_id) {
        this.fine_id = fine_id;
    }

    /**
     * Returns the amount of the fine.
     *
     * @return The fine amount.
     */
    public double getFine_amount() {
        return fine_amount;
    }

    /**
     * Sets the amount of the fine.
     *
     * @param fine_amount The fine amount to set.
     */
    public void setFine_amount(double fine_amount) {
        this.fine_amount = fine_amount;
    }

    /**
     * Returns the status of the fine.
     *
     * @return The fine status.
     */
    public String getFine_status() {
        return fine_status;
    }

    /**
     * Sets the status of the fine.
     *
     * @param fine_status The fine status to set (e.g., "Paid" or "Unpaid").
     */
    public void setFine_status(String fine_status) {
        this.fine_status = fine_status;
    }

    /**
     * Returns the ID of the transaction associated with the fine.
     *
     * @return The transaction ID.
     */
    public int getTransaction_id() {
        return transaction_id;
    }

    /**
     * Sets the ID of the transaction associated with the fine.
     *
     * @param transaction_id The transaction ID to associate with the fine.
     */
    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    /**
     * Returns a string representation of the fine, including its ID, amount, status, and associated transaction ID.
     *
     * @return A string representing the fine details.
     */
    @Override
    public String toString() {
        return "Fine ID: " + fine_id + "\nAmount: " + fine_amount + "\nStatus: " + fine_status + "\nTransaction ID: " + transaction_id;
    }
}
