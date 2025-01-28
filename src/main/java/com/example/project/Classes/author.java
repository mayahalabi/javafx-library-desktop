package com.example.project.Classes;

/**
 * Represents an author of a book in the library system.
 * Contains the author's ID, first name, and last name.
 */
public class author {
    private int author_id;
    private String first_name;
    private String last_name;

    /**
     * Constructor to initialize an Author with an ID, first name, and last name.
     *
     * @param author_id The unique identifier for the author.
     * @param first_name The first name of the author.
     * @param last_name The last name of the author.
     */
    public author(int author_id, String first_name, String last_name) {
        this.author_id = author_id;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    /**
     * Default constructor that initializes the author with default values.
     * (author_id = 0, first_name = empty, last_name = empty)
     */
    public author() {
        this(0, "", "");
    }

    /**
     * Gets the author's unique identifier.
     *
     * @return The author's ID.
     */
    public int getAuthor_id() {
        return author_id;
    }

    /**
     * Sets the author's unique identifier.
     *
     * @param author_id The author's new ID.
     */
    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    /**
     * Gets the author's first name.
     *
     * @return The first name of the author.
     */
    public String getFirst_name() {
        return first_name;
    }

    /**
     * Sets the author's first name.
     *
     * @param first_name The first name to set for the author.
     */
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    /**
     * Gets the author's last name.
     *
     * @return The last name of the author.
     */
    public String getLast_name() {
        return last_name;
    }

    /**
     * Sets the author's last name.
     *
     * @param last_name The last name to set for the author.
     */
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }


    /**
     * Returns a string representation of the author, combining their first and last names.
     *
     * @return A string representation of the author's full name (first name + last name).
     */
    @Override
    public String toString() {
        return first_name + " " + last_name;
    }

}