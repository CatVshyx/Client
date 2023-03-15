package com.example.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.time.LocalDate;

public class Discount {

    private float discountProperty = 0;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate startDate;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate endDate;
    public Discount(){

    }
    public Discount(float discountProperty, LocalDate startDate, LocalDate endDate) {
        this.discountProperty = discountProperty;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    public Discount setDiscountProperty(float discount){
        this.discountProperty = discount;
        return this;
    }
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public float getDiscountProperty() {
        return discountProperty;
    }

    @Override
    public String toString() {
        return "Discount{" +
                "discountProperty=" + discountProperty +
                ", period=" +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
