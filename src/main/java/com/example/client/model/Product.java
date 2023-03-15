package com.example.client.model;

import com.example.client.additional.Category;
public class Product {
    private int id;
    private String productName;
    private float price;
    private int amount;
    private String supplier = "undefined";
    public final static String currency = "UAH";
    // sets from file
    private Category category;
    private Discount discount;
    public Product(){}

    public Product(String productName, float price, int amount, Category category) {
        this.productName = productName;
        this.price = price;
        this.amount = amount;
        this.category = category;
    }
    public Product(Product productTest){
        this.id = productTest.id;
        this.productName = productTest.productName;
        this.price = productTest.price;
        this.amount = productTest.amount;
        this.category = productTest.category;
        this.supplier = productTest.supplier;
    }



    public String getName() {
        return productName;
    }

    public void setName(String productName) {
        this.productName = productName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getSupplier() {

        return supplier == null ? "" : supplier;
    }

    public Product setSupplier(String supplier) {
        this.supplier = supplier;
        return this;
    }

    public Category getCategory() {
        return category;
    }

    public Product setCategory(Category category) {
        this.category = category;
        return this;
    }
    public long getId(){
        return this.id;
    }

    public String getProductName() {
        return productName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public String getFullPrice() {
        return "%s\n %s".formatted(price,currency);
    }
    public String getFullAmount(){
        return "%s\n%s".formatted(amount,category.equals(Category.WATER) || category.equals(Category.DAIRY) ? " lt" : " kg" );
    }

    public String getPriceDiscounted(){
        if (this.discount == null) return null;
        double after = Math.round( this.getPrice() * this.getDiscount().getDiscountProperty() * 100d)/100d;
        return "%s %s\n%s %s ".formatted(after,Product.currency,this.getPrice(), Product.currency);
    }
    public String getFullDiscount(){
        if (this.discount == null) return null;
        return "%d%s".formatted(100 - (int)(this.getDiscount().getDiscountProperty()*100) , "%");
    }

    public String getFullPeriod(){
        if (this.discount == null) return null;
        return  this.discount.getStartDate() + "\n"+this.discount.getEndDate();
    }
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", amount=" + amount +
                ", supplier='" + supplier + '\'' +
                ", category=" + category +
                ", discount=" + discount +
                '}';
    }
}
