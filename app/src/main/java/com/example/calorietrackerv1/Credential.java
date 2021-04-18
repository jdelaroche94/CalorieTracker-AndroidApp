package com.example.calorietrackerv1;

import java.sql.Date;

/**
 * This class is responsible to create and manage a Credential Object, which is similar than
 * the object created in the server.
 */
public class Credential {
    private Long credentialId;
    private String username;
    private User userId;
    private String password;
    private Date signupDate;


    public Credential(Long credentialId, String username, User userId, String password, Date signupDate) {
        this.credentialId = credentialId;
        this.username = username;
        this.userId = userId;
        this.password = password;
        this.signupDate = signupDate;
    }

    public Long getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(Long credentialId) {
        this.credentialId = credentialId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getSignupDate() {
        return signupDate;
    }

    public void setSignupDate(Date signupDate) {
        this.signupDate = signupDate;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }
}
