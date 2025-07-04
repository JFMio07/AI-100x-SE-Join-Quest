package com.order;

public class OrderService {
    
    private int thresholdAmount;
    private int thresholdDiscount;
    private boolean buyOneGetOneCosmeticsActive;
    
    public Order createOrder() {
        return new Order();
    }
    
    public void configureThresholdDiscount(int threshold, int discount) {
        this.thresholdAmount = threshold;
        this.thresholdDiscount = discount;
    }
    
    public void configureBuyOneGetOneForCosmetics() {
        this.buyOneGetOneCosmeticsActive = true;
    }
    
    public void calculateOrder(Order order) {
        int originalAmount = 0;
        
        // 計算原始金額
        for (OrderItem item : order.getItems()) {
            originalAmount += item.getSubtotal();
        }
        
        order.setOriginalAmount(originalAmount);
        
        // 應用 buy-one-get-one 促銷
        if (buyOneGetOneCosmeticsActive) {
            applyBuyOneGetOneForCosmetics(order);
        }
        
        // 計算折扣
        int discount = 0;
        if (originalAmount >= thresholdAmount) {
            discount = thresholdDiscount;
        }
        
        order.setDiscount(discount);
        order.setTotalAmount(originalAmount - discount);
    }
    
    private void applyBuyOneGetOneForCosmetics(Order order) {
        for (OrderItem item : order.getItems()) {
            if ("cosmetics".equals(item.getProduct().getCategory())) {
                // Buy-one-get-one: 每個化妝品項目都額外贈送 1 個
                int originalQuantity = item.getQuantity();
                int bonusQuantity = 1; // 每個項目固定送 1 個
                
                item.setReceivedQuantity(originalQuantity + bonusQuantity);
            }
        }
    }
} 