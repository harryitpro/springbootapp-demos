// Order.java
// This is a simple POJO to represent your order data.
// It implements Serializable for general good practice in Java messaging,
// and has a no-arg constructor and getters/setters for Jackson (JSON) serialization/deserialization.

package com.myapp.jms.model;

import java.io.Serializable;
import java.util.Objects; // Added for Objects.hash and Objects.equals in hashCode/equals

public class Order implements Serializable {
    private String orderId;
    private String item;
    private int quantity;
    private double price;

    // Default constructor is required by Jackson for deserialization
    public Order() {
    }

    public Order(String orderId, String item, int quantity, double price) {
        this.orderId = orderId;
        this.item = item;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters and Setters (required by Jackson for serialization/deserialization)
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", item='" + item + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }

    // Good practice to override equals and hashCode for data classes/POJOs
    // especially if they are used in collections or maps.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return quantity == order.quantity &&
                Double.compare(order.price, price) == 0 &&
                Objects.equals(orderId, order.orderId) &&
                Objects.equals(item, order.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, item, quantity, price);
    }
}
