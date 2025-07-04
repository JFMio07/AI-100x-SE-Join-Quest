package com.order;

public class OrderItem {
    private Product product;
    private int quantity;
    private int receivedQuantity;
    
    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.receivedQuantity = quantity; // 預設收到的數量等於訂購數量
    }
    
    public Product getProduct() {
        return product;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public int getReceivedQuantity() {
        return receivedQuantity;
    }
    
    public void setReceivedQuantity(int receivedQuantity) {
        this.receivedQuantity = receivedQuantity;
    }
    
    public int getSubtotal() {
        return product.getUnitPrice() * quantity;
    }
} 