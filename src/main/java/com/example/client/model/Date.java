package com.example.client.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.time.LocalDate;

public class Date {
    private int id;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private  LocalDate date;
    private  int sold;

    public Date(){

    }
    public Date(LocalDate date, int sold) {
        this.date = date;
        this.sold = sold;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    @Override
    public String toString() {
        return "Date{" +
                "id=" + id +
                ", date=" + date +
                ", sold=" + sold +
                '}';
    }
}
