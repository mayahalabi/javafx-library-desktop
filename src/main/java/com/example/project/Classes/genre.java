package com.example.project.Classes;

/**
 * Represents a genre associated with books in the library.
 * Each genre has a unique ID and a type (name or category).
 */
public class genre {
    private int genre_id;
    private String type;

    /**
     * Constructor to initialize a genre with the given ID and type.
     *
     * @param genre_id The unique identifier for the genre.
     * @param type The type or name of the genre (e.g., "Fiction", "Non-Fiction").
     */
    public genre(int genre_id,String type) {
        this.genre_id = genre_id;
        this.type = type;
    }

    // Getters and setters for each field

    /**
     * Returns the unique identifier for the genre.
     *
     * @return The genre ID.
     */
    public int getGenre_id() {
        return genre_id;
    }

    /**
     * Sets the unique identifier for the genre.
     *
     * @param genre_id The genre ID to set.
     */
    public void setGenre_id(int genre_id) {
        this.genre_id = genre_id;
    }

    /**
     * Returns the name or category of the genre.
     *
     * @return The genre type.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type or name of the genre.
     *
     * @param type The genre type to set (e.g., "Fiction", "Biography").
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns a string representation of the genre.
     *
     * @return The genre's type.
     */
    @Override
    public String toString() {
        return type ;
    }
}

