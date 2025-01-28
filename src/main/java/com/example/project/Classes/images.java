package com.example.project.Classes;

/**
 * Represents an image associated with a book in the library.
 * This class holds information about the image's unique ID and the image's path.
 */
public class images {
    private int image_id;
    private String image_path;

    /**
     * Constructor to initialize the image with the given ID and image path.
     *
     * @param image_id The unique identifier for the image.
     * @param image_path The path or URL where the image is located.
     */
    public images(int image_id, String image_path) {
        this.image_id = image_id;
        this.image_path = image_path;
    }

    /**
     * Default constructor to initialize the image with default values.
     * Sets image_id to 0 and image_path to an empty string.
     */
    public images() {
        this(0, "");
    }

    /**
     * Returns the unique identifier for the image.
     *
     * @return The image ID.
     */
    public int getImage_id() {
        return image_id;
    }

    /**
     * Sets the unique identifier for the image.
     *
     * @param image_id The image ID to set.
     */
    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    /**
     * Returns the file path or URL of the image.
     *
     * @return The image path.
     */
    public String getImage_path() {
        return image_path;
    }

    /**
     * Sets the file path or URL for the image.
     *
     * @param image_path The image path to set.
     */
    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
}
