package com.example.project.Classes;

import java.util.List;

/**
 * Represents a book in the library system.
 * Contains details about the book such as title, author, ISBN, publisher, and more.
 */
public class book {
    private int bookid;
    private int quantity;
    private String status;
    private author author;
    private images image;
    private String title;
    private String isbn;
    private String publisher;
    private String publishedDate;
    private String description;
    private int rate;
    private List<comment> com;
    private List<genre> genres;

    /**
     * Constructor to initialize a Book object with the specified attributes.
     *
     * @param bookid The unique identifier for the book.
     * @param author The author of the book.
     * @param title The title of the book.
     * @param isbn The ISBN number of the book.
     * @param publisher The publisher of the book.
     * @param publishedDate The publication date of the book.
     * @param image The image of the book (e.g., cover image).
     * @param description A brief description or summary of the book.
     * @param status The status of the book (e.g., available, checked out).
     * @param quantity The quantity of the book available in the library.
     * @param rate The rating of the book (e.g., from 1 to 5).
     */
    public book(int bookid, author author, String title, String isbn,
                String publisher, String publishedDate, images image,
                String description, String status, int quantity, int rate) {
        this.bookid = bookid;
        this.author = author;
        this.title = title;
        this.isbn = isbn;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.image = image;
        this.description = description;
        this.status = status;
        this.quantity = quantity;
        this.rate = rate;
        this.com = com;
    }

    /**
     * Default constructor that initializes the Book object with default values.
     */
    public book() {
        this(0, new author(), "", "", "",
                "", new images(), "", "", 0,0);
    }

    // Getter and setter methods for the fields
    public int getBookid() {
        return bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public author getAuthor() {
        return author;
    }

    public void setAuthor(author author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public images getImage() {
        return image;
    }

    public void setImage(images image) {
        this.image = image;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public void setCom(List<comment> com) {
        this.com = com;
    }

    public List<comment> getCom() {
        return com;
    }

    public void setAuthors(List<genre> genres) {
        this.genres = genres;
    }

    public List<genre> getGenres() {
        return genres;
    }

    public void setGenres(List<genre> genres) {
        this.genres = genres;
    }

    /**
     * Returns a string representation of the book with key details.
     * Includes the title, author, publisher, status, genres, and description.
     *
     * @return A formatted string with the book's details.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append( "Title: " + title + "\nAuthor: " + author + "\nPublished year: " + publishedDate +
                "\nPublisher: " + publisher + "\nStatus: " + status +
                "\nISBN: " + isbn);

        if (genres != null && !genres.isEmpty()) {
            result.append("\nGenres: ");
            for (int i = 0; i < genres.size(); i++) {
                result.append(genres.get(i).getType());

                // Append a comma and space if it is not the last element
                if (i < genres.size() - 1) {
                    result.append(", ");
                }
            }
        } else {
            result.append("No genres available");
        }

        result.append("\n\nDescription: " + getDescription());
        return result.toString();

    }
}
