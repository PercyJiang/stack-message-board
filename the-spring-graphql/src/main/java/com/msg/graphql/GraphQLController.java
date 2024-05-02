package com.msg.graphql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class GraphQLController {

  private final MessageService messageService;

  @Autowired
  public GraphQLController(MessageService messageService) {
    this.messageService = messageService;
  }

  @QueryMapping
  public List<Message> graphGetAll() {
    return messageService.getAll();
  }

  @QueryMapping
  public Book bookById(@Argument("id") String id) {
    return Book.getById(id);
  }
}
