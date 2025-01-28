package com.example.project.Classes;

/**
 * Represents a book along with its author, image, and genre information in the library system.
 * Contains all necessary details about the book, its author, image, and genre.
 */
public class BookAuthorImage {
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

    private int image_id;
    private String image_path;

    private int author_id;
    private String first_name;
    private String last_name;

    private int genre_id;
    private String type;

    /**
     * Constructor to initialize a BookAuthorImage object with all fields.
     *
     * @param bookid The unique identifier for the book.
     * @param quantity The quantity of the book available in the library.
     * @param status The status of the book (e.g., available, checked out).
     * @param author The author of the book.
     * @param image The image of the book (e.g., cover image).
     * @param title The title of the book.
     * @param isbn The ISBN number of the book.
     * @param publisher The publisher of the book.
     * @param publishedDate The publication date of the book.
     * @param description A short description of the book.
     * @param rate The rating of the book.
     * @param image_id The unique identifier for the image.
     * @param image_path The path to the image file.
     * @param author_id The unique identifier for the author.
     * @param first_name The first name of the author.
     * @param last_name The last name of the author.
     * @param genre_id The unique identifier for the genre.
     * @param type The genre type of the book.
     */
    public BookAuthorImage(int bookid, int quantity, String status, author author, images image, String title,
                           String isbn, String publisher, String publishedDate, String description, int rate,
                           int image_id, String image_path, int author_id, String first_name, String last_name,
                           int genre_id, String type) {
        this.bookid = bookid;
        this.quantity = quantity;
        this.status = status;
        this.author = author;
        this.image = image;
        this.title = title;
        this.isbn = isbn;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.description = description;
        this.rate = rate;
        this.image_id = image_id;
        this.image_path = image_path;
        this.author_id = author_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.genre_id = genre_id;  // Initialize genre_id
        this.type = type;
    }

    // Getters and setters for all the fields
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

    public images getImage() {
        return image;
    }

    public void setImage(images image) {
        this.image = image;
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

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setGenre_id (int genre_id) {
        this.genre_id = genre_id;
    }

    public int getGenre_id() {
        return genre_id;
    }

    /**
     * Returns a string representation of the book with key details, including its title, author,
     * publication date, and genre. Also includes the description and status of the book.
     *
     * @return A formatted string with the book's details.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        // Basic details about the book
        result.append("Title: ").append(title)
                .append("\nAuthor: ").append(first_name).append(" ").append(last_name)
                .append("\nPublished year: ").append(publishedDate)
                .append("\nPublisher: ").append(publisher)
                .append("\nStatus: ").append(status)
                .append("\nISBN: ").append(isbn);

        // Genre details
        result.append("\nGenre: ").append(type);

        // Append description of the book
        result.append("\n\nDescription: ").append(getDescription());

        return result.toString();
    }

}
