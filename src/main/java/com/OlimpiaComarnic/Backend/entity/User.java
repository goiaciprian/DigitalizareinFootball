package com.OlimpiaComarnic.Backend.entity;


import org.mindrot.jbcrypt.BCrypt;


/**
 * Clasa user contine:
 *    - username
 *    - parola encryptata SHA-256
 *    - daca este admin;
 */
public class User {
    private String Username, encPassword;
    private boolean isAdmin;

    public User(String username, String password, boolean isAdmin) {
        this.Username = username;
        this.encPassword = hashPass(password);
        this.isAdmin = isAdmin;

    }

    public User() {}

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public String getEncPassword() {
        return encPassword;
    }

    public void setEncPassword(String encPassword) {
        this.encPassword = encPassword;
    }

    public void newPassword(String password) {
        this.encPassword = hashPass(password);
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean checkPassword(String password) {
        return BCrypt.checkpw(password, encPassword);
    }

    @Override
    public String toString() {
        return "User{" +
                "Username='" + Username + '\'' +
                ", encPassword='" + encPassword + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }

    private String hashPass(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
