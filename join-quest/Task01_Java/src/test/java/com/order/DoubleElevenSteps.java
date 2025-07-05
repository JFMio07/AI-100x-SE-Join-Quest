package com.order;

import io.cucumber.java.en.Given;

public class DoubleElevenSteps {
    
    @Given("the Double Eleven promotion is active")
    public void the_double_eleven_promotion_is_active() {
        // 為了避免測試間的狀態污染，每次都創建新的 OrderService 實例
        OrderService orderService = new OrderService();
        orderService.configureDoubleElevenPromotion();
        OrderSteps.setOrderService(orderService);
    }
} 