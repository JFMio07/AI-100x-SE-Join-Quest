package com.order;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private List<OrderItem> items;
    private int originalAmount;
    private int discount;
    private int totalAmount;
    
    public Order() {
        this.items = new ArrayList<>();
        this.originalAmount = 0;
        this.discount = 0;
        this.totalAmount = 0;
    }
    
    public void addItem(OrderItem item) {
        items.add(item);
    }
    
    public List<OrderItem> getItems() {
        return items;
    }
    
    public int getOriginalAmount() {
        return originalAmount;
    }
    
    public void setOriginalAmount(int originalAmount) {
        this.originalAmount = originalAmount;
    }
    
    public int getDiscount() {
        return discount;
    }
    
    public void setDiscount(int discount) {
        this.discount = discount;
    }
    
    public int getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public int getReceivedQuantity(String productName) {
        return items.stream()
            .filter(item -> item.getProduct().getName().equals(productName))
            .mapToInt(OrderItem::getReceivedQuantity)
            .sum();
    }
} 