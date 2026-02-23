package com.example.service;

import com.example.model.Order;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Log4j2
@Service
public class OrderService {

    private final DynamoDbTable<Order> orderTable;

    public OrderService(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.orderTable = dynamoDbEnhancedClient.table("order", TableSchema.fromBean(Order.class));
    }

    public void createOrder(Order order) {
        log.info("Creating order {}", order);
        orderTable.putItem(order);
        log.info("Order created successfully");
    }
}
