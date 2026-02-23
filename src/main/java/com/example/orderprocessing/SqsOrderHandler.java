package com.example.orderprocessing;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.example.model.Order;
import com.example.service.OrderDetailsParserService;
import com.example.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Log4j2
@Component("sqs-order-handler-function")
public class SqsOrderHandler implements Function<SQSEvent, Void> {

    private final OrderDetailsParserService orderDetailsParserService;

    private final OrderService orderService;

    @Override
    public Void apply(SQSEvent sqsEvent) {
        log.info("Received SQSEvent: {}", sqsEvent);

        try {
            Order order = orderDetailsParserService.parseOrder(sqsEvent);
            orderService.createOrder(order);
            log.info("Order stored successfully");
        } catch (JsonProcessingException e) {
            log.error("Error while parsing order json", e);
            throw new RuntimeException(e);
        }

        // Successful processing
        return null;
    }
}
