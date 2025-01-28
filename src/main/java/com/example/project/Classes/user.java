package com.example.project.Classes;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Represents a user in the system, with information such as personal details,
 * login credentials, and role within the application.
 */
public class user {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
    private String address;
    private String phoneNumber;
    private Timestamp registration_date;

    /**
     * Constructor to initialize a new user with all details.
     *
     * @param username The unique username of the user.
     * @param firstName The user's first name.
     * @param lastName The user's last name.
     * @param email The user's email address.
     * @param password The user's password.
     * @param role The role of the user (e.g., admin, member).
     * @param address The user's home address.
     * @param phoneNumber The user's phone number.
     */
    public user(String username, String firstName, String lastName, String email, String password, String role, String address, String phoneNumber) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.registration_date = new Timestamp(new Date().getTime()); // Set to current date and time
    }

    /**
     * Constructor to initialize a new user with essential details.
     *
     * @param username The unique username of the user.
     * @param firstName The user's first name.
     * @param lastName The user's last name.
     * @param email The user's email address.
     * @param password The user's password.
     * @param role The role of the user (e.g., admin, member).
     */
    public user(String username, String firstName, String lastName, String email, String password, String role) {
        this(username, firstName, lastName, email, password, role, "", "");
        this.registration_date = new Timestamp(new Date().getTime()); // Set to current date and time
    }

    /**
     * Default constructor to initialize an empty user.
     */
    public user(){
        this("","","","","","","","");
    }

    // Getters and Setters

    /**
     * Gets the username of the user.
     *
     * @return The username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username The username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the first name of the user.
     *
     * @return The first name of the user.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstName The first name to set.
     */
    public void setFirstname(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the user.
     *
     * @return The last name of the user.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastName The last name to set.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the email of the user.
     *
     * @return The email of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the user.
     *
     * @param email The email to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the password of the user.
     *
     * @return The password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the role of the user.
     *
     * @return The role of the user.
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role of the user.
     *
     * @param role The role to set.
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Gets the address of the user.
     *
     * @return The address of the user.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the user.
     *
     * @param address The address to set.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the phone number of the user.
     *
     * @return The phone number of the user.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number of the user.
     *
     * @param phoneNumber The phone number to set.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the registration date of the user.
     *
     * @return The registration date as a string.
     */
    public String getRegistration_date() {
        return registration_date.toString();
    }

    /**
     * Sets the registration date of the user.
     *
     * @param registration_date The registration date to set.
     */
    public void setRegistrationDate(Timestamp registration_date) {
        this.registration_date = registration_date;
    }
}
