package com.msg.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class RabbitDao {

  private static final String QUEUE_NAME = "hello";

  public void publish(String username, Object message) {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");

    try (Connection connection = factory.newConnection();
        Channel channel = connection.createChannel()) {
      channel.queueDeclare(QUEUE_NAME, false, false, false, null);
      BasicProperties props = new BasicProperties.Builder().contentType("application/json").build();

      String contentType;
      byte[] contentBytes;
      if (message instanceof String) {
        contentBytes = message.toString().getBytes();
        contentType = "text/plain";
      } else if (message instanceof MultipartFile) {
        contentBytes = ((MultipartFile) message).getBytes();
        contentType = ((MultipartFile) message).getContentType();
      } else {
        contentBytes =
            new ObjectMapper().writeValueAsString(message).getBytes(StandardCharsets.UTF_8);
        contentType = "application/json";
      }

      Map<String, Object> body =
          new HashMap<>(
              Map.of(
                  "username",
                  username,
                  "contentBytes",
                  contentBytes,
                  "contentType",
                  Objects.requireNonNull(contentType)));
      byte[] bodyBytes = convertMapToBytes(body);

      channel.basicPublish("", QUEUE_NAME, props, bodyBytes);
      System.out.println("percy: rabbit message published");

    } catch (Exception e) {
      System.out.println("percy: e: " + e.getMessage());
    }
  }

  private static byte[] convertMapToBytes(Map<String, Object> data) {
    // Simple serialization - consider using a library like Jackson for more complex objects
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<String, Object> entry : data.entrySet()) {
      sb.append(entry.getKey()).append(":").append(entry.getValue().toString()).append(",");
    }
    if (!sb.isEmpty()) {
      sb.deleteCharAt(sb.length() - 1); // Remove the trailing comma
    }
    return sb.toString().getBytes();
  }
}
