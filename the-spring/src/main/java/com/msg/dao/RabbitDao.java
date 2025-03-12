package com.msg.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

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
      Object content;
      if (message instanceof String) {
        contentType = "text/plain";
        content = message.toString();
      } else if (message instanceof MultipartFile) {
        contentType = ((MultipartFile) message).getContentType();
        content = ((MultipartFile) message).getBytes();
      } else {
        contentType = "application/json";
        content = message;
      }

      Map<String, Object> body =
          new HashMap<>(
              Map.of(
                  "username",
                  username,
                  "contentType",
                  Objects.requireNonNull(contentType),
                  "content",
                  content));

      String json = new ObjectMapper().writeValueAsString(body);
      channel.basicPublish("", QUEUE_NAME, props, json.getBytes());
      System.out.println("percy: rabbit message published");

    } catch (Exception e) {
      System.out.println("percy: e: " + e.getMessage());
    }
  }
}
