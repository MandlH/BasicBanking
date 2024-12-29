package org.mandl;

public class UserDto {
    private String firstName;
    private String lastName;
    private String password;

    public UserDto(String firstName, String lastName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public String getUserName() {
        return firstName + "." + lastName;
    }

    public String getPassword() {
        return password;
    }
}
