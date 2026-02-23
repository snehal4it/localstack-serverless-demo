package com.example.service;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.example.model.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderDetailsParserService {

    private final ObjectMapper objectMapper;

    public Order parseOrder(SQSEvent sqsEvent) throws JsonProcessingException {
        // assumption: only one order per sqs message
        SQSEvent.SQSMessage message = sqsEvent.getRecords().stream().findAny().orElseThrow();
        Order order = objectMapper.readValue(message.getBody(), Order.class);
        String now = Instant.now().toString();
        order.setCreated(now);
        order.setUpdated(now);
        return order;
    }
}
