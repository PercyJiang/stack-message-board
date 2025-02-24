package com.msg.api;

import com.msg.MessageService;
import com.msg.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class MessageGraphQL {

  private final MessageService messageService;

  @Autowired
  public MessageGraphQL(MessageService messageService) {
    this.messageService = messageService;
  }

  @QueryMapping
  public List<Message> graphGetAll() {
    System.out.println("percy: graphGetAll");
    return messageService.getAll();
  }

  @MutationMapping
  public Integer graphPost(@Argument("source") String source, @Argument("content") String content) {
    System.out.println("percy: graphPost");
    return messageService.create(source, content);
  }
}
