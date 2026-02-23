package com.example.orderprocessing;

import java.util.function.Function;
import java.util.logging.Logger;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component("sqs-order-handler-function")
public class SqsOrderHandler implements Function<SQSEvent, Void> {

    @Override
    public Void apply(SQSEvent sqsEvent) {
        log.info("Received SQSEvent: {}", sqsEvent);
//        for (SQSMessage message : sqsEvent.getRecords()) {
//            logger.info("Processing message ID: " + message.getMessageId());
//            logger.info("Message body: " + message.getBody());
//            // In a real application, you would parse the message body and process the order
//            // Example: Order order = new ObjectMapper().readValue(message.getBody(), Order.class);
//            // processOrder(order);
//        }
        return null; // Successful processing
    }
}
