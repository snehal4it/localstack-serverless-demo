package com.example.model;

import lombok.Data;
import lombok.Getter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
@Data
public class Order {
    // This tells Lombok: "Put the @DynamoDbPartitionKey on the generated getId() method"
    @Getter(onMethod_ = {@DynamoDbPartitionKey})
    private String id;

    private String status;
    private String created;
    private String updated;
}
