package com.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderService {
    
    // Double Eleven 促銷常數
    private static final int DOUBLE_ELEVEN_BULK_SIZE = 10;
    private static final int DOUBLE_ELEVEN_DISCOUNT_PERCENTAGE = 20;
    
    private int thresholdAmount;
    private int thresholdDiscount;
    private boolean buyOneGetOneCosmeticsActive;
    private boolean doubleElevenPromotionActive;
    
    public Order createOrder() {
        return new Order();
    }
    
    public void configureDoubleElevenPromotion() {
        this.doubleElevenPromotionActive = true;
    }
    
    public void configureThresholdDiscount(int threshold, int discount) {
        this.thresholdAmount = threshold;
        this.thresholdDiscount = discount;
    }
    
    public void configureBuyOneGetOneForCosmetics() {
        this.buyOneGetOneCosmeticsActive = true;
    }
    
    public void calculateOrder(Order order) {
        calculateOriginalAmount(order);
        
        // 應用促銷折扣
        applyPromotions(order);
        
        // 計算最終金額
        calculateFinalAmount(order);
    }
    
    private void calculateOriginalAmount(Order order) {
        int originalAmount = 0;
        for (OrderItem item : order.getItems()) {
            originalAmount += item.getSubtotal();
        }
        order.setOriginalAmount(originalAmount);
    }
    
    private void applyPromotions(Order order) {
        // 應用 Double Eleven 促銷
        if (doubleElevenPromotionActive) {
            applyDoubleElevenDiscount(order);
        }
        
        // 應用 buy-one-get-one 促銷
        if (buyOneGetOneCosmeticsActive) {
            applyBuyOneGetOneForCosmetics(order);
        }
    }
    
    private void calculateFinalAmount(Order order) {
        int currentAmount = order.getSubtotalAfterDoubleEleven() > 0 ? 
            order.getSubtotalAfterDoubleEleven() : order.getOriginalAmount();
        
        int thresholdDiscountAmount = calculateThresholdDiscount(currentAmount);
        
        order.setThresholdDiscount(thresholdDiscountAmount);
        order.setTotalAmount(currentAmount - thresholdDiscountAmount);
    }
    
    private int calculateThresholdDiscount(int amount) {
        // 只有在明確配置了 threshold discount 且金額達到門檻時才應用折扣
        return (thresholdAmount > 0 && amount >= thresholdAmount) ? thresholdDiscount : 0;
    }
    
    private void applyDoubleElevenDiscount(Order order) {
        Map<String, List<OrderItem>> itemsByProduct = groupItemsByProduct(order.getItems());
        int totalDiscount = calculateDoubleElevenDiscount(itemsByProduct);
        
        order.setDoubleElevenDiscount(totalDiscount);
        order.setSubtotalAfterDoubleEleven(order.getOriginalAmount() - totalDiscount);
    }
    
    private Map<String, List<OrderItem>> groupItemsByProduct(List<OrderItem> items) {
        Map<String, List<OrderItem>> itemsByProduct = new HashMap<>();
        for (OrderItem item : items) {
            String productName = item.getProduct().getName();
            itemsByProduct.computeIfAbsent(productName, k -> new ArrayList<>()).add(item);
        }
        return itemsByProduct;
    }
    
    private int calculateDoubleElevenDiscount(Map<String, List<OrderItem>> itemsByProduct) {
        int totalDiscount = 0;
        
        for (Map.Entry<String, List<OrderItem>> entry : itemsByProduct.entrySet()) {
            List<OrderItem> items = entry.getValue();
            
            int totalQuantity = calculateTotalQuantity(items);
            int discountGroups = totalQuantity / DOUBLE_ELEVEN_BULK_SIZE;
            
            if (discountGroups > 0) {
                int unitPrice = items.get(0).getProduct().getUnitPrice();
                int discountAmount = calculateBulkDiscountAmount(discountGroups, unitPrice);
                totalDiscount += discountAmount;
            }
        }
        
        return totalDiscount;
    }
    
    private int calculateTotalQuantity(List<OrderItem> items) {
        return items.stream().mapToInt(OrderItem::getQuantity).sum();
    }
    
    private int calculateBulkDiscountAmount(int discountGroups, int unitPrice) {
        int discountQuantity = discountGroups * DOUBLE_ELEVEN_BULK_SIZE;
        return (discountQuantity * unitPrice * DOUBLE_ELEVEN_DISCOUNT_PERCENTAGE) / 100;
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