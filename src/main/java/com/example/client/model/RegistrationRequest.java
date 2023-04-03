package com.example.client.model;

public class RegistrationRequest {
    private final String email;
    private final String username;
    private final String password;
    private final String number;

    public RegistrationRequest(String username, String email, String password, String phone) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.number = phone;
    }
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return number;
    }
}
