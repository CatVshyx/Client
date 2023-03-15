package com.example.client.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class User {
    private int id;
    private String name;
    private String email;
    private String role;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dateOfCreation;

    private Allows[] allows;
    private String path;
    private String phoneNumber = "undefined";
    private int company;
    private boolean verified;
    public User(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDate getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(LocalDate dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public Allows[] getAllows() {
        return allows;
    }

    public void setAllows(Allows[] allows) {
        this.allows = allows;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phone) {
        this.phoneNumber = phone;
    }

    public int getCompany() {
        return company;
    }

    public void setCompany(int company) {
        this.company = company;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getUserInfo(){
        return "%s\n%s".formatted(name,dateOfCreation.format(DateTimeFormatter.ofPattern("MMM d uuuu")) );
    }
    public String getUserContacts(){
        return "%s\n%s\n%s".formatted("Contacts",email,phoneNumber);
    }
    public String getUserLevel(){
        return "%s\n%s".formatted(role, role.equals("Administrator") ? "Access: 1" : "Access: 2");
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", dateOfCreation=" + dateOfCreation +
                ", allows=" + Arrays.toString(allows) +
                ", path='" + path + '\'' +
                ", phone='" + phoneNumber + '\'' +
                ", company=" + company +
                ", verified=" + verified +
                '}';
    }
}
