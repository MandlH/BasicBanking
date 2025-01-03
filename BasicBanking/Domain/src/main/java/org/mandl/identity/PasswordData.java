package org.mandl.identity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class PasswordData {
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String salt;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
