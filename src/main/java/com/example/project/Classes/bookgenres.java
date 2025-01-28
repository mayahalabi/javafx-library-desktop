package com.example.project.Classes;

/**
 * Represents a mapping between a book and a genre in the library system.
 * This class associates a book with a specific genre.
 */
public class bookgenres {
    private int book_id;
    private int genre_id;

    /**
     * Constructor to initialize a BookGenres object with specified book_id and genre_id.
     *
     * @param book_id The unique identifier of the book.
     * @param genre_id The unique identifier of the genre.
     */
    public bookgenres(int book_id, int genre_id) {
        this.book_id = book_id;
        this.genre_id = genre_id;
    }

    /**
     * Default constructor initializing with default values (0 for both book_id and genre_id).
     */
    public bookgenres(){
        this(0, 0);
    }

    // Getters and setters

    /**
     * Returns the unique identifier of the book.
     *
     * @return The book's unique identifier.
     */
    public int getBook_id() {
        return book_id;
    }

    /**
     * Sets the unique identifier of the book.
     *
     * @param book_id The unique identifier to set for the book.
     */
    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    /**
     * Returns the unique identifier of the genre.
     *
     * @return The genre's unique identifier.
     */
    public int getGenre_id() {
        return genre_id;
    }

    /**
     * Sets the unique identifier of the genre.
     *
     * @param genre_id The unique identifier to set for the genre.
     */
    public void setGenre_id(int genre_id) {
        this.genre_id = genre_id;
    }
}
