package com.example.client.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;
import java.util.Set;

public class Company {

    private int id;
    private String name;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dateOfCreation;
    private String inviteLink;
    private Set<Date> revenues;
    private Set<Product> products;
    private Set<User> users;
    public Company(){}

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

    public LocalDate getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(LocalDate dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public String getInviteLink() {
        return inviteLink;
    }

    public void setInviteLink(String inviteLink) {
        this.inviteLink = inviteLink;
    }

    public Set<Date> getRevenues() {
        return revenues;
    }

    public void setRevenues(Set<Date> revenues) {
        this.revenues = revenues;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dateOfCreation=" + dateOfCreation +
                ", inviteLink='" + inviteLink + '\'' +
                ", revenues=" + revenues +
                ", products=" + products +
                ", users=" + users +
                '}';
    }
}
