package com.example.orderprocessing;

import static java.util.logging.Level.INFO;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import lombok.extern.java.Log;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.SqsClientBuilder;

/**
 * This is not a spring integration test, this is just test client running in the form of unit test. it assumes that
 * either local or aws environment is already provisioned Without localstack such test would require real aws
 * environment, but now with localstack this can be executed on local
 */
@Log
@Tag("OrderE2ETest")
public class OrderProcessingE2E {

    private static final URI LOCAL_ENDPOINT = URI.create("http://localhost:4566");

    private String queueUrl;
    private String orderTableName;
    private boolean isLocal;

    private SqsClient sqsClient;
    private DynamoDbClient dynamoDbClient;

    // set up conditionally override endpoints for localstack on local
    @BeforeEach
    public void setup() {
        this.queueUrl = System.getProperty("sqs.url");
        log.log(INFO, "Using sqs url={0}", this.queueUrl);

        this.orderTableName = System.getProperty("order.table.name");
        log.log(INFO, "Using order table name={0}", this.orderTableName);

        this.isLocal = Boolean.parseBoolean(System.getProperty("is.local"));
        log.log(INFO, "Using isLocal={0}", this.isLocal);

        createSqsClient();
        createDynamoDbClient();
    }

    private void createSqsClient() {
        SqsClientBuilder sqsClientBuilder = SqsClient.builder();
        if (this.isLocal) {
            sqsClientBuilder.endpointOverride(LOCAL_ENDPOINT)
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("test", "test")));
        }
        this.sqsClient = sqsClientBuilder.build();
    }

    private void createDynamoDbClient() {
        DynamoDbClientBuilder dynamoDbClientBuilder = DynamoDbClient.builder();
        if (this.isLocal) {
            dynamoDbClientBuilder.endpointOverride(LOCAL_ENDPOINT)
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("test", "test")));
        }
        this.dynamoDbClient = dynamoDbClientBuilder.build();
    }

    @Test
    public void testOrderIsReceivedAndStoredInOrderTable() {
        String orderId = "test-e2e-msg-" + UUID.randomUUID();
        String message = """
            {
                "id": "%s",
                "status": "NEW"
            }
            """.formatted(orderId);
        // send order details in queue
        sqsClient.sendMessage(b -> b.queueUrl(queueUrl).messageBody(message));
        log.log(INFO, "Message sent to order queue={0}", message);

        Awaitility.await().atMost(Duration.ofSeconds(20)).untilAsserted(() -> {
            GetItemResponse response = dynamoDbClient.getItem(
                b -> b.tableName(orderTableName).key(Map.of("id", AttributeValue.builder().s(orderId).build())));
            assertThat(response.hasItem()).isTrue();
            assertThat(response.item().get("status").s()).isEqualTo("NEW");
        });
    }
}
