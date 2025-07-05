package com.order;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderSteps {
    
    private static OrderService orderService;
    private Order order;
    
    public static void setOrderService(OrderService service) {
        orderService = service;
    }
    
    public static OrderService getOrderService() {
        return orderService;
    }
    
    @Given("no promotions are applied")
    public void no_promotions_are_applied() {
        orderService = new OrderService();
    }
    
    @Given("the threshold discount promotion is configured:")
    public void the_threshold_discount_promotion_is_configured(DataTable dataTable) {
        if (orderService == null) {
            orderService = new OrderService();
        }
        List<Map<String, String>> rows = dataTable.asMaps();
        Map<String, String> config = rows.get(0);
        int threshold = Integer.parseInt(config.get("threshold"));
        int discount = Integer.parseInt(config.get("discount"));
        orderService.configureThresholdDiscount(threshold, discount);
    }
    
    @Given("the buy one get one promotion for cosmetics is active")
    public void the_buy_one_get_one_promotion_for_cosmetics_is_active() {
        if (orderService == null) {
            orderService = new OrderService();
        }
        orderService.configureBuyOneGetOneForCosmetics();
    }
    
    @When("a customer places an order with:")
    public void a_customer_places_an_order_with(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps();
        order = orderService.createOrder();
        
        for (Map<String, String> row : rows) {
            String productName = row.get("productName");
            int quantity = Integer.parseInt(row.get("quantity"));
            int unitPrice = Integer.parseInt(row.get("unitPrice"));
            String category = row.get("category");
            
            Product product = new Product(productName, unitPrice, category);
            OrderItem item = new OrderItem(product, quantity);
            order.addItem(item);
        }
        
        orderService.calculateOrder(order);
    }
    
    @Then("the order summary should be:")
    public void the_order_summary_should_be(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps();
        Map<String, String> expected = rows.get(0);
        
        if (expected.containsKey("totalAmount")) {
            int expectedTotal = Integer.parseInt(expected.get("totalAmount"));
            assertThat(order.getTotalAmount()).isEqualTo(expectedTotal);
        }
        
        if (expected.containsKey("originalAmount")) {
            int expectedOriginal = Integer.parseInt(expected.get("originalAmount"));
            assertThat(order.getOriginalAmount()).isEqualTo(expectedOriginal);
        }
        
        if (expected.containsKey("discount")) {
            int expectedDiscount = Integer.parseInt(expected.get("discount"));
            int actualDiscount = order.getThresholdDiscount() > 0 ? order.getThresholdDiscount() : order.getDiscount();
            assertThat(actualDiscount).isEqualTo(expectedDiscount);
        }
        
        if (expected.containsKey("doubleElevenDiscount")) {
            int expectedDoubleElevenDiscount = Integer.parseInt(expected.get("doubleElevenDiscount"));
            assertThat(order.getDoubleElevenDiscount()).isEqualTo(expectedDoubleElevenDiscount);
        }
        
        if (expected.containsKey("subtotalAfterDoubleEleven")) {
            int expectedSubtotal = Integer.parseInt(expected.get("subtotalAfterDoubleEleven"));
            assertThat(order.getSubtotalAfterDoubleEleven()).isEqualTo(expectedSubtotal);
        }
        
        if (expected.containsKey("thresholdDiscount")) {
            int expectedThresholdDiscount = Integer.parseInt(expected.get("thresholdDiscount"));
            assertThat(order.getThresholdDiscount()).isEqualTo(expectedThresholdDiscount);
        }
    }
    
    @Then("the customer should receive:")
    public void the_customer_should_receive(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps();
        
        for (Map<String, String> row : rows) {
            String productName = row.get("productName");
            int expectedQuantity = Integer.parseInt(row.get("quantity"));
            
            int actualQuantity = order.getReceivedQuantity(productName);
            assertThat(actualQuantity).isEqualTo(expectedQuantity);
        }
    }
} 