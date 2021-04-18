package com.example.calorietrackerv1;


import java.sql.Date;

/**
 * This class is responsible to create and manage a User Object, which is similar than
 * the object created in the server.
 */
public class User {
    private Long userId;
    private String givenName;
    private String surname;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    private String email;
    private Date dateOfBirth;
    private short height; //cms
    private short weight; //kgs
    private String gender; //M or F
    private String address;
    private Short postcode;
    private short levelAct; //1..5
    private int stepsMile;

    public User() {
    }

    public User(Long userId, String givenName, String surname, String email, Date dateOfBirth, short height, short weight, String gender,String address,short postcode, short levelAct, int stepsMile) {
        this.userId = userId;
        this.givenName = givenName;
        this.surname = surname;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.address = address;
        this.postcode = postcode;
        this.levelAct = levelAct;
        this.stepsMile = stepsMile;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public short getHeight() {
        return height;
    }

    public void setHeight(short height) {
        this.height = height;
    }

    public short getWeight() {
        return weight;
    }

    public void setWeight(short weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Short getPostcode() {
        return postcode;
    }

    public void setPostcode(Short postcode) {
        this.postcode = postcode;
    }

    public short getLevelAct() {
        return levelAct;
    }

    public void setLevelAct(short levelAct) {
        this.levelAct = levelAct;
    }

    public int getStepsMile() {
        return stepsMile;
    }

    public void setStepsMile(int stepsMile) {
        this.stepsMile = stepsMile;
    }

}
