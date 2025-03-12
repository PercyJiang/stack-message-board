package com.msg.dao;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.stereotype.Component;

@Component
public class RabbitDao {

  private static final String QUEUE_NAME = "hello";

  public void publishString(String str) {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");

    try (Connection connection = factory.newConnection();
        Channel channel = connection.createChannel()) {
      channel.queueDeclare(QUEUE_NAME, false, false, false, null);
      channel.basicPublish("", QUEUE_NAME, null, str.getBytes());
      System.out.println("percy: [x] Sent '" + str + "'");
    } catch (Exception e) {
      System.out.println("percy: e: " + e.getMessage());
    }
  }
}
