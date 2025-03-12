package com.msg.api;

import com.msg.dao.MessageRepository;
import com.msg.dto.Message;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@SuppressWarnings("unused")
@Controller
public class MessageGraphQL {

  private final MessageRepository messageRepository;

  @Autowired
  public MessageGraphQL(MessageRepository messageRepository) {
    this.messageRepository = messageRepository;
  }

  @QueryMapping
  public List<Message> graphGetAll() {
    System.out.println("percy: graphGetAll");
    return messageRepository.getAll();
  }

  @MutationMapping
  public Integer graphPost(@Argument("source") String source, @Argument("content") String content) {
    System.out.println("percy: graphPost");
    return messageRepository.create(source, content);
  }
}
